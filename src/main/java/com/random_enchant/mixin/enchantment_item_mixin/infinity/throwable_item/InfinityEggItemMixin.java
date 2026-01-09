package com.random_enchant.mixin.enchantment_item_mixin.infinity.throwable_item;

import com.random_enchant.Config;
import com.random_enchant.enchantment.ModEnchantHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EggItem.class)
public class InfinityEggItemMixin {
    @Redirect(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target =
                            "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"))
    private void
    redirectConsume(ItemStack itemStack, int amount, LivingEntity entity) {
        if (!Config.infinityThrowableItem()) {
            itemStack.consume(amount, entity);
            return;
        }
        if (ModEnchantHelper.getEnchantmentLevel(itemStack, Enchantments.INFINITY) <= 0) {
            itemStack.consume(amount, entity);
        }
    }
}
