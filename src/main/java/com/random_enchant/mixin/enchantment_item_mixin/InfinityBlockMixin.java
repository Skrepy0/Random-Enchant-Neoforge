package com.random_enchant.mixin.enchantment_item_mixin;

import com.random_enchant.enchantment.ModEnchantHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Block.class)
public class InfinityBlockMixin {
    @Inject(method = "setPlacedBy", at = @At("RETURN"))
    private void onSetPlacedBy(Level level, BlockPos pos, BlockState state,
                               @Nullable LivingEntity placer, ItemStack stack,
                               CallbackInfo ci) {
        if (placer instanceof Player player && !player.isCreative()) {
            ItemStack heldItem = player.getMainHandItem();

            // 检查无限附魔
            if (ModEnchantHelper.getEnchantmentLevel(heldItem, Enchantments.INFINITY) > 0) {
                // 恢复一个物品
                ItemStack restored = stack.copy();
                restored.setCount(1);

                if (!player.getInventory().add(restored)) {
                    player.drop(restored, false);
                }
            }
        }
    }
}
