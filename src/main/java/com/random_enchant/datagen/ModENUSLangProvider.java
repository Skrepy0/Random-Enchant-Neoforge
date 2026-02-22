package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.List;

import static com.random_enchant.enchantment.ModEnchantHelper.toRoman;

public class ModENUSLangProvider extends LanguageProvider {
    public ModENUSLangProvider(PackOutput output) { super(output, RandomEnchant.MOD_ID, "en_us"); }

    @Override
    protected void addTranslations() {
        add("itemGroup.random_enchant.title", "Random Enchant");

        // Configuration entries
        add("random_enchant.configuration.randomEnchant", "§gRandom Enchant§r Event");
        add("random_enchant.configuration.isAlwaysEnchantable", "All Items Enchantable");
        add("random_enchant.configuration.infinityUndyingTotem", "§a[Infinity]§r Compatibility with Totem of Undying");
        add("random_enchant.configuration.infinityBlock", "§a[Infinity]§r Compatibility with Placeable Blocks");
        add("random_enchant.configuration.explodeDestroyBlock", "Explosions from this mod can destroy blocks");
        add("random_enchant.configuration.infinityTnt", "§a[Infinity]§r Compatibility with TNT");
        add("random_enchant.configuration.infinityPotion", "§a[Infinity]§r Compatibility with Potions");
        add("random_enchant.configuration.infinityFood", "§a[Infinity]§r Compatibility with Food");
        add("random_enchant.configuration.infinityThrowableItem", "§a[Infinity]§r Compatibility with Throwable Items");
        add("random_enchant.configuration.isEnchantedBlockGetatable", "§b[Silk Touch]§r Obtain Enchanted Blocks");
        add("random_enchant.configuration.bedrockViolable",
            "§7[Bedrock]§r Can be destroyed or removed by this mod's features");
        add("random_enchant.configuration.redirectTridentSetPointDistance", "§6[Redirect]§r Maximum Raycast Distance");
        add("random_enchant.configuration.flyEnchantmentLiftHeightPerTick",
            "Height gained per tick when using elytra with §d[Fly]§r enchantment");

        // Tooltips for configuration
        add("random_enchant.configuration.randomEnchant.tooltip",
            "When enabled, attacking mobs will §drandomly enchant§r the item in the player's main hand");
        add("random_enchant.configuration.isAlwaysEnchantable.tooltip",
            "When enabled, §lall items§r can be enchanted via §aanvil§r");
        add("random_enchant.configuration.infinityBlock.tooltip",
            "When enabled, placing a block with §a[Infinity]§r enchantment does not consume the block from your hand");
        add("random_enchant.configuration.infinityUndyingTotem.tooltip",
            "When enabled, using a §dTotem of Undying§r with §a[Infinity]§r enchantment will return an identical totem. For compatibility reasons, the returned item may end up in a different slot than before use.");
        add("random_enchant.configuration.infinityTnt.tooltip",
            "When enabled, TNT ignited by flint & steel, redstone, or explosions will spawn an entity but the original block remains.");
        add("random_enchant.configuration.infinityThrowableItem.tooltip",
            "When enabled, throwable items with §a[Infinity]§r enchantment (e.g., Ender Pearls, eggs) are not consumed upon use");
        add("random_enchant.configuration.bedrockViolable.tooltip",
            "When enabled, bedrock can be destroyed through this mod's features");
        add("random_enchant.configuration.isEnchantedBlockGetatable.tooltip",
            "When enabled, players using a tool with §b[Silk Touch]§r enchantment can mine enchanted blocks and obtain them with their enchantments intact");
        add("random_enchant.configuration.redirectTridentSetPointDistance.tooltip",
            "Maximum distance between the player and the landing point when right-clicking with a trident enchanted with §6[Redirect]§r");
        add("random_enchant.configuration.flyEnchantmentLiftHeightPerTick.tooltip",
            "Height gained per tick when pressing the jump key while using an elytra enchanted with §d[Fly]§r");

        // Items
        add(ModItems.PEARL_SPEAR.get(), "Pearl Spear");
        add(ModItems.ENCHANT_BRUSH.get(), "Enchant Brush");
        add(ModItems.LIGHTNING_ITEM.get(), "Lightning");
        add(ModItems.MILK_BOTTLE.get(), "Milk Bottle");
        add(ModItems.MEMORY_POTION.get(), "§bMemory Potion");
        add(ModItems.SOULTRANCE_POTION.get(), "§aSoultrance Potion");

        // Enchantments
        add("enchantment.random_enchant.fury_of_fly", "§aFury of Fly");
        add("enchantment.random_enchant.fury_of_fly.desc", "Summon bees to assist in combat");
        add("enchantment.random_enchant.bad_luck_of_the_sea", "§cBad Luck of the Sea§r");
        add("enchantment.random_enchant.bad_luck_of_the_sea.desc", "Not very favored by the sea");
        add("enchantment.random_enchant.redirect_projectile", "§6Redirect Projectile§r");
        add("enchantment.random_enchant.redirect_projectile.desc", "Right-click to freely control your trident");
        add("enchantment.random_enchant.fly", "§dFly§r");
        add("enchantment.random_enchant.fly.desc", "While flying with an elytra, press jump to ascend§r");
        add("enchantment.random_enchant.no_gravity", "No Gravity");
        add("enchantment.random_enchant.no_gravity.desc", "Arrows or tridents shot ignore gravity");
        add("enchantment.random_enchant.no_resistance", "No Resistance");
        add("enchantment.random_enchant.no_resistance.desc",
            "Shot arrows, fishing line, or tridents ignore water and air resistance");
        add("enchantment.random_enchant.no_curse", "§aNo Curse§r");
        add("enchantment.random_enchant.no_curse.desc", "When you pick up an item, all curse enchantments are removed");
        add("enchantment.random_enchant.steady", "§bSteady§r");
        add("enchantment.random_enchant.steady.desc", "Arrows shot have no random momentum, making them more accurate");
        add("enchantment.random_enchant.kinetic", "§bKinetic§r");
        add("enchantment.random_enchant.kinetic.desc", "Arrows or fishing line are shot with higher initial velocity");
        add("enchantment.random_enchant.explode", "§6Explode§r");
        add("enchantment.random_enchant.explode.desc",
            "When applied to a bow or crossbow, shoots explosive arrows. When applied to a block, the block explodes when an entity touches its top surface.");
        add("enchantment.random_enchant.throwable", "Throwable");
        add("enchantment.random_enchant.throwable.desc", "Allows the mace to be thrown");
        add("enchantment.random_enchant.tracking", "§aTracking§r");
        add("enchantment.random_enchant.tracking.desc", "Arrows track their target");
        add("enchantment.random_enchant.double_jump", "Double Jump");
        add("enchantment.random_enchant.double_jump.desc",
            "Grants the player a double jump ability. At level II and above, landing after a double jump negates fall damage.");

        // Messages
        add("message.random_enchant.backup_warning", "§6Random Enchant is in alpha, please back up your world often.");
        add("message.random_enchant.enchant_added", "§6Enchanted:§r");
        add("message.random_enchant.item.enchant_brush.selected_1",
            "§aStarting point set. Please click the second block to define the area.");
        add("message.random_enchant.item.enchant_brush.selected_2", "§aArea enchantment operation completed.");
        add("message.random_enchant.item.enchant_brush.clear_area",
            "§aAll enchantments in the area have been cleared.");
        add("message.random_enchant.item.enchant_brush.status.changed", "§aSwitched to: ");
        add("message.random_enchant.item.enchant_brush.durability_insufficient",
            "§cInsufficient durability. Operation failed. Selected area size: %s");
        add("message.random_enchant.item.enchant_brush.clear_data", "Enchant brush data cleared");
        add("message.random_enchant.item.soultrance_potion.teleport_failed",
            "§cTeleport failed. You have no death records.");

        // Item tooltips
        add("item.tooltip.random_enchant.for_shift_tooltip", "Press §6[SHIFT]§r for more details");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_1",
            "§bLeft-click to attack an entity and teleport; right-click to teleport§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_2",
            "§bEntities in the teleportation path take 8 base damage (actual damage scales with §a[Sharpness]§b level)§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_3",
            "§bLeft-click attack damage increases with relative velocity to the target§r");
        add("item.tooltip.random_enchant.enchant_brush.status", "Mode: ");
        add("item.tooltip.random_enchant.enchant_brush.status.regional", "§aRegional Mode§r");
        add("item.tooltip.random_enchant.enchant_brush.status.single", "§aSingle Block Mode§r");

        // Keybindings
        add("key.random_enchant.toggle_brush_status", "Toggle Enchant Brush Mode");
        add("key.categories.random_enchant", "Random Enchant");

        // Commands
        add("command.random_enchant.randomEnchant.enable", "§aRandom Enchant enabled");
        add("command.random_enchant.randomEnchant.disable", "§6Random Enchant disabled");
        add("command.random_enchant.config.changed", "has been changed to");
        add("command.random_enchant.config.unchanged", "'s status §cwas not effectively changed§r");
        add("command.random_enchant.block_enchant.enchant_tag", "§6Enchantments: §r");
        add("command.random_enchant.block_enchant.has_no_enchantment", "§cThis block has no enchantments§r");
        add("command.random_enchant.block_enchant.add_1", "§bAdded enchantment to block at §a");
        add("command.random_enchant.block_enchant.add_2", "§b enchantment: §a");
        add("command.random_enchant.block_enchant.remove", "§gAll enchantments removed from this block§a");

        // JEI info pages
        add("item.jei_info.random_enchant.pearl_spear",
            "§l§6Pearl Spear§r\n"
                    + "\n"
                    +
                    "§7Base Stats:§f Attack Damage 8, Attack Speed 1.6, Durability 512, greatly increased movement speed while held.\n"
                    + "\n"
                    + "§lRight-click Ability: Blink Strike§r\n"
                    + "• Teleports in the direction you're looking, up to 10 blocks\n"
                    + "• Entities in the path take §e8 + Sweeping Edge level§r damage\n"
                    + "• Cooldown §c200 ticks§r, can be greatly reduced by §bQuick Charge§r\n"
                    + "\n"
                    + "§lLeft-click Ability: Kinetic Strike§r\n"
                    + "• Deals bonus damage based on relative velocity to the target\n"
                    + "  - On ground: Damage = (target velocity projection - player velocity projection) × 10 + 9\n"
                    + "  - In air: Damage = (player velocity projection - target velocity projection) × 10 + 9\n"
                    + "• After attack, teleports horizontally, dealing same damage to entities in the path\n"
                    + "• If enchanted with §6Channeling§r, summons multiple lightning bolts\n"
                    + "• If enchanted with §aFury of Fly§r, summons empowered bees (number = level)\n"
                    + "\n"
                    + "§lExclusive Enchantment: §aFury of Fly§r\n"
                    + "• Levels I~III, summons bees after left-click attack\n"
                    + "• Bees have extra health, fire resistance, strength, etc.\n"
                    + "• Mutually exclusive with §6Channeling§r\n"
                    + "\n"
                    +
                    "§lCompatible Enchantments:§f Sword enchantments (Sharpness, Knockback, Fire Aspect, Looting, Sweeping Edge), Unbreaking, Quick Charge, Channeling, Wind Burst, Fury of Fly\n"
                    + "\n"
                    +
                    "§lTip:§7 Combine with elytra for high-speed burst damage; right-click blink to close in or escape.");
        add("item.jei_info.random_enchant.memory_potion",
            "§d§lMemory Potion§r\\n§lEffect§r:\\nDrink to instantly teleport to your respawn point in the current dimension (i.e., your bed location). Even if the bed is destroyed, you'll return to where you last set your spawn. If you've never set a spawn, you'll teleport to the world spawn.\\n\\n§lUsage§r:\\nQuickly return to your base or shelter, ideal for exploration.\\n\\n§lNote§r:\\nConsumed upon use in Survival mode. Teleportation does not affect your equipped items or armor.");
        add("item.jei_info.random_enchant.soultrance_potion",
            "§a§lSoultrance Potion§r\\n§lEffect§r:\\nDrink to attempt teleportation to your last death location. If you have no death record (e.g., new player or death counter cleared), teleportation fails, the potion is consumed, and a message appears: \"§cTeleport failed. You have no death records.§r\"\\n\\n§lUsage§r:\\nQuickly return to your death point to retrieve items or explore where you died.\\n\\n§lNote§r:\\nThe destination may still be dangerous (e.g., in lava or near mobs), so prepare accordingly.");
        add("item.jei_info.random_enchant.enchant_brush",
            "§l§6Enchant Brush§r\\n\\n§lBase Stats§r:\\n• Durability: §a387§r\\n• Cannot be enchanted in an enchanting table; can be enhanced with enchanted books on an anvil\\n\\n§lDurability Consumption§r:\\nActual consumption = number of blocks processed × durability factor, where durability factor depends on Unbreaking level:\\n• No Unbreaking: factor 1.0\\n• Unbreaking I: factor 0.9\\n• Unbreaking II: factor 0.7\\n• Unbreaking III: factor 0.5\\n• Unbreaking IV: factor 0.35\\n• Unbreaking V: factor 0.2\\n• Unbreaking VI: factor 0.1\\n• Unbreaking VII: factor 0.08\\n• Unbreaking ≥VIII: factor 0.05\\n\\n§lMode Switching§r: Press §eTAB§r\\n\\n§7■ Single Block Mode§r:\\n• Right-click a single block to apply all enchantments from the brush to that block. If the brush has no enchantments, it will remove enchantments from the block (no durability cost).\\n\\n§7■ Regional Mode§r:\\n• First right-click selects the starting point, indicated by §agreen particles§r.\\n• Second right-click selects the end point, applying a batch operation to all §bnon-air/non-water/non-lava§r blocks in the region:\\n  - Brush has enchantments: Adds all brush enchantments to the blocks. Durability cost = number of blocks processed × durability factor. If durability is insufficient, operation fails with a message.\\n  - Brush has no enchantments: Removes enchantments from all blocks in the region. Durability cost = number of blocks processed × durability factor.\\n• If a start point is selected but no end point, hold §eShift+Right-click§r to clear the start point and select anew.\\n\\n§lVisual Indicators§r:\\n• While holding the Enchant Brush, enchanted blocks continuously emit §agreen particles§r.\\n• In Regional Mode, start and end points briefly display particle effects when selected.\\n\\n§lSilk Touch Compatibility§r:\\n• Mining an enchanted block with a Silk Touch tool will drop the block with its enchantments intact (controlled by config §eisEnchantedBlockGetatable§r, enabled by default).\\n\\n§lNotes§r:\\n• Durability consumption in Regional Mode depends on the actual number of blocks processed and is reduced by Unbreaking.\\n• If the brush lacks durability, the operation is canceled and the start point is cleared.\\n• An Enchant Brush with no enchantments can be used to quickly clear enchantments from a region.");

        // Advancements
        add("advancements.enchant.root.title", "Random Enchant");
        add("advancements.enchant.root.description", "Begin your enchanting journey");
        add("advancements.enchant.get_enchant_brush.title", "Enchanting Artist");
        add("advancements.enchant.get_enchant_brush.description", "Obtain an Enchant Brush");
        add("advancements.enchant.get_pearl_spear.title", "Ender Impale");
        add("advancements.enchant.get_pearl_spear.description", "Obtain a Pearl Spear");
        add("advancements.enchant.use_region_mode.title", "Area Planner");
        add("advancements.enchant.use_region_mode.description", "Use Regional Mode to enchant blocks");
        add("advancements.enchant.trigger_fly_of_fury.title", "Swarm Assault");
        add("advancements.enchant.trigger_fly_of_fury.description",
            "Left-click with a Pearl Spear enchanted with Fury of Fly to summon bees");
        add("advancements.enchant.drink_memory_potion.title", "Homecoming");
        add("advancements.enchant.drink_memory_potion.description",
            "Drink a Memory Potion to return to your respawn point");
        add("advancements.enchant.drink_soultrance_potion.title", "Soul Trace");
        add("advancements.enchant.drink_soultrance_potion.description", "Drink a Soultrance Potion");
        add("advancements.enchant.bad_luck_of_the_sea.title", "Bad Luck of the Sea");
        add("advancements.enchant.bad_luck_of_the_sea.description", "Enchant a block with §cBad Luck of the Sea§r");
        add("advancements.enchant.thor_hammer.title", "Mjölnir");
        add("advancements.enchant.thor_hammer.description", "Throw a mace enchanted with Channeling");
        add("advancements.enchant.cleanse_curse.title", "Better Without Curses");
        add("advancements.enchant.cleanse_curse.description",
            "Use the No Curse enchantment to remove all curses from an item");
        add("advancements.enchant.explode.title", "Explosion is Art");
        add("advancements.enchant.explode.description", "Enchant a block with §6Explode§r");
        add("advancements.enchant.hyper_transfer.title", "Hyper Transfer");
        add("advancements.enchant.hyper_transfer.description",
            "Enchant a hopper with §dQuick Charge§r using an Enchant Brush");

        // Event messages
        add("event.random_enchant.get_guide", "Achievement unlocked! You've been rewarded with a §6§lGuide§r");

        // Guide book
        add("item.random_enchant.guide.title", "§6§lGuide§r");
        var book_path = "item.random_enchant.guide.page.";
        List<
                String> pages = List
                                        .of(
                                                // 1. 目录
                                                "§l§nTable of Contents§r\n\n§61. Overview§r\n§62. Random Enchant Event§r\n§63. Block Enchanting & Enchant Brush§r\n§64. Block Enchant Effects§r\n§65. Pearl Spear§r\n§66. New Potions§r\n§67. Other Enchantments§r\n§68. Commands§r\n§69. Config Options§r",

                                                // 2. 概述
                                                "§l§n1. Overview§r\n\n✔ Attacking mobs may randomly enchant held item (toggleable)\n✔ Use §6Enchant Brush§r on blocks\n✔ New §dPearl Spear§r, enchants, potions\n✔ Commands: §b/block_enchant§r and §b/random_enchant§r",

                                                // 3. 随机附魔事件 (第一部分)
                                                "§l§n2. Random Enchant Event§r\n\n§a80%§r chance on attack.\n•§4Pool§r: All registered enchants (equal chance)\n•§2Level§r: §a60%§r: §50-2§r, §640%§r: §56-10§r\n•§2Stacking§r: §7New = current + 1 + random§r",

                                                // 4. 随机附魔事件 (第二部分)
                                                "Success shows screen message.\nExample: Unbreaking II + Unbreaking 6 = Unbreaking 9.\nCan be disabled in config.",

                                                // 5. 方块附魔与附魔刷 (合成)
                                                "§l§n3. Block Enchanting & §dEnchant Brush§r\n\nCraft (workbench):\nTop: [Empty] [String] [Ench. Book]\nMid: [Empty] [Copper] [String]\nBot: [Stick] [Empty] [Empty]\nDurability 387, anvil-enchantable only.",

                                                // 6. 模式切换
                                                "Press §6TAB§r to switch modes:\n\n§7■Single§r: Apply/clear block enchant.\n§7■Regional§r: Select two corners to apply/clear area. Durability cost based on §6Unbreaking§r & block count.",

                                                // 7. 区域模式和精准采集
                                                "Regional: §6Shift+Right-click§r clears start point.\nHolding brush shows enchanted blocks with §agreen particles§r.\nSilk Touch can obtain enchanted blocks (config).",

                                                // 8. 方块附魔效果表 (第一部分)
                                                "§l§n4. Block Enchant Effects§r\n\n§6Bad Luck (Water)§r: Pushes entities ashore. §cNo effect on chests§r.\n§6Aqua Affinity§r: Auto-breaks underwater, chance per level.\n§6Respiration§r: Protects water-sensitive blocks.",

                                                // 9. 方块附魔效果表 (第二部分)
                                                "§6Quick Charge§r (Hopper): No cooldown, double transfer.\n§6Curse of Binding§r (Hopper): Locks hopper.\n§6Explode§r: Entity steps on → explodes (power = level×0.3). Config terrain damage; Infinity keeps block.\n§6Knockback§r: Vertical launch (force = level×0.5).",

                                                // 10. 方块附魔效果表 (第三部分)
                                                "§6Feather Falling§r: No fall damage on block.\n§6Blast Protection§r: Block immune to explosions.\n§6Thorns§r: Damages stepper (level).\n§6Punch§r (Trapdoor): Launches entities (force = level).\n§6Fire Aspect§r: Sets on fire (duration = level×10 ticks).\n§6Fire Protection§r: Extinguishes fire.",

                                                // 11. 方块附魔效果表 (第四部分)
                                                "§6Aqua Affinity (Advanced)§r: Block lets water through, breaks in flowing water.\n§6Infinity§r (TNT): Ignited TNT spawns entity, block remains (config).",

                                                // 12. 新武器: 珍珠矛 (合成和基础属性)
                                                "§l§n5. Pearl Spear§r\n\nRecipe:\n[Empty] [Ender Pearl] [Echo Shard]\n[Popped Chorus] [Mace] [Echo Shard]\n[Breeze Rod] [Popped Chorus] [Empty]\n§2Dmg 8, Spd 1.6, Dur 512, +movespeed§r",

                                                // 13. 珍珠矛 (右键/左键能力)
                                                "§lRight-click§r: Teleport up to 10 blocks, damage path (8 + Sweeping Edge). Cooldown 200 ticks, reduced by §aQuick Charge§r.\n§lLeft-click§r: Bonus velocity damage, teleport horizontally, same path damage.",

                                                // 14. 珍珠矛 (附魔和互斥)
                                                "With §6Channeling§r: Summons lightning. With §aFury of Fly§r: Summons empowered bees (count = level). Mutually exclusive.\nSupports sword enchants, Unbreaking, Quick Charge, Wind Burst.",

                                                // 15. 新药水
                                                "§l§n6. New Potions§r\n\n§6Milk Bottle§r: Glass + Milk bucket, clears effects.\n§bMemory Potion§r: Milk + Ender Pearl + Sugar + Glowstone. Teleports to spawn (even if bed gone).\n§aSoultrance Potion§r: Uncraftable. Teleports to last death.",

                                                // 16. 其他附魔详解 (Fly, Double Jump, Bad Luck boots)
                                                "§l§n7. Other Enchantments§r\n\n§dFly§r (Elytra): Jump to ascend (configurable).\n§6Double Jump§r (Boots): Mid-air jump. Lv2+ negates fall damage, particles.\n§cBad Luck (Boots)§r: In water, pushes to shore.",

                                                // 17. 其他附魔 (Throwable, No Gravity, No Resistance, Kinetic, Steady)
                                                "§6Throwable§r (Mace): Right-click throw, speed scales with Kinetic. Compat: §7Channeling, Loyalty, Explode§r.\n§6No Gravity§r: Ignores gravity.\n§6No Resistance§r: No air/water drag.\n§bKinetic§r: Boosts projectile speed.\n§bSteady§r: No arrow spread.",

                                                // 18. 其他附魔 (Explode bow, Strength shovel)
                                                "§6Explode§r (Bow/Crossbow): Arrows explode on hit (entity: power = level; ground: level×0.2). §cArrow destroyed§r.\n§2Strength§r (Shovel): Right-click launches block, damage based on hardness.",

                                                // 19. 其他附魔 (Redirect, Multishot)
                                                "§6Redirect§r (Trident): Hovers slowly, left-click to direct. Works with Fire Aspect, excludes §aLooting§r.\n§6Multishot+Redirect§r: Spiral throws, each redirectable.",

                                                // 20. 其他附魔 (Tracking, No Curse, Bad Luck curse)
                                                "§aTracking§r (Bow/Crossbow): Arrows home on targets.\n§aNo Curse§r: Drop/pickup removes all curses (including itself).\n§cBad Luck (Curse)§r: Items in water are repelled; affects throwables.",

                                                // 21. 其他附魔 (Totem enchantments)
                                                "§6Totem Enchants§r:\n•§6Infinity§r: Returns totem after use (§cslot may change§r).\n•§6Blast Protection§r: Explodes on use (power = level-1).\n•§6Channeling§r: Lightning strikes attacker.",

                                                // 22. 其他附魔 (Infinity other uses)
                                                "§6Infinity§r Other Uses:\n•§dBucket§r: Not consumed.\n•§bFood§r: Not consumed.\n•§dBlocks§r: Placing doesn't consume (config).\n•§cTNT§r: Ignited TNT block remains (config).\n•§dElytra§r: Flight ignores air resistance.",

                                                // 23. 互斥关系 (拆分自原页23)
                                                "§lExclusions§r:\n•Fury of Fly ↔ Channeling\n•Redirect ↔ Looting\n•Explode ↔ Steady\n•Kinetic ↔ Flame",

                                                // 24. 宝藏附魔 (拆分自原页23)
                                                "§lTreasure Enchants§r (loot/trade only):\n§aFury of Fly§r, §6Redirect§r, §dFly§r, No Gravity, No Resistance, §aNo Curse§r, §bSteady§r, §bKinetic§r, §6Explode§r, Throwable, Double Jump, §aTracking§r, §cBad Luck§r.",

                                                // 25. 命令指南 (block_enchant)
                                                "§l§n8. Commands§r\n\n§6/block_enchant <get|add|remove> <xyz> [id level]\n•get: Show block enchants\n•add: Add enchant\n•remove: Clear all\nExample: §b/block_enchant add ~ ~ ~ minecraft:sharpness 3§r",

                                                // 26. 命令指南 (random_enchant desc 和部分config)
                                                "§6/random_enchant <desc|config> [args]§r\n•§ddesc§r: Get this book\n•§dconfig§r: Toggle settings (perm 2)\n  doRandomEnchant <bool>\n  alwaysEnchantable <bool>",

                                                // 27. 命令指南 (config 子命令 第一部分)
                                                "  infinityUndyingTotem <bool>\n  explodeDestroyBlock <bool>\n  infinityBlock <bool>\n  infinityTnt <bool>\n  infinityFood <bool>\n  infinityThrowableItem <bool>\n  isEnchantedBlockGetatable <bool>",

                                                // 28. 命令指南 (config 子命令 第二部分)
                                                "  infinityPotion <bool>\n  redirectTridentSetPointDistance <int>\n  flyEnchantmentLiftHeightPerTick <double>\n  bedrockViolable <bool>",

                                                // 29. 配置默认值 第一部分
                                                "§l§n9. Config Defaults§r\n\n§6randomEnchant§r: false (attack)\n§6alwaysEnchantable§r: false (anvil all)\n§6explodeDestroyBlock§r: true (terrain damage)",

                                                // 30. 配置默认值 第二部分
                                                "§6infinityUndyingTotem§r: false\n§6infinityBlock§r: false\n§6infinityTnt§r: true\n§6bedrockViolable§r: false\n§6infinityPotion§r: true\n§6infinityFood§r: true\n§6infinityThrowableItem§r: true\n§6isEnchantedBlockGetatable§r: true",

                                                // 31. 配置默认值 第三部分 (redirect 和 fly)
                                                "§6redirectTridentSetPointDistance§r: 15\n§6flyEnchantmentLiftHeightPerTick§r: 0.05",

                                                // 32. 附魔刷耐久和小贴士
                                                "§l§dEnchant Brush§r Durability (Unbreaking):\n•None: 20% save\n•I: 40% save\n•II: 60% save\n•≥III: 80% save\n\n§lTips§r:\n• Ensure enough durability for Regional Mode.\n• Empty brush clears area enchants.");
        for (int i = 1; i <= pages.size(); ++i) {
            add(book_path + i, pages.get(i - 1));
        }
        System.out.println("book pages:" + pages.size());

        // Roman numeral levels up to 255
        for (int i = 11; i < 256; i++) {
            add("enchantment.level." + i, toRoman(i));
        }
    }
}
