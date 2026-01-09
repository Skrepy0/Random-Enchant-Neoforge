package com.random_enchant.mixin.enchantment_item_mixin;

import static com.random_enchant.enchantment.custom.BadLuckOfTheSeaHelper.entityWithBadLuckOfTheSea;

import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO add enchantment
@Mixin(LivingEntity.class)
public abstract class ArmorEnchantmentMixin
        extends Entity implements Attackable, net.neoforged.neoforge.common.extensions.ILivingEntityExtension {

    @Unique private static Vec3 lastPos = new Vec3(0, 0, 0);

    public ArmorEnchantmentMixin(EntityType<?> entityType, Level level) { super(entityType, level); }

    @Shadow public abstract Iterable<ItemStack> getArmorAndBodyArmorSlots();

    @Inject(at = @At("HEAD"), method = "tick")
    private void init1(CallbackInfo info) {
        Iterable<ItemStack> armorItems = this.getArmorAndBodyArmorSlots();


        for (ItemStack armorItem: armorItems) {
            if (armorItem.getItem() instanceof ArmorItem &&
                ((ArmorItem) armorItem.getItem()).getType() == ArmorItem.Type.BOOTS) { // 鞋子
                int k = ModEnchantHelper.getEnchantmentLevel(armorItem, ModEnchantments.BAD_LUCK_OF_THE_SEA); // 海之嫌弃
                if (k > 0) {
                    Level world = this.level();
                    BlockPos blockPos = this.blockPosition();
                    FluidState fluidState = world.getFluidState(blockPos);
                    if (fluidState.is(FluidTags.WATER)) {
                        entityWithBadLuckOfTheSea(this, k);
                    }
                }
            }
        }
    }
}
