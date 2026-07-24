# Cake World

Cake World is a Forge 1.18.2 proof mod that turns Minecraft's dimensions into
edible alternatives using OreSpawn 4. It demonstrates that a child mod can
register its own blocks, fluids, and biomes while delegating biome placement,
terrain strata, surfaces, aquifers, snow, ice, and world-creation settings to
OreSpawn.

The current proof includes:

- chocolate sponge, icing, biscuit stone, biscuit crumbs, frozen lemonade, and
  fudge rock;
- lemonade and hot-fudge fluids with buckets;
- edible Overworld, Nether, and End biomes copied from vanilla starting points;
- an OreSpawn provider and fresh-world auto-template;
- no TerraBlender dependency.

This is a development scaffold, not a finished gameplay release. The visual
assets currently reuse vanilla textures so integration behavior can be tested
before original art and content are produced. The intended full game is an
original, family-friendly confectionery total conversion, documented before
implementation so ambitious features can be built, verified, or deliberately
shelved without losing the overall direction.

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
- OreSpawn 4.x

## Build

Build OreSpawn first, then:

```powershell
./gradlew build
./gradlew genEclipseRuns
./gradlew eclipse
```

The jar is written under `build/libs/`.

## Integration

The packaged declaration is
`src/main/orespawn/provider.json`. The build packages its generated result at
`data/cakeworld/orespawn/provider.json`. It uses provider
schema 4 and selects `cakeworld:edible_world` for fresh worlds unless a pack
author has explicitly chosen another default template.

The design documents describe target behavior. They do not imply that the
current scaffold already implements the full wishlist.
