package com.random_enchant.client.key;

import com.mojang.blaze3d.platform.InputConstants;
import com.random_enchant.RandomEnchant;
import com.random_enchant.data.nbt.BrushNBTUtils;
import com.random_enchant.item.ModItems;
import com.random_enchant.network.packet.C2S.BrushStatusC2SPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = RandomEnchant.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
@OnlyIn(Dist.CLIENT)
public class KeyInputHandler {
    public static final String KEY_CATEGORY_ENCHANT = "itemGroup.random_enchant.title";
    public static final String KEY_TOGGLE_BRUSH_STATUS = "key.random_enchant.toggle_brush_status";

    public static KeyMapping TOGGLE_BRUSH_STATUS = new KeyMapping(KEY_TOGGLE_BRUSH_STATUS, InputConstants.Type.KEYSYM,
                                                                  GLFW.GLFW_KEY_TAB, KEY_CATEGORY_ENCHANT);

    // 记录上次按键状态，防止按住不放重复触发
    private static boolean wasKeyPressed = false;

    @SubscribeEvent
    public static void registerKeyInputs(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        // 获取当前按键状态
        boolean isKeyPressed = TOGGLE_BRUSH_STATUS.isDown();

        // 检查是否是从未按下到按下（按键按下瞬间）
        if (isKeyPressed && !wasKeyPressed) {
            Player player = mc.player;
            if (player.isSpectator()) return;

            ItemStack mainHand = player.getMainHandItem();
            ItemStack offHand = player.getOffhandItem();

            boolean hasBrushInHand = (mainHand.getItem() == ModItems.ENCHANT_BRUSH.get() ||
                                      offHand.getItem() == ModItems.ENCHANT_BRUSH.get());

            if (hasBrushInHand) {
                ItemStack brushStack = mainHand.getItem() == ModItems.ENCHANT_BRUSH.get() ? mainHand : offHand;

                boolean currentStatus = BrushNBTUtils.getStatus(brushStack);
                boolean newStatus = !currentStatus;
                BrushNBTUtils.setStatus(newStatus, brushStack);

                // 发送数据包到服务器
                if (mc.getConnection() != null) {
                    PacketDistributor.sendToServer(new BrushStatusC2SPacket(newStatus));
                }

                // 显示消息
                Component statusText =
                        newStatus ? Component.translatable("item.tooltip.random_enchant.enchant_brush.status.regional")
                                  : Component.translatable("item.tooltip.random_enchant.enchant_brush.status.single");

                player.displayClientMessage(
                        Component.translatable("message.random_enchant.item.enchant_brush.status.changed")
                                .append(": ")
                                .append(statusText),
                        true);

                player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1.0f);
            }
        }

        // 更新按键状态
        wasKeyPressed = isKeyPressed;
    }

    @EventBusSubscriber(modid = RandomEnchant.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class KeyRegistries {
        @SubscribeEvent
        public static void register(RegisterKeyMappingsEvent event) {
            event.register(KeyInputHandler.TOGGLE_BRUSH_STATUS);
        }
    }
}
