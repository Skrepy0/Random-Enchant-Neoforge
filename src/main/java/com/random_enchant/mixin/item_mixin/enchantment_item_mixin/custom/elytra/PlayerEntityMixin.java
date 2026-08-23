package com.random_enchant.mixin.item_mixin.enchantment_item_mixin.custom.elytra;

import com.random_enchant.Config;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.mixin_helper.ElytraJumpMixinHelper;
import com.random_enchant.mixin_helper.RandomHelper;
import java.util.Optional;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
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

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

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

    @Unique
    private static float getProbability(int unbreaking) {
        if (unbreaking <= 0) return 0.08f;
        switch (unbreaking) {
            case 1 -> {
                return 0.072f;
            }
            case 2 -> {
                return 0.056f;
            }
            case 3 -> {
                return 0.04f;
            }
            case 4 -> {
                return 0.028f;
            }
            case 5 -> {
                return 0.016f;
            }
            case 6 -> {
                return 0.008f;
            }
            default -> {
                return 0.004f;
            }
        }
    }

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot1);

    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo ci) {
        if (!this.isFallFlying()) return;

        ItemStack chestItem = this.getItemBySlot(EquipmentSlot.CHEST);
        if (ModEnchantHelper.getEnchantmentLevel(chestItem, ModEnchantments.FLY) <= 0) {
            Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(this);
            if (curiosInventory.isPresent()) {
                ICuriosItemHandler handler = curiosInventory.get();
                Optional<ICurioStacksHandler> backStacks = handler.getStacksHandler("back");
                if (backStacks.isPresent()) {
                    ICurioStacksHandler backHandler = backStacks.get();
                    IDynamicStackHandler stackHandler = backHandler.getStacks(); // 获取槽位物品处理器
                    int i = isEnchantedFly(stackHandler);
                    if (i == -114514) return;
                    chestItem = stackHandler.getStackInSlot(i);
                }
            }
        }

        if (ElytraJumpMixinHelper.isJumpKeyPressed()) {
            this.push(0, Config.getFlyEnchantmentLiftHeightPerTick(), 0);
            if (RandomHelper.random(
                        getProbability(ModEnchantHelper.getEnchantmentLevel(chestItem, Enchantments.UNBREAKING)))) {
                chestItem.setDamageValue(chestItem.getDamageValue() + 1);
            }
        }
    }
}
