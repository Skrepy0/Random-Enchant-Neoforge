package com.random_enchant.mixin.enchantment_block_mixin.custom.badluckofthesea;

import com.llamalad7.mixinextras.sugar.Local;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import com.random_enchant.network.packet.S2C.EntityVelocityUpdateS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Mafuyu33
 * &#064;description  优化后的下落方块实体 Mixin
 */
@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

    // 配置常量
    @Unique
    private static final int SEARCH_RADIUS = 20;
    @Unique
    private static final double UPWARD_VELOCITY = 0.3;
    @Unique
    private static final double HORIZONTAL_SPEED = 0.15;
    @Unique
    private static final double VELOCITY_CHANGE_THRESHOLD = 0.001;

    @Shadow
    private int fallDamageMax;

    @Unique
    private Vec3 randomEnchant$lastSentVelocity = Vec3.ZERO;
    @Unique
    private int randomEnchant$searchCooldown = 0;
    @Unique
    private BlockPos randomEnchant$lastClosestPos = null;

    public FallingBlockEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * 优化搜索最近的非水方块位置
     * 使用缓存和优化算法减少计算量
     */
    @Unique
    private BlockPos randomEnchant$findClosestNonWaterBlock(BlockPos centerPos) {
        Level level = this.level();

        // 使用缓存的搜索结果（如果最近检查过且位置未变化太多）
        if (randomEnchant$lastClosestPos != null && randomEnchant$searchCooldown > 0) {
            randomEnchant$searchCooldown--;
            double distanceSq = centerPos.distSqr(randomEnchant$lastClosestPos);
            if (distanceSq < 4 && !level.getFluidState(randomEnchant$lastClosestPos).is(FluidTags.WATER)) {
                return randomEnchant$lastClosestPos;
            }
        }

        BlockPos closestPos = null;
        double closestDistanceSq = Double.MAX_VALUE;

        // 优化搜索：使用半径逐渐增大的螺旋搜索
        for (int radius = 1; radius <= SEARCH_RADIUS; radius++) {
            boolean foundInThisRadius = false;

            // 搜索当前半径的方形边界
            for (int xOffset = -radius; xOffset <= radius; xOffset += radius * 2) {
                for (int zOffset = -radius; zOffset <= radius; zOffset++) {
                    BlockPos currentPos = centerPos.offset(xOffset, 0, zOffset);
                    if (!level.getFluidState(currentPos).is(FluidTags.WATER)) {
                        double distanceSq = centerPos.distSqr(currentPos);
                        if (distanceSq < closestDistanceSq) {
                            closestDistanceSq = distanceSq;
                            closestPos = currentPos;
                            foundInThisRadius = true;
                        }
                    }
                }
            }

            for (int zOffset = -radius; zOffset <= radius; zOffset += radius * 2) {
                for (int xOffset = -radius + 1; xOffset < radius; xOffset++) {
                    BlockPos currentPos = centerPos.offset(xOffset, 0, zOffset);
                    if (!level.getFluidState(currentPos).is(FluidTags.WATER)) {
                        double distanceSq = centerPos.distSqr(currentPos);
                        if (distanceSq < closestDistanceSq) {
                            closestDistanceSq = distanceSq;
                            closestPos = currentPos;
                            foundInThisRadius = true;
                        }
                    }
                }
            }

            // 如果在这个半径找到了，就不需要搜索更大的半径了
            if (foundInThisRadius) {
                break;
            }
        }

        // 缓存结果
        if (closestPos != null) {
            randomEnchant$lastClosestPos = closestPos;
            randomEnchant$searchCooldown = 20; // 缓存10 tick（0.5秒）
        }

        return closestPos;
    }

    /**
     * 优化网络包发送：只在速度变化足够大时发送
     */
    @Unique
    private void randomEnchant$updateVelocityWithNetworkSync(Vec3 newVelocity) {
        Vec3 currentMotion = this.getDeltaMovement();
        Vec3 finalMotion = currentMotion.add(newVelocity);

        // 只在速度变化超过阈值时发送网络包
        if (finalMotion.distanceToSqr(randomEnchant$lastSentVelocity) > VELOCITY_CHANGE_THRESHOLD * VELOCITY_CHANGE_THRESHOLD) {
            this.setDeltaMovement(finalMotion);
            PacketDistributor.sendToAllPlayers(new EntityVelocityUpdateS2CPacket(this.getId(), finalMotion));
            randomEnchant$lastSentVelocity = finalMotion;
        } else {
            // 本地设置速度，不发送网络包
            this.setDeltaMovement(finalMotion);
        }
    }

    /**
     * 主要 tick 逻辑，优化性能
     */
    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo info) {
        Level level = this.level();

        // 只在服务器端处理
        if (level.isClientSide) return;

        // 只在特殊条件下处理（fallDamageMax == -1 且在水里）
        if (this.fallDamageMax != -1) return;

        BlockPos blockPos = this.blockPosition();
        FluidState fluidState = level.getFluidState(blockPos);

        if (!fluidState.is(FluidTags.WATER)) return;

        // 查找最近的非水方块
        BlockPos closestNonWaterPos = randomEnchant$findClosestNonWaterBlock(blockPos);

        // 计算速度
        Vec3 velocity = Vec3.ZERO;

        if (closestNonWaterPos != null) {
            // 计算朝向最近非水方块的方向
            Vec3 direction = Vec3.atCenterOf(closestNonWaterPos)
                    .subtract(this.position())
                    .normalize();
            velocity = direction.scale(HORIZONTAL_SPEED);
        }

        // 添加向上的速度分量
        velocity = velocity.add(0, UPWARD_VELOCITY, 0);

        // 更新速度（带网络同步优化）
        randomEnchant$updateVelocityWithNetworkSync(velocity);
    }

    /**
     * 方块落地时添加附魔
     */
    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
            )
    )
    public void onBlockLand(CallbackInfo info, @Local BlockPos blockPos) {
        // 只在服务器端处理特殊方块
        if (this.level().isClientSide || this.fallDamageMax != -1) return;

        ResourceKey<Enchantment> enchantment = ModEnchantments.BAD_LUCK_OF_THE_SEA;

        // 创建附魔 NBT
        CompoundTag enchantmentNbt = new CompoundTag();
        enchantmentNbt.putString("id", enchantment.location().toString());
        enchantmentNbt.putInt("lvl", 1);

        // 创建列表并添加附魔
        ListTag enchantmentNbtList = new ListTag();
        enchantmentNbtList.add(enchantmentNbt);

        // 存储到方块附魔存储中
        BlockEnchantmentStorage.addBlockEnchantment(blockPos.immutable(), enchantmentNbtList);
    }

    /**
     * 实体被移除时清理资源
     */
    @Override
    public void remove(RemovalReason reason) {
        // 清理缓存
        randomEnchant$lastClosestPos = null;
        randomEnchant$lastSentVelocity = Vec3.ZERO;
        randomEnchant$searchCooldown = 0;

        super.remove(reason);
    }
}