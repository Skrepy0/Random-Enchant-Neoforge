package com.random_enchant.entity.custom;

import com.random_enchant.Config;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.entity.ModEntities;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ThrownMace extends ThrowableItemProjectile {

    // 定义不同状态的尺寸（调大尺寸）
    private static final float FLYING_WIDTH = 0.8F; // 飞行时宽度：原0.5F -> 0.8F
    private static final float FLYING_HEIGHT = 0.8F; // 飞行时高度：原0.5F -> 0.8F
    private static final float GROUNDED_WIDTH = 1.0F; // 落地后宽度：原0.7F -> 1.0F
    private static final float GROUNDED_HEIGHT = 0.3F; // 落地后高度：原0.2F -> 0.3F
    private static final int PICKUP_COOLDOWN = 5;
    // 忠诚附魔等级
    private final int loyaltyLevel;
    // 当前状态
    private boolean isGrounded = false;
    private boolean isReturning = false; // 是否正在返回
    private boolean hasHit = false; // 是否已经击中过目标
    private boolean hasBlockChanneling = false;
    private boolean hasEntityChanneling = false;
    private boolean explodeFlag = true;
    // 创建时间和拾取冷却时间（20 ticks = 1秒）
    private int createTick = 0;
    // 返回阶段计时器
    private int returnTimer = 0;

    // 返回时是否造成伤害
    private boolean damageOnReturn = false;

    public ThrownMace(EntityType<? extends ThrownMace> entityType, Level level) {
        super(entityType, level);
        this.createTick = this.tickCount;
        this.loyaltyLevel = 0; // 默认为0，无忠诚附魔
    }

    public ThrownMace(Level level, LivingEntity shooter, ItemStack itemStack) {
        super(ModEntities.THROWN_ITEM.get(), shooter, level);
        this.setItem(itemStack);
        this.setOwner(shooter);
        this.createTick = this.tickCount;

        // 从物品堆中获取忠诚附魔等级
        this.loyaltyLevel = ModEnchantHelper.getEnchantmentLevel(Enchantments.LOYALTY, itemStack);
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

    /**
     * 通过重写 EntityDimensions 来动态改变尺寸
     */
    @Override
    public @NotNull EntityDimensions getDimensions(net.minecraft.world.entity.@NotNull Pose pose) {
        // 根据状态返回不同的尺寸
        if (isGrounded) {
            return EntityDimensions.fixed(GROUNDED_WIDTH, GROUNDED_HEIGHT);
        } else {
            return EntityDimensions.fixed(FLYING_WIDTH, FLYING_HEIGHT);
        }
    }

    @Override
    public void tick() {
        super.tick();

        // 如果有忠诚附魔且没有击中过目标，检查是否应该开始返回
        if (!isReturning && !hasHit && loyaltyLevel > 0) {
            // 如果重锤飞行距离太远或时间太长，开始返回
            if (this.tickCount > 100) { // 飞行5秒后自动返回
                startReturning();
            }
        }

        // 返回逻辑
        if (isReturning) {
            returnTimer++;
            handleReturn();
            // 返回过程中不执行其他逻辑
            return;
        }

        // 更新状态
        boolean newGrounded = this.onGround() && this.getDeltaMovement().lengthSqr() < 0.1;
        if (newGrounded != isGrounded) {
            isGrounded = newGrounded;
            // 状态改变时刷新尺寸
            this.refreshDimensions();
        }

        // 检查是否已经过了拾取冷却时间（1秒）
        boolean canPickup = (this.tickCount - createTick) >= PICKUP_COOLDOWN;

        // 拾取逻辑（没有忠诚附魔或忠诚等级为0时）
        if (!isReturning && isGrounded && canPickup && this.getOwner() instanceof Player owner) {

            // 增加拾取范围（1.5格）
            double pickupRange = 1.5;
            if (this.distanceToSqr(owner) <= (pickupRange * pickupRange)) {
                tryPickupItem(owner);
            }
        }

        if (this.tickCount > 1200) {
            this.discard();
        }

        // 添加忠诚附魔的粒子效果
        if (loyaltyLevel > 0 && !this.isReturning && !this.hasHit && this.tickCount % 10 == 0) {
            spawnLoyaltyParticles();
        }
    }

    /**
     * 开始返回重锤到主人手中
     */
    private void startReturning() {
        if (this.getOwner() == null || this.isReturning) return;

        this.isReturning = true;
        this.returnTimer = 0;
        this.hasHit = false;
        this.isGrounded = false;
        this.damageOnReturn = false; // 返回开始时重置伤害标记

        // 修复音效播放问题 - 使用正确的播放方式
        if (!this.level().isClientSide) {
            this.level().playSound(null, // 第一个参数为null，表示对所有玩家播放
                                   this.getOwner().getX(), this.getOwner().getY(), this.getOwner().getZ(),
                                   SoundEvents.TRIDENT_RETURN, SoundSource.PLAYERS, 1.0F, 0.5F);
        }
    }

    /**
     * 处理返回逻辑 - 简化版：直接飞向玩家并自动拾取
     */
    private void handleReturn() {
        if (this.getOwner() == null) {
            this.discard();
            return;
        }

        LivingEntity owner = (LivingEntity) this.getOwner();

        // 计算返回速度（忠诚等级越高，返回越快）
        float returnSpeed = 0.5F + (loyaltyLevel * 0.3F);

        // 如果距离还很远，可以更快
        double distance = this.position().distanceTo(owner.getEyePosition());
        if (distance > 10.0) {
            returnSpeed *= 1.5F; // 远距离加速
        }

        // 计算向主人移动的方向
        Vec3 ownerPos = owner.getEyePosition();
        Vec3 currentPos = this.position();
        Vec3 direction = ownerPos.subtract(currentPos).normalize();

        // 设置返回速度
        Vec3 newVelocity = direction.scale(returnSpeed);
        this.setDeltaMovement(newVelocity);


        // 添加返回粒子效果
        if (this.level().isClientSide && this.tickCount % 2 == 0) {
            // 根据忠诚等级显示不同颜色的粒子
            net.minecraft.core.particles.ParticleOptions particleType = loyaltyLevel >= 3 ? ParticleTypes.ELECTRIC_SPARK
                                                                        : loyaltyLevel >= 2
                                                                                ? ParticleTypes.FIREWORK
                                                                                : ParticleTypes.ENCHANTED_HIT;

            for (int i = 0; i < 2; i++) {
                this.level().addParticle(particleType, this.getX() + (this.random.nextDouble() - 0.5) * 0.5,
                                         this.getY() + 0.5 + (this.random.nextDouble() - 0.5) * 0.3,
                                         this.getZ() + (this.random.nextDouble() - 0.5) * 0.5, 0.0D, 0.0D, 0.0D);
            }
        }

        // 在返回过程中，每20tick播放一次飞行音效
        if (this.tickCount % 20 == 0 && !this.level().isClientSide) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.TRIDENT_THROW,
                                   SoundSource.PLAYERS, 0.3F, 1.2F + (this.random.nextFloat() * 0.2F));
        }

        // 检查是否到达主人身边（距离小于1.5格）
        double distanceToOwner = currentPos.distanceTo(ownerPos);
        if (distanceToOwner < 1.5) {
            // 播放返回完成的音效
            if (!this.level().isClientSide) {
                this.level().playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.ITEM_PICKUP,
                                       SoundSource.PLAYERS, 0.6F, 1.5F);
            }

            // 直接尝试拾取
            if (owner instanceof Player) {
                tryPickupItem((Player) owner);
            } else {
                // 如果不是玩家，就掉落在地上
                if (!this.level().isClientSide) {
                    owner.spawnAtLocation(this.getItem().copy(), 0.5F);
                }
                this.discard();
            }
            return;
        }

        // 检查返回过程中是否碰撞到其他实体（造成伤害）
        checkEntityCollisionsDuringReturn();
    }

    /**
     * 返回过程中检查实体碰撞
     */
    private void checkEntityCollisionsDuringReturn() {
        // 只有在返回过程中并且可以造成伤害时才检查
        if (!isReturning || !damageOnReturn) return;

        // 获取实体周围的碰撞箱
        AABB boundingBox = this.getBoundingBox().inflate(0.3); // 稍微扩大一点碰撞箱

        // 查找所有可能碰撞的实体
        for (Entity entity: this.level().getEntities(this, boundingBox)) {
            // 排除自己、主人、以及已经击中过的实体
            if (entity == this || entity == this.getOwner() || !(entity instanceof LivingEntity target)) {
                continue;
            }

            // 计算伤害（根据忠诚等级和速度）
            float damage = 3.0F + (loyaltyLevel * 1.0F);
            float knockback = 0.5F;

            // 造成伤害
            target.hurt(this.damageSources().thrown(this, this.getOwner()), damage);

            // 添加击退效果
            Vec3 knockbackVector = this.getDeltaMovement().normalize().scale(knockback);
            target.push(knockbackVector.x, knockbackVector.y * 0.1, knockbackVector.z);

            // 播放击中音效
            if (!this.level().isClientSide) {
                this.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.METAL_HIT,
                                       SoundSource.NEUTRAL, 0.6F, 0.9F + (this.random.nextFloat() * 0.2F));

                // 击中粒子效果
                this.level().broadcastEntityEvent(this, (byte) 3);
            }

            // 标记已经造成过伤害，避免多次伤害
            this.damageOnReturn = false;
            break; // 每次只对一个实体造成伤害
        }
    }

    /**
     * 生成忠诚附魔粒子效果
     */
    private void spawnLoyaltyParticles() {
        if (this.level().isClientSide) {
            for (int i = 0; i < 2; i++) {
                this.level().addParticle(ParticleTypes.ENCHANT, this.getX() + (this.random.nextDouble() - 0.5) * 0.3,
                                         this.getY() + this.random.nextDouble() * 0.5,
                                         this.getZ() + (this.random.nextDouble() - 0.5) * 0.3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    /**
     * 尝试拾取物品
     */
    private void tryPickupItem(Player owner) {
        if (owner.isCreative()) {
            this.discard();
            return;
        }
        // 尝试将物品添加到主人背包
        ItemStack itemToReturn = this.getItem().copy();

        // 播放拾取音效（根据是否成功调整音调）
        boolean success = owner.getInventory().add(itemToReturn);

        if (success) {
            // 播放成功拾取音效
            this.level().playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.ITEM_PICKUP,
                                   SoundSource.PLAYERS, 0.4F, 1.2F);

            // 添加成功，销毁实体
            this.discard();
        } else {
            // 背包已满，掉落物品
            if (!this.level().isClientSide) {
                // 播放掉落音效
                this.level().playSound(null, owner.getX(), owner.getY(), owner.getZ(),
                                       SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.PLAYERS, 0.5F, 0.8F);

                // 掉落物品
                owner.spawnAtLocation(itemToReturn, 0.5F);
                this.discard();
            }
        }
        owner.getCooldowns().addCooldown(Items.MACE, 20);
    }

    /**
     * 添加渲染效果：飞行时的拖尾效果
     */
    @Override
    public void onSyncedDataUpdated(net.minecraft.network.syncher.@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        // 可以在这里添加一些视觉效果
        if (this.level().isClientSide && !isGrounded && !isReturning) {
            // 飞行时产生粒子效果（每5 tick一次）
            if (this.tickCount % 5 == 0 && this.getDeltaMovement().lengthSqr() > 0.1) {
                for (int i = 0; i < 2; i++) {
                    double dx = (this.random.nextDouble() - 0.5) * 0.2;
                    double dy = (this.random.nextDouble() - 0.5) * 0.2;
                    double dz = (this.random.nextDouble() - 0.5) * 0.2;

                    this.level().addParticle(ParticleTypes.CLOUD, this.getX() + dx,
                                             this.getY() + this.getBbHeight() / 2 + dy, this.getZ() + dz, 0.0D, 0.0D,
                                             0.0D);
                }
            }
        }
    }

    @Override
    public @NotNull Item getDefaultItem() {
        return Items.MACE;
    }

    private float getEntityDamage(ItemStack mace) {
        int densityLevel = Math.max(ModEnchantHelper.getEnchantmentLevel(mace, Enchantments.DENSITY), 0);
        int breachLevel = Math.max(ModEnchantHelper.getEnchantmentLevel(mace, Enchantments.BREACH), 0);
        return 6.0f + densityLevel + breachLevel;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();

        // 如果正在返回，忽略所有碰撞
        if (isReturning) {
            return;
        }

        // 如果是主人，忽略伤害（防止自伤）
        if (entity == this.getOwner()) {
            return;
        }

        if (entity instanceof LivingEntity) {
            // 标记已击中目标
            this.hasHit = true;

            // 增加击退效果
            float damage = getEntityDamage(this.getItem());
            float knockback = 1.0F; // 击退强度

            // 造成伤害
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), damage);

            // 添加击退效果
            Vec3 knockbackVector = this.getDeltaMovement().normalize().scale(knockback);
            entity.push(knockbackVector.x, knockbackVector.y * 0.1, knockbackVector.z);

            // 击中实体后的效果
            if (!this.level().isClientSide) {
                this.level().broadcastEntityEvent(this, (byte) 3); // 伤害粒子效果

                // 播放击中音效
                this.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.METAL_HIT,
                                       SoundSource.AMBIENT, 0.8F, 0.9F + (this.random.nextFloat() * 0.2F));
            }
            tryExplode();
            if (!hasEntityChanneling) {
                hasEntityChanneling = true;
                int channelingLevel = ModEnchantHelper.getEnchantmentLevel(Enchantments.CHANNELING, this.getItem());
                if (channelingLevel > 0) {
                    Player attacker = (Player) this.getOwner();
                    Level level = null;
                    if (attacker != null) {
                        level = attacker.level();
                    }
                    if (attacker != null) {
                        attacker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3, 5));
                    }
                    LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
                    MinecraftServer server = level.getServer();
                    DamageSource damageSource = null;
                    if (server != null) {
                        damageSource = new DamageSource(server.registryAccess()
                                                                .registryOrThrow(Registries.DAMAGE_TYPE)
                                                                .getHolderOrThrow(DamageTypes.PLAYER_ATTACK),
                                                        attacker, // 造成伤害的实体
                                                        attacker // 直接造成伤害的实体（如射出的箭）
                        );
                    }
                    BlockPos blockPos = result.getEntity().getOnPos();
                    if (lightningBolt != null) {
                        lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
                        if (damageSource != null) {
                            lightningBolt.setCause(damageSource.getDirectEntity() instanceof ServerPlayer
                                                           ? (ServerPlayer) damageSource.getDirectEntity()
                                                           : null);
                        }
                        level.addFreshEntity(lightningBolt);
                        SoundEvent soundEvent = SoundEvents.LIGHTNING_BOLT_THUNDER;
                        result.getEntity().playSound(soundEvent, 1.0F, 1.0F);
                        this.clearFire();
                    }
                }
            }
        }
        discardEndure();
        // 如果有忠诚附魔，立即开始返回
        if (loyaltyLevel > 0) {
            startReturning();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        // 如果正在返回，忽略方块碰撞（直接穿过去）
        if (isReturning) {
            return;
        }

        super.onHitBlock(result);
        this.setDeltaMovement(Vec3.ZERO);
        this.setOnGround(true);

        // 标记已击中目标
        this.hasHit = true;
        tryExplode();
        if (!hasBlockChanneling) {
            hasBlockChanneling = true;
            int channelingLevel = ModEnchantHelper.getEnchantmentLevel(Enchantments.CHANNELING, this.getItem());
            if (channelingLevel > 0) {
                Player attacker = (Player) this.getOwner();
                Level level = null;
                if (attacker != null) {
                    level = attacker.level();
                }
                if (attacker != null) {
                    attacker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3, 5));
                }
                LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
                MinecraftServer server = level.getServer();
                DamageSource damageSource = null;
                if (server != null) {
                    damageSource = new DamageSource(server.registryAccess()
                                                            .registryOrThrow(Registries.DAMAGE_TYPE)
                                                            .getHolderOrThrow(DamageTypes.PLAYER_ATTACK),
                                                    attacker, // 造成伤害的实体
                                                    attacker // 直接造成伤害的实体（如射出的箭）
                    );
                }
                BlockPos blockPos = result.getBlockPos();
                if (lightningBolt != null) {
                    lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
                    if (damageSource != null) {
                        lightningBolt.setCause(damageSource.getDirectEntity() instanceof ServerPlayer
                                                       ? (ServerPlayer) damageSource.getDirectEntity()
                                                       : null);
                    }
                    level.addFreshEntity(lightningBolt);
                    SoundEvent soundEvent = SoundEvents.LIGHTNING_BOLT_THUNDER;
                    level.playSound(null, blockPos, soundEvent, SoundSource.AMBIENT, 1.0F, 1.0F);
                    this.clearFire();
                }
            }
            discardEndure();
        }

        // 如果有忠诚附魔，立即开始返回
        if (loyaltyLevel > 0) {
            startReturning();
        }
    }

    private void discardEndure() {
        ItemStack mace = this.getItem();
        if (mace.getDamageValue() == mace.getMaxDamage()) {
            this.playSound(SoundEvents.ITEM_BREAK, 1.0F, 1.0F);
            this.discard();
        } else {
            mace.setDamageValue(mace.getDamageValue() +
                                getItemDamage(ModEnchantHelper.getEnchantmentLevel(mace, Enchantments.UNBREAKING)));
        }
    }

    private void explode(float power, Level level, double x, double y, double z, Entity entity) {
        float f = 4.0F + (float) (power * 0.5);
        Level.ExplosionInteraction interaction =
                Config.getExplodeDestroyBlock() ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE;
        level.explode(entity, x, y, z, f, interaction);
    }

    private void tryExplode() {
        if (this.level().isClientSide()) return;
        ItemStack mace = this.getItem();
        int explosionLevel = ModEnchantHelper.getEnchantmentLevel(mace, ModEnchantments.EXPLODE);
        if (explosionLevel > 0 && explodeFlag) {
            explode(explosionLevel, this.level(), getX(), getY(), getZ(), this);
            discardEndure();
            explodeFlag = false;
        }
    }

    /**
     * 客户端处理拾取冷却时间的提示
     */
    @Override
    public net.minecraft.network.chat.@NotNull Component getDisplayName() {
        if (this.level().isClientSide) {
            if (isReturning) {
                return net.minecraft.network.chat.Component.literal("重锤 (返回中)");
            }

            int ticksSinceCreation = this.tickCount - createTick;
            if (ticksSinceCreation < PICKUP_COOLDOWN) {
                // 显示剩余时间
                float secondsLeft = (PICKUP_COOLDOWN - ticksSinceCreation) / 20.0F;
                return net.minecraft.network.chat.Component.literal(
                        String.format("重锤 (%.1f秒后可拾取)", secondsLeft));
            }

            // 如果有忠诚附魔，显示忠诚等级
            if (loyaltyLevel > 0) {
                return net.minecraft.network.chat.Component.literal(String.format("重锤 [忠诚 %d]", loyaltyLevel));
            }
        }
        return super.getDisplayName();
    }

    /**
     * 获取忠诚等级（用于其他用途）
     */
    public int getLoyaltyLevel() { return this.loyaltyLevel; }

    /**
     * 是否正在返回
     */
    public boolean isReturning() { return this.isReturning; }
}
