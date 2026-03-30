package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.infinity.dispense;

import com.random_enchant.Config;
import com.random_enchant.enchantment.ModEnchantHelper;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProjectileDispenseBehavior.class)
public class ProjectileDispenseBehaviorMixin {
    @Redirect(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
    private void shrinkInfinityItemStack(ItemStack stack, int amount) {
        if (!Config.infinityDispenser() || ModEnchantHelper.getEnchantmentLevel(stack, Enchantments.INFINITY) <= 0) {
            stack.shrink(amount);
        }
    }
}
