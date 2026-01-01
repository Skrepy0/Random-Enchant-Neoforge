package com.random_enchant.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class GlobalSwitchData extends SavedData {
    private boolean doRandomEnchant = false;

    public boolean getDoRandomEnchant() {
        return doRandomEnchant;
    }
    public void setDoRandomEnchant(boolean value) {
        this.doRandomEnchant = value;
        this.setDirty();
    }

    public static GlobalSwitchData load(CompoundTag tag, HolderLookup.Provider registries) {
        GlobalSwitchData data = new GlobalSwitchData();
        data.doRandomEnchant = tag.getBoolean("doRandomEnchant");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("doRandomEnchant", this.doRandomEnchant);
        return tag;
    }
}
