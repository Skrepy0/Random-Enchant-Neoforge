package com.random_enchant.mixin.enchantment_item_mixin;

import com.random_enchant.Config;
import com.random_enchant.item.ModItems;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    public AnvilMenuMixin(int containerId, net.minecraft.world.entity.player.Inventory playerInventory,
                          ContainerLevelAccess access) {
        super(null, containerId, playerInventory, access);
    }

    @Redirect(method = "createResult",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/world/item/enchantment/Enchantment;areCompatible(Lnet/minecraft/core/" +
                                "Holder;Lnet/minecraft/core/Holder;)Z"))
    private boolean
    redirectAreCompatible(net.minecraft.core.Holder<Enchantment> first, net.minecraft.core.Holder<Enchantment> second) {
        // 获取左边物品
        ItemStack leftItem = this.inputSlots.getItem(0);

        if (!leftItem.isEmpty() &&
            (leftItem.getItem() == ModItems.ENCHANT_BRUSH.get() || Config.isAlwaysEnchantable())) {
            return true;
        }

        // 否则使用原版逻辑
        return Enchantment.areCompatible(first, second);
    }

    @Redirect(method = "createResult",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean
    redirectIsItemCheck(ItemStack stack, net.minecraft.world.item.Item item) {
        // 获取左边物品
        ItemStack leftItem = this.inputSlots.getItem(0);

        if (!leftItem.isEmpty() &&
            (leftItem.getItem() == ModItems.ENCHANT_BRUSH.get() || Config.isAlwaysEnchantable())) {
            return true;
        }

        // 否则使用原版逻辑
        return stack.is(item);
    }

    @Redirect(
            method = "createResult",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/item/ItemStack;supportsEnchantment(Lnet/minecraft/core/Holder;)Z"))
    private boolean
    redirectSupportsEnchantment(ItemStack stack, net.minecraft.core.Holder<Enchantment> enchantment) {
        if (!stack.isEmpty() && (stack.getItem() == ModItems.ENCHANT_BRUSH.get() || Config.isAlwaysEnchantable())) {
            return true;
        }
        return stack.supportsEnchantment(enchantment);
    }

    @Redirect(method = "createResult",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"))
    private boolean
    redirectIsDamageableItem(ItemStack stack) {
        if (!stack.isEmpty() && (stack.getItem() == ModItems.ENCHANT_BRUSH.get() || Config.isAlwaysEnchantable())) {
            return true;
        }

        return stack.isDamageableItem();
    }

    @Redirect(method = "createResult",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;isValidRepairItem(Lnet/minecraft/" +
                                                  "world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean
    redirectIsValidRepairItem(net.minecraft.world.item.Item item, ItemStack stack, ItemStack repairCandidate) {
        if (!stack.isEmpty() && stack.getItem() == ModItems.ENCHANT_BRUSH.get()) {
            return repairCandidate.getItem() == stack.getItem();
        }

        return item.isValidRepairItem(stack, repairCandidate);
    }
}
