package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.fishing_rod;

import com.random_enchant.data.nbt.FishingHookData;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {
    private static final ThreadLocal<ItemStack> fishingRodThreadLocal = new ThreadLocal<>();

    @Inject(method = "use", at = @At("HEAD"))
    private void captureFishingRod(Level level, Player player, InteractionHand hand,
                                   CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack rod = player.getItemInHand(hand);
        fishingRodThreadLocal.set(rod);
        int l = ModEnchantHelper.getEnchantmentLevel(rod, ModEnchantments.KINETIC);
        player.getData(FishingHookData.KINETIC_LEVEL.get()).setKineticLevel(Math.max(l, 0));
    }

    @Inject(method = "use", at = @At("RETURN"))
    private void cleanup(Level level, Player player, InteractionHand hand,
                         CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        fishingRodThreadLocal.remove();
    }

    @Redirect(
            method = "use",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean
    redirectUse(Level level, Entity entity) {
        ItemStack rod = fishingRodThreadLocal.get();
        entity.getData(FishingHookData.NO_RESISTANCE_LEVEL.get())
                .setNoResistanceLevel(
                        Math.max(0, ModEnchantHelper.getEnchantmentLevel(rod, ModEnchantments.NO_RESISTANCE)));
        if (rod != null) {
            int noGravityLevel = ModEnchantHelper.getEnchantmentLevel(rod, ModEnchantments.NO_GRAVITY);
            if (noGravityLevel > 0) {
                entity.setNoGravity(true);
            }
        }
        return level.addFreshEntity(entity);
    }
}
