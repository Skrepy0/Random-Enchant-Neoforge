package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.infinity;

import com.random_enchant.enchantment.ModEnchantHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketItem.class)
public class InfinityFireworkRocketMixin {
    @Redirect(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target =
                            "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"))
    private void
    redirectConsume(ItemStack instance, int amount, LivingEntity entity) {
        if (ModEnchantHelper.getEnchantmentLevel(instance, Enchantments.INFINITY) <= 0) {
            instance.consume(amount, entity);
        }
    }

    @Redirect(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private void redirectShrink(ItemStack instance, int amount) {
        if (ModEnchantHelper.getEnchantmentLevel(instance, Enchantments.INFINITY) <= 0) {
            instance.shrink(amount);
        }
    }
}
