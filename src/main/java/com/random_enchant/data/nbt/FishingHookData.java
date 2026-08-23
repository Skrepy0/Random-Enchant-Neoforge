package com.random_enchant.data.nbt;

import java.util.function.Supplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class FishingHookData {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "random_enchant");

    public static final Supplier<AttachmentType<KineticLevel>> KINETIC_LEVEL =
            ATTACHMENT_TYPES.register("fishing_rod_kinetic_level",
                                      () -> AttachmentType.serializable(KineticLevel::new).build()); // 存储在玩家
    public static final Supplier<AttachmentType<NoResistanceLevel>> NO_RESISTANCE_LEVEL =
            ATTACHMENT_TYPES.register("fishing_rod_no_resistance_level",
                                      () -> AttachmentType.serializable(NoResistanceLevel::new).build()); // 存储在鱼漂

    public static KineticLevel getKineticLevel(Player player) { return player.getData(KINETIC_LEVEL.get()); }

    public static void setKineticLevel(Player player, int kineticLevel) {
        getKineticLevel(player).setKineticLevel(kineticLevel);
    }

    public static NoResistanceLevel getNoResistanceLevel(FishingHook hook) {
        return hook.getData(NO_RESISTANCE_LEVEL.get());
    }

    public static void setNoResistanceLevel(FishingHook hook, int noResistanceLevel) {
        getNoResistanceLevel(hook).setNoResistanceLevel(noResistanceLevel);
    }

    public static class KineticLevel implements INBTSerializable<CompoundTag> {

        private static final String KEY_KINETIC_LEVEL = "KineticLevel";
        private int kineticLevel = 0;

        public int getKineticLevel() { return kineticLevel; }

        public void setKineticLevel(int kineticLevel) { this.kineticLevel = kineticLevel; }

        @Override
        public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
            CompoundTag tag = new CompoundTag();
            tag.putInt(KEY_KINETIC_LEVEL, kineticLevel);
            return tag;
        }

        @Override
        public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag compoundTag) {
            this.kineticLevel = compoundTag.getInt(KEY_KINETIC_LEVEL);
        }
    }

    public static class NoResistanceLevel implements INBTSerializable<CompoundTag> {

        private static final String KEY_NO_RESISTANCE_LEVEL = "NoResistanceLevel";
        private int noResistanceLevel = 0;

        public int getNoResistanceLevel() { return noResistanceLevel; }

        public void setNoResistanceLevel(int noResistanceLevel) { this.noResistanceLevel = noResistanceLevel; }

        @Override
        public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
            CompoundTag tag = new CompoundTag();
            tag.putInt(KEY_NO_RESISTANCE_LEVEL, noResistanceLevel);
            return tag;
        }

        @Override
        public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag compoundTag) {
            this.noResistanceLevel = compoundTag.getInt(KEY_NO_RESISTANCE_LEVEL);
        }
    }
}
