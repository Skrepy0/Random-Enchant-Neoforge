package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModZHCNLangProvider extends LanguageProvider {
    public ModZHCNLangProvider(PackOutput output) {
        super(output, RandomEnchant.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add(ModItems.PEARL_SPEAR.get(), "珍珠矛");
    }
}
