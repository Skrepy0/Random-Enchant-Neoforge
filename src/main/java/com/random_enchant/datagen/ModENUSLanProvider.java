package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModENUSLanProvider extends LanguageProvider {
    public ModENUSLanProvider(PackOutput output) {
        super(output, RandomEnchant.MOD_ID, "en_us");
    }
    public static String toRoman(int number) {
        if (number < 11 || number > 255) {
            throw new IllegalArgumentException("输入必须在11到255之间");
        }

        // 定义罗马数字的基本组成部分
        String[] thousands = {"", "M", "MM", "MMM"};
        String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] units = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        // 分解数字的各个部分
        int thousandPart = number / 1000;
        int hundredPart = (number % 1000) / 100;
        int tenPart = (number % 100) / 10;
        int unitPart = number % 10;

        // 构建罗马数字字符串
        return thousands[thousandPart] +
                hundreds[hundredPart] +
                tens[tenPart] +
                units[unitPart];
    }
    @Override
    protected void addTranslations() {
        add("itemGroup.random_enchant.title", "Random Enchant");
        add(ModItems.PEARL_SPEAR.get(), "Pearl Spear");

        add("enchantment.random_enchant.fury_of_fly","§aFury Of Fly");

        add("item.tooltip.random_enchant.for_shift_tooltip","Press §6[SHIFT]§r show detail information");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_1","§bLeft-click to attack the entity and teleport, right-click to teleport§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_2","§bEntities in the teleportation path take 8 base damage(real damage is related to the level of §a[Sweep Edge]§b)§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_3","§bThe greater the relative speed to the target when left-clicking to attack,the higher the damage§r");
        for (int i = 11 ; i < 256;i++){
            add("enchantment.level."+i,toRoman(i));
        }
    }
}
