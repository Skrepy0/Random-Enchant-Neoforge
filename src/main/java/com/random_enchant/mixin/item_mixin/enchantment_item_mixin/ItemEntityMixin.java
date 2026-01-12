package com.random_enchant.mixin.item_mixin.enchantment_item_mixin;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @ModifyArg(
            method = "playerTouch",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"))
    private ItemStack
    modifyItemStack(ItemStack itemstack) {
        if (ModEnchantHelper.getEnchantmentLevel(itemstack, ModEnchantments.NO_CURSE) > 0) {
            EnchantmentHelper.updateEnchantments(
                    itemstack,
                    p_330066_
                    -> p_330066_.removeIf(p_344368_
                                          -> p_344368_.is(EnchantmentTags.CURSE) ||
                                                     p_344368_.is(ModEnchantments.NO_CURSE)));
        }
        return itemstack;
    }
}
