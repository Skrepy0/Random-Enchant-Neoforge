package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.fishing_rod;

import com.random_enchant.data.nbt.FishingHookData;
import java.util.Objects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FishingHook.class)
public class FishingHookMixin {
    @Redirect(
            method = "<init>(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;II)V",
            at = @At(
                    value = "INVOKE",
                    target =
                            "Lnet/minecraft/world/entity/projectile/FishingHook;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    private void
    setDeltaMovement(FishingHook fishingHook, Vec3 deltaMovement) {
        int l = ((Player) (Objects.requireNonNull(fishingHook.getOwner())))
                        .getData(FishingHookData.KINETIC_LEVEL.get())
                        .getKineticLevel();
        if (l > 0) {
            deltaMovement = deltaMovement.scale(1.0D + 0.12D * l);
        }
        fishingHook.setDeltaMovement(deltaMovement);
    }
    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target =
                            "Lnet/minecraft/world/entity/projectile/FishingHook;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
                    ordinal = 3))
    private void
    redirectDeltaMovementNoGravity(FishingHook fishingHook, Vec3 deltaMovement) {
        if (!fishingHook.isNoGravity()) {
            fishingHook.setDeltaMovement(deltaMovement);
        }
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target =
                            "Lnet/minecraft/world/entity/projectile/FishingHook;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V",
                    ordinal = 5))
    private void
    redirectDeltaMovementNoResistance(FishingHook fishingHook, Vec3 deltaMovement) {
        if (fishingHook.getData(FishingHookData.NO_RESISTANCE_LEVEL).getNoResistanceLevel() <= 0) {
            fishingHook.setDeltaMovement(deltaMovement);
        }
    }
}
