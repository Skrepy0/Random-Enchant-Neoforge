// ModChestLootTables.java
package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItems;
import java.util.function.BiConsumer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ModChestLootTables implements LootTableSubProvider {

    public ModChestLootTables(HolderLookup.Provider provider) {}

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        // 注意：这里使用的是我们模组的命名空间，不是minecraft
        // 这会生成独立的战利品表，然后通过数据包注入到原版战利品表中

        // ==================== 地牢箱子 ====================
        consumer.accept(ResourceKey.create(
                                net.minecraft.core.registries.Registries.LOOT_TABLE,
                                ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "inject/chests/dungeon")),
                        createDungeonLootTable());

        // ==================== 沙漠神殿 ====================
        consumer.accept(ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,
                                           ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID,
                                                                                 "inject/chests/desert_pyramid")),
                        createDesertPyramidLootTable());

        // ==================== 废弃矿井 ====================
        consumer.accept(ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,
                                           ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID,
                                                                                 "chests/abandoned_mineshaft")),
                        createMineshaftLootTable());

        // ==================== 下界要塞 ====================
        consumer.accept(ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,
                                           ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID,
                                                                                 "inject/chests/nether_bridge")),
                        createNetherFortressLootTable());

        // ==================== 末地城 ====================
        consumer.accept(ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,
                                           ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID,
                                                                                 "inject/chests/end_city_treasure")),
                        createEndCityLootTable());

        // ==================== 村庄箱子 ====================
        consumer.accept(ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,
                                           ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID,
                                                                                 "inject/chests/village_house")),
                        createVillageHouseLootTable());

        // ==================== 沉船宝藏 ====================
        consumer.accept(ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,
                                           ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID,
                                                                                 "inject/chests/shipwreck_treasure")),
                        createShipwreckLootTable());

        // ==================== 掠夺者前哨站 ====================
        consumer.accept(ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,
                                           ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID,
                                                                                 "inject/chests/pillager_outpost")),
                        createPillagerOutpostLootTable());

        // ==================== 林地府邸 ====================
        consumer.accept(ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,
                                           ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID,
                                                                                 "inject/chests/woodland_mansion")),
                        createWoodlandMansionLootTable());

        // ==================== 堡垒遗迹 ====================
        consumer.accept(ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,
                                           ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID,
                                                                                 "inject/chests/bastion_treasure")),
                        createBastionTreasureLootTable());

        // ==================== 远古城市 ====================
        consumer.accept(ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,
                                           ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID,
                                                                                 "inject/chests/ancient_city")),
                        createAncientCityLootTable());

        // ==================== 试炼密室 ====================
        consumer.accept(ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE,
                                           ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID,
                                                                                 "inject/chests/trial_chambers")),
                        createTrialChambersLootTable());
    }

    // ==================== 战利品表生成方法 ====================

    /**
     * 地牢箱子战利品表
     */
    private LootTable.Builder createDungeonLootTable() {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .name("random_enchant_dungeon_additions")
                        .setRolls(UniformGenerator.between(1, 5))
                        .add(LootItem.lootTableItem(ModItems.MEMORY_POTION.get())
                                     .setWeight(1)
                                     .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))));
    }

    /**
     * 沙漠神殿战利品表
     */
    private LootTable.Builder createDesertPyramidLootTable() {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .name("random_enchant_desert_pyramid_additions")
                        .setRolls(UniformGenerator.between(0, 2))
                        .add(LootItem.lootTableItem(ModItems.MEMORY_POTION.get())
                                     .setWeight(4)
                                     .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                        .add(LootItem.lootTableItem(ModItems.ENCHANT_BRUSH.get())
                                     .setWeight(3)
                                     .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))));
    }

    /**
     * 村庄房屋战利品表
     */
    private LootTable.Builder createVillageHouseLootTable() {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .name("random_enchant_village_additions")
                        .setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ModItems.MILK_BOTTLE.get())
                                     .setWeight(5)
                                     .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))));
    }

    /**
     * 下界要塞战利品表
     */
    private LootTable.Builder createNetherFortressLootTable() {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .name("random_enchant_nether_additions")
                        .setRolls(UniformGenerator.between(0, 2))
                        .add(LootItem.lootTableItem(ModItems.MEMORY_POTION.get())
                                     .setWeight(3)
                                     .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))));
    }

    /**
     * 末地城战利品表
     */
    private LootTable.Builder createEndCityLootTable() {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .name("random_enchant_end_city_additions")
                        .setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ModItems.MEMORY_POTION.get())
                                     .setWeight(20)
                                     .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                        .add(LootItem.lootTableItem(ModItems.PEARL_SPEAR.get())
                                     .setWeight(1)
                                     .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))));
    }

    /**
     * 废弃矿井战利品表
     */
    private LootTable.Builder createMineshaftLootTable() {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .name("random_enchant_mineshaft_additions")
                        .setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ModItems.MEMORY_POTION.get())
                                     .setWeight(7)
                                     .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                        .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK.asItem())
                                     .setWeight(5)
                                     .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                        .add(LootItem.lootTableItem(Items.GOLDEN_APPLE)
                                     .setWeight(1)
                                     .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))));
    }

    /**
     * 沉船宝藏战利品表
     */
    private LootTable.Builder createShipwreckLootTable() {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .name("random_enchant_shipwreck_additions")
                        .setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ModItems.MEMORY_POTION.get())
                                     .setWeight(4)
                                     .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))));
    }

    /**
     * 掠夺者前哨站战利品表
     */
    private LootTable.Builder createPillagerOutpostLootTable() {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .name("random_enchant_pillager_additions")
                        .setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ModItems.MEMORY_POTION.get())
                                     .setWeight(5)
                                     .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))));
    }

    private LootTable.Builder createWoodlandMansionLootTable() {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .name("random_enchant_mansion_additions")
                        .setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ModItems.MEMORY_POTION.get())
                                     .setWeight(5)
                                     .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))));
    }

    private LootTable.Builder createBastionTreasureLootTable() {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .name("random_enchant_bastion_treasure_additions")
                        .setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ModItems.MEMORY_POTION.get())
                                     .setWeight(5)
                                     .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))));
    }

    private LootTable.Builder createTrialChambersLootTable() {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .name("random_enchant_trial_chambers_additions")
                        .setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ModItems.MEMORY_POTION.get())
                                     .setWeight(5)
                                     .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))));
    }

    private LootTable.Builder createAncientCityLootTable() {
        return LootTable.lootTable().withPool(
                LootPool.lootPool()
                        .name("random_enchant_ancient_city_additions")
                        .setRolls(UniformGenerator.between(0, 3))
                        .add(LootItem.lootTableItem(ModItems.MEMORY_POTION.get())
                                     .setWeight(5)
                                     .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))));
    }
}
