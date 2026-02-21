package com.random_enchant.event.tool;

import com.random_enchant.data.nbt.BrushNBTUtils;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import com.random_enchant.item.ModItems;
import com.random_enchant.mixin_helper.InjectHelper;
import com.random_enchant.network.packet.S2C.AddEnchantedBlockParticleS2CPacket;
import com.random_enchant.render.particle.ParticleRenderType;
import com.random_enchant.util.AdvancementHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class EnchantBrushHelper {
    private static boolean haveHopper = false;
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) return;
        InteractionHand hand = event.getHand();
        ItemStack brush = player.getItemInHand(hand);
        if (brush.getItem() != ModItems.ENCHANT_BRUSH.asItem()) return;
        // 检查是否按住 Shift
        if (player.isShiftKeyDown()) {
            if (BrushNBTUtils.hasStartPos(brush)) {
                BrushNBTUtils.clearSelection(brush);
                player.displayClientMessage(
                        Component.translatable("message.random_enchant.item.enchant_brush.clear_data"), true);
            }
        }
    }

    @SubscribeEvent
    public static void useOnBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        BlockPos pos = event.getPos();

        // 只在服务端执行
        if (level.isClientSide()) return;

        ItemStack brush = player.getMainHandItem();
        // 检查主手物品是否是刷子
        if (!brush.is(ModItems.ENCHANT_BRUSH)) {
            return;
        }
        if (player.isSpectator()) return;
        // 获取刷子状态
        boolean status = BrushNBTUtils.getStatus(brush);
        if (!status) return;

        if (brush.isEnchanted()) {
            // 有附魔的刷子：进行区域附魔操作
            if (!BrushNBTUtils.hasStartPos(brush)) {
                // 第一次点击：记录起始点
                BrushNBTUtils.setStartPos(brush, pos);
                startBlockParticlesRender(level, pos);
                // 给玩家提示
                player.displayClientMessage(
                        Component.translatable("message.random_enchant.item.enchant_brush.selected_1"), true);
            } else {
                // 第二次点击：记录终点并执行区域操作
                BlockPos startPos = BrushNBTUtils.getStartPos(brush);
                double blockCount;
                int unbreakingLevel = ModEnchantHelper.getEnchantmentLevel(Enchantments.UNBREAKING, brush);
                if (!player.isCreative()) {
                    // 判断耐久是否允许
                    // 实际方块数量的0.1%
                    blockCount = (double) Math.abs(startPos.getX() - pos.getX()) / 1000 *
                                 Math.abs(startPos.getZ() - pos.getZ());
                    double durability = (double) (brush.getMaxDamage() - brush.getDamageValue()) / 1000;
                    if (getDamage((int) (blockCount * 1000), unbreakingLevel) >= durability * 1000) {
                        player.displayClientMessage(
                                Component.translatable(
                                        "message.random_enchant.item.enchant_brush.durability_insufficient",
                                        (int) (blockCount * 1000)),
                                true);
                        BrushNBTUtils.clearSelection(brush);
                        return;
                    }
                }
                if (startPos != null) {
                    int originDamage = brushAllBlocks(level, startPos, pos, brush);
                    if (!player.isCreative()) {
                        int damage = getDamage(originDamage, unbreakingLevel);
                        brush.hurtAndBreak(damage, player, EquipmentSlot.MAINHAND);
                    }
                    endBlockParticlesRender(level, pos);
                    // 清除选择状态
                    BrushNBTUtils.clearSelection(brush);
                    player.displayClientMessage(
                            Component.translatable("message.random_enchant.item.enchant_brush.selected_2"), true);
                    if (player instanceof ServerPlayer serverPlayer) {
                        AdvancementHelper.grantAdvancement(serverPlayer, "enchant/use_region_mode", "select_region");
                        if (ModEnchantHelper.getEnchantmentLevel(brush, ModEnchantments.BAD_LUCK_OF_THE_SEA) > 0) {
                            AdvancementHelper.grantAdvancement(serverPlayer, "enchant/bad_luck_of_the_sea",
                                                               "bad_luck_of_the_sea");
                        }
                        if (ModEnchantHelper.getEnchantmentLevel(brush, ModEnchantments.EXPLODE) > 0) {
                            AdvancementHelper.grantAdvancement(serverPlayer, "enchant/explode", "explode");
                        }
                        if (ModEnchantHelper.getEnchantmentLevel(brush, Enchantments.QUICK_CHARGE) > 0 && haveHopper) {
                            AdvancementHelper.grantAdvancement(serverPlayer, "enchant/hyper_transfer",
                                                               "hyper_transfer");
                        }
                    }
                }
            }
        } else {
            // 没有附魔的刷子：清除区域附魔
            if (!BrushNBTUtils.hasStartPos(brush)) {
                BrushNBTUtils.setStartPos(brush, pos);
                player.displayClientMessage(
                        Component.translatable("message.random_enchant.item.enchant_brush.selected_1"), true);
            } else {
                BlockPos startPos = BrushNBTUtils.getStartPos(brush);
                double blockCount;
                int unbreakingLevel = ModEnchantHelper.getEnchantmentLevel(Enchantments.UNBREAKING, brush);
                if (!player.isCreative()) {
                    // 判断耐久是否允许
                    // 实际方块数量的0.1%
                    blockCount = (double) Math.abs(startPos.getX() - pos.getX()) / 1000 *
                                 Math.abs(startPos.getZ() - pos.getZ());
                    double durability = (double) (brush.getMaxDamage() - brush.getDamageValue()) / 1000;
                    if (getDamage((int) (blockCount * 1000), unbreakingLevel) >= durability * 1000) {
                        player.displayClientMessage(
                                Component.translatable(
                                        "message.random_enchant.item.enchant_brush.durability_insufficient",
                                        (int) (blockCount * 1000)),
                                true);
                        BrushNBTUtils.clearSelection(brush);
                        return;
                    }
                }
                int originDamage = clearAllBlocks(level, startPos, pos);
                if (!player.isCreative()) {
                    int damage = getDamage(originDamage, unbreakingLevel);
                    brush.hurtAndBreak(damage, player, EquipmentSlot.MAINHAND);
                }
                BrushNBTUtils.clearSelection(brush);
                player.displayClientMessage(
                        Component.translatable("message.random_enchant.item.enchant_brush.clear_area"), true);
            }
        }
        // 取消事件，防止破坏方块
        event.setCanceled(true);
    }

    private static int getDamage(int originDamage, int unbreaking) {
        if (unbreaking <= 0) return originDamage;
        switch (unbreaking) {
            case 1 -> {
                return (int) (0.9 * originDamage);
            }
            case 2 -> {
                return (int) (0.7 * originDamage);
            }
            case 3 -> {
                return (int) (0.5 * originDamage);
            }
            case 4 -> {
                return (int) (0.35 * originDamage);
            }
            case 5 -> {
                return (int) (0.2 * originDamage);
            }
            case 6 -> {
                return (int) (0.1 * originDamage);
            }
            case 7 -> {
                return (int) (0.08 * originDamage);
            }
            default -> {
                return (int) (0.05 * originDamage);
            }
        }
    }

    private static void startBlockParticlesRender(Level level, BlockPos pos) {
        // 确保在服务端发送数据包
        if (!level.isClientSide()) {
            PacketDistributor.sendToAllPlayers(
                    new AddEnchantedBlockParticleS2CPacket(pos, ParticleRenderType.RenderType.START_BLOCK));
        }
    }

    private static void endBlockParticlesRender(Level level, BlockPos pos) {
        if (!level.isClientSide()) {
            PacketDistributor.sendToAllPlayers(
                    new AddEnchantedBlockParticleS2CPacket(pos, ParticleRenderType.RenderType.END_BLOCK));
        }
    }

    private static int brushAllBlocks(Level world, BlockPos startPos, BlockPos endPos, ItemStack brushItem) {
        // 获取立方体对角方块的坐标
        int minX = Math.min(startPos.getX(), endPos.getX());
        int minY = Math.min(startPos.getY(), endPos.getY());
        int minZ = Math.min(startPos.getZ(), endPos.getZ());
        int maxX = Math.max(startPos.getX(), endPos.getX());
        int maxY = Math.max(startPos.getY(), endPos.getY());
        int maxZ = Math.max(startPos.getZ(), endPos.getZ());
        int damage = 0;
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
                    if (!haveHopper) {
                        haveHopper = blockState.is(Blocks.HOPPER);
                    }

                    // 排除空气、水、岩浆等特定方块
                    if (blockState.is(Blocks.AIR) || blockState.is(Blocks.WATER) || blockState.is(Blocks.LAVA)) {
                        continue;
                    }

                    // 对满足条件的方块添加附魔
                    if (!enchantments.isEmpty()) {
                        BlockEnchantmentStorage.addBlockEnchantment(currentPos, enchantments);
                        damage++;
                    }
                }
            }
        }
        return damage;
    }

    private static int clearAllBlocks(Level world, BlockPos startPos, BlockPos endPos) {
        // 获取立方体对角方块的坐标
        int minX = Math.min(startPos.getX(), endPos.getX());
        int minY = Math.min(startPos.getY(), endPos.getY());
        int minZ = Math.min(startPos.getZ(), endPos.getZ());
        int maxX = Math.max(startPos.getX(), endPos.getX());
        int maxY = Math.max(startPos.getY(), endPos.getY());
        int maxZ = Math.max(startPos.getZ(), endPos.getZ());
        int damage = 0;
        // 遍历立方体内的所有方块
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockState blockState = world.getBlockState(currentPos);
                    // 排除空气、水、岩浆等特定方块
                    if (blockState.is(Blocks.AIR) || blockState.is(Blocks.WATER) || blockState.is(Blocks.LAVA)) {
                        continue;
                    }
                    // 移除方块的附魔
                    BlockEnchantmentStorage.removeBlockEnchantment(currentPos);
                    damage++;
                }
            }
        }
        return damage;
    }
}
