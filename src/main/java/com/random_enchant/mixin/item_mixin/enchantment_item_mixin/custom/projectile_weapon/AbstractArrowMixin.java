package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.projectile_weapon;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import javax.annotation.Nullable;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {
    @Shadow @Nullable private ItemStack firedFromWeapon;

    @ModifyVariable(method = "tick", at = @At(value = "STORE", ordinal = 0), argsOnly = false, name = "f")
    public float modifyTickDeceleration(float original) {
        if (ModEnchantHelper.getEnchantmentLevel(firedFromWeapon, ModEnchantments.NO_RESISTANCE) > 0) {
            return 1.0f;
        } else {
            return original;
        }
    }
}
