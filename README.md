**Random Enchant** 是一个为 Minecraft 添加全新附魔方式的模组。除了传统的附魔台，你现在可以通过攻击生物来随机获得附魔！模组还引入了新工具、新武器以及对方块进行附魔的能力，并新增了多种辅助红石装置的方块附魔，例如帮助水下设置红石线路、调整漏斗行为等。

**此模组必须在客户端和服务端同时安装**才能正常运行。如果你在服务器上游玩，请确保服务端已安装此模组；否则，其所有功能均无法生效。连接到已安装模组的服务器时，客户端也需要安装此模组。

[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.93+-orange?style=flat-square)](https://neoforged.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-brightgreen?style=flat-square)](https://minecraft.net)

## ✨ 特性

- **随机附魔** – 每次攻击生物时，你主手的物品有 **80% 的几率**获得一个随机附魔（可配置）。附魔从所有已注册的附魔（包括来自其他模组的附魔）中平等概率选取。
    - **等级分布**：60% 几率获得 0–2 级，40% 几率获得 6–10 级。
    - **等级叠加**：新等级 = 当前等级 + 1 + 随机值（例如：耐久 II + 耐久 6 → 耐久 9）。

- **方块附魔** – 使用 **附魔刷** 直接对方块施加附魔。
    - **单方块模式**：右键点击方块，添加上附魔刷上的附魔；若附魔刷无附魔，则清除该方块的附魔。
    - **区域模式**：选择两个对角点，对区域内所有非空气、非流体的方块进行批量附魔或清除。耐久消耗取决于方块数量，并受 **耐久** 附魔影响。
    - **粒子反馈**：手持附魔刷时，被附魔的方块会显示绿色粒子。
    - **精准采集**：使用精准采集工具挖掘时，被附魔的方块会保留其附魔（可配置）。

- **珍珠矛** – 一把拥有独特连击技能的全新武器。
    - **右键**：向前冲刺，对路径上的所有生物造成伤害。伤害 = 8 + 横扫之刃等级。
    - **左键**：根据相对速度造成额外伤害，随后水平冲刺（同样对路径上的生物造成伤害）。
    - **特殊协同**：若长矛拥有 **引雷** 附魔，左键攻击会召唤多道闪电。若拥有 **Fly之怒** 附魔，则会召唤强化蜜蜂攻击目标（这两个附魔互斥）。

- **新附魔**（均作为宝藏附魔或通过交易获得）：
    - **Fly之怒**（珍珠矛） – 攻击时召唤蜜蜂。
    - **重定向**（三叉戟） – 投掷后，左键点击可使三叉戟飞向指定位置。
    - **飞行**（鞘翅） – 滑翔时按跳跃键可上升（每 tick 上升高度可配置）。
    - **无重力**/**无阻力**（弓、弩、三叉戟、重锤、钓鱼竿） – 抛射物无视重力/空气和水的阻力。
    - **动能**（弓、弩、钓鱼竿） – 增加抛射物初速度（每级 +30%）。
    - **稳定**（弓、弩） – 消除随机散布（与爆炸附魔互斥）。
    - **爆炸**（弓、弩、方块） – 箭矢击中时爆炸；实体站在方块上时方块爆炸（可配置是否破坏地形）。
    - **可投掷**（重锤） – 右键投掷重锤；支持引雷、忠诚和爆炸附魔。
    - **追踪**（弓、弩） – 箭矢自动导向最近的实体。
    - **二段跳**（靴子） – 可在空中再次跳跃；等级 ≥2 时提供摔落保护。
    - **无诅咒**（任何物品） – 将同时拥有无诅咒和任意诅咒附魔的物品丢出再捡起，即可移除所有诅咒（包括无诅咒本身）。
    - **海之嫌弃**（诅咒） – 物品实体在水中会被排斥。

- **红石辅助** – 方块附魔为红石装置提供了更多可能性：
    - **水下呼吸**（红石元件） – 防止红石线、中继器等被水流冲毁，便于搭建水下电路。
    - **快速装填**（漏斗） – 移除漏斗冷却，实现物品瞬时传输。
    - **绑定诅咒**（漏斗） – 锁定漏斗，禁止其输出物品，可用于精细控制物品流向。

- **新药水**：
    - **回忆药水** – 将你传送回重生点（即使床被破坏）。
    - **魂溯药水** – 将你传送回上一次死亡的位置。

- **游戏内指南** – 当你获得任意一本附魔书时，就会收到一本 **随机附魔指南** 书，详细解释所有特性。你也可以通过命令 `/random_enchant description` 获取。

## 📦 安装方式

1. 为 Minecraft 1.21.1 安装 **NeoForge 21.1.93+**。
2. 安装前置mod **Curios API 9.5.1+1.21.1**（某些功能需要，例如鞘翅的飞行附魔）。可从 [CurseForge](https://www.curseforge.com/minecraft/mc-mods/curios) 或 [Modrinth](https://modrinth.com/mod/curios) 获取。
3. 从 [发布页面](https://github.com/Skrepy0/Random-Enchant-Neoforge/releases)或者[Modrinth](https://modrinth.com/mod/random-enchant-neoforge/versions) 下载最新的版本。
4. 将 JAR 文件放入 **客户端和服务端** 的 `mods` 文件夹中。

## 📜 命令

- `/random_enchant description` – 获取随机附魔指南书。
- `/random_enchant config <选项> [值]` – 在游戏中查看或修改配置（需要权限等级 2）。

  **配置选项**（附默认值）：
    - `doRandomEnchant <true/false>` – 启用/禁用攻击时随机附魔。（默认：`false`）
    - `alwaysEnchantable <true/false>` – 允许任何物品在铁砧上被附魔。（默认：`false`）
    - `explodeDestroyBlock <true/false>` – 爆炸附魔是否破坏地形。（默认：`true`）
    - `infinityUndyingTotem <true/false>` – 无限附魔是否对不死图腾生效。（默认：`false`）
    - `infinityBlock <true/false>` – 无限附魔是否对可放置方块生效。（默认：`false`）
    - `infinityTnt <true/false>` – 无限附魔是否对 TNT 生效（点燃后保留方块）。（默认：`true`）
    - `infinityPotion <true/false>` – 无限附魔是否对药水生效。（默认：`true`）
    - `infinityFood <true/false>` – 无限附魔是否对食物生效。（默认：`true`）
    - `infinityThrowableItem <true/false>` – 无限附魔是否对投掷物（鸡蛋、雪球等）生效。（默认：`true`）
    - `isEnchantedBlockGetatable <true/false>` – 能否用精准采集获取附魔方块。（默认：`true`）
    - `redirectTridentSetPointDistance <整数>` – 重定向附魔的最大飞行距离。（默认：`15`）
    - `flyEnchantmentLiftHeightPerTick <小数>` – 飞行附魔每 tick 的上升速度。（默认：`0.05`）
    - `bedrockViolable <true/false>` – 基岩是否能被模组特性（如海之嫌弃）影响。（默认：`false`）

- `/block_enchant <get|add|remove> <x y z> [附魔ID] [等级]` – 直接管理方块附魔。

## 🛠 新物品与合成配方

| 物品 | 描述 | 合成配方 |
|------|------|----------|
| **附魔刷** | 用于对方块施加附魔。耐久: 387。 | <pre> #P<br> T#<br>B  </pre>其中 P = 附魔书, # = 线, T = 铜锭, B = 木棍。 |
| **珍珠矛** | 拥有冲刺和连击能力的长矛。 | <pre> #P<br>@T#<br>B@ </pre>其中 P = 末影珍珠, # = 回响碎片, T = 沉重核心, @ = 爆裂紫颂果, B = 旋风棒。 |
| **回忆药水** | 传送至你的重生点。 | 无序合成：牛奶瓶 + 末影珍珠 + 糖 + 荧石粉 |
| **魂溯药水** | 传送至你上一次死亡的位置。 | **无合成配方** – 只能通过创造模式物品栏、命令或**战利品宝箱**获得。 |
| **牛奶瓶** | 由玻璃瓶 + 奶桶无序合成。用于合成回忆药水。 | 无序合成：玻璃瓶 + 奶桶 |

## 📖 方块附魔

你可以使用附魔刷赋予方块特殊属性。它们还能为红石系统提供便利：

- **水下速掘（方块版）** – 方块接触水时会被破坏。
- **爆炸保护** – 方块免疫爆炸。
- **爆炸** – 当实体站在方块上时，方块会爆炸（可配置是否破坏地形）。若与无限附魔组合，方块本身不会被摧毁。
- **火焰附加** – 站在方块上的实体被点燃。
- **火焰保护** – 站在方块上的实体身上的火焰被清除。
- **无限** – （TNT专用）TNT被点燃后，原方块保留。
- **击退** – 接触方块的实体会被弹开（向上，或对于按钮、下界传送门、绊线，沿特定方向）。
- **冲击** – （活板门专用）活板门打开时，将上方的实体弹飞。
- **快速装填** – （漏斗专用）**红石辅助**：移除漏斗的冷却时间，实现物品快速传输。
- **绑定诅咒** – （漏斗专用）**红石辅助**：锁定漏斗，使其无法向外输出物品，可用于精准控制物品输出。
- **水下呼吸** – （红石元件）**红石辅助**：防止红石线、中继器等被水流冲毁，便于搭建水下电路。
- **荆棘** – 站在方块上的实体受到等同于附魔等级的伤害。
- **海之嫌弃** – （方块版）水中靠近方块的实体会被推向最近的岸边。

## 🧩 兼容性与依赖

- **必需**：NeoForge 21.1.93+（适用于 Minecraft 1.21.1）
- **必需**：Curios API 9.5.1+1.21.1 – 模组利用 Curios API 添加了新的槽位（如背部槽位），用于支持 Fly 附魔等需要特定槽位的功能。
- **JEI 集成**：与 JEI 兼容。你可以在 JEI 中查看一些新物品及其详细说明。
- **Ender IO 兼容**：某些新附魔可以通过EnderIO中的附魔器获得，可以配合JEI查看配方。
- Random Enchant 与大多数模组兼容。可能会与那些重度修改附魔系统（例如完全替换附魔注册表）的模组冲突。如遇此类情况，部分功能可能受限。如果你遇到问题，请在 [GitHub](https://github.com/Skrepy0/Random-Enchant-Neoforge/issues) 上报告。

## ❤️ 致谢

- **开发者**：[Skrepy2233](https://github.com/Skrepy0)
- **灵感来源与借鉴**：[Mafuyu33](https://github.com/Mafuyu33) ([neomafishmod](https://github.com/Mafuyu33/neomafishmod))，[Mahiru](https://github.com/Mahirukksk)
- **纹理资源**：Ragecraft IV 资源包（珍珠矛纹理的来源）
- **Fabric 版本**：可在 [此处](https://github.com/Skrepy0/Random-Enchant) 获取

---

**Random Enchant** is a Minecraft mod that introduces new ways to enchant items. In addition to the traditional enchanting table, you can now gain enchantments simply by attacking mobs! The mod also adds new tools, weapons, and the ability to enchant blocks, along with several block enchantments that assist with redstone contraptions – such as helping set up underwater redstone circuits, adjusting hopper behavior, and more.

**This mod must be installed on both the client and the server** to function properly. If you're playing on a server, make sure the mod is present on the server; otherwise, none of its features will work. Clients connecting to a modded server will need the mod installed as well.

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
2. Install the required mod **Curios API 9.5.1+1.21.1** (required for certain features, e.g., the Fly enchantment on elytra). Available from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/curios) or [Modrinth](https://modrinth.com/mod/curios).
3. Download the latest version from the [Releases page](https://github.com/Skrepy0/Random-Enchant-Neoforge/releases) or [Modrinth](https://modrinth.com/mod/random-enchant-neoforge/versions).
4. Place the JAR file into the `mods` folder on **both the client and the server**.

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

You can give blocks special properties with the Enchant Brush. They also assist with redstone systems:

- **Aqua Affinity (block)** – Blocks are broken when in contact with water.
- **Blast Protection** – Blocks are immune to explosions.
- **Explosion** – When an entity stands on the block, it explodes (configurable block destruction). If combined with Infinity, the block itself is not destroyed.
- **Fire Aspect** – Entities standing on the block are set on fire.
- **Fire Protection** – Entities standing on the block have their fire cleared.
- **Infinity** – (special for TNT) TNT stays after ignition.
- **Knockback** – Entities touching the block are knocked upward (or in a direction for buttons, nether portals, tripwire).
- **Punch** – (Trapdoors) When opened, entities on top are flung away.
- **Quick Charge** – (Hopper) **Redstone assistance**: Removes the hopper’s cooldown, enabling fast item transfer.
- **Binding Curse** – (Hopper) **Redstone assistance**: Locks the hopper, preventing item output – useful for precise control.
- **Respiration** – (Redstone components) **Redstone assistance**: Prevents redstone dust, repeaters, etc. from being washed away by water, enabling underwater circuits.
- **Thorns** – Entities standing on the block take damage equal to the enchantment level.
- **Bad Luck of the Sea** – (block) Entities in water near the block are pushed toward the nearest shore.

## 🧩 Compatibility & Dependencies

- **Required**: NeoForge 21.1.93+ (for Minecraft 1.21.1)
- **Required**: Curios API 9.5.1+1.21.1 – The mod uses Curios API to add new slots (e.g., back slot) for features like the Fly enchantment on elytra.
- **JEI Integration**: Compatible with JEI. You can view some new items and their detailed descriptions in JEI.
- **Ender IO Compatibility**: Some new enchantments can be obtained through Ender IO's Enchanter, and you can check their recipes with JEI.
- Random Enchant works with most mods. It may conflict with mods that heavily modify the enchantment system (e.g., completely replacing the enchantment registry). In such cases, some features may be limited. If you encounter issues, please report them on [GitHub](https://github.com/Skrepy0/Random-Enchant-Neoforge/issues).

## ❤️ Credits

- **Developer**: [Skrepy2233](https://github.com/Skrepy0)
- **Inspiration & Code Reference**: [Mafuyu33](https://github.com/Mafuyu33) ([neomafishmod](https://github.com/Mafuyu33/neomafishmod)), [Mahiru](https://github.com/Mahirukksk)
- **Textures**: Ragecraft IV resources (source of the Pearl Spear texture)
- **Fabric Edition**: Available [here](https://github.com/Skrepy0/Random-Enchant)