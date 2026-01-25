package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.channeling;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.entity.custom.ThrownMace;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThrowableProjectile.class)
public class ThrowableProjectileMixin {
    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target =
                            "Lnet/minecraft/world/entity/projectile/ThrowableProjectile;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    private void
    redirectSetDeltaMovement(ThrowableProjectile instance, Vec3 vec3) {
        if (instance instanceof ThrownMace) {
            ItemStack stack = ((ThrownMace) instance).getItem();
            if (!(ModEnchantHelper.getEnchantmentLevel(stack, ModEnchantments.NO_RESISTANCE) > 0)) {
                instance.setDeltaMovement(vec3);
            }
            return;
        }
        instance.setDeltaMovement(vec3);
    }
}
