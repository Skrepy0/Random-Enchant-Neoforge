package com.random_enchant.data;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class GlobalSwitchManager {
    // 唯一标识符，建议使用你的模组ID
    private static final String DATA_NAME = "random_enchant_global_switch";

    // 核心方法：从 ServerLevel 获取数据实例
    public static GlobalSwitchData get(ServerLevel level) {
        if (level == null) return null;

        DimensionDataStorage storage = level.getDataStorage();
        // computeIfAbsent: 如果存档中没有，则调用工厂方法创建新实例
        return storage.computeIfAbsent(
                new SavedData.Factory<>(
                        () -> new GlobalSwitchData(), // 创建新实例的工厂
                        (tag, provider) -> GlobalSwitchData.load(tag, provider) // 从NBT加载的工厂
                ),
                DATA_NAME
        );
    }
}