package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModZHCNLangProvider extends LanguageProvider {
    public ModZHCNLangProvider(PackOutput output) {
        super(output, RandomEnchant.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.random_enchant.title", "随机附魔");
        add(ModItems.PEARL_SPEAR.get(), "珍珠矛");

        add("enchantment.random_enchant.fury_of_fly","§aFly之怒");

        add("item.tooltip.random_enchant.for_shift_tooltip","按下§6[SHIFT]§r查看详细信息");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_1","§b左键攻击实体并瞬移，右键瞬移§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_2","§b瞬移路径上的实体会受到8点基础伤害(实际伤害与§a[横扫之刃]§b等级有关)§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_3","§b左键攻击时与目标的相对速度越大，伤害越高§r");


    }
}
