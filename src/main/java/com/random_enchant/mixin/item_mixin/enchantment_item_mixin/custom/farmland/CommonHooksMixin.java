package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.farmland;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.mixin_helper.CuriosApiHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommonHooks.class)
public class CommonHooksMixin {

    @Inject(method = "onFarmlandTrample", at = @At("HEAD"), cancellable = true)
    private static void onFarmlandTrample(Level level, BlockPos pos, BlockState state, float fallDistance,
                                          Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity living) {
            ItemStack stack = living.getItemBySlot(EquipmentSlot.FEET);
            if (ModEnchantHelper.getEnchantmentLevel(stack, Enchantments.FEATHER_FALLING) > 0) {
                cir.cancel();
                return;
            }
            boolean hasFF = CuriosApiHelper.hasStackInSlot(
                    living, "feet", s -> ModEnchantHelper.getEnchantmentLevel(s, Enchantments.FEATHER_FALLING) > 0);
            if (hasFF) {
                cir.cancel();
            }
        }
    }
}
