package com.random_enchant.mixin.enchantment_block_mixin.custom.badluckofthesea;

import com.random_enchant.RandomEnchant;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.lang.reflect.Method;

public class BlockEntityReflectionHelper {

    // 带防护机制的调用，防止递归
    private static final ThreadLocal<Integer> RECURSION_DEPTH = ThreadLocal.withInitial(() -> 0);
    private static Method SAVE_ADDITIONAL_METHOD = null;
    private static boolean METHOD_INITIALIZED = false;

    // 获取saveAdditional方法
    private static synchronized Method getSaveAdditionalMethod() {
        if (!METHOD_INITIALIZED) {
            try {
                SAVE_ADDITIONAL_METHOD = BlockEntity.class.getDeclaredMethod(
                        "saveAdditional",
                        CompoundTag.class,
                        HolderLookup.Provider.class
                );
                SAVE_ADDITIONAL_METHOD.setAccessible(true);
                METHOD_INITIALIZED = true;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Failed to find saveAdditional method in BlockEntity", e);
            }
        }
        return SAVE_ADDITIONAL_METHOD;
    }

    // 使用反射调用saveAdditional
    public static void invokeSaveAdditional(BlockEntity blockEntity, CompoundTag tag, HolderLookup.Provider registries) {
        if (blockEntity == null) return;

        try {
            Method method = getSaveAdditionalMethod();
            method.invoke(blockEntity, tag, registries);
        } catch (Exception e) {
            // 回退方案：使用public方法
            try {
                CompoundTag fallbackData = blockEntity.saveWithoutMetadata(registries);
                if (fallbackData != null && !fallbackData.isEmpty()) {
                    tag.merge(fallbackData);
                }
            } catch (Exception e2) {
                // 最终回退：记录错误
                RandomEnchant.LOGGER.error("Failed to save BlockEntity data: " + e2.getMessage());
            }
        }
    }

    public static void invokeSaveAdditionalSafe(BlockEntity blockEntity, CompoundTag tag, HolderLookup.Provider registries) {
        int depth = RECURSION_DEPTH.get();

        // 防止无限递归
        if (depth > 3) {
            RandomEnchant.LOGGER.error("Recursion depth exceeded when saving BlockEntity at " +
                    blockEntity.getBlockPos() + ", using fallback");
            // 使用简化保存
            try {
                CompoundTag fallback = blockEntity.saveWithoutMetadata(registries);
                if (fallback != null) {
                    tag.merge(fallback);
                }
            } catch (Exception e) {
                // 忽略错误
            }
            return;
        }

        RECURSION_DEPTH.set(depth + 1);
        try {
            invokeSaveAdditional(blockEntity, tag, registries);
        } finally {
            RECURSION_DEPTH.set(depth);
        }
    }
}