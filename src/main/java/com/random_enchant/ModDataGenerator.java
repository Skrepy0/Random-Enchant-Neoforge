package com.random_enchant;

import com.random_enchant.datagen.*;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = RandomEnchant.MOD_ID)
public class ModDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookUpProvider = event.getLookupProvider();
        BlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(packOutput, lookUpProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), new ModRecipesProvider(packOutput, lookUpProvider));
        generator.addProvider(event.includeClient(), new ModItemModelsProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModENUSLanProvider(packOutput));
        generator.addProvider(event.includeClient(), new ModZHCNLangProvider(packOutput));
        generator.addProvider(event.includeClient(), blockTagsProvider);
        generator.addProvider(event.includeClient(),
                              new ModItemTagsProvider(packOutput, lookUpProvider, blockTagsProvider.contentsGetter(),
                                                      existingFileHelper));
        generator.addProvider(event.includeClient(),
                              new ModEnchantmentTagsProvider(packOutput, lookUpProvider, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModEnchantmentsProvider(packOutput, lookUpProvider));
    }
}
