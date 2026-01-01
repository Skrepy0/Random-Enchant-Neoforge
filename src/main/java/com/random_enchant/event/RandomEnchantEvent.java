package com.random_enchant.event;

import com.random_enchant.RandomEnchant;
import com.random_enchant.command.RandomEnchantCommand;
import com.random_enchant.data.GlobalSwitchData;
import com.random_enchant.data.GlobalSwitchManager;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.random_enchant.enchantment.ModEnchantHelper.getDescriptionId;
import static net.minecraft.network.chat.Component.translatable;

public class RandomEnchantEvent {

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        if (event.getEntity().level().isClientSide) return;
        GlobalSwitchData data = GlobalSwitchManager.get((ServerLevel) event.getEntity().level());
        if (!data.getDoRandomEnchant()) {
            return;
        }

        Player player = event.getEntity();
        if (player == null) {
            return;
        }
        Entity target = event.getTarget();
        if (!(target instanceof LivingEntity))return;
        ItemStack mainHandItem = player.getMainHandItem();
        if (mainHandItem.isEmpty()) {
            return;
        }

        // 触发随机附魔
        if (player.getRandom().nextFloat() < 0.8f) {
            applyRandomEnchantment(mainHandItem, player);
        }
    }

    private static void applyRandomEnchantment(ItemStack itemStack, Player player) {
        // 获取所有可用附魔
        List<Enchantment> availableEnchantments = getAvailableEnchantments(player);

        if (availableEnchantments.isEmpty()) {
            return;
        }

        // 随机选择一个附魔
        Enchantment selectedEnchantment = availableEnchantments.get(
                player.getRandom().nextInt(availableEnchantments.size())
        );

        // 获取该附魔的Holder
        Holder<Enchantment> holder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .wrapAsHolder(selectedEnchantment);

        int level;
        java.util.Random random = new java.util.Random();
        int pro = random.nextInt(10);
        if (pro <= 3) {
            level = random.nextInt(5) + 6;
        } else {
            level = random.nextInt(3);
        }
        int currentLevel = itemStack.getEnchantmentLevel(holder);
        int newLevel = currentLevel + 1 + level;

        // 应用附魔
        itemStack.enchant(holder, newLevel);

        // 发送消息
        String enchantmentName = getDescriptionId(selectedEnchantment, player.level().registryAccess());
        player.displayClientMessage(
                translatable("message.random_enchant.enchant_added")
                        .append(translatable(enchantmentName)).append(translatable("enchantment.level." + (newLevel - currentLevel))),
                true
        );

        RandomEnchant.LOGGER.info("为玩家 {} 的物品添加了随机附魔: {} {}",
                player.getName().getString(), enchantmentName, newLevel);
    }

    /**
     * 获取可用于该物品的所有附魔
     */
    private static List<Enchantment> getAvailableEnchantments(Player player) {
        List<Enchantment> available = new ArrayList<>();

        // 获取附魔注册表
        Registry<Enchantment> enchantmentRegistry = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT);

        // 遍历所有附魔
        for (Holder<Enchantment> holder : enchantmentRegistry.asHolderIdMap()) {
            Enchantment enchantment = holder.value();
            available.add(enchantment);
        }

        return available;
    }
}