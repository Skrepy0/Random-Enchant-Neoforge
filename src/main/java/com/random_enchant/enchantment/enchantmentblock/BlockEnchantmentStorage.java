package com.random_enchant.enchantment.enchantmentblock;

import com.random_enchant.ServerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Mafuyu33
 */
public class BlockEnchantmentStorage {
    private static final Map<String, Integer> LEVEL_CACHE = new ConcurrentHashMap<>();
    private static final Logger LOGGER = Logger.getLogger(BlockEnchantmentStorage.class.getName());

    // 添加方块附魔信息（直接操作哈希表）
    public static void addBlockEnchantment(BlockPos blockPos, ListTag enchantments) {
        MinecraftServer server = ServerManager.getServerInstance();
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);
        if (state != null) {
            state.blockEnchantments.put(blockPos.immutable(), enchantments);
            state.setDirty();

            // 清除相关位置的缓存
            clearCacheForPosition(blockPos);

            // 添加调试日志
            LOGGER.fine(() -> String.format("Added enchantments to block at %s: %s",
                    blockPos, enchantments));
        }
    }

    // 移除方块的附魔数据
    public static void removeBlockEnchantment(BlockPos blockPos) {
        MinecraftServer server = ServerManager.getServerInstance();
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);
        if (state != null) {
            state.blockEnchantments.remove(blockPos);
            state.setDirty();

            // 清除缓存
            clearCacheForPosition(blockPos);

            LOGGER.fine(() -> String.format("Removed enchantments from block at %s", blockPos));
        }
    }

    // 获取指定方块的附魔列表
    public static ListTag getEnchantmentsAtPosition(BlockPos blockPos) {
        MinecraftServer server = ServerManager.getServerInstance();
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);
        return (state != null) ? state.blockEnchantments.getOrDefault(blockPos, new ListTag()) : new ListTag();
    }

    // 获取特定附魔的等级（带缓存优化）
    public static int getLevel(ResourceKey<Enchantment> enchantment, BlockPos blockPos) {
        // 安全检查
        if (enchantment == null || blockPos == null) {
            return 0;
        }

        String cacheKey = generateCacheKey(blockPos, enchantment);

        // 从缓存获取（注意：ConcurrentHashMap的get可以返回null）
        Integer cachedLevel = LEVEL_CACHE.get(cacheKey);
        if (cachedLevel != null) {
            return cachedLevel;
        }

        // 计算等级
        ListTag enchantments = getEnchantmentsAtPosition(blockPos);
        int level = findEnchantmentLevel(enchantment, enchantments);

        // 放入缓存（包括0等级）
        LEVEL_CACHE.put(cacheKey, level);

        // 缓存清理（如果缓存太大）
        if (LEVEL_CACHE.size() > 1000) {
            LEVEL_CACHE.clear();
            LOGGER.fine("Cleared cache due to size limit");
        }

        return level;
    }

    // 获取所有附魔方块的坐标集合（直接返回键集合）
    public static Set<BlockPos> getAllEnchantedBlocks() {
        MinecraftServer server = ServerManager.getServerInstance();
        BlockStateSaverAndLoader state = BlockStateSaverAndLoader.getServerState(server);
        return (state != null) ? state.blockEnchantments.keySet() : Collections.emptySet();
    }

    //-----------------------------
    //    私有辅助方法
    //-----------------------------
    private static int findEnchantmentLevel(ResourceKey<Enchantment> enchantment, ListTag enchantments) {
        if (enchantment == null || enchantments == null || enchantments.isEmpty()) {
            return 0;
        }

        for (int i = 0; i < enchantments.size(); i++) {
            CompoundTag tag = enchantments.getCompound(i);
            String enchantmentId = tag.getString("id");
            if (enchantment.location().toString().equals(enchantmentId)) {
                return tag.getInt("lvl");
            }
        }
        return 0;
    }

    private static int calculateMaxLevel(ListTag enchantments) {
        if (enchantments == null || enchantments.isEmpty()) {
            return 0;
        }

        int maxLevel = 0;
        for (int i = 0; i < enchantments.size(); i++) {
            CompoundTag tag = enchantments.getCompound(i);
            int level = tag.getInt("lvl");
            maxLevel = Math.max(maxLevel, level);
        }
        return maxLevel;
    }

    // 修复：添加缺失的方法
    private static String generateCacheKey(BlockPos blockPos, ResourceKey<Enchantment> enchantment) {
        return blockPos.toShortString() + ":" + enchantment.location().toString();
    }

    private static String generateCacheKey(BlockPos blockPos, ListTag enchantments) {
        // 使用附魔列表的哈希值
        return blockPos.toShortString() + ":list:" + Integer.toHexString(enchantments.hashCode());
    }

    // 清除特定位置的缓存
    private static void clearCacheForPosition(BlockPos blockPos) {
        String prefix = blockPos.toShortString() + ":";
        LEVEL_CACHE.keySet().removeIf(key -> key.startsWith(prefix));
    }

    // 工具方法：检查方块是否有特定附魔
    public static boolean hasEnchantment(ResourceKey<Enchantment> enchantment, BlockPos blockPos) {
        return getLevel(enchantment, blockPos) > 0;
    }
}