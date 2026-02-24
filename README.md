**Random Enchant** is a Minecraft mod that introduces new ways to enchant items. In addition to the traditional enchanting table, you can now gain enchantments simply by attacking mobs! The mod also adds new tools, weapons, and the ability to enchant blocks, along with several block enchantments that assist with redstone contraptions – such as helping set up underwater redstone circuits, adjusting hopper behavior, and more.

**This mod must be installed on both the client and the server** to function properly. If you're playing on a server, make sure the mod is present on the server; otherwise, none of its features will work. Clients connecting to a modded server will need the mod installed as well.

[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.93+-orange?style=flat-square)](https://neoforged.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-brightgreen?style=flat-square)](https://minecraft.net)
[![Curios API](https://img.shields.io/badge/Curios%20API-9.5.1%2B-1.21.1-purple?style=flat-square)](https://modrinth.com/mod/curios)

## ✨ Features

- **Random Enchantment** – Every time you attack a living entity, your main‑hand item has an **80% chance** to receive a random enchantment (configurable). Enchantments are chosen from all registered enchantments (including those from other mods) with equal probability.
    - **Level distribution**: 60% chance for 0–2 levels, 40% chance for 6–10 levels.
    - **Stacking**: New level = current level + 1 + random value (e.g. Unbreaking II + Unbreaking 6 → Unbreaking 9).

- **Block Enchanting** – Use the **Enchant Brush** to apply enchantments directly to blocks.
    - **Single‑block mode**: Right‑click a block to add the brush’s enchantments; if the brush has no enchantments, it clears the block’s enchantments.
    - **Area mode**: Select two corners to enchant/clear all non‑air, non‑fluid blocks in the region. Durability consumption depends on the number of blocks and is affected by the **Unbreaking** enchantment.
    - **Particle feedback**: Enchanted blocks glow with green particles when holding the brush.
    - **Precision harvesting**: Blocks enchanted this way keep their enchantments when mined with a Silk‑Touch tool (configurable).

- **Pearl Spear** – A powerful new weapon with unique combo skills.
    - **Right‑click**: Dash forward, damaging all entities in your path. Damage = 8 + Sweeping Edge level.
    - **Left‑click**: Deal bonus damage based on relative velocity, then dash horizontally (also damages along the path).
    - **Special synergy**: If the spear has **Channeling**, the left‑click summons multiple lightning bolts. If it has **Fury of Fly**, it summons empowered bees to attack the target (these two enchantments are mutually exclusive).

- **New Enchantments** (all available as treasure enchantments or through trading):
    - **Fury of Fly** (Pearl Spear) – Summon bees on attack.
    - **Redirect Projectile** (Trident) – After throwing, left‑click to make the trident fly toward the pointed location.
    - **Fly** (Elytra) – Press jump while gliding to ascend (lift height per tick configurable).
    - **No Gravity** / **No Resistance** (Bow, Crossbow, Trident, Mace, Fishing Rod) – Projectiles ignore gravity and/or air/water resistance.
    - **Kinetic** (Bow, Crossbow, Fishing Rod) – Increase projectile velocity (+30% per level).
    - **Steady** (Bow, Crossbow) – Eliminate random spread (mutually exclusive with Explode).
    - **Explode** (Bow, Crossbow, blocks) – Arrows explode on impact; blocks explode when an entity stands on them (configurable block destruction).
    - **Throwable** (Mace) – Right‑click to throw the mace; supports Channeling, Loyalty, and Explode.
    - **Tracking** (Bow, Crossbow) – Arrows home toward the nearest entity.
    - **Double Jump** (Boots) – Jump again in mid‑air; level ≥2 grants fall protection.
    - **No Curse** (any item) – Drop and pick up an item to remove all curses (and the No Curse enchantment itself).
    - **Bad Luck of the Sea** (curse) – Item entities are repelled by water.

- **Redstone Assistance** – Block enchantments offer new possibilities for redstone contraptions:
    - **Respiration** (redstone components) – Prevents redstone dust, repeaters, etc. from being washed away by water, making underwater circuits possible.
    - **Quick Charge** (hopper) – Removes the hopper’s cooldown, enabling instant item transfer.
    - **Binding Curse** (hopper) – Locks the hopper, preventing it from outputting items – useful for fine‑tuning item flow.

- **New Potions**:
    - **Memory Potion** – Teleport you to your spawn point (even if the bed is missing).
    - **Soultrance Potion** – Teleport you to your last death location.

- **In‑Game Guide** – When you obtain any enchanted book, you'll receive the **Random Enchant Guide** book, explaining all features in detail. You can also access it via `/random_enchant description`.

## 📦 Installation

1. Install **NeoForge 21.1.93+** for Minecraft 1.21.1.
2. Install **Curios API 9.5.1+1.21.1** (required for certain features, e.g., the Fly enchantment on elytra). Available from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/curios) or [Modrinth](https://modrinth.com/mod/curios).
3. Download the latest `RandomEnchant-*.jar` from the [Releases](https://github.com/Skrepy0/Random-Enchant-Neoforge/releases) page.
4. Place the JAR file into your `.minecraft/mods` folder on **both the client and the server**.
5. Launch the game and enjoy!

## 🚀 Quick Start

- `/random_enchant doRandomEnchant true` – Enable the random enchantment event.
- `/random_enchant description` – Receive the in‑game guide book (also obtainable by picking up any enchanted book).
- Craft an **Enchant Brush** and experiment with block enchanting.
- Obtain a **Pearl Spear** and try its special moves.

## 📜 Commands

- `/random_enchant description` – Gives you the Random Enchant guide book.
- `/random_enchant config <option> [value]` – View or modify configuration in‑game (requires permission level 2).

  **Configuration options** (with defaults):
    - `doRandomEnchant <true/false>` – Enable/disable random enchantment on attack. (default: `false`)
    - `alwaysEnchantable <true/false>` – Allow any item to be enchanted on an anvil. (default: `false`)
    - `explodeDestroyBlock <true/false>` – Whether explosion enchantments break blocks. (default: `true`)
    - `infinityUndyingTotem <true/false>` – Infinity works on totems of undying. (default: `false`)
    - `infinityBlock <true/false>` – Infinity works on placeable blocks. (default: `false`)
    - `infinityTnt <true/false>` – Infinity works on TNT (block stays after ignition). (default: `true`)
    - `infinityPotion <true/false>` – Infinity works on potions. (default: `true`)
    - `infinityFood <true/false>` – Infinity works on food. (default: `true`)
    - `infinityThrowableItem <true/false>` – Infinity works on throwables (eggs, snowballs, etc.). (default: `true`)
    - `isEnchantedBlockGetatable <true/false>` – Can enchanted blocks be obtained with Silk‑Touch. (default: `true`)
    - `redirectTridentSetPointDistance <integer>` – Maximum distance for Redirect Projectile. (default: `15`)
    - `flyEnchantmentLiftHeightPerTick <double>` – Ascent speed for Fly enchantment. (default: `0.05`)
    - `bedrockViolable <true/false>` – Whether bedrock can be affected by mod features (e.g., Bad Luck of the Sea). (default: `false`)

- `/block_enchant <get|add|remove> <x y z> [enchantment] [level]` – Manage block enchantments directly.

## 🛠 New Items & Recipes

| Item | Description | Recipe |
|------|-------------|--------|
| **Enchant Brush** | Used to apply enchantments to blocks. Durability: 387. | <pre> #P<br> T#<br>B  </pre>Where P = Enchanted Book, # = String, T = Copper Ingot, B = Stick. |
| **Pearl Spear** | A spear with dash and combo abilities. | <pre> #P<br>@T#<br>B@ </pre>Where P = Ender Pearl, # = Echo Shard, T = Heavy Core, @ = Popped Chorus Fruit, B = Breeze Rod. |
| **Memory Potion** | Teleports you to your spawn point. | Shapeless: Milk Bottle + Ender Pearl + Sugar + Glowstone Dust |
| **Soultrance Potion** | Teleports you to your last death location. | **No recipe** – obtainable only via creative inventory, commands, or **loot chests**. |
| **Milk Bottle** | Crafted from Glass Bottle + Milk Bucket (shapeless). Used in Memory Potion recipe. | Shapeless: Glass Bottle + Milk Bucket |

## 📖 Block Enchantments

You can give blocks special properties with the Enchant Brush. Besides combat uses, they also assist with redstone systems:

- **Aqua Affinity (block)** – Blocks allow water to flow through and are instantly broken by water.
- **Blast Protection** – Blocks are immune to explosions.
- **Explosion** – When an entity stands on the block, it explodes (configurable block destruction). If combined with Infinity, the block itself is not destroyed.
- **Fire Aspect** – Entities standing on the block are set on fire.
- **Fire Protection** – Entities standing on the block have their fire cleared.
- **Frost Walker** – (Not implemented yet)
- **Infinity** – (special for TNT) TNT stays after ignition.
- **Knockback** – Entities touching the block are knocked upward (or in a direction for buttons, nether portals, tripwire).
- **Punch** – (Trapdoors) When opened, entities on top are flung away.
- **Quick Charge** – (Hopper) **Redstone assistance**: Removes the hopper’s cooldown, making items flow instantly.
- **Binding Curse** – (Hopper) **Redstone assistance**: Locks the hopper, preventing item output – useful for precise control.
- **Respiration** – (Redstone components) **Redstone assistance**: Prevents redstone dust, repeaters, etc. from being washed away by water, enabling underwater circuits.
- **Thorns** – Entities standing on the block take damage equal to the enchantment level.
- **Depth Strider** – (block) Entities in water near the block are pushed toward the nearest shore.

## 🧩 Compatibility & Dependencies

- **Required**: NeoForge 21.1.93+ (for Minecraft 1.21.1)
- **Required**: Curios API 9.5.1+1.21.1 – The mod uses Curios API to add new slots (e.g., back slot) for features like the Fly enchantment on elytra.
- **Fully compatible with JEI**: You can view all new items, enchanted books, and recipes in JEI.
- Random Enchant works with most mods. It may conflict with mods that heavily modify the enchantment system (e.g., completely replacing the enchantment registry). In such cases, some features may be limited. If you encounter issues, please report them on GitHub.

## ❤️ Credits

- **Developer**: [Skrepy2233](https://github.com/Skrepy0)
- **Inspiration**: [Mafuyu33](https://github.com/Mafuyu33) ([neomafishmod](https://github.com/Mafuyu33/neomafishmod)), [Mahiru](https://github.com/Mahirukksk)
- **Textures**: Ragecraft IV resources (source of the Pearl Spear texture)
- **Fabric Edition**: Available [here](https://github.com/Skrepy0/Random-Enchant)