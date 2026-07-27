# CakeWorld OreSpawn Showcase Contract

## Purpose

CakeWorld is both a game and the reference child mod for OreSpawn 4 on
Minecraft Forge 1.18.2. A developer should be able to find a readable,
enjoyable CakeWorld example for every supported OreSpawn capability, while the
OreSpawn project should be able to use CakeWorld as an integration and
regression test.

This document assigns each OreSpawn capability a stable `OS-*` feature ID, a
CakeWorld example, a delivery slice, and an acceptance scenario. It is a design
contract, not a claim that the wishlist has already been implemented.

OreSpawn owns profile selection, biome overlay, geology, terrain replacement,
ore placement, fluid deposits, world materials, snapshots, and the compiled
world-generation hot path. CakeWorld owns its blocks, fluids, biomes,
structures, entities, tags, recipes, translations, models, loot, and packaged
provider definition.

## Showcase Profiles

CakeWorld defines three templates around one canonical profile model so common
settings cannot drift. Provider revision 11 currently packages the two
adventure templates; the diagnostic Sampler remains a Slice 7 feature.

| ID | Template | Selection | Purpose |
|---|---|---|---|
| OS-001 | `cakeworld:edible_world` | Automatically selected for a fresh world when CakeWorld is installed and no higher-priority compatible template or explicit global default wins. | The normal adventure. Uses only features that improve the playable total conversion. |
| OS-002 | `cakeworld:edible_world_basemetals` | Higher priority than `edible_world`, requires both `cakeworld` and `basemetals`, and is eligible only for fresh-world automatic selection. | The same adventure with CakeWorld counterparts for every supported BaseMetals resource. |
| OS-003 | `cakeworld:sampler_platter` | Never auto-selected; chosen explicitly by developers or pack makers. | Diagnostic world for augment mode, extreme formations, aliases, retrogen, flat bedrock, intrusive test rules, and compact visual inspection. |

An automatically selected template applies only during fresh-world creation.
It never silently rewrites an existing world's self-contained profile.
Selecting or migrating an existing world is an explicit administrative action.

The BaseMetals template is an overlay generated from the same canonical
`edible_world` definition. It must not become a separately maintained fork of
the adventure profile. A deterministic build check removes the thirteen
BaseMetals rule overrides from the generated compatibility profile and requires
the remaining serialized profile to match the normal adventure exactly.

## Capability Matrix

“Main” means the ordinary edible-world adventure, “BaseMetals” means its
compatibility overlay, and “Sampler” means the optional diagnostic template.

### Provider lifecycle and public API

| ID | OreSpawn capability | CakeWorld use | Placement | Slice | Verification expectation |
|---|---|---|---|---|---|
| OS-004 | Schema 4 packaged provider | Ship `data/cakeworld/orespawn/provider.json` with matching provider mod ID and an incremented positive revision. | Main | 1 | Schema validation and a fresh dedicated-server world both accept the packaged provider. |
| OS-005 | Full-mod dependency and supported API boundary | Depend on OreSpawn 4.x and use only `zone.moddev.mc.orespawn.api` from Java. | Main | 1 | Dependency metadata rejects an absent or incompatible OreSpawn; source audit finds no implementation-package imports. |
| OS-006 | Forge IMC provider submission | Keep the packaged JSON as the canonical distributable declaration; add an IMC-built equivalent only when it demonstrates a real integration need. | Sampler | 7 | A dedicated test provider submitted during `InterModEnqueueEvent` freezes with the same rule ownership and no parallel-setup mutation. |
| OS-007 | Immutable definitions and bake-time registry resolution | Store registry IDs declaratively and let OreSpawn validate and bake holders, tags, fluids, settings, and weights. The public active-profile view rejects collection mutation and returns detached diagnostic JSON. | Main | 1 | A dedicated-server test cannot mutate ID collections or the active profile through returned JSON; the unchanged profile then generates its expected rocks, ores, fluids, surfaces, and biomes. Missing-optional-ID behaviour is isolated under OS-046. |
| OS-008 | Active-profile query | Use `OreSpawnApi.getActiveProfile` for a developer status command or diagnostic screen. | Sampler | 7 | Server-side query reports the selected template and expected rule counts without mutating the profile. |
| OS-009 | Production geology sampler | Use `OreSpawnApi.createSampler` for Cookbook geology clues or a developer tasting probe. | Main | 2 | One sampled column reports its biome, geome, and rocks at several Y levels; sampling remains read-only. |
| OS-010 | Ore-takeover state | Suppress a child mod's native generation only when `isOreTakeoverActive(modid)` is active; pending and inactive are fail-safe. | BaseMetals | 2 | No duplicate generation when takeover is active and no lost native ores when discovery is pending or inactive. |
| OS-011 | Legacy schema reading | Keep CakeWorld on schema 4, but include one schema-3 migration fixture. | Sampler | 7 | Fixture loads with documented defaults; re-export or snapshot retains equivalent effective settings. |
| OS-012 | Deprecated migration adapters | Document that CakeWorld does not use `OilDefinition` or `OreSpawnOreIntegration`; retain a fixture only if OreSpawn needs regression coverage. | Sampler | 7 | Static source audit confirms production CakeWorld uses the current API. |

CakeWorld's themed vanilla blocks currently expose their mining and processing
contracts without taking over placement. OS-010 needs an OreSpawn rule field
that separately names the vanilla source block to suppress when a managed rule
places a different CakeWorld output. The existing global suppression switch is
not an acceptable substitute because it would remove unknown third-party ore
features. `docs/COMPATIBILITY.md` records the full boundary and re-entry
condition.

### Profiles, templates, snapshots, and overrides

| ID | OreSpawn capability | CakeWorld use | Placement | Slice | Verification expectation |
|---|---|---|---|---|---|
| OS-013 | `profile_defaults` | Establish shared edible geology and safe generation defaults before per-rule overrides. | Main | 2 | Snapshot shows the intended defaults and rule-specific overrides win deterministically. |
| OS-014 | Template `required_mods` | Require CakeWorld for the main template and CakeWorld plus BaseMetals for the compatibility template. | Main/BaseMetals | 1/2 | Template list and auto-selection change correctly with BaseMetals absent and present. |
| OS-015 | Automatic template selection and priority | Use priority ordering so BaseMetals fresh worlds select the compatibility template; an explicit global default remains authoritative. | Main/BaseMetals | 1/2 | Four-way test covers CakeWorld alone, CakeWorld plus BaseMetals, explicit default, and competing equal-priority lexical ordering. |
| OS-016 | Fresh-world snapshot | Freeze the selected effective profile into the world so it remains self-contained. | Main | 1 | Save/reload with packaged files changed still uses the world's recorded profile until an explicit migration. |
| OS-017 | Existing-world rule merge and tombstones | Demonstrate that newly introduced IDs merge while player edits, disabled rules, unassigned rules, and removed-rule tombstones are preserved. | Sampler | 7 | Upgrade a copied test world through two provider revisions and compare the resulting snapshot. |
| OS-018 | Pack override precedence | Support `config/cakeworld-orespawn.json` as an authoritative provider override. | Main | 7 | Valid override replaces the packaged declaration; removal restores packaged behavior only for a new or explicitly migrated profile. |
| OS-019 | Malformed override fail-closed behavior | A present malformed override must leave CakeWorld's provider inactive rather than silently falling back. | Sampler | 7 | Dedicated server logs one actionable validation failure and generates no CakeWorld takeover terrain from the ignored fallback. |
| OS-020 | Creation-time editor | Keep all IDs and settings compatible with OreSpawn's world-creation editor, including biome and material pages when strata are disabled. | Main | 7 | Create an edited fresh world and confirm its snapshot and generated output match the editor selections. |

### Rocks, geology, and geomes

| ID | OreSpawn capability | CakeWorld example | Placement | Slice | Verification expectation |
|---|---|---|---|---|---|
| OS-021 | Igneous-intrusive rock family | Peppermint crystal masses and slow-cooled candy intrusions. | Main | 2 | Profile and sampled columns identify intrusive-family hosts at intended depths. |
| OS-022 | Igneous-volcanic rock family | Fudge rock and crystallised burnt-sugar volcanic material. | Main | 2/4 | Profile and sampled columns identify volcanic-family hosts in their intended dimensions. |
| OS-023 | Sedimentary rock family | Layered wafer, biscuit, chocolate sponge, and soft fudge strata. | Main | 2 | Sampled columns show weighted sedimentary members and compatible ore replacement. |
| OS-024 | Metamorphic rock family | Dense nougat, folded rock-candy, and fantastical compressed meringue crust. | Main | 2/5 | Folded/deep host bands appear and host-family ore rules match them in Overworld and End. |
| OS-025 | Per-rock depth, weight, and ore-replaceable controls | Heavy nougat dominates deep bands; light wafer dominates shelves; sparse structural Candy Glass is never an ore host. | Main | 2 | Statistical sample matches the declared depth/weight controls, and paired sampler/final-block evidence shows managed ores never replace predicted Candy Glass. |
| OS-026 | Stable-layers geology mode | Make stable edible strata the readable default for the adventure. | Main | 2 | Fixed-seed vertical sections remain stable across save/reload and within the agreed performance budget. |
| OS-027 | Alternate or legacy geology mode | Expose legacy/alternative formation behavior only as a labelled comparison plot. | Sampler | 7 | Adjacent fixed-seed plots visibly distinguish modes without affecting adventure defaults. |
| OS-028 | Formation presets | Demonstrate preset horizontal size and waviness with broad Cocoa Basins and compact Rock-Candy Uplifts. | Main | 2 | Profile dump reports the presets and fixed-seed sections show materially different shapes. |
| OS-029 | Custom formation values | Use deliberately extreme but bounded formation settings in a labelled sampler quadrant. | Sampler | 7 | Values survive serialization and produce the expected extreme section without timeouts. |
| OS-030 | Geomes | Define Cocoa Basin, Wafer Shelf, Peppermint Fold, Rock-Candy Uplift, Fudge Mantle, and Meringue Crust. | Main | 2/4/5 | The sampler API can identify every geome in a fixed-seed survey. |
| OS-031 | Geome rock weights and vertical identity | Give each geome a recognisable palette rather than merely renaming the same strata. | Main | 2 | Block counts and column samples distinguish all six canonical geomes. |
| OS-032 | Biome rules and biome-dictionary matching | Bias geomes using exact CakeWorld biome IDs plus Forge COLD and HOT dictionary names; retain an unmatched MUSHROOM control. Peppermint Pinewoods combines an exact Peppermint-Fold/Rock-Candy rule with COLD identity. Gummy Jungle combines explicit Cocoa-Basin/Wafer-Shelf/Rock-Candy-Uplift weights with HOT identity, deliberately admitting the additive Fudge-Mantle seam. Caramel Bogs adds an exact equal `8/8` Cocoa-Basin/Wafer-Shelf bias while retaining Forge SWAMP and WET identity. Sherbet Dunes adds an exact `4/12` Wafer-Shelf/Rock-Candy-Uplift bias. Candy-Cane Badlands adds exact `6/10/14` Wafer-Shelf/Peppermint-Fold/Rock-Candy-Uplift weights. Both dry biomes deliberately retain the additive HOT/Fudge-Mantle seam. | Main | 2 | The active revision-18 profile retains exact-ID and dictionary forms and registered types match only intended biomes. Fresh/reload sampling kept all earlier contracts, while the Badlands landmark region exposed genuine Rock-Candy-Uplift output and five distinct final rock materials. |
| OS-033 | Terrain-replacement dimension controls | Enable complete replacement independently in Overworld, Nether, and End. | Main | 2/4/5 | Fresh chunks in all three dimensions contain no unintended ordinary base geology within the declared scope. |

### Biome overlay and world materials

| ID | OreSpawn capability | CakeWorld example | Placement | Slice | Verification expectation |
|---|---|---|---|---|---|
| OS-034 | Forge biome registration helper | Start simple biomes with `copyAndRegister`; Gingerbread Hearthlands, Cookie Crumb Forest, Peppermint Pinewoods, Gummy Jungle, Caramel Bogs, Sherbet Dunes and Candy-Cane Badlands demonstrate copying complete Plains, Forest, Snowy-Taiga, Bamboo-Jungle, Swamp, Desert and Badlands definitions while deliberately replacing climate, ambience and spawn roles. Use `blankAndRegister` only for intentionally complete advanced definitions. | Main | 1/3 | Direct registry tests prove Hearthlands `0.85/0.55`, Cookie Forest `0.7/0.8`, Pinewoods `-0.2/0.7`, Gummy Jungle `0.95/0.95`, Caramel Bogs `0.8/0.9`, Sherbet Dunes `2.0/0.0` and Candy-Cane Badlands `2.0/0.0`; each retains its intended inherited source roles while substituting exact CakeWorld creatures. |
| OS-035 | Replace placement mode | Make the three vanilla dimensions wholly edible in the main adventure. | Main | 1/3/4/5 | Eligible source regions select only CakeWorld biomes with zero fallback weight. |
| OS-036 | Augment placement mode | Add a few CakeWorld biomes while retaining the delegated source as a weighted fallback. | Sampler | 7 | Both source and CakeWorld biomes occur at deterministic weighted rates. |
| OS-037 | `minecraft_only` replacement scope | Replace vanilla source biomes while allowing unrelated modded biomes to survive in a compatibility fixture. | Sampler | 7 | Vanilla source samples convert and a fixture namespace passes through unchanged. |
| OS-038 | `selected_namespaces` replacement scope | Convert an explicit small namespace allow-list. | Sampler | 7 | Included namespaces convert; all others delegate unchanged. |
| OS-039 | `all` replacement scope | Every adventure palette uses `scope=all`; CakeWorld itself is excluded to prevent repeat replacement. | Main | 1/3/4/5 | The active profile retained all-scope replacement, while fixed Nether and End regions were 6,400/6,400 CakeWorld surface columns and the sampled Overworld exposed every current target biome. A separate delegated mod-source fixture remains part of OS-052. |
| OS-040 | Namespace include and exclude lists | Carve out or target source namespaces without per-biome callbacks. | Sampler | 7 | Include/exclude precedence matches the baked profile across fixed source-biome fixtures. |
| OS-041 | Region sizes | Assign broad realm-scale palettes in the adventure and display tiny through huge region sizes side by side in the sampler. | Main/Sampler | 3/7 | Boundary spacing corresponds to 128, 256, 512, 1024, and 2048-block region classes. |
| OS-042 | Coverage | **Adventure half verified:** all four production palettes retain full `coverage=1.0`; partial coverage remains a labelled Sampler fixture. | Main/Sampler | 1/7 | Main fixed-world surfaces and active profile prove full coverage. Do not mark the combined capability complete until the partial Sampler percentage is measured. |
| OS-043 | Fallback weight | **Adventure half verified:** all production palettes retain `fallback_weight=0.0`; positive delegated-source pockets belong only in the Sampler. | Main/Sampler | 1/7 | Main fixed-world replacement proves the zero-fallback contract. Positive weighted fallback still requires the later diagnostic fixture. |
| OS-044 | Weighted output biomes | Gingerbread Hearthlands enters the Overworld-land palette at weight `2.5`, alongside common Candy Plains and above the `2.0` Cookie Forest, `1.5` Peppermint Pinewoods, `1.5` Caramel Bogs, `1.25` Gummy Jungle, `1.25` Sherbet Dunes, `1.25` Candy-Cane Badlands, and less-common Marshmallow Peaks; later Cupcake Gardens or Jellybean islands become the rare classes. | Main | 3 | The revision-18 profile retains every populated weight. The fixed surface survey observed every current output, including 1,787 Candy-Cane-Badlands columns and its distinct Wafer/Candy-Cane surface bands; broader statistical tolerance remains part of the final palette audit. |
| OS-045 | Similar-biome matching | Gingerbread Hearthlands maps optionally to Plains, Sunflower Plains, and Meadow; Cookie Forest to Forest, Flower Forest, and Birch Forest; Peppermint Pinewoods to Snowy Taiga, Taiga, and Old-Growth Pine Taiga; Gummy Jungle to Bamboo Jungle, Jungle, and Sparse Jungle; Caramel Bogs to Swamp; Sherbet Dunes to Desert; Candy-Cane Badlands to Badlands, Eroded Badlands and Wooded Badlands. Each hint remains optional rather than becoming a hard dependency. | Main | 1/3 | Revision 18 retains all seven hint sets with empty required-hint lists; accepted fixed-world sampling observed every mapped CakeWorld biome, including 1,787 Candy-Cane-Badlands columns. A missing optional-hint fixture remains for the Sampler. |
| OS-046 | Required-similar-biome matching | Provide one diagnostic output that correctly disables when its strict fixture biome is unavailable. | Sampler | 7 | Absent required ID disables only that output and emits one bake-time warning. |
| OS-047 | Temperature and downfall ranges | Gingerbread Hearthlands demonstrates a temperate selector (`0.4..1.2` temperature, `0.25..0.8` downfall); Cookie Forest narrows the wet edge (`0.15..1.5`, `0.55..1.0`); Peppermint Pinewoods supplies a cold/wet window (`-2.0..0.3`, `0.35..1.0`); Gummy Jungle supplies a tropical window (`0.7..2.0`, `0.7..1.0`); Caramel Bogs adds a saturated warm-wet window (`0.5..1.2`, `0.75..1.0`); Sherbet Dunes and Candy-Cane Badlands supply the dry extreme (`1.5..2.0`, `0.0..0.2`). | Main | 1/3 | Revision 18 retains all seven selector contracts and fixed-world selection observes every biome; boundary fixtures at min/max and just outside them remain. |
| OS-048 | Surface top block | Use icing turf, sponge, crumb sand, fudge crust, meringue, elastic Gummy, sticky Caramel Crust, four-colour Sherbet and Wafer-mesa signatures. Gingerbread Hearthlands uses gravity-affected Biscuit Crumbs, Cookie Forest uses Chocolate Sponge, Peppermint Pinewoods uses Icing Layers, Gummy Jungle uses Gummy Blocks beneath its bounce-grove ecology, Caramel Bogs uses Caramel Crust beneath its mangroves, Sherbet Dunes uses Raspberry Sherbet Powder beneath its fossils, and Candy-Cane Badlands uses Wafer Rock beneath its hoodoos. | Main | 1/3/4/5 | Revision-18 fixed-world sampling retained every earlier surface plus 1,722 Wafer-Rock tops across 1,787 Badlands columns, alongside green Candy, Marshmallow, Fudge and Meringue controls. |
| OS-049 | Surface filler block and depth | Put Chocolate Sponge four blocks below Hearthlands crumbs, Gummy Jungle and Caramel Bogs, five below Cookie Forest, five blocks of Peppermint Rock below Pinewoods icing, five blocks of Lemon Sherbet Powder below Dunes, and seven blocks of Candy-Cane Pillar below Badlands mesa tops. | Main | 1/3/4/5 | Revision 18 retains every declared depth; fixed-world sampling exposed 10,069 Candy-Cane-Pillar cells in Badlands while all earlier filler bands and realm controls remained green. |
| OS-050 | Underwater surface block | Use Biscuit Crumbs beneath Soda Ocean, Blueberry Gummy beneath Gummy Jungle water, Caramel Crust beneath Caramel-Bogs Lemonade, Orange Sherbet Powder beneath Dunes fluid, Peppermint Rock beneath Badlands fluid, and later custard sediment along Custard Coast. | Main | 3 | Revision 18 retains all five live biome fields; fixed-world surface and active-profile tests retain the new Peppermint-Rock field while earlier Soda, Caramel and Sherbet wet surfaces remain green. A deliberately flooded Gummy fixture remains useful final presentation evidence. |
| OS-102 | Surface-before-child-finalizer ordering | OreSpawn's terrain surface replacement must complete before copied child-mod `TOP_LAYER_MODIFICATION` features that author or repair final landmark cells. CakeWorld deliberately uses late, idempotent finalizers for structures whose terrain-facing cells may otherwise be damaged by generation order. | Main | 2/3/4/5 | Direct feature tests and the last stable integrated build retain complete plans. A current candidate that appends OreSpawn's surface feature after copied child features reproducibly removes late-authored cells from the Crystal Mine and several older landmarks while leaving their saved structure starts intact. Re-enter integrated acceptance only when a fresh/reload matrix proves surface output, every representative late finalizer, and player edits together. |
| OS-051 | Ceiling surface block | **Shelved:** OreSpawn 4.0.1 documents an underside material, but its surface feature scans downward from build height and replaces the first solid block—the inaccessible upper face of the Nether roof—not a player-facing ceiling underside. | Main | 2/4 | Re-enter after OreSpawn targets solid blocks exposed to air or fluid below without replacing structures. A revision-11 diagnostic marker appeared on 20,716/20,736 topmost columns and 0/29,607 downward-exposed ceiling faces, so the accepted adventure does not claim this as a working example. |
| OS-052 | Delegated-source composition | Wrap the final dimension biome source without adding TerraBlender to CakeWorld. | Main | 7 | Vanilla and a TerraBlender/fixture source both load; OreSpawn applies its baked overlay after delegation. |
| OS-053 | Default aquifer fluid | Use lemonade aquifers where water would normally form in the Overworld. | Main | 2 | New aquifer cavities contain the registered lemonade fluid block. |
| OS-054 | Deep aquifer fluid and threshold | Use deep hot fudge below the configured Y threshold. | Main | 2 | Samples immediately above and below the threshold use the correct fluids. |
| OS-055 | Snow material | **Shelved at runtime:** the profile retains `snow_block=cakeworld:icing_layer`, but neither the public chunk-load/world-tick routes nor a temporary direct handler diagnostic converted a top-of-column vanilla snow fixture. | Main | 1/3 | Re-enter after OreSpawn exposes/fixes the baked weather-material conversion and a real load/tick changes snow to icing across save/reload. |
| OS-056 | Ice material | **Shelved at runtime:** the profile retains `ice_block=cakeworld:frozen_lemonade`, but the same public and direct-handler diagnostics left a top-of-column vanilla ice fixture unchanged. | Main | 1/3 | Re-enter with OS-055 after CakeWorld and other integrated mods prove the uncommitted OreSpawn repair stable. |

### Ore placement

| ID | OreSpawn capability | CakeWorld example | Placement | Slice | Verification expectation |
|---|---|---|---|---|---|
| OS-057 | Stable provider-owned ore IDs | Give every resource a namespaced rule ID independent of its output block ID. | Main | 2 | Rule IDs remain stable through an output-block revision and the world snapshot merges correctly. |
| OS-058 | Single block output | Use one Rock-Candy Diamond output for its ordinary rule. | Main | 2 | Every successful placement uses the declared block. |
| OS-059 | Weighted outputs | Mix ordinary and rich Sprinkle Cluster variants or flavour colours from one rule. | Main | 2 | Large fixed-seed counts match declared weights within tolerance. |
| OS-060 | Deep output | Change vanilla-compatible resources to explicit deep CakeWorld ore blocks below the threshold. | Main | 2 | Adjacent samples above and below the cutoff use normal and deep outputs while keeping the same resource role. |
| OS-061 | Source-mod ownership metadata | **Shelved as an ownership gate:** CakeWorld retains descriptive `source_mod` values, but OreSpawn 4.0.1 does not consult them when baking placement or takeover state. | Main/BaseMetals | 2 | Re-enter after OreSpawn validates the owning mod and complete rule set before enabling takeover; metadata acceptance alone is not behavioural proof. |
| OS-062 | Native-generation suppression contract | Disable native vanilla/BaseMetals placement only after OreSpawn takeover is confirmed. | Main/BaseMetals | 2 | Chunk counts show one generation source, while an invalid profile falls safely back to native ownership. |
| OS-063 | OS3-style ordinary-dimension selector | Demonstrate `orespawn:all_except_nether_end` with a harmless sprinkle resource. | Sampler | 7 | Fixture custom dimensions receive the rule; Nether and End do not. |
| OS-064 | Explicit dimension override | Override or explicitly disable a selector-derived rule in one named dimension. | Sampler | 7 | The named dimension follows the explicit entry with no duplicate selector placement. |
| OS-065 | Fixed quantity | Use exact-budget Mint Crystal pockets. | Main | 2 | Instrumented placements never exceed or under-run the configured successful-block budget except for unavailable hosts. |
| OS-066 | Inclusive quantity range | Use variable Liquorice Veins with both minimum and maximum declared. | Main | 2 | Observed budgets include both boundaries and never fall outside 1-64. |
| OS-067 | Frequency as expected attempts per chunk | Make common Cocoa Clouds and rare Fizzy Pearls visibly distinct. | Main | 2 | Multi-chunk attempt counts fall within documented statistical tolerance. |
| OS-068 | Uniform height distribution | Use evenly distributed Sprinkle Clusters. | Main | 2 | Histogram is approximately flat across the configured range. |
| OS-069 | Triangle height distribution | Use a Rock-Candy resource range centred on its actual metamorphic hosts. | Main | 2 | A fixed-seed three-band histogram peaks in the middle third and tapers toward both ends across save/reload. |
| OS-070 | Bottom-triangle height distribution | Concentrate a precious resource toward the bottom of its range. | Main | 2 | Histogram strongly favours lower Y without escaping the range. |
| OS-071 | Uniform-bottom-triangle height distribution | Use a mixed distribution for varied Cocoa or Mint deposits. | Main | 2 | Histogram shows both uniform coverage and a lower-Y bias. |
| OS-072 | Air-exposure discard chance | **Shelved as strict showcase proof:** production rules retain their declarative chances, but the full baked acceptance predicate is private to OreSpawn's compiled pattern context. | Main | 2 | Re-enter when a public controlled-host fixture can exercise 0, partial, and 1.0 values without calling internal generation code. |
| OS-073 | Host geology family | **Shelved as strict negative proof:** production resources still use family hosts, but final blocks discard their original host and placement provenance. | Main | 2/4/5 | The public sampler agreed for 2,432/2,441 representative outputs; nine Cocoa/Liquorice cells could be later overlap or a real mismatch and cannot be classified honestly without a public controlled-host fixture. |
| OS-074 | Explicit host blocks | Mint Crystal targets Peppermint Rock where family matching would be too broad. | Main | 2 | The fixed revision-10 world found 25 Mint outputs and the public pre-ore sampler attributed all 25 to Peppermint Rock, with zero violations. |
| OS-075 | Host tags | Use CakeWorld and Forge replaceable tags to preserve pack extensibility. | Main | 2 | A tagged fixture block becomes eligible without changing the provider file. |
| OS-076 | Weighted host blocks and tags | Bias a diagnostic rule between two eligible host sources. | Sampler | 7 | Successful host selections approximate their weights. |
| OS-077 | Biome include/exclude IDs | **Shelved:** intended to allow Sprinkle Clusters in Candy Plains and exclude protected Cookie Forest. OreSpawn 4.0.1 currently resolves ore-filter IDs to Forge-registry biome objects, but generation receives dynamic-registry biome instances and compares by identity. | Main | 2 | Re-enter after OreSpawn compares stable biome keys: included biome places, excluded biome does not, and neutral control does not place. CakeWorld revision 10 proved the current include rule bakes but produces zero output; the normal profile therefore leaves Sprinkle unfiltered rather than claiming a broken example. |
| OS-078 | Forge biome-dictionary filters | **Shelved:** OreSpawn 4.0.1 expands dictionary categories into the same Forge-registry `Biome` object sets used by OS-077, then compares them by identity with dynamic-registry biome instances during ore generation. | Sampler | 7 | Re-enter with OS-077 after OreSpawn compares stable biome keys or holders. The eventual fixture must place across two matching registered biomes and not place in a non-matching control. |
| OS-079 | Geome filters and weights | Favour flavour-specific deposits; the representative integrated control gives Mint Crystal a 5.0 Peppermint Fold weight and a 0.0 Cocoa Basin weight. | Main | 2 | The fixed world retained the baked weights, placed 25 Mint Crystals in Peppermint Fold, and placed none across 51 Cocoa Basin control chunks while all broader geology and placement tests remained green. |
| OS-080 | Default pattern | Use the standard compiled default for an ordinary starter resource. | Main | 2 | Fixed-seed snapshot and block budget match the documented baseline. |
| OS-081 | Vein pattern | Use Liquorice Veins. | Main | 2 | Deposits are connected, elongated, and stay within configured budgets. |
| OS-082 | Normal-cloud pattern | Use broad Cocoa Clouds. | Main | 2 | Deposits form diffuse three-dimensional clouds with the expected density. |
| OS-083 | Precision pattern | Use small, exact Mint Crystal placements. | Main | 2 | Shapes and successful-block budgets are reproducible at fixed seed. |
| OS-084 | Cluster pattern | Use Sprinkle Clusters. | Main | 2 | Several compact local clusters form per placement attempt. |
| OS-085 | Under-fluids pattern | Put Fizzy Pearls under lemonade and soda-floor surfaces. | Main | 2/3 | The registered Lemonade fluid resolves through the public pattern API; integrated placements occur below it and not under an air control. |
| OS-086 | Public custom-pattern registry | Register `cakeworld:layer_cake` as an experimental `OrePatternType`. | Sampler | 7 | Codec decodes once, compiler runs while baking, and only the compiled placer runs during generation. |
| OS-087 | Custom pattern settings JSON | Give `layer_cake` explicit layer count, radius, thickness, and flavour-output settings. | Sampler | 7 | Valid settings round-trip; invalid bounds fail validation with a useful path. |
| OS-088 | Custom-pattern performance decision | Keep, simplify, or shelve `layer_cake` based on clarity and benchmark evidence rather than novelty alone. | Sampler | 7 | Ledger records benchmark, comparison pattern, decision, reason, commit, and test evidence. |

### Fluid deposits

| ID | OreSpawn capability | CakeWorld example | Placement | Slice | Verification expectation |
|---|---|---|---|---|---|
| OS-089 | Provider-owned multi-dimension fluid deposit | Five stable rules declare jam, custard, caramel, and syrup in the Overworld plus Hot Fudge in the Nether. | Main | 2/4 | Revision 11 baked five definitions across two dimensions; the fixed normal world gained 3,383 Hot Fudge cells over the revision-10 baseline and retained exactly 121,073 after reload. The BaseMetals template retained 121,068 plus all ten enabled counterparts after reload. |
| OS-090 | Fluid Y range and frequency | Separate shallow custard pockets from deep jam or hot-fudge reservoirs. | Main | 2/4 | No blocks occur outside range; attempt statistics match rarity classes. |
| OS-091 | Horizontal and vertical radius | Use broad shallow syrup lenses and compact deep jam bulbs. | Main | 2 | Fixed-seed sections visibly distinguish both geometries. |
| OS-092 | Multi-lobe geometry | **Shelved as exact integrated proof:** the adventure keeps configured multi-lobe caramel and jam reservoirs, but merged final fluid components have no lobe or placement provenance. | Main | 2 | Re-enter when a deterministic public fixture or off-hot-path trace exposes the chosen lobe count; cover, shell, bounds, bake, and generated output remain separately verified. |
| OS-093 | Solid cover | Keep covered jam reservoirs concealed until found. | Main | 2 | Too-shallow fixtures reject; deposits meeting minimum cover generate. |
| OS-094 | Solid shell | Prevent exposed or leaking fluid bodies where a shell is required. | Main | 2 | Shell inspection finds the declared minimum except at an intentional access feature. |
| OS-095 | Fluid host blocks and tags | Restrict Jam through an explicit Wafer Rock host plus a family, and Custard through the edible-host tag. | Main | 2/4 | The active snapshot retains both host forms, the rules bake, and generated reservoirs replace eligible geology; strict negative-host proof resumes when the public API exposes a compiled acceptance predicate or deterministic deposit fixture. |
| OS-096 | Fluid biome filters | Start Jam only from Cookie Forest chunks and exclude Syrup starts from Soda Ocean chunks. | Main | 2/3 | Fixed-seed surveys find allowed output and no excluded starts; final lobes may cross underground biome-cell boundaries after an accepted surface-biome start. |
| OS-097 | Fluid geome weights | Bias covered deposits by flavour geology; Custard provides the current measurable Wafer Shelf example, with Jam/Cocoa and future hot-fudge/Fudge-Mantle tuning retained as later flavour goals. | Main | 2/4 | With baked weights 3.0 for Wafer Shelf and 1.2 for Cocoa Basin, the fixed survey found 1,850 Custard blocks across 51 Wafer chunks versus 250 across 30 Cocoa chunks; all bodies also passed the solid-envelope audit. |

### Intrusive diagnostics

| ID | OreSpawn capability | CakeWorld example | Placement | Slice | Verification expectation |
|---|---|---|---|---|---|
| OS-098 | Aliases and renamed IDs | Include a controlled old-to-new CakeWorld rule-ID migration. | Sampler | 7 | Old snapshot resolves to the new stable owner without duplicating generation. |
| OS-099 | Retrogen | Add one brightly identifiable diagnostic sprinkle rule to already-generated test chunks. | Sampler | 7 | Copied test world changes only eligible old chunks/rule IDs; normal adventure keeps retrogen off by default. |
| OS-100 | Flat bedrock | Use a labelled sampler quadrant/dimension to prove floor and ceiling controls. | Sampler | 7 | Boundary scan confirms the configured flat layers and no adventure profile changes. |
| OS-101 | Extreme formations | Put minimum and maximum sensible formation presets beside the normal profile. | Sampler | 7 | Visual section and benchmark distinguish them without exhausting generation time or memory. |

## Main-World Feature Map

The ordinary adventure deliberately demonstrates the complete useful placement
vocabulary:

| Feature family | CakeWorld examples |
|---|---|
| Rock families | Wafer, biscuit, and sponge sedimentary strata; nougat and rock-candy metamorphic strata; peppermint igneous intrusions; fudge and burnt-sugar volcanic strata. |
| Geomes | Cocoa Basin, Wafer Shelf, Peppermint Fold, Rock-Candy Uplift, Fudge Mantle, Meringue Crust. |
| Terrain replacement | Complete replacement in Overworld, Nether, and End, delivered in slices 2, 4, and 5. |
| Biome placement | Replace palettes, weighted climates, similar-biome hints, and all five surface fields. |
| World materials | Lemonade aquifers, deep hot-fudge aquifers, icing snow, and frozen-lemonade ice. |
| Ore shapes | Compact Rock-Candy deposits, Liquorice Veins, Cocoa Clouds, precise Mint Crystals, Sprinkle Clusters, and Fizzy Pearls beneath fluids. |
| Placement controls | Every height distribution, fixed/ranged quantities, weighted outputs, deep outputs, host families, host blocks, host tags, biome filters, dictionary filters, and geome filters. |
| Fluid deposits | Covered jam, custard, caramel, syrup, and hot-fudge bodies with dimension-specific geometry. |
| Compatibility takeover | Themed vanilla ores and the optional thirteen-ore BaseMetals contract while preserving tags, drops, recipes, and native-generation safety. |

Features that are intrusive, confusing to ordinary players, migration-only, or
primarily useful to OreSpawn developers belong in `sampler_platter`.

## Verification Programme

### Required configurations

| ID | Configuration | Required evidence |
|---|---|---|
| OS-TEST-001 | CakeWorld + supported OreSpawn only | Dependency load, packaged provider bake, automatic `edible_world` selection, fresh-world generation, and no BaseMetals registry warnings. |
| OS-TEST-002 | CakeWorld + OreSpawn + BaseMetals | Automatic `edible_world_basemetals` selection, all thirteen compatibility blocks present, enabled source ores converted once, and BaseMetals recipes/tags functional. |
| OS-TEST-003 | CakeWorld absent | OreSpawn and BaseMetals retain their independent behavior; no CakeWorld template or blocks are referenced. |
| OS-TEST-004 | `sampler_platter` explicit selection | Every labelled diagnostic plot generates and the template never wins automatic selection. |
| OS-TEST-005 | Dedicated multiplayer server | Two players keep separate Cookbook discoveries; chunks, profiles, entities, and effects synchronize; reconnect preserves state. |

### Required evidence classes

For each `OS-*` feature, the living ledger records these independently:

1. **Source evidence** — exact provider entry, API call, generated resource, or
   code location.
2. **Build evidence** — schema validation, data generation, unit/GameTest, and
   production build result.
3. **Runtime evidence** — focused game or dedicated-server observation.
4. **Integrated-world evidence** — fixed-seed generation in the appropriate
   real dimension, including save/reload when state or snapshots matter.

A source diff or passing build is not runtime proof. A runtime screenshot is
not statistical or save/reload proof. “Verified” requires all evidence relevant
to that feature and its acceptance scenario.

### World-generation gates

Each delivery slice that changes world generation must pass:

- fresh-world creation with the expected automatically selected template;
- fixed-seed surveys in every affected dimension;
- block/biome/geome counts against stated tolerances;
- save, stop, restart, reload, travel away, and generate new chunks;
- a copied existing-world non-conversion check;
- server and two-client multiplayer travel across chunk boundaries;
- valid pack-override and malformed-override checks;
- comparison of the active world snapshot before and after provider revision;
- generation timing and allocation benchmarks against the last accepted
  baseline;
- an exact list of mod versions, profile revision, seed, commands, and relevant
  commit in the ledger.

## Performance Contract

CakeWorld configuration stays declarative. OreSpawn must bake registry IDs,
tags, dimensions, fluids, climate ranges, surfaces, pattern settings, and
weights before generation.

No CakeWorld or provider callback, config read, JSON access, registry lookup,
tag lookup, string construction, logging, or avoidable allocation is permitted
inside terrain, biome-selection, ore-placement, or fluid-deposit inner loops.
`cakeworld:layer_cake` must compile its decoded settings once and expose only a
small allocation-free placement function to the hot path.

A clever feature that makes ordinary generation unstable, hard to reason
about, or materially slower may be moved to the Sampler, simplified, or
shelved. The ledger must preserve the benchmark and decision.

## Known Showcase Re-entry Conditions

`OS-013` is not safe to demonstrate in the normal adventure with the current
snapshot merge contract. Top-level `profile_defaults` contribute new IDs to
existing profiles, while CakeWorld promises that existing worlds are never
silently converted. The production provider therefore keeps all adventure
rules inside the auto-selected fresh-world template. Re-enter shared defaults
only in a copied-world Sampler migration fixture, or after OreSpawn adds a
creation-only/defaults scope that cannot alter an existing snapshot.

`OS-028` cannot currently give Cocoa Basin and Rock-Candy Uplift different
formation presets. OreSpawn 4.0.1 exposes one profile-wide `formations`
object, so horizontal size, thickness, waviness, edge irregularity, and
continuity apply to every geome in that profile. CakeWorld retains the stable
global defaults and the per-geome creative goal, but must not claim
per-geome-preset proof until OreSpawn supports baked geome-level overrides.

`OS-055` and `OS-056` remain valid declarative profile examples but are not
working runtime examples under the tested OreSpawn build. CakeWorld placed
vanilla snow and ice at the live motion-blocking top of a loaded player chunk.
A public chunk-load event, a public end-of-world tick, and one temporary
diagnostic invocation of OreSpawn's own handler all left both blocks
unchanged. The internal call was removed immediately; CakeWorld must not
publish it as an integration pattern. Re-enter only after OreSpawn's baked
weather materials are fixed or exposed through a supported test fixture.

`OS-061` remains useful descriptive metadata, but source inspection confirms
that OreSpawn 4.0.1 reads `source_mod` only in configuration, editor, and
migration paths. Ore placement and takeover bake from native-generation,
suppression, management, and output-block settings without validating that
the named source mod owns the rule. CakeWorld therefore must not present
metadata acceptance as a safe ownership gate.

`OS-072`, `OS-073`, and `OS-074` distinguish three different evidence
boundaries. Air-exposure rejection and full host acceptance run inside
OreSpawn's internal compiled placement context, which has no public
controlled-block fixture. Final output also loses its original host and
placement origin. A public-sampler comparison consequently left nine
unclassifiable family-host cells among 2,441 Cocoa Cloud and Liquorice Vein
outputs. By contrast, Mint Crystal's explicit-block rule had a sound
representative result: all 25 fixed-world outputs sampled Peppermint Rock and
none sampled another host. CakeWorld verifies that explicit example while
shelving stricter exposure and family-negative claims.

`OS-085` is currently blocked at integrated-world verification. OreSpawn's
under-fluids pattern samples only two blocks vertically from an ordinary
ore-placement origin, while the ore rule's `min_y` and `max_y` also bound the
pattern's descent to the fluid floor. A child provider therefore cannot bias
origins toward sea level while also allowing descent to substantially deeper
ocean floors. CakeWorld retains the declarative Fizzy Pearl example and a
public-registry compiler test, but it must not claim generated-world proof
until OreSpawn offers a surface/fluid-column anchor, an independent search
range, or an equivalent compiled hot-path-safe setting.

`OS-089` has independent source, bake, differential-world, reload, and
integrated-mod evidence. Revision 11 adds
`cakeworld:fluid_deposit/hot_fudge` only in the Nether, restricted to Fudge
Wastes, Fudge Mantle, and volcanic edible hosts from Y 16 through 112. Against
the otherwise identical fixed-seed revision-10 scan, Hot Fudge increased from
117,690 to 121,073 cells and retained that count after reload. The BaseMetals
template independently baked the same five deposits across two dimensions,
retained 121,068 cells after reload, and kept every enabled metal counterpart.

`OS-092` is implemented declaratively but cannot yet receive exact
integrated-world lobe-count proof from the child-mod API. Final chunks retain
the generated fluid and its solid envelope, but overlapping lobes and nearby
deposits merge into ordinary fluid components with no placement origin or lobe
provenance. CakeWorld can verify the configured maximum, successful bake,
generated output, cover, and shell; it must not infer an exact lobe count from
the merged component. Re-enter strict lobe-count verification when OreSpawn
offers a deterministic placement fixture or optional diagnostic placement
trace that is absent from the generation hot path.

`OS-095` and `OS-096` deliberately distinguish rule acceptance from final
block location. Fluid host blocks, tags, and geology families are additive
eligible-host sets, not intersections. Biome filters are evaluated once from
the generating chunk's surface biome before the reservoir is shaped; they do
not clip every underground block to a three-dimensional biome cell. CakeWorld
therefore uses an explicit Wafer Rock plus metamorphic-family Jam rule, a
tag-hosted Custard rule, a Cookie Forest Jam allow-list, and a Soda Ocean Syrup
deny-list. Fixed-seed generation and save/reload prove the baked filters
produce and suppress starts as intended.

Strict `OS-095` negative-host proof is shelved at the child-mod boundary.
OreSpawn 4.0.1 supports only `zone.moddev.mc.orespawn.api`; that API exposes
declarative fluid definitions, the active profile, and read-only geology
sampling, but not a compiled deposit acceptance predicate or deterministic
placement fixture. Calling `worldgen.FluidDepositFeature` directly would
couple this example mod to an unsupported implementation package. Final chunks
also contain no pre-placement host or lobe provenance, so CakeWorld must not
infer a negative control from neighbouring final blocks. Re-enter when the
public API can run a baked deposit against a controlled eligible/ineligible
host grid, or expose an equivalent off-hot-path deterministic fixture.

`OS-025` uses sparse Candy Glass as the production non-replaceable control.
The public sampler predicts the rock before final chunk materialization, then
the integrated audit compares that prediction with the resulting block and
all OreSpawn-managed ore outputs. This proves the setting from baked profile
through final terrain without making ordinary generation perform diagnostic
work. Candy Glass also remains outside CakeWorld's general ore-host tag.

`OS-069` keeps the triangle distribution itself unchanged and aligns its
declared vertical range with the Rock-Candy-bearing metamorphic host band.
Fixed-seed three-band sampling must show a genuine middle peak, and the exact
histogram must survive save/reload before the example is considered verified.

`OS-032` demonstrates that exact biome-ID and Forge biome-dictionary weights
are additive. CakeWorld registers explicit dictionary types for its biomes
during common setup, adds COLD Peppermint and HOT Fudge weights, and keeps a
MUSHROOM rule which intentionally matches none of the current CakeWorld
biomes. OreSpawn's built-in dictionary defaults also apply to registered
types, so the exact CakeWorld biome weights remain deliberately stronger than
generic MOUNTAIN, FOREST, or OCEAN tendencies. Fixed-seed sampling must retain
both Rock-Candy Uplift and Peppermint Fold in Marshmallow Peaks while Fudge
Wastes remains dominated by Fudge Mantle.

## Original Scaffold: Verified Prototype Evidence

This table records the verified initial scaffold before the delivery slices.
It proves that the integration direction worked; it is not a description of
the later wishlist implementation state.

| Prototype | Current evidence | Product status |
|---|---|---|
| Registered CakeWorld content | Six blocks, two fluids, and six copied biomes load with their resources. | Verified prototype; palettes, behavior, art, recipes, and content breadth remain wishlist work. |
| Schema-4 provider | A packaged provider declares five edible rocks and one template with terrain replacement, replace-mode biome palettes, and dimension materials. It does not yet declare ore rules or fluid deposits. | Verified prototype; it is a small demonstrator, not the canonical final profile. |
| Three-realm integration | CakeWorld biomes and edible materials were observed in Overworld, Nether, and End. | Verified prototype; only an introductory subset exists. |
| Persistence | The integration survived save and reload in the verified prototype world. | Verified prototype; formal snapshot migration coverage remains open. |
| Tooling | Production build and Forge run-generation tasks passed for the verified scaffold. | Verified prototype; every later slice needs fresh evidence. |

The exact commands, seed, measurements, known shutdown issue, and commit
evidence belong in the ignored local feature ledger rather than this public
design contract.

## Completion Rule

The OreSpawn showcase is complete only when every `OS-*` row is either:

- implemented and verified with the required evidence;
- deliberately placed in the Sampler and verified there; or
- shelved/rejected with a durable technical or design reason.

Passing the happy-path adventure alone does not complete CakeWorld's OreSpawn
validation objective.
