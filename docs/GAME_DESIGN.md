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
| MOB-038 | `minecraft:pig` | Truffle Pig | Farm animal retaining Pig breeding, saddle, rider, Carrot on a Stick, loot, lightning, progression and exact Plains 10/4-4 spawn roles. An adult standing on a tagged soft confectionery surface can consume a Simple Biscuit to snuffle up one Cocoa Truffle without breaking or changing the terrain, then rests for one minute before foraging again. Lightning deliberately keeps the vanilla Zombified Piglin handoff until MOB-073 Stale Fudge Folk owns that replacement. |
| MOB-039 | `minecraft:piglin` | Fudge Folk | Genuine Piglin society role retaining the 1.18.2 brain, inventory, gold equipment, admiration, bartering, hunting, retreat, baby riding, zombification, empty loot and exact Fudge Wastes 15/4-4 spawn role. Fresh literal structure Piglins convert only inside CakeWorld biomes; loaded/outside/third-party entities remain untouched. Easy and Normal sword or crossbow hits become a sticky zero-damage splat with fall/fire rescue, while Hard retains real damage. Both Distract Piglin paths and Monsters Hunted are bridged deliberately. The inherited literal Zombified Piglin result remains until MOB-073, and the final Foundry presentation remains with STRUCT-018. |
| MOB-040 | `minecraft:piglin_brute` | Fudge Brute | Genuine structure-only Piglin Brute role retaining the 1.18.2 50-health, 0.35-speed, seven-damage and 20-XP body; Brute brain, home memory, always-hostile player and nemesis targeting, 600-tick anger, adult Piglin alliance, guaranteed Golden Axe, Golden-Axe-only pickup, sounds, Peaceful removal, empty loot and staged zombification. Fresh literal Bastion Brutes convert only inside CakeWorld biomes; loaded/outside/third-party entities remain untouched, and the custom Fudge Folk family is recognised during idle interaction. Easy and Normal axe blows become zero-damage toffee thumps with fall/fire rescue; Hard retains real damage. Monsters Hunted, spawn egg and Lollipop Lorikeet mimic roles are bridged deliberately. No open-biome spawn or custom placement is registered; the final Burnt-Toffee Foundry belongs to STRUCT-018 and the themed zombified result to MOB-073. |
| MOB-041 | `minecraft:pillager` | Biscuit Bandit | Genuine Pillager-derived ranged raider retaining the 1.18.2 24-health body, crossbow charging/shooting, five-slot persisted inventory, patrol/captain/banner/Bad Omen roles, outpost spawn override, raid membership/leadership/wave enchanting, Illager alliances, sounds and empty loot. Fresh literal patrol, Outpost and raid Pillagers convert only inside CakeWorld biomes with patrol, inventory, mount and raid state retained; loaded/outside/third-party entities remain untouched. A narrow visible-hostile repair preserves Pillager's exact 15-block Villager fear role. Easy and Normal bolts become zero-damage obscuring crumb puffs with complete fall/fire rescue; Hard retains real crossbow damage. Raiders tag, Monsters Hunted, Who's the Pillager Now, spawn egg and Lollipop Lorikeet mimic roles are bridged deliberately. STRUCT-002 owns the edible Biscuit Bandit Lookout and later structure activation. |
| MOB-042 | `minecraft:polar_bear` | Vanilla-Ice Bear | Genuine Polar-Bear-derived tundra guardian retaining the 1.18.2 30-health body, neutral persistent anger, adult defence of nearby cubs, baby-to-adult hurt alerting, Fox hunting, standing warning, swimming, panic, parent following, non-food/non-breedable player contract, sounds, exact weighted Cod/Salmon loot and powder-snow/freeze immunity. Its literal offspring factory returns the CakeWorld type for command/mod-driven family creation. A dormant Ice-Cream Tundra hook preserves the snowy-biome 1/1-2 encounter while every current biome remains free of vanilla and custom bears. Peaceful, Easy and Normal warning swipes become zero-damage cushioned vanilla-cream shoves with complete fall/fire rescue; Hard retains the exact six-point attack. A spawn egg and inherited Polar Bear renderer are supplied. Vanilla 1.18.2 assigns no Polar Bear criterion in Monsters Hunted or Two by Two, so CakeWorld deliberately fabricates no advancement credit. |
| MOB-043 | `minecraft:pufferfish` | Fizzball Fish | Genuine Pufferfish-derived Soda Ocean animal retaining the exact three-health body, water goals, three inflation states and scale/timing/cues, bucket capture, exact loot, Axolotl prey and Tactical Fishing roles. Its dedicated Lemonade bucket preserves custom identity, name and from-bucket state. The exact Soda Ocean `5/1-3` profile replaces literal Pufferfish. Peaceful, Easy and Normal contacts are intercepted before vanilla's private poison step and become a zero-health-damage, landing-safe fizzy bounce with complete rescue; Hard retains state-scaled damage and poison. |
| MOB-044 | `minecraft:rabbit` | Gummy Bunny | Genuine Rabbit-derived starter animal retaining all six ordinary climate-weighted colour variants, group spawning, hopping, panic/avoidance, carrots/golden carrots/dandelions, breeding inheritance, mob-griefing-controlled mature-carrot raiding, sounds, exact hide/meat/foot loot and the command-only type-99 combat role. Sprinkle Seeds extend the food and temptation contract. Current Candy Plains and future Chocolate Sponge Meadows use the exact Meadow `2/2-6` profile; future Gummy Jungle uses `4/2-3`. Edible spawn surfaces and powder-snow walking are tagged, Fox predation works through Rabbit inheritance, and a narrow untamed-vanilla-Wolf bridge repairs its literal entity-type predicate. The type-99 name is originalised as Ferocious Gummy Bunny; below Hard its bite becomes a zero-damage elastic rescue bounce, while Hard retains the exact eight-point attack. Two by Two is bridged deliberately. |
| MOB-045 | `minecraft:ravager` | Gingerbread Stomper | Genuine Ravager-derived raid mount retaining the exact 100-health body, attributes, XP, targets, alliances, rider controls, shield stun, delayed roar, navigation, sounds, exact Saddle loot and vanilla wave/rider source. Fresh literal raid Ravagers convert only in CakeWorld biomes with raid, wave, passenger and animation NBT retained; loaded, outside-world and third-party entities remain untouched. Peaceful, Easy and Normal melee and roar impacts cause zero health damage under a complete fall/fire rescue envelope while preserving visible attack/stun/roar and displacement cues; Hard retains exact 12-point melee and 6-point roar damage. All gamerule-mediated leaf, crop and farmland destruction is denied below Hard, with a safe obstacle hop in place of breakage; Hard falls back to the world's `mobGriefing` rule and retains real leaf breaking. Raiders, Monsters Hunted, spawn egg and Lollipop Lorikeet mimic roles are bridged deliberately; it has exact vanilla `NO_RESTRICTIONS` placement metadata but no open-biome spawn. |
| MOB-046 | `minecraft:salmon` | Sherbet Salmon | Genuine Salmon-derived cool-water food fish retaining the exact three-health body, five-fish schools and clusters, follower navigation, panic/player avoidance/random swimming, water and land-flop movement, air/persistence rules, sounds, XP and raw/cooked Salmon plus rare Bone Meal loot. Its dedicated Lemonade bucket preserves identity, name and from-bucket state and bridges Tactical Fishing. The current Soda Ocean and future frozen-lemonade lakes of Ice-Cream Tundra use the exact cold-ocean `15/1-5` profile with literal Salmon excluded; Lemonade extends the vanilla surface-water depth predicate, Axolotls retain the hunt role, and a spawn egg plus inherited renderer are supplied. |
| MOB-047 | `minecraft:sheep` | Candyfloss Sheep | Dye-compatible farm animal supplying candyfloss fibre. |
| MOB-048 | `minecraft:shulker` | Macaron Clam | End City defender and portable-storage progression source. |
| MOB-049 | `minecraft:silverfish` | Crumb Mite | Infested-block nuisance. |
| MOB-050 | `minecraft:skeleton` | Candy-Cane Archer | Ranged mischief creature with non-damaging sticky or knockback shots below Hard. |
| MOB-051 | `minecraft:skeleton_horse` | Brittle Biscuit Steed | Rare trap and undead-mount role. |
| MOB-052 | `minecraft:slime` | Jelly Blob | Elastic surface/cave creature and sticky material source. |
| MOB-053 | `minecraft:snow_golem` | Ice-Cream Golem | Player-built ranged helper leaving suitable icing traces. |
| MOB-054 | `minecraft:spider` | Liquorice Weaver | Climbing nocturnal creature using webs and slowing below Hard. |
| MOB-055 | `minecraft:squid` | Liquorice Squid | Dark-ink aquatic creature. |
| MOB-056 | `minecraft:stray` | Frosted Archer | Tundra ranged variant using chilling movement effects below Hard. |
| MOB-057 | `minecraft:strider` | Fudge Skater | Rideable hot-fudge traversal creature. |
| MOB-058 | `minecraft:trader_llama` | Sprinkle Llama | Travelling Confectioner caravan animal. |
| MOB-059 | `minecraft:tropical_fish` | Jellybean Fish | Large colour-and-pattern variant family. |
| MOB-060 | `minecraft:turtle` | Wafer Turtle | Beach nesting and shell-progression role. |
| MOB-061 | `minecraft:vex` | Sour Sprite | Small summoned flying nuisance. |
| MOB-062 | `minecraft:villager` | Gingerbread Folk | Settlement professions, trade, schedules, gossip, and family life. |
| MOB-063 | `minecraft:vindicator` | Rolling-Pin Raider | Melee raid role with displacement below Hard. |
| MOB-064 | `minecraft:wandering_trader` | Travelling Confectioner | Roaming trade and rare sapling/ingredient source. |
| MOB-065 | `minecraft:witch` | Bitter Baker | Potion and raid-support role reframed through kitchen mixtures. |
| MOB-066 | `minecraft:wither` | Burnt-Sugar Tempest | Optional summoned boss preserving beacon progression. |
| MOB-067 | `minecraft:wither_skeleton` | Burnt-Candy Knight | Liquorice Fortress melee defender and boss-summoning drop role. |
| MOB-068 | `minecraft:wolf` | Ginger-Snap Hound | Tameable companion and pack hunter. |
| MOB-069 | `minecraft:zoglin` | Stale Fudge Boar | Overworld-exposed Fudge Boar conversion role. |
| MOB-070 | `minecraft:zombie` | Stale Crumbler | Common night mischief creature. |
| MOB-071 | `minecraft:zombie_horse` | Stale Gingerbread Steed | Command-only compatibility mount unless a future encounter is approved. |
| MOB-072 | `minecraft:zombie_villager` | Crumbled Gingerbread Folk | Curable settlement inhabitant preserving recovery gameplay. |
| MOB-073 | `minecraft:zombified_piglin` | Stale Fudge Folk | Neutral group-anger and cross-dimension conversion role. |

Target-version note for MOB-023: Java 1.18.2's Goat horn contract is the
visible adult model only. Horn items, horn drops, and horn loss were added
after this target version, so CakeWorld does not invent a false vanilla
compatibility contract by backporting them.

Target-version note for MOB-025: vanilla 1.18.2 Piglin and Hoglin sensors
recognize the Fudge Boar subclass, and CakeWorld repairs the hard-coded
same-family mating type locally. A few later Piglin hunt bookkeeping branches
still compare the literal vanilla Hoglin type; the main Piglin hunt-target
sensor is preserved and verified without copying vanilla Piglin AI. Until
MOB-069 is implemented, an unprotected Fudge Boar that completes its
Overworld conversion becomes a vanilla Zoglin. Both seams must remain visible
in the evidence ledger and be revisited with Fudge Folk and Stale Fudge Boar.

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
literal vanilla Llama types, so the Meringue Llama replaces just that goal
with a subclass-friendly equivalent. Trader Llamas remain untouched for the
separate Sprinkle Llama and Travelling Confectioner work in MOB-058 and
MOB-064.

Projectiles, vehicles, decorative entities, experience, items, the player, and
other non-mob entity types retain vanilla identity unless a later feature
specification gives them a clear CakeWorld purpose.

## Vanilla Structure Replacement Matrix

| ID | Vanilla structure family | CakeWorld conversion | Key contract |
|---|---|---|---|
| STRUCT-001 | `minecraft:village` | Gingerbread Villages | Biome variants, candy-cane roads, gumdrop houses, workstations, farms, and Cookbook libraries. |
| STRUCT-002 | `minecraft:pillager_outpost` | Biscuit Bandit Lookout | Raid-compatible lookout with readable bandit theming. |
| STRUCT-003 | `minecraft:mineshaft` | Wafer Mine | Fragile supports, rail continuity, cave loot, and edible geology integration. |
| STRUCT-004 | `minecraft:mansion` | Grand Gingerbread Manor | Sour Sorcerers, secret kitchens, rare recipes, and raid-role compatibility. |
| STRUCT-005 | `minecraft:jungle_pyramid` | Gummy Shrine | Elastic traps, jungle clues, and themed treasure. |
| STRUCT-006 | `minecraft:desert_pyramid` | Sherbet Pyramid | Powder traps, fizzy fossils, and buried sweet jars. |
| STRUCT-007 | `minecraft:igloo` | Ice-Cream Parlour | Tundra shelter retaining the hidden-curing discovery role. |
| STRUCT-008 | `minecraft:ruined_portal` | Burnt-Sugar Arch | Clearly recognisable portal ruin using scorched confectionery materials. |
| STRUCT-009 | `minecraft:shipwreck` | Wafer Wreck | Fragile edible ship with map and treasure roles. |
| STRUCT-010 | `minecraft:swamp_hut` | Caramel Cottage | Bitter Baker home with syrup garden and cat role. |
| STRUCT-011 | `minecraft:stronghold` | Ancient Cake Vault | Eyes, portal progression, libraries, and sprawling underground navigation. |
| STRUCT-012 | `minecraft:monument` | Soda Palace | Gumball Guardians, sponge-role rewards, and underwater navigation. |
| STRUCT-013 | `minecraft:ocean_ruin` | Sunken Sweetshop | Small warm/cold variants, archaeology-style clues, and soda-ocean ambience. |
| STRUCT-014 | `minecraft:fortress` | Liquorice Fortress | Burnt-Candy Knights, Cinnamon Sparks, wart-role crop, and boss progression. |
| STRUCT-015 | `minecraft:endcity` | Macaron Citadel | Macaron Clams, storage progression, and Wafer Airship branches. |
| STRUCT-016 | `minecraft:buried_treasure` | Buried Sweet Tin | Map-compatible compact reward cache. |
| STRUCT-017 | `minecraft:nether_fossil` | Rock-Candy Fossil | Fudge-realm geological landmark and bone-role material source. |
| STRUCT-018 | `minecraft:bastion_remnant` | Burnt-Toffee Foundry | Fudge Folk society, treasure rooms, bridges, stables, and high-value loot. |

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
