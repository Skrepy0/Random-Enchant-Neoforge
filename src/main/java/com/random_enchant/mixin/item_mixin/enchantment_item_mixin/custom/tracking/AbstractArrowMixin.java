package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.tracking;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Monster;
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

    // ========== 核心参数 ==========
    @Unique
    private static final double SEEK_DISTANCE = 8.0;
    @Unique
    private static final double SEEK_FACTOR = 0.8;
    @Unique
    private static final double SEEK_ANGLE = Math.PI / 6.0;
    @Unique
    private static final double SEEK_THRESHOLD = 0.5;
    @Unique
    private static final double Y_OFFSET = 0.045;
    @Unique
    private static final double MIN_SPEED_FOR_TRACKING = 0.1;
    @Unique
    private static final int SEARCH_COOLDOWN = 3;
    @Unique
    private static final float ROTATION_LERP_FACTOR = 0.8f;
    @Unique
    private static final double BASE_TRACKING_RANGE = 15.0;
    @Unique
    private static final double RAYCAST_RANGE = 100.0; // 射线检测最大范围

    @Inject(method = "shoot(DDDFF)V", at = @At("RETURN"))
    private void onShoot(double x, double y, double z, float velocity, float inaccuracy, CallbackInfo ci) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        if (!arrow.level().isClientSide) {
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

        if (arrow.level().isClientSide() && randomEnchantTracking$isActivelyTracking) {
            for (int i = 0; i < 2; ++i) {
                double offsetX = arrow.getDeltaMovement().x() * i / 4.0D;
                double offsetY = arrow.getDeltaMovement().y() * i / 4.0D;
                double offsetZ = arrow.getDeltaMovement().z() * i / 4.0D;
                arrow.level().addParticle(
                        ParticleTypes.GLOW,
                        arrow.getX() + offsetX,
                        arrow.getY() + offsetY,
                        arrow.getZ() + offsetZ,
                        -arrow.getDeltaMovement().x(),
                        -arrow.getDeltaMovement().y() + 0.2D,
                        -arrow.getDeltaMovement().z()
                );
            }
        }

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
                randomEnchantTracking$steerTowardsTargetSeekerStyle(arrow);
            }
        }

        randomEnchantTracking$updateArrowRotation(arrow);
    }

    @Unique
    private void randomEnchantTracking$updateTrackingTarget(AbstractArrow arrow, LivingEntity owner) {
        boolean needSearch = randomEnchantTracking$ticksSinceLastSearch >= SEARCH_COOLDOWN ||
                randomEnchantTracking$trackedTarget == null ||
                !randomEnchantTracking$trackedTarget.isAlive() ||
                !randomEnchantTracking$isInTrackingRange(arrow, randomEnchantTracking$trackedTarget);

        if (needSearch) {
            randomEnchantTracking$ticksSinceLastSearch = 0;
            randomEnchantTracking$trackedTarget = randomEnchantTracking$findOptimalTarget(arrow, owner);
            randomEnchantTracking$isActivelyTracking = randomEnchantTracking$trackedTarget != null;
        } else if (randomEnchantTracking$trackedTarget != null) {
            Vec3 motionVec = arrow.getDeltaMovement().normalize();
            Vec3 targetVec = randomEnchantTracking$getVectorToTarget(arrow, randomEnchantTracking$trackedTarget).normalize();
            double dotProduct = motionVec.dot(targetVec);

            if (dotProduct < SEEK_THRESHOLD) {
                randomEnchantTracking$trackedTarget = null;
                randomEnchantTracking$isActivelyTracking = false;
            } else {
                randomEnchantTracking$isActivelyTracking = true;
            }
        }
    }

    @Nullable
    private LivingEntity randomEnchantTracking$findOptimalTarget(AbstractArrow arrow, LivingEntity owner) {
        if (owner.level() != arrow.level()) return null;

        // ========== 第一步：优先执行50格射线检测（贴合参考逻辑） ==========
        LivingEntity raycastTarget = randomEnchantTracking$raycastForTarget(arrow, owner);
        if (raycastTarget != null) {
            return raycastTarget;
        }

        // ========== 第二步：原AABB范围搜索逻辑（射线未命中时执行） ==========
        AABB positionBB = arrow.getBoundingBox();
        AABB targetBB = positionBB;

        Vec3 courseVec = arrow.getDeltaMovement().scale(SEEK_DISTANCE).yRot((float) SEEK_ANGLE);
        targetBB = targetBB.minmax(positionBB.move(courseVec));

        courseVec = arrow.getDeltaMovement().scale(SEEK_DISTANCE).yRot((float) -SEEK_ANGLE);
        targetBB = targetBB.minmax(positionBB.move(courseVec));

        targetBB = targetBB.inflate(0, SEEK_DISTANCE * 0.5, 0);
        double levelRange = randomEnchantTracking$trackingLevel * 3.0;
        targetBB = targetBB.inflate(levelRange);

        List<LivingEntity> candidates = owner.level().getEntitiesOfClass(LivingEntity.class, targetBB,
                e -> e != owner && e.isAlive() && !e.is(arrow));

        // 优先级1：攻击主人的怪物
        for (LivingEntity e : candidates) {
            if (e instanceof Monster monster && monster.getTarget() == owner) {
                return monster;
            }
        }

        // 优先级2：有视线的怪物（改用参考逻辑的视线检测）
        for (LivingEntity e : candidates) {
            if (e instanceof Monster && randomEnchantTracking$checkLineOfSight(arrow, e)) {
                return e;
            }
        }

        // 优先级3：方向匹配度最高的有效目标
        Vec3 arrowDir = arrow.getDeltaMovement().normalize();
        double bestDot = SEEK_THRESHOLD;
        LivingEntity bestTarget = null;

        for (LivingEntity e : candidates) {
            if (e == owner) continue;
            if (e instanceof TamableAnimal tame && tame.getOwner() == owner) continue;
            if (!randomEnchantTracking$checkLineOfSight(arrow, e)) continue;

            Vec3 toTarget = randomEnchantTracking$getVectorToTarget(arrow, e).normalize();
            double dot = arrowDir.dot(toTarget);

            if (dot > bestDot) {
                bestDot = dot;
                bestTarget = e;
            }
        }

        return bestTarget;
    }

    // ========== 核心修正：完全贴合参考逻辑的视线检测 ==========
    @Unique
    private boolean randomEnchantTracking$checkLineOfSight(AbstractArrow arrow, LivingEntity target) {
        // 1. 定义射线起点（箭矢眼睛位置）和终点（目标眼睛位置）
        Vec3 startPos = arrow.position().add(0, arrow.getEyeHeight(), 0);
        Vec3 endPos = target.getEyePosition();
        double distance = startPos.distanceTo(endPos);

        // 超出50格范围直接返回无视线
        if (distance > RAYCAST_RANGE) return false;

        // 2. 参考逻辑：先做方块射线检测
        HitResult blockHitResult = arrow.level().clip(
                new ClipContext(startPos, endPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, arrow)
        );

        // 3. 如果方块检测命中（视线被阻挡），直接返回false
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            return false;
        }

        // 4. 参考逻辑：方块未命中时，做实体射线检测
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                arrow.level(),          // 世界
                arrow,                  // 发射者（箭矢自身）
                startPos,               // 起点
                endPos,                 // 终点
                new AABB(startPos, endPos).inflate(1.0), // 搜索范围（扩1格）
                entity -> {             // 过滤条件（完全参考你的逻辑）
                    return entity != arrow &&        // 不检测自己
                            entity.isAlive() &&       // 实体存活
                            entity.isPickable() &&    // 可被击中
                            !entity.isSpectator() &&  // 非旁观者
                            entity == target;         // 仅检测目标实体
                },
                0.0F                    // 距离阈值
        );

        // 5. 实体检测命中目标 → 视线畅通
        return entityHitResult != null && entityHitResult.getEntity() == target;
    }

    // ========== 修正：50格射线检测（贴合参考逻辑） ==========
    @Unique
    @Nullable
    private LivingEntity randomEnchantTracking$raycastForTarget(AbstractArrow arrow, LivingEntity owner) {
        // 计算射线起点（箭矢位置）和终点（沿运动方向50格）
        Vec3 rayStart = arrow.position();
        Vec3 rayDir = arrow.getDeltaMovement().normalize();
        Vec3 rayEnd = rayStart.add(rayDir.scale(RAYCAST_RANGE));

        // 第一步：参考逻辑做方块射线检测
        HitResult blockHit = arrow.level().clip(
                new ClipContext(rayStart, rayEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, arrow)
        );
        // 如果方块命中，且命中点在50格内 → 射线被阻挡，无目标
        if (blockHit.getType() == HitResult.Type.BLOCK && blockHit.getLocation().distanceTo(rayStart) < RAYCAST_RANGE) {
            return null;
        }

        // 第二步：方块未阻挡时，做实体射线检测（参考你的逻辑）
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                arrow.level(),
                arrow,
                rayStart,
                rayEnd,
                new AABB(rayStart, rayEnd).inflate(1.0),
                entity -> {
                    // 过滤条件：非主人、非宠物、存活、可被击中、非旁观者
                    if (entity == owner || !entity.isAlive() || !entity.isPickable() || entity.isSpectator()) {
                        return false;
                    }
                    if (entity instanceof TamableAnimal tame && tame.getOwner() == owner) {
                        return false;
                    }
                    return entity instanceof LivingEntity; // 仅检测生物
                },
                0.0F
        );

        // 命中有效生物 → 返回该目标
        if (entityHitResult != null && entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
            return livingEntity;
        }

        // 无命中 → 返回null（走原AABB逻辑）
        return null;
    }

    @Unique
    private void randomEnchantTracking$steerTowardsTargetSeekerStyle(AbstractArrow arrow) {
        if (randomEnchantTracking$trackedTarget == null) return;

        Vec3 targetVec = randomEnchantTracking$getVectorToTarget(arrow, randomEnchantTracking$trackedTarget).scale(SEEK_FACTOR);
        Vec3 courseVec = arrow.getDeltaMovement();

        double courseLen = courseVec.length();
        double targetLen = targetVec.length();
        double totalLen = Math.sqrt(courseLen * courseLen + targetLen * targetLen);

        double dotProduct = courseVec.dot(targetVec) / (courseLen * targetLen);

        if (dotProduct > SEEK_THRESHOLD) {
            Vec3 newMotion = courseVec.scale(courseLen / totalLen)
                    .add(targetVec.scale(courseLen / totalLen))
                    .add(0, Y_OFFSET, 0);

            arrow.setDeltaMovement(newMotion);
            arrow.hasImpulse = true;
        } else {
            randomEnchantTracking$trackedTarget = null;
            randomEnchantTracking$isActivelyTracking = false;
        }
    }

    @Unique
    private boolean randomEnchantTracking$isInTrackingRange(AbstractArrow arrow, LivingEntity target) {
        double range = BASE_TRACKING_RANGE + randomEnchantTracking$trackingLevel * 2.0;
        return arrow.position().distanceToSqr(target.position()) <= range * range;
    }

    @Unique
    private Vec3 randomEnchantTracking$getVectorToTarget(AbstractArrow arrow, Entity target) {
        return new Vec3(
                target.getX() - arrow.getX(),
                (target.getY() + target.getEyeHeight()) - arrow.getY(),
                target.getZ() - arrow.getZ()
        );
    }

    @Unique
    private void randomEnchantTracking$updateArrowRotation(AbstractArrow arrow) {
        Vec3 motion = arrow.getDeltaMovement();
        if (motion.lengthSqr() < 0.0001) return;

        double horizontalDistance = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        float yaw = (float) (Math.atan2(motion.x, motion.z) * (180.0 / Math.PI));
        float pitch = (float) (Math.atan2(motion.y, horizontalDistance) * (180.0 / Math.PI));

        float smoothedYaw = arrow.getYRot() + (yaw - arrow.getYRot()) * ROTATION_LERP_FACTOR;
        float smoothedPitch = arrow.getXRot() + (pitch - arrow.getXRot()) * ROTATION_LERP_FACTOR;

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