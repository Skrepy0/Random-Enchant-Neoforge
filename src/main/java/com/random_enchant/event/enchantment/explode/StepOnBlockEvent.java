package com.random_enchant.event.enchantment.explode;

import com.random_enchant.Config;
import com.random_enchant.RandomEnchant;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
@EventBusSubscriber(modid = RandomEnchant.MOD_ID)
public class StepOnBlockEvent {
    @SubscribeEvent
    public static void onStepOnBlockEvent(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        BlockPos pos = entity.getOnPos();
        int level = BlockEnchantmentStorage.getLevel(ModEnchantments.EXPLODE, pos);
        if (level > 0) {
            explode(level * 0.3f, entity.level(), entity.getX(), entity.getY(), entity.getZ(), null);
            if (!Config.getExplodeDestroyBlock() &&
                !(BlockEnchantmentStorage.getLevel(Enchantments.INFINITY, pos) > 0)) {
                entity.level().destroyBlock(pos, false);
            }
        }
    }
    private static void explode(float power, Level level, double x, double y, double z, Entity entity) {
        float f = 4.0F + (float) (power * 0.5);
        Level.ExplosionInteraction interaction =
                Config.getExplodeDestroyBlock() ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE;
        level.explode(entity, x, y, z, f, interaction);
    }
}
