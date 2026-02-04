// ModChestLootTableProvider.java
package com.random_enchant.datagen;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class ModChestLootTableProvider extends LootTableProvider {

    public ModChestLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(new SubProviderEntry(ModChestLootTables::new, LootContextParamSets.CHEST)),
              registries);
    }
}
