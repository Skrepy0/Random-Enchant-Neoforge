package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.luoyangshovel;

import com.random_enchant.Config;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import com.random_enchant.mixin.enchantment_block_mixin.custom.badluckofthesea.BlockEntityReflectionHelper;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin extends DiggerItem {
    public ShovelItemMixin(Tier tier, TagKey<Block> blocks, Properties properties) { super(tier, blocks, properties); }

    @Unique
    private static void randomEnchant$launchBlock(BlockPos targetPos, int power, Player user,
                                                  FallingBlockEntity fallingBlockEntity) {
        // 获取用户的位置
        Vec3 userPos = user.position();

        // 获取目标位置并转换为方块中心的浮点坐标
        Vec3 targetVec = new Vec3(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5);

        // 计算指向目标位置的向量
        Vec3 direction = targetVec.subtract(userPos).normalize();

        // 将Y轴的分量稍微增加一点
        direction = new Vec3(direction.x, direction.y + 0.2, direction.z).normalize();

        // 根据power调整速度
        Vec3 velocity = direction.scale(power * 0.5);

        // 设置方块实体的速度
        fallingBlockEntity.setDeltaMovement(velocity);
    }

    @Inject(at = @At("HEAD"), method = "useOn", cancellable = true)
    private void init(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        int k = ModEnchantHelper.getEnchantmentLevel(context.getItemInHand(), Enchantments.POWER);
        if (k > 0) {
            randomEnchant$generateFallingBlock(context.getClickedPos(),
                                               context.getLevel().getBlockState(context.getClickedPos()),
                                               context.getLevel(), k, context.getPlayer());
            cir.setReturnValue(InteractionResult.sidedSuccess(context.getLevel().isClientSide));
        }
    }

    /**
     * 生成一个掉落方块实体，并处理附魔信息
     * @param targetPos 目标方块位置
     * @param blockState 方块状态
     * @param world 世界对象
     * @param power 附魔力量值
     * @param user 使用者玩家对象
     */
    @Unique
    private void randomEnchant$generateFallingBlock(BlockPos targetPos, BlockState blockState, Level world, int power,
                                                    Player user) { // 确保在服务器端执行
        if (!world.isClientSide()) {
            if (!Config.getBedrockViolable()) {
                if (blockState.is(Blocks.BEDROCK)) return;
            }
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
            // 设置速度
            randomEnchant$launchBlock(targetPos, power, user, fallingBlockEntity);
            // 设置伤害
            if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(targetPos),
                                new ListTag())) { // 附魔海之嫌弃
                fallingBlockEntity.setHurtsEntities(0, -1);
                BlockEnchantmentStorage.removeBlockEnchantment(targetPos.immutable()); // 删除信息
            } else {
                fallingBlockEntity.setHurtsEntities(
                        50, (int) (power * 0.5 + blockState.getBlock().defaultDestroyTime() * 0.5));
            }

            // 如果方块有附加的 BlockEntity 数据，可以设置 blockEntityData 字段
            if (blockEntity != null) {
                CompoundTag blockEntityData = new CompoundTag();
                BlockEntityReflectionHelper.invokeSaveAdditionalSafe(blockEntity, blockEntityData,
                                                                     world.registryAccess());
                fallingBlockEntity.blockData = blockEntityData;
            }
            if (!enchantments.isEmpty()) { // 保存附魔信息到实体数据中
                CompoundTag entityData = fallingBlockEntity.getPersistentData();
                entityData.put("BlockEnchantments", enchantments);
            }

            world.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);

            world.addFreshEntity(fallingBlockEntity);
        }
    }
}
