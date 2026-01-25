package com.random_enchant.event;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.entity.custom.ThrownMace;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class ThrowableMaceEvent {

    @SubscribeEvent
    public static void useItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();

        // 只处理服务器端
        if (level.isClientSide()) return;
        if (stack.getItem() != Items.MACE) return;
        int throwableLevel = ModEnchantHelper.getEnchantmentLevel(stack, ModEnchantments.THROWABLE);
        if (throwableLevel > 0) {
            event.setCanceled(true); // 取消原事件，防止重复处理

            // 创建一个新的物品堆副本用于投掷
            ItemStack thrownStack = stack.copy();
            thrownStack.setCount(1); // 只投掷一个
            Player player = event.getEntity();
            // 创建投掷物品实体
            ThrownMace thrownItem = new ThrownMace(level, player, thrownStack);
            int kineticLevel = Math.max(ModEnchantHelper.getEnchantmentLevel(stack, ModEnchantments.KINETIC), 0);
            if (ModEnchantHelper.getEnchantmentLevel(stack, ModEnchantments.NO_GRAVITY) > 0) {
                thrownItem.setNoGravity(true);
            }
            // 设置投掷物品的速度和方向
            thrownItem.shootFromRotation(event.getEntity(), event.getEntity().getXRot(), event.getEntity().getYRot(),
                                         0.0F,
                                         1.4F * (1.0f + (kineticLevel - 1) * 0.2f), // 根据附魔等级调整速度
                                         1.0F);

            // 将投掷物品实体添加到世界中
            level.addFreshEntity(thrownItem);

            // 减少原物品数量
            if (!event.getEntity().getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
    }
}
