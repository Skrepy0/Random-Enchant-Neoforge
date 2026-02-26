package com.random_enchant.item;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.custom.misc.GuideItem;
import com.random_enchant.item.custom.misc.LightningItem;
import com.random_enchant.item.custom.misc.MilkBottleItem;
import com.random_enchant.item.custom.potion.MemoryPotion;
import com.random_enchant.item.custom.potion.SoultrancePotion;
import com.random_enchant.item.custom.tool.EnchantBrush;
import com.random_enchant.item.custom.weapon.PearlSpear;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RandomEnchant.MOD_ID);
    public static final DeferredItem<Item> ENCHANT_BRUSH =
            ITEMS.register("enchant_brush", () -> new EnchantBrush(new Item.Properties()));
    public static final DeferredItem<Item> PEARL_SPEAR =
            ITEMS.register("pearl_spear", () -> new PearlSpear(new Item.Properties()));
    public static final DeferredItem<Item> LIGHTNING_ITEM = ITEMS.register(
            "lightning", () -> new LightningItem(Tiers.NETHERITE, new Item.Properties().fireResistant().stacksTo(8)));
    public static final DeferredItem<Item> MILK_BOTTLE = ITEMS.register(
            "milk_bottle",
            ()
                    -> new MilkBottleItem(
                            new Item.Properties()
                                    .craftRemainder(Items.GLASS_BOTTLE)
                                    .food((new FoodProperties.Builder()).nutrition(0).saturationModifier(0F).build())
                                    .stacksTo(16)));
    public static final DeferredItem<Item> MEMORY_POTION = ITEMS.register(
            "memory_potion",
            ()
                    -> new MemoryPotion(
                            new Item.Properties()
                                    .craftRemainder(Items.GLASS_BOTTLE)
                                    .food((new FoodProperties.Builder()).nutrition(0).saturationModifier(0F).build())
                                    .stacksTo(16)));
    public static final DeferredItem<Item> SOULTRANCE_POTION = ITEMS.register(
            "soultrance_potion",
            ()
                    -> new SoultrancePotion(
                            new Item.Properties()
                                    .craftRemainder(Items.GLASS_BOTTLE)
                                    .food((new FoodProperties.Builder()).nutrition(0).saturationModifier(0F).build())
                                    .stacksTo(16)));
    public static final DeferredItem<Item> GUIDE = ITEMS.register("guide",()->new GuideItem(new Item.Properties()));
    public static void registerModItems(IEventBus bus) { ITEMS.register(bus); }
}
