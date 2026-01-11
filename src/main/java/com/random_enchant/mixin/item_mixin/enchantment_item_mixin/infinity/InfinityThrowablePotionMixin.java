package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.infinity;

import com.random_enchant.Config;
import com.random_enchant.enchantment.ModEnchantHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThrowablePotionItem.class)
public class InfinityThrowablePotionMixin {
    @Redirect(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target =
                            "Lnet/minecraft/world/item/ItemStack;consume(ILnet/minecraft/world/entity/LivingEntity;)V"))
    private void
    redirectConsume(ItemStack instance, int amount, LivingEntity entity) {
        if (!Config.infinityPotion()) {
            instance.consume(amount, entity);
            return;
        }
        if (entity instanceof Player player) {
            for (InteractionHand hand: InteractionHand.values()) {
                if (ModEnchantHelper.getEnchantmentLevel(player.getItemInHand(hand), Enchantments.INFINITY) > 0) {
                    return;
                }
            }
        }
        instance.consume(amount, entity);
    }
}
