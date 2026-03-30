package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.infinity.dispense;

import com.random_enchant.Config;
import com.random_enchant.enchantment.ModEnchantHelper;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DefaultDispenseItemBehavior.class)
public class DefaultDispenseItemBehaviorMixin {
    @Redirect(method = "execute",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack
    redirectSplit(ItemStack stack, int amount) {
        if (!Config.infinityDispenser()) return stack.split(amount);
        if (stack.getCount() == 1) {
            if (ModEnchantHelper.getEnchantmentLevel(stack, Enchantments.INFINITY) > 0) {
                stack.grow(1);
            }
            return stack.split(amount);
        } else {
            ItemStack itemStack = stack.split(amount);
            if (ModEnchantHelper.getEnchantmentLevel(stack, Enchantments.INFINITY) > 0) {
                stack.grow(1);
            }
            return itemStack;
        }
    }
}
