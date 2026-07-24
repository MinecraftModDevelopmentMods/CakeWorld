# CakeWorld Integration Notes

CakeWorld is an OreSpawn provider. Its packaged declaration is
`data/cakeworld/orespawn/provider.json`, using provider schema 4.

Supported OreSpawn Java APIs are under `com.mcmoddev.orespawn.api`. CakeWorld
registers its own content; OreSpawn owns biome placement, terrain replacement,
world materials, ores, templates, and the world-creation editor.

The `cakeworld:edible_world` template is auto-selected for fresh worlds unless
a pack has explicitly selected another default. Existing world profiles are
not rewritten.

See the source repository's `README.md` and `docs/DEVELOPER_GUIDE.md` for the
complete development guide.
