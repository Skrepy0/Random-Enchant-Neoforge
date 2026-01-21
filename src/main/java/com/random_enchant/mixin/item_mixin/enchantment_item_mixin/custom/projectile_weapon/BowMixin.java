package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.projectile_weapon;

import net.minecraft.world.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BowItem.class)
public class BowMixin {
    //    /**
    //     * @author Skrepy
    //     * @reason Quick Charge
    //     */
    //    @Overwrite
    //    public int getUseDuration(ItemStack stack, LivingEntity entity) {
    //        float f = ModEnchantHelper.modifyBowChargingTime(stack, entity, 2.0F);
    //        return Mth.floor(f * 20.0F) + 3;
    //    }
    //
    //    /**
    //     * @author
    //     * @reason
    //     */
    //    @Overwrite
    //    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    //        ItemStack itemstack = player.getItemInHand(hand);
    //        boolean flag = !player.getProjectile(itemstack).isEmpty();
    //        ChargedProjectiles chargedprojectiles = itemstack.get(DataComponents.CHARGED_PROJECTILES);
    //        InteractionResultHolder<ItemStack> ret = net.neoforged.neoforge.event.EventHooks.onArrowNock(itemstack,
    //        level, player, hand, flag); if (ret != null) return ret; else if (!player.hasInfiniteMaterials() && !flag)
    //        {
    //            return InteractionResultHolder.fail(itemstack);
    //        } else {
    //            if (chargedprojectiles != null && !chargedprojectiles.isEmpty()) {
    //                this.performShooting(level, player, hand, itemstack, 3.15f, 1.0F, null);
    //            } else if (!player.getProjectile(itemstack).isEmpty()) {
    //                player.startUsingItem(hand);
    //            }
    //            return InteractionResultHolder.consume(itemstack);
    //        }
    //    }
    //    public void performShooting(
    //            Level level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, float velocity, float
    //            inaccuracy, @Nullable LivingEntity target
    //    ) {
    //        if (level instanceof ServerLevel serverlevel) {
    //            if (shooter instanceof Player player && net.neoforged.neoforge.event.EventHooks.onArrowLoose(weapon,
    //            shooter.level(), player, 1, true) < 0) return; ChargedProjectiles chargedprojectiles =
    //            weapon.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY); if (chargedprojectiles !=
    //            null && !chargedprojectiles.isEmpty()) {
    //                if (shooter instanceof ServerPlayer serverplayer) {
    //                    CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, weapon);
    //                    serverplayer.awardStat(Stats.ITEM_USED.get(weapon.getItem()));
    //                }
    //            }
    //        }
    //    }
}
