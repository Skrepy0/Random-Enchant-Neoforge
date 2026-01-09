package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModZHCNLangProvider extends LanguageProvider {
    public ModZHCNLangProvider(PackOutput output) { super(output, RandomEnchant.MOD_ID, "zh_cn"); }

    @Override
    protected void addTranslations() {
        add("itemGroup.random_enchant.title", "随机附魔");

        add("random_enchant.configuration.randomEnchant", "§g随机附魔§r事件");
        add("random_enchant.configuration.isAlwaysEnchantable", "所有物品可附魔");
        //        add("random_enchant.configuration.isAlwaysEnchantable.comment","§l所有物品§r可以通过§a铁砧§r附魔");
        add("random_enchant.configuration.infinityUndyingTotem", "§a[无限]§r附魔对不死图腾的兼容");
        add("random_enchant.configuration.infinityBlock", "§a[无限]§r附魔对可放置方块的兼容");
        add("random_enchant.configuration.infinityTnt", "§a[无限]§r附魔对TNT的兼容");
        add("random_enchant.configuration.infinityPotion", "§a[无限]§r附魔对药水的兼容");
        add("random_enchant.configuration.infinityFood", "§a[无限]§r附魔对食物的兼容");
        add("random_enchant.configuration.infinityThrowableItem", "§a[无限]§r附魔对投掷物品的兼容");
        add("random_enchant.configuration.isEnchantedBlockGetatable", "§b[精准采集]§r获得已经附魔的方块");
        add("random_enchant.configuration.redirectTridentSetPointDistance", "§6[重定向]§r射线检测最大距离");
        //        add("random_enchant.configuration.redirectTridentSetPointDistance.comment","含有§6重定向§r附魔的三叉戟在使用时右键，落点与玩家的最大距离");
        add("random_enchant.configuration.flyEnchantmentLiftHeightPerTick", "含有§d[Fly]§r附魔的鞘翅每tick提升的高度");


        add(ModItems.PEARL_SPEAR.get(), "珍珠矛");
        add(ModItems.ENCHANT_BRUSH.get(), "附魔刷");

        add("enchantment.random_enchant.fury_of_fly", "§aFly之怒");
        add("enchantment.random_enchant.bad_luck_of_the_sea", "§c海之嫌弃§r");
        add("enchantment.random_enchant.redirect_projectile", "§6重定向§r");
        add("enchantment.random_enchant.fly", "§dFly§r");

        add("message.random_enchant.enchant_added", "§6已附魔：§r");
        add("message.random_enchant.item.enchant_brush.selected_1", "§a已设置起始点，请点击第二个方块以确定区域。");
        add("message.random_enchant.item.enchant_brush.selected_2", "§a区域附魔操作完成。");
        add("message.random_enchant.item.enchant_brush.clear_area", "§a已清除区域内的所有附魔。");
        add("message.random_enchant.item.enchant_brush.status.changed", "§a转换至：");

        add("item.tooltip.random_enchant.for_shift_tooltip", "按下§6[SHIFT]§r查看详细信息");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_1", "§b左键攻击实体并瞬移，右键瞬移§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_2",
            "§b瞬移路径上的实体会受到8点基础伤害(实际伤害与§a[横扫之刃]§b等级有关)§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_3",
            "§b左键攻击时与目标的相对速度越大，伤害越高§r");
        add("item.tooltip.random_enchant.enchant_brush.status", "状态：");
        add("item.tooltip.random_enchant.enchant_brush.status.regional", "§a区域模式§r");
        add("item.tooltip.random_enchant.enchant_brush.status.single", "§a单方块模式§r");

        add("key.random_enchant.toggle_brush_status", "更改附魔刷模式（仅创造）");
        add("key.categories.random_enchant", "随机附魔");
        add("command.random_enchant.randomEnchant.enable", "§a已启用随机附魔");
        add("command.random_enchant.randomEnchant.disable", "§6已禁用随机附魔");
        add("command.random_enchant.config.changed", "已被更改为");
        add("command.random_enchant.config.unchanged", "的状态§c未发生有效更改§r");
    }
}
