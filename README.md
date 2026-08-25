# Cake World

Cake World is an early-alpha, family-friendly confectionery world conversion
for Forge 1.18.2, powered by MMD OreSpawn 4. It turns Minecraft's dimensions
into edible alternatives while delegating biome placement, terrain strata,
surfaces, aquifers, ores, and fresh-world settings to OreSpawn.

The first public alpha includes a broad but incomplete collection of edible
terrain and fluids, themed biomes across all three vanilla dimensions, foods,
creatures, structures, an Explorer's Cookbook, and optional Base Metals
integration. Cake World has no TerraBlender dependency.

## Alpha Notice

This is a testable development alpha, not a finished gameplay release.
Advanced structures and rare world-generation scenes are still being tuned,
and many visual and audio assets deliberately reuse vanilla placeholders.

- Create a fresh test world so the Cake World OreSpawn template can be
  selected. Existing worlds keep their saved OreSpawn profile.
- Back up any world you care about before testing an alpha build.
- Expect incomplete presentation, balance, accessibility review, and
  occasional missing or variable rare world-generation features.
- Please report reproducible crashes, corrupt saves, or missing registry and
  resource errors with the relevant log and world seed.

## Design and Integration Documents

- [Game Design Bible](docs/GAME_DESIGN.md) — player experience, all three
  realms, biomes, physical blocks, foods, gadgets, Cookbook, creatures,
  structures, and playable delivery slices.
- [OreSpawn Showcase Contract](docs/ORESPAWN_SHOWCASE.md) — every OreSpawn
  capability mapped to an adventure, compatibility, or diagnostic example and
  its verification scenario.
- [Compatibility Contract](docs/COMPATIBILITY.md) — themed vanilla resources
  and all thirteen BaseMetals conversions, with tags, recipes, placement
  ownership, and world-migration boundaries.
- [Developer Guide](docs/DEVELOPER_GUIDE.md) — the current scaffold's ownership
  model, provider structure, and safe extension points.

## Requirements

- Minecraft 1.18.2
- Forge 40.3.0 or compatible Forge 40 build
- Java 17
- MMD OreSpawn 4.0.6.118021 or a newer compatible 4.x release for Minecraft
  1.18.2

## Installation

1. Install Forge for Minecraft 1.18.2 and run it with Java 17.
2. Put the Cake World and MMD OreSpawn JARs in the profile's `mods` directory.
3. Start Minecraft and create a fresh world for the automatic edible-world
   profile.

## Build

CakeWorld pins the published MMD OreSpawn release through CurseMaven using
CurseForge project `245586`, file `8688546`. A sibling OreSpawn checkout or
locally built JAR is not required.

```powershell
./gradlew build
./gradlew genEclipseRuns
./gradlew eclipse
```

The reobfuscated mod JAR is written under `build/libs/`.

## Integration

The packaged declaration is
`src/main/orespawn/provider.json`. The build packages its generated result at
`data/cakeworld/orespawn/provider.json`. It uses provider
schema 4 and selects `cakeworld:edible_world` for fresh worlds unless a pack
author has explicitly chosen another default template. The generated provider
contains the normal adventure, its conditional BaseMetals overlay, and the
optional `cakeworld:sampler_platter` developer template. The Sampler is never
auto-selected. Its packaged retrogen rules are disabled fixtures; the build
harness enables them only inside an explicitly reused, disposable test save.
Its ninth biome plot targets only the dormant `cakeworld_fixture` namespace;
that namespace is registered and installed as a source only when the explicit
third-party GameTest switch is present.

OreSpawn's creation editor can be opened against an isolated client directory
with:

```powershell
./gradlew runClient -PcakeworldEditorRunDirectory=run-editor-acceptance-local
```

The harness refuses to reuse an existing save unless
`-PcakeworldEditorReuseWorld=true` is explicit. Published OreSpawn
4.0.6.118021 currently prevents CakeWorld from completing this acceptance:
its editor displays CakeWorld's biome/material pages, including when strata
are disabled, but rejects the provider's valid namespaced geomes on Done (for
example `Invalid geome: cakeworld:cocoa_basin`). OS-020 is therefore shelved
until a published OreSpawn version makes editor validation match the provider
schema/runtime. Do not work around the defect by renaming CakeWorld's public
geomes or using a locally patched dependency.

To run the currently implemented Sampler biome plots, profile-wide extreme
`sky_v1` formation comparison, installed-output alias and three-layer
flat-bedrock proof against a fresh fixed-seed world:

```powershell
./gradlew runGameTestServer -PcakeworldSamplerRuntime=true -PcakeworldSamplerRunDirectory=run-sampler-platter-local '-PcakeworldGameTestNamespaces=cakeworld_sampler,cakeworld_formation' -PcakeworldExpectedFormationAlgorithm=sky_v1
```

The task refuses a directory that already owns a saved world profile. Pass
`-PcakeworldSamplerReuseWorld=true` as an additional, explicit flag only when
collecting same-save reload evidence.

The separate minimum-preset formation case is generated as an isolated
test-only provider override from the exact packaged declaration. It keeps the
same Sampler template and geology but selects `tiny` for all five profile-wide
formation controls:

```powershell
./gradlew runGameTestServer -PcakeworldSamplerRuntime=true -PcakeworldSamplerFormationCase=minimum -PcakeworldSamplerRunDirectory=run-sampler-formation-minimum-local -PcakeworldGameTestNamespaces=cakeworld_formation -PcakeworldExpectedFormationAlgorithm=sky_v1
```

Add `-PcakeworldSamplerReuseWorld=true` to the same command for reload proof.
The override and save stay under the ignored run directory and never alter the
packaged Sampler or an ordinary adventure.

The unrelated-source composition proof installs a fixed test-only biome source
before OreSpawn's released wrapper and never adds TerraBlender or another
production dependency:

```powershell
./gradlew runGameTestServer -PcakeworldSamplerThirdPartyRuntime=true -PcakeworldSamplerRunDirectory=run-sampler-third-party-local
```

Use the normal explicit reuse flag for same-save reload. Without the dedicated
switch, neither the fixture biome nor its source owner is registered.

The main adventure's full replace-mode proof reuses that dormant source owner
in a separate disposable world. It asserts all five production palettes use
`replace`, `scope=all`, full coverage, zero fallback and a CakeWorld namespace
exclusion, then surveys 66,049 fixed-seed positions on each of four planes:

```powershell
./gradlew runGameTestServer -PcakeworldReplaceModeRuntime=true -PcakeworldReplaceModeRunDirectory=run-replace-mode-local
./gradlew runGameTestServer -PcakeworldReplaceModeRuntime=true -PcakeworldReplaceModeRunDirectory=run-replace-mode-local -PcakeworldReplaceModeReuseWorld=true
```

The unrelated constant Overworld source maps every sampled surface and cave
position to the adventure's generic `cakeworld:candy_plains` output; outputs
with vanilla similarity restrictions remain ineligible for that unknown ID.
Vanilla-backed Nether and End samples retain exact seven-biome fixed-seed
distributions. All 264,196 results are CakeWorld biomes, the unrelated source
never leaks through, and fresh/reload signatures are identical. The first
command refuses an existing saved profile; reuse must be explicit. Without
`cakeworldReplaceModeRuntime`, the fixture is not registered or installed.

OreSpawn's advanced `blankAndRegister` helper has a separate registry-only
laboratory proof:

```powershell
./gradlew runGameTestServer -PcakeworldBlankBiomeRuntime=true -PcakeworldBlankBiomeRunDirectory=run-blank-biome-local
```

The opt-in `cakeworld_fixture:blank_biome_laboratory` supplies every required
biome-builder field itself, including deliberately empty base spawn and
generation settings. The runtime proof requires OreSpawn's two later lifecycle
hooks (`orespawn:biome_surfaces` and `orespawn:stone_replacer`) and rejects any
other placed feature. It also proves the laboratory is absent from the active
profile and biome-source outputs. A normal startup does not register it, and
the command refuses to reuse a directory that already owns a saved profile.

The complete production climate-range boundary matrix is exercised separately:

```powershell
./gradlew runGameTestServer -PcakeworldClimateBoundaryRuntime=true -PcakeworldClimateBoundaryRunDirectory=run-climate-boundaries-local
```

The preparation task derives all 35 packaged biome outputs into their 17
distinct temperature/downfall ranges, then builds an ignored diagnostic
provider. A test-only 102-biome checkerboard gives every range two exact corner
cases plus the closest representable source climate beyond each of its four
limits. Released OreSpawn must select both inclusive corners and delegate all
four outside cases. The diagnostic provider and source biomes are absent from
ordinary startup, and an existing saved profile is always refused.

In any running CakeWorld server, `/cakeworld orespawn` gives players and pack
makers a read-only summary of the world-owned OreSpawn profile: selected
template, geology mode, and the counts of rocks, geomes, ores, fluid deposits,
biome palettes and terrain dimensions. It uses only OreSpawn's public active-
profile view, requires no operator permission and cannot reload or mutate the
world.

The copied-world retrogen proof is deliberately three-phase and uses one
ignored test directory. Never point these commands at a player world:

```powershell
./gradlew runGameTestServer -PcakeworldSamplerRuntime=true -PcakeworldSamplerRunDirectory=run-sampler-retrogen-local -PcakeworldSamplerRetrogenPhase=baseline -PcakeworldGameTestNamespaces=cakeworld_sampler_retrogen
./gradlew runGameTestServer -PcakeworldSamplerRuntime=true -PcakeworldSamplerReuseWorld=true -PcakeworldSamplerRunDirectory=run-sampler-retrogen-local -PcakeworldSamplerRetrogenPhase=apply -PcakeworldGameTestNamespaces=cakeworld_sampler_retrogen
./gradlew runGameTestServer -PcakeworldSamplerRuntime=true -PcakeworldSamplerReuseWorld=true -PcakeworldSamplerRunDirectory=run-sampler-retrogen-local -PcakeworldSamplerRetrogenPhase=reload -PcakeworldGameTestNamespaces=cakeworld_sampler_retrogen
```

The baseline writes exact eligible and ineligible slabs into old chunks. Apply
enables revision `5301` only in that save and proves bounded processing; reload
proves the revision marker prevents a second pass while `force` remains false.

The adventure remains on average `stable_layers`. The developer guide gives
the separate same-seed baseline command; OreSpawn formations are profile-wide,
so CakeWorld does not claim two algorithms can occupy adjacent plots in one
world.

Pack authors can replace CakeWorld's packaged declaration with
`config/cakeworld-orespawn.json`. Valid precedence, same-save persistence after
removal, fresh-world restoration, and malformed fail-closed behavior have
isolated Gradle proofs documented in `docs/DEVELOPER_GUIDE.md`.

That harness also has an unbundled schema-3 compatibility case:

```powershell
./gradlew runGameTestServer -PcakeworldProviderOverrideMode=schema3 -PcakeworldProviderOverrideRunDirectory=run-provider-schema3-local
./gradlew runGameTestServer -PcakeworldProviderOverrideMode=schema3 -PcakeworldProviderOverrideRunDirectory=run-provider-schema3-local -PcakeworldProviderOverrideReuseWorld=true
./gradlew runGameTestServer -PcakeworldProviderOverrideMode=equal_priority -PcakeworldProviderOverrideRunDirectory=run-provider-equal-priority-local
./gradlew runGameTestServer -PcakeworldProviderOverrideMode=equal_priority -PcakeworldProviderOverrideRunDirectory=run-provider-equal-priority-local -PcakeworldProviderOverrideReuseWorld=true
```

The pair proves legacy declaration reading, current snapshot normalization and
same-save retention. `check` also rejects deprecated
`OilDefinition`/`OreSpawnOreIntegration` use and imports outside OreSpawn's
public `zone.moddev.mc.orespawn.api` package.

The equal-priority case proves lexical fresh-world selection and same-save
retention with an unbundled two-template fixture. OreSpawn 4.0.6.118021 does
not emit its documented tie warning when the lexical winner is declared first;
`docs/ORESPAWN_SHOWCASE.md` records that released limitation as OS-105.

Existing-world provider merge behavior has a separate three-phase disposable
world proof. Never point these commands at a player world:

```powershell
./gradlew runGameTestServer -PcakeworldProviderMergePhase=baseline -PcakeworldProviderMergeRunDirectory=run-provider-merge-local
./gradlew runGameTestServer -PcakeworldProviderMergePhase=upgrade -PcakeworldProviderMergeRunDirectory=run-provider-merge-local
./gradlew runGameTestServer -PcakeworldProviderMergePhase=reload -PcakeworldProviderMergeRunDirectory=run-provider-merge-local
```

The baseline installs provider revision `1701`. Before revision `1702`, the
upgrade task edits one rock and one ore, disables another ore, removes an ore
assignment while retaining its known-ID tombstone, and adds a world-owned rule
and alias. Released OreSpawn must preserve all of those choices, add the new
provider IDs, retain a removed provider rule as orphaned, and make reload a
byte-stable no-op. The two provider declarations are test fixtures and are not
included in the mod JAR.

The packaged JSON remains CakeWorld's canonical distributable declaration. A
separate one-shot harness demonstrates the alternative Forge IMC path without
shipping two competing definitions:

```powershell
./gradlew runGameTestServer -PcakeworldImcProviderRuntime=true -PcakeworldImcProviderRunDirectory=run-imc-provider-local
```

The harness removes only the generated provider from the development runtime,
installs one test-only flat ordinary dimension, submits a disabled lifecycle
probe plus an active selector probe during `InterModEnqueueEvent`, verifies
public ownership/freeze/immutability, and restores the generated resource while
removing the dimension in a task finalizer even when the server fails. The
selector produces output in that ordinary dimension, is explicitly disabled
in the Overworld, and is excluded from the Nether and End. The harness refuses
a directory containing a saved world profile.

The design documents describe target behavior. They do not imply that the
current alpha already implements the full wishlist.

## License

Cake World is licensed under the GNU General Public License version 3 only
(`GPL-3.0-only`).
