package com.random_enchant.event;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class ThrowableItemEvent {
    @SubscribeEvent
    public static void useItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();
        if (level.isClientSide()) return;
        int throwableLevel = ModEnchantHelper.getEnchantmentLevel(stack, ModEnchantments.THROWABLE);
        if (throwableLevel > 0) {
            stack.shrink(1);
        }
    }
}
