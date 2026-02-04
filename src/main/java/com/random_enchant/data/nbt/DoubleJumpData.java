package com.random_enchant.data.nbt;

import java.util.function.Supplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.UnknownNullability;

public class DoubleJumpData {

    // ==================== 1. 注册 Attachment ====================

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "doublejump");

    public static final Supplier<AttachmentType<DoubleJumpState>> DOUBLE_JUMP_STATE =
            ATTACHMENT_TYPES.register("double_jump", () -> AttachmentType.serializable(DoubleJumpState::new).build());

    // ==================== 2. 数据类 ====================

    public static class DoubleJumpState implements INBTSerializable<CompoundTag> {
        // 核心数据：是否在落地前使用了二段跳
        private boolean usedDoubleJump = false;

        // ==================== 写入接口 ====================

        /** 设置是否使用了二段跳 */
        public void setUsedDoubleJump(boolean used) { this.usedDoubleJump = used; }
        public boolean getUsedDoubleJump() { return usedDoubleJump; }

        /** 标记已使用二段跳 */
        public void markDoubleJumpUsed() { this.usedDoubleJump = true; }

        /** 重置二段跳状态（落地时调用） */
        public void reset() { this.usedDoubleJump = false; }

        // ==================== 读取接口 ====================

        /** 检查是否已经使用了二段跳 */
        public boolean hasUsedDoubleJump() { return usedDoubleJump; }

        /** 检查是否可以使用二段跳（还没使用且在空中） */
        public boolean canDoubleJump(Player player) { return !usedDoubleJump && !player.onGround(); }

        // ==================== NBT序列化 ====================

        @Override
        public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("UsedDoubleJump", usedDoubleJump);
            return tag;
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
            usedDoubleJump = tag.getBoolean("UsedDoubleJump");
        }
    }

    // ==================== 3. 便捷访问方法 ====================

    /** 获取玩家的二段跳数据 */
    public static DoubleJumpState get(Player player) { return player.getData(DOUBLE_JUMP_STATE.get()); }

    /** 检查玩家是否使用了二段跳 */
    public static boolean hasUsedDoubleJump(Player player) { return get(player).hasUsedDoubleJump(); }

    /** 设置玩家的二段跳状态 */
    public static void setUsedDoubleJump(Player player, boolean used) { get(player).setUsedDoubleJump(used); }

    /** 标记玩家已使用二段跳 */
    public static void markDoubleJumpUsed(Player player) { get(player).markDoubleJumpUsed(); }

    /** 重置玩家的二段跳状态 */
    public static void reset(Player player) { get(player).reset(); }

    /** 检查玩家是否可以使用二段跳 */
    public static boolean canDoubleJump(Player player) { return get(player).canDoubleJump(player); }
}
