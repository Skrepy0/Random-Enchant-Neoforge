package com.random_enchant.item.custom.misc;

import static com.random_enchant.datagen.GuidePages.pages;
import static net.minecraft.network.chat.Component.translatable;

import com.random_enchant.client.gui.GuideScreen;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class GuideItem extends Item {

    public GuideItem(Properties properties) { super(properties.rarity(Rarity.UNCOMMON)); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide()) return super.use(level, player, usedHand);
        List<String> pagesContent = new ArrayList<>();
        for (int i = 1; i < pages.size(); i++) {
            pagesContent.add(translatable("item.random_enchant.guide.page." + i).getString());
        }
        Minecraft.getInstance().setScreen(new GuideScreen(pagesContent));
        return super.use(level, player, usedHand);
    }
}
