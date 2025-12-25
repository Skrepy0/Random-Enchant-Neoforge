package com.random_enchant.enchantment;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

public class ModEnchantHelper {
    public static int getEnchantmentLevel(ItemStack stack, Level world, ResourceKey<Enchantment> enchantmentKey) {
        // 获取物品的附魔组件
        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);

        // 通过 ResourceKey 获取 Holder<Enchantment>
        if (world != null) {
            Holder<Enchantment> enchantmentHolder = world.registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolder(enchantmentKey)
                    .orElse(null);

            // 如果有附魔组件和 Holder，返回等级
            if (enchantments != null && enchantmentHolder != null) {
                return enchantments.getLevel(enchantmentHolder);
            }
        }

        return 0;
    }
    public static Holder<Enchantment> getHolder(ResourceKey<Enchantment> enchantmentKey) {
        Level world = Minecraft.getInstance().level;
        return world.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolder(enchantmentKey)
                .orElse(null);
    }
}
