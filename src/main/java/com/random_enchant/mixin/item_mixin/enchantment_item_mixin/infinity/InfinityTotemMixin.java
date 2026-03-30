package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.infinity;


import static com.random_enchant.enchantment.ModEnchantHelper.getEnchantmentLevel;

import com.random_enchant.Config;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class InfinityTotemMixin {
    @Redirect(method = "checkTotemDeathProtection",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private void
    redirectInfinityTotem(ItemStack totem, int amount) {
        if (!Config.infinityUndyingTotem() || getEnchantmentLevel(totem, Enchantments.INFINITY) <= 0) {
            totem.shrink(amount);
        }
    }
}
