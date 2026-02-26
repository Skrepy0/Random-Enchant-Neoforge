package com.random_enchant.mixin.lightning_rod;

import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import com.random_enchant.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningRodBlock.class)
public class LightningRodBlockMixin {
    @Inject(method = "onLightningStrike", at = @At("RETURN"))
    private void onLightningStrikeMixin(BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        if (BlockEnchantmentStorage.getLevel(Enchantments.SILK_TOUCH, pos)>0){
            ItemStack stack = new ItemStack(ModItems.LIGHTNING_ITEM.get());
            ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            itemEntity.setInvulnerable(true);
            level.addFreshEntity(itemEntity);
        }
    }
}
