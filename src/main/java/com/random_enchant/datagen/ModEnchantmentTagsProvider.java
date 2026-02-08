package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.enchantment.ModEnchantmentTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModEnchantmentTagsProvider extends EnchantmentTagsProvider {
    public ModEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                      @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, RandomEnchant.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModEnchantmentTags.FURY_OF_FLY_EXCLUSIVE).add(Enchantments.CHANNELING);
        tag(ModEnchantmentTags.REDIRECT_PROJECTILE_EXCLUSIVE).add(Enchantments.LOOTING);
        tag(ModEnchantmentTags.EXPLODE_EXCLUSIVE)
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "steady"));
        tag(ModEnchantmentTags.KINETIC_EXCLUSIVE).add(Enchantments.FLAME);
        tag(EnchantmentTags.CURSE)
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "bad_luck_of_the_sea"));
        tag(EnchantmentTags.TRADEABLE)
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "fury_of_fly"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "bad_luck_of_the_sea"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "steady"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "kinetic"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "double_jump"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "explode"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "throwable"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "fly"));
        tag(EnchantmentTags.ON_RANDOM_LOOT)
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "fury_of_fly"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "bad_luck_of_the_sea"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "no_gravity"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "no_resistance"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "no_curse"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "steady"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "kinetic"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "fly"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "double_jump"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "redirect_projectile"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "explode"));
        tag(EnchantmentTags.TREASURE)
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "fury_of_fly"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "redirect_projectile"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "fly"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "no_gravity"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "no_resistance"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "no_curse"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "steady"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "kinetic"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "explode"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "throwable"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "double_jump"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "bad_luck_of_the_sea"));
        tag(EnchantmentTags.ON_MOB_SPAWN_EQUIPMENT)
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "bad_luck_of_the_sea"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "redirect_projectile"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "steady"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "kinetic"));
        tag(EnchantmentTags.IN_ENCHANTING_TABLE)
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "kinetic"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "double_jump"))
                .addOptional(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "steady"));
    }
}
