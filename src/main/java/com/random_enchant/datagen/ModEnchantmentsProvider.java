package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.enchantment.ModEnchantments;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

public class ModEnchantmentsProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER =
            new RegistrySetBuilder().add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);


    public ModEnchantmentsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(RandomEnchant.MOD_ID));
    }
}
