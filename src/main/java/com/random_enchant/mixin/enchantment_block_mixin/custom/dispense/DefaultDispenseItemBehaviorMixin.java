package com.random_enchant.mixin.enchantment_block_mixin.custom.dispense;

import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DefaultDispenseItemBehavior.class)
public abstract class DefaultDispenseItemBehaviorMixin {


    private static BlockSource dispenseSource;

    @Inject(method = "dispense", at = @At("HEAD"))
    private static void dispenseHead(BlockSource blockSource, ItemStack item, CallbackInfoReturnable<ItemStack> cir) {
        dispenseSource = blockSource;
    }

    @Redirect(method = "spawnItem",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"))
    private static void
    setDeltaMovement(ItemEntity itemEntity, double d, double e, double f) {
        if (dispenseSource == null) return;
        int l = BlockEnchantmentStorage.getLevel(ModEnchantments.KINETIC, dispenseSource.pos());
        if (l > 0) {
            itemEntity.setDeltaMovement(d * (double) l * 0.45, e * (double) l * 0.3, f * (double) l * 0.45);
        }
    }
    @Redirect(method = "spawnItem",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;triangle(DD)D", ordinal = 0))
    private static double
    getTriangle1(RandomSource instance, double center, double maxDeviation) {
        if (dispenseSource == null) return instance.triangle(center, maxDeviation);
        if (BlockEnchantmentStorage.getLevel(ModEnchantments.STEADY, dispenseSource.pos()) > 0) {
            return center;
        }
        return instance.triangle(center, maxDeviation);
    }
    @Redirect(method = "spawnItem",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;triangle(DD)D", ordinal = 1))
    private static double
    getTriangle2(RandomSource instance, double center, double maxDeviation) {
        if (dispenseSource == null) return instance.triangle(center, maxDeviation);
        if (BlockEnchantmentStorage.getLevel(ModEnchantments.STEADY, dispenseSource.pos()) > 0) {
            maxDeviation = 0;
        }
        return instance.triangle(center, maxDeviation);
    }
}
