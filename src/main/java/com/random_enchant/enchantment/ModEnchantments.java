package com.random_enchant.enchantment;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItemTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> FURY_OF_FLY = of("fury_of_fly");
    public static final ResourceKey<Enchantment> BAD_LUCK_OF_THE_SEA = of("bad_luck_of_the_sea");
    public static final ResourceKey<Enchantment> REDIRECT_PROJECTILE = of("redirect_projectile");
    public static final ResourceKey<Enchantment> FLY = of("fly");

    public static void bootstrap(BootstrapContext<Enchantment> registry) {
        HolderGetter<Enchantment> registryEntryLookup2 = registry.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> registryEntryLookup3 = registry.lookup(Registries.ITEM);
        register(registry, FURY_OF_FLY,
                 Enchantment
                         .enchantment(Enchantment.definition(
                                 registryEntryLookup3.getOrThrow(ModItemTags.FURY_OF_FLY_AVAILABLE), 3, 5,
                                 Enchantment.dynamicCost(10, 5), Enchantment.dynamicCost(27, 10), 5,
                                 EquipmentSlotGroup.MAINHAND))
                         .exclusiveWith(registryEntryLookup2.getOrThrow(ModEnchantmentTags.FURY_OF_FLY_EXCLUSIVE)));
        register(registry, BAD_LUCK_OF_THE_SEA,
                 Enchantment.enchantment(Enchantment.definition(
                         registryEntryLookup3.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE), 2, 3,
                         Enchantment.constantCost(25), Enchantment.constantCost(50), 8, EquipmentSlotGroup.FEET)));
        register(registry, REDIRECT_PROJECTILE,
                 Enchantment.enchantment(Enchantment.definition(
                         registryEntryLookup3.getOrThrow(ModItemTags.REDIRECT_PROJECTILE_AVAILABLE), 2, 1,
                         Enchantment.constantCost(25), Enchantment.constantCost(50), 8, EquipmentSlotGroup.ANY)));
        register(registry, FLY,
                 Enchantment.enchantment(Enchantment.definition(
                         registryEntryLookup3.getOrThrow(ModItemTags.FLY_AVAILABLE), 2, 1, Enchantment.constantCost(30),
                         Enchantment.constantCost(50), 10, EquipmentSlotGroup.CHEST)));
    }

    public static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key,
                                Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }

    public static ResourceKey<Enchantment> of(String id) {
        return ResourceKey.create(Registries.ENCHANTMENT,
                                  ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, id));
    }
}
