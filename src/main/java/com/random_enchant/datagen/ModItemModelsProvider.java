package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelsProvider extends ItemModelProvider {
    public ModItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RandomEnchant.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        handheldItem(ModItems.ENCHANT_BRUSH.asItem());
        basicItem(ModItems.MILK_BOTTLE.asItem());
    }
}
