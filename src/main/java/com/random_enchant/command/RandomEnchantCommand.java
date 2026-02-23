package com.random_enchant.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.random_enchant.Config;
import com.random_enchant.RandomEnchant;
import com.random_enchant.util.CustomBookBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.network.chat.Component.translatable;

public class RandomEnchantCommand {
    private static boolean boolConfig(CommandContext<CommandSourceStack> context, boolean preStatus, String configId) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        if (preStatus == enabled) {
            Component message =
                    Component.literal("§a[" + configId + "]§r")
                            .append(translatable(
                                    "command.random_enchant.config.unchanged"));
            RandomEnchant.LOGGER.info(message.getString());
            context.getSource().sendSuccess(() -> message, false);
            return false;
        }
        Component message =
                Component.literal("§a[" + configId + "]§r")
                        .append(translatable(
                                "command.random_enchant.config.changed"))
                        .append(enabled ? "§a[true]§r" : "§c[false]§r");
        RandomEnchant.LOGGER.info(message.getString());
        context.getSource().sendSuccess(() -> message, false);
        return true;
    }

    private static void showValue(CommandContext<CommandSourceStack> context, String configId, String value) {
        Component message = Component.literal(translatable("command.random_enchant.value").getString().formatted(configId) +
                value);
        context.getSource().sendSuccess(() -> message, false);
    }

    private static String getBoolString(boolean value) {
        return value ? "§atrue§r" : "§cfalse§r";
    }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("random_enchant")
                        .then(Commands.literal("description").executes(context -> {
                            List<String> pages = new ArrayList<>();
                            for (int i = 1; i < 33; i++) {
                                pages.add(translatable("item.random_enchant.guide.page." + i).getString());
                            }
                            ItemStack book = CustomBookBuilder.createBook(
                                    translatable("item.random_enchant.guide.title").getString(), "§kuniverse itself",
                                    pages, true);
                            context.getSource().getPlayer().getInventory().add(book);
                            return 1;
                        }))
                        .then(Commands.literal("config")
                                .requires(source -> source.hasPermission(2))
                                .then(Commands.literal("doRandomEnchant")
                                        .executes(context -> {
                                            showValue(context, "doRandomEnchant", getBoolString(Config.randomEnchant()));
                                            return 1;
                                        })
                                        .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                            boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                            Config.setRandomEnchant(enabled);
                                            Component message =
                                                    enabled ? translatable(
                                                            "command.random_enchant.randomEnchant.enable")
                                                            : translatable(
                                                            "command.random_enchant.randomEnchant.disable");

                                            RandomEnchant.LOGGER.info(message.getString());
                                            context.getSource().sendSuccess(() -> message, false);

                                            return 1;
                                        })))
                                .then(Commands.literal("alwaysEnchantable")
                                        .executes(context -> {
                                            showValue(context, "alwaysEnchantable", getBoolString(Config.isAlwaysEnchantable()));
                                            return 1;
                                        })
                                        .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                            boolean preStatus = Config.isAlwaysEnchantable();
                                            if (boolConfig(context, preStatus, "isAlwaysEnchantable")) {
                                                Config.setIsAlwaysEnchantable(!preStatus);
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("infinityUndyingTotem")
                                        .executes(context -> {
                                            showValue(context, "infinityUndyingTotem", getBoolString(Config.infinityUndyingTotem()));
                                            return 1;
                                        })
                                        .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                            boolean preStatus = Config.infinityUndyingTotem();
                                            if (boolConfig(context, preStatus, "infinityUndyingTotem")) {
                                                Config.setInfinityUndyingTotem(!preStatus);
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("explodeDestroyBlock")
                                        .executes(context -> {
                                            showValue(context, "explodeDestroyBlock", getBoolString(Config.getExplodeDestroyBlock()));
                                            return 1;
                                        })
                                        .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                            boolean preStatus = Config.getExplodeDestroyBlock();
                                            if (boolConfig(context, preStatus, "explodeDestroyBlock")) {
                                                Config.setExplodeDestroyBlock(!preStatus);
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("infinityBlock")
                                        .executes(context -> {
                                            showValue(context, "infinityBlock", getBoolString(Config.infinityBlock()));
                                            return 1;
                                        })
                                        .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                            boolean preStatus = Config.infinityBlock();
                                            if (boolConfig(context, preStatus, "infinityBlock")) {
                                                Config.setInfinityBlock(!preStatus);
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("infinityTnt")
                                        .executes(context -> {
                                            showValue(context, "infinityTnt", getBoolString(Config.infinityTnt()));
                                            return 1;
                                        })
                                        .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                            boolean preStatus = Config.infinityTnt();
                                            if (boolConfig(context, preStatus, "infinityTnt")) {
                                                Config.setInfinityTnt(!preStatus);
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("infinityFood")
                                        .executes(context -> {
                                            showValue(context, "infinityFood", getBoolString(Config.infinityFood()));
                                            return 1;
                                        })
                                        .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                            boolean preStatus = Config.infinityFood();
                                            if (boolConfig(context, preStatus, "infinityFood")) {
                                                Config.setInfinityFood(!preStatus);
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("infinityThrowableItem")
                                        .executes(context -> {
                                            showValue(context, "infinityThrowableItem", getBoolString(Config.infinityThrowableItem()));
                                            return 1;
                                        })
                                        .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                            boolean preStatus = Config.infinityThrowableItem();
                                            if (boolConfig(context, preStatus, "infinityThrowableItem")) {
                                                Config.setInfinityThrowableItem(!preStatus);
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("isEnchantedBlockGetatable")
                                        .executes(context -> {
                                            showValue(context, "isEnchantedBlockGetatable", getBoolString(Config.isEnchantedBlockGetatable()));
                                            return 1;
                                        })
                                        .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                            boolean preStatus = Config.isEnchantedBlockGetatable();
                                            if (boolConfig(context, preStatus, "isEnchantedBlockGetatable")) {
                                                Config.setIsEnchantedBlockGetatable(!preStatus);
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("infinityPotion")
                                        .executes(context -> {
                                            showValue(context, "infinityPotion", getBoolString(Config.infinityPotion()));
                                            return 1;
                                        })
                                        .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                            boolean preStatus = Config.infinityPotion();
                                            if (boolConfig(context, preStatus, "infinityPotion")) {
                                                Config.setInfinityPotion(!preStatus);
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("bedrockViolable")
                                        .executes(context -> {
                                            showValue(context, "bedrockViolable", getBoolString(Config.getBedrockViolable()));
                                            return 1;
                                        })
                                        .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                            boolean preStatus = Config.getBedrockViolable();
                                            if (boolConfig(context, preStatus, "bedrockViolable")) {
                                                Config.setBedrockViolable(!preStatus);
                                            }
                                            return 1;
                                        })))
                                .then(Commands.literal("redirectTridentSetPointDistance")
                                        .executes(context -> {
                                            showValue(context, "redirectTridentSetPointDistance", "§b" + Config.getRedirectTridentSetPointDistance());
                                            return 1;
                                        })
                                        .then(Commands.argument("value", IntegerArgumentType.integer(
                                                        1, Integer.MAX_VALUE))
                                                .executes(context -> {
                                                    int preValue =
                                                            Config.getRedirectTridentSetPointDistance();
                                                    int value = IntegerArgumentType.getInteger(
                                                            context, "value");
                                                    if (preValue == value) {
                                                        Component message =
                                                                Component
                                                                        .literal(
                                                                                "§a[redirectTridentSetPointDistance]§r")
                                                                        .append(translatable(
                                                                                "command.random_enchant.config.unchanged"));
                                                        RandomEnchant.LOGGER.info(
                                                                message.getString());
                                                        context.getSource().sendSuccess(
                                                                () -> message, false);
                                                        return 1;
                                                    }
                                                    Config.setRedirectTridentSetPointDistance(value);
                                                    Component message =
                                                            Component
                                                                    .literal(
                                                                            "§a[redirectTridentSetPointDistance]§r")
                                                                    .append(translatable(
                                                                            "command.random_enchant.config.changed"))
                                                                    .append(" §6" + value);
                                                    RandomEnchant.LOGGER.info(message.getString());
                                                    context.getSource().sendSuccess(
                                                            () -> message, false);
                                                    return 1;
                                                })))
                                .then(Commands.literal("flyEnchantmentLiftHeightPerTick")
                                        .executes(context -> {
                                            showValue(context, "flyEnchantmentLiftHeightPerTick", "§b" + Config.getFlyEnchantmentLiftHeightPerTick());
                                            return 1;
                                        })
                                        .then(Commands.argument("value",
                                                        DoubleArgumentType.doubleArg(0.00000001, 1))
                                                .executes(context -> {
                                                    double preValue =
                                                            Config.getFlyEnchantmentLiftHeightPerTick();
                                                    double value = DoubleArgumentType.getDouble(
                                                            context, "value");
                                                    if (preValue == value) {
                                                        Component message =
                                                                Component
                                                                        .literal(
                                                                                "§a[flyEnchantmentLiftHeightPerTick]§r")
                                                                        .append(translatable(
                                                                                "command.random_enchant.config.unchanged"));
                                                        RandomEnchant.LOGGER.info(
                                                                message.getString());
                                                        context.getSource().sendSuccess(
                                                                () -> message, false);
                                                        return 1;
                                                    }
                                                    Config.setFlyEnchantmentLiftHeightPerTick(value);
                                                    Component message =
                                                            Component
                                                                    .literal(
                                                                            "§a[flyEnchantmentLiftHeightPerTick]§r")
                                                                    .append(translatable(
                                                                            "command.random_enchant.config.changed"))
                                                                    .append(" §6" + value);
                                                    RandomEnchant.LOGGER.info(message.getString());
                                                    context.getSource().sendSuccess(
                                                            () -> message, false);
                                                    return 1;
                                                })))));
    }
}
