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
be selected explicitly. Provider revision 49 retains its first bounded
diagnostic plot—Candy Plains augmenting the delegated vanilla source with
`minecraft_only` scope, tiny regions, partial coverage, and a positive
fallback weight—and adds two namespace-filter plots. The Nether uses an
explicit `minecraft` allow-list with `selected_namespaces`; its Fudge output
lists one installed and one missing optional similar biome, while a competing
Crags output requires a deliberately missing biome. The End declares a
Meringue output under `all` scope but excludes `minecraft`, so vanilla End
sources must delegate unchanged. Together they demonstrate include/exclude
lists and optional/required dependency behavior without callbacks or effects
on either adventure.

Run its isolated two-test fresh-world proof with:

```powershell
./gradlew runGameTestServer -PcakeworldSamplerRuntime=true -PcakeworldSamplerRunDirectory=run-sampler-platter-local
```

The preparation task refuses to reuse a directory that already contains a
world-owned OreSpawn profile. To prove persistence against the same save, run
the same command again with `-PcakeworldSamplerReuseWorld=true`. This reuse
flag is intentionally opt-in so an ordinary diagnostic run cannot silently
turn into reload evidence.

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
```

The preparation task refuses fresh scenarios in a directory with an existing
world-owned profile. The `removed` mode requires both that profile and the
explicit reuse flag, deletes only the isolated `cakeworld-orespawn.json`
fixture, and preserves the save. These fixtures are test resources and are
not included in the mod JAR.

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
  exclude lists, optional/required similar-biome behavior, and live
  tiny/small/average region settings. An unrelated mod-biome pass-through
  fixture, large/huge regions, extreme formations, aliases, retrogen, flat
  bedrock, and the remaining labelled plots are still Slice 7 work.
