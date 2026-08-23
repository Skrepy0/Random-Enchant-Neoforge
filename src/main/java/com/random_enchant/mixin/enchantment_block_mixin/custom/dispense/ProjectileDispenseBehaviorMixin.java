package com.random_enchant.mixin.enchantment_block_mixin.custom.dispense;

import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileDispenseBehavior.class)
public class ProjectileDispenseBehaviorMixin {
    private static BlockSource dispenseSource;

    @Inject(method = "execute", at = @At("HEAD"))
    private void injectExecuteHead(BlockSource blockSource, ItemStack item, CallbackInfoReturnable<ItemStack> cir) {
        dispenseSource = blockSource;
    }

    @Redirect(
            method = "execute",
            at = @At(
                    value = "INVOKE",
                    target =
                            "Lnet/minecraft/world/item/ProjectileItem;shoot(Lnet/minecraft/world/entity/projectile/Projectile;DDDFF)V"))
    private void
    redirectShoot(ProjectileItem projectileItem, Projectile projectile, double x, double y, double z, float velocity,
                  float inaccuracy) {
        int l = Math.max(BlockEnchantmentStorage.getLevel(ModEnchantments.KINETIC, dispenseSource.pos()), 0);
        if (BlockEnchantmentStorage.getLevel(ModEnchantments.NO_GRAVITY, dispenseSource.pos()) > 0)
            projectile.setNoGravity(true);
        if (BlockEnchantmentStorage.getLevel(ModEnchantments.STEADY, dispenseSource.pos()) > 0) {
            inaccuracy = 0;
        }
        projectile.shoot(x, y, z, randomEnchant$getVelocity(l, velocity), inaccuracy);
    }

    @Unique
    private float randomEnchant$getVelocity(int level, float velocity) {
        return (float) (velocity * (1 + level * 0.3));
    }
}
