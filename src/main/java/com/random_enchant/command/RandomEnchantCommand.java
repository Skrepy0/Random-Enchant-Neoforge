package com.random_enchant.command;

import static net.minecraft.network.chat.Component.translatable;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.random_enchant.Config;
import com.random_enchant.RandomEnchant;
import com.random_enchant.util.CustomBookBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class RandomEnchantCommand {
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
                                                        Component message = Component.literal("doRandomEnchant: " +
                                                                                              Config.randomEnchant());
                                                        context.getSource().sendSuccess(() -> message, false);
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
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                                        boolean preStatus = Config.isAlwaysEnchantable();
                                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                                        if (preStatus == enabled) {
                                                            Component message =
                                                                    Component.literal("§a[isAlwaysEnchantable]§r")
                                                                            .append(translatable(
                                                                                    "command.random_enchant.config.unchanged"));
                                                            RandomEnchant.LOGGER.info(message.getString());
                                                            context.getSource().sendSuccess(() -> message, false);
                                                            return 1;
                                                        }
                                                        Config.setIsAlwaysEnchantable(enabled);
                                                        Component message =
                                                                Component.literal("§a[isAlwaysEnchantable]§r")
                                                                        .append(translatable(
                                                                                "command.random_enchant.config.changed"))
                                                                        .append(enabled ? "§a[true]§r" : "§c[false]§r");
                                                        RandomEnchant.LOGGER.info(message.getString());
                                                        context.getSource().sendSuccess(() -> message, false);
                                                        return 1;
                                                    })))
                                      .then(Commands.literal("infinityUndyingTotem")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                                        boolean preStatus = Config.infinityUndyingTotem();
                                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                                        if (preStatus == enabled) {
                                                            Component message =
                                                                    Component.literal("§a[infinityUndyingTotem]§r")
                                                                            .append(translatable(
                                                                                    "command.random_enchant.config.unchanged"));
                                                            RandomEnchant.LOGGER.info(message.getString());
                                                            context.getSource().sendSuccess(() -> message, false);
                                                            return 1;
                                                        }
                                                        Config.setInfinityUndyingTotem(enabled);
                                                        Component message =
                                                                Component.literal("§a[infinityUndyingTotem]§r")
                                                                        .append(translatable(
                                                                                "command.random_enchant.config.changed"))
                                                                        .append(enabled ? "§a[true]§r" : "§c[false]§r");
                                                        RandomEnchant.LOGGER.info(message.getString());
                                                        context.getSource().sendSuccess(() -> message, false);
                                                        return 1;
                                                    })))
                                      .then(Commands.literal("explodeDestroyBlock")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                                        boolean preStatus = Config.getExplodeDestroyBlock();
                                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                                        if (preStatus == enabled) {
                                                            Component message =
                                                                    Component.literal("§a[explodeDestroyBlock]§r")
                                                                            .append(translatable(
                                                                                    "command.random_enchant.config.unchanged"));
                                                            RandomEnchant.LOGGER.info(message.getString());
                                                            context.getSource().sendSuccess(() -> message, false);
                                                            return 1;
                                                        }
                                                        Config.setExplodeDestroyBlock(enabled);
                                                        Component message =
                                                                Component.literal("§a[infinityUndyingTotem]§r")
                                                                        .append(translatable(
                                                                                "command.random_enchant.config.changed"))
                                                                        .append(enabled ? "§a[true]§r" : "§c[false]§r");
                                                        RandomEnchant.LOGGER.info(message.getString());
                                                        context.getSource().sendSuccess(() -> message, false);
                                                        return 1;
                                                    })))
                                      .then(Commands.literal("infinityBlock")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                                        boolean preStatus = Config.infinityBlock();
                                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                                        if (preStatus == enabled) {
                                                            Component message =
                                                                    Component.literal("§a[infinityBlock]§r")
                                                                            .append(translatable(
                                                                                    "command.random_enchant.config.unchanged"));
                                                            RandomEnchant.LOGGER.info(message.getString());
                                                            context.getSource().sendSuccess(() -> message, false);
                                                            return 1;
                                                        }
                                                        Config.setInfinityBlock(enabled);
                                                        Component message =
                                                                Component.literal("§a[infinityBlock]§r")
                                                                        .append(translatable(
                                                                                "command.random_enchant.config.changed"))
                                                                        .append(enabled ? "§a[true]§r" : "§c[false]§r");
                                                        RandomEnchant.LOGGER.info(message.getString());
                                                        context.getSource().sendSuccess(() -> message, false);
                                                        return 1;
                                                    })))
                                      .then(Commands.literal("infinityTnt")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                                        boolean preStatus = Config.infinityTnt();
                                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                                        if (preStatus == enabled) {
                                                            Component message =
                                                                    Component.literal("§a[infinityTnt]§r")
                                                                            .append(translatable(
                                                                                    "command.random_enchant.config.unchanged"));
                                                            RandomEnchant.LOGGER.info(message.getString());
                                                            context.getSource().sendSuccess(() -> message, false);
                                                            return 1;
                                                        }
                                                        Config.setInfinityTnt(enabled);
                                                        Component message =
                                                                Component.literal("§a[infinityTnt]§r")
                                                                        .append(translatable(
                                                                                "command.random_enchant.config.changed"))
                                                                        .append(enabled ? "§a[true]§r" : "§c[false]§r");
                                                        RandomEnchant.LOGGER.info(message.getString());
                                                        context.getSource().sendSuccess(() -> message, false);
                                                        return 1;
                                                    })))
                                      .then(Commands.literal("infinityFood")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                                        boolean preStatus = Config.infinityFood();
                                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                                        if (preStatus == enabled) {
                                                            Component message =
                                                                    Component.literal("§a[infinityFood]§r")
                                                                            .append(translatable(
                                                                                    "command.random_enchant.config.unchanged"));
                                                            RandomEnchant.LOGGER.info(message.getString());
                                                            context.getSource().sendSuccess(() -> message, false);
                                                            return 1;
                                                        }
                                                        Config.setInfinityFood(enabled);
                                                        Component message =
                                                                Component.literal("§a[infinityFood]§r")
                                                                        .append(translatable(
                                                                                "command.random_enchant.config.changed"))
                                                                        .append(enabled ? "§a[true]§r" : "§c[false]§r");
                                                        RandomEnchant.LOGGER.info(message.getString());
                                                        context.getSource().sendSuccess(() -> message, false);
                                                        return 1;
                                                    })))
                                      .then(Commands.literal("infinityThrowableItem")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                                        boolean preStatus = Config.infinityThrowableItem();
                                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                                        if (preStatus == enabled) {
                                                            Component message =
                                                                    Component.literal("§a[infinityThrowableItem]§r")
                                                                            .append(translatable(
                                                                                    "command.random_enchant.config.unchanged"));
                                                            RandomEnchant.LOGGER.info(message.getString());
                                                            context.getSource().sendSuccess(() -> message, false);
                                                            return 1;
                                                        }
                                                        Config.setInfinityThrowableItem(enabled);
                                                        Component message =
                                                                Component.literal("§a[infinityFood]§r")
                                                                        .append(translatable(
                                                                                "command.random_enchant.config.changed"))
                                                                        .append(enabled ? "§a[true]§r" : "§c[false]§r");
                                                        RandomEnchant.LOGGER.info(message.getString());
                                                        context.getSource().sendSuccess(() -> message, false);
                                                        return 1;
                                                    })))
                                      .then(Commands.literal("isEnchantedBlockGetatable")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                                        boolean preStatus = Config.isEnchantedBlockGetatable();
                                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                                        if (preStatus == enabled) {
                                                            Component message =
                                                                    Component.literal("§a[isEnchantedBlockGetatable]§r")
                                                                            .append(translatable(
                                                                                    "command.random_enchant.config.unchanged"));
                                                            RandomEnchant.LOGGER.info(message.getString());
                                                            context.getSource().sendSuccess(() -> message, false);
                                                            return 1;
                                                        }
                                                        Config.setIsEnchantedBlockGetatable(enabled);
                                                        Component message =
                                                                Component.literal("§a[isEnchantedBlockGetatable]§r")
                                                                        .append(translatable(
                                                                                "command.random_enchant.config.changed"))
                                                                        .append(enabled ? "§a[true]§r" : "§c[false]§r");
                                                        RandomEnchant.LOGGER.info(message.getString());
                                                        context.getSource().sendSuccess(() -> message, false);
                                                        return 1;
                                                    })))
                                      .then(Commands.literal("infinityPotion")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                                        boolean preStatus = Config.infinityPotion();
                                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                                        if (preStatus == enabled) {
                                                            Component message =
                                                                    Component.literal("§a[infinityPotion]§r")
                                                                            .append(translatable(
                                                                                    "command.random_enchant.config.unchanged"));
                                                            RandomEnchant.LOGGER.info(message.getString());
                                                            context.getSource().sendSuccess(() -> message, false);
                                                            return 1;
                                                        }
                                                        Config.setInfinityPotion(enabled);
                                                        Component message =
                                                                Component.literal("§a[infinityPotion]§r")
                                                                        .append(translatable(
                                                                                "command.random_enchant.config.changed"))
                                                                        .append(enabled ? "§a[true]§r" : "§c[false]§r");
                                                        RandomEnchant.LOGGER.info(message.getString());
                                                        context.getSource().sendSuccess(() -> message, false);
                                                        return 1;
                                                    })))
                                      .then(Commands.literal("bedrockViolable")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool()).executes(context -> {
                                                        boolean preStatus = Config.getBedrockViolable();
                                                        boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                                        if (preStatus == enabled) {
                                                            Component message =
                                                                    Component.literal("§a[bedrockViolable]§r")
                                                                            .append(translatable(
                                                                                    "command.random_enchant.config.unchanged"));
                                                            RandomEnchant.LOGGER.info(message.getString());
                                                            context.getSource().sendSuccess(() -> message, false);
                                                            return 1;
                                                        }
                                                        Config.setBedrockViolable(enabled);
                                                        Component message =
                                                                Component.literal("§a[bedrockViolable]§r")
                                                                        .append(translatable(
                                                                                "command.random_enchant.config.changed"))
                                                                        .append(enabled ? "§a[true]§r" : "§c[false]§r");
                                                        RandomEnchant.LOGGER.info(message.getString());
                                                        context.getSource().sendSuccess(() -> message, false);
                                                        return 1;
                                                    })))
                                      .then(Commands.literal("redirectTridentSetPointDistance")
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
