package com.random_enchant.item;

import com.random_enchant.RandomEnchant;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> FURY_OF_FLY_AVAILABLE = of("fury_of_fly_available");
    public static final TagKey<Item> QUICK_CHARGE_AVAILABLE = of("quick_charge_available");
    public static final TagKey<Item> WIND_BURST_AVAILABLE = of("wind_burst_available");
    public static final TagKey<Item> CHANNELING = of("channeling_available");
    public static final TagKey<Item> FROST_WALKER = of("frost_walker_available");
    public static final TagKey<Item> REDIRECT_PROJECTILE_AVAILABLE = of("redirect_projectile_available");
    public static final TagKey<Item> FLY_AVAILABLE = of("fly_available");
    public static final TagKey<Item> NO_GRAVITY_AVAILABLE = of("no_gravity_available");
    public static final TagKey<Item> NO_RESISTANCE_AVAILABLE = of("no_resistance_available");
    public static final TagKey<Item> NO_CURSE_AVAILABLE = of("no_curse_available");
    public static final TagKey<Item> STEADY_AVAILABLE = of("steady_available");
    public static final TagKey<Item> KINETIC_AVAILABLE = of("kinetic_available");
    public static final TagKey<Item> EXPLODE_AVAILABLE = of("explode_available");
    public static final TagKey<Item> THROWABLE_AVAILABLE = of("throwable_available");

    public static TagKey<Item> of(String id) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, id));
    }

    public static void registerModItemTags() { RandomEnchant.LOGGER.info("Register Mod Item Tags"); }
}
