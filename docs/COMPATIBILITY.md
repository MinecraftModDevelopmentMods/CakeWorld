# CakeWorld Compatibility Contract

## Purpose

CakeWorld is a total conversion, but it must not break the resource economy
that Minecraft and cooperating mods expect. The landscape may look edible and
fantastical while ore tags, drops, recipes, advancements, tools, automation,
and recipe viewers continue to understand the underlying resource.

This document defines the conversion contract for vanilla resources and
BaseMetals. Every row has a stable `COMPAT-*` ID for the living feature ledger.
Names and artwork are CakeWorld's; resource identity remains recognisable.

## General Rules

1. CakeWorld uses genuine registered CakeWorld blocks, not model swaps applied
   to vanilla or BaseMetals blocks.
2. A compatibility ore keeps its source resource's identity in its display
   name, block and item tags, intended drops, and processing roles.
3. OreSpawn owns world-placement takeover. CakeWorld never disables native
   generation until OreSpawn reports that takeover for the source mod is
   active.
4. Exactly one system owns placement in a configured world. Compatibility must
   neither duplicate resources nor remove them on an invalid profile.
5. Soft edible terrain may be nibbled; metal-bearing or mineral-bearing blocks
   are mined and processed. Their confectionery theme never implies that
   mercury, lead, metal ore, or mineral ore is food.
6. CakeWorld has no hard code dependency on BaseMetals classes. Optional
   integration uses installed-mod conditions, registry IDs, tags, data, and
   OreSpawn templates.
7. Missing optional mods and absent registry IDs are resolved during profile
   validation. They never cause runtime registry lookups in generation loops.
8. Existing worlds retain their saved OreSpawn profile. Compatibility
   conversion requires explicit template/profile migration and is never
   silently selected after a mod is added.

## Vanilla Resource Conversion

The ordinary `cakeworld:edible_world` template replaces vanilla ore placement
with CakeWorld outputs while preserving the vanilla resource economy.

| ID | Vanilla resource and source blocks | CakeWorld identity | Compatibility contract |
|---|---|---|---|
| COMPAT-VAN-001 | Coal; `minecraft:coal_ore`, `minecraft:deepslate_coal_ore` | Cocoa Coal | Drops coal under the same tool/enchantment conditions, joins `forge:ores/coal`, and preserves fuel, recipe, and advancement use. A dense deep-cocoa variant may represent deepslate. |
| COMPAT-VAN-002 | Iron; `minecraft:iron_ore`, `minecraft:deepslate_iron_ore` | Iron Wafer | Retains raw-iron or equivalent intended drops, `forge:ores/iron`, smelting/blasting outputs, and tool progression. A dark layered wafer is the deep variant. |
| COMPAT-VAN-003 | Copper; `minecraft:copper_ore`, `minecraft:deepslate_copper_ore` | Copper-Caramel | Retains raw-copper drop counts, fortune behavior, `forge:ores/copper`, oxidation-independent processing, and vanilla copper recipes. A dark caramel crunch is the deep variant. |
| COMPAT-VAN-004 | Gold; `minecraft:gold_ore`, `minecraft:deepslate_gold_ore` | Honeycomb Gold | Retains raw-gold drops, `forge:ores/gold`, piglin-relevant resource identity, and smelting/blasting roles. A dark honeycomb brittle is the deep variant. |
| COMPAT-VAN-005 | Redstone; `minecraft:redstone_ore`, `minecraft:deepslate_redstone_ore` | Raspberry Redstone | Retains redstone dust drops, light-up interaction where practical, fortune behavior, `forge:ores/redstone`, and all redstone recipes. A dark jam-crystal variant covers deep placement. |
| COMPAT-VAN-006 | Lapis lazuli; `minecraft:lapis_ore`, `minecraft:deepslate_lapis_ore` | Blueberry Lapis | Retains lapis drops, fortune behavior, `forge:ores/lapis`, enchanting, dyeing, and recipes. A dark blueberry-rock variant covers deep placement. |
| COMPAT-VAN-007 | Diamond; `minecraft:diamond_ore`, `minecraft:deepslate_diamond_ore` | Rock-Candy Diamond | Retains diamond drops, silk-touch/fortune behavior, `forge:ores/diamond`, tool progression, recipes, and advancement expectations. A deep geode variant covers deep placement. |
| COMPAT-VAN-008 | Emerald; `minecraft:emerald_ore`, `minecraft:deepslate_emerald_ore` | Mint Emerald | Retains emerald drops, fortune behavior, `forge:ores/emerald`, villager-economy roles, recipes, and advancement expectations. A dark mint-crystal variant covers deep placement. |
| COMPAT-VAN-009 | Nether quartz; `minecraft:nether_quartz_ore` | Vanilla Quartz | Retains quartz drops, experience, fortune/silk-touch behavior, `forge:ores/quartz`, and comparator/building recipes. |
| COMPAT-VAN-010 | Nether gold; `minecraft:nether_gold_ore` | Fudge Gold | Retains nugget drops, experience, fortune/silk-touch behavior, `forge:ores/gold`, and piglin-recognisable gold identity. |
| COMPAT-VAN-011 | Ancient debris | Ancient Nougat | Retains blast resistance, mining tier, netherite-scrap smelting output, rarity, non-renewability assumptions, and netherite progression. It is a hard ancient material, never nibbleable terrain. |

### Vanilla data obligations

Each compatibility block requires:

- the correct Forge ore tag and any applicable vanilla mineable/tool-tier tags;
- matching block and item tag membership where the source ecosystem expects
  both;
- loot behavior covering normal mining, silk touch, fortune, experience, and
  explosion survival as appropriate;
- smelting, blasting, crafting, and advancement behavior equivalent to the
  source resource;
- recipe-viewer discoverability;
- localisation that keeps the resource word clear;
- blockstate, model, item model, texture, particles, and sounds appropriate to
  CakeWorld;
- explicit OreSpawn source ownership and host rules;
- tests proving that native and OreSpawn generation do not both run.

CakeWorld may use its own ore item or raw-resource presentation only when every
consumer of the vanilla role still works. Otherwise it drops the canonical
vanilla resource.

### Current vanilla-takeover limitation

The themed blocks, loot, tags, processing recipes, mining tiers, piglin gold
identity, and Ancient Nougat advancement bridge can exist safely before world
placement changes. The normal template must nevertheless leave
`manage_vanilla_ores` disabled until OreSpawn can distinguish:

- the CakeWorld block a managed rule places; and
- the vanilla source block whose native placed features that rule replaces.

OreSpawn 4.0.6.118021 currently keys its vanilla feature gate from the managed
rule's output block. A rule that outputs `cakeworld:rock_candy_diamond`
therefore cannot suppress the gates for `minecraft:diamond_ore`. Enabling both
would duplicate resources. Enabling `suppress_all_ore_features` would also
remove unknown third-party ore features, which violates this compatibility
contract.

The required OreSpawn capability is a declarative, bake-time validated source
mapping—such as a `suppresses` or `native_source_blocks` list—kept out of
generation hot loops. Until that capability exists and copied-world tests pass,
CakeWorld exposes the themed content but deliberately leaves vanilla placement
unchanged. No existing-world profile is migrated implicitly.

## BaseMetals Conversion

### Optional integration model

BaseMetals is optional. CakeWorld registers the thirteen themed compatibility
blocks as dormant content so resource packs, documentation, and data generation
remain stable. The normal `edible_world` profile does not generate them.

When BaseMetals is installed, the higher-priority fresh-world template
`cakeworld:edible_world_basemetals` is eligible. It is generated from the
canonical adventure profile plus the BaseMetals output overlay. CakeWorld code
does not import BaseMetals implementation classes.

Every themed block joins the corresponding `forge:ores/<metal>` block and item
tags. The twelve BaseMetals-only counterparts drop themselves under the same
mining constraints as the source BaseMetals ore. Shared Copper-Caramel retains
its vanilla-compatible raw-copper drop and can be processed as an ore block
when harvested with Silk Touch. BaseMetals' tag-based smelting, blasting,
crushing, and recipe-viewer integrations accept all thirteen block items. If a
future BaseMetals version changes its resource-drop contract, CakeWorld must
update deliberately and test both behaviors.

### Current implementation

Provider revision 39 builds both adventure templates from
`src/main/orespawn/provider.json` and the small
`src/main/orespawn/basemetals-overlay.json`. The compatibility overlay keeps
the ten currently enabled BaseMetals resources enabled, keeps Copper,
Antimony, and Bismuth non-generating, and replaces the Nether and End host
lists with CakeWorld edible geology. The canonical Nougat-Depths,
Burnt-Toffee-Deltas, Cinnamon-Ember-Groves, Black-Liquorice-Labyrinths,
Treacle-Soul-Valleys, Chilli-Chocolate-Crags and
Molten-Marshmallow-Calderas biome, landmark, geology and fluid-deposit profiles are
byte-for-byte identical between the normal and BaseMetals adventures; the
compatibility template differs only by the thirteen deliberate BaseMetals ore
entries. Structural Toasted Nougat Tiles
are not an edible ore host, so compatibility placement cannot consume the
Ancient Nougat Kitchen floor.

The automated integration scenario verifies:

- CakeWorld without BaseMetals still selects `cakeworld:edible_world`;
- CakeWorld with BaseMetals selects
  `cakeworld:edible_world_basemetals`;
- all thirteen block and item tags resolve;
- all thirteen smelting, blasting, and crushing recipe families accept the
  themed block item and return the BaseMetals resource;
- enabled counterparts generate in edible Overworld, Nether, and End geology;
- Copper, Antimony, and Bismuth remain present but non-generating.

### Thirteen-ore matrix

| ID | BaseMetals identity | CakeWorld block and display name | Placement and compatibility notes |
|---|---|---|---|
| COMPAT-BM-001 | Cold-Iron; `basemetals:coldiron_ore` | Frosted Cold-Iron | Nether material with a cold frosting crust. Joins `forge:ores/coldiron`; retains Cold-Iron smelting, blasting, crushing, mining tier, and recipe roles. |
| COMPAT-BM-002 | Adamantine; `basemetals:adamantine_ore` | Jawbreaker Adamantine | Rare, exceptionally hard Nether jawbreaker. Joins `forge:ores/adamantine`; retains Adamantine processing, hardness intent, and progression. |
| COMPAT-BM-003 | Starsteel; `basemetals:starsteel_ore` | Starlight Starsteel | End resource in Starlight Sugar Fields and Meringue geology. Joins `forge:ores/starsteel`; retains Starsteel processing and progression. |
| COMPAT-BM-004 | Tin; `basemetals:tin_ore` | Silver-Dragée Tin | Overworld dragée-speckled ore. Joins `forge:ores/tin`; retains Tin smelting, blasting, crushing, and alloy recipe roles. |
| COMPAT-BM-005 | Lead; `basemetals:lead_ore` | Liquorice Lead | Dense liquorice-striped ore. Joins `forge:ores/lead`; retains Lead processing and recipe roles. It is explicitly non-food. |
| COMPAT-BM-006 | Zinc; `basemetals:zinc_ore` | Lemon-Drop Zinc | Bright lemon-drop crystal ore. Joins `forge:ores/zinc`; retains Zinc processing and alloy roles. |
| COMPAT-BM-007 | Silver; `basemetals:silver_ore` | Silver-Leaf Silver | Wafer-thin silver-leaf seams. Joins `forge:ores/silver`; retains Silver processing and recipe roles. |
| COMPAT-BM-008 | Mercury; `basemetals:mercury_ore` | Mirror-Glaze Mercury | Reflective glaze mineral. Joins `forge:ores/mercury`; retains Mercury processing and recipe roles. It is hard mineral content and explicitly non-food. |
| COMPAT-BM-009 | Nickel; `basemetals:nickel_ore` | Mint-Wafer Nickel | Mint-striped wafer ore. Joins `forge:ores/nickel`; retains Nickel processing and alloy roles. |
| COMPAT-BM-010 | Platinum; `basemetals:platinum_ore` | Sugar-Star Platinum | Rare star-shaped crystal inclusions. Joins `forge:ores/platinum`; retains Platinum processing and recipe roles. |
| COMPAT-BM-011 | Copper; `basemetals:copper_ore` | Copper-Caramel | Uses the shared CakeWorld copper identity and joins `forge:ores/copper`. BaseMetals' own copper-generation rule is disabled by default in the current integration, so the compatibility template must not introduce a second copper source. |
| COMPAT-BM-012 | Antimony; `basemetals:antimony_ore` | Aniseed Antimony | Dark aniseed-like crystals. Joins `forge:ores/antimony`; retains processing recipes. The current BaseMetals provider registers the block but supplies no default generation rule, so CakeWorld does not silently invent ordinary-world abundance. |
| COMPAT-BM-013 | Bismuth; `basemetals:bismuth_ore` | Rainbow-Rock Bismuth | Iridescent stepped candy-rock appearance. Joins `forge:ores/bismuth`; retains processing recipes. The current BaseMetals provider registers the block but supplies no default generation rule, so generation requires an explicit compatible rule or pack decision. |

### Provider ownership

The compatibility generator emits overrides by stable BaseMetals source-rule
identity, not by copying opaque runtime objects. The overlay is intentionally
small enough to compare directly with the supported BaseMetals provider when
that contract changes.

For the eleven resources with current BaseMetals OreSpawn rules, CakeWorld
changes the generated output to the corresponding CakeWorld block while
preserving intentional dimension, height, frequency, quantity, distribution,
and enablement defaults unless the creative design explicitly documents a
change. BaseMetals copper remains disabled when vanilla Copper-Caramel already
owns copper placement.

Antimony and Bismuth receive real CakeWorld blocks and complete tags/recipes,
but CakeWorld does not pretend that BaseMetals currently gives them ordinary
generation. A future supported BaseMetals rule, an explicit CakeWorld design
decision, or a pack-authored rule may activate them. That activation becomes a
separate ledger item with abundance and progression evidence.

### Template selection

| Installed mods and world state | Expected result |
|---|---|
| Fresh world: CakeWorld + OreSpawn | `cakeworld:edible_world` is selected, subject to an explicit global default or a higher-priority third-party template. |
| Fresh world: CakeWorld + OreSpawn + BaseMetals | `cakeworld:edible_world_basemetals` wins over the normal CakeWorld template. |
| Fresh world with explicit global default | The administrator's explicit default wins; CakeWorld does not override it. |
| Existing CakeWorld world, then BaseMetals added | Existing snapshot remains unchanged. Administrator must explicitly migrate/select a compatible profile. |
| Existing non-CakeWorld world, then CakeWorld added | Existing snapshot and terrain remain unchanged. No silent total conversion. |
| BaseMetals removed from a copied compatibility world | The saved profile is not silently rewritten. Loading must fail safely or require an explicit migration that replaces missing outputs before play. |

## Shared Canonical Profile

The two adventure templates are generated rather than hand-copied:

```text
canonical edible-world profile
        |
        +-- normal template
        |
        +-- BaseMetals conditional overlay
              +-- required_mods += basemetals
              +-- higher auto-select priority
              +-- source ore outputs -> CakeWorld compatibility blocks
```

The generation step must be deterministic and reviewable. A test compares
their serialized profiles after removing the explicitly allowed BaseMetals
differences. Any other drift fails the build.

## Compatibility Acceptance Matrix

| ID | Scenario | Acceptance evidence |
|---|---|---|
| COMPAT-TEST-001 | Vanilla resource tags | Every CakeWorld vanilla ore appears in the intended block/item ore tags and is accepted by tag-based consumers. |
| COMPAT-TEST-002 | Vanilla mining and drops | Correct tool, wrong tool, silk touch, fortune, experience, and explosion cases match the intended vanilla resource behavior. |
| COMPAT-TEST-003 | Vanilla processing and progression | Smelting/blasting/crafting, beacon or enchanting inputs where relevant, advancements, and progression work from CakeWorld-mined resources. |
| COMPAT-TEST-004 | Vanilla takeover counts | Fixed-seed chunk survey contains themed resources in expected ranges and no duplicate native vanilla placements. |
| COMPAT-TEST-005 | BaseMetals absent | CakeWorld loads cleanly; dormant compatibility blocks resolve; no BaseMetals template, rules, or missing-ID warnings activate. |
| COMPAT-TEST-006 | All thirteen BaseMetals tags | Each themed block appears in `forge:ores/<metal>` block and item tags alongside the source identity when installed. |
| COMPAT-TEST-007 | BaseMetals furnace recipes | Each themed ore accepted by BaseMetals smelting and blasting produces the same result and experience as the source ore. |
| COMPAT-TEST-008 | BaseMetals crushing | Every supported themed ore is accepted by the installed BaseMetals crushing/Crack Hammer route with the intended output. |
| COMPAT-TEST-009 | Recipe viewer | Smelting, blasting, crushing, and other tag-driven uses remain visible and navigable in the supported recipe viewer configuration. |
| COMPAT-TEST-010 | BaseMetals takeover counts | Enabled metals generate once with expected distribution; disabled Copper does not duplicate vanilla Copper-Caramel; Antimony and Bismuth follow their explicit no-default-generation contract. |
| COMPAT-TEST-011 | Mining tiers and hardness | Every themed metal requires the intended tool tier and retains the source ore's meaningful hardness/resistance behavior. |
| COMPAT-TEST-012 | Fresh auto-selection | BaseMetals presence changes only the fresh-world template choice and compatibility outputs. |
| COMPAT-TEST-013 | Existing-world safety | Adding or removing BaseMetals does not silently rewrite a copied world's saved profile or old chunks. |
| COMPAT-TEST-014 | Override and failure safety | Valid pack override works; malformed override leaves takeover inactive so native generation is not incorrectly suppressed. |
| COMPAT-TEST-015 | Multiplayer parity | Dedicated server and two clients agree on blocks, tags, loot, recipes, profile, and generated chunk contents. |

Each test records source, build, runtime, and integrated-world evidence
separately in the ignored local feature ledger.

## Adding Future Integrations

A new compatibility module must document:

- the exact supported source-mod versions;
- stable source registry and rule IDs;
- whether the source is required or optional;
- ownership and native-generation suppression behavior;
- each themed output block, display name, tags, drops, recipes, and tool tier;
- automatic-template conditions and priority;
- fresh-world and existing-world behavior;
- absent-mod, malformed-profile, dedicated-server, and multiplayer tests.

Unknown third-party ores and mobs remain untouched until a contract like this
exists. CakeWorld does not guess based on names at runtime.
