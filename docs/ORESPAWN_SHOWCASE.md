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
settings cannot drift. Provider revision 54 packages two adventure templates
and the explicitly selected diagnostic Sampler. The build gives the Sampler an
exact copy of the adventure's rocks, geomes, biome rules, dictionary rules, and
terrain dimensions before applying its diagnostic-only settings. Its augment,
namespace-filter, optional/required similarity, five-size region boundaries,
alternate extreme formation profile, output alias, three-layer flat-bedrock,
copied-world retrogen profile, and unrelated-source composition plot are
verified; the remaining labelled Slice 7 plots are still wishlist work.

The current integration target is the published Minecraft 1.18.2 OreSpawn
`4.0.6.118021` release from CurseForge project `245586`, file `8688546`.
The pinned raw artifact has SHA-256
`670b0401df84a19c485d261f4dd355b52a94c04a1e3d503a8b6e824bec1e7e9b`.

| ID | Template | Selection | Purpose |
|---|---|---|---|
| OS-001 | `cakeworld:edible_world` | Automatically selected for a fresh world when CakeWorld is installed and no higher-priority compatible template or explicit global default wins. | The normal adventure. Uses only features that improve the playable total conversion. |
| OS-002 | `cakeworld:edible_world_basemetals` | Higher priority than `edible_world`, requires both `cakeworld` and `basemetals`, and is eligible only for fresh-world automatic selection. | The same adventure with CakeWorld counterparts for every supported BaseMetals resource. |
| OS-003 | `cakeworld:sampler_platter` | Never auto-selected; chosen explicitly by developers or pack makers. | **In progress:** revision 54 retains nine verified biome plots, the profile-wide `sky_v1` extreme-formation comparison, an installed-output alias, three-layer flat bedrock and inert retrogen fixtures while inheriting the adventure geology definitions exactly at build time. Fresh and same-save reload retained the established biome/formation results, baked 512 aliased Sprinkle samples with zero original Burnt-Sugar samples, and retained exact `256/256/256/0/0` floor/ceiling layer counts. Separate isolated cycles verified bounded retrogen, a generated all-`tiny` minimum-preset profile, and unrelated-source composition without adding a packaged template or production dependency. Remaining intrusive test rules and compact hands-on inspection remain later plots. |

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
| OS-007 | Immutable definitions and bake-time registry resolution | Store registry IDs declaratively and let OreSpawn validate and bake holders, tags, fluids, settings, and weights. The public active-profile view rejects collection mutation and returns detached diagnostic JSON. | Main | 1 | A dedicated-server test cannot mutate ID collections or the active profile through returned JSON; the unchanged profile then generates its expected rocks, ores, fluids, surfaces, and biomes. Missing optional and required similarity IDs are isolated under OS-045/046. |
| OS-008 | Active-profile query | **Verified:** `/cakeworld orespawn` calls only `OreSpawnApi.getActiveProfile(server)` and renders the selected template, geology mode and typed-view counts for rocks, geomes, ores, fluid deposits, biome palettes and terrain dimensions. | Sampler | 7 | A permission-zero focused fresh-world GameTest compared every rendered argument to the released immutable view, executed the registered command successfully and saved all dimensions. The command has no reload, retrogen or mutation path. |
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
| OS-018 | Pack override precedence | **Verified:** released OreSpawn 4.0.6 treats `config/cakeworld-orespawn.json` as CakeWorld's authoritative provider declaration. An isolated revision-4701 fixture replaced packaged revision 47, selected only `cakeworld:override_probe`, and exposed no packaged CakeWorld rocks or palettes. Removing the file did not silently rewrite that existing world's saved selection; a separate fresh world without the override selected packaged `cakeworld:edible_world`. | Main | 7 | Valid override replaces the packaged declaration; same-save removal retains the world-owned profile, while a new world restores packaged behavior. The tracked harness refuses accidental reuse and removes only its isolated override fixture. |
| OS-019 | Malformed override fail-closed behavior | **Verified:** an intentionally truncated `config/cakeworld-orespawn.json` produced one unique actionable EOF validation error (reported during both Forge provider-discovery passes), left CakeWorld's provider inactive, selected no CakeWorld template, exposed no CakeWorld rocks or palettes, and completed world creation using OreSpawn's vanilla-stone fallback. | Sampler | 7 | A dedicated server continues safely without CakeWorld takeover terrain. Never repair a malformed override by silently falling back to the packaged declaration. |
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
| OS-027 | Alternate or legacy geology mode | **Verified:** keep `stable_layers` with average presets in the adventure and expose `sky_v1` only through the explicitly selected Sampler. Because formations are profile-wide, the honest comparison uses separate same-seed world-owned profiles over identical CakeWorld geology rather than claiming adjacent algorithms in one world. | Sampler | 7 | Fixed seed `5059928472718672684` produced exact stable `5,393/634/7/5/6,720,209,891,956,171,365` and Sampler `36,515/1,176/7/14/2,804,912,711,855,593,311` vertical transitions/horizontal transitions/distinct rocks/distinct geomes/signature. Fresh and reload retained both signatures without changing the adventure default. |
| OS-028 | Formation presets | Demonstrate preset horizontal size and waviness with broad Cocoa Basins and compact Rock-Candy Uplifts. | Main | 2 | Profile dump reports the presets and fixed-seed sections show materially different shapes. |
| OS-029 | Custom formation values | **Verified:** the Sampler declares all five controls as `custom` under `sky_v1`, with bounded `32` stratum wavelength, `8,192` family-region wavelength, thickness `1`, amplitudes `512/256`, edge wavelength `8`, eight octaves, zero continuity, and a retained `32` stable waviness wavelength for algorithm swaps. | Sampler | 7 | Packaged, active, and saved profiles retain every value. A 70,785-point public-API survey reproduced its exact signature on fresh/reload and completed without a generation timeout; ignored-by-`sky_v1` stable-edge fields remain serialized settings, not falsely claimed live inputs. |
| OS-030 | Geomes | Define Cocoa Basin, Wafer Shelf, Peppermint Fold, Rock-Candy Uplift, Fudge Mantle, and Meringue Crust. | Main | 2/4/5 | The sampler API can identify every geome in a fixed-seed survey. |
| OS-031 | Geome rock weights and vertical identity | Give each geome a recognisable palette rather than merely renaming the same strata. | Main | 2 | Block counts and column samples distinguish all six canonical geomes. |
| OS-032 | Biome rules and biome-dictionary matching | Bias geomes using exact CakeWorld biome IDs plus Forge dictionary identities. Revision 46 retains every earlier rule and adds Fondant Chorus Gardens at exact `12/8` Meringue-Crust/Rock-Candy-Uplift bias with END, VOID, MAGICAL and LUSH identity. | Main | 2/4/5 | Direct tests prove all thirty-four exact biome contracts. Fixed seed `5059928472718672684` selected End Highlands into Fondant Chorus Gardens and retained independent Wafer, Rock-Candy, Nougat and Meringue geology; declarative geome weights are not inferred from one bounded region. |
| OS-033 | Terrain-replacement dimension controls | Enable complete replacement independently in Overworld, Nether, and End. | Main | 2/4/5 | Fresh chunks in all three dimensions contain no unintended ordinary base geology within the declared scope. |

### Biome overlay and world materials

| ID | OreSpawn capability | CakeWorld example | Placement | Slice | Verification expectation |
|---|---|---|---|---|---|
| OS-034 | Forge biome registration helper | Use `copyAndRegister` for readable source-compatible definitions. The thirty-three earlier biomes retain their copied vanilla generation state; Fondant Chorus Gardens adds the thirty-fourth definition by copying End Highlands and replacing climate, ambience and spawn roles. Use `blankAndRegister` only for intentionally complete advanced definitions. | Main | 1/2/3/4/5 | Direct registry tests prove all thirty-four copied climates, including seven Nether and seven End outputs. Fixed fresh/reload generation selected Fondant through the live overlay and retained its exact-save identity around the natural Carousel at `804,60,791`. |
| OS-035 | Replace placement mode | Make the three vanilla dimensions wholly edible in the main adventure. | Main | 1/3/4/5 | Eligible source regions select only CakeWorld biomes with zero fallback weight. |
| OS-036 | Augment placement mode | **Verified:** the explicit Sampler adds Candy Plains while retaining the delegated vanilla source. Its active palette is `augment`, `minecraft_only`, `tiny`, `coverage=0.75`, and `fallback_weight=3.0`. | Sampler | 7 | Revision 47 isolated 1,414 Candy Plains and 64,635 direct Minecraft fallbacks across 66,049 positions. Revision 50 retains exactly 1,414 Candy results; its later labelled boundary palettes deliberately transform part of the fallback, leaving 3,414 final Minecraft positions. Fresh/reload reproduce every final count. |
| OS-037 | `minecraft_only` replacement scope | **Verified:** six Sampler Overworld plots use `minecraft_only`. An opt-in test owner installs `cakeworld_fixture:delegated_meadow` as the complete source before OreSpawn wraps it, so none of those six plots may capture it. | Sampler | 7 | The final explicit fixture plot observed only 1,736 unchanged fixture results and 2,489 deliberate Candy-Plains selections across 4,225 fresh/reload samples. No output from an earlier Minecraft-only plot appeared. An ordinary Sampler asserts the fixture ID is not registered. |
| OS-038 | `selected_namespaces` replacement scope | **Verified:** the Sampler's Nether plot uses the explicit `minecraft` allow-list; its fixture plot independently uses the explicit `cakeworld_fixture` allow-list with half coverage. | Sampler | 7 | The Nether plot consistently converts 1,511 Nether-Wastes samples while delegating 2,714 other vanilla samples with no eligible output. The fixture plot reproducibly converts 2,489 selected-namespace positions and delegates 1,736 uncovered positions, with no third result. |
| OS-039 | `all` replacement scope | Every adventure palette uses `scope=all`; CakeWorld itself is excluded to prevent repeat replacement. | Main | 1/3/4/5 | The active profile retained all-scope replacement, while fixed Nether and End regions were 6,400/6,400 CakeWorld surface columns and the sampled Overworld exposed every current target biome. OS-052 separately proves wrapping and delegation against an unrelated fixture source. |
| OS-040 | Namespace include and exclude lists | **Verified:** the Nether plot's `include_namespaces=[minecraft]` targets vanilla source biomes, while the End plot's `scope=all` and `exclude_namespaces=[minecraft]` carves them out without a callback. | Sampler | 7 | Revision 48 isolated the lists by converting all 4,225 included Nether positions and delegating all 4,225 excluded End positions. Revision 49 retains both lists while similarity deliberately narrows the Nether output to 1,511 positions; End exclusion remains exact across five vanilla IDs with zero Meringue output. |
| OS-041 | Region sizes | **Verified:** revision 50 adds five labelled Overworld boundary plots using Gingerbread Hearthlands, Peppermint Pinewoods, Marshmallow Peaks, Cookie Forest and Soda Ocean for `tiny`, `small`, `average`, `large` and `huge`. | Main/Sampler | 3/7 | Packaged and active profiles retain exact 128/256/512/1,024/2,048-block settings. Fixed fresh/reload sampled `4,225/1,089/289/81/25` aligned regions, observed outputs in `2,119/468/117/29/7`, observed delegated regions at every size, and found zero output crossing into a later-stage or vanilla result inside its own aligned boundary. |
| OS-042 | Coverage | **Verified declaration and integrated result:** all five production palettes retain `coverage=1.0`; the augment plot persists `coverage=0.75` and every labelled size plot uses `coverage=0.5`. | Main/Sampler | 1/7 | Main fixed-world output proves full replacement. Revision 50 fresh/reload observe both covered and delegated aligned regions at all five sizes. Coverage and earlier palette outputs interact in the effective distribution, so counts are evidence of deterministic boundaries rather than a claim that final biome area equals the numeric percentage. |
| OS-043 | Fallback weight | **Verified declaration and integrated result:** production palettes retain `fallback_weight=0.0`; the explicit Sampler bakes and persists a positive `3.0` delegated-source weight. | Main/Sampler | 1/7 | Main replacement retains zero fallback. The same fixed Sampler survey proves positive fallback keeps vanilla output live and reproduces the exact result on reload; its area ratio is not a direct interpretation of the numeric weight. |
| OS-044 | Weighted output biomes | Retain all verified land, ocean, cave and Nether weights. Revision 46's End palette selects Cosmic Jelly Reefs at `0.6`, Macaron Archipelago at `0.75`, Fondant Chorus Gardens and Meringue Islands at `1.0`, Starlight Sugar Fields at `1.25`, Mooncake Barrens at `1.5` and Candyfloss Cloudbanks at `2.0`. | Main | 2/3/4/5 | Normal and BaseMetals templates are generated from one canonical profile and differ only by thirteen compatibility ores. Direct tests prove all seven End entries and weights; weights affect overlapping candidates and are not global area-ratio promises. |
| OS-045 | Similar-biome matching | **Verified, including the missing-optional case:** thirty-four adventure rules retain their optional exact hints, and the Sampler Fudge output now lists both real `minecraft:nether_wastes` and absent `cakeworld:missing_optional_sampler_source`. | Main/Sampler | 1/2/3/4/5/7 | Released 4.0.6 silently ignores the missing optional member, keeps the resolved Nether-Wastes match active, and fresh/reload each convert the same 1,511 samples. The strict companion remains isolated under OS-046. |
| OS-046 | Required-similar-biome matching | **Verified:** the competing Chilli-Chocolate-Crags Sampler output requires absent `cakeworld:missing_required_sampler_source`. | Sampler | 7 | Fresh and reload each emit exactly one actionable bake warning, disable only the strict output, produce zero Crags across 4,225 Nether samples, and retain the valid Fudge output plus delegated source biomes. |
| OS-047 | Temperature and downfall ranges | The thirty-three earlier selectors retain their source-inclusive or deliberately narrow climate windows. Fondant Chorus Gardens adds the thirty-fourth selector with the complete End-Highlands source range (`-2.0..2.0` temperature, `0.0..1.0` downfall) mapped to registered `0.5/0.0`. | Main | 1/2/3/4/5 | Revision 46 direct tests retain all thirty-four selector contracts. Fixed fresh/reload selected Fondant through its declared source window; exact boundary fixtures remain the completion gate for the showcase as a whole. |
| OS-048 | Surface top block | Every surface biome has a distinct registered top material while the three underground biomes deliberately omit `surface`. Revision 46 adds nibbleable, fall-softening Pastel Fondant beside the six previously verified End signatures. | Main | 1/3/4/5 | Direct tests prove the declaration and quarter-fall contract. Fixed fresh/reload retained `6,539` independent Pastel-Fondant cells, including `6,331` natural Fondant cells directly above configured Meringue-Foam filler. |
| OS-049 | Surface filler block and depth | Every surface output declares an edible filler and exact depth. Revision 46 adds four-deep Meringue Foam beneath Pastel Fondant alongside the six earlier End filler contracts. | Main | 1/3/4/5 | Direct tests prove the exact four-block declaration. Fixed fresh/reload retained `6,331` natural Fondant-over-Meringue-Foam surface cells and `25,533` Meringue-Foam cells in the bounded garden region; counts are observations, while depth four is the exact contract. |
| OS-050 | Underwater surface block | Retain the twenty verified wet-edge declarations and add Candy Glass beneath Fondant Chorus Gardens, keeping underwater presentation explicit even in an ordinarily dry End source family. | Main | 3/5 | Revision 46 retains all twenty-one live fields and every cave omission. This declaration is source/runtime-bake evidence; earlier wet-world fixtures retain behavioural proof and OS-056 remains separately shelved. |

Rock-Candy Caverns, Jam Grottoes and Nougat Depths deliberately declare no
`surface` object.
OreSpawn's surface fields describe the exposed top of a world column, not
internal cave floors, walls or ceilings. All three cave biomes demonstrate
three-dimensional replacement and geome selection without presenting a
top-column material as cave-surface support.
| OS-102 | Surface-before-child-feature ordering | OreSpawn 4.0.6 applies provider-owned exposed-ground surfaces during `LOCAL_MODIFICATIONS`, before CakeWorld's ordinary decoration features. CakeWorld landmarks use bounded, one-chunk searches, accept declared natural edible support, fill only clear gaps and reject fluids, block entities, ores, structures and authored solids. Fondant Chorus Carousel adds the seventh End case: a seven-by-seven directed-blink teaching garden inside a nine-by-nine safety envelope, with no entity, block entity, inventory, marker, repair or replay path. | Main | 2/3/4/5 | Published `4.0.6.118021` (CurseForge file `8688546`) remains the tested dependency. Revision 46 direct tests cover all rotations, relief, support, fluid, block-entity, obstacle and containment controls. Fresh found the Carousel at `804,60,791`, `NONE`, with exact `49/20/4/9/16/16/8/4/1/1` identity amid `847,313/6,539/6,331/25,533/51,552/117,310/52,267/24,032/475/221/0` biome/Fondant/Fondant-over-filler/Meringue/Biscuit/Wafer/Rock-Candy/Nougat/natural-Stem/natural-Bloom/vanilla-Chorus observations; reload retained the same natural counts and `49/20/4/9/16/16/8/3/1/1+Brick`. No OreSpawn change was required. |
| OS-103 | Biome-output vertical selector | **Shelved:** OreSpawn 4.0.6.118021 biome-palette outputs expose climate, similarity, namespace, geome and weight controls, but no minimum/maximum Y selector. Nougat Depths therefore competes honestly with Rock-Candy Caverns for Dripstone-Caves sources at any height; only its landmark and geology evidence are restricted to deep Y. | Main | 2 | Re-enter after the public schema/API and compiled selection path support bounded output Y without registry lookups or allocation in the hot loop. Prove adjacent fixtures above and below each cutoff, fresh/reload persistence, weighted overlap and unchanged delegated-source behaviour before claiming a depth-only biome. |
| OS-104 | Vanilla spring compatibility with child-mod rock | Use Minecraft's real configured Water Spring inside a Wafer-Rock fixture, with ordinary Bricks as an ineligible negative control. CakeWorld calls no OreSpawn implementation package; this is a public behavioural integration test of OreSpawn's baked host extension. | Main | 2/4 | The direct test requires the loaded OreSpawn version to be exactly `4.0.6.118021`, proves the real `minecraft:spring_water` configured feature succeeds in the valid Wafer-Rock shell and refuses Bricks, then fixed-seed fresh generation creates the source at `28672,40,28672`. Same-save reload retains the source, complete Wafer shell and a player Brick sentinel. |
| OS-051 | Ceiling surface block | **Shelved:** OreSpawn 4.0.1 documents an underside material, but its surface feature scans downward from build height and replaces the first solid block—the inaccessible upper face of the Nether roof—not a player-facing ceiling underside. | Main | 2/4 | Re-enter after OreSpawn targets solid blocks exposed to air or fluid below without replacing structures. A revision-11 diagnostic marker appeared on 20,716/20,736 topmost columns and 0/29,607 downward-exposed ceiling faces, so the accepted adventure does not claim this as a working example. |
| OS-052 | Delegated-source composition | **Verified:** conditionally install a fixed test-only Overworld source under the unrelated `cakeworld_fixture` namespace at highest-priority level load, then let released OreSpawn wrap the final source normally. CakeWorld adds no TerraBlender or production source hook. | Sampler | 7 | Fresh/reload each retained exact 1,736 delegated fixture and 2,489 selected Candy-Plains results across 4,225 samples. The fixture source installed before OreSpawn's wrap, while ordinary Sampler and normal adventure runs kept the fixture registry ID absent and retained their established outputs. |
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
| OS-060 | Deep output | The Mint-Crystal rule emits ordinary Mint Crystal above Y `-24` and Rock-Candy Deposit at and below that threshold. Nougat Depths gives the deep form a thematic home without attributing its three authored teaching samples to OreSpawn. | Main | 2 | Fixed-seed Nougat evidence found independent deep Rock-Candy outputs after excluding every cell around the authored kitchen. Full verification still requires controlled adjacent samples above and below Y `-24` that keep the same resource role. |
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
| OS-073 | Host geology family | Production resources use family hosts; optional Starsteel demonstrates one End rule accepting sedimentary, metamorphic and intrusive families. **Strict per-cell negative proof remains shelved:** final blocks discard their original host and placement provenance. | Main | 2/4/5 | With BaseMetals absent the fixed Starlight audit found zero Starsteel; with it loaded the same region found `15` natural Starsteel cells and retained all `15` on reload. This proves the baked family-hosted rule produces integrated output, not which family each final cell replaced. The earlier public sampler agreed for 2,432/2,441 representative outputs; unresolved overlap still prevents claiming a strict negative predicate. |
| OS-074 | Explicit host blocks | Mint Crystal targets Peppermint Rock where family matching would be too broad. | Main | 2 | The fixed revision-10 world found 25 Mint outputs and the public pre-ore sampler attributed all 25 to Peppermint Rock, with zero violations. |
| OS-075 | Host tags | Use CakeWorld and Forge replaceable tags to preserve pack extensibility. | Main | 2 | A tagged fixture block becomes eligible without changing the provider file. |
| OS-076 | Weighted host blocks and tags | Bias a diagnostic rule between two eligible host sources. | Sampler | 7 | Successful host selections approximate their weights. |
| OS-077 | Biome include/exclude IDs | **Shelved:** intended to allow Sprinkle Clusters in Candy Plains and exclude protected Cookie Forest. OreSpawn 4.0.1 currently resolves ore-filter IDs to Forge-registry biome objects, but generation receives dynamic-registry biome instances and compares by identity. | Main | 2 | Re-enter after OreSpawn compares stable biome keys: included biome places, excluded biome does not, and neutral control does not place. CakeWorld revision 10 proved the current include rule bakes but produces zero output; the normal profile therefore leaves Sprinkle unfiltered rather than claiming a broken example. |
| OS-078 | Forge biome-dictionary filters | **Shelved:** OreSpawn 4.0.1 expands dictionary categories into the same Forge-registry `Biome` object sets used by OS-077, then compares them by identity with dynamic-registry biome instances during ore generation. | Sampler | 7 | Re-enter with OS-077 after OreSpawn compares stable biome keys or holders. The eventual fixture must place across two matching registered biomes and not place in a non-matching control. |
| OS-079 | Geome filters and weights | Favour flavour-specific deposits. The representative integrated Mint control gives Peppermint Fold weight `5.0` and Cocoa Basin weight `0.0`; optional Starsteel adds positive Meringue-Crust `4.0` and Rock-Candy-Uplift `6.0` weights without using the shelved biome-filter seam. | Main | 2/5 | The Mint fixed world retained its baked weights, placed 25 crystals in Peppermint Fold and none across 51 Cocoa Basin control chunks. The BaseMetals fixed Starlight world baked the Starsteel weights, generated `15` natural cells amid an exact `8/12` Meringue/Rock-Candy biome bias and retained all `15` on reload. These observations prove integrated weighted-rule output without misrepresenting a bounded count as a selection-ratio measurement. |
| OS-080 | Default pattern | Use the standard compiled default for an ordinary starter resource. | Main | 2 | Fixed-seed snapshot and block budget match the documented baseline. |
| OS-081 | Vein pattern | Use Liquorice Veins. | Main | 2 | Deposits are connected, elongated, and stay within configured budgets. |
| OS-082 | Normal-cloud pattern | Use broad Cocoa Clouds. | Main | 2 | Deposits form diffuse three-dimensional clouds with the expected density. |
| OS-083 | Precision pattern | Use small, exact Mint Crystal placements. | Main | 2 | Shapes and successful-block budgets are reproducible at fixed seed. |
| OS-084 | Cluster pattern | Use Sprinkle Clusters. | Main | 2 | Several compact local clusters form per placement attempt. |
| OS-085 | Under-fluids pattern | Keep the declarative Fizzy Pearl rule as the public compiler example while a Wafer Reef Nursery supplies one clearly authored adventure treasure. | Main | 2/3 | The registered Lemonade fluid resolves through the public pattern API, but integrated OreSpawn output remains shelved. Fixed-world attribution must exclude complete authored Nurseries and must never present their Pearls as pattern evidence. |
| OS-086 | Public custom-pattern registry | Register `cakeworld:layer_cake` as an experimental `OrePatternType`. | Sampler | 7 | Codec decodes once, compiler runs while baking, and only the compiled placer runs during generation. |
| OS-087 | Custom pattern settings JSON | Give `layer_cake` explicit layer count, radius, thickness, and flavour-output settings. | Sampler | 7 | Valid settings round-trip; invalid bounds fail validation with a useful path. |
| OS-088 | Custom-pattern performance decision | Keep, simplify, or shelve `layer_cake` based on clarity and benchmark evidence rather than novelty alone. | Sampler | 7 | Ledger records benchmark, comparison pattern, decision, reason, commit, and test evidence. |

### Fluid deposits

| ID | OreSpawn capability | CakeWorld example | Placement | Slice | Verification expectation |
|---|---|---|---|---|---|
| OS-089 | Provider-owned multi-dimension fluid deposit | Six stable rules declare Jam, Custard, Caramel and Syrup in the Overworld plus Hot Fudge and Molten Mallow in the Nether. Revision 44 retains all seven CakeWorld Nether biomes in the one shared Hot-Fudge rule and the distinct `cakeworld:fluid_deposit/molten_mallow` rule available only in Molten-Marshmallow Calderas. | Main | 2/4 | Revision 44 bakes all six definitions across two dimensions. Direct tests retain the exact seven-biome Hot-Fudge allow-list and the independent Calderas-only Molten-Mallow filter. Fixed fresh/reload Fudge-Wastes evidence retained 42,264 Hot-Fudge cells outside its authored fountain; Burnt-Toffee retained 6,563, Black Liquorice 12,257, Treacle Soul Valleys 2,281 and Chilli-Chocolate Crags 85,739 in their independent audits; Calderas retained 29,977 Hot-Fudge cells plus 1,158 independently managed Molten-Mallow cells. The Jam-Grotto survey retained 1,960 managed Jam cells in the allowed Cookie-Forest region. |
| OS-090 | Fluid Y range and frequency | Separate shallow custard pockets from deep jam or hot-fudge reservoirs. Jam currently declares Y `-40..48` at frequency `0.12`. | Main | 2/4 | Direct tests retain the exact Jam declaration; fresh/reload runtime surveys find managed Jam output within the configured depth band. Attempt-level frequency statistics remain a later diagnostic. |
| OS-091 | Horizontal and vertical radius | Use broad shallow syrup lenses and compact deep Jam bodies; Jam declares horizontal radius `4..9`, vertical radius `2..4`, and at most three lobes. | Main | 2 | Direct tests retain the exact Jam radii. Fixed-seed fresh/reload surveys find the resulting managed output, while lobe provenance remains separately shelved under OS-092. |
| OS-092 | Multi-lobe geometry | **Shelved as exact integrated proof:** the adventure keeps configured multi-lobe caramel and jam reservoirs, but merged final fluid components have no lobe or placement provenance. | Main | 2 | Re-enter when a deterministic public fixture or off-hot-path trace exposes the chosen lobe count; cover, shell, bounds, bake, and generated output remain separately verified. |
| OS-093 | Solid cover | Keep covered Jam reservoirs concealed until found, with a declared minimum of three solid blocks. | Main | 2 | The fixed-seed fresh world found 1,960 managed Jam cells in the allowed Cookie-Forest region, including 548 cells beneath at least three solid blocks; save/reload retained the same counts. This is independent of the fourteen authored Jam sources in the distant Jam Lantern Walk. |
| OS-094 | Solid shell | Prevent exposed or leaking fluid bodies where a shell is required. | Main | 2 | Shell inspection finds the declared minimum except at an intentional access feature. |
| OS-095 | Fluid host blocks and tags | Restrict Jam through explicit Wafer Rock plus the metamorphic host family, Custard through the edible-host tag, Hot Fudge through volcanic edible hosts, and Molten Mallow through the `igneous_volcanic` family. | Main | 2/4 | Revision 44 retains the Jam host forms, volcanic Hot-Fudge hosts across all seven Nether biomes and the independent Molten-Mallow host family. All rules bake; Calderas fresh/reload retained 1,158 Molten-Mallow cells plus 29,977 Hot-Fudge cells, while strict negative-host proof resumes when the public API exposes a compiled acceptance predicate or deterministic deposit fixture. |
| OS-096 | Fluid biome filters | Start Jam only from Cookie-Forest chunks, exclude Syrup starts from Soda-Ocean chunks, and admit Molten Mallow only from Molten-Marshmallow-Calderas chunks. OreSpawn evaluates the filter from the chunk's surface-biome sample, so these are deliberately start filters rather than final-cell ownership claims. | Main | 2/3/4 | Direct tests retain the exact Calderas-only filter, and fixed fresh/reload retained its managed output. A separate fixed-seed survey found 100 allowed Cookie-Forest chunks and 1,960 managed Jam cells; fresh/reload retained the result. Final bodies may cross underground biome-cell boundaries after an accepted surface-biome start. |
| OS-097 | Fluid geome weights | Bias covered deposits by flavour geology; Jam declares Cocoa Basin `3.0`, Wafer Shelf `1.0`, Peppermint Fold `0.5` and Rock-Candy Uplift `0.5`. Custard remains the ratio-measured Wafer-Shelf example, while Molten Mallow deliberately uses one exact Fudge-Mantle weight. | Main | 2/4 | Direct tests retain every Jam weight and the exact Molten-Mallow geome declaration. Fixed fresh/reload worlds retain managed output; observations are not presented as proof of selection ratios. Earlier Custard evidence measured 1,850 blocks across 51 Wafer chunks versus 250 across 30 Cocoa chunks, with solid envelopes intact. |

OreSpawn `4.0.1` could fail to generate springs when their neighbouring blocks
were non-vanilla. Published release `4.0.6.118021` is now pinned and `OS-104` verifies
the repaired path directly and across fresh/save/reload boundaries. Covered
Jam, Custard, Caramel, Syrup and Hot-Fudge bodies remain fluid-deposit examples;
they are not reclassified as spring evidence.

### Intrusive diagnostics

| ID | OreSpawn capability | CakeWorld example | Placement | Slice | Verification expectation |
|---|---|---|---|---|---|
| OS-098 | Aliases and renamed IDs | **Verified:** map the still-installed diagnostic source output `cakeworld:burnt_sugar_rock` to `cakeworld:sprinkle_cluster` only in the explicitly selected Sampler. The adventure definitions remain unchanged. | Sampler | 7 | Packaged and active profiles retain the one-to-one alias. Public Nether sampling on fresh/reload returns exactly 512 Sprinkle cells, zero original Burnt-Sugar cells and 1,057 independent Fudge-Rock cells, proving bake-time substitution without a duplicate output. |
| OS-099 | Retrogen | **Verified:** package one disabled Sprinkle rule marked for retrogen beside one disabled non-retrogen Fizzy-Pearl control, then enable both and revision `5301` only in an ignored copied Sampler save. The ordinary adventure and packaged Sampler remain inert. | Sampler | 7 | Baseline created exact 256-block eligible Biscuit and ineligible Brick layers in old chunks. Apply produced exactly 64 Sprinkles plus 192 Biscuit, zero Fizzy Pearls, an unchanged 256-block Brick control and intact sentinels. Same-save reload retained those exact counts with `force: false`, proving the revision marker prevented a second pass. A separate ordinary fresh world still selected `edible_world`; packaged Sampler fresh/reload baked zero managed ores. |
| OS-100 | Flat bedrock | **Verified:** enable exactly three layers in the Sampler Overworld and Nether, with bedrock retrogen deliberately off. | Sampler | 7 | Fresh/reload scans of one generated Overworld chunk and one generated Nether chunk each return exact bottom `256/256/256/0/0` counts; the Nether ceiling returns the same boundary. A separate ordinary fresh world still selects the unchanged adventure. |
| OS-101 | Extreme formations | **In progress:** revision 54 retains the maximum-variation bounded custom case beside the separately verified normal adventure. An isolated generated provider override adds the opposite named-preset case by selecting `tiny` for all five controls without changing the packaged three-template contract. Only hands-on visual/readability review remains. | Sampler | 7 | Same-seed public-API surveys retain exact stable `5,393/634/7/5/6,720,209,891,956,171,365`, extreme `36,515/1,176/7/14/2,804,912,711,855,593,311`, and minimum-preset `32,350/1,627/8/14/12,479,179,277,466,877,779` transition/identity signatures. Minimum fresh/reload and both regression profiles passed and saved. The label describes the `tiny` named controls, not a guaranteed minimum observed transition count. |

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
| OS-TEST-004 | `sampler_platter` explicit selection | **In progress:** packaged and active-profile tests prove explicit selection, non-automatic metadata, canonical shared geology definitions, custom and minimum-preset `sky_v1` formations, augment, namespace filters, optional/required similarity, output aliases, flat bedrock, bounded copied-world retrogen, exact three-realm fresh/reload distributions, and independent automatic selection of `edible_world`. Completion still requires every remaining labelled diagnostic plot. |
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
formation presets. OreSpawn 4.0.6 exposes one profile-wide `formations`
object, so horizontal size, thickness, waviness, edge irregularity, and
continuity apply to every geome in that profile. CakeWorld retains the stable
global defaults and the per-geome creative goal, but must not claim
per-geome-preset proof until OreSpawn supports baked geome-level overrides.
OS-027 and OS-029 work within that released boundary by comparing separate
same-seed adventure and diagnostic profiles; they do not require an OreSpawn
change and do not unshelve the per-geome OS-028 goal.

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

The Soda Ocean adventure therefore places one visibly authored Fizzy Pearl in
each complete Wafer Reef Nursery. This is gameplay treasure, not OreSpawn
pattern output. Fixed-world attribution recognises the Nursery's Wafer cross,
four Candy-Glass markers and surrounding reef before excluding that Pearl.
`OS-085` remains shelved unless a Pearl outside those authored scenes can be
proven to originate from the compiled under-fluids rule.

`OS-089` has independent source, bake, differential-world, reload, and
integrated-mod evidence. Revision 11 adds
`cakeworld:fluid_deposit/hot_fudge` only in the Nether, restricted to Fudge
Wastes or Burnt-Toffee Deltas, Fudge Mantle, and volcanic edible hosts from
Y 16 through 112. Against
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
OreSpawn 4.0.6.118021 supports only `zone.moddev.mc.orespawn.api`; that API exposes
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
