package com.random_enchant.compat;

import com.random_enchant.item.ModItems;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;

public class ModItemsInfo {
    public static void register(IRecipeRegistration registration) {
        registration.addIngredientInfo(ModItems.PEARL_SPEAR.get(),
                                       Component.translatable("item.jei_info.random_enchant.pearl_spear"));
        registration.addIngredientInfo(ModItems.ENCHANT_BRUSH.get(),
                                       Component.translatable("item.jei_info.random_enchant.enchant_brush"));
        registration.addIngredientInfo(ModItems.MEMORY_POTION.get(),
                                       Component.translatable("item.jei_info.random_enchant.memory_potion"));
        registration.addIngredientInfo(ModItems.SOULTRANCE_POTION.get(),
                                       Component.translatable("item.jei_info.random_enchant.soultrance_potion"));
        registration.addIngredientInfo(ModItems.LIGHTNING_ITEM.get(),
                Component.translatable("item.jei_info.random_enchant.lightning"));
    }
}
