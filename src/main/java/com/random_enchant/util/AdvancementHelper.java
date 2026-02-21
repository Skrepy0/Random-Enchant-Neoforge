package com.random_enchant.util;

import com.random_enchant.RandomEnchant;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementHelper {
    private AdvancementHelper() {}
    public static void grantAdvancement(ServerPlayer player, String path, String criterion) {
        ResourceLocation advId = ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, path);
        AdvancementHolder advancement = player.server.getAdvancements().get(advId);
        if (advancement == null) return;

        PlayerAdvancements advancements = player.getAdvancements();
        AdvancementProgress progress = advancements.getOrStartProgress(advancement);

        if (!progress.isDone()) {
            advancements.award(advancement, criterion);
        }
    }
}
