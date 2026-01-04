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

        add("random_enchant.configuration.isAlwaysEnchantable","Always Enchantable");
//        add("random_enchant.configuration.isAlwaysEnchantable.comment","§lAll items§rcan be enchanted by §aAnvil§r");
        add("random_enchant.configuration.infinityUndyingTotem","Infinity Undying Totem");
        add("random_enchant.configuration.infinityBlock","Infinity Block");
        add("random_enchant.configuration.infinityTnt","Infinity TNT");
        add("random_enchant.configuration.InfinityPotion","Infinity Potion");
        add("random_enchant.configuration.redirectTridentSetPointDistance","§6[Redirect Projectile]§rMax distance for radiographic testing");
//        add("random_enchant.configuration.redirectTridentSetPointDistance.comment","The max distance between the right-clicking point and the player when using a Trident with §6Redirect Projectile§r");
        add("random_enchant.configuration.flyEnchantmentLiftHeightPerTick", "The height that the elytra with §d[Fly]§r enchantment can lift per tick");


        add(ModItems.PEARL_SPEAR.get(), "Pearl Spear");
        add(ModItems.ENCHANT_BRUSH.get(), "Enchant Brush");

        add("enchantment.random_enchant.fury_of_fly","§aFury Of Fly");

        add("entity.minecraft.bee.random_enchant.spawn_name","§aFly");
        add("enchantment.random_enchant.bad_luck_of_the_sea","§cBad Luck Of The Sea§r");
        add("enchantment.random_enchant.redirect_projectile","§6Redirect Projectile§r");
        add("enchantment.random_enchant.fly","§dFly§r");

        add("message.random_enchant.enchant_added","§6Enchanted:§r");
        add("message.random_enchant.item.enchant_brush.selected_1", "§aStarting point set. Please click the second block to define the area.");
        add("message.random_enchant.item.enchant_brush.selected_2", "§aArea enchantment operation completed.");
        add("message.random_enchant.item.enchant_brush.clear_area", "§aAll enchantments in the area have been cleared.");
        add("message.random_enchant.item.enchant_brush.status.changed","Status has Changed to:");


        add("item.tooltip.random_enchant.for_shift_tooltip","Press §6[SHIFT]§r show detail information");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_1","§bLeft-click to attack the entity and teleport, right-click to teleport§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_2","§bEntities in the teleportation path take 8 base damage(real damage is related to the level of §a[Sweep Edge]§b)§r");
        add("item.tooltip.random_enchant.pearl_spear.detail_description_3","§bThe greater the relative speed to the target when left-clicking to attack,the higher the damage§r");
        add("item.tooltip.random_enchant.enchant_brush.status","Mode:");
        add("item.tooltip.random_enchant.enchant_brush.status.regional","§aRegional§r");
        add("item.tooltip.random_enchant.enchant_brush.status.single","§aSingle§r");

        add("key.random_enchant.toggle_brush_status","Change enchant brush's status(Creative Mode Only)");
        add("key.categories.random_enchant","Random Enchant");
        add("command.random_enchant.randomEnchant.enable","§aEnabled Random Enchant");
        add("command.random_enchant.randomEnchant.disable","§6Disabled Random Enchant");

        for (int i = 11 ; i < 256;i++){
            add("enchantment.level."+i,toRoman(i));
        }
    }
}
