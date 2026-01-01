package com.random_enchant.mixin.enchantment_item_mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(LivingEntity.class)
public abstract class InfiniteUndyingMixin extends Entity implements Attackable {

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract boolean removeEffectsCuredBy(net.neoforged.neoforge.common.EffectCure cure);

    @Shadow
    public abstract boolean addEffect(MobEffectInstance effectInstance);

    @Shadow
    public abstract ItemStack getItemInHand(InteractionHand hand);

    @Shadow
    public abstract boolean removeAllEffects();


    @Shadow
    public abstract void remove(RemovalReason reason);

    @Shadow
    @javax.annotation.Nullable
    private LivingEntity lastHurtByMob;


    @Shadow
    public abstract void readAdditionalSaveData(CompoundTag compound);

    public InfiniteUndyingMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private void explode(int power) {
        float f = 4.0F + (float) (power * 0.5);
        this.level().explode(this, this.getX(), this.getY(0.0625), this.getZ(), f, Level.ExplosionInteraction.TNT);
    }


    /**
     * @author Mafuyu33
     * @reason Change the totem of undying code
     */
    @Overwrite
    private boolean checkTotemDeathProtection(DamageSource damageSource) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        } else {
            ItemStack itemstack = null;

            for (InteractionHand interactionhand : InteractionHand.values()) {
                ItemStack newStack = this.getItemInHand(interactionhand);
                if (newStack.is(Items.TOTEM_OF_UNDYING) && net.neoforged.neoforge.common.CommonHooks.onLivingUseTotem((LivingEntity) (Object) this, damageSource, newStack, interactionhand)) {
                    itemstack = newStack.copy();
//                    ItemEnchantments itemEnchantments = newStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                    ItemEnchantments itemEnchantments = newStack.get(DataComponents.ENCHANTMENTS);
                    if (itemEnchantments == null || !itemEnchantments.keySet().stream().map(holder -> holder.getKey()).collect(Collectors.toSet()).contains(Enchantments.INFINITY)) {
                        newStack.shrink(1);
                    }
                    break;
                }
            }

            if (itemstack != null) {
                if ((LivingEntity) (Object) this instanceof ServerPlayer serverplayer) {
                    serverplayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
                    CriteriaTriggers.USED_TOTEM.trigger(serverplayer, itemstack);
                    this.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                }

                this.setHealth(1.0F);
                this.removeEffectsCuredBy(net.neoforged.neoforge.common.EffectCures.PROTECTED_BY_TOTEM);
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));

                int protectLevel = getEnchantmentLevel(itemstack, Enchantments.PROTECTION);// 保护
                if (protectLevel > 0) {
                    this.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 900, protectLevel - 1));
                    this.setHealth(1);
                }

                int blastProtectLevel = getEnchantmentLevel(itemstack, Enchantments.BLAST_PROTECTION);//爆炸保护
                if (blastProtectLevel > 0) {
                    if (damageSource.getEntity() != null && damageSource.getDirectEntity() != null) {
                        explode(blastProtectLevel - 1);
                    }
                }

                int powerLevel = getEnchantmentLevel(itemstack, Enchantments.POWER);//力量
                if (powerLevel > 0) {
                    if (damageSource.getEntity() != null && damageSource.getDirectEntity() != null) {
                        this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 900, powerLevel - 1));
                    }
                }

                int k = getEnchantmentLevel(itemstack, Enchantments.CHANNELING);//引雷
                if (k > 0) {
                    if (this.level() instanceof ServerLevel) {
                        if (damageSource.getEntity() != null && damageSource.getDirectEntity() != null) {
                            BlockPos blockPos = lastHurtByMob.getOnPos();
                            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(this.level());
                            if (lightningBolt != null) {
                                lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
                                lightningBolt.setCause(damageSource.getDirectEntity() instanceof ServerPlayer ? (ServerPlayer) damageSource.getDirectEntity() : null);
                                this.level().addFreshEntity(lightningBolt);
                                SoundEvent soundEvent = SoundEvents.LIGHTNING_BOLT_THUNDER;
                                this.playSound(soundEvent, 5, 1.0F);
                            }
                        }
                    }
                }

                //
                this.level().broadcastEntityEvent(this, (byte) 35);
            }

            return itemstack != null;
        }
    }

    @Unique
    private static int getEnchantmentLevel(ItemStack item, ResourceKey<Enchantment> enchantmentResourceKey) {
        ItemEnchantments itemEnchantments = item.get(DataComponents.ENCHANTMENTS);
        if (itemEnchantments == null) {
            return -1;
        }
        Optional<Object2IntMap.Entry<Holder<Enchantment>>> levelOptional = itemEnchantments.entrySet().stream().filter(int2Enchatment -> int2Enchatment.getKey().is(enchantmentResourceKey)).findFirst();
        return levelOptional.map(Object2IntMap.Entry::getIntValue).orElse(-1);
    }

}
