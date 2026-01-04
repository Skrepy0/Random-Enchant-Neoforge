package com.random_enchant.event;

import com.random_enchant.RandomEnchant;
import com.random_enchant.mixin_helper.BowDashMixinHelper;
import com.random_enchant.mixin_helper.ElytraJumpMixinHelper;
import com.random_enchant.mixin_helper.ShieldDashMixinHelper;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = RandomEnchant.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class AttackKeyCheckHandler {
    private static boolean wasKeyPressedLastFrame = false;
    private static boolean wasJumpKeyPressedLastFrame = false;

    @SubscribeEvent
    public static void OnClientTickStart(ClientTickEvent.Pre event) {
        Minecraft client = Minecraft.getInstance();
        registerAttackKeyListener(client);
    }

    // 在初始化阶段注册攻击键的按下事件监听器
    public static void registerAttackKeyListener(Minecraft client) {
        // 检查攻击键是否在当前帧被按下
        boolean isKeyPressed = client.options.keyAttack.isDown();

        // 如果攻击键在上一帧被按下而在当前帧没有被按下，表示松开了按键
        if (wasKeyPressedLastFrame && !isKeyPressed) {
            ShieldDashMixinHelper.setIsAttackKeyPressed(false);
            BowDashMixinHelper.setIsAttackKeyPressed(false);
        }

        // 更新攻击键上一帧的按下状态
        wasKeyPressedLastFrame = isKeyPressed;

        // 检查攻击键是否在当前帧被按下
        if (isKeyPressed) {
            ShieldDashMixinHelper.setIsAttackKeyPressed(true);
            BowDashMixinHelper.setIsAttackKeyPressed(true);
        }


        boolean isJumpKeyPressed = client.options.keyJump.isDown();
        // 如果攻击键在上一帧被按下而在当前帧没有被按下，表示松开了按键
        if (wasJumpKeyPressedLastFrame && !isJumpKeyPressed) {
            ElytraJumpMixinHelper.setIsJumpKeyPressed(false);
            // 在这里执行您的操作
        }

        // 更新攻击键上一帧的按下状态
        wasJumpKeyPressedLastFrame = isJumpKeyPressed;

        // 检查攻击键是否在当前帧被按下
        if (isJumpKeyPressed) {
            ElytraJumpMixinHelper.setIsJumpKeyPressed(true);
        }

        if (wasJumpKeyPressedLastFrame && !isJumpKeyPressed) {
            ElytraJumpMixinHelper.setIsJumpKeyPressed(false);
        }

        wasJumpKeyPressedLastFrame = isJumpKeyPressed;

        // 检查攻击键是否在当前帧被按下
        if (isJumpKeyPressed) {
            ElytraJumpMixinHelper.setIsJumpKeyPressed(true);
        }
    }
}