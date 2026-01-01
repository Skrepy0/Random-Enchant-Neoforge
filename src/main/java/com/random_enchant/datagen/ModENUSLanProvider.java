package com.random_enchant.datagen;

import com.random_enchant.RandomEnchant;
import com.random_enchant.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import static com.random_enchant.enchantment.ModEnchantHelper.toRoman;

public class ModENUSLanProvider extends LanguageProvider {
    public ModENUSLanProvider(PackOutput output) {
        super(output, RandomEnchant.MOD_ID, "en_us");
    }
    @Override
    protected void addTranslations() {
        add("itemGroup.random_enchant.title", "Random Enchant");
        add(ModItems.PEARL_SPEAR.get(), "Pearl Spear");

        add("enchantment.random_enchant.fury_of_fly","§aFury Of Fly");

        add("entity.minecraft.bee.random_enchant.spawn_name","§aFly");

        add("message.random_enchant.enchant_added","§6Enchanted:§r");

        add("item.tooltip.random_enchant.for_shift_tooltip","Press §6[SHIFT]§r show detail information");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_1","§bLeft-click to attack the entity and teleport, right-click to teleport§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_2","§bEntities in the teleportation path take 8 base damage(real damage is related to the level of §a[Sweep Edge]§b)§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_3","§bThe greater the relative speed to the target when left-clicking to attack,the higher the damage§r");

        add("command.random_enchant.randomEnchant.enable","§aEnabled Random Enchant");
        add("command.random_enchant.randomEnchant.disable","§6Disabled Random Enchant");

        for (int i = 11 ; i < 256;i++){
            add("enchantment.level."+i,toRoman(i));
        }
    }
}
