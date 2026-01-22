package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.channeling;

import com.random_enchant.Config;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaceItem.class)
public class MaceItemMixin {
    @Inject(method = "hurtEnemy", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", ordinal = 0, shift = At.Shift.AFTER))
    private void injectHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        int channelingLevel = ModEnchantHelper.getEnchantmentLevel(stack, Enchantments.CHANNELING);
        int explosionLevel = ModEnchantHelper.getEnchantmentLevel(stack, ModEnchantments.EXPLODE);
        Level level = attacker.level();
        if (channelingLevel > 0) {
            attacker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3, 5));
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
            MinecraftServer server = level.getServer();
            DamageSource damageSource = null;
            if (server != null) {
                damageSource = new DamageSource(
                        server.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.PLAYER_ATTACK),
                        attacker, // 造成伤害的实体
                        attacker  // 直接造成伤害的实体（如射出的箭）
                );
            }
            BlockPos blockPos = target.blockPosition();
            if (lightningBolt != null) {
                lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
                if (damageSource != null) {
                    lightningBolt.setCause(damageSource.getDirectEntity() instanceof ServerPlayer
                            ? (ServerPlayer) damageSource.getDirectEntity()
                            : null);
                }
                level.addFreshEntity(lightningBolt);
                SoundEvent soundEvent = SoundEvents.LIGHTNING_BOLT_THUNDER;
                target.playSound(soundEvent, 5, 1.0F);
            }
        }
        if (explosionLevel>0&&target.isAlive()){
            attacker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3, 5));
            randomEnchant$explode(explosionLevel*0.8f, level,target.getX(), target.getY(), target.getZ(), target);
        }
    }
    @Unique
    private static void randomEnchant$explode(float power, Level level, double x, double y, double z, Entity entity) {
        float f = 4.0F + (float) (power * 0.5);
        Level.ExplosionInteraction interaction =
                Config.getExplodeDestroyBlock() ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE;
        level.explode(entity, x, y, z, f, interaction);
    }
}
