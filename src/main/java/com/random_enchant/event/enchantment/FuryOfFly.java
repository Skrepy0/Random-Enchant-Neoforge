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
import net.minecraft.world.phys.Vec3;
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
        if (attacker.isAlive() && target.isAlive()) {
            if (target instanceof LivingEntity) {
                InteractionHand hand = attacker.getUsedItemHand();
                ItemStack weapon = attacker.getItemInHand(hand);
                if (attacker instanceof Player player) {
                    if (player.getCooldowns().isOnCooldown(weapon.getItem())) return;
                }
                int lvl = ModEnchantHelper.getEnchantmentLevel(weapon, ModEnchantments.FURY_OF_FLY);
                if (lvl > 0) {
                    spawnBee(level, target, lvl, attacker);
                    if (attacker instanceof ServerPlayer serverPlayer)
                        AdvancementHelper.grantAdvancement(serverPlayer, "enchant/trigger_fly_of_fury",
                                                           "trigger_fly_of_fury");
                    if (attacker instanceof Player player) player.getCooldowns().addCooldown(weapon.getItem(), 50);
                }
            }
        }
    }

    private static void spawnBee(Level world, Entity target, int count, LivingEntity livingEntity) {
        if (target == null || world.isClientSide() || livingEntity == null) return;

        for (int i = 0; i < Math.min(count, 20); i++) {
            // 修改点1：实体创建方式
            Bee bee = new Bee(EntityType.BEE, world) {
                @Override
                public boolean doHurtTarget(@NotNull Entity target) {
                    boolean result = super.doHurtTarget(target);
                    if (result && !this.level().isClientSide()) {
                        this.discard();
                    }
                    return result;
                }

                @Override
                public void tick() {
                    super.tick();
                    // 在服务端持续检查：如果目标无效（死亡、消失等），则立即消失
                    if (!this.level().isClientSide()) {
                        LivingEntity currentTarget = this.getTarget();
                        if (currentTarget == null || !currentTarget.isAlive()) {
                            this.discard();
                        }
                    }
                }
            };

            bee.setPos(target.getX(), target.getY() + 1, target.getZ());
            if (target instanceof LivingEntity) {
                bee.setTarget((LivingEntity) target);
            }
            showBeeParticleEffect(world, livingEntity);
            world.addFreshEntity(bee);
            bee.setCustomName(Component.translatable("entity.minecraft.bee.random_enchant.spawn_name"));
            bee.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 11451419, count * 2));
            bee.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 11451419, count * 2));
            bee.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 11451419, (int) (count * 0.2)));
            bee.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 11451419, count * 2));
            bee.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 11451419, count * 2));
        }
    }

    private static void showBeeParticleEffect(Level world, LivingEntity target) {
        Vec3 pos = target.position();
        final int PARTICLE_COUNT = 20;
        final double RADIUS = 2.0;
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double angle = 2 * Math.PI * i / PARTICLE_COUNT;
            double x = target.getX() + RADIUS * Math.sin(angle);
            double y = target.getY();
            double z = target.getZ() + RADIUS * Math.cos(angle);
            // 在服务器端发送粒子数据包给所有客户端
            if (!world.isClientSide()) {
                ServerLevel serverWorld = (ServerLevel) world;
                double speed = 0.08;

                // 计算从中心指向粒子位置的方向（向外）
                Vec3 direction1 = new Vec3(x - pos.x, y - pos.y, z - pos.z).normalize(); // Vec3d -> Vec3

                // 使用 sendParticles 方法，通过速度参数设置粒子运动方向
                serverWorld.sendParticles(ParticleTypes.FLASH, x, y + 0.3, z, // 粒子位置
                                          10, // 粒子数量
                                          direction1.x * speed, // X方向速度
                                          direction1.y * speed, // Y方向速度
                                          direction1.z * speed, // Z方向速度
                                          0.01 // 基础速度（会被方向向量缩放）
                );
                serverWorld.sendParticles( // spawnParticles -> sendParticles
                        ParticleTypes.ENCHANTED_HIT, x, y + 0.3, z, // 粒子位置
                        10, // 粒子数量
                        direction1.x * speed, // X方向速度
                        direction1.y * speed, // Y方向速度
                        direction1.z * speed, // Z方向速度
                        0.01 // 基础速度（会被方向向量缩放）
                );
                serverWorld.sendParticles( // spawnParticles -> sendParticles
                        ParticleTypes.ENCHANT, x, y + 0.3, z, // 粒子位置
                        10, // 粒子数量
                        direction1.x * speed * 1.1, // X方向速度
                        direction1.y * speed * 1.1, // Y方向速度
                        direction1.z * speed * 1.1, // Z方向速度
                        0.03 // 基础速度（会被方向向量缩放）
                );
            }
        }
    }
}
