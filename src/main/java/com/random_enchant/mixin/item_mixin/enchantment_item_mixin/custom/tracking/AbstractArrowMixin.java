package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.tracking;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    @Unique
    @Nullable
    private LivingEntity randomEnchantTracking$trackedTarget;
    @Unique
    private int randomEnchantTracking$trackingLevel = 1;
    @Unique
    private int randomEnchantTracking$ticksSinceLastSearch = 0;
    @Unique
    private boolean randomEnchantTracking$isActivelyTracking = false;

    @Unique
    private static final double TRACKING_RANGE = 30.0; // 最大追踪距离（方块）
    @Unique
    private static final double MAX_TRACKING_ANGLE = Math.PI / 2; // 最大追踪角度（90度），超出则目标可能丢失
    @Unique
    private static final double MIN_SPEED_FOR_TRACKING = 0.2; // 启用追踪所需的最小箭矢速度（米/刻）
    @Unique
    private static final double BASE_TURN_RATE = 0.8; // 基础转向速率（弧度/刻），受等级和角度动态调整
    @Unique
    private static final double MAX_TURN_RATE = 0.8; // 最大转向速率限制（弧度/刻），防止瞬间转向
    @Unique
    private static final int SEARCH_COOLDOWN = 5; // 目标搜索的冷却间隔（刻），避免每刻都进行昂贵的范围查询
    @Unique
    private static final double PREDICTION_FACTOR_BASE = 0.3; // 目标位置预测的基础系数，影响预测偏移量
    @Unique
    private static final double PREDICTION_FACTOR_PER_LEVEL = 0.1; // 每级附魔增加的预测系数，高等级预判更准

    @Inject(method = "shoot(DDDFF)V", at = @At("RETURN"))
    private void onShoot(double x, double y, double z, float velocity, float inaccuracy, CallbackInfo ci) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        if (!arrow.level().isClientSide) {
            // 重置状态
            this.randomEnchantTracking$trackedTarget = null;
            this.randomEnchantTracking$isActivelyTracking = false;

            ItemStack weapon = arrow.getWeaponItem();
            if (weapon != null && !weapon.isEmpty()) {
                int level = ModEnchantHelper.getEnchantmentLevel(weapon, ModEnchantments.TRACKING);
                this.randomEnchantTracking$trackingLevel = level;

                if (level > 0) {
                    Entity owner = arrow.getOwner();
                    if (owner instanceof LivingEntity livingOwner) {
                        this.randomEnchantTracking$trackedTarget =
                                this.randomEnchantTracking$findOptimalTarget(arrow, livingOwner);
                        this.randomEnchantTracking$isActivelyTracking =
                                (this.randomEnchantTracking$trackedTarget != null);
                    }
                }
            } else {
                this.randomEnchantTracking$trackingLevel = 0;
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        if (randomEnchantTracking$trackingLevel <= 0 || arrow.onGround() || !arrow.isAlive()) return;

        randomEnchantTracking$ticksSinceLastSearch++;

        if (!arrow.level().isClientSide) {
            double currentSpeed = arrow.getDeltaMovement().length();
            if (currentSpeed < MIN_SPEED_FOR_TRACKING) {
                randomEnchantTracking$isActivelyTracking = false;
                return;
            }

            Entity owner = arrow.getOwner();
            if (owner instanceof LivingEntity livingOwner) {
                randomEnchantTracking$updateTrackingTarget(arrow, livingOwner);
            }

            if (randomEnchantTracking$isActivelyTracking && randomEnchantTracking$trackedTarget != null) {
                randomEnchantTracking$steerTowardsTarget(arrow);
            }
        }

        randomEnchantTracking$updateArrowRotation(arrow);
    }

    @Unique
    private void randomEnchantTracking$updateTrackingTarget(AbstractArrow arrow, LivingEntity owner) {
        // 仅在冷却期满或目标无效时重新搜索
        boolean needSearch = randomEnchantTracking$ticksSinceLastSearch >= SEARCH_COOLDOWN ||
                randomEnchantTracking$trackedTarget == null ||
                !randomEnchantTracking$trackedTarget.isAlive() ||
                !randomEnchantTracking$isInTrackingRange(arrow, randomEnchantTracking$trackedTarget);

        if (needSearch) {
            randomEnchantTracking$ticksSinceLastSearch = 0;
            randomEnchantTracking$trackedTarget = randomEnchantTracking$findOptimalTarget(arrow, owner);
            randomEnchantTracking$isActivelyTracking = randomEnchantTracking$trackedTarget != null;
        } else if (randomEnchantTracking$trackedTarget != null) {
            // 检查目标是否仍在视野内，否则暂停追踪
            randomEnchantTracking$isActivelyTracking =
                    randomEnchantTracking$isTargetInSight(arrow, randomEnchantTracking$trackedTarget);
        }
    }

    @Nullable
    private LivingEntity randomEnchantTracking$findOptimalTarget(AbstractArrow arrow, LivingEntity owner) {
        if (owner.level() != arrow.level()) return null;

        double range = TRACKING_RANGE + randomEnchantTracking$trackingLevel * 8.0;
        AABB searchArea = arrow.getBoundingBox().inflate(range);
        List<LivingEntity> candidates = owner.level().getEntitiesOfClass(LivingEntity.class, searchArea,
                e -> e != owner && e.isAlive() && !e.is(arrow));

        // 第一步：寻找正在攻击射手的怪物
        for (LivingEntity e : candidates) {
            if (e instanceof Monster monster && monster.getTarget() == owner) {
                return monster;
            }
        }

        // 第二步：寻找有视线的怪物（跳过中立生物）
        for (LivingEntity e : candidates) {
            if (e instanceof Monster monster && e.hasLineOfSight(arrow)) {
                return monster;
            }
        }

        // 第三步：在所有LivingEntity中（排除自身、主人、主人的宠物）选择方向点积最大且超过阈值0.5的
        Vec3 arrowPos = arrow.position();
        Vec3 arrowDir = arrow.getDeltaMovement().normalize();
        double bestDot = 0.5; // SeekerArrow中的阈值
        LivingEntity bestTarget = null;

        for (LivingEntity e : candidates) {
            if (e == owner) continue;
            if (e instanceof TamableAnimal tame && tame.getOwner() == owner) continue; // 跳过主人的宠物
            if (!e.hasLineOfSight(arrow)) continue; // 必须有视线

            Vec3 toTarget = e.getBoundingBox().getCenter().subtract(arrowPos).normalize();
            double dot = arrowDir.dot(toTarget);
            if (dot > bestDot) {
                bestDot = dot;
                bestTarget = e;
            }
        }

        return bestTarget;
    }

    @Unique
    private boolean randomEnchantTracking$isInTrackingRange(AbstractArrow arrow, LivingEntity target) {
        double range = TRACKING_RANGE + randomEnchantTracking$trackingLevel * 8.0;
        return arrow.position().distanceToSqr(target.position()) <= range * range;
    }

    @Unique
    private boolean randomEnchantTracking$isTargetInSight(AbstractArrow arrow, LivingEntity target) {
        Vec3 toTarget = target.getBoundingBox().getCenter().subtract(arrow.position()).normalize();
        double dot = arrow.getDeltaMovement().normalize().dot(toTarget);
        return dot > Math.cos(MAX_TRACKING_ANGLE); // 角度检查
    }

    @Unique
    private void randomEnchantTracking$steerTowardsTarget(AbstractArrow arrow) {
        if (randomEnchantTracking$trackedTarget == null) return;

        Vec3 arrowPos = arrow.position();
        Vec3 targetPos = randomEnchantTracking$trackedTarget.getBoundingBox().getCenter();

        // 预测目标位置（参考SeekerArrow，但保留原Mixin的预测逻辑）
        Vec3 targetMotion = randomEnchantTracking$trackedTarget.getDeltaMovement();
        double distance = arrowPos.distanceTo(targetPos);
        double arrowSpeed = arrow.getDeltaMovement().length();
        double timeToImpact = distance / (arrowSpeed + 0.1);
        double predictionFactor =
                PREDICTION_FACTOR_BASE + randomEnchantTracking$trackingLevel * PREDICTION_FACTOR_PER_LEVEL;
        Vec3 predictedPos = targetPos.add(targetMotion.scale(timeToImpact * predictionFactor));

        Vec3 toTarget = predictedPos.subtract(arrowPos);
        double distToTarget = toTarget.length();
        if (distToTarget < 1.0) return;

        Vec3 desiredDir = toTarget.normalize();
        Vec3 currentDir = arrow.getDeltaMovement().normalize();

        double dot = Math.max(-1.0, Math.min(1.0, currentDir.dot(desiredDir)));
        double angleRad = Math.acos(dot);

        // 动态转向速率：等级越高、角度越大，转向越快
        double levelFactor = 0.5 + randomEnchantTracking$trackingLevel * 0.25;
        double angleFactor = (angleRad / Math.PI) * 2.0;
        double turnRate = Math.min(MAX_TURN_RATE, BASE_TURN_RATE * angleFactor * levelFactor);

        Vec3 newDir;
        if (angleRad <= turnRate) {
            newDir = desiredDir;
        } else {
            // 计算旋转轴
            Vec3 axis = currentDir.cross(desiredDir).normalize();
            if (axis.lengthSqr() < 1e-6) {
                axis = new Vec3(0, 1, 0).cross(currentDir).normalize();
                if (axis.lengthSqr() < 1e-6) axis = new Vec3(1, 0, 0);
            }
            // 罗德里格斯旋转
            double cos = Math.cos(turnRate);
            double sin = Math.sin(turnRate);
            Vec3 rotated = currentDir.scale(cos).add(axis.cross(currentDir).scale(sin));
            newDir = rotated.normalize();
        }

        // 保持速度大小，并添加SeekerArrow中的Y轴偏移（0.045）
        double currentSpeed = arrow.getDeltaMovement().length();
        Vec3 newMotion = newDir.scale(currentSpeed).add(0, 0.045, 0);
        arrow.setDeltaMovement(newMotion);
    }

    @Unique
    private void randomEnchantTracking$updateArrowRotation(AbstractArrow arrow) {
        Vec3 motion = arrow.getDeltaMovement();
        if (motion.lengthSqr() < 0.0001) return;

        double horizontalDistance = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        float yaw = (float) (Math.atan2(motion.x, motion.z) * (180.0 / Math.PI));
        float pitch = (float) (Math.atan2(motion.y, horizontalDistance) * (180.0 / Math.PI));

        float prevYaw = arrow.getYRot();
        float prevPitch = arrow.getXRot();
        float lerpFactor = 0.3f;

        float smoothedYaw = prevYaw + (yaw - prevYaw) * lerpFactor;
        float smoothedPitch = prevPitch + (pitch - prevPitch) * lerpFactor;

        arrow.setYRot(smoothedYaw);
        arrow.setXRot(smoothedPitch);

        arrow.yRotO = smoothedYaw;
        arrow.xRotO = smoothedPitch;
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void onHitEntity(net.minecraft.world.phys.EntityHitResult result, CallbackInfo ci) {
        this.randomEnchantTracking$trackedTarget = null;
        this.randomEnchantTracking$isActivelyTracking = false;
    }

    @Inject(method = "onHitBlock", at = @At("HEAD"))
    private void onHitBlock(net.minecraft.world.phys.BlockHitResult result, CallbackInfo ci) {
        this.randomEnchantTracking$trackedTarget = null;
        this.randomEnchantTracking$isActivelyTracking = false;
    }

    @Inject(method = "tickDespawn", at = @At("HEAD"))
    private void onTickDespawn(CallbackInfo ci) {
        this.randomEnchantTracking$trackedTarget = null;
        this.randomEnchantTracking$isActivelyTracking = false;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void onReadAdditionalSaveData(net.minecraft.nbt.CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("TrackingLevel")) {
            this.randomEnchantTracking$trackingLevel = compound.getInt("TrackingLevel");
        }
        if (compound.contains("IsTracking")) {
            this.randomEnchantTracking$isActivelyTracking = compound.getBoolean("IsTracking");
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void onAddAdditionalSaveData(net.minecraft.nbt.CompoundTag compound, CallbackInfo ci) {
        compound.putInt("TrackingLevel", this.randomEnchantTracking$trackingLevel);
        compound.putBoolean("IsTracking", this.randomEnchantTracking$isActivelyTracking);
    }
}