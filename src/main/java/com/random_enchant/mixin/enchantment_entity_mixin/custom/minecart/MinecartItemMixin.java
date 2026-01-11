package com.random_enchant.mixin.enchantment_entity_mixin.custom.minecart;

import com.llamalad7.mixinextras.sugar.Local;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Mafuyu33
 */
@Mixin(MinecartItem.class)
public abstract class MinecartItemMixin extends Item {
    public MinecartItemMixin(Properties properties) { super(properties); }

    @Inject(method = "useOn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/"
                                                + "minecraft/world/entity/Entity;)Z"))
    private void
    init(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir,
         @Local(ordinal = 0) AbstractMinecart abstractminecart) {
        // 获取正在使用的物品
        ItemStack stack = context.getItemInHand();
        if (ModEnchantHelper.getEnchantmentLevel(stack, ModEnchantments.BAD_LUCK_OF_THE_SEA) > 0) {
            abstractminecart.addTag("bad_luck_of_the_sea");
        }
    }
}
