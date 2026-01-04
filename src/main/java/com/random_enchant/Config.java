package com.random_enchant;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static ModConfigSpec.BooleanValue isAlwaysEnchantable = BUILDER
            .comment(
                    "§l所有物品§r可以通过§a铁砧§r附魔"
            )
            .define("isAlwaysEnchantable", false);

    public static ModConfigSpec.BooleanValue infinityUndyingTotem = BUILDER.define("infinityUndyingTotem", false);
    public static ModConfigSpec.BooleanValue infinityBlock = BUILDER.define("infinityBlock", false);
    public static ModConfigSpec.BooleanValue infinityTnt = BUILDER.define("infinityTnt", true);
    public static ModConfigSpec.BooleanValue infinityPotion = BUILDER.define("infinityPotion", true);

    public static ModConfigSpec.IntValue redirectTridentSetPointDistance = BUILDER
            .comment("含有§6[重定向]§r附魔的三叉戟在使用时右键，落点与玩家的最大距离")
            .defineInRange("redirectTridentSetPointDistance", 15, 1, Integer.MAX_VALUE);
    public static ModConfigSpec.DoubleValue flyEnchantmentLiftHeightPerTick = BUILDER
            .comment("玩家使用附魔有§d[Fly]§r的鞘翅，并按下跳远键时，每tick提升的高度")
            .defineInRange("flyEnchantmentLiftHeightPerTick", 0.05, 0.00000001, 1);
    public static boolean isAlwaysEnchantable() {
        return isAlwaysEnchantable.get();
    }

    public static boolean infinityUndyingTotem() {
        return infinityUndyingTotem.get();
    }

    public static boolean infinityBlock() {return infinityBlock.get();}

    public static boolean infinityTnt(){return infinityTnt.get();}

    public static boolean infinityPotion(){return infinityPotion.get();}

    public static int getRedirectTridentSetPointDistance() {
        return redirectTridentSetPointDistance.get();
    }

    public static double getFlyEnchantmentLiftHeightPerTick() {return flyEnchantmentLiftHeightPerTick.get();}

    static final ModConfigSpec SPEC = BUILDER.build();

}
