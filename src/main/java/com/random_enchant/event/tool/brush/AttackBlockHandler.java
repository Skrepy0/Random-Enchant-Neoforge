package com.random_enchant.event.tool.brush;

import com.random_enchant.data.ngt.BrushNBTUtils;
import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import com.random_enchant.item.ModItems;
import com.random_enchant.mixin_helper.InjectHelper;
import com.random_enchant.network.packet.S2C.AddEnchantedBlockParticleS2CPacket;
import com.random_enchant.render.particle.ParticleRenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class AttackBlockHandler {

    @SubscribeEvent
    public static void attackBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        BlockPos pos = event.getPos();

        // 只在服务端执行
        if (level.isClientSide()) return;

        ItemStack mainHandItem = player.getMainHandItem();
        // 检查主手物品是否是我们的刷子
        if (!mainHandItem.is(ModItems.ENCHANT_BRUSH)) {
            return;
        }

        // 获取刷子物品
        ItemStack brush = mainHandItem;

        if (brush.isEnchanted()) {
            // 有附魔的刷子：进行区域附魔操作
            if (!BrushNBTUtils.hasStartPos(brush)) {
                // 第一次点击：记录起始点
                BrushNBTUtils.setStartPos(brush, pos);
                startBlockParticlesRender(level, pos);
                // 给玩家提示
                player.displayClientMessage(Component.translatable("message.random_enchant.item.enchant_brush.selected_1"), true);
            } else {
                // 第二次点击：记录终点并执行区域操作
                BlockPos startPos = BrushNBTUtils.getStartPos(brush);
                brushAllBlocks(level, startPos, pos, brush);
                endBlockParticlesRender(level, pos);
                // 清除选择状态
                BrushNBTUtils.clearSelection(brush);
                player.displayClientMessage(Component.translatable("message.random_enchant.item.enchant_brush.selected_2"), true);
            }
        } else {
            // 没有附魔的刷子：清除区域附魔
            if (!BrushNBTUtils.hasStartPos(brush)) {
                BrushNBTUtils.setStartPos(brush, pos);
                player.displayClientMessage(Component.translatable("message.random_enchant.item.enchant_brush.selected_1"), true);
            } else {
                BlockPos startPos = BrushNBTUtils.getStartPos(brush);
                clearAllBlocks(level, startPos, pos);
                BrushNBTUtils.clearSelection(brush);
                player.displayClientMessage(Component.translatable("message.random_enchant.item.enchant_brush.clear_area"), true);
            }
        }

        // 取消事件，防止破坏方块
        event.setCanceled(true);
    }

    private static void startBlockParticlesRender(Level level, BlockPos pos) {
        // 确保在服务端发送数据包
        if (!level.isClientSide()) {
            PacketDistributor.sendToAllPlayers(new AddEnchantedBlockParticleS2CPacket(pos, ParticleRenderType.RenderType.START_BLOCK));
        }
    }

    private static void endBlockParticlesRender(Level level, BlockPos pos) {
        if (!level.isClientSide()) {
            PacketDistributor.sendToAllPlayers(new AddEnchantedBlockParticleS2CPacket(pos, ParticleRenderType.RenderType.END_BLOCK));
        }
    }

    private static void brushAllBlocks(Level world, BlockPos startPos, BlockPos endPos, ItemStack brushItem) {
        // 获取立方体对角方块的坐标
        int minX = Math.min(startPos.getX(), endPos.getX());
        int minY = Math.min(startPos.getY(), endPos.getY());
        int minZ = Math.min(startPos.getZ(), endPos.getZ());
        int maxX = Math.max(startPos.getX(), endPos.getX());
        int maxY = Math.max(startPos.getY(), endPos.getY());
        int maxZ = Math.max(startPos.getZ(), endPos.getZ());

        // 从刷子物品中获取附魔列表
        ListTag enchantments = BrushNBTUtils.getEnchantments(brushItem);
        // 如果刷子物品没有存储附魔，则尝试从物品的NBT中获取（兼容旧方式）
        if (enchantments.isEmpty()) {
            enchantments = InjectHelper.enchantmentsToNbtList(brushItem);
        }

        // 遍历立方体内的所有方块
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockState blockState = world.getBlockState(currentPos);

                    // 排除空气、水、岩浆等特定方块
                    if (blockState.is(Blocks.AIR) ||
                            blockState.is(Blocks.WATER) ||
                            blockState.is(Blocks.LAVA)) {
                        continue;
                    }

                    // 对满足条件的方块添加附魔
                    if (!enchantments.isEmpty()) {
                        BlockEnchantmentStorage.addBlockEnchantment(currentPos, enchantments);
                    }
                }
            }
        }
    }

    private static void clearAllBlocks(Level world, BlockPos startPos, BlockPos endPos) {
        // 获取立方体对角方块的坐标
        int minX = Math.min(startPos.getX(), endPos.getX());
        int minY = Math.min(startPos.getY(), endPos.getY());
        int minZ = Math.min(startPos.getZ(), endPos.getZ());
        int maxX = Math.max(startPos.getX(), endPos.getX());
        int maxY = Math.max(startPos.getY(), endPos.getY());
        int maxZ = Math.max(startPos.getZ(), endPos.getZ());

        // 遍历立方体内的所有方块
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockState blockState = world.getBlockState(currentPos);
                    // 排除空气、水、岩浆等特定方块
                    if (blockState.is(Blocks.AIR) ||
                            blockState.is(Blocks.WATER) ||
                            blockState.is(Blocks.LAVA)) {
                        continue;
                    }
                    // 移除方块的附魔
                    BlockEnchantmentStorage.removeBlockEnchantment(currentPos);
                }
            }
        }
    }
}