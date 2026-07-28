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

The development build resolves the published Minecraft 1.18.2 OreSpawn 4.0.1
beta through CurseMaven. `gradle.properties` pins CurseForge project `245586`
and file `8525123`, rather than reading a mutable sibling build directory. When
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
- Copied biomes retain vanilla feature lists, mobs, structures, and effects.
- There is no finished food, recipe, vegetation, structure, or progression
  design yet.
- The provider needs generated-world inspection and tuning before release.
