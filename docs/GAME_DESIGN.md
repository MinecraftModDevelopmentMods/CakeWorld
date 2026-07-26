# CakeWorld Game Design Bible

## Purpose

CakeWorld is an original, family-friendly confectionery fantasy and a complete
working example of OreSpawn integration. It must succeed as three things at
once:

1. a joyful Minecraft mod that children, families, builders, explorers, and
   long-running multiplayer worlds want to keep playing;
2. a broad integration and regression test for OreSpawn;
3. a readable reference showing other mod developers how an OreSpawn child mod
   can own content while delegating world-generation policy and execution.

CakeWorld is a fresh-world total conversion. Its normal template transforms the
Overworld, Nether, and End into related edible realms. Existing worlds retain
their saved OreSpawn profile and are never silently rewritten.

The creative direction is storybook confectionery, kitchen magic, playful
gadgets, and surprising edible ecology. Familiar confectionery literature may
inspire the sense of wonder, but CakeWorld must use original names, characters,
stories, structures, textures, sounds, and visual identities.

## Product Pillars

### A world worth exploring

Every biome needs more than a different top block. It must have a recognisable
silhouette, colour and material palette, edible surface, signature ingredient,
creature, landmark, ambient sound, and reason to revisit.

### The world is food

Soft terrain can be nibbled directly. Hard, structural, or mineral-bearing
materials must be harvested, sliced, melted, baked, or cooked. A raw bite is a
small emergency snack; discovered recipes provide the useful nutrition,
saturation, and playful effects. CakeWorld rewards variety without adding a
punitive diet or sugar-overload system.

### Peril without cruelty

CakeWorld can be mischievous, surprising, and challenging without being harsh.
Environmental hazards remain real but clearly signposted and forgiving.
Creature behaviour follows this contract:

| Difficulty | Creature damage contract |
|---|---|
| Peaceful | Normal peaceful behaviour; hostile CakeWorld creatures do not spawn. |
| Easy | CakeWorld creatures cause no direct or indirect health damage. |
| Normal | CakeWorld creatures cause no direct or indirect health damage. |
| Hard | CakeWorld creatures may deal real, clearly telegraphed damage. |

On Easy and Normal, attacks may bounce, slow, stick, obscure, colour, corral,
or temporarily inconvenience a player. They must not destroy possessions,
silently break builds, disguise damage as another source, or create a lethal
chain reaction. Hard mode adds genuine peril without abandoning readable
telegraphs or the cheerful tone.

### Discovery, not obligation

The Explorer's Cookbook is a player-specific illustrated discovery guide rather
than a quest chain. It fills itself as a player visits places, tastes foods,
meets creatures, mines materials, crafts recipes, and finds landmarks. Its
completion is the long-term optional milestone; ordinary building and
exploration never require completion.

### A faithful OreSpawn child mod

CakeWorld registers its blocks, items, fluids, entities, biomes, structures,
assets, data, and compatibility content. OreSpawn owns biome overlays, geology,
ore and fluid-deposit placement, surfaces, dimension materials, templates,
world profiles, and the world-creation editor. CakeWorld must not duplicate
OreSpawn's hot-loop generation engine or add TerraBlender merely to place its
biomes.

## Feature Identity and Lifecycle

Every wishlist entry has a stable design ID. IDs remain stable even if a
feature is renamed or shelved. Public documents describe intent; the ignored
local feature ledger records delivery and evidence.

Priorities:

- **Core**: required for the central CakeWorld promise;
- **Expansion**: intended after the core experience is healthy;
- **Dream**: ambitious content worth preserving even without a delivery date;
- **Experiment**: a testable idea that may be rejected or shelved.

Delivery states are Wishlist, Specified, In Progress, Implemented, Verified,
Shelved, and Rejected. Source code alone is not verification. Build, runtime,
integrated-world, compatibility, and performance evidence remain separate.

## Fixed System Contracts

| ID | Priority | Slice | System contract |
|---|---|---:|---|
| SYS-001 | Core | 1 | CakeWorld is a fresh-world total conversion selected through OreSpawn templates. Installing or updating it never silently rewrites an existing world's saved profile. |
| SYS-002 | Core | 1/4/5 | Realm identities remain distinct: Overworld is wonder and settlement, Nether is heat and kitchen peril, and End is dreams and starlight. |
| SYS-003 | Core | 1 | The Explorer's Cookbook is a player-specific discovery guide rather than a quest chain or mandatory progression gate. |
| SYS-004 | Core | 1/2 | Most soft terrain can be nibbled in place; hard, structural, mineral-bearing, or metal-bearing blocks must be harvested, sliced, melted, or cooked. |
| SYS-005 | Core | 1 | Raw terrain is only an emergency snack. Prepared recipes provide worthwhile nutrition, saturation, discovery, and playful effects. |
| SYS-006 | Core | 1/3/4/5/6 | Peaceful keeps peaceful behavior; Easy and Normal CakeWorld mobs cause zero health damage; Hard permits real damage with clear telegraphs. |
| SYS-007 | Core | 1/3/4/5/6 | Non-damaging attacks may bounce, slow, stick, obscure, paint, or temporarily inconvenience, but cannot destroy possessions or cause disguised indirect health damage. |
| SYS-008 | Core | 1/4/5 | Environmental hazards remain real but forgiving through warnings, cushioning, rescue blocks, escape routes, and accessible recovery. |
| SYS-009 | Core | 3/4/5/6 | Every living vanilla mob role eventually receives a genuinely new CakeWorld entity with deliberate compatibility behavior. |
| SYS-010 | Core | 3/4/5/6 | Every major vanilla structure family eventually receives an edible replacement that preserves required progression and hooks. |
| SYS-011 | Core | 1/6 | CakeWorld uses original storybook confectionery names, art, writing, creatures, and structures; it does not copy copyrighted characters, scenes, or distinctive designs. |
| SYS-012 | Core | 6/7 | Unknown third-party mobs, structures, and resources remain untouched until an explicit compatibility contract exists. |

## Realm Identity

| Realm | Emotional role | Material language | Play emphasis |
|---|---|---|---|
| Overworld | Wonder, welcome, and settlement | Cake, biscuit, sweets, fruit, soda, gardens | Exploration, building, farming, villages, and discovery |
| Nether | Heat, spice, and kitchen peril | Fudge, burnt sugar, treacle, cinnamon, chilli chocolate | Hazard navigation, valuable ingredients, and bold structures |
| End | Dreams, quiet, and starlight | Meringue, candyfloss, mooncake, macarons, cosmic jelly | Aerial movement, mystery, rare materials, and Cookbook completion |

## Biome Wishlist

Each biome specification must eventually define its palette, surface and
subsurface, vegetation or growths, signature ingredient, inhabitants, landmark,
ambient loop, climate, neighbouring-biome logic, and OreSpawn mechanism.

### Overworld and caves

| ID | Priority | Slice | Biome | Identity and promised features |
|---|---|---:|---|---|
| BIO-OW-001 | Core | 1 | Chocolate Sponge Meadows | Welcoming starter country with icing turf, sprinkle flowers, Cocoa Cows, and fall-softening chocolate sponge soil. |
| BIO-OW-002 | Core | 3 | Gingerbread Hearthlands | Gingerbread villages, candy-cane roads, Cookbook libraries, wafer windmills, orchards, and friendly trade. |
| BIO-OW-003 | Core | 1 | Cookie Crumb Forest | Biscuit paths, wafer trunks, cookie canopies, hidden crumb burrows, and Mallow Chicks. |
| BIO-OW-004 | Expansion | 3 | Peppermint Pinewoods | Striped trunks, cool mint air, chiming branches, frosted clearings, and Peppermint Foxes. |
| BIO-OW-005 | Expansion | 3 | Gummy Jungle | Translucent foliage, elastic vines, jelly pools, bright canopy layers, bouncing wildlife, and shy Sherbet Ocelots. |
| BIO-OW-006 | Core | 2 | Caramel Bogs | Sticky ground, treacle reeds, toffee mangroves, covered caramel deposits, and slow safe routes. |
| BIO-OW-007 | Expansion | 3 | Sherbet Dunes | Coloured powder dunes, fizzy dust weather, buried sweet jars, sugar fossils, and rare Sherbet Ocelots. |
| BIO-OW-008 | Core | 2 | Candy-Cane Badlands | Dramatic striped geological layers, crystal mines, wafer mesas, and visible OreSpawn formation boundaries. |
| BIO-OW-009 | Core | 3 | Marshmallow Peaks | Soft summits, icing snow, Candyfloss Sheep, cloud bridges, and safe bouncing descents. |
| BIO-OW-010 | Expansion | 3 | Ice-Cream Tundra | Scoop-shaped hills, frozen lemonade lakes, wafer igloos, Vanilla-Ice Bears, and cooling foods. |
| BIO-OW-011 | Dream | 3 | Waffle Plateaus | Gridded cliffs, syrup falls, lightweight bridges, wafer windmills, and kitchen gadget settlements. |
| BIO-OW-012 | Expansion | 3 | Cupcake Gardens | Sprinkle meadows, flowering cakes, Cupcake Cows, Sugar Bees, and gentle pollinator play. |
| BIO-OW-013 | Expansion | 3 | Liquorice Darkwood | Twisting roots, maze-like paths, playful spooky ambience, Liquorice Weavers, and hidden shortcuts. |
| BIO-OW-014 | Expansion | 3 | Lollipop Orchards | Harvestable boiled-sweet trees, confectioners' cottages, bright paths, and flavour-specific crops. |
| BIO-OW-015 | Dream | 3 | Popcorn Prairie | Rustling kernel fields, popping seed pods, lightweight building material, and wide-open travel. |
| BIO-OW-016 | Core | 1 | Soda Ocean | Lemonade water, wafer reefs, Jellybean Fish, Soda Dolphins, sunken sweetshops, and fizzy-floor treasure. |
| BIO-OW-017 | Expansion | 3 | Custard Coast | Creamy shallows, pudding beaches, Wafer Turtles, seaside kitchens, and sheltered starter ports. |
| BIO-OW-018 | Dream | 3 | Jellybean Archipelago | Bright flavour-themed islands, island-specific ecology, small settlements, and boat exploration. |
| BIO-OW-019 | Core | 2 | Rock-Candy Caverns | Geodes, crystal bridges, compact ore demonstrations, Mint Emeralds, and prismatic ambience. |
| BIO-OW-020 | Core | 2 | Jam Grottoes | Glowing fruit vines, covered strawberry-jam reservoirs, sticky cave routes, and recipe ingredients. |
| BIO-OW-021 | Core | 2 | Nougat Depths | Dense chewy strata, ancient underground kitchens, deep ore outputs, and BaseMetals compatibility. |

### Nether

| ID | Priority | Slice | Biome | Identity and promised features |
|---|---|---:|---|---|
| BIO-NE-001 | Core | 4 | Fudge Wastes | Hot-fudge seas, fudge rock, scorched chocolate terrain, Fudge Folk, and the realm's readable baseline. |
| BIO-NE-002 | Core | 4 | Burnt-Toffee Deltas | Brittle caramel columns, crunchy ash, snapping ground, Burnt-Toffee Foundries, and volcanic geology. |
| BIO-NE-003 | Expansion | 4 | Cinnamon Ember Groves | Glowing spice trees, Cinnamon Sparks, warming ingredients, and ember-like particles. |
| BIO-NE-004 | Core | 4 | Black-Liquorice Labyrinths | Tangled roots, fortress passages, looping shortcuts, Burnt-Candy Knights, and dark confectionery stone. |
| BIO-NE-005 | Expansion | 4 | Treacle Soul Valleys | Dark syrup flats, slow movement, sugar wisps, bridge-building pressure, and mournful soft ambience. |
| BIO-NE-006 | Core | 4 | Chilli-Chocolate Crags | The hottest and most perilous mining region, valuable deposits, Fudge Brutes, and strong Hard-mode telegraphs. |
| BIO-NE-007 | Dream | 4 | Molten-Marshmallow Calderas | Swelling molten mallow, elastic rescue islands, steam vents, and unusual fluid-material interactions. |

### End

| ID | Priority | Slice | Biome | Identity and promised features |
|---|---|---:|---|---|
| BIO-END-001 | Core | 5 | Meringue Islands | Airy white terrain, the first dreamlike End landscape, and a safe visual introduction to the realm. |
| BIO-END-002 | Expansion | 5 | Candyfloss Cloudbanks | Drifting pink clouds, soft aerial travel, Meringue Llamas, and low-density building blocks. |
| BIO-END-003 | Expansion | 5 | Mooncake Barrens | Quiet golden plains, crater kitchens, ancient crumbs, and sparse contemplative ambience. |
| BIO-END-004 | Core | 5 | Starlight Sugar Fields | Sparkling crystal grass, Starsteel compatibility, luminous ingredients, and the clearest night-sky views. |
| BIO-END-005 | Core | 5 | Macaron Archipelago | Layered floating islands, Macaron Citadels, Macaron Clams, and colour-coded navigation. |
| BIO-END-006 | Dream | 5 | Cosmic Jelly Reefs | Translucent void growths, bioluminescent creatures, elastic paths, and rare jelly ingredients. |
| BIO-END-007 | Expansion | 5 | Fondant Chorus Gardens | Sculpted pastel plants, chorus-role food, Taffy Tallwalkers, and strange but readable teleport play. |

## Physical and Edible Blocks

| ID | Priority | Slice | Feature contract |
|---|---|---:|---|
| BLK-001 | Core | 1 | Chocolate sponge reduces fall damage to roughly one quarter of the normal result without adding a bounce. Final tuning requires playtesting. |
| BLK-002 | Core | 3 | Marshmallow cancels fall damage and returns a gentle, controllable bounce; careful movement suppresses the bounce. |
| BLK-003 | Core | 2 | Caramel retains 35% horizontal movement and halves downward movement; Treacle Syrup retains 55% horizontal and 70% downward movement. Upward movement and some steering are preserved so neither fluid becomes an unavoidable trap. |
| BLK-004 | Expansion | 3 | Gummy blocks provide stronger elastic movement and translucent coloured building sets. |
| BLK-005 | Core | 1 | Biscuit crumbs behave like gravity-affected sand and remain valid OreSpawn surface and host material. |
| BLK-006 | Expansion | 3 | Wafer blocks are light, fragile, fast to build with, and useful for temporary bridges and village machinery. |
| BLK-007 | Core | 1 | Icing supports layer-like accumulation, snow substitution, soft landing behaviour, and suitable melting rules. |
| BLK-008 | Core | 1 | Frozen lemonade has a recognisable sliding profile distinct from ordinary ice while remaining controllable. |
| BLK-009 | Expansion | 3 | Candy glass supplies clear and stained variants with strong silhouettes for boiled-sweet windows. |
| BLK-010 | Core | 3 | Candy-cane pillars support axis placement and form roads, supports, lamp posts, and structure palettes. |
| BLK-011 | Core | 3 | Gingerbread masonry and frosting mortar form the principal settlement building family. |
| BLK-012 | Core | 1 | Suitable soft blocks expose visible bite stages, crumbs, sounds, and particles before being fully consumed. |
| BLK-013 | Core | 1 | Nibbling requires deliberate use, never activates at full hunger, and cannot consume mineral-bearing blocks. |
| BLK-014 | Expansion | 3 | Replenishable plants and crops prevent direct terrain eating from becoming the only sustainable food loop. |

## Foods, Effects, and Kitchen Gadgets

| ID | Priority | Slice | Wishlist |
|---|---|---:|---|
| FOOD-001 | Core | 1 | Sponge slices, icing, simple biscuits, lemonade, and the first prepared starter meal. |
| FOOD-002 | Core | 3 | Gingerbread, gummies, boiled sweets, fudge, caramel, jam, custard, sherbet, sodas, and sundaes. |
| FOOD-003 | Core | 1 | Raw terrain provides low nutrition and low saturation; recipes are always the better long-term choice. |
| FOOD-004 | Expansion | 3 | Sugar Rush gives a short, clearly indicated movement boost without a punitive crash. |
| FOOD-005 | Expansion | 3 | Cocoa Comfort provides gentle recovery or resilience with conservative balance. |
| FOOD-006 | Expansion | 3 | Minty Fresh supports cooling, breathing, or clarity effects appropriate to its recipe. |
| FOOD-007 | Expansion | 3 | Fizzy Feet supports playful jumping or safe movement without causing camera discomfort. |
| FOOD-008 | Core | 6 | Cookbook variety discoveries reward tasting breadth but never weaken repeatedly eaten food. |
| GADGET-001 | Core | 1 | Mixing bowl establishes shapeless kitchen preparation. |
| GADGET-002 | Core | 1 | Oven provides a themed but tag-compatible cooking route. |
| GADGET-003 | Expansion | 3 | Cooling rack finishes fudge, boiled sweets, and temperature-sensitive recipes. |
| GADGET-004 | Expansion | 3 | Piping bag applies icing decoration and detail blocks. |
| GADGET-005 | Expansion | 3 | Rolling pin prepares dough and doubles as a readable village tool. |
| GADGET-006 | Expansion | 3 | Candy cooker handles caramel, boiled sweets, and syrup stages. |
| GADGET-007 | Expansion | 3 | Soda fountain mixes drinks and appears as a settlement workstation. |
| GADGET-008 | Dream | 3 | Wafer windmills and syrup pipes animate villages without becoming an industrial automation mod. |

## Explorer's Cookbook

| ID | Priority | Slice | Feature contract |
|---|---|---:|---|
| BOOK-001 | Core | 1 | Every player can obtain or recraft the Cookbook; losing one cannot destroy progress. |
| BOOK-002 | Core | 1 | Discovery is stored per player and remains server-authoritative in multiplayer. |
| BOOK-003 | Core | 1 | Tabs cover Places, Creatures, Ingredients, Recipes, Landmarks, and Curiosities. |
| BOOK-004 | Core | 1 | Pages unlock from explicit events: visiting, tasting, meeting, mining, crafting, and finding. |
| BOOK-005 | Core | 3 | Undiscovered pages show optional gentle hints without coordinates or compulsory objectives. |
| BOOK-006 | Expansion | 6 | Biome stamps, collection summaries, and completion percentages provide the optional long game. |
| BOOK-007 | Expansion | 6 | Multiplayer discoveries remain individual; a later shared-library block may copy already discovered lore without granting progression. |
| BOOK-008 | Core | 6 | Accessibility includes scalable text, subtitles for discovery sounds, non-colour-only icons, and reduced-motion presentation. |

## Vanilla Creature Replacement Matrix

All replacements are new CakeWorld entity types. They must preserve relevant
vanilla tags, loot roles, advancement hooks, structure responsibilities,
breeding or taming roles, transport roles, and difficulty behaviour
deliberately. Unknown modded entities remain untouched unless a compatibility
document explicitly says otherwise.

| ID | Vanilla entity | CakeWorld replacement | Design role |
|---|---|---|---|
| MOB-001 | `minecraft:axolotl` | Jellylotl | Friendly aquatic helper with translucent flavour variants. |
| MOB-002 | `minecraft:bat` | Bonbon Bat | Harmless cave ambience and wrapped-sweet silhouette. |
| MOB-003 | `minecraft:bee` | Sugar Bee | Pollinator for cupcake and lollipop plants. |
| MOB-004 | `minecraft:blaze` | Cinnamon Spark | Hot floating Nether guardian; harmless knockback on Easy/Normal. |
| MOB-005 | `minecraft:cat` | Custard Cat | Tameable settlement companion. |
| MOB-006 | `minecraft:cave_spider` | Deep Liquorice Weaver | Small cave ambusher using sticky effects rather than poison below Hard. |
| MOB-007 | `minecraft:chicken` | Mallow Chick | Farm animal supplying recipe ingredients and the egg role. |
| MOB-008 | `minecraft:cod` | Soda Cod | Common soda-ocean food fish. |
| MOB-009 | `minecraft:cow` | Cocoa Cow | Farm animal supplying milk and cocoa ingredients. |
| MOB-010 | `minecraft:creeper` | Pop-Rock Popper | Confetti, sound, and knockback explosion below Hard; damaging explosion only on Hard. |
| MOB-011 | `minecraft:dolphin` | Soda Dolphin | Friendly guide and swimming-speed role. |
| MOB-012 | `minecraft:donkey` | Dough Donkey | Rideable pack animal. |
| MOB-013 | `minecraft:drowned` | Soggy Biscuit | Waterlogged mischief creature preserving aquatic ruin roles. |
| MOB-014 | `minecraft:elder_guardian` | Grand Gumball Guardian | Soda Palace boss and mining-fatigue role, non-damaging below Hard. |
| MOB-015 | `minecraft:ender_dragon` | Great Meringue Dragon | End climax, aerial spectacle, exit activation, and repeatable encounter. |
| MOB-016 | `minecraft:enderman` | Taffy Tallwalker | Long-limbed fondant-garden wanderer preserving teleport and carried-block roles safely. |
| MOB-017 | `minecraft:endermite` | Sugar Mite | Tiny teleport-associated nuisance. |
| MOB-018 | `minecraft:evoker` | Sour Sorcerer | Gingerbread Manor caster and raid role. |
| MOB-019 | `minecraft:fox` | Peppermint Fox | Berry-and-sweet hunter with sleeping and trust behaviour. |
| MOB-020 | `minecraft:ghast` | Mallow Floater | Large floating Nether creature firing visible cocoa or spice projectiles. |
| MOB-021 | `minecraft:giant` | Giant Stale Crumbler | Command-only compatibility replacement; not added to normal spawning. |
| MOB-022 | `minecraft:glow_squid` | Glow-Jelly | Luminous aquatic ambience and glowing-ink compatibility role. |
| MOB-023 | `minecraft:goat` | Nougat Goat | Mountain animal preserving ramming and horn roles. |
| MOB-024 | `minecraft:guardian` | Gumball Guardian | Soda Palace defender with clearly signalled beam behaviour. |
| MOB-025 | `minecraft:hoglin` | Fudge Boar | Huntable Nether food animal retaining Hoglin breeding, repellent and knockback roles; protected throws below Hard and real charges on Hard. |
| MOB-026 | `minecraft:horse` | Gingerbread Pony | Primary tameable, saddleable and armour-compatible rideable mount, retaining Horse appearance and attribute inheritance. |
| MOB-027 | `minecraft:husk` | Dried Crumbler | Daylight-safe Sherbet Dunes Crumbler retaining Husk dimensions, attributes, sounds, loot, water conversion and progression roles; dusty harmless contact below Hard, real damage and Hunger on Hard. |
| MOB-028 | `minecraft:illusioner` | Mirage Confectioner | Command-only compatibility caster retaining mirage copies, invisibility, Hard-only blindness, bow AI, raider state and empty loot; harmless obscuring sweet shots below Hard and real arrows on Hard. |
| MOB-029 | `minecraft:iron_golem` | Jawbreaker Guardian | Settlement defender retaining village patrol, hostile targeting, player-created loyalty, flower offering, anger, crack/repair and construction roles; protected slowing bounce below Hard and real launch damage on Hard. |
| MOB-030 | `minecraft:llama` | Meringue Llama | Carpet-decorated caravan and strength-scaled pack-animal role; sticky protected spit below Hard and real one-point spit on Hard. |
| MOB-031 | `minecraft:magma_cube` | Hot-Fudge Blob | Size-scaled, fireproof elastic Nether hazard retaining Magma Cube jumps, splitting, freeze vulnerability and magma-cream progression; sticky protected contact below Hard and real size-scaled collision damage on Hard. Ordinary Fudge Wastes spawns are replaced in the biome list, while fresh literal Nether-Fortress spawns are converted only inside CakeWorld biomes. |
| MOB-032 | `minecraft:mule` | Marzipan Mule | Sterile rideable pack-animal hybrid created by Gingerbread Pony and Dough Donkey from either parent direction; retains inherited physical attributes, chest storage, saddle, rider control, charged jump, food, fall behavior and leather progression without open-biome spawning. |
| MOB-033 | `minecraft:mooshroom` | Cupcake Cow | Red/brown garden cow retaining bowl stew, flower-fed suspicious stew, lightning switching, variant inheritance, shearing and exact food/loot progression. Shearing turns it into Cocoa Cow rather than leaking a vanilla Cow. Its exact Mushroom Fields spawn profile is reserved for Cupcake Gardens. |
| MOB-034 | `minecraft:ocelot` | Sherbet Ocelot | Shy Gummy Jungle and desert-edge hunter retaining fish temptation and one-in-three trust, player avoidance until trusted, stalking poses, chicken and baby-on-land Turtle prey, breeding, conditional despawn, zero fall damage, exact empty loot and progression roles. Pounces are sticky and protective below Hard; Hard restores the exact three-point prey attack. Its dormant Gummy Jungle hook deliberately uses the unusual vanilla Jungle `MONSTER` spawn-cap list at weight 2 in groups of 1-3 even though the entity itself remains a creature; Sherbet Dunes adds a rare 1/1-1 encounter in that same cap list. |
| MOB-035 | `minecraft:panda` | Chocolate Panda | Gummy Jungle Panda retaining genes, moods, rolling, sneezing and Bamboo compatibility while accepting tagged Candy Sprouts and Candy-Cane vegetation; bites inconvenience without health damage below Hard. |
| MOB-036 | `minecraft:parrot` | Lollipop Lorikeet | Tameable flying companion retaining all five colour variants, seed taming, sitting, owner-following, shoulder riding, jukebox dancing, hostile-mob mimicry, cookie danger, feather loot, fall immunity and the deliberate no-baby/no-breeding role. Sprinkle Seeds extend the taming diet, and CakeWorld creatures are added to the mimic repertoire. Its exact Jungle 40/1-2 profile is reserved for Lollipop Orchards and Gummy Jungle. Pecking is sticky and protective below Hard; Hard restores the exact three-point attack. |
| MOB-037 | `minecraft:phantom` | Wafer Wraith | Insomnia-spawned flying nuisance retaining Phantom timing, open-sky/local-difficulty checks, 1-4 Hard-scaled groups, size, circling/swooping, sunlight burning, Cat deterrence, membrane loot and advancement roles. Fresh literal Phantoms are converted only in CakeWorld biomes; loaded and outside-world Phantoms remain untouched. Easy/Normal swoops briefly obscure, slow and gently displace under a complete fall/fire/combat rescue envelope without resetting time-since-rest, so sleep remains the answer without disguised damage. Hard restores real size-scaled damage. |
| MOB-038 | `minecraft:pig` | Truffle Pig | Farm animal retaining Pig breeding, saddle, rider, Carrot on a Stick, loot, lightning, progression and exact Plains 10/4-4 spawn roles. An adult standing on a tagged soft confectionery surface can consume a Simple Biscuit to snuffle up one Cocoa Truffle without breaking or changing the terrain, then rests for one minute before foraging again. Non-Peaceful lightning now completes vanilla's finalized baby, AI, name, persistence and Golden-Sword handoff as MOB-073 Stale Fudge Folk inside CakeWorld terrain; Peaceful retains the Pig. |
| MOB-039 | `minecraft:piglin` | Fudge Folk | Genuine Piglin society role retaining the 1.18.2 brain, inventory, gold equipment, admiration, bartering, hunting, retreat, baby riding, zombification, empty loot and exact Fudge Wastes 15/4-4 spawn role. Fresh literal structure Piglins convert only inside CakeWorld biomes; loaded/outside/third-party entities remain untouched. Easy and Normal sword or crossbow hits become a sticky zero-damage splat with fall/fire rescue, while Hard retains real damage. Both Distract Piglin paths and Monsters Hunted are bridged deliberately. Cross-dimension conversion now finalizes as MOB-073 Stale Fudge Folk with equipment, baby, persistence, Nausea and passenger state retained; the final Foundry presentation remains with STRUCT-018. |
| MOB-040 | `minecraft:piglin_brute` | Fudge Brute | Genuine structure-only Piglin Brute role retaining the 1.18.2 50-health, 0.35-speed, seven-damage and 20-XP body; Brute brain, home memory, always-hostile player and nemesis targeting, 600-tick anger, adult Piglin alliance, guaranteed Golden Axe, Golden-Axe-only pickup, sounds, Peaceful removal, empty loot and cross-dimension zombification. Fresh literal Bastion Brutes convert only inside CakeWorld biomes; loaded/outside/third-party entities remain untouched, and the custom Fudge Folk family is recognised during idle interaction. Easy and Normal axe blows become zero-damage toffee thumps with fall/fire rescue; Hard retains real damage. Monsters Hunted, spawn egg and Lollipop Lorikeet mimic roles are bridged deliberately. Zombification now finalizes as MOB-073 Stale Fudge Folk with Golden Axe, persistence, Nausea and passenger state retained. No open-biome spawn or custom placement is registered; the final Burnt-Toffee Foundry belongs to STRUCT-018. |
| MOB-041 | `minecraft:pillager` | Biscuit Bandit | Genuine Pillager-derived ranged raider retaining the 1.18.2 24-health body, crossbow charging/shooting, five-slot persisted inventory, patrol/captain/banner/Bad Omen roles, outpost spawn override, raid membership/leadership/wave enchanting, Illager alliances, sounds and empty loot. Fresh literal patrol, vanilla Outpost and raid Pillagers convert only inside CakeWorld biomes with patrol, inventory, mount and raid state retained; loaded/outside/third-party entities remain untouched. STRUCT-002's Cookie Forest Biscuit Bandit Lookout now uses the custom type directly for its structure-wide `1 / 1-1` spawn override and begins with four persistent bandits including a banner captain. A narrow visible-hostile repair preserves Pillager's exact 15-block Villager fear role. Easy and Normal bolts become zero-damage obscuring crumb puffs with complete fall/fire rescue; Hard retains real crossbow damage. Raiders tag, Monsters Hunted, Who's the Pillager Now, spawn egg and Lollipop Lorikeet mimic roles are bridged deliberately. |
| MOB-042 | `minecraft:polar_bear` | Vanilla-Ice Bear | Genuine Polar-Bear-derived tundra guardian retaining the 1.18.2 30-health body, neutral persistent anger, adult defence of nearby cubs, baby-to-adult hurt alerting, Fox hunting, standing warning, swimming, panic, parent following, non-food/non-breedable player contract, sounds, exact weighted Cod/Salmon loot and powder-snow/freeze immunity. Its literal offspring factory returns the CakeWorld type for command/mod-driven family creation. A dormant Ice-Cream Tundra hook preserves the snowy-biome 1/1-2 encounter while every current biome remains free of vanilla and custom bears. Peaceful, Easy and Normal warning swipes become zero-damage cushioned vanilla-cream shoves with complete fall/fire rescue; Hard retains the exact six-point attack. A spawn egg and inherited Polar Bear renderer are supplied. Vanilla 1.18.2 assigns no Polar Bear criterion in Monsters Hunted or Two by Two, so CakeWorld deliberately fabricates no advancement credit. |
| MOB-043 | `minecraft:pufferfish` | Fizzball Fish | Genuine Pufferfish-derived Soda Ocean animal retaining the exact three-health body, water goals, three inflation states and scale/timing/cues, bucket capture, exact loot, Axolotl prey and Tactical Fishing roles. Its dedicated Lemonade bucket preserves custom identity, name and from-bucket state. The exact Soda Ocean `5/1-3` profile replaces literal Pufferfish. Peaceful, Easy and Normal contacts are intercepted before vanilla's private poison step and become a zero-health-damage, landing-safe fizzy bounce with complete rescue; Hard retains state-scaled damage and poison. |
| MOB-044 | `minecraft:rabbit` | Gummy Bunny | Genuine Rabbit-derived starter animal retaining all six ordinary climate-weighted colour variants, group spawning, hopping, panic/avoidance, carrots/golden carrots/dandelions, breeding inheritance, mob-griefing-controlled mature-carrot raiding, sounds, exact hide/meat/foot loot and the command-only type-99 combat role. Sprinkle Seeds extend the food and temptation contract. Current Candy Plains and future Chocolate Sponge Meadows use the exact Meadow `2/2-6` profile; future Gummy Jungle uses `4/2-3`. Edible spawn surfaces and powder-snow walking are tagged, Fox predation works through Rabbit inheritance, and a narrow untamed-vanilla-Wolf bridge repairs its literal entity-type predicate. The type-99 name is originalised as Ferocious Gummy Bunny; below Hard its bite becomes a zero-damage elastic rescue bounce, while Hard retains the exact eight-point attack. Two by Two is bridged deliberately. |
| MOB-045 | `minecraft:ravager` | Gingerbread Stomper | Genuine Ravager-derived raid mount retaining the exact 100-health body, attributes, XP, targets, alliances, rider controls, shield stun, delayed roar, navigation, sounds, exact Saddle loot and vanilla wave/rider source. Fresh literal raid Ravagers convert only in CakeWorld biomes with raid, wave, passenger and animation NBT retained; loaded, outside-world and third-party entities remain untouched. Peaceful, Easy and Normal melee and roar impacts cause zero health damage under a complete fall/fire rescue envelope while preserving visible attack/stun/roar and displacement cues; Hard retains exact 12-point melee and 6-point roar damage. All gamerule-mediated leaf, crop and farmland destruction is denied below Hard, with a safe obstacle hop in place of breakage; Hard falls back to the world's `mobGriefing` rule and retains real leaf breaking. Raiders, Monsters Hunted, spawn egg and Lollipop Lorikeet mimic roles are bridged deliberately; it has exact vanilla `NO_RESTRICTIONS` placement metadata but no open-biome spawn. |
| MOB-046 | `minecraft:salmon` | Sherbet Salmon | Genuine Salmon-derived cool-water food fish retaining the exact three-health body, five-fish schools and clusters, follower navigation, panic/player avoidance/random swimming, water and land-flop movement, air/persistence rules, sounds, XP and raw/cooked Salmon plus rare Bone Meal loot. Its dedicated Lemonade bucket preserves identity, name and from-bucket state and bridges Tactical Fishing. The current Soda Ocean and future frozen-lemonade lakes of Ice-Cream Tundra use the exact cold-ocean `15/1-5` profile with literal Salmon excluded; Lemonade extends the vanilla surface-water depth predicate, Axolotls retain the hunt role, and a spawn egg plus inherited renderer are supplied. |
| MOB-047 | `minecraft:sheep` | Candyfloss Sheep | Genuine Sheep-derived starter animal retaining the exact eight-health body, movement, goals, Wheat temptation/breeding, sixteen dye colours, NBT, random natural colour distribution, dye-recipe offspring mixing, shearing readiness and `1-3` colour-matched wool, edible-surface fleece regrowth, baby growth, sounds and exact raw/cooked/looting Mutton plus colour-wool death loot. Coloured vanilla wool remains the deliberately compatible candyfloss-fibre item, preserving every wool tag, recipe and downstream mod role while original fleece art is developed. Every current CakeWorld biome replaces any inherited literal Sheep without adding a new flock where none existed; Candy Plains retains its exact Plains `12/4-4` profile, and future Chocolate Sponge Meadows explicitly uses the same profile. Edible grazing emits crumbs but does not consume the landscape. Two by Two, the spawn egg and inherited colour-aware Sheep renderer are bridged deliberately; as a passive non-attacker it needs no difficulty damage branch. |
| MOB-048 | `minecraft:shulker` | Macaron Clam | Genuine Shulker-derived Macaron Citadel defender retaining the exact thirty-health body, fire immunity, six-face attachment, grid alignment, shell animation and pushing, closed-shell armour and arrow immunity, teleportation, hostile target/peek/attack goals, colour NBT, homing Shulker Bullet, sounds and colour-aware inherited renderer. Fresh literal Shulkers emitted by End City markers convert only inside CakeWorld biomes; loaded entities and unknown third-party worlds remain untouched. Projectile hits on Easy and Normal become harmless glowing Macaron Dust with a gentle lift, slow movement and a complete fall/fire/damage-rescue envelope; Hard preserves the exact four-point hit and ten-second Levitation peril. Open clams struck by their projectile use the vanilla crowding curve but count and create Macaron Clams while retaining shell colour. Exact Shulker Shell loot preserves Shulker Box storage progression, and Monsters Hunted, the spawn egg, placement metadata and Lollipop Lorikeet mimicry are bridged deliberately. Macaron Citadel art and bespoke textures remain with `STRUCT-015`/`BIO-END-005`; this slice uses the inherited Shulker presentation as an honest functional prototype. |
| MOB-049 | `minecraft:silverfish` | Crumb Mite | Genuine Silverfish-derived edible-cave nuisance retaining the exact eight-health arthropod body, movement and target goals, powder-snow climbing, five-block player exclusion in its placement predicate, sounds, empty loot and inherited renderer. Fresh literal Silverfish from Stronghold spawners and broken/exploded infested blocks convert only inside CakeWorld biomes with saved state retained; loaded entities and unknown third-party worlds remain untouched. A true InfestedBlock counterpart lets inherited merge and friend-waking AI hide in Biscuit Stone and later reveal Crumb Mites, while Silk Touch recovers ordinary Biscuit Stone. Easy and Normal bites scatter harmless glowing crumbs with a gentle lift and complete fall/fire/damage rescue; Hard retains the exact one-point bite. Easy and Normal also deny mob-griefing merge/wake changes so possessions cannot become nests and hurt mites quietly clear nearby infestations back to their host; Hard follows the world's `mobGriefing` rule and preserves real wake chains. Monsters Hunted, powder walking, the spawn egg, placement metadata and Lollipop Lorikeet mimicry are bridged deliberately. Ancient Cake Vault placement and bespoke mite/nest art remain with `STRUCT-011`; this slice uses inherited presentation as an honest functional prototype. |
| MOB-050 | `minecraft:skeleton` | Candy-Cane Archer | Genuine Skeleton-derived ranged mischief creature retaining the exact twenty-health undead body, sunlight and helmet rules, bow equipment and enchantments, 40/20-tick Normal/Hard firing intervals, aiming and inaccuracy, melee fallback, Wolf avoidance and hostile targeting, freeze-conversion NBT, sounds, exact Arrow/Bone loot and charged-Creeper skull role. Every inherited Skeleton profile in CakeWorld biomes is replaced without inventing a spawn where none existed; fresh literal spawner, jockey and skeleton-trap riders convert with state, equipment and vehicle relationships retained. Peaceful despawns normally; Easy and Normal arrows and melee hits become zero-damage sticky peppermint splats with a complete fall/fire rescue envelope, while Hard retains exact vanilla damage. The `minecraft:skeletons` tag preserves Creeper music-disc progression, and Monsters Hunted, Sniper Duel, the spawn egg, placement metadata and Lollipop Lorikeet mimicry are bridged deliberately. Freeze conversion retains the inherited intermediate Stray transition and then hands that fresh result to `MOB-056` without losing state. Bespoke art remains future presentation work; the inherited Skeleton renderer is an honest functional prototype. |
| MOB-051 | `minecraft:skeleton_horse` | Brittle Biscuit Steed | Genuine Skeleton-Horse-derived undead mount retaining the exact fifteen-health body, speed and jump attributes, dry-land Horse goals, sounds, underwater riding and slowdown, taming, saddle, rider control, charged jump, no-armour and no-breeding roles, one-to-three experience and exact Bone loot. Vanilla thunder remains the source of truth: with mob spawning enabled its local-difficulty chance creates a trap that detects a living player within ten blocks, expires after 18,000 unopened trap ticks, then emits visual-only lightning and produces four tamed adult mounts with four persistent, temporarily protected and equipped Candy-Cane Archer riders; the three newly created mounts retain vanilla persistence and temporary protection. Fresh literal original and child Skeleton Horses convert only inside CakeWorld biomes with trap timer, tame/saddle inventory, name, health, invulnerability, passengers and vehicle relationships retained; loaded entities, outside worlds and unknown third-party mounts remain untouched. Exact vanilla placement metadata is registered without adding an ordinary biome spawn profile. The spawn egg and a safe inherited-art renderer are supplied; no advancement or Parrot-mimic role is invented because vanilla assigns neither. As a passive non-attacker it requires no difficulty damage branch. Natural thunder observation and bespoke brittle-biscuit art and sounds remain presentation evidence, so this is an honest functional prototype. |
| MOB-052 | `minecraft:slime` | Jelly Blob | Genuine Slime-derived elastic cave and future Caramel Bogs creature retaining the exact size-one-to-127 NBT and body, health, speed, damage and experience scaling; squish, jumping and movement control; Player and Iron-Golem targets; small/large sounds; two-to-four same-type half-size children with name, persistence, AI and invulnerability retained; and size-one-only Slime-Ball loot for sticky-piston, lead, Slime Block and Magma Cream compatibility. Every inherited Slime profile in a CakeWorld Overworld biome is replaced without inventing a new profile, preserving the current common `100/4-4` groups and exact seeded slime-chunk/Y-below-40 predicate; Fudge Wastes and Meringue Islands remain empty. Future `caramel_bogs` receives the vanilla Swamp surface, height, moon and light branch by stable biome ID. Peaceful removes Jelly Blobs normally, tiny blobs remain harmless on every difficulty, and larger Easy/Normal contact becomes a zero-damage elastic rescue bounce with sticky/visible inconvenience and a complete fall/fire/damage safety envelope; Hard retains exact size-scaled contact damage. Exact placement metadata, the spawn egg, Slime-Ball loot, Slime renderer, Monsters Hunted bridge and Lollipop Lorikeet mimic are supplied deliberately. Natural slime-chunk and future Caramel Bogs encounters plus original blob art and sounds remain presentation evidence, so this is an honest functional prototype. |
| MOB-053 | `minecraft:snow_golem` | Ice-Cream Golem | Genuine Snow-Golem-derived player-built helper retaining the exact four-health body, movement and persistent/fall-immune golem role; ranged attack, stroll, look and Enemy-target goals; vanilla Snowball trajectory and firing sound; water sensitivity and one-point-per-step hot-biome melting; powder-snow path immunity; pumpkin NBT, Forge shearing and carved-pumpkin return; ambient, hurt and death sounds; leash position; zero experience; and exact zero-to-fifteen Snowball loot. The ordinary two-Snow-Block and carved-pumpkin pattern remains valid: fresh literal golems built or commanded inside a CakeWorld biome defer-convert after the pattern finishes, retaining health, pumpkin, name, persistence, AI, invulnerability, temporary protection, riding, passengers or leash state; loaded entities, outside worlds and unknown third-party golems remain untouched. The inherited `mobGriefing` gate remains authoritative, but only Snow layers created by that golem during the current step translate to CakeWorld Icing Layers, so existing snow and possessions are not rewritten. Vanilla Snowballs remain harmless to ordinary targets; on Easy and Normal even their three-point Blaze-family exception becomes a zero-damage slowing, glowing, weakening scoop, while Hard retains the exact exception. Exact dormant placement metadata, Snowball loot, spawn egg and inherited renderer are supplied. Vanilla 1.18.2 defines no Snow Golem advancement or Parrot mimic, so neither is invented. Hands-on construction/shearing play and original ice-cream art and sounds remain presentation evidence, making this an honest functional prototype. |
| MOB-054 | `minecraft:spider` | Liquorice Weaver | Genuine Spider-derived nocturnal climber retaining the exact sixteen-health arthropod body, movement and two-point bite; WallClimberNavigation, horizontal-collision climbing state and half-height passenger offset; Float, Leap, brightness-sensitive attack, stroll, look, retaliation, Player and Iron-Golem goals; cobweb and poison immunity; sounds; one-percent Skeleton jockey; Hard-only local-difficulty Speed, Strength, Regeneration or Invisibility effect; and exact String/Spider-Eye loot. Every inherited Spider profile in a CakeWorld biome is replaced without inventing new ecology, while fresh literal natural, spawner and jockey Spiders convert with saved effects, state and relationships retained; loaded entities, outside worlds and unknown third-party mobs remain untouched. Peaceful removes Weavers normally. Easy and Normal bites become zero-damage visible string splats with slowing, mining inconvenience and a complete fall/fire/damage rescue envelope, without placing Cobwebs or changing possessions; Hard preserves the exact vanilla bite. Exact placement metadata, spawn egg, inherited renderer, Monsters Hunted bridge and Lollipop Lorikeet mimicry are supplied deliberately. Natural nocturnal observation and original liquorice art and sounds remain presentation evidence, so this is an honest functional prototype. |
| MOB-055 | `minecraft:squid` | Liquorice Squid | Genuine Squid-derived passive Soda Ocean creature retaining the exact ten-health Water-animal body, dimensions, tracking and one-to-three experience; underwater breathing, dry-air damage, no fluid pushing, leashability and 120-tick ambience; random-swim and close-attacker flee goals; tentacle animation, direct aquatic travel, movement sound events; four exact Squid sounds; and the thirty-particle dark-ink defence. Soda Ocean's inherited weight-one, group-one-to-four Squid school is replaced without inventing profiles elsewhere. The vanilla surface-water height band is preserved while its literal-Water check is deliberately generalized to the water fluid tag so CakeWorld Lemonade works. Fresh literal natural, command and spawner Squid convert only inside CakeWorld biomes with health, air, name, persistence, AI, invulnerability, leash, vehicle and passengers retained; loaded entities, outside worlds and unknown third-party mobs remain untouched. Exact one-to-three-plus-Looting Ink Sac loot preserves dye and Book-and-Quill recipes, and Axolotl/Guardian predator compatibility remains deliberate through the prey tag and genuine Squid inheritance. Vanilla assigns Squid no attack, bucket, breeding, Monsters Hunted criterion or Parrot mimic, so CakeWorld invents none; the passive role needs no difficulty damage branch. Natural Soda Ocean school observation and original liquorice art and sounds remain presentation evidence, making this an honest functional prototype. |
| MOB-056 | `minecraft:stray` | Frosted Archer | Genuine Stray-derived chilled archer retaining the exact twenty-health undead Skeleton body; sunlight and helmet response; bow equipment and melee switching; 40/20-tick Normal/Hard firing intervals; hostile targets; four Stray sounds; six-hundred-tick Slowness arrows; powder-snow-column and sky-visible spawn predicate; powder-snow and freezing immunity; and exact Arrow, Bone and player-killed Slowness-tipped-Arrow loot. Fresh literal Strays, including the inherited intermediate result of Candy-Cane Archer freezing, convert only inside CakeWorld biomes with health, name, persistence, AI, equipment, invulnerability, leash, vehicle and passenger state retained; loaded, outside-world and third-party entities remain untouched. Existing Stray profiles are replaced without fabrication, and all current biomes deliberately remain empty until Ice-Cream Tundra supplies real ecology. Peaceful removes Frosted Archers normally. Easy and Normal arrows and melee become zero-damage visible chills with slowing, outline and a complete fall/fire/damage-rescue envelope; Hard retains exact incoming arrow and melee damage. The Skeleton-family and freeze-immunity tags, exact placement metadata, spawn egg, inherited Stray renderer, Monsters Hunted credit and Lollipop Lorikeet Stray mimic are supplied deliberately. Sniper Duel remains literal-Skeleton-only exactly as vanilla defines it. Natural tundra observation and original frosted art and sounds remain presentation evidence, making this an honest functional prototype. |
| MOB-057 | `minecraft:strider` | Fudge Skater | Genuine Strider-derived passive hot-fluid mount retaining the exact twenty-health fire-immune body, movement and follow attributes, cold shiver and speed/steering modifiers, saddle/boost NBT, Warped Fungus food and temptation, rider control, dismount safety, floating/fall reset, sounds, water weakness, one-in-thirty Zombified-Piglin jockey and subsequent one-in-ten baby-Skater jockey branches, and exact two-to-five-plus-Looting String loot. Fudge Wastes' inherited weight-sixty group-one-to-two profile is replaced without inventing ecology elsewhere. CakeWorld Hot Fudge deliberately joins the Lava fluid and Strider-warm-block roles; the two vanilla literal-Lava navigation checks are generalized to that shared fluid tag, preserving vanilla Lava too. Fresh literal natural, spawner, command and jockey Striders convert only inside CakeWorld biomes with health, age, saddle, name, persistence, AI, invulnerability, leash, vehicle and passengers retained; loaded, outside-world and third-party entities remain untouched. The vanilla Warped Fungus on a Stick keeps directional control, one-point boost durability and Fishing-Rod break replacement despite its literal-entity gate. Two by Two, This Boat Has Legs and Feels Like Home receive deliberate custom-type bridges. Exact IN_LAVA placement, spawn egg and inherited renderer are supplied; Strider has no attack, Monsters Hunted criterion or Parrot mimic, so none is invented. Natural Fudge Wastes riding observation and original skater art and sounds remain presentation evidence, making this an honest functional prototype. |
| MOB-058 | `minecraft:trader_llama` | Sprinkle Llama | Genuine Trader-Llama-derived roaming caravan animal retaining the exact Llama body, attributes, strength-one-to-five pack capacity, four variants, chest and carpet inventory, Wheat/Hay food, temper/taming, uncontrolled riding, breeding calculation, sounds, visible Llama Spit and exact Leather loot. Trader-specific behaviour remains authoritative: EVENT sources are adults, the extra speed-two panic and trader-defence target coexist with ordinary Llama AI, trader leashes block mounting and synchronize despawn delay, while taming, a non-trader leash or exactly one player passenger suspends despawn. Fresh literal caravan animals spawned inside CakeWorld biomes defer-convert one tick so the wandering-trader spawner can attach its lead first; saved state, inventory, delay and every valid leash, passenger or vehicle relationship are retained, while loaded, outside-world and third-party entities remain untouched. The literal-type caravan goal alone is replaced by the subclass-friendly equivalent shared with Meringue Llama, and same-family offspring receive a deliberate Two by Two bridge. Peaceful, Easy and Normal spit becomes a zero-damage sticky sprinkle splat with a complete fall/fire/damage-rescue envelope; Hard retains exact one-point projectile damage. Vanilla's `NO_RESTRICTIONS` placement metadata, spawn egg and Trader-Llama renderer layer are supplied without inventing a biome spawn profile, kill criterion or Parrot mimic. MOB-064 remains responsible for the Travelling Confectioner itself; natural caravan observation and original art/sounds remain presentation evidence, making this an honest functional prototype. |
| MOB-059 | `minecraft:tropical_fish` | Jellybean Fish | Genuine Tropical-Fish-derived Soda Ocean animal retaining the exact three-health Water-Ambient body, eight-fish clusters and schools, panic/player-avoidance/random-swim/follower goals, water and land-flop movement, air and persistence rules, sounds, XP and exact Tropical Fish plus rare Bone Meal loot. Vanilla's complete packed variant engine remains authoritative: twelve body/pattern shapes, fifteen generated base colours, fifteen generated pattern colours, all 2,700 combinations, twenty-two common named forms, same-variant common schools, rare solitary forms, textures and `Variant`/`BucketVariantTag` persistence. Its dedicated Lemonade bucket preserves name, health, flags, from-bucket state and the exact predefined-name or type-and-colour tooltip, bridges Tactical Fishing and remains a valid Axolotl/Jellylotl food with Lemonade returned after feeding. Soda Ocean uses the exact warm-ocean `25/8-8` profile with literal Tropical Fish excluded; the Lush-Caves exception and thirteen-block surface band remain exact while only the literal-Water checks generalize to the water fluid tag. Fresh exact-type natural, command, spawner and vanilla-bucket sources defer-convert only inside CakeWorld biomes with packed variant, bucket, health, air, name, persistence, AI, invulnerability, vehicle and passenger state retained; loaded, outside-world and third-party entities remain untouched. Axolotl prey, spawn egg, placement metadata and the inherited colour-and-pattern renderer are supplied deliberately. Tropical Fish has no attack, breeding, Monsters Hunted criterion or Parrot mimic, so none is invented. Jellybean Archipelago ecology belongs to its later biome slice; natural Soda Ocean school observation and original jellybean art and sounds remain presentation evidence, making this an honest functional prototype. |
| MOB-060 | `minecraft:turtle` | Wafer Turtle | Functional prototype: a genuine Turtle-derived entity retains the thirty-health amphibious body, home/travel nesting cycle, Seagrass breeding, one-to-four vanilla Turtle Eggs, moonlit hatching, baby home assignment, Scute/Turtle-Shell progression, sounds, navigation, predator compatibility and exact loot. Biscuit Sand and Biscuit Crumbs are edible nesting surfaces; fresh literal Turtle sources and hatchlings convert only in CakeWorld biomes, while loaded, outside and third-party entities remain untouched. The exact Beach `5/2-5` Custard Coast ecology is dormant until BIO-OW-017 exists; original presentation and natural nesting play remain. |
| MOB-061 | `minecraft:vex` | Sour Sprite | Functional prototype: a genuine Vex-derived summoned nuisance retains the fire-immune fourteen-health, four-attack, three-XP flying body; no-physics/no-gravity movement; owner-target copying; charge and bound-origin wandering goals; Iron Sword equipment; full-bright charging texture; sounds; bound origin; 30-to-119-second limited life and starvation decay; exact empty loot; Monsters Hunted and Lollipop-Lorikeet mimic roles. Fresh literal Vex summons defer-convert only inside CakeWorld biomes with owner, target, charging, life, equipment, state and riding relationships retained; loaded, outside and third-party entities remain untouched. Easy and Normal contact becomes the visible protected sour surprise shared with Sour Sorcerer, while Hard keeps real four-point damage. No natural ecology or placement is invented; original presentation and observed in-play summoning remain. |
| MOB-062 | `minecraft:villager` | Gingerbread Folk | Functional prototype: a genuine Villager-derived settlement resident retains the twenty-health persistent body; door-aware navigation; full profession, type, level, trade, demand, restock and XP system; eight-slot food/inventory contract; daily work, meeting, rest, play, idle, panic, raid and hiding schedules; POI memories; gossip and player reputation; hero gifts; golem discussions; lightning conversion; Zombie-Villager cure output and vanilla/Forge trade compatibility. A narrow additive brain bridge replaces only vanilla's literal-type social blind spots so Gingerbread Folk can look at, meet, gossip with and perform real bed-backed breeding with their own family; same-family children keep vanilla's biome/parent type probabilities. Fresh literal structure, cure, breeding, command, egg and spawner Villagers defer-convert only in CakeWorld biomes with brain, POI, profession, offers, inventory, food, gossip, XP, restock, trading and riding state retained; loaded, outside and third-party villagers remain untouched. Exact dormant Villager placement metadata and a testing egg are supplied, but no natural spawn profile, hostile kill role or Parrot mimic is invented. Gingerbread professions, trades, presentation and complete villages remain later Living Sweetlands work, making this an honest functional prototype. |
| MOB-063 | `minecraft:vindicator` | Rolling-Pin Raider | Functional prototype: a genuine Vindicator-derived Illager retains the exact twenty-four-health, five-attack Monster body; five base XP plus the inherited one-to-three equipped-item bonus; randomized follow range; Iron Axe and raid Sharpness buffs; patrol, captain, raid-wave and leader-banner roles; Ravager riding; village door, hold-ground, melee, Johnny and target goals; crossed-arm, attack and celebration poses; sounds; exact player-killed Emerald loot; raider tag, Monsters Hunted credit and Lollipop-Lorikeet mimic. Vanilla raid waves, Ravager seats and mansion markers remain authoritative literal-Vindicator sources; fresh results defer-convert only inside CakeWorld biomes with Johnny, patrol, raid, leader, target, equipment, invulnerability, vehicle and passenger state retained, while loaded, outside and third-party entities remain untouched. A narrow awareness bridge repairs the vanilla Villager hostile-sensor literal-type seam. Peaceful removes the monster normally; Easy and Normal melee becomes a visible zero-damage rolling-pin shove with slowing, outline and complete fall/fire/damage rescue, while Hard retains real axe and enchantment damage. Inherited Normal/Hard door breaking is deliberately narrowed to Hard to protect possessions. No open-biome ecology is invented; original presentation and observed raid, mansion and rider play remain. |
| MOB-064 | `minecraft:wandering_trader` | Travelling Confectioner | Genuine Wandering-Trader-derived roaming merchant retaining the exact twenty-health Creature body, ground navigation, hostile avoidance, panic, trade, wandering, restriction and look AI, night Invisibility Potion and daytime Milk Bucket routines, voice and drinking sounds, trading interaction/statistics, three-to-six trade XP, advancement hooks, empty loot and pause-while-trading despawn. Its six vanilla offers remain Forge-extensible through the inherited trade event; one rotating CakeWorld snack and a scarce low-use Sprinkle Seed offer are appended idempotently. Fresh literal Wandering Traders inside CakeWorld terrain defer-convert after the vanilla caravan spawner finishes, retaining saved offers and uses, inventory, despawn and wander state, health, name, persistence, invulnerability, active trading player, hurt memory, runtime restriction, vehicle and passengers; the world-level active-trader UUID and all dependent caravan leads move to the replacement. Its two literal Trader Llamas independently become Sprinkle Llamas and remain leashed, including the inherited trader-defence, mount-blocking and synchronized-despawn roles. Loaded, outside-world, custom-subclass and direct non-literal sources remain untouched. A spawn egg, inherited renderer and Cookbook meeting discovery are explicit; vanilla's default `NO_RESTRICTIONS` placement metadata is preserved without inventing biome ecology, a kill role or a Parrot mimic. Natural caravan observation and original art/sounds remain presentation evidence, making this an honest functional prototype. |
| MOB-065 | `minecraft:witch` | Bitter Baker | Genuine Witch-derived potion specialist retaining the exact body, attributes, self-drinking choices and speed penalty, magic resistance, sounds, raid support, exact loot and inherited natural `5/1-1` profiles. Fresh literal natural, raid and swamp-hut Witches convert only inside CakeWorld biomes with saved state, runtime targets, active raid membership, riding relationships and even a mid-sip potion action retained; loaded, outside-world and third-party types remain untouched. A narrow literal-type repair keeps naturally spawned Bakers out of later raids and prevents Bakers from choosing Witch-family allies as healing targets, while ordinary injured Raiders still receive Healing or Regeneration. Peaceful removes the monster normally; Easy and Normal hostile throws become visible Slowness mixtures whose entire splash area receives fall, fire and damage rescue; Hard retains the genuine context-sensitive Harming, Slowness, Poison and Weakness attack. Exact placement, raider tag, testing egg, inherited Witch renderer, Lollipop-Lorikeet mimic and Monsters Hunted credit are supplied deliberately. Original art/sounds, a natural or swamp-hut encounter and hands-on splash play remain presentation evidence, so this is a functional prototype rather than a finished product feature. |
| MOB-066 | `minecraft:wither` | Burnt-Sugar Tempest | Genuine Wither-derived summoned boss retaining the complete three-headed flying AI, 300-health body, armour, 220-tick summoning charge and healing, boss bar, regeneration, powered half-health phase and arrow immunity, skull volleys, block-breaking boundary, Wither immunities, sounds, 50 XP and extended-life Nether Star for beacon progression. Vanilla's Soul Sand/Wither-Skeleton-Skull pattern remains authoritative, so Withering Heights triggers on the literal Wither before a fresh boss in CakeWorld terrain defer-converts with its charge, NBT, main and alternative targets and passengers intact; loaded, outside-world and third-party types remain untouched. Peaceful keeps vanilla removal and summoning rules. Easy and Normal stop direct, skull and explosion damage before Wither can be applied, cancel boss and owned-skull explosions, deny boss griefing, protect possessions and turn hits into a visible slowing, glowing gust with landing, fire and follow-on damage rescue. Hard releases the complete vanilla boss peril. Exact fire/Wither-Rose immunity, placement, testing egg, inherited Wither renderer, empty loot table plus inherited Nether-Star drop, Lollipop-Lorikeet mimic and Monsters Hunted credit are explicit; the boss has no natural ecology. Original model/texture/sounds and hands-on full-fight presentation remain, so this is a functional prototype rather than finished product art. |
| MOB-067 | `minecraft:wither_skeleton` | Burnt-Candy Knight | Genuine Wither-Skeleton-derived fortress defender retaining the exact twenty-health undead body, attributes and randomized follow-range bonus; lava path malus; sun, Wolf, player, Golem, baby-Turtle and Abstract-Piglin goals; Stone Sword equipment and four-point finalized attack; optional Bow reassessment and burning arrows; sounds, five base XP, Wither immunity and ten-second Wither melee. Nether Fortress deliberately remains the authoritative literal `8 / 5-5` structure source: fresh exact-type fortress, command, egg and spawner entities defer-convert only inside CakeWorld biomes with health, equipment, name, persistence, AI, invulnerability, combat targets, vehicle and passengers retained, while loaded, outside-world and third-party entities remain untouched. No open-biome spawn profile is invented. Peaceful keeps vanilla Monster removal; Easy and Normal stop melee and owned-arrow damage before Wither or fire can be applied and add a visible sticky shove with complete fall, fire and follow-on rescue, while Hard preserves real damage and Wither peril. Exact Coal/Bone/player-kill skull loot plus the special one-per-charged-Creeper skull route preserve `A Spooky Scary Skeleton`, the vanilla Soul-Sand/skull Tempest summon and Nether-Star/Beacon progression. The skeleton role tag, testing egg, placement metadata, inherited Wither-Skeleton renderer, Lollipop-Lorikeet mimic and Monsters Hunted credit are explicit. Natural fortress observation and original burnt-candy knight art and sounds remain presentation evidence, making this an honest functional prototype. |
| MOB-068 | `minecraft:wolf` | Ginger-Snap Hound | Functional prototype: a genuine Wolf-derived tameable companion retains the exact eight-health body, movement and attack attributes, navigation, wild and tame goals, pack anger, owner defence, begging, sitting and following, bone taming, red default and dyeable collars, wet shaking, sounds, meat healing, non-player damage reduction, breeding and persistent owner state. An additive food tag accepts Simple Biscuits without removing vanilla meat, and one parallel prey goal repairs Wolf's literal-type predicate so wild Hounds can still hunt Candyfloss Sheep, Gummy Bunnies and Peppermint Foxes. Cookie Crumb Forest inherits Wolf's exact `5 / 4-4` profile and every future CakeWorld profile containing Wolves is replaced automatically; fresh exact-type command, egg, spawner and retained-source Wolves defer-convert only inside CakeWorld biomes with health, owner, tame/sit, collar, anger, targets, hurt memory, leash, vehicle and passenger state retained, while loaded, outside-world and third-party entities remain untouched. Peaceful retains peaceful animal behaviour. Easy and Normal bites deal no health damage and instead give a visible shake-cue shove, slowing and outline with complete landing, fire and follow-on damage rescue; Hard preserves the exact four-point Wolf bite. The testing egg, exact Wolf placement rule, inherited Wolf renderer, empty loot and `Best Friends Forever` remain native; a narrow `Two by Two` bridge credits the vanilla Wolf breeding criterion because the custom child type cannot satisfy its literal predicate. Minecraft 1.18.2 has no Wolf entry in `Monsters Hunted`, so no kill credit or Parrot mimic is invented. Original hound art and sounds plus natural pack, taming and companion observation remain presentation evidence, making this an honest functional prototype. |
| MOB-069 | `minecraft:zoglin` | Stale Fudge Boar | Genuine Zoglin-derived Overworld conversion role retaining the exact forty-health fireproof undead body, broad hostile Brain, adult and baby attack/cooldown/throw differences, sounds, leashing, five XP and Rotten Flesh loot. Fudge Boars now complete their inherited 301-tick conversion as Stale Fudge Boars with finalized baby, equipment, vehicle, Nausea, leash and passenger state retained; fresh literal Zoglins convert only inside CakeWorld biomes, while loaded, outside-world and third-party entities remain untouched. The custom family is excluded from its own indiscriminate targeting alongside vanilla Zoglins and Creepers. Peaceful removes the Monster normally; Easy and Normal contact becomes a zero-damage slowing, glowing protected shove with fall/fire rescue, while Hard retains randomized adult damage and launch or the baby half-point bite. Monsters Hunted, the testing egg, default placement and Lollipop-Lorikeet mimic roles are bridged deliberately. No open-biome ecology is invented because vanilla 1.18.2 supplies none; original presentation and an observed natural conversion remain future evidence, so this is an honest functional prototype. |
| MOB-070 | `minecraft:zombie` | Stale Crumbler | Genuine Zombie-derived common night creature retaining the exact twenty-health undead body, attributes, dimensions, tracking, goals, targets, ground navigation, sunlight and helmet behaviour, sounds, equipment rolls, baby state and speed, chicken-jockey support, reinforcement rules, Hard-only door breaking, pickup, drowning and villager-conversion roles. Every currently shipped CakeWorld Overworld biome replaces the literal Zombie profile with Stale Crumblers at the exact `95 / 4-4` weight and group size; fresh exact-type command, egg, spawner and retained-source Zombies defer-convert only in CakeWorld biomes with health, baby, equipment, targets, hurt memory, leash, vehicle and passengers retained, while loaded, outside-world and third-party entities remain untouched. Peaceful removes the Monster normally. Easy and Normal contact becomes a zero-damage sticky shove with visible slowing and outline plus complete landing, fire and follow-on damage rescue, and mob griefing is denied so Crumblers cannot trample Turtle eggs or collect player possessions. Hard restores the exact three-point attack, burning contact, equipment pickup, reinforcements and ordinary mob-griefing rules. Inherited drowning resolves to Soggy Biscuit with finalized baby, equipment and riding relationships retained. The first two loot pools exactly preserve Zombie Rotten Flesh and rare player-kill Iron Ingot, Carrot and Potato roles, while a guaranteed Cookie is the themed addition; charged-Creeper Zombie Head, exact placement, testing egg, inherited Zombie renderer, Lollipop-Lorikeet mimic and Monsters Hunted credit are explicit. Original art and sounds plus natural night, reinforcement and jockey observation remain presentation evidence, making this an honest functional prototype. |
| MOB-071 | `minecraft:zombie_horse` | Stale Gingerbread Steed | Genuine Zombie-Horse-derived command-only compatibility mount retaining the exact fifteen-health undead body, fixed speed, randomized finalized jump strength, dimensions, tracking, dry-land Horse goals, zero targets, ground navigation, sounds, effect immunities, XP, temper and two-slot inventory. Untamed interaction remains inert because vanilla 1.18.2 supplies no survival taming path; command/NBT-tamed adults accept a saddle, rider control, jumping and leashing but no armour, while ordinary Horse food mounts rather than heals them. Tamed babies retain vanilla's subtler Animal-level food growth without Horse healing or temper. The role remains sterile, but its public command-only offspring factory is repaired to return the CakeWorld type instead of leaking a literal Zombie Horse. Exact Horse fall and mounted-passenger damage remain real. Fresh exact-type command, egg and spawner Zombie Horses defer-convert only inside CakeWorld biomes with health, tame/owner/temper/saddle, age, name, persistence, AI, invulnerability, targets, hurt memory and all legitimate leash, vehicle and passenger relationships retained; loaded, outside-world and third-party types remain untouched. Vanilla supplies no open-biome ecology, progression criterion or Parrot mimic role, so none is invented. The exact testing egg, placement metadata, empty native loot plus separately dropped saddle inventory and inherited Zombie-Horse renderer are explicit. Original gingerbread-steed art and sounds plus hands-on command/NBT mounting remain presentation evidence, making this an honest functional prototype. |
| MOB-072 | `minecraft:zombie_villager` | Crumbled Gingerbread Folk | Functional prototype: a genuine Zombie-Villager-derived curable settlement inhabitant retaining the exact twenty-health undead body, attributes, dimensions, tracking, five XP, Zombie goals and targets, sunlight and door behavior, deliberate no-water-conversion rule, baby state, distance persistence, sounds and empty skull role. Every currently shipped CakeWorld Overworld biome replaces the literal Zombie-Villager profile at the exact `5 / 1-1` weight and group size; Nether and End ecology remain absent. Fresh exact-type command, egg, spawner and retained-source Zombie Villagers defer-convert only inside CakeWorld biomes with active cure timer/player, Villager data, offers, gossip, XP, health, equipment, name, persistence, AI, invulnerability, targets, hurt memory, vehicle and passengers retained; loaded, outside-world and third-party types remain untouched. Stale Crumbler kills now complete the inherited Normal/Hard Villager-infection path as Crumbled Gingerbread Folk, while Weakness plus Golden Apple, the private `3600-6000` cure timer, Iron-Bars/Bed acceleration, Strength, cure sound, Zombie Doctor and reputation remain vanilla-authoritative and finish as Gingerbread Folk. CakeWorld snapshots leash/passenger state around Forge's conversion events and explicitly restores the saved gossip list around vanilla 1.18.2's mismatched NBT type check; it deliberately preserves vanilla's separate cursed-equipment slot-index quirk rather than inventing a different cure outcome. Peaceful removes the Monster normally; Easy and Normal contact becomes the same visible zero-damage sticky rescue used by Stale Crumblers, while Hard retains the exact three-point Zombie attack and ordinary griefing. Exact Zombie-Villager Rotten Flesh and rare Iron/Carrot/Potato loot, testing egg colors, placement metadata, inherited renderer, Lollipop-Lorikeet mimic, Monsters Hunted credit and recovery progression are explicit. Original crumbled-folk art and sounds plus natural infection and hands-on curing remain presentation evidence, making this an honest functional prototype rather than a finished product feature. |
| MOB-073 | `minecraft:zombified_piglin` | Stale Fudge Folk | Functional prototype: a genuine Zombified-Piglin-derived neutral undead retains the exact twenty-health fireproof body, 0.23 movement, five-point attack, two armour, zero reinforcement chance, eight-point lava malus, golden-sword equipment, baby body and riding offset, item pickup, no-water-conversion rule, sounds, empty skull role and five XP. Its inherited neutral system keeps targeted and universal anger, the exact 20-39 second persisted timer, angry speed boost and sound, bed prevention, same-family hurt alerts and periodic visible-target alerts. Every CakeWorld biome replaces an inherited literal profile without inventing a new one: Fudge Wastes retains Nether Wastes' exact `100 / 4-4` profile while current Overworld and End biomes remain empty. Fresh exact-type command, egg, spawner and Truffle-Pig lightning results defer-convert only inside CakeWorld biomes with health, baby, equipment, Nausea, anger, targets, hurt memory, vehicle and passengers retained; loaded, outside-world and third-party subtypes remain untouched. Fudge Folk and Fudge Brutes finish their vanilla 301-tick unsafe-dimension conversion directly as this role without copying the private timer. Peaceful removes the Monster normally; Easy and Normal attacks become zero-damage sticky shoves with complete fall, fire and follow-on rescue and possession pickup is denied, while Hard retains the exact five-point attack and ordinary griefing. Exact Rotten Flesh, Gold Nugget and rare player-kill Gold Ingot loot, vanilla egg colours, placement metadata, configured Zombified-Piglin renderer and Monsters Hunted credit are explicit. Minecraft 1.18.2 gives Zombified Piglins no Parrot mimic entry, so CakeWorld deliberately invents none. Original stale-fudge art and sounds plus natural Nether and cross-dimension encounters remain presentation evidence, making this an honest functional prototype. |

Target-version note for MOB-023: Java 1.18.2's Goat horn contract is the
visible adult model only. Horn items, horn drops, and horn loss were added
after this target version, so CakeWorld does not invent a false vanilla
compatibility contract by backporting them.

Target-version note for MOB-025: vanilla 1.18.2 Piglin and Hoglin sensors
recognize the Fudge Boar subclass, and CakeWorld repairs the hard-coded
same-family mating type locally. A few later Piglin hunt bookkeeping branches
still compare the literal vanilla Hoglin type; the main Piglin hunt-target
sensor is preserved and verified without copying vanilla Piglin AI. MOB-069
now replaces the finalized vanilla Zoglin result after inherited conversion,
preserving its state and relationships without copying Hoglin's private
timer. The remaining Piglin bookkeeping seam stays visible in the evidence
ledger for Fudge Folk work.

Delivery dependencies for MOB-070: inherited Zombie drowning completes as
MOB-025 Soggy Biscuit, and MOB-072 now replaces the finalized
`ZombieVillager` infection result while leaving the private infection and cure
algorithms authoritative. Jockey initialization can use a nearby MOB-007
Mallow Chick and preserves that relationship during conversion; vanilla's
private fallback factory can still create a literal Chicken when no suitable
mount exists, so MOB-007 retains that fresh-source replacement seam. Neither
dependency requires copying a private conversion timer.

Target-version note for MOB-071: Minecraft 1.18.2 includes a Zombie Horse spawn
egg and complete mount mechanics but no natural encounter or ordinary survival
taming path. CakeWorld preserves that command/testing role deliberately rather
than inventing ecology before the design approves one.

Target-version note for MOB-072: Minecraft 1.18.2 saves Zombie-Villager gossip
as an NBT list but its corresponding load check asks for a compound. CakeWorld
restores that list through the public setter while replacing entities so
infection and cure do not erase reputation. The same vanilla cure code writes
Binding-cursed equipment to an unrecognized `300 + index` slot; CakeWorld
preserves the resulting empty Villager equipment slot as target-version
behavior rather than quietly changing recovery rules.

MOB-032 completes the MOB-026/MOB-012 crossbreeding triangle. Gingerbread
Pony and Dough Donkey now produce Marzipan Mule from either parent direction
using vanilla's hybrid physical-attribute calculation. The Mule role remains
sterile, chest-capable, rideable and absent from open-biome spawn lists; its
spawn egg is retained for creative/testing parity.

Delivery dependency for MOB-033: Cupcake Cow is registered, interactive and
fully testable before Cupcake Gardens exists, but it deliberately has no
natural encounter in the currently shipped biomes. Its exact Mooshroom
replacement hook is dormant on `cakeworld:cupcake_gardens`; BIO-OW-012 must
activate and reverify the Mushroom Fields weight 8, group 4-8 profile.
Chocolate Sponge joins the standard Mooshroom-spawnable surface tag so the
future biome can remain wholly edible. Vanilla's private shearing helper is
the one role seam CakeWorld reproduces: both vanilla and Forge shearing paths
preserve the five type-coloured mushroom drops and create Cocoa Cow instead of
silently returning to a vanilla Cow.

Delivery dependency for MOB-027: Dried Crumbler is registered, summonable and
fully testable before Sherbet Dunes exists, but it deliberately has no natural
encounter in the currently shipped biomes. Its exact Husk replacement hook is
dormant on `cakeworld:sherbet_dunes`; BIO-OW-007 must activate and reverify the
80-weight, four-creature desert spawn. Water conversion already resolves to
Stale Crumbler instead of leaking a vanilla Zombie.

MOB-028 remains deliberately command-only. Fresh literal Illusioners summoned
inside CakeWorld terrain convert to Mirage Confectioners, while loaded
entities and commands outside CakeWorld biomes remain untouched. It has no
natural spawn list, no spawn egg, and no fabricated Monsters Hunted criterion,
matching all three vanilla Illusioner boundaries. A future structure must
explicitly approve and specify any ordinary encounter.

MOB-029 converts fresh literal Iron Golems created by villagers, the vanilla
player block pattern or commands inside CakeWorld terrain; loaded entities and
fresh entities outside CakeWorld biomes remain untouched. The deferred player
construction path lets the literal entity satisfy the vanilla Summon an Iron
Golem criterion before conversion. The inherited pattern still uses iron
blocks and a carved pumpkin; a future themed construction pattern must define
its own materials and criterion bridge explicitly.

Villager golem awareness and Raider combat activity compare literal entity
types in 1.18.2. Jawbreaker Guardians refresh those two memories at the same
low-frequency cadences while retaining the inherited village, flower, anger
and defence AI. They keep Iron Golem's exact loot and ground placement but
have no biome spawn list, spawn egg or Monsters Hunted criterion. Gingerbread
Villages and their natural defender encounter remain a STRUCT-001 integration
dependency rather than an invented open-biome spawn.

Delivery dependency for MOB-030: Meringue Llama is registered, summonable,
breedable and fully testable before Candyfloss Cloudbanks exists, but it has
no natural encounter in the currently shipped biomes. Its exact Llama
replacement hook is dormant on `cakeworld:candyfloss_cloudbanks`, using the
vanilla windswept-hills weight and group size of 5/4-6. BIO-END-002 must
provide a block in `minecraft:animals_spawnable_on` and reverify that spawn
contract when it activates. Vanilla 1.18.2 caravan AI searches only the two
literal vanilla Llama types, so Meringue and Sprinkle Llamas replace just that
goal with one shared subclass-friendly equivalent. Fresh literal Trader
Llamas now hand off to Sprinkle Llamas under MOB-058; MOB-064 remains
responsible for replacing the Wandering Trader itself.

Projectiles, vehicles, decorative entities, experience, items, the player, and
other non-mob entity types retain vanilla identity unless a later feature
specification gives them a clear CakeWorld purpose.

## Vanilla Structure Replacement Matrix

| ID | Vanilla structure family | CakeWorld conversion | Key contract |
|---|---|---|---|
| STRUCT-001 | `minecraft:village` | Gingerbread Villages | Functional first variant: a Candy Plains village with candy-cane and biscuit roads, three gumdrop-roof gingerbread houses, real beds, profession workstations, a farm, meeting bell, Cookbook library and loot, Wafer Windmill, four employed Gingerbread Folk, and a Jawbreaker Guardian. It is a genuine saved and locatable `VILLAGE` structure with occupied POIs and village/raid-location semantics. Hearthlands and other biome variants, expanding multi-piece layouts, presentation, and hands-on raid play remain. |
| STRUCT-002 | `minecraft:pillager_outpost` | Biscuit Bandit Lookout | Functional first Cookie Forest variant: a tall Wafer and Candy-Cane watchtower with gummy-and-icing roof, ladder, supply chest, biscuit paths, gingerbread camp, holding cage, target range, four persistent crossbow bandits, and a banner captain. It has a dedicated saved structure/set/tag, correctly bounded structure-wide custom-bandit spawn override, vanilla's one-in-five candidate gate, and ten-chunk avoidance of both vanilla and Gingerbread village sets. Layout variation, original presentation, and hands-on Bad Omen/raid play remain. |
| STRUCT-003 | `minecraft:mineshaft` | Wafer Mine | Functional first underground variant: a saved, locatable and exactly bounded `41 x 13 x 41` branching mine with fragile Wafer floors and beams, Candy-Cane support frames, a continuous 41-block rail, themed minecart loot, a Deep Liquorice Weaver spawner, cobweb nest, lighting, and exposed CakeWorld ore faces. It retains vanilla's every-chunk candidate, `0.004` probability and non-adapted noise contract while remaining eligible in all four current CakeWorld Overworld biomes; those biomes do not gain literal vanilla-Mineshaft eligibility. A wider random piece graph, corridor variation, original presentation, and hands-on exploration/balance play remain. |
| STRUCT-004 | `minecraft:mansion` | Grand Gingerbread Manor | Functional first Cookie Forest variant: a saved, locatable and exactly bounded `49 x 30 x 49` three-storey manor with Gingerbread masonry, Wafer floors, Candy-Glass windows, Candy-Cane supports, tiered gummy-and-icing roofs, two kitchens, dining hall, Cookbook library and kiosk, ordinary cache, sealed secret kitchen and guaranteed rare Cookbook cache. Five Rolling-Pin Raiders, two Sour Sorcerers and one Bitter Baker form its persistent non-raid household. It retains the Woodland Mansion's `80/20` triangular placement, terrain-height gate and Woodland Explorer Map role without granting literal vanilla-Mansion eligibility. Room/layout variation, Liquorice Darkwood placement, original presentation and hands-on exploration/balance remain. |
| STRUCT-005 | `minecraft:jungle_pyramid` | Gummy Shrine | Functional first Cookie Forest variant: a saved, locatable and exactly bounded `15 x 12 x 15` gummy-roofed Gingerbread ruin with a broad elastic approach, two attached tripwire lanes, two dispensers holding six harmless Slowness splashes, a three-flavour lever-and-piston clue, ordinary cache and hidden guaranteed-Cookbook cache. It retains the Jungle Temple's surface and terrain checks plus exact `32/8/14357619` linear placement without granting literal vanilla-Jungle-Temple eligibility. Gummy Jungle placement, random rotation/layout variation, original presentation and hands-on clue/trap play remain. |
| STRUCT-006 | `minecraft:desert_pyramid` | Sherbet Pyramid | Functional first Candy Plains variant: a saved, locatable and exactly bounded `21 x 25 x 21` stepped Biscuit-Stone pyramid with gummy warning stripes, Wafer entrance and walkways, Candy-Glass windows, Fizzy-Pearl crown, two fizzy-fossil displays and four protected buried sweet jars. It preserves the Desert Pyramid's surface/terrain checks and exact `32/8/14357617` linear placement. Its recognisable central stone plate triggers one real TNT charge instead of nine; a bright gummy warning ring, marshmallow cushioning, Rock-Candy baffles and an independent 13-rung recovery shaft make the environmental peril legible and recoverable. It does not grant literal vanilla-Desert-Pyramid eligibility. Sherbet Dunes placement, random orientation/layout variation, original presentation and hands-on trap/balance play remain. |
| STRUCT-007 | `minecraft:igloo` | Ice-Cream Parlour | Functional first Marshmallow Peaks variant: a saved, locatable and exactly bounded `13 x 27 x 13` scoop-roofed parlour with frozen-lemonade floor, wafer counter, kitchen gadgets, bed and a stable 50% chance of a concealed cellar. The cellar retains the Igloo's curing discovery with a stocked Weakness splash, guaranteed Golden Apple and one persistent Snow-clothed Cleric Gingerbread Folk/Crumbled Gingerbread Folk pair, activated exactly once after live-world handoff. It preserves the Igloo's surface/top-biome checks and exact `32/8/14357618` linear placement without granting literal vanilla-Igloo eligibility. Ice-Cream Tundra placement, random orientation, variable descent depth, original presentation and hands-on discovery/accessibility remain. |
| STRUCT-008 | `minecraft:ruined_portal` | Burnt-Sugar Arch | Functional first cross-realm family: a saved, locatable and exactly bounded `17 x 17 x 17` scorched arch in all four current CakeWorld Overworld biomes and Fudge Wastes. It preserves the Ruined Portal's exact `40/15/34222645` linear placement and chooses a surface, Soda-Ocean-floor or deterministic Nether-pocket height without granting literal vanilla-Ruined-Portal eligibility. Burnt Sugar, realm-specific Biscuit/Fudge geology, Honeycomb/Fudge Gold, Magma and adjacent Marshmallow warning/rescue blocks surround an incomplete Obsidian frame with exactly four gaps. Its chest guarantees those four Obsidian plus Flint and Steel, and filling only those gaps creates a genuine vanilla portal. Vanilla's 13 templates, giant chance, rotation/mirror, broader vertical/weathering variants, original presentation and hands-on repair/balance play remain. |
| STRUCT-009 | `minecraft:shipwreck` | Wafer Wreck | Functional first Soda-Ocean variant: a saved, locatable and exactly bounded `33 x 17 x 33` full wreck with deterministic cardinal orientation, deliberately breached 700-block Wafer hull and deck, Candy-Cane ribs and mast, torn Icing sail, Candy-Glass portholes, gummy pennant and Biscuit-Stone ballast. It preserves the Shipwreck's exact `24/4/165745295` linear placement, ocean-floor/top-biome checks and independent supply, map and treasure holds without granting literal vanilla-Shipwreck biome eligibility. The supply hold offers edible emergency provisions and repair material; treasure retains vanilla metal, gem and experience roles; the map hold creates a genuine filled Lost Cargo Map to another unvisited Wafer Wreck until the Buried Sweet Tin target exists. Joining Minecraft's additive Shipwreck configured-structure tag preserves Soda-Dolphin treasure guidance. Beached Custard-Coast Wrecks, vanilla's 20 submerged and 11 beached plan matrix, full/half/upright/sideways/upside-down/intact/degraded/mast silhouettes, eight palette variants, original presentation and hands-on underwater exploration remain. |
| STRUCT-010 | `minecraft:swamp_hut` | Caramel Cottage | Functional first Cookie-Forest variant: an independently saved, locatable and exactly bounded `15 x 12 x 15` cottage with a seed-and-centre-stable cardinal orientation, four-corner `MOTION_BLOCKING_NO_LEAVES` average-ground alignment and downward Candy-Cane supports. It preserves the Swamp Hut's `WORLD_SURFACE_WG` top-biome gate and exact `32/8/14357620` linear placement without granting literal vanilla-Swamp-Hut biome eligibility. The first plan uses Gingerbread walls, Wafer floor and roof, thick Icing, Candy-Glass windows, an accessible stair, a lit working kitchen, the red-mushroom keepsake, three Candy Sprouts on Chocolate Sponge, two Syrup Pipes, a sealed barrel containing a real Syrup bucket, two caramel cauldrons and a second non-loot barrel containing two real Caramel buckets. A durable hidden marker and live-server tick handoff create exactly one persistent structure-spawned Bitter Baker and explicit all-black Custard Cat; PIECE-bounded `1/1-1` overrides preserve their ongoing Witch/Cat spawn roles, while late terrain repair cannot reopen the marker or replace residents. Caramel Bogs is the intended future home; biome-specific cottage plans, original art and sounds, and hands-on discovery, combat and accessibility review remain. |
| STRUCT-011 | `minecraft:stronghold` | Ancient Cake Vault | Eyes, portal progression, libraries, and sprawling underground navigation. |
| STRUCT-012 | `minecraft:monument` | Soda Palace | Gumball Guardians, sponge-role rewards, and underwater navigation. |
| STRUCT-013 | `minecraft:ocean_ruin` | Sunken Sweetshop | Small warm/cold variants, archaeology-style clues, and soda-ocean ambience. |
| STRUCT-014 | `minecraft:fortress` | Liquorice Fortress | Burnt-Candy Knights, Cinnamon Sparks, wart-role crop, and boss progression. |
| STRUCT-015 | `minecraft:endcity` | Macaron Citadel | Macaron Clams, storage progression, and Wafer Airship branches. |
| STRUCT-016 | `minecraft:buried_treasure` | Buried Sweet Tin | Map-compatible compact reward cache. |
| STRUCT-017 | `minecraft:nether_fossil` | Rock-Candy Fossil | Fudge-realm geological landmark and bone-role material source. |
| STRUCT-018 | `minecraft:bastion_remnant` | Burnt-Toffee Foundry | Fudge Folk society, treasure rooms, bridges, stables, and high-value loot. |

The `STRUCT-001` prototype owns a dedicated configured structure, structure
set, public locate tag, and CakeWorld-only Candy Plains biome tag. Its first
start pool intentionally contains one procedural fixed-layout piece; this
keeps the initial settlement deterministic and does not yet claim the spatial
variety of a full multi-piece jigsaw village. It augments the vanilla
`#minecraft:village` locate family without rewriting vanilla or third-party
structures. Generated blocks, loot, the structure start, beds, jobs, meeting
point, occupied-village state, residents, and defender survive save/reload.
Biome-specific expansion begins when Gingerbread Hearthlands is delivered.

The `STRUCT-002` prototype uses a serializable CakeWorld bounded-feature pool
element rather than vanilla's zero-sized decoration wrapper. Its saved piece
therefore contains the complete `25 x 21 x 25` build and gives the
structure-wide spawn override a real area. Terrain adaptation expands the
outer saved structure bounds without losing that exact inner layout. The
Lookout retains Pillager-Outpost surface placement, spacing `32`, separation
`8`, a one-in-five candidate gate, and village exclusion, while its own
structure spawn list names Biscuit Bandits directly instead of relying on a
one-tick literal-Pillager conversion. It does not rewrite vanilla or
third-party Outposts.

The `STRUCT-003` prototype is CakeWorld-owned because vanilla 1.18.2 embeds
Oak and Dark-Oak mine palettes in a closed `MineshaftFeature.Type` enum and
package-scoped piece graph; child-mod configuration cannot substitute edible
supports. CakeWorld therefore retains the vanilla normal-Mineshaft frequency
contract but supplies its own configured structure, set, public locate tag,
underground generation step, correctly bounded piece, palette, loot and
Weaver-spawner role. The shared bounded pool element slices large procedural
pieces by the chunk currently being post-processed, so the mine's complete
saved bounds do not permit unsafe far-chunk writes. The first fixed branching
plan preserves the identifying player contract but does not claim vanilla's
full random corridor graph. It does not rewrite vanilla or third-party
Mineshafts; current CakeWorld biomes select the Wafer Mine instead of joining
vanilla's Mineshaft biome tags. Like vanilla normal Mineshafts, it does not
adapt surrounding terrain noise, so saved bounds remain exactly
`41 x 13 x 41`.

The `STRUCT-004` prototype similarly owns a dedicated configured structure,
set, biome tag and public locate identity rather than recolouring vanilla
Mansion templates. It preserves vanilla's surface-structure step, minimum
terrain height of `60`, exact spacing `80`, separation `20`, triangular
spread and salt `10387319`, and joins
`#minecraft:on_woodland_explorer_maps`. The first fixed plan provides three
connected floors, ordinary and secret-room loot roles, Cookbook facilities
and a persistent CakeWorld raider household without inventing an ambient
structure spawn override. Cookie Forest vegetation normally decorates after
surface structures, so a final CakeWorld biome-decoration pass restores only
the manor's bounded block slices; it resolves the configured structure from
the active world registry and deliberately does not respawn inhabitants.
This keeps routes readable without unsafe far-chunk writes or duplicate
entities. The current Cookie Forest home is an available proving ground;
Liquorice Darkwood becomes the thematic primary home when that biome exists.
The prototype does not rewrite vanilla or third-party Mansions and does not
claim vanilla's full procedural room graph.

The `STRUCT-005` prototype owns a dedicated configured structure, set, biome
tag and public locate identity rather than recolouring vanilla Jungle Temple
pieces. It preserves the vanilla surface-structure step, top-biome and
above-sea-level terrain checks, exact spacing `32`, separation `8`, linear
spread and salt `14357619`, without adding the literal
`#minecraft:has_structure/jungle_temple` biome tag. Its first fixed
`15 x 12 x 15` plan keeps the recognisable two-tripwire, two-dispenser and
hidden-treasure progression, but substitutes sticky Slowness splashes for
damaging arrows and adds an elastic gummy approach plus a visible
three-flavour lever-and-piston clue. A final CakeWorld biome-decoration pass
restores only the shrine's bounded block slices after later forest surface and
vegetation features; it resolves the configured structure from the active
world registry and creates no entities. Cookie Forest is an available proving
ground until Gummy Jungle becomes the thematic primary home. The prototype
does not rewrite vanilla or third-party Jungle Temples and does not yet claim
random orientation, layout variation, finished clue logic, or hands-on
balance and accessibility proof.

The `STRUCT-006` prototype likewise owns a dedicated configured structure,
set, biome tag and public locate identity rather than recolouring vanilla
Desert Pyramid pieces. It preserves the vanilla surface-structure step,
top-biome and `21 x 21` above-sea-level terrain checks, exact spacing `32`,
separation `8`, linear spread and salt `14357617`, without adding the literal
`#minecraft:has_structure/desert_pyramid` biome tag. Its first fixed
`21 x 25 x 21` plan makes the buried chamber part of the saved bounds and
keeps the recognisable stone-pressure-plate, TNT and four-cardinal-cache
discovery. The trap is reduced from nine TNT blocks to one and surrounded by
gummy warning colours, marshmallow cushioning and Rock-Candy baffles; all
four sweet jars and a separate 13-rung recovery ladder remain outside its
destructive path. Two above-ground fizzy-fossil displays provide the
version-appropriate archaeology flavour: CakeWorld does not backport later
suspicious-sand mechanics into Minecraft 1.18.2. A final CakeWorld
biome-decoration pass restores only the pyramid's bounded block slices after
later surface and vegetation features and creates no entities. Candy Plains
is the available proving ground until Sherbet Dunes becomes the thematic
primary home. The prototype does not rewrite vanilla or third-party Desert
Pyramids and does not yet claim random rotation, layout variation, original
art and sounds, or hands-on balance and accessibility proof.

## Original Structure Additions

These are CakeWorld additions rather than vanilla replacements.

| ID | Priority | Slice | Structure | Key contract |
|---|---|---:|---|---|
| STRUCT-019 | Expansion | 3 | Confectioner's Cottage | Small Lollipop Orchard home/shop with an ingredient garden and useful early trades. |
| STRUCT-020 | Expansion | 3 | Wafer Windmill | Waffle or Hearthlands landmark that visibly powers simple village kitchen machinery. |
| STRUCT-021 | Expansion | 3 | Candy-Cane Bridge | Road-network crossing that makes settlement routes readable and demonstrates axis-oriented pillars. |
| STRUCT-022 | Expansion | 5 | Crater Kitchen | Mooncake Barren ruin containing rare recipes, ancient crumbs, and quiet environmental storytelling. |
| STRUCT-023 | Core | 2 | Rock-Candy Crystal Mine | Cavern landmark exposing safe examples of geology, host families, and compact deposits. |
| STRUCT-024 | Core | 1 | Cookbook Kiosk | Small starter landmark that restores a lost Cookbook and teaches discovery without becoming a quest marker. |
| STRUCT-025 | Expansion | 3/4/5 | Roadside Curiosity Set | Biome-specific tiny scenes, carts, picnic spots, wells, rescue shelters, and visual jokes that reward wandering without map clutter. |
| STRUCT-026 | Core | 1 | First Bite Picnic Hamlet | Sparse Chocolate Sponge Meadows welcome stop containing a Cookbook Kiosk, two icing-roof shelters, cushioned seats, and a biscuit path; it is deliberately smaller than a village. |

## Delivery Slices

| Slice | Name | Playable promise |
|---:|---|---|
| 1 | First Bite | Chocolate Sponge Meadows and Cookie Crumb Forest, Soda Ocean proof, nibbling, Cookbook foundation, starter foods, first creatures, and one small settlement. |
| 2 | Deep Pantry | Edible geology, caves, every OreSpawn ore pattern, fluid deposits, themed vanilla ores, and BaseMetals compatibility. |
| 3 | Living Sweetlands | Remaining Overworld biomes, Gingerbread society, village variants, broad creature replacement, farming, foods, and gadgets. |
| 4 | Fudge Below | Complete Nether biome, material, creature, structure, ingredient, and difficulty conversion. |
| 5 | Meringue Beyond | Complete End conversion, Macaron Citadels, aerial play, Great Meringue Dragon, and rare Cookbook pages. |
| 6 | Whole World Made Sweet | Remaining vanilla roles and structures, full Cookbook, accessibility, localisation, multiplayer, balance, and art polish. |
| 7 | Sampler Platter | Intrusive OreSpawn edge features, compatibility matrix, migration exercises, benchmarks, and developer-facing proof. |

Every slice must be enjoyable on its own. A slice is not complete merely
because all of its registry objects exist.

## Accessibility, Safety, and Popularity

- Important blocks and creatures use silhouette, pattern, and sound as well as
  colour.
- Particle-heavy or bouncing effects require reduced-particle and
  reduced-motion consideration.
- All consequential sounds receive subtitles.
- Interfaces support Minecraft GUI scale and do not rely on tiny decorative
  text.
- Sticky and displacement effects must always leave a recoverable route.

The `BOOK-008` prototype keeps the Cookbook presentation static, reflows its
tabs and visible page list to the scaled viewport, and supports mouse, arrow,
A/D, Home/End, and number-key navigation. The selected tab has a shape marker
as well as a colour change, earned stamps use a `+` glyph rather than colour
alone, and narrator text reports the active category and progress. Discovery
sounds use a dedicated subtitle. Human checks at multiple GUI scales, with
Minecraft Narrator and subtitles enabled, remain an acceptance requirement.
- Food and creature names avoid implying that real toxic materials such as
  mercury are safe to eat. Metal-bearing compatibility ores are confectionery
  geology, not nibbleable food.
- Multiplayer behaviour is server-authoritative and does not assume one player
  owns a world.
- Original art, sound, writing, and creature identities are release
  requirements, not optional polish.

## Explicit Non-Goals

- CakeWorld is not a copy of a particular book, film, factory, character, or
  branded confection.
- CakeWorld does not replace arbitrary third-party mobs or structures without
  an explicit compatibility contract.
- CakeWorld does not duplicate OreSpawn's terrain, biome, ore, fluid-deposit,
  profile, or template engines.
- CakeWorld does not make every raw block a strong food or remove all
  environmental challenge.
- CakeWorld does not silently rewrite existing world profiles when installed
  or updated.
