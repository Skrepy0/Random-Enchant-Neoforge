package com.random_enchant.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.random_enchant.RandomEnchant;
import com.random_enchant.data.GlobalSwitchData;
import com.random_enchant.data.GlobalSwitchManager;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.ArrayList;

public class RandomEnchantCommand {

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("random_enchant")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("doRandomEnchant")
                        .then(Commands.argument("enabled", BoolArgumentType.bool())
                                .executes(context -> {
                                            boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                            GlobalSwitchData data = GlobalSwitchManager.get(context.getSource().getLevel());
                                            data.setDoRandomEnchant(enabled);
                                            Component message = enabled ?
                                                    Component.translatable("command.random_enchant.randomEnchant.enable") :
                                                    Component.translatable("command.random_enchant.randomEnchant.disable");

                                            RandomEnchant.LOGGER.info(message.getString());
                                            context.getSource().sendSuccess(() -> message, false);

                                            return 1;
                                        }
                                )
                        )
                )
                .then(Commands.literal("description").executes(context -> {
                    Component modName = Component.literal("§6[§nRandom Enchant§r§6]§r")
                            .withStyle(style -> style
                                    .withUnderlined(true)
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Skrepy0/Random-Enchant-Neoforge"))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                            Component.literal("点击打开§6Random Enchant§r 的Github仓库")))
                            );
                    Component description_1 = Component.literal("  本mod").append(modName).append("由§bSkrepy2233§r制作，以下是几点说明");
                    Component description_2 = Component.literal("1.可以使用/§arandom_enchant doRandomEnchant §b<true/false>§r 进行配置，默认是关闭");
                    Component description_3 = Component.literal("2.§adoRandomEnchant§r开启后,玩家击打有生命实体后会对玩家主手物品进行随机附魔（等级也是随机）");
                    Component description_4 = Component.literal("3.已经启用本mod自带的材质包（修复§b附魔等级的罗马数字显示§r）");
                    Component description_5 = Component.literal("4.附魔随机的范围是§d所有已附魔§r");
                    Component description_6 = Component.literal("5.本mod对原版附魔添加了一些效果，如附魔有§a[无限]§r的食物使用后数量不会减少，方块、不死图腾亦同");
                    Component description_7 = Component.literal("最后，添加的附魔与对应的物品：\n §a[无限]§r投掷类物品，如鸡蛋、末影珍珠、药水（饮用除外）、食物（蛋糕除外）等;各种方块、不死图腾\n §a[力量]§r 火焰弹、铲子、粘液球");
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
        );
    }
}
