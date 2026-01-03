package com.random_enchant.data.nbt;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class BrushNBTUtils {
    // NBT键名常量
    private static final String KEY_BRUSH_DATA = "BrushData";
    private static final String KEY_SELECTION_START = "SelectionStart";
    private static final String KEY_SELECTION_END = "SelectionEnd";
    private static final String KEY_HAS_START = "HasStart";
    private static final String KEY_ENCHANTMENTS = "Enchantments";
    private static final String KEY_BRUSH_STATUS = "BrushStatus";

    /**
     * 获取刷子数据的Tag（只读，适用于读取操作）
     */
    private static CompoundTag getBrushTagForReading(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return new CompoundTag();
        }

        CompoundTag rootTag = customData.copyTag();
        if (rootTag.contains(KEY_BRUSH_DATA)) {
            return rootTag.getCompound(KEY_BRUSH_DATA);
        }
        return new CompoundTag();
    }

    /**
     * 获取刷子数据的Tag（可修改，需要配合saveBrushTag使用）
     */
    private static CompoundTag getBrushTagForUpdate(ItemStack stack) {
        return getBrushTagForReading(stack);
    }

    /**
     * 保存刷子数据的Tag回物品
     */
    private static void saveBrushTag(ItemStack stack, CompoundTag brushTag) {
        // 获取或创建根Tag
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag rootTag;

        if (customData != null) {
            rootTag = customData.copyTag();
        } else {
            rootTag = new CompoundTag();
        }

        // 更新刷子数据
        rootTag.put(KEY_BRUSH_DATA, brushTag);

        // 保存回物品
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(rootTag));
    }

    // ==================== 坐标操作方法 ====================

    /**
     * 设置起始点坐标
     */
    public static void setStartPos(ItemStack stack, BlockPos pos) {
        CompoundTag brushTag = getBrushTagForUpdate(stack);

        // 创建起始点坐标的Tag
        CompoundTag startTag = new CompoundTag();
        startTag.putInt("X", pos.getX());
        startTag.putInt("Y", pos.getY());
        startTag.putInt("Z", pos.getZ());

        // 保存到刷子数据
        brushTag.put(KEY_SELECTION_START, startTag);
        brushTag.putBoolean(KEY_HAS_START, true);

        // 保存回物品
        saveBrushTag(stack, brushTag);
    }

    /**
     * 设置终点坐标
     */
    public static void setEndPos(ItemStack stack, BlockPos pos) {
        CompoundTag brushTag = getBrushTagForUpdate(stack);

        CompoundTag endTag = new CompoundTag();
        endTag.putInt("X", pos.getX());
        endTag.putInt("Y", pos.getY());
        endTag.putInt("Z", pos.getZ());

        brushTag.put(KEY_SELECTION_END, endTag);
        saveBrushTag(stack, brushTag);
    }
    /**
     * 设置状态
     */
    public static void setStatus(boolean status, ItemStack stack) {
        CompoundTag brushTag = getBrushTagForUpdate(stack);
        CompoundTag statusTag = new CompoundTag();
        statusTag.putBoolean(KEY_BRUSH_STATUS, status);
        brushTag.put(KEY_BRUSH_STATUS, statusTag);
        saveBrushTag(stack, brushTag);
    }
    /**
     * 清除选择状态
     */
    public static void clearSelection(ItemStack stack) {
        CompoundTag brushTag = getBrushTagForUpdate(stack);
        brushTag.remove(KEY_SELECTION_START);
        brushTag.remove(KEY_SELECTION_END);
        brushTag.putBoolean(KEY_HAS_START, false);
        saveBrushTag(stack, brushTag);
    }

    /**
     * 获取起始点坐标
     */
    public static BlockPos getStartPos(ItemStack stack) {
        CompoundTag brushTag = getBrushTagForReading(stack);

        if (!brushTag.contains(KEY_SELECTION_START)) {
            return null;
        }

        CompoundTag startTag = brushTag.getCompound(KEY_SELECTION_START);
        return new BlockPos(
                startTag.getInt("X"),
                startTag.getInt("Y"),
                startTag.getInt("Z")
        );
    }

    /**
     * 获取终点坐标
     */
    public static BlockPos getEndPos(ItemStack stack) {
        CompoundTag brushTag = getBrushTagForReading(stack);

        if (!brushTag.contains(KEY_SELECTION_END)) {
            return null;
        }

        CompoundTag endTag = brushTag.getCompound(KEY_SELECTION_END);
        return new BlockPos(
                endTag.getInt("X"),
                endTag.getInt("Y"),
                endTag.getInt("Z")
        );
    }
    /**
     * 获取状态
     */
    public static boolean getStatus(ItemStack stack) {
        CompoundTag brushTag = getBrushTagForReading(stack);
        if (!brushTag.contains(KEY_BRUSH_STATUS)) {
            return false;
        }
        CompoundTag statusTag = brushTag.getCompound(KEY_BRUSH_STATUS);
        return statusTag.getBoolean(KEY_BRUSH_STATUS);
    }

    /**
     * 检查是否有起始点
     */
    public static boolean hasStartPos(ItemStack stack) {
        CompoundTag brushTag = getBrushTagForReading(stack);
        return brushTag.getBoolean(KEY_HAS_START);
    }

    /**
     * 获取选择的两个点（用于计算区域）
     */
    public static BlockPos[] getSelectionPoints(ItemStack stack) {
        BlockPos start = getStartPos(stack);
        BlockPos end = getEndPos(stack);

        if (start != null && end != null) {
            return new BlockPos[]{start, end};
        }
        return null;
    }

    // ==================== 附魔数据操作方法 ====================

    /**
     * 设置刷子的附魔列表
     */
    public static void setEnchantments(ItemStack stack, ListTag enchantments) {
        CompoundTag brushTag = getBrushTagForUpdate(stack);
        brushTag.put(KEY_ENCHANTMENTS, enchantments.copy());
        saveBrushTag(stack, brushTag);
    }

    /**
     * 获取刷子的附魔列表
     */
    public static ListTag getEnchantments(ItemStack stack) {
        CompoundTag brushTag = getBrushTagForReading(stack);

        if (brushTag.contains(KEY_ENCHANTMENTS)) {
            return brushTag.getList(KEY_ENCHANTMENTS, 10);
        }
        return new ListTag();
    }

}