package com.random_enchant.mixin_helper;

//import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;

import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import java.util.Set;

import static com.random_enchant.RandomEnchant.LOGGER;


/**
 * @author Mafuyu33
 */
public class InjectHelper {


    public static void onPlacedInject(Level world, ItemStack itemStack, BlockPos pos) {
        //只在服务端运行
        if (!world.isClientSide) {
            // 获取物品栈上的附魔信息
            ItemEnchantments enchantments = itemStack.getTagEnchantments();
            // 如果附魔信息不为空
            if (!enchantments.isEmpty()) {
                // 获取物品栈上的附魔信息列表,储存信息
                addToList(itemStack, pos.immutable());
            }
        }
    }

    public static ListTag enchantmentsToNbtList(ItemStack itemStack) {
        // 在这里对满足条件的方块进行处理
        ItemEnchantments itemEnchantments = itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        ListTag enchantmentNbtList = new ListTag();
        Set<Object2IntMap.Entry<Holder<Enchantment>>> entries = itemEnchantments.entrySet();
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : entries) {
            Holder<Enchantment> key = entry.getKey();
            int intValue = entry.getIntValue();


            CompoundTag enchantmentNbt = new CompoundTag();
            enchantmentNbt.putString("id", key.getKey().location().toString());
            enchantmentNbt.putInt("lvl", intValue);
            enchantmentNbtList.add(enchantmentNbt);

        }
        return enchantmentNbtList;
    }

    public static void addToList(ItemStack itemStack, BlockPos currentPos) {
        ListTag listTag = enchantmentsToNbtList(itemStack);
        LOGGER.info("Storing enchantments at position: " + currentPos + " with data: " + listTag);
        BlockEnchantmentStorage.addBlockEnchantment(currentPos, listTag);
    }

}
