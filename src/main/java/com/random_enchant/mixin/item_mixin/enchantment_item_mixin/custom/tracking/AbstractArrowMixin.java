package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.tracking;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    @Unique @Nullable private LivingEntity randomEnchantTracking$trackedTarget;
    @Unique private int randomEnchantTracking$trackingLevel = 1;
    @Unique private int randomEnchantTracking$ticksSinceLastSearch = 0;
    @Unique private boolean randomEnchantTracking$isActivelyTracking = false;

    @Unique private static final double TRACKING_RANGE = 30.0; // 最大追踪距离（方块）
    @Unique private static final double MAX_TRACKING_ANGLE = Math.PI / 2; // 最大追踪角度（90度），超出则目标可能丢失
    @Unique private static final double MIN_SPEED_FOR_TRACKING = 0.2; // 启用追踪所需的最小箭矢速度（米/刻）
    @Unique private static final double BASE_TURN_RATE = 0.8; // 基础转向速率（弧度/刻），受等级和角度动态调整
    @Unique private static final double MAX_TURN_RATE = 0.8; // 最大转向速率限制（弧度/刻），防止瞬间转向
    @Unique private static final int SEARCH_COOLDOWN = 5; // 目标搜索的冷却间隔（刻），避免每刻都进行昂贵的范围查询
    @Unique private static final double PREDICTION_FACTOR_BASE = 0.3; // 目标位置预测的基础系数，影响预测偏移量
    @Unique private static final double PREDICTION_FACTOR_PER_LEVEL = 0.1; // 每级附魔增加的预测系数，高等级预判更准

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
                    // 可以选择立即搜索目标，或延迟到第一个 tick 再搜索
                    Entity owner = arrow.getOwner();
                    if (owner instanceof LivingEntity livingOwner) {
                        this.randomEnchantTracking$trackedTarget =
                                this.randomEnchantTracking$findOptimalTarget(arrow, livingOwner);
                        this.randomEnchantTracking$isActivelyTracking =
                                (this.randomEnchantTracking$trackedTarget != null);
                    }
                }
            } else {
                // 如果没有武器，等级设为0
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

        // 1. 射线直接击中（考虑方块阻挡）
        HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(
                owner, entity -> entity instanceof LivingEntity && entity != owner && entity.isAlive(), TRACKING_RANGE);
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            return (LivingEntity) ((EntityHitResult) hitResult).getEntity();
        }

        double range = TRACKING_RANGE + randomEnchantTracking$trackingLevel * 8.0;
        Vec3 eyePos = owner.getEyePosition();
        Vec3 lookVec = owner.getLookAngle();

        // 获取所有可能的候选实体（排除自己）
        AABB searchArea = owner.getBoundingBox().inflate(range);
        List<LivingEntity> candidates =
                owner.level().getEntitiesOfClass(LivingEntity.class, searchArea, e -> e != owner && e.isAlive());

        // 2. 距离视线射线最近的实体（忽略阻挡）
        LivingEntity closestToRay = null;
        double minDistToRay = Double.MAX_VALUE;
        for (LivingEntity e: candidates) {
            Vec3 toEntity = e.position().subtract(eyePos);
            double dot = toEntity.dot(lookVec);
            if (dot <= 0) continue; // 在身后

            double distToRay = toEntity.cross(lookVec).length();
            if (distToRay < minDistToRay) {
                minDistToRay = distToRay;
                closestToRay = e;
            }
        }
        if (closestToRay != null) return closestToRay;

        // 3. 加权评分（基于距离、方向、可见性）
        Vec3 arrowPos = arrow.position();
        Vec3 arrowDir = arrow.getDeltaMovement().normalize();

        LivingEntity best = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (LivingEntity e: candidates) {
            // 快速过滤：距离必须在范围内
            double distSq = arrowPos.distanceToSqr(e.position());
            if (distSq > range * range) continue;

            double score = randomEnchantTracking$calculateTargetScore(arrow, e, arrowDir);
            if (score > bestScore) {
                bestScore = score;
                best = e;
            }
        }
        return best;
    }
    @Unique
    private double randomEnchantTracking$calculateTargetScore(AbstractArrow arrow, LivingEntity target, Vec3 arrowDir) {
        Vec3 arrowPos = arrow.position();
        Vec3 targetPos = target.getBoundingBox().getCenter();
        Vec3 toTarget = targetPos.subtract(arrowPos);
        double distance = toTarget.length();
        toTarget = toTarget.normalize();

        // 距离分数（越近越好）
        double distanceScore = 1.0 / (distance + 1.0);

        // 方向分数（越正对越好）
        double dot = arrowDir.dot(toTarget);
        double directionScore = (dot + 1.0) / 2.0; // [0,1]

        // 可见性分数（简单射线检测，检查是否有方块阻挡）
        double visibilityScore = 1.0;
        if (!arrow.level().isClientSide) {
            HitResult blockHit = arrow.level().clip(
                    new ClipContext(arrowPos, targetPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, arrow));
            if (blockHit.getType() == HitResult.Type.BLOCK &&
                blockHit.getLocation().distanceToSqr(arrowPos) < targetPos.distanceToSqr(arrowPos) - 0.5) {
                visibilityScore = 0.5; // 被阻挡则减分
            }
        }

        // 加权求和（方向为主，距离次之，可见性辅助）
        return directionScore * 0.7 + distanceScore * 0.2 + visibilityScore * 0.1;
    }
    @Unique
    private boolean randomEnchantTracking$isValidTarget(LivingEntity owner, LivingEntity entity, Vec3 arrowPos,
                                                        Vec3 arrowDirection) {
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

        // 预测目标位置
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
        double angleFactor = (angleRad / Math.PI) * 2.0; // 角度越大转向越猛
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

        // 保持速度大小
        double currentSpeed = arrow.getDeltaMovement().length();
        arrow.setDeltaMovement(newDir.scale(currentSpeed));
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
