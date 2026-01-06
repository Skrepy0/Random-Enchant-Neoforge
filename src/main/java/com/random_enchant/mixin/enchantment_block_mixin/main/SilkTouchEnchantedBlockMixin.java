package com.random_enchant.mixin.enchantment_block_mixin.main;

import com.random_enchant.Config;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Block.class)
public abstract class SilkTouchEnchantedBlockMixin {

    /**
     * 当方块被破坏时，检查玩家是否使用精准采集工具挖掘附魔方块
     * 如果是，则掉落带有附魔的方块
     */
    @Inject(at = @At("HEAD"), method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V", cancellable = true)
    private static void onBlockDropResources(BlockState state, Level level, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack tool, CallbackInfo ci) {
        // 只在服务端执行
        if (level.isClientSide) {
            return;
        }

        if (!Config.isEnchantedBlockGetatable()) return;

        // 检查方块是否有附魔
        ListTag enchantments = BlockEnchantmentStorage.getEnchantmentsAtPosition(pos);
        if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(pos), new ListTag())) {
            BlockEnchantmentStorage.removeBlockEnchantment(pos.immutable());//删除信息
        }
        if (enchantments.isEmpty()) {
            return;
        }

        // 检查破坏方块的实体是否是玩家
        if (!(entity instanceof Player)) {
            return;
        }

        // 检查工具是否有精准采集附魔
        if (ModEnchantHelper.getEnchantmentLevel(tool, Enchantments.SILK_TOUCH) <= 0) {
            return;
        }

        // 取消原始掉落
        ci.cancel();

        // 创建带有附魔的物品栈
        ItemStack enchantedBlockStack = new ItemStack(state.getBlock());
        if (!enchantments.isEmpty()) {
            // 将附魔应用到物品栈上
            for (int i = 0; i < enchantments.size(); i++) {
                net.minecraft.nbt.CompoundTag enchantmentTag = enchantments.getCompound(i);
                String fullEnchantmentId = enchantmentTag.getString("id");
                int lvl = enchantmentTag.getInt("lvl");

                // 修复资源位置格式问题
                // 处理可能包含多个冒号的附魔ID
                String[] parts = fullEnchantmentId.split(":");
                if (parts.length < 2) {
                    continue; // 跳过格式不正确的附魔ID
                }

                // 只保留命名空间和路径，忽略可能的额外部分
                String nameSpace = parts[0];
                String enchantmentId = parts[1];

                // 创建有效的资源位置
                net.minecraft.resources.ResourceLocation resourceLocation =
                        net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(nameSpace, enchantmentId);

                // 获取附魔对象
                net.minecraft.core.Registry<net.minecraft.world.item.enchantment.Enchantment> enchantmentRegistry =
                        level.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT);
                net.minecraft.core.Holder<net.minecraft.world.item.enchantment.Enchantment> enchantmentHolder =
                        enchantmentRegistry.getHolder(resourceLocation).orElse(null);

                if (enchantmentHolder != null) {
                    // 应用附魔到物品栈
                    enchantedBlockStack.enchant(enchantmentHolder, lvl);
                }
            }
        }

        // 掉落带有附魔的方块
        Block.popResource(level, pos, enchantedBlockStack);
    }

    @Unique
    private static ResourceKey<Enchantment> key(String name, String namespace) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(namespace, name));
    }
}