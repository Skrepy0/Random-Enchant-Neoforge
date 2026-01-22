package com.random_enchant.item.custom.tool;

import com.random_enchant.data.nbt.BrushNBTUtils;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import com.random_enchant.mixin_helper.InjectHelper;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;


public class EnchantBrush extends BrushItem {
    public EnchantBrush(Properties properties) { super(properties.durability(64).rarity(Rarity.UNCOMMON).stacksTo(1)); }

    private static int getItemDamage(int unbreakingLevel) {
        Random random = new Random();
        int rand = random.nextInt(100);
        if (unbreakingLevel == 0) {
            if (rand <= 20) {
                return 0;
            }
            return 1;
        } else if (unbreakingLevel == 1) {
            if (rand <= 40) {
                return 0;
            }
            return 1;
        } else if (unbreakingLevel == 2) {
            if (rand <= 60) {
                return 0;
            }
            return 1;
        } else if (unbreakingLevel > 2) {
            if (rand <= 80) {
                return 0;
            }
            return 1;
        }
        return 1;
    }

    private static ListTag mergeNbtLists(ListTag list1, ListTag list2) {
        ListTag mergedList = new ListTag();
        mergedList.addAll(list1);
        mergedList.addAll(list2);
        return mergedList;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player user = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (!context.getLevel().isClientSide) {
            if (context.getItemInHand().isEnchanted()) {
                // 如果Pos位置方块没有附魔
                if (Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(context.getClickedPos()),
                                   new ListTag())) {
                    InjectHelper.addToList(context.getItemInHand(), context.getClickedPos());
                    EquipmentSlot equipmentSlot =
                            context.getItemInHand().equals(context.getPlayer().getItemBySlot(EquipmentSlot.OFFHAND))
                                    ? EquipmentSlot.OFFHAND
                                    : EquipmentSlot.MAINHAND;
                    context.getItemInHand().hurtAndBreak(1, context.getPlayer(), equipmentSlot);
                } else {
                    ListTag oldEnchantments =
                            BlockEnchantmentStorage.getEnchantmentsAtPosition(context.getClickedPos());
                    BlockEnchantmentStorage.removeBlockEnchantment(context.getClickedPos().immutable());
                    ListTag enchantments = InjectHelper.enchantmentsToNbtList(context.getItemInHand());
                    // 合并附魔列表
                    ListTag newEnchantments = mergeNbtLists(oldEnchantments, enchantments);
                    // 储存信息
                    BlockEnchantmentStorage.addBlockEnchantment(context.getClickedPos().immutable(), newEnchantments);
                }
            } else {
                // 删除信息
                BlockEnchantmentStorage.removeBlockEnchantment(context.getClickedPos().immutable());
            }
            if (!user.isCreative()) {
                int unbreakingLevel = ModEnchantHelper.getEnchantmentLevel(stack, Enchantments.UNBREAKING);
                stack.setDamageValue(stack.getDamageValue() + getItemDamage(unbreakingLevel));
            }
        }
        return super.useOn(context);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 10;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        // 获取刷子状态
        boolean status = BrushNBTUtils.getStatus(stack);

        // 显示状态信息
        Component statusComponent =
                Component.translatable("item.tooltip.random_enchant.enchant_brush.status")
                        .append(status ? Component.translatable(
                                                 "item.tooltip.random_enchant.enchant_brush.status.regional")
                                       : Component.translatable(
                                                 "item.tooltip.random_enchant.enchant_brush.status.single"));

        tooltipComponents.add(statusComponent);
        if (BrushNBTUtils.hasStartPos(stack)) {
            BlockPos startPos = BrushNBTUtils.getStartPos(stack);
            tooltipComponents.add(Component.literal("§dStart Pos: §e{" + startPos.toShortString() + "}"));
        }
    }
}
