package com.random_enchant.mixin.item_mixin.enchantment_item_mixin;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.util.AdvancementHelper;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    private boolean success = false;
    @ModifyArg(
            method = "playerTouch",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"))
    private ItemStack
    modifyItemStack(ItemStack itemStack) {
        boolean flag = false;
        ItemEnchantments tagEnchantments = itemStack.getTagEnchantments();
        for (Holder<Enchantment> enchantment: tagEnchantments.keySet()) {
            if (enchantment.is(EnchantmentTags.CURSE)) {
                flag = true;
                break;
            }
        }
        if (ModEnchantHelper.getEnchantmentLevel(itemStack, ModEnchantments.NO_CURSE) > 0 && flag) {
            EnchantmentHelper.updateEnchantments(
                    itemStack,
                    mutable
                            -> mutable.removeIf(enchantmentHolder
                                                -> enchantmentHolder.is(EnchantmentTags.CURSE) ||
                                                           enchantmentHolder.is(ModEnchantments.NO_CURSE)));
            success = true;
        }
        return itemStack;
    }

    @Inject(method = "playerTouch",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;I)V"))
    private void
    onPlayerTouch(Player entity, CallbackInfo ci) {
        if (entity instanceof ServerPlayer serverPlayer && success) {
            AdvancementHelper.grantAdvancement(serverPlayer, "enchant/cleanse_curse", "cleansed");
        }
    }
}
