package com.random_enchant;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec.BooleanValue randomEnchant = BUILDER.define("randomEnchant", false);
    public static ModConfigSpec.BooleanValue isAlwaysEnchantable = BUILDER.define("isAlwaysEnchantable", false);
    public static ModConfigSpec.BooleanValue explodeDestroyBlock = BUILDER.define("explodeDestroyBlock", true);
    public static ModConfigSpec.BooleanValue infinityUndyingTotem = BUILDER.define("infinityUndyingTotem", false);
    public static ModConfigSpec.BooleanValue infinityBlock = BUILDER.define("infinityBlock", false);
    public static ModConfigSpec.BooleanValue infinityTnt = BUILDER.define("infinityTnt", true);
    public static ModConfigSpec.BooleanValue infinityPotion = BUILDER.define("infinityPotion", true);
    public static ModConfigSpec.BooleanValue infinityFood = BUILDER.define("infinityFood", true);
    public static ModConfigSpec.BooleanValue infinityThrowableItem = BUILDER.define("infinityThrowableItem", true);
    public static ModConfigSpec.BooleanValue infinityDispenser = BUILDER.define("infinityDispenser", false);
    public static ModConfigSpec.BooleanValue bedrockViolable = BUILDER.define("bedrockViolable", false);
    public static ModConfigSpec.BooleanValue isEnchantedBlockGetatable =
            BUILDER.define("isEnchantedBlockGetatable", true);
    public static ModConfigSpec.IntValue redirectTridentSetPointDistance =
            BUILDER.defineInRange("redirectTridentSetPointDistance", 15, 1, Integer.MAX_VALUE);
    public static ModConfigSpec.DoubleValue flyEnchantmentLiftHeightPerTick =
            BUILDER.defineInRange("flyEnchantmentLiftHeightPerTick", 0.05, 0.00000001, 1);

    public static boolean randomEnchant() { return randomEnchant.get(); }

    public static boolean isAlwaysEnchantable() { return isAlwaysEnchantable.get(); }

    public static boolean infinityUndyingTotem() { return infinityUndyingTotem.get(); }

    public static boolean infinityBlock() { return infinityBlock.get(); }

    public static boolean infinityTnt() { return infinityTnt.get(); }

    public static boolean infinityPotion() { return infinityPotion.get(); }

    public static boolean infinityFood() { return infinityFood.get(); }

    public static boolean infinityThrowableItem() { return infinityThrowableItem.get(); }

    public static boolean infinityDispenser() { return infinityDispenser.get(); }

    public static boolean isEnchantedBlockGetatable() { return isEnchantedBlockGetatable.get(); }

    public static int getRedirectTridentSetPointDistance() { return redirectTridentSetPointDistance.get(); }

    public static void setRedirectTridentSetPointDistance(int value) { redirectTridentSetPointDistance.set(value); }

    public static boolean getBedrockViolable() { return bedrockViolable.get(); }

    public static void setBedrockViolable(boolean value) { bedrockViolable.set(value); }

    public static double getFlyEnchantmentLiftHeightPerTick() { return flyEnchantmentLiftHeightPerTick.get(); }

    public static void setFlyEnchantmentLiftHeightPerTick(double value) { flyEnchantmentLiftHeightPerTick.set(value); }

    public static boolean getExplodeDestroyBlock() { return explodeDestroyBlock.get(); }

    public static void setExplodeDestroyBlock(boolean value) { explodeDestroyBlock.set(value); }

    public static void setRandomEnchant(boolean value) { randomEnchant.set(value); }

    public static void setIsAlwaysEnchantable(boolean value) { isAlwaysEnchantable.set(value); }

    public static void setInfinityUndyingTotem(boolean value) { infinityUndyingTotem.set(value); }

    public static void setInfinityBlock(boolean value) { infinityBlock.set(value); }

    public static void setInfinityPotion(boolean value) { infinityPotion.set(value); }

    public static void setInfinityTnt(boolean value) { infinityTnt.set(value); }

    public static void setIsEnchantedBlockGetatable(boolean value) { isEnchantedBlockGetatable.set(value); }

    public static void setInfinityFood(boolean value) { infinityFood.set(value); }

    public static void setInfinityDispenser(boolean value) { infinityDispenser.set(value); }

    public static void setInfinityThrowableItem(boolean value) { infinityThrowableItem.set(value); }

    static final ModConfigSpec SPEC = BUILDER.build();
}
