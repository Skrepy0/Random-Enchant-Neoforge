package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.infinity;

import static com.random_enchant.enchantment.ModEnchantHelper.getEnchantmentLevel;

import com.random_enchant.Config;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class InfinityBlockMixin {
    @Inject(method = "setPlacedBy", at = @At("RETURN"))
    private void onSetPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
                               ItemStack stack, CallbackInfo ci) {
        if (!Config.infinityBlock())
            return;
        if (placer instanceof Player player && !level.isClientSide()) {
            // 检查创造模式
            if (player.isCreative()) {
                return;
            }
            // 检查主手和副手
            for (InteractionHand hand: InteractionHand.values()) {
                ItemStack handStack = player.getItemInHand(hand);
                if (hand == InteractionHand.OFF_HAND) {
                    if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BlockItem) {
                        continue;
                    }
                }
                // 如果手中的物品有无限附魔
                if (getEnchantmentLevel(handStack, Enchantments.INFINITY) > 0) {
                    // 恢复物品数量
                    if (handStack.getItem() instanceof BlockItem) {
                        if (handStack.getCount() == 0) {
                            // 如果物品已耗尽，设置为1
                            player.setItemInHand(hand, new ItemStack(handStack.getItem(), 1));
                        } else {
                            // 否则增加1个
                            handStack.setCount(handStack.getCount() + 1);
                        }
                        player.containerMenu.broadcastChanges();
                        break; // 只需要处理一个手
                    }
                }
            }
        }
    }
}
