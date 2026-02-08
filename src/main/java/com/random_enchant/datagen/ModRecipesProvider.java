package com.random_enchant.datagen;

import com.random_enchant.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category,
                                      ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, ingredients, category, result,
                   experience, cookingTime, group, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category,
                                      ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, ingredients, category, result,
                   experience, cookingTime, group, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void
    oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> serializer,
               AbstractCookingRecipe.Factory<T> recipeFactory, List<ItemLike> ingredients, RecipeCategory category,
               ItemLike result, float experience, int cookingTime, String group, String suffix) {
        for (ItemLike itemlike: ingredients) {
            SimpleCookingRecipeBuilder
                    .generic(Ingredient.of(itemlike), category, result, experience, cookingTime, serializer,
                             recipeFactory)
                    .group(group)
                    .unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, getItemName(result) + suffix + "_" + getItemName(itemlike));
        }
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        super.buildRecipes(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.PEARL_SPEAR)
                .pattern(" #P")
                .pattern("@T#")
                .pattern("B@ ")
                .define('P', Items.ENDER_PEARL)
                .define('#', Items.ECHO_SHARD)
                .define('T', Items.HEAVY_CORE)
                .define('@', Items.POPPED_CHORUS_FRUIT)
                .define('B', Items.BREEZE_ROD)
                .unlockedBy(getHasName(ModItems.PEARL_SPEAR), has(Items.ENDER_PEARL))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.ENCHANT_BRUSH)
                .pattern(" #P")
                .pattern(" T#")
                .pattern("B  ")
                .define('P', Items.ENCHANTED_BOOK)
                .define('#', Items.STRING)
                .define('T', Items.COPPER_INGOT)
                .define('B', Items.STICK)
                .unlockedBy(getHasName(ModItems.ENCHANT_BRUSH), has(Items.STICK))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MILK_BOTTLE)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.MILK_BUCKET)
                .unlockedBy(getHasName(ModItems.MILK_BOTTLE), has(Items.MILK_BUCKET))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MEMORY_POTION)
                .requires(ModItems.MILK_BOTTLE)
                .requires(Items.ENDER_PEARL)
                .requires(Items.SUGAR)
                .requires(Items.GLOWSTONE_DUST)
                .unlockedBy(getHasName(ModItems.MEMORY_POTION), has(ModItems.MILK_BOTTLE))
                .save(recipeOutput);
    }
}
