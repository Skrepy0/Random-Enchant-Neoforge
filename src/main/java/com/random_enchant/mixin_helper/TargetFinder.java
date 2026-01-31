package com.random_enchant.mixin_helper;

import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TargetFinder {

    // 核心查找方法
    public static LivingEntity findOptimalTarget(Player player, double maxDistanceToPlayer, double maxRayOffset) {

        Vec3 eyePos = player.getEyePosition(1.0f);
        Vec3 lookVec = player.getViewVector(1.0f);
        Level level = player.level();

        // 1. 构建优化的检测区域（“双锥形”AABB）
        Vec3 searchCenter = eyePos.add(lookVec.scale(maxDistanceToPlayer * 0.5));
        AABB searchBox =
                new AABB(searchCenter, searchCenter).inflate(maxDistanceToPlayer * 0.8); // 适当缩小边界提高效率

        // 2. 获取预选实体列表
        List<LivingEntity> candidates =
                level.getEntitiesOfClass(LivingEntity.class, searchBox, entity -> isValidCandidate(entity, player));

        // 3. 初始化最佳目标记录
        LivingEntity bestTarget = null;
        double bestCost = Double.MAX_VALUE;
        double maxPlayerDistSqr = maxDistanceToPlayer * maxDistanceToPlayer;
        double maxRayOffsetSqr = maxRayOffset * maxRayOffset;

        // 4. 权重配置（可外部化到配置文件）
        double playerDistWeight = 0.4;
        double rayOffsetWeight = 0.6;

        // 5. 遍历并评估每个候选者
        for (LivingEntity entity: candidates) {
            Vec3 eyeToEntity = entity.position().subtract(eyePos);

            // 关键计算：投影和垂直距离（全部使用平方值优化）
            double projection = eyeToEntity.dot(lookVec);
            // 计算到射线的垂直距离平方
            Vec3 entityVector = eyeToEntity;
            double perpendicularDistSqr = entityVector.lengthSqr() - projection * projection;

            // 到玩家的距离平方
            double playerDistSqr = entity.distanceToSqr(player);

            // 双重约束检查
            if (playerDistSqr > maxPlayerDistSqr || perpendicularDistSqr > maxRayOffsetSqr) {
                continue; // 不满足基本条件，跳过
            }

            // 计算归一化距离（转换为0-1范围）
            double normPlayerDist = Math.sqrt(playerDistSqr) / maxDistanceToPlayer;
            double normRayDist = Math.sqrt(perpendicularDistSqr) / maxRayOffset;

            // 计算加权综合成本
            double cost = (playerDistWeight * normPlayerDist) + (rayOffsetWeight * normRayDist);

            // 成本补偿：优先考虑正前方的目标（投影为正）
            if (projection < 0) {
                cost += 1.0; // 显著惩罚玩家后方的目标
            }

            // 更新最佳目标
            if (cost < bestCost) {
                bestCost = cost;
                bestTarget = entity;
            }
        }

        // 6. 返回结果（可为null）
        return bestTarget;
    }

    // 验证候选者有效性
    private static boolean isValidCandidate(LivingEntity entity, Player player) {
        return entity != player && entity.isAlive() && entity.isAttackable() && !entity.isAlliedTo(player);
    }
}
