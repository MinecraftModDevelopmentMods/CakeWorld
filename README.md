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
before original art and content are produced.

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
`src/main/resources/data/cakeworld/orespawn/provider.json`. It uses provider
schema 4 and selects `cakeworld:edible_world` for fresh worlds unless a pack
author has explicitly chosen another default template.

See [docs/DEVELOPER_GUIDE.md](docs/DEVELOPER_GUIDE.md) for the ownership model,
provider structure, and safe extension points.
