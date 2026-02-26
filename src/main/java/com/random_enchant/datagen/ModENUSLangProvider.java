package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.List;

import static com.random_enchant.enchantment.ModEnchantHelper.toRoman;

public class ModENUSLangProvider extends LanguageProvider {
    public ModENUSLangProvider(PackOutput output) {
        super(output, RandomEnchant.MOD_ID, "en_us");
    }

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
        add("random_enchant.configuration.infinityDispenser","§a[Infinity]§r Compatibility with Dispenser");
        add("random_enchant.configuration.infinityDispenser.tooltip","Enable this to make dispenser and dispenser launcher shoot items with §a[Infinity]§r enchantment not disappear");
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
        add(ModItems.GUIDE.get(),"Guide");

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
        add("command.random_enchant.value", "Config §d[%s]§r is currently set to:");
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
        add("item.jei_info.random_enchant.lightning",
                "§6Lightning Item§r can be obtained by having a lightning bolt strike a §aLightning Rod§r enchanted with §aSilk Touch§r during a thunderstorm.\n\n" +
                        "The lightning rod must be placed outdoors in a thunderstorm, and the strike must directly hit the rod. The item will drop as an entity to be picked up.");
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
        add("advancements.enchant.lightning_catcher.title","Lightning Catcher");
        add("advancements.enchant.lightning_catcher.description","Get a §bLightning§r");

        // Event messages
        add("event.random_enchant.get_guide", "Achievement unlocked! You've been rewarded with a §6§lGuide§r");

        // Guide book
        add("item.random_enchant.guide.title", "§6§lGuide§r");
        add("gui.random_enchant.guide.title","§d§lRandom Enchant§r§l Guide");
        add("gui.random_enchant.guide.no_results", "§cNo results found");
        add("gui.random_enchant.guide.search","🔍Search...");
        add("gui.random_enchant.guide.previous_button","§6Previous Page");
        add("gui.random_enchant.guide.next_button","§6Next Page");
        var book_path = "item.random_enchant.guide.page.";
        List<String> pages = List.of(
                // Page 1: Core Mechanics
                "§l§6Random Enchant Guide§r\n\n§oObtain this book by completing the root advancement§r\n\n§l§nTable of Contents§r\n§61. Core Mechanics§r\n§62. Block Enchantment Encyclopedia§r\n§63. Pearl Spear & Potions§r\n§64. Item Enchantment Encyclopedia§r\n§65. Command Guide§r\n§66. Default Configurations§r\n§67. Tips§r\n\n§l§n1. Core Mechanics§r\n\n✔ When attacking mobs, the item in your main hand has an §a80%§r chance to receive a random enchantment.\n• §eEnchantment Pool§r: All registered enchantments (including those from other mods), equal probability.\n• §eLevels§r: §a60%§r chance for levels §b0–2§r, §a40%§r chance for levels §b6–10§r.\n• §eStacking§r: New level = current level + §b1§r + random value.\nExample: Unbreaking §bII§r + Unbreaking §b6§r = Unbreaking §b9§r.\n\n✔ Use the §6Enchant Brush§r to enchant blocks. Press §6TAB§r to switch modes.\n• §eSingle-Block Mode§r: Right-click a block to add/remove enchantments.\n• §eArea Mode§r: Select two points to enchant/clear blocks in bulk. Durability consumption is affected by the §6Unbreaking§r enchantment and the number of blocks.\n• §eClear Start Point§r: §6Shift+Right-Click§r.\n\nEnchanted blocks display §agreen particles§r when holding the brush. Enchanted blocks can be obtained with Silk Touch (configurable).",

// Page 2: Block Enchantment Encyclopedia (with Dispenser/Projector enchants)
                "§l§n2. Block Enchantment Encyclopedia§r\n\n" +
                        "§6Bad Luck of the Sea (Curse)§r (bad_luck_of_the_sea)\n" +
                        "• Effect: When water flows over the block, it breaks into a falling block entity that is pushed toward the nearest shore in water. Upon landing, it reverts to its original block and retains enchantments.\n" +
                        "• Trigger: Water flows through or toward the block.\n" +
                        "• Applicable to: Any solid block (bedrock is configurable).\n\n" +
                        "§6Blast Protection§r (blast_protection)\n" +
                        "• Effect: The block is immune to explosion damage.\n" +
                        "• Trigger: Any explosion occurs.\n\n" +
                        "§6Feather Falling§r (feather_falling)\n" +
                        "• Effect: Reduces fall damage when an entity lands on the block. Damage reduction factor = §e(-0.25×level+1)§r.\n" +
                        "• Trigger: Entity lands on the block.\n\n" +
                        "§6Quick Charge§r (quick_charge) — Hopper exclusive\n" +
                        "• Effect: The hopper's cooldown is forced to §b0§r, enabling instant item transfer.\n" +
                        "• Trigger: After the hopper attempts to move items.\n\n" +
                        "§6Binding Curse§r (binding_curse) — Hopper exclusive\n" +
                        "• Effect: The hopper cannot output items (locked).\n" +
                        "• Trigger: Hopper attempts to eject items.\n\n" +
                        "§6Knockback§r (knockback)\n" +
                        "• Effect: Entities touching the block are knocked back. Force = level × §b0.5§r.\n" +
                        "  - Normal blocks / pressure plates: Vertical upward.\n" +
                        "  - Buttons: Horizontal in the button's facing direction.\n" +
                        "  - Nether portals / tripwire: Pushed outward from the center.\n" +
                        "• Trigger: Entity enters the block's collision box or steps on it.\n\n" +
                        "§6Punch§r (punch) — Trapdoor exclusive\n" +
                        "• Effect: When the trapdoor opens, entities on top are flung at a §b45°§r upward angle. Force = level.\n" +
                        "• Trigger: Trapdoor opens.\n\n" +
                        "§6Respiration§r (respiration)\n" +
                        "• Effect: The block is not washed away by water.\n" +
                        "• Trigger: Water attempts to destroy the block.\n\n" +
                        "§6Projectile Protection§r (projectile_protection) — Target block exclusive\n" +
                        "• Effect: When hit by a projectile, it fires an arrow back at the attacker.\n" +
                        "• Trigger: Projectile hits the target block.\n\n" +
                        "§6Thorns§r (thorns)\n" +
                        "• Effect: Entities standing on the block take damage equal to the level (damage source: cactus).\n" +
                        "• Trigger: Entity enters the block's collision box or steps on it.\n\n" +
                        "§6Infinity§r (infinity) — TNT exclusive\n" +
                        "• Effect: When TNT is ignited, a primed TNT entity is spawned, but the original block remains (configurable).\n" +
                        "• Trigger: Flint and steel, redstone signal, explosion, or flaming projectile ignites TNT.\n\n" +
                        "§6Aqua Affinity§r (aqua_affinity)\n" +
                        "• Effect: Allows water to flow through the block, and water destroys the block on contact (drops as item).\n" +
                        "• Trigger: Water spreads or flows through.\n\n" +

                        "§lDispenser/Dropper Exclusive Enchantments§r\n" +
                        "§6Kinetic§r (KINETIC)\n" +
                        "• Effect: Items or projectiles launched gain increased velocity, §b+30%§r per level.\n" +
                        "• Applicable to: Dispensers, droppers.\n\n" +
                        "§6Steady§r (STEADY)\n" +
                        "• Effect: Eliminates random inaccuracy, launching projectiles in a straight line.\n" +
                        "• Applicable to: Dispensers, droppers.\n\n" +
                        "§6No Gravity§r (NO_GRAVITY)\n" +
                        "• Effect: Projectiles launched are unaffected by gravity.\n" +
                        "• Applicable to: Dispensers, droppers.\n\n" +

                        "§lGeneral Mechanics§r\n" +
                        "• Enchantment data is stored independently; enchanted blocks can be obtained with Silk Touch (configurable).\n" +
                        "• Area mode with the Enchant Brush consumes durability, reduced by the §6Unbreaking§r enchantment.\n" +
                        "• Some behaviors are controlled by configs: §6bedrockViolable§r, §6infinityTnt§r, §6infinityDispenser§r, etc.",

// Page 3: Pearl Spear & Potions
                "§l§n3. Pearl Spear & New Potions§r\n\n" +
                        "§lPearl Spear§r\nCrafting:\n[ ] [Ender Pearl] [Echo Shard]\n[Popped Chorus Fruit] [Heavy Core] [Echo Shard]\n[Breeze Rod] [Popped Chorus Fruit] [ ]\n" +
                        "Base stats: Attack Damage §b8§r, Attack Speed §b1.6§r, Durability §b512§r, movement speed increased by §b114.514%§r while held (multiplicative).\n\n" +
                        "§lSkill Mechanics§r\n" +
                        "§6Right-Click: Dash Strike§r\n" +
                        "• Attempts to dash in the look direction, max distance §b10§r blocks, automatically adjusts to a safe position (non-air block).\n" +
                        "• Damages all entities along the dash path. Damage = §b8§r + Sweeping Edge level.\n" +
                        "• Cooldown: Base §c200 ticks§r (§c10 seconds§r). If enchanted with §aQuick Charge§r, the actual cooldown is calculated as:\n" +
                        "  §ecooldown = (-39.1606) / (1 - 1.193 × e^(0.06727 × Quick Charge level))§r (rounded).\n" +
                        "• Durability loss: Affected by §6Unbreaking§r. Loss probability §ep = 7.054 / (1 + 5.948 × e^(0.314996 × Unbreaking level))§r.\n\n" +
                        "§6Left-Click: Kinetic Strike§r\n" +
                        "• Deals bonus damage based on the relative velocity between the user and the target:\n" +
                        "  - On ground: §edamage = max(0, target velocity projection - player velocity projection) × 10 + 9§r\n" +
                        "  - In air: §edamage = max(0, player velocity projection - target velocity projection) × 10 + 9§r\n" +
                        "• After the attack, triggers a horizontal dash of about §b10§r blocks, damaging entities along the path with the same damage as the right-click.\n\n" +
                        "§lEnchantment Synergy§r\n" +
                        "• §6Channeling§r: On left-click, summons multiple lightning bolts near the target. Number of bolts = Channeling level (max §b8§r), with random offsets.\n" +
                        "• §aFury of Fly§r: On left-click, if the target survives, summons empowered bees to assist. Number of bees = enchantment level. Bees have Health Boost, Fire Resistance, Strength, Regeneration, Speed, and disappear after attacking. Triggers the \"Swarm Assault\" advancement.\n" +
                        "• §cNote§r: Channeling and Fury of Fly are §cmutually exclusive§r and cannot coexist on the same spear.\n\n" +
                        "§lParticle Effects§r\n" +
                        "• Dash path: Generates §eFirework§r, §dTotem of Undying§r, and §aHappy Villager§r particles. §b50§r particles along the path, accelerating near the end.\n" +
                        "• Start and end points: §bElectric Spark§r particles.\n" +
                        "• Hit: §cDamage Indicator§r particles at the target's location.\n" +
                        "• Bee summoning: Bursts of §eFlash§r and §dEnchantment§r particles.\n\n" +
                        "§lEnchantable List§r\n" +
                        "• Sword enchantments (Sharpness, Knockback, Fire Aspect, Looting, Sweeping Edge, etc.)\n" +
                        "• §6Unbreaking§r\n" +
                        "• §6Quick Charge§r (affects cooldown)\n" +
                        "• §6Channeling§r\n" +
                        "• §6Wind Burst§r\n" +
                        "• §aFury of Fly§r (exclusive)\n\n" +
                        "§lNew Potions§r\n" +
                        "§6Milk Bottle§r: Glass Bottle + Milk Bucket, clears effects. If enchanted with §6Infinity§r, the bottle is not consumed.\n" +
                        "§bMemory Potion§r: Milk Bottle + Ender Pearl + Sugar + Glowstone Dust. Teleports you to your spawn point (even if the bed is missing).\n" +
                        "§aSoultrance Potion§r: No recipe. Teleports you to your last death location (no effect if no death record).",

// Page 4: Item Enchantment Encyclopedia (including Lightning Item, Milk Bottle, Dispenser Infinity)
                "§l§n4. Item Enchantment Encyclopedia§r\n\n" +
                        "§6Channeling§r\n" +
                        "• Effect: When attacking an entity with a mace, lightning strikes the target, and the user gains Resistance §bV§r for §b3§r ticks.\n" +
                        "• Trigger: Left-click attack hits an entity.\n" +
                        "• Applicable to: Mace, Pearl Spear, Trident (mutually exclusive with Fury of Fly on Pearl Spear).\n" +
                        "• Obtaining: Enchanting table, loot, trading.\n" +
                        "• Level scaling: Number of lightning bolts = level (max §b8§r).\n\n" +

                        "§6Explode§r\n" +
                        "• Effect: Projectiles (arrows, thrown mace) explode on impact.\n" +
                        "  - Mace: Power = level × §b0.8§r.\n" +
                        "  - Arrow hitting entity: Power = level.\n" +
                        "  - Arrow hitting ground: Power = level × §b0.2§r.\n" +
                        "• Trigger: Arrow hits target or ground; thrown mace hits entity or block.\n" +
                        "• Applicable to: Bow, Crossbow, Mace (requires Throwable).\n" +
                        "• Obtaining: Treasure, trade, random loot.\n" +
                        "• Config: §6explodeDestroyBlock§r controls terrain destruction.\n" +
                        "• Mutually exclusive with Steady.\n\n" +

                        "§6No Gravity§r\n" +
                        "• Effect: Projectiles are unaffected by gravity, flying in a straight line.\n" +
                        "• Applicable to: Bow, Crossbow, Trident, Mace, Fishing Rod.\n" +
                        "• Obtaining: Treasure, random loot.\n\n" +

                        "§6No Resistance§r\n" +
                        "• Effect: Projectiles ignore air and water resistance, maintaining constant velocity.\n" +
                        "• Applicable to: Same as above.\n" +
                        "• Obtaining: Treasure, random loot.\n\n" +

                        "§6Kinetic§r\n" +
                        "• Effect: Increases projectile velocity by §b+20%§r per level (arrows, fishing bobbers).\n" +
                        "• Applicable to: Bow, Crossbow, Fishing Rod.\n" +
                        "• Obtaining: Enchanting table, loot, trading.\n" +
                        "• Mutually exclusive with Flame.\n\n" +

                        "§6Steady§r\n" +
                        "• Effect: Eliminates arrow random spread, greatly improving accuracy.\n" +
                        "• Applicable to: Bow, Crossbow.\n" +
                        "• Obtaining: Enchanting table, loot, trading.\n" +
                        "• Mutually exclusive with Explode.\n\n" +

                        "§6Tracking§r\n" +
                        "• Effect: Arrows automatically home toward nearby targets.\n" +
                        "  - Priority: monsters targeting the owner, then monsters with line of sight, then by directional alignment.\n" +
                        "  - Searches every §b3§r ticks; tracking range = §b15§r + level × §b2§r.\n" +
                        "  - Emits glowing particles during flight.\n" +
                        "• Applicable to: Bow, Crossbow.\n" +
                        "• Obtaining: Treasure, trade, random loot.\n" +
                        "• Level scaling: Tracking range increases.\n\n" +

                        "§6Loyalty§r\n" +
                        "• Effect: A thrown mace returns to the owner after hitting a target or flying for §b5§r seconds. Return speed increases with level.\n" +
                        "• Trigger: Mace hits entity/block, or after §b5§r seconds of flight.\n" +
                        "• Applicable to: Mace (requires Throwable).\n" +
                        "• Obtaining: Vanilla enchantment (for tridents), extended to mace in this mod.\n" +
                        "• Deals damage to entities in its path on return: §b3§r + level × §b1§r.\n" +
                        "• Level scaling: Return speed = §b0.5§r + level × §b0.3§r, accelerated at long distances.\n\n" +

                        "§6Multishot§r\n" +
                        "• Effect: Launches multiple projectiles.\n" +
                        "  - Wind Charge: Fires level §b+2§r additional wind charges.\n" +
                        "  - Trident: When combined with Redirect, spawns multiple tridents in a spiral pattern; otherwise, random offsets.\n" +
                        "• Applicable to: Wind Charge, Trident.\n\n" +

                        "§6Quick Charge§r\n" +
                        "• Effect: Removes the cooldown for using Wind Charges.\n" +
                        "• Applicable to: Wind Charge.\n\n" +

                        "§6Infinity§r — Applied to items\n" +
                        "• Effect: Items are not consumed when used (requires corresponding configs).\n" +
                        "  - Throwables (eggs, snowballs, ender pearls, eyes of ender): Not consumed.\n" +
                        "  - Food: Returns the item after eating.\n" +
                        "  - Buckets: Returns itself (not an empty bucket).\n" +
                        "  - Placeable blocks: Not consumed when placed (requires §6infinityBlock§r).\n" +
                        "  - TNT: Block remains after ignition (requires §6infinityTnt§r).\n" +
                        "  - Elytra: Flight ignores air resistance (stacks with Fly).\n" +
                        "  - Firework rockets: Not consumed when used (including launching and use).\n" +
                        "  - Milk Bottle: Returns itself after drinking (rather than a glass bottle).\n" +
                        "  - Lightning Item: Not consumed when thrown.\n" +
                        "• Configs: §6infinityThrowableItem§r, §6infinityFood§r, §6infinityBlock§r, §6infinityTnt§r, §6infinityDispenser§r control these.\n\n" +

                        "§6Infinity§r — Applied to Dispensers/Droppers\n" +
                        "• Effect: When a dispenser/dropper is enchanted with Infinity and §6infinityDispenser§r is set to §atrue§r, items inside are not consumed (infinite usage).\n" +
                        "• Trigger: Dispenser/dropper activates.\n\n" +

                        "§6Fly§r (Elytra exclusive)\n" +
                        "• Effect: While gliding with an elytra, holding the jump key causes continuous ascent. Ascent height per tick is configurable.\n" +
                        "• Trigger: Pressing jump while gliding.\n" +
                        "• Applicable to: Elytra (supports Curios back slots).\n" +
                        "• Obtaining: Treasure, trade, random loot.\n" +
                        "• Durability cost: Each ascent has a §b1§r durability consumption chance, reduced by Unbreaking (no Unbreaking: §a8%§r, Unbreaking I: §a7.2%§r, II: §a5.6%§r, III: §a4%§r, IV: §a2.8%§r, V: §a1.6%§r, VI: §a0.8%§r, ≥VII: §a0.4%§r).\n" +
                        "• Config: §6flyEnchantmentLiftHeightPerTick§r controls ascent speed.\n\n" +

                        "§6Double Jump§r\n" +
                        "• Effect: Allows an extra mid-air jump. Jump power = level × §b0.45§r. At level ≥ §b2§r, provides fall protection (no fall damage).\n" +
                        "• Trigger: Pressing jump in mid-air (once, resets on ground).\n" +
                        "• Particles: Flame and cloud particles under feet.\n" +
                        "• Applicable to: Boots.\n" +
                        "• Obtaining: Enchanting table, loot, trading.\n\n" +

                        "§6Bad Luck of the Sea§r (Curse)\n" +
                        "• Effect (boots): Wearer is repelled from water, pushed toward the nearest shore. Repulsion force increases with level.\n" +
                        "• Effect (items): Item entities are repelled when in water.\n" +
                        "• Applicable to: Boots, any item (as a cursed book).\n" +
                        "• Obtaining: Treasure, trade, random loot, mob equipment.\n" +
                        "• Level scaling: Repulsion speed = §b0.34§r × level (horizontal), §b0.3§r × level (vertical).\n\n" +

                        "§6No Curse§r\n" +
                        "• Effect: When an item has both No Curse and any curses, dropping and picking it up removes all curses (including No Curse itself).\n" +
                        "• Trigger: Picking up the item.\n" +
                        "• Applicable to: Any enchantable item.\n" +
                        "• Obtaining: Treasure, random loot.\n" +
                        "• Advancement: Successfully removing a curse triggers the \"Purifier\" advancement.\n\n" +

                        "§6Redirect Projectile§r\n" +
                        "• Effect: A thrown trident has very low initial velocity (§b0.03§r) and is unaffected by gravity. Left-clicking a location makes the trident fly rapidly toward that point, speed = level.\n" +
                        "• Trigger: Left-click within §b0.1§r seconds after throwing.\n" +
                        "• Applicable to: Trident.\n" +
                        "• Obtaining: Treasure, trade, random loot, mob equipment.\n" +
                        "• Special: Can be combined with Fire Aspect; ignites target on hit (duration = level × §b80§r ticks). Mutually exclusive with Looting.\n" +
                        "• Config: §6redirectTridentSetPointDistance§r controls max flight distance (default §b15§r).\n\n" +

                        "§6Throwable§r\n" +
                        "• Effect: Right-click to throw a mace. The thrown entity's speed is affected by Kinetic. Supports Channeling, Loyalty, Explode.\n" +
                        "• Applicable to: Mace.\n" +
                        "• Obtaining: Treasure, trade, random loot.\n\n" +

                        "§6Wind Burst§r\n" +
                        "• Effect: Wind charge entities can merge to form larger ones. When radius exceeds §b4§r, they begin attracting nearby entities, making them orbit and gradually approach the center.\n" +
                        "• Applicable to: Wind Charge, Pearl Spear (via tag).\n\n" +

                        "§6Fury of Fly§r\n" +
                        "• Effect: When attacking with a Pearl Spear, if the target survives, summons empowered bees to assist. Number of bees = enchantment level. Bees have Health Boost, Fire Resistance, Strength, Regeneration, Speed, and disappear after attacking.\n" +
                        "• Applicable to: Pearl Spear.\n" +
                        "• Obtaining: Treasure, trade, random loot.\n" +
                        "• Mutually exclusive with Channeling.\n" +
                        "• Advancement: Triggers \"Swarm Assault\" when used.\n\n" +

                        "§6Power§r\n" +
                        "• Effect: Right-click a block with a shovel to launch it as a falling block. Launch speed scales with level. When it lands, it damages entities (damage = block hardness × level × §b0.5§r + §b50§r).\n" +
                        "• Applicable to: Shovel.\n\n" +

                        "§6Binding Curse§r — See Block Enchantment section (hopper exclusive)\n" +
                        "• Effect: Locks a hopper, preventing item output.\n\n" +

                        "§lSpecial Items§r\n" +
                        "§6Milk Bottle§r\n" +
                        "• Clears all status effects when drunk. If enchanted with §6Infinity§r, it is not consumed (returns itself).\n" +
                        "• Crafting: Glass Bottle + Milk Bucket (shapeless).\n\n" +
                        "§6Lightning Item§r\n" +
                        "• Throwing it summons a lightning bolt at the impact point.\n" +
                        "• Obtaining: During a thunderstorm, have lightning strike a §aLightning Rod§r enchanted with §aSilk Touch§r. The item drops as an entity.\n" +
                        "• Enchantment support: Can be enchanted with §6Infinity§r to prevent consumption.\n\n" +

                        "§lVanilla Enchantment Extensions§r\n" +
                        "§6Feather Falling§r\n" +
                        "• Extra effect: When wearing boots with this enchantment, or having an item with it in the Curios \"feet\" slot, you will not trample crops.\n" +
                        "• Applicable to: Boots, any item that can be placed in the Curios feet slot.\n\n" +

                        "§lMutually Exclusive Relationships§r\n" +
                        "• Fury of Fly ↔ Channeling (Pearl Spear)\n" +
                        "• Redirect Projectile ↔ Looting (Trident)\n" +
                        "• Explode ↔ Steady (Bow/Crossbow)\n" +
                        "• Kinetic ↔ Flame (Bow/Crossbow)\n\n" +

                        "§lTreasure Enchantments§r (obtainable only from loot/trading, not enchanting table):\n" +
                        "§aFury of Fly§r, §6Redirect Projectile§r, §dFly§r, No Gravity, No Resistance, §aNo Curse§r, §bSteady§r, §bKinetic§r, §6Explode§r, Throwable, Double Jump, §aTracking§r, §cBad Luck of the Sea§r.",

// Page 5: Command Guide
                "§l§n5. Command Guide§r\n\n" +
                        "§6/block_enchant §r — Manage block enchantments (requires permission level §b2§r)\n" +
                        "• §eget <x y z>§r: Displays the enchantments of the block at the specified coordinates.\n" +
                        "• §eadd <x y z> <enchantment ID> <level>§r: Adds the specified enchantment to the block (merges with existing enchantments). Level range §b1~255§r.\n" +
                        "• §eremove <x y z>§r: Removes all enchantments from the block.\n" +
                        "Examples:\n" +
                        "  §7/block_enchant add ~ ~ ~ minecraft:sharpness 3§r\n" +
                        "  §7/block_enchant get 100 64 100§r\n" +
                        "  §7/block_enchant remove ~ ~ ~§r\n\n" +
                        "§6/random_enchant §r — Mod information and configuration (requires permission level §b2§r)\n" +
                        "• §edescription§r: Gives you this guide book.\n" +
                        "• §econfig <option> [value]§r: Views or modifies configuration.\n" +
                        "  Without arguments, displays the current value; with an argument, sets it.\n" +
                        "  Available options:\n" +
                        "  §7doRandomEnchant§r, §7alwaysEnchantable§r, §7infinityUndyingTotem§r, §7explodeDestroyBlock§r,\n" +
                        "  §7infinityBlock§r, §7infinityTnt§r, §7infinityFood§r, §7infinityThrowableItem§r,\n" +
                        "  §7isEnchantedBlockGetatable§r, §7infinityPotion§r, §7bedrockViolable§r,\n" +
                        "  §7redirectTridentSetPointDistance <integer>§r, §7flyEnchantmentLiftHeightPerTick <decimal>§r\n" +
                        "Examples:\n" +
                        "  §7/random_enchant config doRandomEnchant true§r\n" +
                        "  §7/random_enchant config redirectTridentSetPointDistance§r  (queries current value)",

// Page 6: Default Configurations (includes infinityDispenser)
                "§l§n6. Default Configurations§r\n\n" +
                        "§6doRandomEnchant§r: §cfalse§r  §7Random enchantment on attack§r\n" +
                        "§6alwaysEnchantable§r: §cfalse§r  §7All items can be enchanted on an anvil§r\n" +
                        "§6explodeDestroyBlock§r: §atrue§r  §7Whether explosion enchantments destroy terrain§r\n" +
                        "§6infinityUndyingTotem§r: §cfalse§r  §7Infinity works on totems of undying§r\n" +
                        "§6infinityBlock§r: §cfalse§r  §7Infinity works on placeable blocks§r\n" +
                        "§6infinityTnt§r: §atrue§r  §7Infinity works on TNT (block remains after ignition)§r\n" +
                        "§6infinityPotion§r: §atrue§r  §7Infinity works on potions§r\n" +
                        "§6infinityFood§r: §atrue§r  §7Infinity works on food§r\n" +
                        "§6infinityThrowableItem§r: §atrue§r  §7Infinity works on throwables§r\n" +
                        "§6infinityDispenser§r: §cfalse§r  §7Infinity works on dispensers/droppers (items not consumed)§r\n" +
                        "§6isEnchantedBlockGetatable§r: §atrue§r  §7Silk Touch can obtain enchanted blocks§r\n" +
                        "§6bedrockViolable§r: §cfalse§r  §7Whether bedrock can be affected by mod features§r\n" +
                        "§6redirectTridentSetPointDistance§r: §b15§r  §7Maximum flight distance for Redirect Projectile§r\n" +
                        "§6flyEnchantmentLiftHeightPerTick§r: §b0.05§r  §7Ascent height per tick for the Fly enchantment§r\n\n" +
                        "§lEnchant Brush Durability Consumption§r (affected by §6Unbreaking§r):\n" +
                        "• No Unbreaking: §a20%§r chance to not consume durability\n" +
                        "• Unbreaking I: §a40%§r chance to not consume durability\n" +
                        "• Unbreaking II: §a60%§r chance to not consume durability\n" +
                        "• Unbreaking ≥III: §a80%§r chance to not consume durability",

// Page 7: Tips (includes dispenser/lightning tips)
                "§l§n7. Tips & Conclusion§r\n\n" +
                        "• Ensure the Enchant Brush has enough durability before area enchanting; otherwise, the operation fails and no durability is consumed.\n" +
                        "• An unenchanted Enchant Brush can quickly clear block enchantments.\n" +
                        "• Using certain enchantments (e.g., §6Explode§r, §6Bad Luck of the Sea§r, §6Quick Charge§r on a hopper) triggers corresponding advancements.\n" +
                        "• For §6Redirect Projectile§r on a trident, left-click within §b0.1§r seconds after throwing to redirect.\n" +
                        "• The Infinity enchantment on a totem of undying may not return to the original inventory slot; keep an eye on your inventory space.\n" +
                        "• Silk Touch is your best friend for moving enchanted blocks.\n" +
                        "• Wearing boots with §6Feather Falling§r (or having it in a Curios feet slot) prevents crop trampling.\n" +
                        "• Firework rockets combined with §6Infinity§r can be used infinitely, great for elytra flying.\n" +
                        "• Dispensers/droppers can be enchanted with §6Kinetic§r, §6Steady§r, §6No Gravity§r to enhance projectiles; if §6infinityDispenser§r is enabled and the dispenser has §6Infinity§r, it can launch items infinitely.\n" +
                        "• Some enchantments (e.g., §dFly§r, §6Feather Falling§r) support Curios slots – use them to save equipment slots.\n" +
                        "• The Lightning Item can be obtained by having lightning strike a Silk-Touch Lightning Rod – a fun collectible!\n\n" +
                        "§l§oThank you for reading!§r\n\n" +
                        "We hope you enjoy the randomness of Random Enchant. For feedback, please visit the project page.\n\n" +
                        "§7—— End of Guide ——§r"
        );
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
