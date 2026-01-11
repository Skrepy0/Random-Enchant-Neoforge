package com.random_enchant.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.random_enchant.Config;
import com.random_enchant.RandomEnchant;
import java.util.ArrayList;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class RandomEnchantCommand {

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("random_enchant")
                        .then(Commands.literal("description").executes(context -> {
                            Component modName =
                                    Component.literal("§6[§nRandom Enchant§r§6]§r")
                                            .withStyle(
                                                    style
                                                    -> style.withUnderlined(true)
                                                               .withClickEvent(new ClickEvent(
                                                                       ClickEvent.Action.OPEN_URL,
                                                                       "https://github.com/Skrepy0/"
                                                                               + "Random-Enchant-Neoforge"))
                                                               .withHoverEvent(new HoverEvent(
                                                                       HoverEvent.Action.SHOW_TEXT,
                                                                       Component.literal("点击打开§6Random Enchant§r "
                                                                                         + "的Github仓库"))));
                            Component description_1 = Component.literal("  本mod").append(modName).append(
                                    "由§bSkrepy2233§r制作，以下是几点说明");
                            Component description_2 = Component.literal("1.可以使用/§arandom_enchant doRandomEnchant "
                                                                        + "§b<true/false>§r 进行配置，默认是关闭");
                            Component description_3 = Component.literal(
                                    "2.§adoRandomEnchant§r开启后,"
                                    + "玩家击打有生命实体后会对玩家主手物品进行随机附魔（等级也是随机）");
                            Component description_4 =
                                    Component.literal("3.已经启用本mod自带的材质包（修复§b附魔等级的罗马数字显示§r）");
                            Component description_5 = Component.literal("4.附魔随机的范围是§d所有已附魔§r");
                            Component description_6 =
                                    Component.literal("5.本mod对原版附魔添加了一些效果，如附魔有§a[无限]"
                                                      + "§r的食物使用后数量不会减少，方块、不死图腾亦同");
                            Component description_7 = Component.literal(
                                    "最后，添加的附魔与对应的物品：\n "
                                    + "§a[无限]§r投掷类物品，如鸡蛋、末影珍珠、药水（饮用除外）、食物（蛋糕除外）等;"
                                    + "各种方块、不死图腾\n §a[力量]§r 火焰弹、铲子、粘液球");
                            Component description = Component.literal("§c最后§r:按§d[T]§r查看全部");
                            ArrayList<Component> messageList = new ArrayList<>();
                            messageList.add(description_1);
                            messageList.add(description_2);
                            messageList.add(description_3);
                            messageList.add(description_4);
                            messageList.add(description_5);
                            messageList.add(description_6);
                            messageList.add(description_7);
                            messageList.add(description);
                            for (int i = 0; i < 8; i++) {
                                RandomEnchant.LOGGER.info(messageList.get(i).getString());
                                // 反馈给玩家
                                int finalI = i;
                                context.getSource().sendSuccess(() -> messageList.get(finalI), true);
                            }
                            return 1;
                        }))
                        .then(Commands.literal("config")
                                      .requires(source -> source.hasPermission(2))
                                      .then(Commands.literal("doRandomEnchant")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool())
                                                                  .executes(context -> {
                                                                      boolean enabled = BoolArgumentType.getBool(
                                                                              context, "enabled");
                                                                      Config.setRandomEnchant(enabled);
                                                                      Component message =
                                                                              enabled ? Component.translatable(
                                                                                                "command.random_"
                                                                                                + "enchant."
                                                                                                +
                                                                                                "randomEnchant.enable")
                                                                                      : Component.translatable(
                                                                                                "command.random_"
                                                                                                + "enchant."
                                                                                                + "randomEnchant."
                                                                                                + "disable");

                                                                      RandomEnchant.LOGGER.info(message.getString());
                                                                      context.getSource().sendSuccess(
                                                                              () -> message, false);

                                                                      return 1;
                                                                  })))
                                      .then(Commands.literal("alwaysEnchantable")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool())
                                                                  .executes(context -> {
                                                                      boolean preStatus = Config.isAlwaysEnchantable();
                                                                      boolean enabled = BoolArgumentType.getBool(
                                                                              context, "enabled");
                                                                      if (preStatus == enabled) {
                                                                          Component message =
                                                                                  Component
                                                                                          .literal("§a["
                                                                                                   + "isAlwaysEnchantab"
                                                                                                   + "le]§r")
                                                                                          .append(Component
                                                                                                          .translatable(
                                                                                                                  "co"
                                                                                                                  + "mm"
                                                                                                                  + "an"
                                                                                                                  + "d."
                                                                                                                  + "ra"
                                                                                                                  + "nd"
                                                                                                                  + "om"
                                                                                                                  + "_e"
                                                                                                                  + "nc"
                                                                                                                  + "ha"
                                                                                                                  + "nt"
                                                                                                                  + ".c"
                                                                                                                  + "on"
                                                                                                                  + "fi"
                                                                                                                  + "g."
                                                                                                                  + "un"
                                                                                                                  + "ch"
                                                                                                                  + "an"
                                                                                                                  + "ge"
                                                                                                                  +
                                                                                                                  "d"));
                                                                          RandomEnchant.LOGGER.info(
                                                                                  message.getString());
                                                                          context.getSource().sendSuccess(
                                                                                  () -> message, false);
                                                                          return 1;
                                                                      }
                                                                      Config.setIsAlwaysEnchantable(enabled);
                                                                      Component message =
                                                                              Component
                                                                                      .literal("§a["
                                                                                               +
                                                                                               "isAlwaysEnchantable]§r")
                                                                                      .append(Component.translatable(
                                                                                              "command.random_"
                                                                                              +
                                                                                              "enchant.config.changed"))
                                                                                      .append(enabled ? "§a[true]§r"
                                                                                                      : "§c[false]§r");
                                                                      RandomEnchant.LOGGER.info(message.getString());
                                                                      context.getSource().sendSuccess(
                                                                              () -> message, false);
                                                                      return 1;
                                                                  })))
                                      .then(Commands.literal("infinityUndyingTotem")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool())
                                                                  .executes(context -> {
                                                                      boolean preStatus = Config.infinityUndyingTotem();
                                                                      boolean enabled = BoolArgumentType.getBool(
                                                                              context, "enabled");
                                                                      if (preStatus == enabled) {
                                                                          Component message =
                                                                                  Component
                                                                                          .literal("§a["
                                                                                                   + "infinityUndyingTo"
                                                                                                   + "tem]§r")
                                                                                          .append(Component
                                                                                                          .translatable(
                                                                                                                  "co"
                                                                                                                  + "mm"
                                                                                                                  + "an"
                                                                                                                  + "d."
                                                                                                                  + "ra"
                                                                                                                  + "nd"
                                                                                                                  + "om"
                                                                                                                  + "_e"
                                                                                                                  + "nc"
                                                                                                                  + "ha"
                                                                                                                  + "nt"
                                                                                                                  + ".c"
                                                                                                                  + "on"
                                                                                                                  + "fi"
                                                                                                                  + "g."
                                                                                                                  + "un"
                                                                                                                  + "ch"
                                                                                                                  + "an"
                                                                                                                  + "ge"
                                                                                                                  +
                                                                                                                  "d"));
                                                                          RandomEnchant.LOGGER.info(
                                                                                  message.getString());
                                                                          context.getSource().sendSuccess(
                                                                                  () -> message, false);
                                                                          return 1;
                                                                      }
                                                                      Config.setInfinityUndyingTotem(enabled);
                                                                      Component message =
                                                                              Component
                                                                                      .literal(
                                                                                              "§a["
                                                                                              +
                                                                                              "infinityUndyingTotem]§r")
                                                                                      .append(Component.translatable(
                                                                                              "command.random_"
                                                                                              +
                                                                                              "enchant.config.changed"))
                                                                                      .append(enabled ? "§a[true]§r"
                                                                                                      : "§c[false]§r");
                                                                      RandomEnchant.LOGGER.info(message.getString());
                                                                      context.getSource().sendSuccess(
                                                                              () -> message, false);
                                                                      return 1;
                                                                  })))
                                      .then(Commands.literal("infinityBlock")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool())
                                                                  .executes(context -> {
                                                                      boolean preStatus = Config.infinityBlock();
                                                                      boolean enabled = BoolArgumentType.getBool(
                                                                              context, "enabled");
                                                                      if (preStatus == enabled) {
                                                                          Component message =
                                                                                  Component
                                                                                          .literal(
                                                                                                  "§a[infinityBlock]§r")
                                                                                          .append(Component
                                                                                                          .translatable(
                                                                                                                  "co"
                                                                                                                  + "mm"
                                                                                                                  + "an"
                                                                                                                  + "d."
                                                                                                                  + "ra"
                                                                                                                  + "nd"
                                                                                                                  + "om"
                                                                                                                  + "_e"
                                                                                                                  + "nc"
                                                                                                                  + "ha"
                                                                                                                  + "nt"
                                                                                                                  + ".c"
                                                                                                                  + "on"
                                                                                                                  + "fi"
                                                                                                                  + "g."
                                                                                                                  + "un"
                                                                                                                  + "ch"
                                                                                                                  + "an"
                                                                                                                  + "ge"
                                                                                                                  +
                                                                                                                  "d"));
                                                                          RandomEnchant.LOGGER.info(
                                                                                  message.getString());
                                                                          context.getSource().sendSuccess(
                                                                                  () -> message, false);
                                                                          return 1;
                                                                      }
                                                                      Config.setInfinityBlock(enabled);
                                                                      Component message =
                                                                              Component.literal("§a[infinityBlock]§r")
                                                                                      .append(Component.translatable(
                                                                                              "command.random_"
                                                                                              +
                                                                                              "enchant.config.changed"))
                                                                                      .append(enabled ? "§a[true]§r"
                                                                                                      : "§c[false]§r");
                                                                      RandomEnchant.LOGGER.info(message.getString());
                                                                      context.getSource().sendSuccess(
                                                                              () -> message, false);
                                                                      return 1;
                                                                  })))
                                      .then(Commands.literal("infinityTnt")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool())
                                                                  .executes(context -> {
                                                                      boolean preStatus = Config.infinityTnt();
                                                                      boolean enabled = BoolArgumentType.getBool(
                                                                              context, "enabled");
                                                                      if (preStatus == enabled) {
                                                                          Component message =
                                                                                  Component.literal("§a[infinityTnt]§r")
                                                                                          .append(Component
                                                                                                          .translatable(
                                                                                                                  "co"
                                                                                                                  + "mm"
                                                                                                                  + "an"
                                                                                                                  + "d."
                                                                                                                  + "ra"
                                                                                                                  + "nd"
                                                                                                                  + "om"
                                                                                                                  + "_e"
                                                                                                                  + "nc"
                                                                                                                  + "ha"
                                                                                                                  + "nt"
                                                                                                                  + ".c"
                                                                                                                  + "on"
                                                                                                                  + "fi"
                                                                                                                  + "g."
                                                                                                                  + "un"
                                                                                                                  + "ch"
                                                                                                                  + "an"
                                                                                                                  + "ge"
                                                                                                                  +
                                                                                                                  "d"));
                                                                          RandomEnchant.LOGGER.info(
                                                                                  message.getString());
                                                                          context.getSource().sendSuccess(
                                                                                  () -> message, false);
                                                                          return 1;
                                                                      }
                                                                      Config.setInfinityTnt(enabled);
                                                                      Component message =
                                                                              Component.literal("§a[infinityTnt]§r")
                                                                                      .append(Component.translatable(
                                                                                              "command.random_"
                                                                                              +
                                                                                              "enchant.config.changed"))
                                                                                      .append(enabled ? "§a[true]§r"
                                                                                                      : "§c[false]§r");
                                                                      RandomEnchant.LOGGER.info(message.getString());
                                                                      context.getSource().sendSuccess(
                                                                              () -> message, false);
                                                                      return 1;
                                                                  })))
                                      .then(Commands.literal("infinityFood")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool())
                                                                  .executes(context -> {
                                                                      boolean preStatus = Config.infinityFood();
                                                                      boolean enabled = BoolArgumentType.getBool(
                                                                              context, "enabled");
                                                                      if (preStatus == enabled) {
                                                                          Component message =
                                                                                  Component
                                                                                          .literal("§a[infinityFood]§r")
                                                                                          .append(Component
                                                                                                          .translatable(
                                                                                                                  "co"
                                                                                                                  + "mm"
                                                                                                                  + "an"
                                                                                                                  + "d."
                                                                                                                  + "ra"
                                                                                                                  + "nd"
                                                                                                                  + "om"
                                                                                                                  + "_e"
                                                                                                                  + "nc"
                                                                                                                  + "ha"
                                                                                                                  + "nt"
                                                                                                                  + ".c"
                                                                                                                  + "on"
                                                                                                                  + "fi"
                                                                                                                  + "g."
                                                                                                                  + "un"
                                                                                                                  + "ch"
                                                                                                                  + "an"
                                                                                                                  + "ge"
                                                                                                                  +
                                                                                                                  "d"));
                                                                          RandomEnchant.LOGGER.info(
                                                                                  message.getString());
                                                                          context.getSource().sendSuccess(
                                                                                  () -> message, false);
                                                                          return 1;
                                                                      }
                                                                      Config.setInfinityFood(enabled);
                                                                      Component message =
                                                                              Component.literal("§a[infinityFood]§r")
                                                                                      .append(Component.translatable(
                                                                                              "command.random_"
                                                                                              +
                                                                                              "enchant.config.changed"))
                                                                                      .append(enabled ? "§a[true]§r"
                                                                                                      : "§c[false]§r");
                                                                      RandomEnchant.LOGGER.info(message.getString());
                                                                      context.getSource().sendSuccess(
                                                                              () -> message, false);
                                                                      return 1;
                                                                  })))
                                      .then(Commands.literal("infinityThrowableItem")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool())
                                                                  .executes(context -> {
                                                                      boolean preStatus =
                                                                              Config.infinityThrowableItem();
                                                                      boolean enabled = BoolArgumentType.getBool(
                                                                              context, "enabled");
                                                                      if (preStatus == enabled) {
                                                                          Component message =
                                                                                  Component
                                                                                          .literal("§a["
                                                                                                   + "infinityThrowable"
                                                                                                   + "Item]§r")
                                                                                          .append(Component
                                                                                                          .translatable(
                                                                                                                  "co"
                                                                                                                  + "mm"
                                                                                                                  + "an"
                                                                                                                  + "d."
                                                                                                                  + "ra"
                                                                                                                  + "nd"
                                                                                                                  + "om"
                                                                                                                  + "_e"
                                                                                                                  + "nc"
                                                                                                                  + "ha"
                                                                                                                  + "nt"
                                                                                                                  + ".c"
                                                                                                                  + "on"
                                                                                                                  + "fi"
                                                                                                                  + "g."
                                                                                                                  + "un"
                                                                                                                  + "ch"
                                                                                                                  + "an"
                                                                                                                  + "ge"
                                                                                                                  +
                                                                                                                  "d"));
                                                                          RandomEnchant.LOGGER.info(
                                                                                  message.getString());
                                                                          context.getSource().sendSuccess(
                                                                                  () -> message, false);
                                                                          return 1;
                                                                      }
                                                                      Config.setInfinityThrowableItem(enabled);
                                                                      Component message =
                                                                              Component.literal("§a[infinityFood]§r")
                                                                                      .append(Component.translatable(
                                                                                              "command.random_"
                                                                                              +
                                                                                              "enchant.config.changed"))
                                                                                      .append(enabled ? "§a[true]§r"
                                                                                                      : "§c[false]§r");
                                                                      RandomEnchant.LOGGER.info(message.getString());
                                                                      context.getSource().sendSuccess(
                                                                              () -> message, false);
                                                                      return 1;
                                                                  })))
                                      .then(Commands.literal("isEnchantedBlockGetatable")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool())
                                                                  .executes(context -> {
                                                                      boolean preStatus =
                                                                              Config.isEnchantedBlockGetatable();
                                                                      boolean enabled = BoolArgumentType.getBool(
                                                                              context, "enabled");
                                                                      if (preStatus == enabled) {
                                                                          Component message =
                                                                                  Component
                                                                                          .literal("§a["
                                                                                                   + "isEnchantedBlockG"
                                                                                                   + "etatable]§r")
                                                                                          .append(Component
                                                                                                          .translatable(
                                                                                                                  "co"
                                                                                                                  + "mm"
                                                                                                                  + "an"
                                                                                                                  + "d."
                                                                                                                  + "ra"
                                                                                                                  + "nd"
                                                                                                                  + "om"
                                                                                                                  + "_e"
                                                                                                                  + "nc"
                                                                                                                  + "ha"
                                                                                                                  + "nt"
                                                                                                                  + ".c"
                                                                                                                  + "on"
                                                                                                                  + "fi"
                                                                                                                  + "g."
                                                                                                                  + "un"
                                                                                                                  + "ch"
                                                                                                                  + "an"
                                                                                                                  + "ge"
                                                                                                                  +
                                                                                                                  "d"));
                                                                          RandomEnchant.LOGGER.info(
                                                                                  message.getString());
                                                                          context.getSource().sendSuccess(
                                                                                  () -> message, false);
                                                                          return 1;
                                                                      }
                                                                      Config.setIsEnchantedBlockGetatable(enabled);
                                                                      Component message =
                                                                              Component
                                                                                      .literal("§a["
                                                                                               + "isEnchantedBlockGetat"
                                                                                               + "able]§r")
                                                                                      .append(Component.translatable(
                                                                                              "command.random_"
                                                                                              +
                                                                                              "enchant.config.changed"))
                                                                                      .append(enabled ? "§a[true]§r"
                                                                                                      : "§c[false]§r");
                                                                      RandomEnchant.LOGGER.info(message.getString());
                                                                      context.getSource().sendSuccess(
                                                                              () -> message, false);
                                                                      return 1;
                                                                  })))
                                      .then(Commands.literal("infinityPotion")
                                                    .then(Commands.argument("enabled", BoolArgumentType.bool())
                                                                  .executes(context -> {
                                                                      boolean preStatus = Config.infinityPotion();
                                                                      boolean enabled = BoolArgumentType.getBool(
                                                                              context, "enabled");
                                                                      if (preStatus == enabled) {
                                                                          Component message =
                                                                                  Component
                                                                                          .literal("§a["
                                                                                                   +
                                                                                                   "infinityPotion]§r")
                                                                                          .append(Component
                                                                                                          .translatable(
                                                                                                                  "co"
                                                                                                                  + "mm"
                                                                                                                  + "an"
                                                                                                                  + "d."
                                                                                                                  + "ra"
                                                                                                                  + "nd"
                                                                                                                  + "om"
                                                                                                                  + "_e"
                                                                                                                  + "nc"
                                                                                                                  + "ha"
                                                                                                                  + "nt"
                                                                                                                  + ".c"
                                                                                                                  + "on"
                                                                                                                  + "fi"
                                                                                                                  + "g."
                                                                                                                  + "un"
                                                                                                                  + "ch"
                                                                                                                  + "an"
                                                                                                                  + "ge"
                                                                                                                  +
                                                                                                                  "d"));
                                                                          RandomEnchant.LOGGER.info(
                                                                                  message.getString());
                                                                          context.getSource().sendSuccess(
                                                                                  () -> message, false);
                                                                          return 1;
                                                                      }
                                                                      Config.setInfinityPotion(enabled);
                                                                      Component message =
                                                                              Component.literal("§a[infinityPotion]§r")
                                                                                      .append(Component.translatable(
                                                                                              "command.random_"
                                                                                              +
                                                                                              "enchant.config.changed"))
                                                                                      .append(enabled ? "§a[true]§r"
                                                                                                      : "§c[false]§r");
                                                                      RandomEnchant.LOGGER.info(message.getString());
                                                                      context.getSource().sendSuccess(
                                                                              () -> message, false);
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
                                                                                          .literal("§a["
                                                                                                   + "redirectTridentSe"
                                                                                                   +
                                                                                                   "tPointDistance]§r")
                                                                                          .append(Component
                                                                                                          .translatable(
                                                                                                                  "co"
                                                                                                                  + "mm"
                                                                                                                  + "an"
                                                                                                                  + "d."
                                                                                                                  + "ra"
                                                                                                                  + "nd"
                                                                                                                  + "om"
                                                                                                                  + "_e"
                                                                                                                  + "nc"
                                                                                                                  + "ha"
                                                                                                                  + "nt"
                                                                                                                  + ".c"
                                                                                                                  + "on"
                                                                                                                  + "fi"
                                                                                                                  + "g."
                                                                                                                  + "un"
                                                                                                                  + "ch"
                                                                                                                  + "an"
                                                                                                                  + "ge"
                                                                                                                  +
                                                                                                                  "d"));
                                                                          RandomEnchant.LOGGER.info(
                                                                                  message.getString());
                                                                          context.getSource().sendSuccess(
                                                                                  () -> message, false);
                                                                          return 1;
                                                                      }
                                                                      Config.setRedirectTridentSetPointDistance(value);
                                                                      Component message =
                                                                              Component
                                                                                      .literal("§a["
                                                                                               + "redirectTridentSetPoi"
                                                                                               + "ntDistance]§r")
                                                                                      .append(Component.translatable(
                                                                                              "command.random_"
                                                                                              +
                                                                                              "enchant.config.changed"))
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
                                                                                          .literal("§a["
                                                                                                   + "flyEnchantmentLif"
                                                                                                   +
                                                                                                   "tHeightPerTick]§r")
                                                                                          .append(Component
                                                                                                          .translatable(
                                                                                                                  "co"
                                                                                                                  + "mm"
                                                                                                                  + "an"
                                                                                                                  + "d."
                                                                                                                  + "ra"
                                                                                                                  + "nd"
                                                                                                                  + "om"
                                                                                                                  + "_e"
                                                                                                                  + "nc"
                                                                                                                  + "ha"
                                                                                                                  + "nt"
                                                                                                                  + ".c"
                                                                                                                  + "on"
                                                                                                                  + "fi"
                                                                                                                  + "g."
                                                                                                                  + "un"
                                                                                                                  + "ch"
                                                                                                                  + "an"
                                                                                                                  + "ge"
                                                                                                                  +
                                                                                                                  "d"));
                                                                          RandomEnchant.LOGGER.info(
                                                                                  message.getString());
                                                                          context.getSource().sendSuccess(
                                                                                  () -> message, false);
                                                                          return 1;
                                                                      }
                                                                      Config.setFlyEnchantmentLiftHeightPerTick(value);
                                                                      Component message =
                                                                              Component
                                                                                      .literal("§a["
                                                                                               + "flyEnchantmentLiftHei"
                                                                                               + "ghtPerTick]§r")
                                                                                      .append(Component.translatable(
                                                                                              "command.random_"
                                                                                              +
                                                                                              "enchant.config.changed"))
                                                                                      .append(" §6" + value);
                                                                      RandomEnchant.LOGGER.info(message.getString());
                                                                      context.getSource().sendSuccess(
                                                                              () -> message, false);
                                                                      return 1;
                                                                  })))));
    }
}
