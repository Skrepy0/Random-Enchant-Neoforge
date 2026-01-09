package com.random_enchant.item.custom.tool;

import com.random_enchant.RandomEnchant;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PearlSpear extends Item {
    private final int COOL_DOWN_TIME = 200;

    public PearlSpear(Properties properties) {
        super(properties.durability(128)
                      .rarity(Rarity.EPIC)
                      .stacksTo(1)
                      .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
                      .attributes(createAttributeModifiers()));
    }

    private static ItemAttributeModifiers createAttributeModifiers() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                     new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, Tiers.DIAMOND.getAttackDamageBonus() + 1,
                                           AttributeModifier.Operation.ADD_VALUE

                                           ),
                     EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                     new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4F, AttributeModifier.Operation.ADD_VALUE),
                     EquipmentSlotGroup.MAINHAND)
                .add(Attributes.MOVEMENT_SPEED,
                     new AttributeModifier(
                             ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "pearl_spear_speed_boot"),
                             1.14514, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                     EquipmentSlotGroup.HAND)
                .build();
    }

    private static int getItemDamage(int unbreakingLevel) {
        Random random = new Random();
        int rand = random.nextInt(100);
        if (unbreakingLevel == 0) {
            if (rand <= 20) {
                return 0;
            }
            return 1;
        } else if (unbreakingLevel == 1) {
            if (rand <= 40) {
                return 0;
            }
            return 1;
        } else if (unbreakingLevel == 2) {
            if (rand <= 60) {
                return 0;
            }
            return 1;
        } else if (unbreakingLevel > 2) {
            if (rand <= 80) {
                return 0;
            }
            return 1;
        }
        return 1;
    }

    private static void spawnBee(Level world, Entity target, int count, LivingEntity livingEntity) {
        if (target == null || world.isClientSide() || livingEntity == null)
            return;

        for (int i = 0; i < Math.min(count, 20); i++) {
            // 修改点1：实体创建方式
            Bee bee = new Bee(EntityType.BEE, world) {
                @Override
                public boolean doHurtTarget(Entity target) {
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand usedHand) {
        ItemStack stack = user.getItemInHand(usedHand);
        if (level.isClientSide)
            return InteractionResultHolder.pass(stack);
        if (user.getCooldowns().isOnCooldown(this))
            return InteractionResultHolder.pass(stack);
        // 闪现user(3D)
        teleportUser(stack, 10, user, level, false);
        // 损耗耐久
        if (!user.isCreative()) {
            int unbreakingLevel = ModEnchantHelper.getEnchantmentLevel(stack, level, Enchantments.UNBREAKING);
            stack.setDamageValue(stack.getDamageValue() + getItemDamage(unbreakingLevel));
        }
        // 播放音效
        level.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.TOTEM_USE, SoundSource.AMBIENT, 0.2F,
                        1.4F);
        // 快速装填附魔等级
        int quickCharge = ModEnchantHelper.getEnchantmentLevel(stack, level, Enchantments.QUICK_CHARGE);
        user.getCooldowns().addCooldown(this, getRealCoolDownTime(quickCharge));
        // 粒子效果
        showParticleEffect(level, user);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Player user = (Player) attacker;
        Level world = user.level();
        if (world.isClientSide)
            return false;
        if (user.getCooldowns().isOnCooldown(this))
            return false;
        Vec3 playerVelocity = user.getDeltaMovement();
        Vec3 entityVelocity = target.getDeltaMovement();
        Vec3 playerPos = user.position();
        Vec3 entityPos = target.position();
        Vec3 playerToEntity = entityPos.subtract(playerPos);
        // 计算投影长度
        double playerVeLength = playerVelocity.dot(playerToEntity.normalize());
        double entityVeLength = entityVelocity.dot(playerToEntity.normalize());
        double dV;
        if (user.onGround())
            dV = entityVeLength - playerVeLength > 0 ? entityVeLength - playerVeLength : 0;
        else
            dV = playerVeLength - entityVeLength > 0 ? playerVeLength - entityVeLength : 0;

        float damage = (float) (dV * 10 + 9);
        stack.setDamageValue(1);
        teleportUser(stack, 10, user, world, true);
        // 引雷附魔
        int channelingLevel = ModEnchantHelper.getEnchantmentLevel(stack, world, Enchantments.CHANNELING);
        if (channelingLevel > 0) {
            spawnLightningEntity(channelingLevel, world, target);
        }
        // 播放声音
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.TOTEM_USE, SoundSource.AMBIENT, 0.2F,
                        1.0F);
        // 快速装填附魔等级
        int quickCharge = ModEnchantHelper.getEnchantmentLevel(stack, world, Enchantments.QUICK_CHARGE);
        user.getCooldowns().addCooldown(this, getRealCoolDownTime(quickCharge));
        // 物品损耗
        if (!user.isCreative()) {
            int unbreakingLevel = ModEnchantHelper.getEnchantmentLevel(stack, world, Enchantments.UNBREAKING);
            stack.setDamageValue(stack.getDamageValue() + getItemDamage(unbreakingLevel));
        }
        // 粒子效果
        showParticleEffect(world, user);
        // 实体伤害
        target.hurt(user.damageSources().playerAttack(user), damage);
        // System.out.println(damage);
        int furyOfFlyLevel = ModEnchantHelper.getEnchantmentLevel(stack, world, ModEnchantments.FURY_OF_FLY);
        if (furyOfFlyLevel > 0) {
            spawnBee(world, target, furyOfFlyLevel, user);
        }

        ServerLevel serverLevel = (ServerLevel) world;
        serverLevel.sendParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getY() + 0.5, target.getZ(),
                                  10, // 粒子数量
                                  0.5, // X方向速度
                                  0.5, // Y方向速度
                                  0.5, // Z方向速度
                                  0.03 // 基础速度（会被方向向量缩放）
        );
        return true;
    }

    private int getRealCoolDownTime(int quickCharge) {
        double realCoolDownTime = COOL_DOWN_TIME;
        if (quickCharge > 0) {
            realCoolDownTime = ((double) 11451 / (quickCharge + 4.6) - 20) * 0.08;
        }
        return (int) realCoolDownTime;
    }

    private BlockPos findGroundPosition(ServerLevel world, BlockPos pos) {
        // 从高空开始向下寻找第一个非空气方块
        for (int y = world.getMaxBuildHeight(); y >= world.getMinBuildHeight(); y--) {
            BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
            if (!world.getBlockState(checkPos).isAir()) {
                return checkPos.above(); // 返回到地面之上
            }
        }
        return pos; // 如果没有找到地面，返回原位置
    }

    private void teleportUser(ItemStack stack, float distance, Player player, Level level, boolean isPlane) {
        float spawnDistance = distance;
        final float DEGREES_TO_RADIANS = 0.017453292F;

        // 保存当前状态
        Vec3 originalVelocity = player.getDeltaMovement();
        boolean wasOnGround = player.onGround();

        while (spawnDistance > 0) {
            // 获取朝向
            float yawRadians = player.getYRot() * DEGREES_TO_RADIANS;
            float pitch = player.getXRot() * DEGREES_TO_RADIANS;

            double offsetX = -Math.sin(yawRadians) * spawnDistance;
            double offsetY = isPlane ? 0 : -Math.sin(pitch) * spawnDistance;
            double offsetZ = Math.cos(yawRadians) * spawnDistance;

            double tpX = player.getX() + offsetX;
            double tpY = player.getY() + offsetY;
            double tpZ = player.getZ() + offsetZ;

            // 检查目标位置
            BlockPos targetPos = BlockPos.containing(tpX, tpY, tpZ);
            if (level.getBlockState(targetPos).getBlock() == Blocks.AIR) {
                showTrack(level, player.position(), new Vec3(tpX, tpY, tpZ));
                int sweepingEdgeLevel = ModEnchantHelper.getEnchantmentLevel(stack, level, Enchantments.SWEEPING_EDGE);
                damageEntitiesNearTrack(level, player.position(), new Vec3(tpX, tpY, tpZ), player, sweepingEdgeLevel);

                // 传送玩家
                player.teleportTo((ServerLevel) level, tpX, tpY, tpZ, EnumSet.noneOf(RelativeMovement.class),
                                  Mth.wrapDegrees(player.getYRot()), Mth.wrapDegrees(player.getXRot()));

                // 恢复动量
                player.setDeltaMovement(originalVelocity);
                player.hurtMarked = true;
                player.setOnGround(wasOnGround);

                // 服务器同步
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(player));
                }
                break;
            }
            spawnDistance -= 0.2F;
        }
    }

    private void showParticleEffect(Level world, Player player) {
        final int PARTICLE_COUNT = 20;
        final double RADIUS = 2.0;
        Vec3 pos = player.position();

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double angle = 2 * Math.PI * i / PARTICLE_COUNT;
            double x = player.getX() + RADIUS * Math.sin(angle);
            double y = player.getY();
            double z = player.getZ() + RADIUS * Math.cos(angle);

            // 在服务器端发送粒子数据包给所有客户端
            if (!world.isClientSide()) {
                ServerLevel serverWorld = (ServerLevel) world;
                double speed = 0.08;

                // 计算从中心指向粒子位置的方向（向外）
                Vec3 direction1 = new Vec3(x - pos.x, y - pos.y, z - pos.z).normalize(); // Vec3d → Vec3

                // 使用 sendParticles 方法，通过速度参数设置粒子运动方向
                serverWorld.sendParticles(ParticleTypes.END_ROD, x, y + 0.3, z, // 粒子位置
                                          10, // 粒子数量
                                          direction1.x * speed, // X方向速度
                                          direction1.y * speed, // Y方向速度
                                          direction1.z * speed, // Z方向速度
                                          0.01 // 基础速度（会被方向向量缩放）
                );
                serverWorld.sendParticles(ParticleTypes.FLASH, x, y + 0.3, z, // 粒子位置
                                          10, // 粒子数量
                                          direction1.x * speed, // X方向速度
                                          direction1.y * speed, // Y方向速度
                                          direction1.z * speed, // Z方向速度
                                          0.01 // 基础速度（会被方向向量缩放）
                );
                serverWorld.sendParticles(ParticleTypes.DRAGON_BREATH, x, y + 0.3, z, // 粒子位置
                                          10, // 粒子数量
                                          direction1.x * speed * 1.1, // X方向速度
                                          direction1.y * speed * 1.1, // Y方向速度
                                          direction1.z * speed * 1.1, // Z方向速度
                                          0.03 // 基础速度（会被方向向量缩放）
                );
            }
        }
    }

    private double distanceToLineSegment(Vec3 point, Vec3 lineStart, Vec3 lineEnd) {
        // 线段向量
        Vec3 lineVec = lineEnd.subtract(lineStart);
        // 点到线段起点的向量
        Vec3 pointToStart = point.subtract(lineStart);

        // 计算投影比例 t
        double lineLengthSquared = lineVec.lengthSqr();
        if (lineLengthSquared == 0.0) {
            // 线段退化为点
            return pointToStart.length();
        }
        double t = pointToStart.dot(lineVec) / lineLengthSquared;

        // 将 t 限制在 [0,1] 范围内
        t = Mth.clamp(t, 0.0, 1.0);

        // 计算线段上最近的点
        Vec3 closestPoint = lineStart.add(lineVec.scale(t));

        // 返回到最近点的距离
        return point.distanceTo(closestPoint);
    }

    private boolean isEntityNearTrack(LivingEntity entity, Vec3 startPos, Vec3 endPos, double maxDistance) {
        Vec3 entityPos = entity.position();

        // 计算实体到线段的最短距离
        double distance = distanceToLineSegment(entityPos, startPos, endPos);

        return distance <= maxDistance;
    }

    private void damageEntitiesNearTrack(Level world, Vec3 startPos, Vec3 endPos, Player player,
                                         int sweepingEdgeLevel) {
        final double DAMAGE_RADIUS = 3.0;
        final double SEARCH_RADIUS = 20.0;
        final float DAMAGE_AMOUNT = 8.0f + sweepingEdgeLevel; // 调整伤害值
        AABB searchBox =
                new AABB(player.getX() - SEARCH_RADIUS, player.getY() - SEARCH_RADIUS, player.getZ() - SEARCH_RADIUS,
                         player.getX() + SEARCH_RADIUS, player.getY() + SEARCH_RADIUS, player.getZ() + SEARCH_RADIUS);
        List<LivingEntity> nearbyEntities =
                world.getEntitiesOfClass(LivingEntity.class, searchBox,
                                         entity -> entity != player && entity.isAlive() // 排除玩家自己和死亡的生物
                );

        for (LivingEntity entity: nearbyEntities) {
            if (isEntityNearTrack(entity, startPos, endPos, DAMAGE_RADIUS)) {
                DamageSource damageSource = world.damageSources().playerAttack(player);
                boolean damageSuccess = entity.hurt(damageSource, DAMAGE_AMOUNT);
                // 4. 粒子效果生成逻辑变更（仅在服务端执行并同步给客户端）
                if (damageSuccess && !world.isClientSide() && world instanceof ServerLevel serverLevel) {
                    // 在服务端生成粒子并发送给附近客户端
                    ClientboundLevelParticlesPacket particlesPacket =
                            new ClientboundLevelParticlesPacket(ParticleTypes.DAMAGE_INDICATOR, // 粒子类型
                                                                false, // 是否远距离渲染
                                                                entity.getX(), entity.getY(0.5), entity.getZ(), // 位置
                                                                0.3f, 0.3f, 0.3f, // 偏移量 (deltaX/Y/Z)
                                                                0.02f, // 速度
                                                                5 // 粒子数量
                            );

                    // 向追踪此实体的所有玩家发送粒子数据包
                    serverLevel.getChunkSource().broadcastAndSend(entity, particlesPacket);

                    // 备选：直接使用世界方法（会发送给所有能看到该位置的玩家）
                    // serverLevel.sendParticles(
                    //         ParticleTypes.DAMAGE_INDICATOR,
                    //         entity.getX(), entity.getY(0.5), entity.getZ(),
                    //         5, // 数量
                    //         0.3, 0.3, 0.3, // 随机偏移
                    //         0.02 // 速度
                    // );
                }
            }
        }
    }

    private void showTrack(Level world, Vec3 startPos, Vec3 endPos) {
        final int PARTICLE_COUNT = 50;

        for (int i = 0; i <= PARTICLE_COUNT; i++) {
            double t = (double) i / PARTICLE_COUNT;

            // 线性插值计算位置
            double x = startPos.x + t * (endPos.x - startPos.x);
            double y = startPos.y + t * (endPos.y - startPos.y);
            double z = startPos.z + t * (endPos.z - startPos.z);

            if (!world.isClientSide()) {
                ServerLevel serverWorld = (ServerLevel) world;

                // 根据位置在路径上的比例调整粒子特性
                int particleCount = t > 0.7 ? 5 : 3; // 末端更多粒子
                double speed = 0.03 + t * 0.02; // 逐渐加速

                // 主轨迹粒子
                serverWorld.sendParticles(ParticleTypes.FIREWORK, x, y, z, particleCount, 0.1, 0.1, 0.1, speed);

                // 辅助粒子效果
                if (i % 3 == 0) {
                    serverWorld.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, x, y, z, 1, 0.05, 0.05, 0.05, 0.01);
                }
                // 辅助粒子效果
                if (i % 4 == 0) {
                    serverWorld.sendParticles(ParticleTypes.HAPPY_VILLAGER, x, y, z, 1, 0.05, 0.05, 0.05, 0.01);
                }

                // 在起点和终点添加特殊效果
                if (i == 0 || i == PARTICLE_COUNT) {
                    serverWorld.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 10, 0.3, 0.3, 0.3, 0.1);
                }
            }
        }
    }

    private void spawnLightningEntity(int channelingLevel, Level world, Entity entity) {
        Random random = new Random();
        if (channelingLevel > 0 && world instanceof ServerLevel serverWorld) {
            // 限制最大闪电数量，防止性能问题
            int lightningCount = Math.min(channelingLevel, 8); // 最多8道闪电

            for (int i = 0; i < lightningCount; i++) {
                // 计算随机偏移位置，避免所有闪电都在同一点
                double offsetX = (random.nextDouble() - 0.5) * 10.0; // ±5格范围
                double offsetZ = (random.nextDouble() - 0.5) * 10.0;

                // 在目标实体位置附近生成闪电
                BlockPos lightningPos = entity.blockPosition().offset((int) offsetX, 0, (int) offsetZ);

                // 找到该位置的地面高度 (findGroundPosition方法需要你根据原有逻辑自行适配)
                BlockPos groundPos = findGroundPosition(serverWorld, lightningPos); // 注意参数名通常为 `level`

                // 创建闪电实体
                LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
                // 设置位置
                lightningBolt.setPos(Vec3.atBottomCenterOf(groundPos));
                // 在世界中生成闪电 - 方法名改变
                world.addFreshEntity(lightningBolt);

                // 添加音效和粒子效果
                world.playSound(null, // 玩家
                                groundPos.getX(), groundPos.getY(), groundPos.getZ(), // 位置坐标分开
                                SoundEvents.LIGHTNING_BOLT_THUNDER,
                                SoundSource.WEATHER, // 常量名改变
                                5.0F, 1.0F);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip,
                                TooltipFlag tooltipFlag) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("item.tooltip.random_enchant.pearl_spear.detail_description_1"));
            tooltip.add(Component.translatable("item.tooltip.random_enchant.pearl_spear.detail_description_2"));
            tooltip.add(Component.translatable("item.tooltip.random_enchant.pearl_spear.detail_description_3"));
        } else {
            tooltip.add(Component.translatable("item.tooltip.random_enchant.for_shift_tooltip"));
        }
    }

    @Override
    public ItemStack getDefaultInstance() {
        return super.getDefaultInstance();
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }

    @Override
    public int getDefaultMaxStackSize() {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
}
