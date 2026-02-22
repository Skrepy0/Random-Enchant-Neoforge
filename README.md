# Random Enchant

**Random Enchant** is a Minecraft mod that overhauls the vanilla enchantment system. Instead of using an enchanting table, you can gain enchantments simply by attacking mobs! The mod also introduces new tools, weapons, and the ability to enchant blocks, opening up countless possibilities for creative builds and combat.

[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.216+-orange?style=flat-square)](https://neoforged.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-brightgreen?style=flat-square)](https://minecraft.net)

## ✨ Features

- **Random Enchantment** – Every time you attack a living entity, your main‑hand item has an **80% chance** to receive a random enchantment (configurable). Enchantments are chosen from all registered enchantments (including those from other mods) with equal probability.
    - **Level distribution**: 60% chance for 0–2 levels, 40% chance for 6–10 levels.
    - **Stacking**: New level = current level + 1 + random value (e.g. Durability II + Durability 6 → Durability 9).

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

- **New Potions**:
    - **Memory Potion** – Teleport you to your spawn point (even if the bed is missing).
    - **Soultrance Potion** – Teleport you to your last death location.

## 📦 Installation

1. Install **NeoForge 21.1.216+** for Minecraft 1.21.1.
2. Download the latest `RandomEnchant-*.jar` from the [Releases](https://github.com/Skrepy0/Random-Enchant-Neoforge/releases) page.
3. Place the JAR file into your `.minecraft/mods` folder.
4. Launch the game and enjoy!

## 🚀 Quick Start

- `/random_enchant doRandomEnchant true` – Enable the random enchantment event.
- `/random_enchant description` – Receive the in‑game guide book.
- Craft an **Enchant Brush** and experiment with block enchanting.
- Obtain a **Pearl Spear** and try its special moves.

## 📜 Commands

- `/random_enchant description` – Gives you the Random Enchant guide book.
- `/random_enchant config <option> <value>` – Modify configuration in‑game (requires permission level 2).

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

- `/block_enchant <get|add|remove> <x y z> [enchantment] [level]` – Manage block enchantments directly.

## 🛠 New Items

| Item | Description |
|------|-------------|
| **Enchant Brush** | Used to apply enchantments to blocks. Durability: 387. Craft with Stick, Copper Ingot, String, and an Enchanted Book. |
| **Pearl Spear** | A spear with dash and combo abilities. Craft with Ender Pearl, Echo Shard, Popped Chorus Fruit, Breeze Rod, and a Heavy Core. |
| **Memory Potion** | Teleports you to your spawn point. Craft with Milk Bottle + Ender Pearl + Sugar + Glowstone Dust. |
| **Soultrance Potion** | Teleports you to your last death location. Brew: Awkward Potion + Sculk Block. |
| **Milk Bottle** | Crafted from Glass Bottle + Milk Bucket. Used in Memory Potion recipe. |

## 📖 Block Enchantments

You can give blocks special properties with the Enchant Brush:

- **Aqua Affinity (block)** – Blocks allow water to flow through and are instantly broken by water.
- **Blast Protection** – Blocks are immune to explosions.
- **Explosion** – When an entity stands on the block, it explodes (configurable block destruction). If combined with Infinity, the block itself is not destroyed.
- **Fire Aspect** – Entities standing on the block are set on fire.
- **Fire Protection** – Entities standing on the block have their fire cleared.
- **Frost Walker** – (Not implemented yet)
- **Infinity** – (special for TNT) TNT stays after ignition.
- **Knockback** – Entities touching the block are knocked upward (or in a direction for buttons, nether portals, tripwire).
- **Punch** – (Trapdoors) When opened, entities on top are flung away.
- **Quick Charge** – (Hoppers) Removes the hopper’s cooldown, making items flow instantly.
- **Respiration** – Redstone components are not washed away by water.
- **Thorns** – Entities standing on the block take damage equal to the enchantment level.
- **Depth Strider** – (block) Entities in water near the block are pushed toward the nearest shore.

## 🧩 Compatibility

Random Enchant works with most mods. It may conflict with mods that heavily modify the enchantment system (e.g., completely replacing the enchantment registry). In such cases, some features may be limited. If you encounter issues, please report them on GitHub.

## ❤️ Credits

- **Developer**: [Skrepy2233](https://github.com/Skrepy0)
- **Inspiration**: [Mafuyu33](https://github.com/Mafuyu33) ([neomafishmod](https://github.com/Mafuyu33/neomafishmod)), [Mahiru](https://github.com/Mahirukksk)
- **Textures**: Ragecraft IV resources (source of the Pearl Spear texture)
- **Fabric Edition**: Available [here](https://github.com/Skrepy0/Random-Enchant)
---
