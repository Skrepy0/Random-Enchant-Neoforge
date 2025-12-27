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

    public static TagKey<Item> of(String id) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, id));
    }

    public static void registerModItemTags() {
        RandomEnchant.LOGGER.info("Register Mod Item Tags");
    }
}
