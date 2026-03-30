package com.random_enchant.event;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItems;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;

@EventBusSubscriber(modid = RandomEnchant.MOD_ID)
public class GetGuideEvent {

    // 你要监听的进度ID - 附魔师对应的进度
    private static final String TARGET_ADVANCEMENT_ID = "random_enchant:enchant/root";

    @SubscribeEvent
    public static void onAdvancementDone(AdvancementEvent.AdvancementEarnEvent event) {
        // 1. 只在服务端执行
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        // 2. 获取进度信息
        AdvancementHolder advancement = event.getAdvancement();
        if (advancement == null) return;

        // 3. 检查是否是指定的进度
        String advancementId = advancement.id().toString();
        if (!TARGET_ADVANCEMENT_ID.equals(advancementId)) {
            return;
        }

        // 4. 只有玩家（ServerPlayer）才能获得进度
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        giveGuide(player);
    }

    private static void giveGuide(ServerPlayer player) {

        // 尝试添加到玩家背包
        boolean added = player.getInventory().add(ModItems.GUIDE.toStack());

        if (!added) {
            // 背包满了，掉落在玩家位置
            ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(),
                                                   ModItems.GUIDE.toStack());
            player.level().addFreshEntity(itemEntity);
        } else {
            // 更新玩家背包（可选，但推荐）
            player.inventoryMenu.broadcastChanges();
        }

        // 可选：发送提示消息
        player.displayClientMessage(Component.translatable("event.random_enchant.get_guide"), true);
    }
}
