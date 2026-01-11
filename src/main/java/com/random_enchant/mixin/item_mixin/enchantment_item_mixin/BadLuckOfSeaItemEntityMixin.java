package com.random_enchant.mixin.item_mixin.enchantment_item_mixin;

import static com.random_enchant.enchantment.custom.BadLuckOfTheSeaHelper.entityWithBadLuckOfTheSea;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class BadLuckOfSeaItemEntityMixin extends Entity implements TraceableEntity {

    public BadLuckOfSeaItemEntityMixin(EntityType<?> entityType, Level level) { super(entityType, level); }

    @Shadow public abstract ItemStack getItem();


    @Inject(at = @At("HEAD"), method = "tick") private void init(CallbackInfo ci) { // 实现丢出去的实体被水排斥
        ItemStack itemStack = this.getItem();
        Item item = itemStack.getItem();
        Level world = this.level();
        BlockPos blockPos = this.blockPosition();
        FluidState fluidState = world.getFluidState(blockPos);
        int lvl = ModEnchantHelper.getEnchantmentLevel(itemStack, ModEnchantments.BAD_LUCK_OF_THE_SEA);
        if (fluidState.is(FluidTags.WATER) && lvl > 0) {
            entityWithBadLuckOfTheSea(this, lvl);
        }
    }
}
