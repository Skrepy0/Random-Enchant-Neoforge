package com.random_enchant.enchantment;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.Optional;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.runIterationOnItem;

public class ModEnchantHelper {
    public static int getEnchantmentLevel(ItemStack stack, Level world, ResourceKey<Enchantment> enchantmentKey) {
        // 获取物品的附魔组件
        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);

        // 通过 ResourceKey 获取 Holder<Enchantment>
        if (world != null) {
            Holder<Enchantment> enchantmentHolder = world.registryAccess()
                                                            .registryOrThrow(Registries.ENCHANTMENT)
                                                            .getHolder(enchantmentKey)
                                                            .orElse(null);

            // 如果有附魔组件和 Holder，返回等级
            if (enchantments != null && enchantmentHolder != null) {
                return enchantments.getLevel(enchantmentHolder);
            }
        }

        return 0;
    }

    public static int getEnchantmentLevel(ItemStack item, ResourceKey<Enchantment> enchantmentResourceKey) {
        if (item == null) {
            return 0;
        }
        ItemEnchantments itemEnchantments = item.get(DataComponents.ENCHANTMENTS);
        if (itemEnchantments == null) {
            return -1;
        }
        Optional<Object2IntMap.Entry<Holder<Enchantment>>> levelOptional =
                itemEnchantments.entrySet()
                        .stream()
                        .filter(int2Enchatment -> int2Enchatment.getKey().is(enchantmentResourceKey))
                        .findFirst();
        return levelOptional.map(Object2IntMap.Entry::getIntValue).orElse(-1);
    }

    public static int getEnchantmentLevel(ResourceKey<Enchantment> enchantmentResourceKey,ItemStack item) {
        return getEnchantmentLevel(item, enchantmentResourceKey);
    }

    public static Holder<Enchantment> getHolder(ResourceKey<Enchantment> enchantmentKey) {
        Level world = Minecraft.getInstance().level;
        return world.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolder(enchantmentKey).orElse(null);
    }

    public static String toRoman(int number) {

        if (number < 1 || number > 255) {
            throw new IllegalArgumentException("输入必须在1到255之间");
        }

        // 定义罗马数字的基本组成部分
        String[] thousands = {"", "M", "MM", "MMM"};
        String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] units = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        if (number <= 10) {
            if (number == 10) {
                return "X";
            } else
                return units[number];
        }
        // 分解数字的各个部分
        int thousandPart = number / 1000;
        int hundredPart = (number % 1000) / 100;
        int tenPart = (number % 100) / 10;
        int unitPart = number % 10;

        // 构建罗马数字字符串
        return thousands[thousandPart] + hundreds[hundredPart] + tens[tenPart] + units[unitPart];
    }

    public static String getDescriptionId(Enchantment enchantment, RegistryAccess registryAccess) {
        Registry<Enchantment> registry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
        ResourceLocation id = registry.getKey(enchantment);
        if (id == null) {
            return "enchantment.unknown";
        }
        return "enchantment." + id.getNamespace() + "." + id.getPath();
    }

    /**
     * 获取当前使用物品的实体
     * 这个方法用于在静态上下文中获取正在使用弓的实体
     * @return 当前使用物品的实体，如果没有则返回null
     */
    public static LivingEntity getCurrentUsingEntity() {
        // 尝试从客户端获取
        if (Minecraft.getInstance().player != null) {
            return Minecraft.getInstance().player;
        }
        // 注意：在服务端，这个方法可能无法正常工作
        // 如果需要在服务端使用，可能需要通过其他方式传递实体引用
        return null;
    }
    public static float modifyBowChargingTime(ItemStack stack, LivingEntity entity, float bowChargingTime) {
        MutableFloat mutablefloat = new MutableFloat(bowChargingTime);
        runIterationOnItem(stack,
                           (p_352869_, p_352870_)
                                   -> ((Enchantment) p_352869_.value())
                                              .modifyCrossbowChargeTime(entity.getRandom(), p_352870_, mutablefloat));
        return Math.max(0.0F, mutablefloat.floatValue());
    }
}
