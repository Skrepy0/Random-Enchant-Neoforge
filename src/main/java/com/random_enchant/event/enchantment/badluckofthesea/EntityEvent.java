package com.random_enchant.event.enchantment.badluckofthesea;

import com.random_enchant.RandomEnchant;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import static com.random_enchant.enchantment.custom.BadLuckOfTheSeaHelper.entityWithBadLuckOfTheSea;
import static com.random_enchant.enchantment.custom.BadLuckOfTheSeaHelper.thrownTridentEntityWithBadLuckOfTheSea;

@EventBusSubscriber(modid = RandomEnchant.MOD_ID)
public class EntityEvent {
    @SubscribeEvent
    public static void entityInFluid(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        Level level = entity.level();
        if (level.isClientSide) return;
        if (entity.isInWater()) {
            if (entity instanceof ThrownTrident trident) {
                int lvl = ModEnchantHelper.getEnchantmentLevel(trident.getWeaponItem(), ModEnchantments.BAD_LUCK_OF_THE_SEA);
                if (lvl > 0) thrownTridentEntityWithBadLuckOfTheSea(trident, lvl * 0.5);
            } else if (entity instanceof ThrownEnderpearl ||
                    entity instanceof Fireball ||
                    entity instanceof Snowball ||
                    entity instanceof EyeOfEnder ||
                    entity instanceof ThrownEgg ||
                    entity instanceof ThrownPotion) {
                int lvl = ModEnchantHelper.getEnchantmentLevel(((ItemSupplier) entity).getItem(), ModEnchantments.BAD_LUCK_OF_THE_SEA);
                if (lvl > 0) entityWithBadLuckOfTheSea(entity, lvl);
            }
        }
    }
}
