package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.projectile_weapon;

import com.llamalad7.mixinextras.sugar.Local;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.network.packet.S2C.UpdateProjectileVelocityPacket;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {
    @Unique private boolean randomEnchant$InaccuracyFlag = false;
    @Unique public int randomEnchant$KineticFlag = 0;
    @Inject(method = "shoot",
            at = @At(value = "INVOKE_ASSIGN",
                     target = "Lnet/minecraft/world/item/ProjectileWeaponItem;createProjectile(Lnet/minecraft/world/"
                              + "level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/"
                              + "ItemStack;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/"
                              + "projectile/Projectile;",
                     shift = At.Shift.AFTER))
    private void
    afterCreateProjectile(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon,
                          List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit,
                          @Nullable LivingEntity target, CallbackInfo ci, @Local Projectile projectile) {
        if (ModEnchantHelper.getEnchantmentLevel(weapon, ModEnchantments.NO_GRAVITY) > 0) {
            projectile.setNoGravity(true);
        } else if (ModEnchantHelper.getEnchantmentLevel(weapon, ModEnchantments.NO_RESISTANCE) > 0) {
            projectile.setDeltaMovement(projectile.getDeltaMovement());
        }
    }

    @Inject(method = "shoot",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/item/ProjectileWeaponItem;createProjectile(Lnet/minecraft/world/"
                              + "level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/"
                              + "ItemStack;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/"
                              + "projectile/Projectile;",
                     shift = At.Shift.AFTER))
    private void
    solveInaccuracy(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon,
                    List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit,
                    LivingEntity target, CallbackInfo ci) {
        if (ModEnchantHelper.getEnchantmentLevel(weapon, ModEnchantments.STEADY) > 0) {
            randomEnchant$InaccuracyFlag = true;
        }
    }
    @ModifyArg(method = "shoot",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/world/item/ProjectileWeaponItem;shootProjectile(Lnet/minecraft/"
                                 + "world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/"
                                 + "Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V"),
               index = 4)
    private float
    modifyInaccuracy(float inaccuracy) {
        if (randomEnchant$InaccuracyFlag) {
            randomEnchant$InaccuracyFlag = false;
            return 0;
        }
        return inaccuracy;
    }
    @Inject(method = "shoot", at = @At(value = "INVOKE",
                                       target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/" +
                                                "minecraft/world/entity/Entity;)Z",
                                       shift = At.Shift.AFTER))
    private void
    modifyProjectileVelocity(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon,
                             List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit,
                             LivingEntity target, CallbackInfo ci, @Local Projectile projectile) {
        randomEnchant$KineticFlag = Math.max(ModEnchantHelper.getEnchantmentLevel(weapon, ModEnchantments.KINETIC), 0);
        projectile.setDeltaMovement(projectile.getDeltaMovement().scale(1 + randomEnchant$KineticFlag * 2.0f));
        PacketDistributor.sendToAllPlayers(
                new UpdateProjectileVelocityPacket(projectile.getId(), projectile.getDeltaMovement()));
    }
}
