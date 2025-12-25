package com.random_enchant.item;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.custom.tool.PearlSpear;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RandomEnchant.MOD_ID);

    public static final DeferredItem<Item> PEARL_SPEAR = ITEMS.register("pearl_spear",()-> new PearlSpear(new Item.Properties()));
    public static void registerModItems(IEventBus bus) {
        ITEMS.register(bus);
    }

}
