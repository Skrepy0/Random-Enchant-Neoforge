package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.farmland;

import com.random_enchant.enchantment.ModEnchantHelper;
import java.util.Optional;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

@Mixin(CommonHooks.class)
public class CommonHooksMixin {
    @Unique
    private static boolean randomEnchant$isEnchantedFF(IDynamicStackHandler stackHandler) {
        // 3. 遍历槽位的所有格子（可能有多个）
        for (int i = 0; i < stackHandler.getSlots(); i++) {
            ItemStack stackInSlot = stackHandler.getStackInSlot(i);
            // 4. 检查物品是否匹配（非空且相同）
            if (!stackInSlot.isEmpty() &&
                ModEnchantHelper.getEnchantmentLevel(stackInSlot, Enchantments.FEATHER_FALLING) > 0) {
                return true;
            }
        }
        return false;
    }

    @Inject(method = "onFarmlandTrample", at = @At("HEAD"), cancellable = true)
    private static void onFarmlandTrample(Level level, BlockPos pos, BlockState state, float fallDistance,
                                          Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity living) {
            ItemStack stack = living.getItemBySlot(EquipmentSlot.FEET);
            if (ModEnchantHelper.getEnchantmentLevel(stack, Enchantments.FEATHER_FALLING) > 0) {
                cir.cancel();
            } else {
                Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(living);
                if (curiosInventory.isPresent()) {
                    ICuriosItemHandler handler = curiosInventory.get();
                    Optional<ICurioStacksHandler> feetStacks = handler.getStacksHandler("feet");
                    if (feetStacks.isPresent()) {
                        ICurioStacksHandler feetHandler = feetStacks.get();
                        IDynamicStackHandler stackHandler = feetHandler.getStacks();
                        if (randomEnchant$isEnchantedFF(stackHandler)) {
                            cir.cancel();
                        }
                    }
                }
            }
        }
    }
}
