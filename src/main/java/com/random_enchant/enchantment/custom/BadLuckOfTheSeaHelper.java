package com.random_enchant.enchantment.custom;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class BadLuckOfTheSeaHelper {
    private static final List<BlockPos> SEARCH_PATTERN = createSpiralSearchPattern(20);

    private static List<BlockPos> createSpiralSearchPattern(int radius) {
        List<BlockPos> pattern = new ArrayList<>();

        // 创建从近到远的螺旋搜索模式
        for (int r = 0; r <= radius; r++) {
            // 添加当前半径层的所有边界点
            for (int x = -r; x <= r; x++) {
                pattern.add(new BlockPos(x, 0, -r));
                pattern.add(new BlockPos(x, 0, r));
            }
            for (int z = -r + 1; z < r; z++) {
                pattern.add(new BlockPos(-r, 0, z));
                pattern.add(new BlockPos(r, 0, z));
            }
        }

        return pattern;
    }

    public static void entityWithBadLuckOfTheSea(Entity entity, int lvl) {
        Level level = entity.level();
        BlockPos entityPos = entity.blockPosition();

        // 快速检查
        if (!level.getFluidState(entityPos).is(FluidTags.WATER)) {
            return;
        }

        double upwardForce = 0.3 * lvl;
        double horizontalSpeed = 0.34 * lvl;

        // 使用预计算的搜索模式
        BlockPos closestPos = null;
        double closestDistanceSq = Double.MAX_VALUE;

        for (BlockPos offset: SEARCH_PATTERN) {
            BlockPos checkPos = entityPos.offset(offset);

            if (!level.getFluidState(checkPos).is(FluidTags.WATER)) {
                double distanceSq = offset.distSqr(BlockPos.ZERO);

                if (distanceSq < closestDistanceSq) {
                    closestDistanceSq = distanceSq;
                    closestPos = checkPos;

                    // 如果找到足够近的方块，可以提前退出
                    if (distanceSq < 4) { // 2格以内
                        break;
                    }
                }
            }

            // 如果距离已经超过当前最近距离，可以跳过后续更远的搜索
            if (closestPos != null && offset.distSqr(BlockPos.ZERO) > closestDistanceSq + 10) {
                break;
            }
        }

        // 应用速度
        Vec3 currentVelocity = entity.getDeltaMovement();
        Vec3 newVelocity = currentVelocity.add(0, upwardForce, 0);

        if (closestPos != null) {
            Vec3 direction = Vec3.atCenterOf(closestPos).subtract(entity.position()).normalize().scale(horizontalSpeed);
            newVelocity = newVelocity.add(direction);
        }

        entity.setDeltaMovement(newVelocity);
    }

    public static void thrownTridentEntityWithBadLuckOfTheSea(Entity entity, double lvl) {
        BlockPos closestNonLiquidBlockPos = null;
        double closestDistanceSq = Double.MAX_VALUE; // 初始设置为最大值
        BlockPos blockPos = entity.blockPosition();
        Level world = entity.level();
        for (int xOffset = -20; xOffset <= 19; xOffset++) {
            for (int zOffset = -20; zOffset <= 19; zOffset++) {
                BlockPos currentPos = blockPos.offset(xOffset, 0, zOffset);
                FluidState fluidState1 = world.getFluidState(currentPos);

                // 检查当前方块是否不是液体方块
                if (!fluidState1.is(FluidTags.WATER)) {
                    double distanceSq = entity.distanceToSqr(Vec3.atCenterOf(currentPos));

                    // 如果当前方块更近，则更新最近的非液体方块信息
                    if (distanceSq < closestDistanceSq) {
                        closestDistanceSq = distanceSq;
                        closestNonLiquidBlockPos = currentPos;
                    }
                }
            }
        }

        if (closestNonLiquidBlockPos != null) {
            // 计算方向向量
            Vec3 direction = Vec3.atCenterOf(closestNonLiquidBlockPos).subtract(entity.position()).normalize();

            double speed = 0.5; // 设定速度大小（可以根据需要调整）

            // 计算最终的速度向量
            Vec3 velocity = direction.scale(speed);

            entity.push(0, 1, 0);
            entity.addDeltaMovement(velocity); // 应用速度
        }
    }
}
