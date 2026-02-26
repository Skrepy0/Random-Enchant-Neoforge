package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import static com.random_enchant.datagen.GuidePages.pages;


public class ModZHCNLangProvider extends LanguageProvider {
    public ModZHCNLangProvider(PackOutput output) {
        super(output, RandomEnchant.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.random_enchant.title", "随机附魔");

        add("random_enchant.configuration.randomEnchant", "§g随机附魔§r事件");
        add("random_enchant.configuration.isAlwaysEnchantable", "所有物品可附魔");
        add("random_enchant.configuration.infinityUndyingTotem", "§a[无限]§r附魔对不死图腾的兼容");
        add("random_enchant.configuration.infinityBlock", "§a[无限]§r附魔对可放置方块的兼容");
        add("random_enchant.configuration.explodeDestroyBlock", "本模组中的爆炸效果可破坏方块");
        add("random_enchant.configuration.infinityTnt", "§a[无限]§r附魔对TNT的兼容");
        add("random_enchant.configuration.infinityPotion", "§a[无限]§r附魔对药水的兼容");
        add("random_enchant.configuration.infinityFood", "§a[无限]§r附魔对食物的兼容");
        add("random_enchant.configuration.infinityThrowableItem", "§a[无限]§r附魔对投掷物品的兼容");
        add("random_enchant.configuration.isEnchantedBlockGetatable", "§b[精准采集]§r获得已经附魔的方块");
        add("random_enchant.configuration.bedrockViolable", "§7[基岩]§r可以被本模组的特性破坏或移除");
        add("random_enchant.configuration.redirectTridentSetPointDistance", "§6[重定向]§r射线检测最大距离");
        add("random_enchant.configuration.flyEnchantmentLiftHeightPerTick", "含有§d[Fly]§r附魔的鞘翅每tick提升的高度");

        add("random_enchant.configuration.randomEnchant.tooltip", "启用后，攻击生物时，为玩家主手上的物品§d随机附魔§r");
        add("random_enchant.configuration.isAlwaysEnchantable.tooltip", "启用后，§l所有物品§r可以通过§a铁砧§r附魔");
        add("random_enchant.configuration.infinityBlock.tooltip",
                "启用后，放置带有§a[无限]§r附魔的方块时，手中方块不会被消耗");
        add("random_enchant.configuration.infinityUndyingTotem.tooltip",
                "启用后，使用§a[无限]§r附魔的§d不死图腾§r时，返还一个一样的图腾，处于兼容性考虑，返还的物品槽位可能会与使用前不同");
        add("random_enchant.configuration.infinityTnt.tooltip",
                "启用后，TNT点燃后生成实体但原方块保留。打火石、红石、爆炸均有效。");
        add("random_enchant.configuration.infinityThrowableItem.tooltip",
                "启用后，§a[无限]§r附魔的可投掷物品，如末影珍珠、鸡蛋等使用时不会消失");
        add("random_enchant.configuration.infinityDispenser","§a[无限]§r附魔对发射器与投掷器的兼容");
        add("random_enchant.configuration.infinityDispenser.tooltip","启用后，发射器与投掷器发射带有§a[无限]§r附魔的物品时，物品不会消失");
        add("random_enchant.configuration.bedrockViolable.tooltip", "启用后，可以通过本模组的特性破坏基岩");
        add("random_enchant.configuration.isEnchantedBlockGetatable.tooltip",
                "启用后，玩家使用带有§b[精准采集]§r附魔的工具破坏被附魔的方块时，可以掉落该带有附魔的方块");
        add("random_enchant.configuration.redirectTridentSetPointDistance.tooltip",
                "含有§6[重定向]§r附魔的三叉戟在使用时右键，落点与玩家的最大距离");
        add("random_enchant.configuration.flyEnchantmentLiftHeightPerTick.tooltip",
                "玩家使用附魔有§d[Fly]§r的鞘翅，并按下跳远键时，每tick提升的高度");


        add(ModItems.PEARL_SPEAR.get(), "珍珠矛");
        add(ModItems.ENCHANT_BRUSH.get(), "附魔刷");
        add(ModItems.LIGHTNING_ITEM.get(), "雷电");
        add(ModItems.MILK_BOTTLE.get(), "牛奶瓶");
        add(ModItems.MEMORY_POTION.get(), "§b回忆药水");
        add(ModItems.SOULTRANCE_POTION.get(), "§a魂溯药水");
        add(ModItems.GUIDE.get(), "指南");

        add("enchantment.random_enchant.fury_of_fly", "§aFly之怒");
        add("enchantment.random_enchant.fury_of_fly.desc", "召唤出蜜蜂助攻");
        add("enchantment.random_enchant.bad_luck_of_the_sea", "§c海之嫌弃§r");
        add("enchantment.random_enchant.bad_luck_of_the_sea.desc", "不是很被眷顾");
        add("enchantment.random_enchant.redirect_projectile", "§6重定向§r");
        add("enchantment.random_enchant.redirect_projectile.desc", "右键自由操纵三叉戟");
        add("enchantment.random_enchant.fly", "§dFly§r");
        add("enchantment.random_enchant.fly.desc", "鞘翅飞行时，按跳跃键上升§r");
        add("enchantment.random_enchant.no_gravity", "无重力");
        add("enchantment.random_enchant.no_gravity.desc", "射出的箭矢或三叉戟无视重力");
        add("enchantment.random_enchant.no_resistance", "无阻力");
        add("enchantment.random_enchant.no_resistance.desc", "射出的箭矢,鱼漂或三叉戟无视水和空气的阻力");
        add("enchantment.random_enchant.no_curse", "§a无诅咒§r");
        add("enchantment.random_enchant.no_curse.desc", "重新捡起物品，诅咒附魔消失");
        add("enchantment.random_enchant.steady", "§b稳定§r");
        add("enchantment.random_enchant.steady.desc", "使射出的箭矢不在有随机动量，射击更精确");
        add("enchantment.random_enchant.kinetic", "§b动能§r");
        add("enchantment.random_enchant.kinetic.desc", "射出的箭矢,鱼漂具有更高的初速度");
        add("enchantment.random_enchant.explode", "§6爆炸§r");
        add("enchantment.random_enchant.explode.desc",
                "附魔在弓或弩时射出爆炸剑，附魔在方块上时，有实体接触方块顶部时，方块爆炸");
        add("enchantment.random_enchant.throwable", "可投掷");
        add("enchantment.random_enchant.throwable.desc", "使重锤可以被投掷");
        add("enchantment.random_enchant.tracking", "§a追踪§r");
        add("enchantment.random_enchant.tracking.desc", "使箭矢追踪目标");
        add("enchantment.random_enchant.double_jump", "二段跳");
        add("enchantment.random_enchant.double_jump.desc",
                "给予玩家二段跳技能，等级达到II及以上时二段跳后落地不受衰落伤害");

        add("message.random_enchant.backup_warning", "§6Random Enchant是alpha版本，请做好存档备份");
        add("message.random_enchant.enchant_added", "§6已附魔：§r");
        add("message.random_enchant.item.enchant_brush.selected_1", "§a已设置起始点，请点击第二个方块以确定区域。");
        add("message.random_enchant.item.enchant_brush.selected_2", "§a区域附魔操作完成。");
        add("message.random_enchant.item.enchant_brush.clear_area", "§a已清除区域内的所有附魔。");
        add("message.random_enchant.item.enchant_brush.status.changed", "§a转换至：");
        add("message.random_enchant.item.enchant_brush.durability_insufficient",
                "§c耐久不足，执行失败，选中区域大小为%s");
        add("message.random_enchant.item.enchant_brush.clear_data", "已清除附魔刷数据");
        add("message.random_enchant.item.soultrance_potion.teleport_failed", "§c传送失败，当前玩家没有死亡记录");

        add("item.tooltip.random_enchant.for_shift_tooltip", "按下§6[SHIFT]§r查看详细信息");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_1", "§b左键攻击实体并瞬移，右键瞬移§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_2",
                "§b瞬移路径上的实体会受到8点基础伤害(实际伤害与§a[横扫之刃]§b等级有关)§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_3",
                "§b左键攻击时与目标的相对速度越大，伤害越高§r");
        add("item.tooltip.random_enchant.enchant_brush.status", "状态：");
        add("item.tooltip.random_enchant.enchant_brush.status.regional", "§a区域模式§r");
        add("item.tooltip.random_enchant.enchant_brush.status.single", "§a单方块模式§r");

        add("key.random_enchant.toggle_brush_status", "更改附魔刷模式");
        add("key.categories.random_enchant", "随机附魔");
        add("command.random_enchant.value", "配置§d[%s]§r的值目前为：");
        add("command.random_enchant.randomEnchant.enable", "§a已启用随机附魔");
        add("command.random_enchant.randomEnchant.disable", "§6已禁用随机附魔");
        add("command.random_enchant.config.changed", "已被更改为");
        add("command.random_enchant.config.unchanged", "的状态§c未发生有效更改§r");
        add("command.random_enchant.block_enchant.enchant_tag", "§6附魔标签：§r");
        add("command.random_enchant.block_enchant.has_no_enchantment", "§c该方块没有附魔§r");
        add("command.random_enchant.block_enchant.add_1", "§b已为方块位置§a");
        add("command.random_enchant.block_enchant.add_2", "§b 添加附魔 §a");
        add("command.random_enchant.block_enchant.remove", "§g已经移除此方块的所有附魔§a");

        add("item.jei_info.random_enchant.pearl_spear",
                "§l§6珍珠矛§r\n"
                        + "\n"
                        + "§7基础属性：§f攻击伤害 8，攻速 1.6，耐久 512，手持时移动速度大幅提升。\n"
                        + "\n"
                        + "§l右键能力：闪现突刺§r\n"
                        + "• 向视线方向闪现，最大距离 10 格\n"
                        + "• 路径上的实体受到 §e8 + 横扫之刃等级§r 伤害\n"
                        + "• 冷却 §c200 tick§r，附魔 §b快速装填§r 可大幅减少冷却\n"
                        + "\n"
                        + "§l左键能力：动能打击§r\n"
                        + "• 根据与目标的相对速度造成额外伤害\n"
                        + "  - 地面：伤害 = (目标速度投影 - 玩家速度投影) × 10 + 9\n"
                        + "  - 空中：伤害 = (玩家速度投影 - 目标速度投影) × 10 + 9\n"
                        + "• 攻击后水平闪现，对路径实体造成相同伤害\n"
                        + "• 若附有 §6引雷§r，召唤多道闪电\n"
                        + "• 若附有 §aFly之怒§r，召唤强化蜜蜂助攻（数量 = 等级）\n"
                        + "\n"
                        + "§l专属附魔：§aFly之怒§r\n"
                        + "• 等级 I~III，左键攻击后召唤蜜蜂\n"
                        + "• 蜜蜂具有生命提升、抗火、力量等效果\n"
                        + "• 与 §6引雷§r 互斥\n"
                        + "\n"
                        +
                        "§l可附魔：§f剑类附魔（锋利、击退、火焰附加、抢夺、横扫之刃）、耐久、快速装填、引雷、风暴、Fly之怒\n"
                        + "\n"
                        + "§l提示：§7配合鞘翅高速飞行可打出爆发伤害，右键闪现可快速接近或逃离。");
        add("item.jei_info.random_enchant.memory_potion",
                "§d§l回忆药水§r\\n§l效果§r：\\n饮用后立即传送至当前世界的重生点（即床的位置）。即使床被破坏，也会回到上次设置床的位置；若从未设置床，则传送到世界出生点。\\n\\n§l用途§r：\\n快速返回基地或庇护所，适合探险时使用。\\n\\n§l提示§r：\\n生存模式下可用，药水消耗后消失。传送时不会破坏身上的装备或物品。");
        add("item.jei_info.random_enchant.soultrance_potion",
                "§a§l魂溯药水§r\\n§l效果§r：\\n饮用后尝试传送至玩家上一次死亡的位置。如果玩家没有死亡记录（例如新玩家或死亡次数已被清除），则传送失败，药水消耗但无效果，并提示“§c传送失败，当前玩家没有死亡记录§r”。\\n\\n§l用途§r：\\n在死亡后快速返回死亡地点拾取掉落物，或探索上次死亡的地方。\\n\\n§l提示§r：\\n传送位置可能存在危险（如仍在岩浆中或怪物旁），建议提前做好准备。");
        add("item.jei_info.random_enchant.enchant_brush",
                "§l§6附魔刷§r\\n\\n§l基础属性§r：\\n• 耐久度：§a387§r\\n• 无法在附魔台附魔，可在铁砧上用附魔书增强\\n\\n§l耐久损耗机制§r：\\n实际损耗 = 处理的方块数 × 耐久系数，耐久系数由附魔等级决定：\\n• 无耐久：系数 1.0\\n• 耐久 I：系数 0.9\\n• 耐久 II：系数 0.7\\n• 耐久 III：系数 0.5\\n• 耐久 IV：系数 0.35\\n• 耐久 V：系数 0.2\\n• 耐久 VI：系数 0.1\\n• 耐久 VII：系数 0.08\\n• 耐久 ≥VIII：系数 0.05\\n\\n§l模式切换§r：按 §eTAB§r 切换\\n\\n§7■ 单方块模式§r：\\n• 右键单个方块，为方块添加刷子上的所有附魔；若刷子无附魔，则清除该方块的魔咒（不消耗耐久）。\\n\\n§7■ 区域模式§r：\\n• 第一次右键选择起点，起点位置会显示 §a绿色粒子§r 提示。\\n• 第二次右键选择终点，为区域内所有 §b非空气/非水/非岩浆§r 的方块进行批量操作：\\n  - 刷子有附魔：为方块添加刷子上的所有附魔。消耗耐久 = 处理的方块数 × 耐久系数。若耐久不足，操作失败并提示“耐久不足”。\\n  - 刷子无附魔：清除区域内所有方块的魔咒。消耗耐久 = 处理的方块数 × 耐久系数。\\n• 若已选择起点但未选终点，按住 §eShift+右键§r 可清除起点坐标，重新选择。\\n\\n§l可视化提示§r：\\n• 手持附魔刷时，被附魔的方块表面持续显示 §a绿色粒子§r。\\n• 区域模式选择起点/终点时，对应位置会短暂显示粒子特效。\\n\\n§l精准采集兼容§r：\\n• 使用带精准采集的工具挖掘附魔方块，挖下的方块会保留所有魔咒（由配置 §eisEnchantedBlockGetatable§r 控制，默认开启）。\\n\\n§l注意事项§r：\\n• 区域操作时，消耗的耐久由实际处理的方块数计算，受耐久附魔减免。\\n• 若刷子耐久不足，操作将被取消，已选择的起点也会被清除。\\n• 附魔刷本身无附魔时，可用于快速清除区域内的所有方块附魔。");
        add("item.jei_info.random_enchant.lightning",
                "§6雷电物品§r可通过在雷暴天气中让闪电击中带有§a精准采集§r的避雷针获得。\n\n" +
                        "避雷针必须放置在室外且处于雷暴天气中，闪电必须直接击中避雷针。物品会以掉落物形式出现，可拾取。");
        add("advancements.enchant.root.title", "随机附魔");
        add("advancements.enchant.root.description", "开启附魔之旅");
        add("advancements.enchant.get_enchant_brush.title", "附魔艺术家");
        add("advancements.enchant.get_enchant_brush.description", "获得一把附魔刷");
        add("advancements.enchant.get_pearl_spear.title", "末影穿刺");
        add("advancements.enchant.get_pearl_spear.description", "获得一把珍珠矛");
        add("advancements.enchant.use_region_mode.title", "区域规划师");
        add("advancements.enchant.use_region_mode.description", "使用区域模式附魔方块");
        add("advancements.enchant.trigger_fly_of_fury.title", "蜂群出击");
        add("advancements.enchant.trigger_fly_of_fury.description", "使用附有Fly之怒的珍珠矛左键攻击，召唤蜜蜂");
        add("advancements.enchant.drink_memory_potion.title", "归乡");
        add("advancements.enchant.drink_memory_potion.description", "喝下回忆药水，返回重生点");
        add("advancements.enchant.drink_soultrance_potion.title", "溯魂");
        add("advancements.enchant.drink_soultrance_potion.description", "喝下溯魂药水");
        add("advancements.enchant.bad_luck_of_the_sea.title", "海之嫌弃");
        add("advancements.enchant.bad_luck_of_the_sea.description", "给方块附魔§c海之嫌弃§r");
        add("advancements.enchant.thor_hammer.title", "雷神之锤");
        add("advancements.enchant.thor_hammer.description", "投掷附有引雷的重锤");
        add("advancements.enchant.cleanse_curse.title", "无咒胜有咒");
        add("advancements.enchant.cleanse_curse.description", "利用无诅咒附魔成功移除物品上的所有诅咒");
        add("advancements.enchant.explode.title", "爆炸就是艺术");
        add("advancements.enchant.explode.description", "给方块附魔§6爆炸§r");
        add("advancements.enchant.hyper_transfer.title", "极速传输");
        add("advancements.enchant.hyper_transfer.description", "用附魔刷给漏斗附魔§d快速装填§r");
        add("advancements.enchant.lightning_catcher.title","天雷引电");
        add("advancements.enchant.lightning_catcher.description","获得§b雷电");

        add("event.random_enchant.get_guide", "成就已达成，奖励§6§l指南§rx1");
        add("item.random_enchant.guide.title", "§6§l指南§r");
        add("gui.random_enchant.guide.title","§d§l随机附魔§r§l指南");
        add("gui.random_enchant.guide.no_results","§c没有找到结果");
        add("gui.random_enchant.guide.search","🔍搜索...");
        add("gui.random_enchant.guide.previous_button","§6上一页");
        add("gui.random_enchant.guide.next_button","§6下一页");
        var book_path = "item.random_enchant.guide.page.";

        for (int i = 1; i <= pages.size(); ++i) {
            add(book_path + i, pages.get(i - 1));
        }
        System.out.println("book pages:" + pages.size());
    }
}
