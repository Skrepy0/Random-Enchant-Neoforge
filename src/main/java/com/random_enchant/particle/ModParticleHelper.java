package com.random_enchant.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;

public class ModParticleHelper {
    private ModParticleHelper() {
    }

    public static void addParticlesOnBlock(BlockPos blockPos, SimpleParticleType particleType) {
        // 在方块顶部创建粒子效果
        Minecraft.getInstance().level.addParticle(particleType,
                blockPos.getX() + 0.5,
                blockPos.getY() + 1.1,
                blockPos.getZ() + 0.5,
                0.0, 0.0, 0.0);

        // 在方块底部创建粒子效果
        Minecraft.getInstance().level.addParticle(particleType,
                blockPos.getX() + 0.5,
                blockPos.getY() - 0.1,
                blockPos.getZ() + 0.5,
                0.0, 0.0, 0.0);

        // 在方块北侧创建粒子效果
        Minecraft.getInstance().level.addParticle(particleType,
                blockPos.getX() + 0.5,
                blockPos.getY() + 0.5,
                blockPos.getZ() - 0.1,
                0.0, 0.0, 0.0);

        // 在方块南侧创建粒子效果
        Minecraft.getInstance().level.addParticle(particleType,
                blockPos.getX() + 0.5,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 1.1,
                0.0, 0.0, 0.0);

        // 在方块西侧创建粒子效果
        Minecraft.getInstance().level.addParticle(particleType,
                blockPos.getX() - 0.1,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 0.5,
                0.0, 0.0, 0.0);

        // 在方块东侧创建粒子效果
        Minecraft.getInstance().level.addParticle(particleType,
                blockPos.getX() + 1.1,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 0.5,
                0.0, 0.0, 0.0);
    }
}
