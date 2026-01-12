package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.projectile_weapon;

import com.llamalad7.mixinextras.sugar.Local;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {
    @Inject(method = "shoot",
            at = @At(value = "INVOKE_ASSIGN",
                     target = "Lnet/minecraft/world/item/ProjectileWeaponItem;createProjectile(Lnet/minecraft/world/" +
                              "level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/" +
                              "ItemStack;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/" +
                              "projectile/Projectile;",
                     shift = At.Shift.AFTER))
    private void
    afterCreateProjectile(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon,
                          List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit,
                          @Nullable LivingEntity target, CallbackInfo ci, @Local Projectile projectile) {
        if (ModEnchantHelper.getEnchantmentLevel(weapon, ModEnchantments.NO_GRAVITY) > 0) {
            projectile.setNoGravity(true);
        } else if (ModEnchantHelper.getEnchantmentLevel(weapon, ModEnchantments.NO_RESISTANCE) > 0) {
            projectile.setDeltaMovement(projectile.getDeltaMovement().scale(1));
        }
    }
}
