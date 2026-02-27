package com.random_enchant.event.enchantment;

import com.random_enchant.Config;
import com.random_enchant.RandomEnchant;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingUseTotemEvent;

import static com.random_enchant.enchantment.ModEnchantHelper.getEnchantmentLevel;
@EventBusSubscriber(modid = RandomEnchant.MOD_ID)
public class UndyingTotem {
    private static void explode(float power, Level level, double x, double y, double z, Entity entity) {
        float f = 4.0F + (float) (power * 0.5);
        Level.ExplosionInteraction interaction =
                Config.getExplodeDestroyBlock() ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE;
        level.explode(entity, x, y, z, f, interaction);
    }

    @SubscribeEvent
    public static void useTotem(LivingUseTotemEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (level.isClientSide) return;
        ItemStack totem = event.getTotem();
        DamageSource damageSource = event.getSource();
        int blastProtectLevel = getEnchantmentLevel(totem, Enchantments.BLAST_PROTECTION); // 爆炸保护
        if (blastProtectLevel > 0) {
            if (damageSource.getEntity() != null && damageSource.getDirectEntity() != null) {
                explode(blastProtectLevel - 1, level, entity.getX(), entity.getY(0.0625), entity.getZ(),
                        damageSource.getDirectEntity());
            }
        }

        int channelingLevel = getEnchantmentLevel(totem, Enchantments.CHANNELING); // 引雷
        if (channelingLevel > 0) {
            if (level instanceof ServerLevel) {
                if (damageSource.getEntity() != null && damageSource.getDirectEntity() != null) {
                    BlockPos blockPos = null;
                    if (entity.getLastHurtByMob() != null) {
                        blockPos = entity.getLastHurtByMob().getOnPos();
                    }
                    LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
                    if (lightningBolt != null) {
                        if (blockPos != null) {
                            lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
                        }
                        lightningBolt.setCause(damageSource.getDirectEntity() instanceof ServerPlayer
                                                       ? (ServerPlayer) damageSource.getDirectEntity()
                                                       : null);
                        level.addFreshEntity(lightningBolt);
                        SoundEvent soundEvent = SoundEvents.LIGHTNING_BOLT_THUNDER;
                        entity.playSound(soundEvent, 5, 1.0F);
                    }
                }
            }
        }
    }
}
