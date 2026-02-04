package com.random_enchant;

import com.mojang.logging.LogUtils;
import com.random_enchant.command.ModCommands;
import com.random_enchant.data.nbt.DoubleJumpData;
import com.random_enchant.enchantment.ModEnchantmentTags;
import com.random_enchant.entity.ModEntities;
import com.random_enchant.item.ModItemGroup;
import com.random_enchant.item.ModItemModelProperties;
import com.random_enchant.item.ModItemTags;
import com.random_enchant.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(RandomEnchant.MOD_ID)
public class RandomEnchant {
    public static final String MOD_ID = "random_enchant";

    public static final Logger LOGGER = LogUtils.getLogger();

    public RandomEnchant(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modEventBus.addListener(this::commonSetup);

        // 注册物品
        ModItems.registerModItems(modEventBus);
        ModItemGroup.registerModItemGroup(modEventBus);

        ModEntities.register(modEventBus);

        DoubleJumpData.ATTACHMENT_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        // 使用静态方法引用
        modEventBus.addListener(this::onClientSetup);

        // 注册命令
        NeoForge.EVENT_BUS.addListener(this::onCommandSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    private void onClientSetup(FMLClientSetupEvent event) {
        // 在客户端线程中注册
        event.enqueueWork(() -> {
            ModItemModelProperties.registerProperties();
            ModItemTags.registerModItemTags();
            ModEnchantmentTags.registerModEnchantmentTags();
        });
    }

    private void onCommandSetup(RegisterCommandsEvent event) { ModCommands.registerModCommands(event); }
}
