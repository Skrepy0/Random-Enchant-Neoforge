package com.random_enchant.event;

import com.random_enchant.RandomEnchant;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = RandomEnchant.MOD_ID)
public class PlayerLoginHandler {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        // 只在服务端执行
        if (player.level().isClientSide()) return;

        // 发送消息
        Component message = Component.translatable("message.random_enchant.backup_warning");

        player.sendSystemMessage(message);
    }
}
