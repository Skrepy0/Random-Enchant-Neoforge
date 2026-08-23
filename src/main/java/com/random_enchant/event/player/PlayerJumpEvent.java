package com.random_enchant.event.player;

import com.random_enchant.data.nbt.DoubleJumpData;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class PlayerJumpEvent {
    // 存储玩家是否有摔落保护的映射
    private static final Map<UUID, Boolean> fallProtectionMap = new HashMap<>();
    // 存储玩家获得保护时的位置，用于检测是否已经移动
    private static final Map<UUID, Vec3> protectionStartPositions = new HashMap<>();
    // 配置常量
    private static final float PARTICLE_SPEED = 0.1f;
    private static final float JUMP_SOUND_VOLUME = 1.0f;
    private static final float JUMP_SOUND_PITCH = 1.2f;

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {
        event.getServer().getPlayerList().getPlayers().forEach(PlayerJumpEvent::checkPlayerLanding);
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        UUID playerId = player.getUUID();
        fallProtectionMap.remove(playerId);
        protectionStartPositions.remove(playerId);
    }

    @SubscribeEvent
    public static void onPlayerJump(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        boolean isJumpPressed = mc.options.keyJump.isDown();
        Player player = mc.player;
        DoubleJumpData.DoubleJumpState state = DoubleJumpData.get(player);
        boolean flag = false;
        if (isJumpPressed) {
            if (player.mayFly()) return;

            if (player.onGround()) {
                state.reset();
                return;
            }

            if (state.hasUsedDoubleJump()) return;
            if (player.isInWater() || player.isInLava()) return;

            int jumpLevel = ModEnchantHelper.getEnchantmentLevel(player.getItemBySlot(EquipmentSlot.FEET),
                                                                 ModEnchantments.DOUBLE_JUMP);

            if (jumpLevel > 0) {
                if (jumpLevel > 1) {
                    // 设置摔落保护
                    fallProtectionMap.put(player.getUUID(), true);
                    // 记录保护开始时的位置
                    protectionStartPositions.put(player.getUUID(), player.position());
                }
                // 执行二段跳
                player.addDeltaMovement(new Vec3(0, jumpLevel * 0.45f, 0));
                state.setUsedDoubleJump(true);
                player.resetFallDistance();
                flag = true;
                // 添加粒子效果
                spawnJumpParticles(player, jumpLevel);
            }
        }
        if (flag) {
            float pitch = new Random().nextFloat();
            player.playSound(SoundEvents.PLAYER_BREATH, 0.5f, pitch);
        }
    }

    private static void checkPlayerLanding(Player player) {
        if (player.level().isClientSide()) return;

        UUID playerId = player.getUUID();

        // 检查玩家是否有摔落保护
        boolean hasProtection = fallProtectionMap.getOrDefault(playerId, false);
        if (!hasProtection) return;

        // 获取保护开始时的位置
        Vec3 startPos = protectionStartPositions.get(playerId);
        if (startPos == null) {
            fallProtectionMap.put(playerId, false);
            return;
        }

        // 计算玩家移动的距离
        Vec3 currentPos = player.position();
        double distanceMoved =
                Math.sqrt(Math.pow(currentPos.x - startPos.x, 2) + Math.pow(currentPos.y - startPos.y, 2) +
                          Math.pow(currentPos.z - startPos.z, 2));

        // 只有当玩家移动了一定距离后，才开始检测落地
        if (distanceMoved < 0.1) {
            player.resetFallDistance();
            return;
        }

        // 简单的落地检测：玩家是否站在地面上
        if (player.onGround()) {
            // 玩家已经落地，移除保护
            player.resetFallDistance();
            fallProtectionMap.put(playerId, false);
            protectionStartPositions.remove(playerId);
        } else {
            // 玩家还在空中，保持保护并重置摔落距离
            player.resetFallDistance();
        }
    }

    /**
     * 生成二段跳粒子效果
     */
    private static void spawnJumpParticles(Player player, int jumpLevel) {
        if (player.level().isClientSide()) {
            // 根据跳跃等级生成不同数量和类型的粒子
            int particleCount = 8 + jumpLevel * 4;

            // 粒子生成位置（玩家脚下）
            double posX = player.getX();
            double posY = player.getY() - 0.2;
            double posZ = player.getZ();

            // 生成圆形粒子效果
            for (int i = 0; i < particleCount; i++) {
                // 计算圆形位置
                double angle = (2 * Math.PI * i) / particleCount;
                double radius = 0.5 + jumpLevel * 0.1;
                double offsetX = Math.cos(angle) * radius;
                double offsetZ = Math.sin(angle) * radius;

                // 根据跳跃等级选择粒子类型
                var particleType = ParticleTypes.FLAME;

                // 添加粒子
                player.level().addParticle(particleType, posX + offsetX, posY, posZ + offsetZ, offsetX * PARTICLE_SPEED,
                                           0.3 + jumpLevel * 0.05, offsetZ * PARTICLE_SPEED);
            }

            // 额外添加一些垂直向上的粒子
            for (int i = 0; i < jumpLevel * 3; i++) {
                player.level().addParticle(ParticleTypes.CLOUD, posX + (Math.random() - 0.5) * 0.3, posY,
                                           posZ + (Math.random() - 0.5) * 0.3, 0, 0.5 + Math.random() * 0.3, 0);
            }
        }
    }
}
