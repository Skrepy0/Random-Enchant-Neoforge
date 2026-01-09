package com.random_enchant.item;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.custom.tool.EnchantBrush;
import com.random_enchant.item.custom.tool.PearlSpear;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RandomEnchant.MOD_ID);
    public static final DeferredItem<Item> ENCHANT_BRUSH =
            ITEMS.register("enchant_brush", () -> new EnchantBrush(new Item.Properties()));
    public static final DeferredItem<Item> PEARL_SPEAR =
            ITEMS.register("pearl_spear", () -> new PearlSpear(new Item.Properties()));

    public static void registerModItems(IEventBus bus) { ITEMS.register(bus); }
}
