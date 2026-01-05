package com.random_enchant.enchantment;

import com.random_enchant.RandomEnchant;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantmentTags {
    public static final TagKey<Enchantment> FURY_OF_FLY_EXCLUSIVE = of("fury_of_fly_exclusive");
    public static final TagKey<Enchantment> REDIRECT_PROJECTILE_EXCLUSIVE = of("redirect_projectile_exclusive");

    public static TagKey<Enchantment> of(String id) {
        return TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, id));
    }

    public static void registerModEnchantmentTags() {
        RandomEnchant.LOGGER.info("Register Mod Enchantment Tags");
    }
}
