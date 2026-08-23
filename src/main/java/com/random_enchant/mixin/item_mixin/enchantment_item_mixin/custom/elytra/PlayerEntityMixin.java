package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.elytra;

import com.random_enchant.Config;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.mixin_helper.CuriosApiHelper;
import com.random_enchant.mixin_helper.ElytraJumpMixinHelper;
import com.random_enchant.mixin_helper.RandomHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private static float randomEnchant$getProbability(int unbreaking) {
        if (unbreaking <= 0) return 0.08f;
        switch (unbreaking) {
            case 1 -> {
                return 0.072f;
            }
            case 2 -> {
                return 0.056f;
            }
            case 3 -> {
                return 0.04f;
            }
            case 4 -> {
                return 0.028f;
            }
            case 5 -> {
                return 0.016f;
            }
            case 6 -> {
                return 0.008f;
            }
            default -> {
                return 0.004f;
            }
        }
    }

    @Shadow public abstract @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot slot1);

    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo ci) {
        if (!this.isFallFlying()) return;

        ItemStack chestItem = this.getItemBySlot(EquipmentSlot.CHEST);
        if (ModEnchantHelper.getEnchantmentLevel(chestItem, ModEnchantments.FLY) <= 0) {
            ItemStack curiosStack =
                    CuriosApiHelper.getFirstStackInSlot(this, "back")
                            .filter(stack -> ModEnchantHelper.getEnchantmentLevel(stack, ModEnchantments.FLY) > 0)
                            .orElse(null);
            if (curiosStack == null) return;
            chestItem = curiosStack;
        }

        if (ElytraJumpMixinHelper.isJumpKeyPressed()) {
            this.push(0, Config.getFlyEnchantmentLiftHeightPerTick(), 0);
            if (RandomHelper.random(randomEnchant$getProbability(
                        ModEnchantHelper.getEnchantmentLevel(chestItem, Enchantments.UNBREAKING)))) {
                chestItem.setDamageValue(chestItem.getDamageValue() + 1);
            }
        }
    }
}
