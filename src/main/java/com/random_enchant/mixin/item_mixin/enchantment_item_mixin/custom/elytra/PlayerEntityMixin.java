package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.elytra;

import com.random_enchant.Config;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.mixin_helper.ElytraJumpMixinHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot1);

    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo ci) {
        if (!this.isFallFlying())
            return;

        ItemStack chestItem = this.getItemBySlot(EquipmentSlot.CHEST);
        if (ModEnchantHelper.getEnchantmentLevel(chestItem, ModEnchantments.FLY) <= 0)
            return;

        if (ElytraJumpMixinHelper.isJumpKeyPressed()) {
            this.push(0, Config.getFlyEnchantmentLiftHeightPerTick(), 0);
        }
    }
}
