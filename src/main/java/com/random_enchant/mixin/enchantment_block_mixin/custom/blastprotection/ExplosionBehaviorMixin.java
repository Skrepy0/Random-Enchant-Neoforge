package com.random_enchant.mixin.enchantment_block_mixin.custom.blastprotection;

import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityBasedExplosionDamageCalculator.class)
public abstract class ExplosionBehaviorMixin {
    @Inject(at = @At(value = "HEAD"), method = "shouldBlockExplode", cancellable = true)
    private void init(Explosion explosion, BlockGetter world, BlockPos pos, BlockState state, float power, CallbackInfoReturnable<Boolean> cir) {
        int i = BlockEnchantmentStorage.getLevel(Enchantments.BLAST_PROTECTION, pos);
        if (i > 0) {
            cir.setReturnValue(false);
        }
    }
}