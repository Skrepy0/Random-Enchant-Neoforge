package com.random_enchant.util;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;

public class CustomBookBuilder {

    public static ItemStack createBook(String title, String author, List<String> pages, boolean isEnchanted) {
        ItemStack bookStack = new ItemStack(Items.WRITTEN_BOOK);

        // 标题需要 Filterable<String>
        Filterable<String> filteredTitle = Filterable.passThrough(title);

        // 页面需要 List<Filterable<Component>>。必须确保每个元素是 Filterable<Component> 类型。
        List<Filterable<Component>> filteredPages = pages.stream()
                                                            .map(pageText -> {
                                                                // 先创建 Component
                                                                Component pageComponent = Component.literal(pageText);
                                                                // 使用显式类型参数创建 Filterable<Component>
                                                                return Filterable.<Component>passThrough(pageComponent);
                                                            })
                                                            .collect(Collectors.toList());

        // 创建书的内容组件
        WrittenBookContent bookContent = new WrittenBookContent(filteredTitle, author,
                                                                0, // generation: 0 表示原作
                                                                filteredPages,
                                                                true // resolved 必须为 true，否则书无法打开
        );

        bookStack.set(DataComponents.WRITTEN_BOOK_CONTENT, bookContent);

        if (isEnchanted) {
            bookStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        }

        return bookStack;
    }

    public static ItemStack createBook(String title, String author, List<String> pages) {
        return createBook(title, author, pages, false);
    }
}
