package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItemTags;
import com.random_enchant.item.ModItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, RandomEnchant.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModItemTags.FURY_OF_FLY_AVAILABLE).add(ModItems.PEARL_SPEAR.get());
        tag(ModItemTags.REDIRECT_PROJECTILE_AVAILABLE).add(Items.TRIDENT);
        tag(ItemTags.SWORDS).add(ModItems.PEARL_SPEAR.get());
        tag(ModItemTags.EXPLODE_AVAILABLE).addTag(ItemTags.CROSSBOW_ENCHANTABLE).addTag(ItemTags.BOW_ENCHANTABLE);
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(ModItems.PEARL_SPEAR.get());
        tag(ModItemTags.WIND_BURST_AVAILABLE).addTag(ItemTags.MACE_ENCHANTABLE).add(ModItems.PEARL_SPEAR.get());
        tag(ModItemTags.QUICK_CHARGE_AVAILABLE).addTag(ItemTags.CROSSBOW_ENCHANTABLE).add(ModItems.PEARL_SPEAR.get());
        tag(ModItemTags.REDIRECT_PROJECTILE_AVAILABLE).add(Items.TRIDENT);
        tag(ModItemTags.FLY_AVAILABLE).add(Items.ELYTRA);
        tag(ModItemTags.CHANNELING).add(ModItems.PEARL_SPEAR.get()).addTag(ItemTags.TRIDENT_ENCHANTABLE);
        tag(ItemTags.SWORD_ENCHANTABLE).add(ModItems.PEARL_SPEAR.get());
        tag(ItemTags.WEAPON_ENCHANTABLE).add(ModItems.PEARL_SPEAR.get());
        tag(ModItemTags.FROST_WALKER).add(Items.ARROW).addTag(ItemTags.FOOT_ARMOR_ENCHANTABLE);
        tag(ModItemTags.NO_GRAVITY_AVAILABLE).add(Items.BOW).add(Items.CROSSBOW).add(Items.TRIDENT);
        tag(ModItemTags.NO_RESISTANCE_AVAILABLE).add(Items.BOW).add(Items.CROSSBOW).add(Items.TRIDENT);
    }
}
