package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.tracking;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

    @Unique @Nullable private LivingEntity randomEnchantTracking$trackedTarget;
    @Unique private int randomEnchantTracking$trackingLevel = 0;
    @Unique private int randomEnchantTracking$ticksInAir = 0;
    @Unique private double randomEnchantTracking$previousHorizontalSpeed = 0.0;
    @Unique private boolean randomEnchantTracking$isActivelyTracking = false;

    @Unique private static final double TRACKING_STRENGTH = 0.2; // 增加追踪强度
    @Unique private static final double TRACKING_RANGE = 30.0;
    @Unique private static final double MAX_TRACKING_ANGLE = Math.PI / 3; // 最大追踪角度 60度
    @Unique private static final int TRACKING_START_DELAY = 3; // 追踪开始延迟（ticks）
    @Unique private static final double MIN_SPEED_FOR_TRACKING = 0.2; // 追踪所需最小速度
    @Unique private static final double MAX_TURN_RATE = 0.3; // 最大转弯速率

    @Inject(method = "shoot(DDDFF)V", at = @At("RETURN"))
    private void onShoot(double x, double y, double z, float velocity, float inaccuracy, CallbackInfo ci) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        if (!arrow.level().isClientSide) {
            ItemStack weapon = arrow.getWeaponItem();
            if (!weapon.isEmpty()) {
                this.randomEnchantTracking$trackingLevel =
                        ModEnchantHelper.getEnchantmentLevel(weapon, ModEnchantments.TRACKING);

                if (this.randomEnchantTracking$trackingLevel > 0) {
                    this.randomEnchantTracking$ticksInAir = 0;
                    this.randomEnchantTracking$previousHorizontalSpeed =
                            Math.sqrt(x * x + z * z) * velocity;
                    this.randomEnchantTracking$isActivelyTracking = false;

                    // 立即寻找初始目标
                    Entity owner = arrow.getOwner();
                    if (owner instanceof LivingEntity livingOwner) {
                        this.randomEnchantTracking$trackedTarget =
                                this.randomEnchantTracking$findOptimalTarget(arrow, livingOwner);
                    }
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;

        if (this.randomEnchantTracking$trackingLevel <= 0) return;
        if (arrow.onGround() || !arrow.isAlive() || arrow.isNoPhysics()) return;

        this.randomEnchantTracking$ticksInAir++;

        // 只在服务端执行追踪逻辑
        if (!arrow.level().isClientSide) {
            // 延迟开始追踪，让箭矢先稳定飞行
            if (this.randomEnchantTracking$ticksInAir < TRACKING_START_DELAY) {
                return;
            }

            // 检查当前速度是否足够
            double currentSpeed = arrow.getDeltaMovement().length();
            if (currentSpeed < MIN_SPEED_FOR_TRACKING) {
                this.randomEnchantTracking$isActivelyTracking = false;
                return;
            }

            // 更新或寻找目标
            Entity owner = arrow.getOwner();
            if (owner instanceof LivingEntity livingOwner) {
                this.randomEnchantTracking$updateTrackingTarget(arrow, livingOwner);
            }

            // 执行追踪
            if (this.randomEnchantTracking$isActivelyTracking &&
                    this.randomEnchantTracking$trackedTarget != null) {
                this.randomEnchantTracking$steerTowardsTarget(arrow);

                // 强制更新位置以确保平滑移动
                this.randomEnchantTracking$updateArrowPosition(arrow);
            }
        }

        // 客户端更新旋转
        this.randomEnchantTracking$updateArrowRotation(arrow);
    }

    @Unique
    private void randomEnchantTracking$updateTrackingTarget(AbstractArrow arrow, LivingEntity owner) {
        // 检查当前目标是否有效
        if (this.randomEnchantTracking$trackedTarget != null) {
            boolean targetValid = this.randomEnchantTracking$trackedTarget.isAlive() &&
                    !this.randomEnchantTracking$trackedTarget.isRemoved() &&
                    this.randomEnchantTracking$isInTrackingRange(arrow, this.randomEnchantTracking$trackedTarget);

            if (!targetValid) {
                this.randomEnchantTracking$trackedTarget = null;
                this.randomEnchantTracking$isActivelyTracking = false;
            } else {
                // 检查目标是否在视野范围内
                if (this.randomEnchantTracking$isTargetInSight(arrow, this.randomEnchantTracking$trackedTarget)) {
                    this.randomEnchantTracking$isActivelyTracking = true;
                } else {
                    // 如果目标不在视野内，尝试寻找新目标
                    this.randomEnchantTracking$trackedTarget =
                            this.randomEnchantTracking$findOptimalTarget(arrow, owner);
                    this.randomEnchantTracking$isActivelyTracking =
                            this.randomEnchantTracking$trackedTarget != null;
                }
            }
        } else {
            // 寻找新目标
            this.randomEnchantTracking$trackedTarget =
                    this.randomEnchantTracking$findOptimalTarget(arrow, owner);
            this.randomEnchantTracking$isActivelyTracking =
                    this.randomEnchantTracking$trackedTarget != null;
        }
    }

    @Unique
    @Nullable
    private LivingEntity randomEnchantTracking$findOptimalTarget(AbstractArrow arrow, LivingEntity owner) {
        if (owner.level() != arrow.level()) return null;

        double range = TRACKING_RANGE + (this.randomEnchantTracking$trackingLevel * 8.0);
        Vec3 arrowPos = arrow.position();
        Vec3 arrowDirection = arrow.getDeltaMovement().normalize();

        AABB searchArea = new AABB(
                arrowPos.x - range, arrowPos.y - range, arrowPos.z - range,
                arrowPos.x + range, arrowPos.y + range, arrowPos.z + range
        );

        List<LivingEntity> entities = owner.level().getEntitiesOfClass(
                LivingEntity.class, searchArea,
                entity -> this.randomEnchantTracking$isValidTarget(owner, entity, arrowPos, arrowDirection)
        );

        if (entities.isEmpty()) return null;

        // 使用加权评分系统选择最佳目标
        LivingEntity bestTarget = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (LivingEntity entity : entities) {
            double score = this.randomEnchantTracking$calculateTargetScore(arrow, entity, arrowDirection);
            if (score > bestScore) {
                bestScore = score;
                bestTarget = entity;
            }
        }

        return bestTarget;
    }

    @Unique
    private double randomEnchantTracking$calculateTargetScore(AbstractArrow arrow, LivingEntity target, Vec3 arrowDirection) {
        Vec3 arrowPos = arrow.position();
        Vec3 targetPos = target.getBoundingBox().getCenter();
        Vec3 toTarget = targetPos.subtract(arrowPos);

        // 距离分数（越近越好）
        double distance = toTarget.length();
        double distanceScore = 1.0 / (distance + 1.0);

        // 方向分数（越正前方越好）
        double dotProduct = arrowDirection.dot(toTarget.normalize());
        double directionScore = (dotProduct + 1.0) / 2.0; // 归一化到 0-1

        // 视线清晰度分数（检查是否有障碍物）
        double visibilityScore = 1.0;
        // 这里可以添加视线检测逻辑

        // 最终加权分数
        return distanceScore * 0.4 + directionScore * 0.5 + visibilityScore * 0.1;
    }

    @Unique
    private boolean randomEnchantTracking$isValidTarget(LivingEntity owner, LivingEntity entity, Vec3 arrowPos, Vec3 arrowDirection) {
        // 基础检查
        if (entity == owner) return false;
        if (!entity.isAlive() || entity.isRemoved()) return false;

        // 距离检查
        double range = TRACKING_RANGE + (this.randomEnchantTracking$trackingLevel * 8.0);
        double distance = arrowPos.distanceToSqr(entity.position());
        if (distance > range * range) return false;

        // 方向检查（必须在追踪角度内）
        Vec3 toTarget = entity.getBoundingBox().getCenter().subtract(arrowPos).normalize();
        double angle = Math.acos(arrowDirection.dot(toTarget));
        return angle <= MAX_TRACKING_ANGLE;
    }

    @Unique
    private boolean randomEnchantTracking$isInTrackingRange(AbstractArrow arrow, LivingEntity target) {
        double range = TRACKING_RANGE + (this.randomEnchantTracking$trackingLevel * 8.0);
        double distance = arrow.position().distanceToSqr(target.position());
        return distance <= range * range;
    }

    @Unique
    private boolean randomEnchantTracking$isTargetInSight(AbstractArrow arrow, LivingEntity target) {
        Vec3 arrowPos = arrow.position();
        Vec3 arrowDirection = arrow.getDeltaMovement().normalize();
        Vec3 toTarget = target.getBoundingBox().getCenter().subtract(arrowPos).normalize();

        double angle = Math.acos(arrowDirection.dot(toTarget));
        return angle <= MAX_TRACKING_ANGLE;
    }

    @Unique
    private void randomEnchantTracking$steerTowardsTarget(AbstractArrow arrow) {
        if (this.randomEnchantTracking$trackedTarget == null ||
                !this.randomEnchantTracking$trackedTarget.isAlive()) return;

        Vec3 arrowPos = arrow.position();
        Vec3 targetPos = this.randomEnchantTracking$trackedTarget.getBoundingBox().getCenter();

        // 预测目标位置（根据目标速度和箭矢速度）
        Vec3 targetMotion = this.randomEnchantTracking$trackedTarget.getDeltaMovement();
        double distance = arrowPos.distanceTo(targetPos);
        double arrowSpeed = arrow.getDeltaMovement().length();
        double timeToImpact = distance / (arrowSpeed + 0.1); // 防止除以零

        // 简单预测：假设目标保持当前速度移动
        Vec3 predictedPos = targetPos.add(targetMotion.scale(timeToImpact * 0.5));

        // 计算转向向量
        Vec3 toTarget = predictedPos.subtract(arrowPos);
        double distanceToTarget = toTarget.length();

        if (distanceToTarget < 1.0) return; // 非常接近时停止调整

        Vec3 desiredDirection = toTarget.normalize();
        Vec3 currentDirection = arrow.getDeltaMovement().normalize();

        // 计算转向角度
        double angle = Math.acos(Math.min(1.0, Math.max(-1.0, currentDirection.dot(desiredDirection))));

        // 根据附魔等级和距离计算转向强度
        double baseStrength = TRACKING_STRENGTH * this.randomEnchantTracking$trackingLevel;
        double distanceFactor = Math.min(1.0, 15.0 / distanceToTarget);
        double angleFactor = 1.0 - (angle / Math.PI); // 角度越大，转向越强

        double trackingStrength = baseStrength * distanceFactor * angleFactor;
        trackingStrength = Math.min(trackingStrength, MAX_TURN_RATE);

        // 应用转向
        Vec3 newDirection = currentDirection.lerp(desiredDirection, trackingStrength).normalize();
        double currentSpeed = arrow.getDeltaMovement().length();
        Vec3 newMotion = newDirection.scale(currentSpeed);

        arrow.setDeltaMovement(newMotion);
    }

    @Unique
    private void randomEnchantTracking$updateArrowPosition(AbstractArrow arrow) {
        // 获取当前速度和位置
        Vec3 motion = arrow.getDeltaMovement();
        Vec3 position = arrow.position();

        // 计算新位置
        Vec3 newPosition = position.add(motion);

        // 更新箭头位置
        arrow.setPos(newPosition.x, newPosition.y, newPosition.z);

        // 更新边界框
        arrow.setBoundingBox(arrow.getBoundingBox().move(motion));
    }

    @Unique
    private void randomEnchantTracking$updateArrowRotation(AbstractArrow arrow) {
        Vec3 motion = arrow.getDeltaMovement();
        if (motion.lengthSqr() < 0.0001) return;

        // 计算偏航角 (yaw)
        double horizontalDistance = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        float yaw = (float) (Math.atan2(motion.x, motion.z) * (180.0 / Math.PI));

        // 计算俯仰角 (pitch)
        float pitch = (float) (Math.atan2(motion.y, horizontalDistance) * (180.0 / Math.PI));

        // 平滑更新旋转
        float prevYaw = arrow.getYRot();
        float prevPitch = arrow.getXRot();

        // 使用插值平滑旋转变化
        float lerpFactor = 0.3f; // 插值因子
        float smoothedYaw = prevYaw + (yaw - prevYaw) * lerpFactor;
        float smoothedPitch = prevPitch + (pitch - prevPitch) * lerpFactor;

        arrow.setYRot(smoothedYaw);
        arrow.setXRot(smoothedPitch);

        // 保存上一帧的旋转
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
        // 箭矢即将消失时清理状态
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