package com.random_enchant.mixin.enchantment_block_mixin.custom.badluckofthesea;

import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowingFluid.class)
public abstract class FlowableFluidMixin {

    @Inject(at = @At("HEAD"), method = "canPassThroughWall", cancellable = true)
    private void init1(Direction face, BlockGetter world, BlockPos pos, BlockState state, BlockPos fromPos,
                       BlockState fromState, CallbackInfoReturnable<Boolean> cir) {

        int k = BlockEnchantmentStorage.getLevel(ModEnchantments.BAD_LUCK_OF_THE_SEA, pos);
        if (k > 0) {
            // 获取当前方块的世界对象，必须确保world是World类型
            if (world instanceof Level mutableWorld) {
                // 破坏方块
                randomEnchant$generateFallingBlock(pos, state, mutableWorld);
            }
            cir.setReturnValue(true);
        }
    }


    @Unique
    private void randomEnchant$generateFallingBlock(BlockPos targetPos, BlockState blockState, Level world) {
        if (!world.isClientSide()) {
            BlockEntity blockEntity = world.getBlockEntity(targetPos);

            // 获取原始位置的附魔信息
            ListTag enchantments = BlockEnchantmentStorage.getEnchantmentsAtPosition(targetPos);

            if (!Objects.equals(enchantments, new ListTag())) {
                // 删除信息
                BlockEnchantmentStorage.removeBlockEnchantment(targetPos.immutable());
            }

            FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, world);

            fallingBlockEntity.blockState = blockState;
            fallingBlockEntity.time = 1;
            fallingBlockEntity.setNoGravity(false);
            fallingBlockEntity.blocksBuilding = true;
            fallingBlockEntity.setPos(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
            fallingBlockEntity.setDeltaMovement(0, 0.2, 0);
            fallingBlockEntity.xOld = targetPos.getX() + 0.5;
            fallingBlockEntity.yOld = targetPos.getY();
            fallingBlockEntity.zOld = targetPos.getZ() + 0.5;
            fallingBlockEntity.setStartPos(targetPos);
            fallingBlockEntity.hasImpulse = true;
            // 设置伤害
            fallingBlockEntity.setHurtsEntities(0, -1);

            // 保存附魔信息到实体数据中
            if (enchantments != null && !enchantments.isEmpty()) {
                CompoundTag entityData = fallingBlockEntity.getPersistentData();
                entityData.put("BlockEnchantments", enchantments);
            }

            // 如果方块有附加的 BlockEntity 数据，可以设置 blockEntityData 字段
            if (blockEntity != null) {
                CompoundTag blockEntityData = new CompoundTag();
                BlockEntityReflectionHelper.invokeSaveAdditionalSafe(blockEntity, blockEntityData,
                                                                     world.registryAccess());
                fallingBlockEntity.blockData = blockEntityData;
            }

            world.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);

            world.addFreshEntity(fallingBlockEntity);
        }
    }
}
