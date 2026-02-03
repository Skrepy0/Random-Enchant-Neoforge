package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.projectile_weapon;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
    @Shadow @Nullable private ItemStack firedFromWeapon;

    @Redirect(method = "tick",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/"
                                                  + "AbstractArrow;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    public void redirectSetDeltaMovement(AbstractArrow instance, Vec3 vec3) {
        ItemStack stack = instance.getWeaponItem();
        if (stack != null && stack.isEmpty()){
            instance.setDeltaMovement(vec3);
            return;
        }
        if (ModEnchantHelper.getEnchantmentLevel(stack, ModEnchantments.NO_RESISTANCE) > 0) {
        } else if (firedFromWeapon != null &&
                   ModEnchantHelper.getEnchantmentLevel(firedFromWeapon, ModEnchantments.NO_RESISTANCE) > 0) {
        } else {
            instance.setDeltaMovement(vec3);
        }
    }
}
