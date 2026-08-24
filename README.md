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
author has explicitly chosen another default template.

The design documents describe target behavior. They do not imply that the
current alpha already implements the full wishlist.

## License

Cake World is licensed under the GNU General Public License version 3 only
(`GPL-3.0-only`).
