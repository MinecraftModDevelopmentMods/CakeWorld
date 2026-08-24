# CakeWorld Developer Guide

## Architecture

CakeWorld is a child mod of OreSpawn. It registers content through normal Forge
registries and declares world-generation intent in the canonical provider
source:

```text
src/main/orespawn/provider.json
```

`generateCakeWorldOreSpawnProvider` copies the normal adventure and applies
`src/main/orespawn/basemetals-overlay.json` to create the conditional
compatibility template. The build packages the result at
`data/cakeworld/orespawn/provider.json`.

OreSpawn discovers the packaged result during mod loading, validates all
referenced registry IDs, merges provider-owned entries, and snapshots the
selected template into each new world's server configuration. Generation does
not call back into CakeWorld.

Only `zone.moddev.mc.orespawn.api` is a supported Java API. Other OreSpawn
packages are internal.

The development build resolves the published Minecraft 1.18.2 OreSpawn
4.0.6.118021 release through CurseMaven. `gradle.properties` pins CurseForge project `245586`
and file `8688546`, rather than reading a mutable sibling build directory. When
testing a newer OreSpawn release, change the pinned file ID deliberately and
repeat the compile, GameTest, fresh-world, save/reload, package, and client
gates before treating earlier evidence as transferable.

## Biomes

`CakeWorldBiomes` uses `OreSpawnBiomes.copyAndRegister` to copy stable vanilla
biome definitions and adjust their climate. Copying is a convenience for
registration; placement is controlled by `biome_palettes` in the provider.
CakeWorld also registers appropriate Forge `BiomeDictionary` types during
common setup. Those types are required when the geology profile uses
`biome_dictionary` rules; copying a biome does not copy its registry-key
dictionary membership.

Exact `biomes` weights and matching `biome_dictionary` weights are additive.
OreSpawn's built-in dictionary defaults may also contribute generic geology
such as mountain belts or coastal shelves, so a total-conversion provider
should give its exact biome identities enough weight to remain readable.

OreSpawn wraps the biome source that the dimension already uses. This means
CakeWorld does not own the base climate sampler and does not need TerraBlender.
If another installed mod already changed the biome source, OreSpawn evaluates
the resulting biome and applies CakeWorld's configured palette afterward.

Palette order matters. The CakeWorld template handles oceans first and excludes
the `cakeworld` namespace from its broader land pass, preserving soda oceans
while replacing the remaining source biomes.

## Terrain And Materials

The provider's top-level `rocks` are inert until an enabled
`terrain_dimensions` rule exists. CakeWorld's auto-template enables explicit
rules for the three vanilla dimensions and supplies biome palettes and material
overrides in the same self-contained profile.

Dimension materials can replace:

- the normal aquifer fluid;
- deep aquifer fluid below a configured Y level;
- placed snow;
- placed ice.

They do not dynamically replace every arbitrary water or lava block after world
generation. Features and structures that explicitly place vanilla fluids may
still need targeted compatibility later.

## Templates

`cakeworld:edible_world` is marked `auto_select` with priority 100. OreSpawn
uses deterministic priority and ID ordering if multiple providers offer
automatic templates. An explicit pack default wins, and an existing world's
saved profile is never silently rewritten.

`cakeworld:edible_world_basemetals` is generated from that same canonical
profile with the optional thirteen-resource compatibility overlay. The third
template, `cakeworld:sampler_platter`, is deliberately non-automatic and must
be selected explicitly. Provider revision 54 retains its first bounded
diagnostic plot—Candy Plains augmenting the delegated vanilla source with
`minecraft_only` scope, tiny regions, partial coverage, and a positive
fallback weight—and adds two namespace-filter plots. Five labelled Overworld
boundary plots then use Gingerbread Hearthlands, Peppermint Pinewoods,
Marshmallow Peaks, Cookie Forest and Soda Ocean to show `tiny`, `small`,
`average`, `large` and `huge` regions at exact 128, 256, 512, 1,024 and 2,048
block scales. Each plot has half coverage, so covered and delegated regions
remain visible together. The Nether uses an
explicit `minecraft` allow-list with `selected_namespaces`; its Fudge output
lists one installed and one missing optional similar biome, while a competing
Crags output requires a deliberately missing biome. The End declares a
Meringue output under `all` scope but excludes `minecraft`, so vanilla End
sources must delegate unchanged. Together they demonstrate include/exclude
lists and optional/required dependency behavior without callbacks or effects
on either adventure. A ninth selected-namespace plot targets only an opt-in
`cakeworld_fixture` source. The fixture registers a Plains-derived biome and
installs a fixed source at the final level-load boundary before OreSpawn wraps
it; normal CakeWorld never registers either. During provider generation the Sampler inherits the
adventure's rocks, geomes, biome rules, dictionary rules and terrain dimensions
exactly. It then selects the profile-wide legacy `sky_v1` algorithm with
deliberately extreme but schema-bounded custom values. This makes the
fixed-seed comparison meaningful while leaving both automatically selected
adventures on average `stable_layers` settings. The same explicit Sampler maps
its installed Burnt-Sugar geology output to Sprinkle Cluster at bake time and
enables exactly three flat bedrock layers in the Overworld and Nether. Its
bedrock retrogen switch remains off. Two additional ore rules are packaged
disabled beside a disabled profile-wide retrogen control. They are inert in an
ordinary Sampler and exist solely so a copied test save can activate one
retrogen-enabled Sprinkle rule beside one non-retrogen Fizzy-Pearl control.

Run its isolated four-test fresh-world proof, including the formation survey,
with:

```powershell
./gradlew runGameTestServer -PcakeworldSamplerRuntime=true -PcakeworldSamplerRunDirectory=run-sampler-platter-local '-PcakeworldGameTestNamespaces=cakeworld_sampler,cakeworld_formation' -PcakeworldExpectedFormationAlgorithm=sky_v1
```

The preparation task refuses to reuse a directory that already contains a
world-owned OreSpawn profile. To prove persistence against the same save, run
the same command again with `-PcakeworldSamplerReuseWorld=true`. This reuse
flag is intentionally opt-in so an ordinary diagnostic run cannot silently
turn into reload evidence.

The bounded retrogen proof uses a separate ignored directory and three explicit
phases. Do not substitute a player world:

```powershell
./gradlew runGameTestServer -PcakeworldSamplerRuntime=true -PcakeworldSamplerRunDirectory=run-sampler-retrogen-local -PcakeworldSamplerRetrogenPhase=baseline -PcakeworldGameTestNamespaces=cakeworld_sampler_retrogen
./gradlew runGameTestServer -PcakeworldSamplerRuntime=true -PcakeworldSamplerReuseWorld=true -PcakeworldSamplerRunDirectory=run-sampler-retrogen-local -PcakeworldSamplerRetrogenPhase=apply -PcakeworldGameTestNamespaces=cakeworld_sampler_retrogen
./gradlew runGameTestServer -PcakeworldSamplerRuntime=true -PcakeworldSamplerReuseWorld=true -PcakeworldSamplerRunDirectory=run-sampler-retrogen-local -PcakeworldSamplerRetrogenPhase=reload -PcakeworldGameTestNamespaces=cakeworld_sampler_retrogen
```

Baseline creates a 256-block Biscuit-Stone layer and a 256-block Brick control
layer in two already-generated chunks. Apply changes only the saved profile to
revision `5301`, `force: false`, and four chunks per tick. The released
OreSpawn queue may first process the ordinary spawn backlog, so the harness
allows 200 ticks before checking the deliberately distant chunks. The accepted
result is exactly 64 Sprinkle blocks and 192 remaining Biscuit blocks in the
eligible layer, zero Fizzy-Pearl control output, an unchanged 256-block Brick
layer, and intact sentinels. Reload must retain those exact counts, showing that
the revision marker prevents a second pass. A public operator-command probe in
the apply phase returns zero for both already auto-queued chunks; it is
diagnostic and does not perform the proof itself.

The minimum-style formation comparison uses a separate ignored world and a
test-only provider override generated from the exact packaged declaration. It
changes only the Sampler's five profile-wide formation controls to the released
`tiny` preset; it does not add a fourth packaged template:

```powershell
./gradlew runGameTestServer -PcakeworldSamplerRuntime=true -PcakeworldSamplerFormationCase=minimum -PcakeworldSamplerRunDirectory=run-sampler-formation-minimum-local -PcakeworldGameTestNamespaces=cakeworld_formation -PcakeworldExpectedFormationAlgorithm=sky_v1
```

Repeat with `-PcakeworldSamplerReuseWorld=true` to verify the saved profile.
Fixed seed `5059928472718672684` produces exact `32,350/1,627/8/14` vertical
transitions, horizontal transitions, distinct rocks and distinct geomes, with
unsigned signature `12,479,179,277,466,877,779`. This is a labelled minimum
named-preset case, not a claim that it minimises every observed transition
count or that presets can vary per geome.

The unrelated-source proof is also isolated. It uses no TerraBlender and no
production biome-source hook:

```powershell
./gradlew runGameTestServer -PcakeworldSamplerThirdPartyRuntime=true -PcakeworldSamplerRunDirectory=run-sampler-third-party-local
```

The dedicated switch conditionally registers
`cakeworld_fixture:delegated_meadow`, installs a fixed Overworld source at
highest-priority level load, and then lets released OreSpawn perform its normal
wrap. Across 4,225 samples, the final selected-namespace plot yields exactly
1,736 untouched fixture results and 2,489 Candy-Plains selections. This proves
that the six earlier `minecraft_only` Overworld plots delegated the unrelated
source unchanged before the explicit fixture plot. Fresh/reload reproduce the
counts. An ordinary Sampler asserts the fixture registry ID is absent.

### Read-only active-profile status

Run `/cakeworld orespawn` in a dedicated or integrated server to inspect the
world-owned OreSpawn profile without opening its snapshot or invoking a
maintenance command. CakeWorld calls only
`OreSpawnApi.getActiveProfile(server)` and reports the selected template,
geology mode and typed-view counts for rocks, geomes, ores, fluid deposits,
biome palettes and terrain dimensions. The command is available at ordinary
player permission, performs no file/config/registry lookup in a generation
loop, and cannot reload, retrogen or otherwise mutate the active profile.

The same-seed stable baseline uses a separate world because OreSpawn formation
settings are profile-wide:

```powershell
./gradlew runGameTestServer -PcakeworldFreshWorldgenRuntime=true -PcakeworldFreshWorldgenRunDirectory=run-formation-stable-local -PcakeworldGameTestNamespaces=cakeworld_formation -PcakeworldExpectedFormationAlgorithm=stable_layers
```

Do not describe these as two adjacent algorithms inside one world. Per-geome
formation presets are not part of the released contract.

## Provider Overrides

Released OreSpawn 4.0.6 looks for CakeWorld's pack-author override at:

```text
config/cakeworld-orespawn.json
```

A present, valid file is authoritative: it replaces CakeWorld's packaged
provider declaration for that installation. New worlds snapshot the selected
effective profile into `world/serverconfig/orespawn-worldgen.json`; deleting
the global override later does not silently convert that existing save. New
worlds created after deletion use the packaged provider again unless another
explicit global selection wins.

A present malformed override fails closed. OreSpawn reports the validation
error and leaves CakeWorld's provider inactive instead of falling back to the
packaged total conversion. In Forge's development lifecycle the same unique
error can be emitted in both provider-discovery passes. This is noisy but does
not create duplicate active definitions or stop the server safely creating a
non-CakeWorld world.

The repository contains an isolated three-part proof. Use a disposable run
directory; the valid run creates the saved profile needed by the removal run:

```powershell
./gradlew runGameTestServer -PcakeworldProviderOverrideMode=valid -PcakeworldProviderOverrideRunDirectory=run-provider-override-local
./gradlew runGameTestServer -PcakeworldProviderOverrideMode=removed -PcakeworldProviderOverrideRunDirectory=run-provider-override-local -PcakeworldProviderOverrideReuseWorld=true
./gradlew runGameTestServer -PcakeworldProviderOverrideMode=malformed -PcakeworldProviderOverrideRunDirectory=run-provider-override-malformed-local
./gradlew runGameTestServer -PcakeworldProviderOverrideMode=schema3 -PcakeworldProviderOverrideRunDirectory=run-provider-schema3-local
./gradlew runGameTestServer -PcakeworldProviderOverrideMode=schema3 -PcakeworldProviderOverrideRunDirectory=run-provider-schema3-local -PcakeworldProviderOverrideReuseWorld=true
```

The preparation task refuses fresh scenarios in a directory with an existing
world-owned profile. The `removed` mode requires both that profile and the
explicit reuse flag, deletes only the isolated `cakeworld-orespawn.json`
fixture, and preserves the save. These fixtures are test resources and are
not included in the mod JAR.

The final pair installs an isolated schema-3 provider whose auto-selected
template deliberately contains an empty profile. Released OreSpawn must turn
that declaration into a current schema-5 world snapshot with `geome` mode,
`stable_layers`, all five `average` formation presets, the configured fluid
default and provider revision `1101`, then retain those settings on same-save
reload. CakeWorld's `check` task separately scans all packaged Java source and
fails on deprecated `OilDefinition`/`OreSpawnOreIntegration` use or any
OreSpawn import outside `zone.moddev.mc.orespawn.api`.

## Forge IMC Provider Alternative

CakeWorld intentionally distributes only the packaged JSON provider. To show
the equivalent Java submission lifecycle without creating duplicate production
definitions, run this isolated fixture:

```powershell
./gradlew runGameTestServer -PcakeworldImcProviderRuntime=true -PcakeworldImcProviderRunDirectory=run-imc-provider-local
```

The preparation task first runs resource generation, removes only
`build/resources/main/data/cakeworld/orespawn/provider.json`, installs a
test-only flat `cakeworld:sampler_pantry` dimension, and refuses any saved
profile in the requested directory. With the explicit JVM switch, CakeWorld
builds a disabled `cakeworld:ore/imc_probe` plus
`cakeworld:ore/imc_selector_probe` entirely from registry IDs during
`InterModEnqueueEvent` and calls `OreSpawnApi.enqueue`. The selector uses
`orespawn:all_except_nether_end`, while an explicit disabled Overworld rule
overrides it.

The focused test requires public status `ACTIVE`, exact CakeWorld ownership in
the world snapshot, provider revision `6001`, no selected template, immutable
built JSON and exact selector/override declarations. At fixed seed and chunk
`512,512`, the ordinary test dimension contains exactly 1,818 Sprinkle outputs;
Overworld, Nether and End each contain zero. A finalizer restores the generated
packaged provider and removes the test dimension after success or failure. The
source provider is never mutated during parallel setup.

## Adding Content

For a new rock:

1. Register its block and block item.
2. Add blockstate, block/item models, loot, translations, and mining tags.
3. Add a provider-owned rock rule with its output block, family, depth,
   dimensions, and ore-replaceable flag.
4. Add it to relevant vanilla ore-replaceable tags if native ores should use it.

For a new biome:

1. Register it directly or use `OreSpawnBiomes.copyAndRegister`.
2. Register its Forge `BiomeDictionary` types during common setup if geology
   or compatibility rules use dictionary names.
3. Add it to a provider palette with weight, climate range, optional similar
   biomes, and surface materials.
4. Decide whether missing comparison biomes are optional (`similar_biomes`) or
   make the entry unavailable (`required_similar_biomes`).

For a new fluid:

1. Register source and flowing fluids, its liquid block, and bucket.
2. Add models, translations, and fluid tags.
3. Reference the liquid block ID from `dimension_materials` for aquifers, or
   add a provider-owned fluid-deposit rule for covered underground deposits.

## Performance

Keep provider input declarative. OreSpawn resolves IDs, tags, climates, block
states, and dimension tables before generation. Do not introduce per-block
callbacks or duplicate biome/terrain loops in CakeWorld.

CakeWorld forwards OreSpawn's opt-in GameTest benchmark properties. For a
three-run, 81-chunk Overworld proof:

```powershell
./gradlew runGameTestServer -PorespawnBenchmarkMode=sky \
  -PorespawnBenchmarkRadius=4 -PorespawnBenchmarkRepetitions=3
```

Set `-PorespawnBenchmarkDimension=nether` or `end` to exercise the other
dimension profiles. Benchmark saves and logs are generated under `run/` and
remain ignored.

## Current Proof Limits

- Textures and sounds are placeholders borrowed from vanilla.
- Visual, audio, balance, accessibility, multiplayer, and family-play review
  remain incomplete even where automated gameplay contracts are verified.
- A few OreSpawn capabilities are explicitly shelved with evidence in the
  showcase contract; they must not be presented as working examples.
- The Sampler Platter currently proves explicit selection, augment mode,
  `minecraft_only` declaration, `selected_namespaces`, namespace include and
  exclude lists, optional/required similar-biome behavior, and all five live
  region settings with fixed 128/256/512/1,024/2,048-block boundary evidence.
  Its extreme formation, registered-output alias, three-layer flat-bedrock and
  copied-world retrogen and minimum-preset formation cases are also automated.
  The unrelated mod-biome pass-through fixture is automated as well. Hands-on
  formation readability review and the remaining labelled plots are still
  Slice 7 work.
