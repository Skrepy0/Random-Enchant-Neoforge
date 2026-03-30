package com.random_enchant.item.custom.tool;

import com.random_enchant.data.nbt.BrushNBTUtils;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.ModEnchantments;
import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import com.random_enchant.mixin_helper.InjectHelper;
import com.random_enchant.util.AdvancementHelper;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.Blocks;


public class EnchantBrush extends BrushItem {
    public EnchantBrush(Properties properties) {
        super(properties.durability(387).rarity(Rarity.UNCOMMON).stacksTo(1));
    }

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
        for (int i = 0; i < list2.size(); i++) {
            CompoundTag tag = list2.getCompound(i);
            if (!mergedList.contains(tag)) {
                mergedList.add(tag);
            } else {
                int index = -1;
                for (int j = 0; j < mergedList.size(); j++) {
                    CompoundTag mergedTag = mergedList.getCompound(j);
                    if (mergedTag.getString("id").equals(tag.getString("id"))) {
                        index = j;
                        break;
                    }
                }
                if (tag.getInt("lvl") > mergedList.getCompound(index).getInt("lvl")) {
                    mergedList.getCompound(index).putInt("lvl", tag.getInt("lvl"));
                }
            }
        }
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
        boolean status = BrushNBTUtils.getStatus(stack);
        if (status) return super.useOn(context);
        if (!context.getLevel().isClientSide) {
            if (stack.isEnchanted()) {
                // 如果Pos位置方块没有附魔
                if (Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(context.getClickedPos()),
                                   new ListTag())) {
                    InjectHelper.addToList(stack, context.getClickedPos());
                    EquipmentSlot equipmentSlot = stack.equals(context.getPlayer().getItemBySlot(EquipmentSlot.OFFHAND))
                                                          ? EquipmentSlot.OFFHAND
                                                          : EquipmentSlot.MAINHAND;
                    stack.hurtAndBreak(1, context.getPlayer(), equipmentSlot);
                } else {
                    ListTag oldEnchantments =
                            BlockEnchantmentStorage.getEnchantmentsAtPosition(context.getClickedPos());
                    BlockEnchantmentStorage.removeBlockEnchantment(context.getClickedPos().immutable());
                    ListTag enchantments = InjectHelper.enchantmentsToNbtList(stack);
                    // 合并附魔列表
                    ListTag newEnchantments = mergeNbtLists(oldEnchantments, enchantments);
                    // 储存信息
                    BlockEnchantmentStorage.addBlockEnchantment(context.getClickedPos().immutable(), newEnchantments);
                }
                if (user instanceof ServerPlayer serverPlayer) {
                    if (ModEnchantHelper.getEnchantmentLevel(stack, ModEnchantments.BAD_LUCK_OF_THE_SEA) > 0) {
                        AdvancementHelper.grantAdvancement(serverPlayer, "enchant/bad_luck_of_the_sea",
                                                           "bad_luck_of_the_sea");
                    }
                    if (ModEnchantHelper.getEnchantmentLevel(stack, ModEnchantments.EXPLODE) > 0) {
                        AdvancementHelper.grantAdvancement(serverPlayer, "enchant/explode", "explode");
                    }
                    if (ModEnchantHelper.getEnchantmentLevel(stack, Enchantments.QUICK_CHARGE) > 0 &&
                        context.getLevel().getBlockState(context.getClickedPos()).is(Blocks.HOPPER)) {
                        AdvancementHelper.grantAdvancement(serverPlayer, "enchant/hyper_transfer", "hyper_transfer");
                    }
                }

            } else {
                // 删除信息
                BlockEnchantmentStorage.removeBlockEnchantment(context.getClickedPos().immutable());
            }
            // 创造模式不消耗耐久
            if (!user.isCreative()) {
                int unbreakingLevel = ModEnchantHelper.getEnchantmentLevel(stack, Enchantments.UNBREAKING);
                stack.hurtAndBreak(getItemDamage(unbreakingLevel), user, user.getEquipmentSlotForItem(stack));
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
