package com.random_enchant.event.enchantment.arrow_event;

import com.random_enchant.Config;
import com.random_enchant.RandomEnchant;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

@EventBusSubscriber(modid = RandomEnchant.MOD_ID)
public class ArrowEvent {
    @SubscribeEvent
    public static void arrowHitEntity(ProjectileImpactEvent event) {
        Entity entity = event.getProjectile();
        Entity target = event.getEntity();
        if (entity instanceof AbstractArrow arrow) {
            ItemStack stack = arrow.getWeaponItem();
            int explodeLevel = ModEnchantHelper.getEnchantmentLevel(stack, ModEnchantments.EXPLODE);
            if (explodeLevel > 0) {
                if (target != null) {
                    explode(explodeLevel, arrow.level(), arrow.getX(), arrow.getY(), arrow.getZ(), arrow.getOwner());
                } else {
                    explode(explodeLevel * 0.2f, arrow.level(), arrow.getX(), arrow.getY(), arrow.getZ(),
                            arrow.getOwner());
                }
                arrow.kill();
            }
        }
    }

    private static void explode(float power, Level level, double x, double y, double z, Entity entity) {
        float f = 4.0F + (float) (power * 0.5);
        Level.ExplosionInteraction interaction = Config.getExplodeDestroyBlock()
                                                         ? Level.ExplosionInteraction.TNT
                                                         : net.minecraft.world.level.Level.ExplosionInteraction.NONE;
        level.explode(entity, x, y, z, f, interaction);
    }
}
