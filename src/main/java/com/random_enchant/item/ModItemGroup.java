package com.random_enchant.item;

import com.random_enchant.RandomEnchant;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ModItemGroup {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RandomEnchant.MOD_ID);
    private static final List<ResourceKey<Enchantment>> ENCHANTMENT_BOOK_LIST = List.of(
            Enchantments.INFINITY, Enchantments.PROTECTION, Enchantments.BLAST_PROTECTION, Enchantments.POWER,
            Enchantments.CHANNELING, Enchantments.UNBREAKING, ModEnchantments.FURY_OF_FLY,
            ModEnchantments.BAD_LUCK_OF_THE_SEA, ModEnchantments.REDIRECT_PROJECTILE, ModEnchantments.FLY,
            ModEnchantments.NO_GRAVITY, ModEnchantments.NO_RESISTANCE, ModEnchantments.EXPLODE,
            Enchantments.QUICK_CHARGE, Enchantments.FEATHER_FALLING, ModEnchantments.NO_CURSE, ModEnchantments.STEADY,
            ModEnchantments.KINETIC, ModEnchantments.THROWABLE, ModEnchantments.TRACKING, ModEnchantments.PARRY);
    public static final Supplier<CreativeModeTab> RANDOM_ENCHANT = CREATIVE_MODE_TAB.register(
            "random_enchant_tab",
            ()
                    -> CreativeModeTab.builder()
                               .icon(() -> new ItemStack(Items.ENCHANTED_BOOK))
                               .title(Component.translatable("itemGroup.random_enchant.title"))
                               .displayItems((itemDisplayParameters, output) -> {
                                   HolderLookup<Enchantment> enchantmentLookup =
                                           itemDisplayParameters.holders().lookup(Registries.ENCHANTMENT).orElseThrow();

                                   for (ResourceKey<Enchantment> enchantmentResourceKey: ENCHANTMENT_BOOK_LIST) {
                                       // 通过 lookup 获取 Holder
                                       Holder<Enchantment> enchantmentHolder =
                                               enchantmentLookup.get(enchantmentResourceKey).orElse(null);

                                       if (enchantmentHolder != null) {
                                           int maxLevel = enchantmentHolder.value().definition().maxLevel();
                                           output.accept(EnchantedBookItem.createForEnchantment(
                                                   new EnchantmentInstance(enchantmentHolder, maxLevel)));
                                       }
                                   }
                                   output.accept(Blocks.ANVIL.asItem());
                                   output.accept(ModItems.PEARL_SPEAR.get());
                                   output.accept(ModItems.ENCHANT_BRUSH.get());
                                   output.accept(ModItems.LIGHTNING_ITEM);
                                   output.accept(ModItems.MILK_BOTTLE.get());
                               })
                               .build());

    public static void registerModItemGroup(IEventBus bus) { CREATIVE_MODE_TAB.register(bus); }
}
