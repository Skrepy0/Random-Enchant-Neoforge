package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.windcharge;

import com.random_enchant.enchantment.ModEnchantHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    @Unique int randomEnchant$particleCooldown = 0;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) { super(entityType, level); }

    @Inject(method = "tick", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        // 每 tick 增加计时器
        if (this.randomEnchant$particleCooldown > 0) {
            this.randomEnchant$particleCooldown--;
        }

        ItemStack itemStack = this.getMainHandItem();
        if (itemStack.is(Items.WIND_CHARGE)) {
            int i = ModEnchantHelper.getEnchantmentLevel(itemStack, Enchantments.LOYALTY);
            if (i > 0 && level().isClientSide() && this.randomEnchant$particleCooldown == 0) {
                Vec3 lookVec = Vec3.directionFromRotation(this.getRotationVector());
                double distance = 2d;
                // 将朝向向量乘以所需的距离，以确定爆炸生成的位置
                double offsetX = lookVec.x * distance;
                double offsetY = lookVec.y * distance;
                double offsetZ = lookVec.z * distance;
                Vec3 explorePos = new Vec3(this.getX() + offsetX, this.getY() + offsetY + 1.625, this.getZ() + offsetZ);
                level().addParticle(ParticleTypes.COMPOSTER, true, explorePos.x, explorePos.y, explorePos.z, 0, 0, 0);

                this.randomEnchant$particleCooldown = 4;
            }
        }
    }
}
