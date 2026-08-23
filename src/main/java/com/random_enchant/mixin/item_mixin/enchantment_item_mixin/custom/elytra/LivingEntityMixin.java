package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.elytra;

import com.llamalad7.mixinextras.sugar.Local;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import java.util.Optional;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) { super(entityType, level); }

    @Unique
    private static int isEnchantedFly(IDynamicStackHandler stackHandler) {
        // 3. 遍历背部槽位的所有格子（可能有多个）
        for (int i = 0; i < stackHandler.getSlots(); i++) {
            ItemStack stackInSlot = stackHandler.getStackInSlot(i);
            // 4. 检查物品是否匹配（非空且相同）
            if (!stackInSlot.isEmpty() && ModEnchantHelper.getEnchantmentLevel(stackInSlot, ModEnchantments.FLY) > 0) {
                return i;
            }
        }
        return -114514;
    }

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot1);

    @Shadow
    private SoundEvent getFallDamageSound(int height) {
        return null;
    }

    @Inject(at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/"
                              + "Vec3;)V",
                     ordinal = 6),
            method = "travel", cancellable = true)
    private void
    init1(CallbackInfo ci, @Local(ordinal = 1) Vec3 vec3, @Local(ordinal = 3) double d3) {
        ItemStack itemstack = this.getItemBySlot(EquipmentSlot.CHEST);
        if (ModEnchantHelper.getEnchantmentLevel(itemstack, Enchantments.INFINITY) <= 0) {
            Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory((LivingEntity) (Object) this);
            if (curiosInventory.isPresent()) {
                ICuriosItemHandler handler = curiosInventory.get();
                Optional<ICurioStacksHandler> backStacks = handler.getStacksHandler("back");
                if (backStacks.isPresent()) {
                    ICurioStacksHandler backHandler = backStacks.get();
                    IDynamicStackHandler stackHandler = backHandler.getStacks(); // 获取槽位物品处理器
                    int i = isEnchantedFly(stackHandler);
                    if (i == -114514) return;
                }
            }
        }
        this.setDeltaMovement(vec3);
        ci.cancel();
        this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.horizontalCollision && !this.level().isClientSide) {
            double d11 = this.getDeltaMovement().horizontalDistance();
            double d7 = d3 - d11;
            float f1 = (float) (d7 * 10.0 - 3.0);
            if (f1 > 0.0F) {
                this.playSound(this.getFallDamageSound((int) f1), 1.0F, 1.0F);
                this.hurt(this.damageSources().flyIntoWall(), f1);
            }
        }
        if (this.onGround() && !this.level().isClientSide) {
            this.setSharedFlag(7, false);
        }
    }
}
