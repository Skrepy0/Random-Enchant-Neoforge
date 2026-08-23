package com.random_enchant.item.custom.potion;

import com.random_enchant.enchantment.ModEnchantHelper;
import java.util.Random;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.EffectCures;

public class MemoryPotion extends Item {
    private static final int DRINK_DURATION = 40;
    private static final Random RANDOM = new Random();

    public MemoryPotion(Properties properties) { super(properties); }

    /**
     * 在玩家周围创建光环粒子效果
     */
    public static void createOminousAura(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        Vec3 pos = player.position();

        // 在玩家周围创建3个光环
        for (int ring = 0; ring < 3; ring++) {
            double radius = 1.0 + ring * 0.5;
            int particles = 20 + ring * 10;

            for (int i = 0; i < particles; i++) {
                double angle = 2 * Math.PI * i / particles;
                double x = pos.x + radius * Math.cos(angle);
                double y = pos.y + 0.5 + ring * 0.2;
                double z = pos.z + radius * Math.sin(angle);

                // 添加一些随机性
                x += (RANDOM.nextDouble() - 0.5) * 0.2;
                y += (RANDOM.nextDouble() - 0.5) * 0.2;
                z += (RANDOM.nextDouble() - 0.5) * 0.2;

                level.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS, x, y, z, 1, 0, 0, 0, 0.02);
            }
        }
    }

    /**
     * 创建旋转的粒子螺旋
     */
    public static void createOminousSpiral(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        Vec3 pos = player.position();

        double time = level.getGameTime() * 0.1; // 随时间旋转

        for (int i = 0; i < 50; i++) {
            double angle = time + 2 * Math.PI * i / 50;
            double radius = 2.0;
            double height = (i / 50.0) * 3.0;

            double x = pos.x + radius * Math.cos(angle);
            double y = pos.y + 1.0 + height;
            double z = pos.z + radius * Math.sin(angle);

            level.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 1, 0, 0, 0, 0.05);
        }
    }

    /**
     * 创建粒子爆炸效果
     */
    public static void createOminousExplosion(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        Vec3 center = player.position().add(0, 1, 0);

        for (int i = 0; i < 100; ++i) {
            // 随机方向
            double phi = RANDOM.nextDouble() * 2 * Math.PI;
            double theta = RANDOM.nextDouble() * Math.PI;

            double speed = 0.5 + RANDOM.nextDouble() * 0.5;
            double xVel = speed * Math.sin(theta) * Math.cos(phi);
            double yVel = speed * Math.cos(theta);
            double zVel = speed * Math.sin(theta) * Math.sin(phi);

            level.sendParticles(ParticleTypes.FLAME, center.x, center.y, center.z, 1, xVel, yVel, zVel, 0.1);
        }
    }

    private static boolean teleportPlayer(ServerPlayer player, ServerLevel targetLevel, double x, double y, double z,
                                          float yaw, float pitch) {

        // 检查是否需要跨维度传送
        if (player.level().dimension() != targetLevel.dimension()) {
            // 跨维度传送
            player.teleportTo(targetLevel, x, y, z, net.minecraft.world.entity.RelativeMovement.ALL, yaw, pitch);
        } else {
            // 同维度传送
            player.connection.teleport(x, y, z, yaw, pitch);
        }

        // 设置玩家朝向
        player.setYRot(yaw);
        player.setXRot(pitch);
        player.setYHeadRot(yaw);
        player.setDeltaMovement(0, 0, 0);
        player.resetFallDistance();
        return true;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        super.finishUsingItem(stack, level, entityLiving);
        if (entityLiving instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, stack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }
        boolean flag = false;
        if (!level.isClientSide) {
            entityLiving.removeEffectsCuredBy(EffectCures.HONEY);
            if (entityLiving instanceof Player player) {
                if (player instanceof ServerPlayer serverPlayer) {
                    ResourceKey<Level> dimension = serverPlayer.getRespawnDimension();
                    BlockPos respawnPosition = serverPlayer.getRespawnPosition();
                    float respawnAngle = serverPlayer.getRespawnAngle();
                    ServerLevel targetLevel = serverPlayer.server.getLevel(dimension);
                    if (respawnPosition == null) {
                        respawnPosition = targetLevel.getSharedSpawnPos();
                        respawnAngle = targetLevel.getSharedSpawnAngle();
                    }
                    boolean result = teleportPlayer(serverPlayer, targetLevel, respawnPosition.getX() + 0.5,
                                                    respawnPosition.getY(), respawnPosition.getZ() + 0.5, respawnAngle,
                                                    player.getVoicePitch());
                    if (!result) {
                        serverPlayer.sendSystemMessage(net.minecraft.network.chat.Component.literal("§c传送失败"));
                    } else {
                        flag = true;
                    }
                }
            }
        }
        if (flag) {
            if (entityLiving instanceof ServerPlayer serverPlayer) playSoundAndShowParticle(serverPlayer);
            level.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(),
                            SoundEvents.TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundSource.PLAYERS, 20.0F, 1.0F);
        }

        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (entityLiving instanceof Player player && !player.hasInfiniteMaterials()) {
                if (ModEnchantHelper.getEnchantmentLevel(stack, Enchantments.INFINITY) <= 0) {
                    ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
                    if (!player.getInventory().add(itemstack)) {
                        player.drop(itemstack, false);
                    }
                }
            }
            return stack;
        }
    }

    private void playSoundAndShowParticle(ServerPlayer serverPlayer) {
        createOminousAura(serverPlayer);
        createOminousSpiral(serverPlayer);
        createOminousExplosion(serverPlayer);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return DRINK_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
