package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModENUSLanProvider extends LanguageProvider {
    public ModENUSLanProvider(PackOutput output) {
        super(output, RandomEnchant.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModItems.PEARL_SPEAR.get(), "pearl_spear");
    }
}
