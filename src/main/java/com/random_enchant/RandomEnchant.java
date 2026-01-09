package com.random_enchant;

import com.mojang.logging.LogUtils;
import com.random_enchant.command.ModCommands;
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

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(RandomEnchant.MOD_ID)
public class RandomEnchant {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "random_enchant";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public RandomEnchant(IEventBus modEventBus, ModContainer modContainer) {
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // 注册物品
        ModItems.registerModItems(modEventBus);
        ModItemGroup.registerModItemGroup(modEventBus);

        ModEntities.register(modEventBus);
        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (RandomEnchant) to respond directly to
        // events. Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like
        // onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // 使用静态方法引用
        modEventBus.addListener(this::onClientSetup);

        // 注册命令
        NeoForge.EVENT_BUS.addListener(this::onCommandSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

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
