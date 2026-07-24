# CakeWorld Integration Notes For Humans And Agents

CakeWorld is a normal Forge content mod and an OreSpawn provider. Do not
reimplement OreSpawn's hot-loop world-generation logic here.

Supported integration surfaces:

- packaged JSON at `data/cakeworld/orespawn/provider.json`;
- public Java types under `com.mcmoddev.orespawn.api`;
- normal Forge registries for CakeWorld-owned blocks, fluids, items, and
  biomes.

Provider schema 4 supplies biome palettes and dimension materials. The
CakeWorld template is automatic only for fresh worlds and remains editable
through OreSpawn's world-creation UI. Existing worlds retain their profile.

Build with Java 17. Treat a successful Java compile as necessary but
insufficient: validate JSON, process resources, build the jar, regenerate
Eclipse runs, and inspect a client/world smoke for missing models, invalid
registry IDs, provider rejection, and generation errors.
