package com.random_enchant.mixin.enchantment_item_mixin.infinity;

import com.random_enchant.Config;
import com.random_enchant.enchantment.ModEnchantHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PotionItem.class)
public class InfinityPotionItemMixin {

    @Unique private boolean flag = false;

    @Redirect(
            method = "finishUsingItem",
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
                    flag = true;
                    return;
                }
            }
        }
        instance.consume(amount, entity);
    }

    @Redirect(method = "finishUsingItem", at = @At(value = "NEW", target = "net/minecraft/world/item/ItemStack"),
              slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/" +
                                                                   "Player;awardStat(Lnet/minecraft/stats/Stat;)V")))
    private ItemStack
    redirectItemStackCreation(ItemLike item) {
        if (Config.infinityPotion() && flag) {
            flag = false;
            return ItemStack.EMPTY;
        }
        return new ItemStack(Items.GLASS_BOTTLE);
    }
}
