package com.random_enchant.mixin.enchantment_item_mixin.custom.trident.redirect_trident;

import com.random_enchant.Config;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.event.enchantment.trident_redirect.OnPlayerLeftClick;
import com.random_enchant.network.packet.C2S.RedirectTridentC2SPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {

    // 常量定义
    @Unique
    private static final EntityDataAccessor<Byte> DATA_REDIRECT_LEVEL =
            SynchedEntityData.defineId(ThrownTridentMixin.class, EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> DATA_FIRE_ASPECT_LEVEL =
            SynchedEntityData.defineId(ThrownTridentMixin.class, EntityDataSerializers.BYTE);
    @Unique
    private static final String NBT_REDIRECT_LEVEL = "RandomEnchant_RedirectLevel";
    @Unique
    private static final String NBT_FIRE_ASPECT_LEVEL = "RandomEnchant_FireAspectLevel";
    @Unique
    private static final double GROUND_TELEPORT_DISTANCE = 0.5;
    @Shadow
    private boolean dealtDamage;

    protected ThrownTridentMixin(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }


    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/network/syncher/SynchedEntityData;set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V"))
    private void onInitWithShooter(Level level, LivingEntity shooter, ItemStack pickupItemStack, CallbackInfo ci) {
        this.randomEnchant$setRedirectLevelFromItem(pickupItemStack);
        this.randomEnchant$setFireAspectLevelFromItem(pickupItemStack);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/network/syncher/SynchedEntityData;set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V"))
    private void onInitWithPosition(Level level, double x, double y, double z, ItemStack pickupItemStack, CallbackInfo ci) {
        this.randomEnchant$setRedirectLevelFromItem(pickupItemStack);
        this.randomEnchant$setFireAspectLevelFromItem(pickupItemStack);
    }

    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    private void onDefineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_REDIRECT_LEVEL, (byte) 0);
        builder.define(DATA_FIRE_ASPECT_LEVEL, (byte) 0);
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "RETURN"))
    public void onSaveAdditionalData(CompoundTag compound, CallbackInfo ci) {
        compound.putByte(NBT_REDIRECT_LEVEL, this.randomEnchant$getRedirectLevel());
        compound.putByte(NBT_FIRE_ASPECT_LEVEL, this.entityData.get(DATA_FIRE_ASPECT_LEVEL));
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "RETURN"))
    private void onLoadAdditionalData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains(NBT_REDIRECT_LEVEL)) {
            this.entityData.set(DATA_REDIRECT_LEVEL, compound.getByte(NBT_REDIRECT_LEVEL));
        } else {
            // 兼容旧存档：从物品重新计算
            this.randomEnchant$setRedirectLevelFromItem(this.getPickupItem());
        }

        if (compound.contains(NBT_FIRE_ASPECT_LEVEL)) {
            this.entityData.set(DATA_FIRE_ASPECT_LEVEL, compound.getByte(NBT_FIRE_ASPECT_LEVEL));
        } else {
            this.randomEnchant$setFireAspectLevelFromItem(this.getPickupItem());
        }
    }

    @Inject(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/syncher/SynchedEntityData;get(Lnet/minecraft/network/syncher/EntityDataAccessor;)Ljava/lang/Object;"),
            method = "tick")
    private void onTick(CallbackInfo ci) {
        int redirectLevel = this.randomEnchant$getRedirectLevel();
        Entity owner = this.getOwner();

        if (redirectLevel > 0 && owner != null) {
            // 检查左键点击
            if (!OnPlayerLeftClick.onPlayerLeftClicked()) {
                this.randomEnchant$resetGravityIfNeeded();
                return;
            }

            this.dealtDamage = false; // 重置伤害标志，允许再次伤害

            if (this.randomEnchant$shouldPerformRedirect()) {
                this.randomEnchant$performRedirect(owner, redirectLevel);
            }
        }
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void onHitEntity(EntityHitResult result, CallbackInfo ci) {
        Entity hitEntity = result.getEntity();
        Level level = hitEntity.level();
        if (level.isClientSide) return;
        if (hitEntity instanceof LivingEntity) {
            int fireAspectLevel = this.entityData.get(DATA_FIRE_ASPECT_LEVEL);
            if (fireAspectLevel > 0) {
                hitEntity.setRemainingFireTicks(fireAspectLevel * 80);
            }
        }
    }

    @Unique
    private byte randomEnchant$getRedirectLevel() {
        return this.entityData.get(DATA_REDIRECT_LEVEL);
    }

    @Unique
    private void randomEnchant$setRedirectLevelFromItem(ItemStack stack) {
        byte level = this.randomEnchant$calculateRedirectLevelFromItem(stack);
        this.entityData.set(DATA_REDIRECT_LEVEL, level);
    }

    @Unique
    private void randomEnchant$setFireAspectLevelFromItem(ItemStack stack) {
        byte level = this.randomEnchant$calculateFireAspectLevelFromItem(stack);
        this.entityData.set(DATA_FIRE_ASPECT_LEVEL, level);
    }

    @Unique
    private byte randomEnchant$calculateRedirectLevelFromItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }

        Level level = this.level();
        if (level instanceof ServerLevel) {
            int enchantLevel = ModEnchantHelper.getEnchantmentLevel(stack, ModEnchantments.REDIRECT_PROJECTILE);
            return (byte) Math.min(enchantLevel, Byte.MAX_VALUE);
        }
        return 0;
    }

    @Unique
    private byte randomEnchant$calculateFireAspectLevelFromItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }

        Level level = this.level();
        if (level instanceof ServerLevel) {
            int enchantLevel = ModEnchantHelper.getEnchantmentLevel(stack, Enchantments.FIRE_ASPECT);
            return (byte) Math.min(enchantLevel, Byte.MAX_VALUE);
        }
        return 0;
    }

    @Unique
    private boolean randomEnchant$shouldPerformRedirect() {
        // 在地面或飞行中未造成伤害时都可以重定向
        return this.inGround || (!this.dealtDamage && !this.isNoPhysics());
    }

    @Unique
    private void randomEnchant$performRedirect(Entity owner, int redirectLevel) {
        double raycastDistance = Config.getRedirectTridentSetPointDistance();
        Vec3 targetPos = this.randomEnchant$calculateTargetPosition(owner, raycastDistance);

        if (targetPos == null) {
            return;
        }

        // 计算方向和速度
        Vec3 direction = targetPos.subtract(this.position()).normalize();
        Vec3 velocity = direction.scale(redirectLevel);

        // 地面上的特殊处理：向前传送一小段距离
        if (this.inGround) {
            this.randomEnchant$teleportForward(direction);
        }

        // 应用速度和同步
        this.randomEnchant$applyRedirect(velocity);
    }

    @Unique
    private Vec3 randomEnchant$calculateTargetPosition(Entity owner, double maxDistance) {
        Vec3 lookVec = owner.getLookAngle();
        Vec3 startPos = owner.getEyePosition();
        Vec3 endPos = startPos.add(lookVec.scale(maxDistance));

        // 射线检测
        HitResult hitResult = owner.level().clip(
                new ClipContext(startPos, endPos,
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE,
                        owner
                )
        );

        // 检查实体
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            // 进行实体检测
            EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                    owner.level(),
                    owner,
                    startPos,
                    endPos,
                    new AABB(startPos, endPos).inflate(1.0), // 搜索范围
                    entity -> {
                        // 过滤条件：不检测自己，且实体可被击中
                        return entity != owner
                                && entity.isAlive()
                                && entity.isPickable()
                                && !entity.isSpectator();
                    },
                    0.0F // 距离阈值
            );

            if (entityHitResult != null) {
                Vec3 res = entityHitResult.getLocation();
                return new Vec3(res.x, res.y + 0.3, res.z);
            }
        }

        // 返回命中点或视线末端
        if (hitResult.getType() == HitResult.Type.BLOCK || hitResult.getType() == HitResult.Type.ENTITY) {
            return hitResult.getLocation();
        } else {
            return endPos;
        }
    }

    @Unique
    private void randomEnchant$teleportForward(Vec3 direction) {
        Vec3 teleportPos = this.position().add(direction.scale(GROUND_TELEPORT_DISTANCE));
        this.setPos(teleportPos.x, teleportPos.y, teleportPos.z);
        PacketDistributor.sendToServer(new RedirectTridentC2SPacket(this.getId(), teleportPos, 2));
    }

    @Unique
    private void randomEnchant$applyRedirect(Vec3 velocity) {
        this.setDeltaMovement(velocity);
        PacketDistributor.sendToServer(new RedirectTridentC2SPacket(this.getId(), velocity, 1));
    }

    @Unique
    private void randomEnchant$resetGravityIfNeeded() {
        if (this.isNoPhysics()) {
            this.setNoGravity(false);
            PacketDistributor.sendToServer(new RedirectTridentC2SPacket(this.getId(), Vec3.ZERO, 1));
        }
    }
}