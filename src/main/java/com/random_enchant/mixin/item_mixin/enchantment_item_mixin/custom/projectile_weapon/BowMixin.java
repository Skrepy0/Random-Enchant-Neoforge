package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.projectile_weapon;

import com.random_enchant.enchantment.ModEnchantHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BowItem.class)
public class BowMixin {
    // 修改弓的使用持续时间
    @Inject(
            method = "getUseDuration(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)I",
            at = @At("RETURN"),
            cancellable = true
    )
    private void modifyGetUseDuration(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        int quickChargeLevel = ModEnchantHelper.getEnchantmentLevel(stack, Enchantments.QUICK_CHARGE);
        if (quickChargeLevel > 0) {
            // 注意：原版 getUseDuration 固定返回 72000，这是用于判断玩家是否还在拉弓的时间
            // 实际快速装填应该影响的是拉弓速度，而不是这个超长的持续时间
            int originalDuration = 72000; // 原版固定值
            // 每级减少25%的拉弓时间
            float reduction = quickChargeLevel * 0.25F;
            int newDuration = (int)(originalDuration * (1.0F - reduction));
            if (newDuration < 1) {
                newDuration = 1;
            }
            cir.setReturnValue(newDuration);
        }
    }

    // 修改关键方法：注入到原版的 getPowerForTime 方法中
    @Inject(
            method = "getPowerForTime(I)F",
            at = @At("HEAD"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void modifyGetPowerForTime(int charge, CallbackInfoReturnable<Float> cir) {
        // 使用一个全局的或线程本地的变量来获取当前使用者
        // 这里需要一个方法来追踪当前正在使用弓的实体
        LivingEntity entity = ModEnchantHelper.getCurrentUsingEntity();

        if (entity != null) {
            ItemStack stack = entity.getUseItem();
            if (stack.getItem() instanceof BowItem) {
                int quickChargeLevel = ModEnchantHelper.getEnchantmentLevel(stack, Enchantments.QUICK_CHARGE);

                if (quickChargeLevel > 0) {
                    // 原版计算
                    float f = (float)charge / 20.0F;
                    f = (f * f + f * 2.0F) / 3.0F;

                    // 根据附魔等级调整 charge 值，模拟更快的拉弓
                    // 每级增加25%的进度
                    float adjustedCharge = charge * (1.0F + quickChargeLevel * 0.25F);
                    f = adjustedCharge / 20.0F;
                    f = (f * f + f * 2.0F) / 3.0F;

                    if (f > 1.0F) {
                        f = 1.0F;
                    }

                    cir.setReturnValue(f);
                    return;
                }
            }
        }
    }

    // 添加一个 Mixin 到 releaseUsing 方法来更好地追踪
    @Inject(
            method = "releaseUsing",
            at = @At("HEAD")
    )
    private void onReleaseUsing(ItemStack stack, net.minecraft.world.level.Level level,
                                LivingEntity entityLiving, int timeLeft,
                                org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
    }
}