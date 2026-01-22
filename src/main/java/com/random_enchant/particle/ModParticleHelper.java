package com.random_enchant.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ModParticleHelper {
    private ModParticleHelper() {}

    public static void addParticlesOnBlock(BlockPos blockPos, SimpleParticleType particleType) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        // 检查每个面是否被遮挡
        // 顶部
        if (!isSideBlocked(level, blockPos, Direction.UP)) {
            level.addParticle(particleType, blockPos.getX() + 0.5, blockPos.getY() + 1.1, blockPos.getZ() + 0.5, 0.0,
                              0.0, 0.0);
        }

        // 底部
        if (!isSideBlocked(level, blockPos, Direction.DOWN)) {
            level.addParticle(particleType, blockPos.getX() + 0.5, blockPos.getY() - 0.1, blockPos.getZ() + 0.5, 0.0,
                              0.0, 0.0);
        }

        // 北侧
        if (!isSideBlocked(level, blockPos, Direction.NORTH)) {
            level.addParticle(particleType, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() - 0.1, 0.0,
                              0.0, 0.0);
        }

        // 南侧
        if (!isSideBlocked(level, blockPos, Direction.SOUTH)) {
            level.addParticle(particleType, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 1.1, 0.0,
                              0.0, 0.0);
        }

        // 西侧
        if (!isSideBlocked(level, blockPos, Direction.WEST)) {
            level.addParticle(particleType, blockPos.getX() - 0.1, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0.0,
                              0.0, 0.0);
        }

        // 东侧
        if (!isSideBlocked(level, blockPos, Direction.EAST)) {
            level.addParticle(particleType, blockPos.getX() + 1.1, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0.0,
                              0.0, 0.0);
        }
    }

    /**
     * 检查方块的某个面是否被其他方块遮挡
     * @param level 世界实例
     * @param blockPos 方块位置
     * @param direction 要检查的方向
     * @return 如果该面被遮挡返回true，否则返回false
     */
    private static boolean isSideBlocked(Level level, BlockPos blockPos, Direction direction) {
        BlockPos adjacentPos = blockPos.relative(direction);
        BlockState adjacentState = level.getBlockState(adjacentPos);

        // 如果相邻方块不透明且能完全遮挡视线，则认为该面被遮挡
        return adjacentState.isSolidRender(level, adjacentPos);
    }
}
