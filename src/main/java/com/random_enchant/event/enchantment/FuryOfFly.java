package com.random_enchant.event.enchantment;

import com.random_enchant.RandomEnchant;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.util.AdvancementHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = RandomEnchant.MOD_ID)
public class FuryOfFly {
    @SubscribeEvent
    public static void attackMobEvent(AttackEntityEvent event) {
        Entity target = event.getTarget();
        Level level = target.level();
        if (level.isClientSide()) return;
        LivingEntity attacker = event.getEntity();
        if (!attacker.isAlive() || !target.isAlive()) return;
        if (!(target instanceof LivingEntity livingTarget)) return;

        InteractionHand hand = attacker.getUsedItemHand();
        ItemStack weapon = attacker.getItemInHand(hand);
        if (attacker instanceof Player player) {
            if (player.getCooldowns().isOnCooldown(weapon.getItem())) return;
        }
        int lvl = ModEnchantHelper.getEnchantmentLevel(weapon, ModEnchantments.FURY_OF_FLY);
        if (lvl <= 0) return;

        spawnBee((ServerLevel) level, livingTarget, lvl);
        if (attacker instanceof ServerPlayer serverPlayer)
            AdvancementHelper.grantAdvancement(serverPlayer, "enchant/trigger_fly_of_fury", "trigger_fly_of_fly");
        if (attacker instanceof Player player) player.getCooldowns().addCooldown(weapon.getItem(), 50);
    }

    private static void spawnBee(ServerLevel level, LivingEntity target, int count) {
        int beeCount = Math.min(count, 20);
        double tx = target.getX();
        double ty = target.getY() + 1;
        double tz = target.getZ();

        for (int i = 0; i < beeCount; i++) {
            Bee bee = new FuryBee(level, target);
            bee.setPos(tx, ty, tz);
            bee.setTarget(target);
            level.addFreshEntity(bee);
            bee.setCustomName(Component.translatable("entity.minecraft.bee.random_enchant.spawn_name"));
            bee.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 11451419, count * 2));
            bee.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 11451419, count * 2));
            bee.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 11451419, (int) (count * 0.2)));
            bee.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 11451419, count * 2));
            bee.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 11451419, count * 2));
        }

        // 粒子效果只生成一次，而非每只蜜蜂都生成
        showBeeParticleEffect(level, target);
    }

    /**
     * 自定义蜜蜂实体：攻击后自毁，目标消失后自毁。
     * 提取为具名类避免每次循环创建匿名内部类。
     */
    private static class FuryBee extends Bee {
        FuryBee(Level level, LivingEntity target) { super(EntityType.BEE, level); }

        @Override
        public boolean doHurtTarget(@NotNull Entity target) {
            boolean result = super.doHurtTarget(target);
            if (result) this.discard();
            return result;
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity currentTarget = this.getTarget();
            if (currentTarget == null || !currentTarget.isAlive()) {
                this.discard();
            }
        }
    }

    private static void showBeeParticleEffect(ServerLevel world, LivingEntity target) {
        final int PARTICLE_COUNT = 20;
        final double RADIUS = 2.0;
        final double SPEED = 0.08;
        double tx = target.getX();
        double ty = target.getY();
        double tz = target.getZ();

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double angle = 2 * Math.PI * i / PARTICLE_COUNT;
            double x = tx + RADIUS * Math.sin(angle);
            double z = tz + RADIUS * Math.cos(angle);

            // 计算从中心指向粒子位置的方向（向外）
            double dx = x - tx;
            double dy = 0; // y - pos.y = 0 since particle is at same y
            double dz = z - tz;
            double len = Math.sqrt(dx * dx + dz * dz);
            double nx = dx / len;
            double nz = dz / len;

            world.sendParticles(ParticleTypes.FLASH, x, ty + 0.3, z, 10, nx * SPEED, 0, nz * SPEED, 0.01);
            world.sendParticles(ParticleTypes.ENCHANTED_HIT, x, ty + 0.3, z, 10, nx * SPEED, 0, nz * SPEED, 0.01);
            world.sendParticles(ParticleTypes.ENCHANT, x, ty + 0.3, z, 10, nx * SPEED * 1.1, 0, nz * SPEED * 1.1, 0.03);
        }
    }
}
