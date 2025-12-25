package com.random_enchant.item;

import com.random_enchant.RandomEnchant;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ModItemModelProperties {
    public static void registerProperties() {
        ItemProperties.register(
                ModItems.PEARL_SPEAR.asItem(),
                ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "cooldown"),
                (stack, world, entity, seed) -> {
                    if (entity instanceof Player player) {
                        return player.getCooldowns().isOnCooldown(stack.getItem()) ? 1.0F : 0.0F;
                    }
                    return 0.0F;
                }
        );
    }
}
