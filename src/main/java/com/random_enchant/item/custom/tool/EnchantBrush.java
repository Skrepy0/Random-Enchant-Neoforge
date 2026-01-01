package com.random_enchant.item.custom.tool;

import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import com.random_enchant.mixin_helper.InjectHelper;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Objects;


public class EnchantBrush extends BrushItem {
    public EnchantBrush(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide) {
            if (context.getItemInHand().isEnchanted()) {
                //如果Pos位置方块没有附魔
                if (Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(context.getClickedPos()), new ListTag())) {
                    InjectHelper.addToList(context.getItemInHand(), context.getClickedPos());
                    EquipmentSlot equipmentSlot = context.getItemInHand().equals(context.getPlayer().getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                    context.getItemInHand().hurtAndBreak(1, context.getPlayer(), equipmentSlot);
                } else {
                    ListTag oldEnchantments = BlockEnchantmentStorage.getEnchantmentsAtPosition(context.getClickedPos());
                    BlockEnchantmentStorage.removeBlockEnchantment(context.getClickedPos().immutable());
                    ListTag enchantments = InjectHelper.enchantmentsToNbtList(context.getItemInHand());
                    // 合并附魔列表
                    ListTag newEnchantments = mergeNbtLists(oldEnchantments, enchantments);
                    //储存信息
                    BlockEnchantmentStorage.addBlockEnchantment(context.getClickedPos().immutable(), newEnchantments);
                }
            } else {
                //删除信息
                BlockEnchantmentStorage.removeBlockEnchantment(context.getClickedPos().immutable());
            }
        }
        return super.useOn(context);
    }

    private static ListTag mergeNbtLists(ListTag list1, ListTag list2) {
        ListTag mergedList = new ListTag();
        mergedList.addAll(list1);
        mergedList.addAll(list2);
        return mergedList;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 10;
    }
}
