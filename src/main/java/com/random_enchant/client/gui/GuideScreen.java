package com.random_enchant.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GuideScreen extends Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuideScreen.class);

    private final List<String> pages; // 原始每页文本
    private int currentPage = 0; // 当前页码（从0开始）
    private double scrollOffset = 0; // 当前页的垂直滚动偏移（单位：行）
    private List<FormattedCharSequence> currentPageLines; // 当前页拆分成行后的列表
    private int maxLines; // 内容区域可显示的最大行数

    // 搜索相关
    private EditBox searchBox; // 搜索输入框
    private Button searchButton; // 搜索按钮
    private Button prevButton; // 上一页按钮
    private Button nextButton; // 下一页按钮
    private SearchResultsList searchResultsList; // 搜索结果列表
    private List<SearchResult> allSearchResults; // 所有搜索结果（原始数据）
    private boolean searchMode = false; // 是否处于搜索模式（即已执行过搜索）

    // 动态布局参数（在init中计算）
    private int contentX;
    private int contentY;
    private int contentWidth;
    private int contentHeight;
    private int rightPanelWidth;
    private int searchBoxY;
    private int searchListY;
    private int searchListHeight;
    private int buttonY;
    private int padding;

    private boolean isDraggingScrollbar = false;
    private int dragStartMouseY;

    // 底部按钮布局参数（供render使用）
    private int buttonStartX;
    private int buttonSize;
    private int buttonSpacing;
    private int pageTextWidth;

    public GuideScreen(List<String> pages) {
        super(Component.translatable("gui.random_enchant.guide.title"));
        this.pages = pages;
        this.currentPageLines = new ArrayList<>();
    }

    /**
     * 根据屏幕大小计算布局参数
     * 左侧搜索区域，右侧内容区域
     */
    private void calculateLayout() {
        int screenW = this.width;
        int screenH = this.height;
        // 基础内边距，最小8像素
        this.padding = Math.max(8, screenW / 100);

        // 底部按钮区域高度预留
        int bottomAreaHeight = Math.max(30, screenH / 15);
        this.buttonY = screenH - bottomAreaHeight;

        // 标题区域高度预留
        int titleAreaHeight = Math.max(25, screenH / 20);

        // 左侧面板宽度（搜索区域）：根据屏幕宽度动态调整
        this.rightPanelWidth = Math.max(100, Math.min(screenW / 4, 180));

        // 搜索区域（左侧）
        this.searchBoxY = titleAreaHeight;
        int searchBoxHeight = 20;
        this.searchListY = this.searchBoxY + searchBoxHeight + this.padding;
        this.searchListHeight = this.buttonY - this.searchListY - this.padding;

        // 内容区域（右侧）
        this.contentX = this.rightPanelWidth + this.padding * 2;
        this.contentY = titleAreaHeight + this.padding;
        this.contentWidth = screenW - this.rightPanelWidth - this.padding * 3;
        this.contentHeight = this.buttonY - this.contentY - this.padding;
    }

    @Override
    protected boolean shouldNarrateNavigation() {
        return this.minecraft != null && super.shouldNarrateNavigation();
    }

    @Override
    public void handleDelayedNarration() {
        if (this.minecraft != null) {
            super.handleDelayedNarration();
        }
    }

    @Override
    protected void init() {
        super.init();

        // 计算动态布局
        calculateLayout();

        Font font = Minecraft.getInstance().font;

        // ----- 左侧搜索框和按钮 -----
        int searchBoxWidth = this.rightPanelWidth - 24;
        this.searchBox = new EditBox(font, 1, this.searchBoxY, searchBoxWidth, 20,
                                     Component.translatable("gui.random_enchant.guide.search"));
        this.searchBox.setMaxLength(50);
        this.searchBox.setResponder(this::onSearchTextChanged);
        this.searchBox.setSuggestion(Component.translatable("gui.random_enchant.guide.search").getString());
        this.addRenderableWidget(searchBox);

        int searchButtonX = this.padding + searchBoxWidth - 4;
        this.searchButton = Button.builder(Component.literal("🔍"), btn -> performSearch())
                                    .bounds(searchButtonX, this.searchBoxY, 20, 20)
                                    .build();
        this.searchButton.setTooltip(Tooltip.create(Component.translatable("gui.random_enchant.guide.search")));
        this.addRenderableWidget(searchButton);

        int searchListX = this.padding;
        this.searchResultsList = new SearchResultsList(this.minecraft, this.rightPanelWidth, this.searchListHeight,
                                                       this.searchListY, font.lineHeight);
        this.addRenderableWidget(searchResultsList);

        // ----- 底部翻页按钮（搜索区域下方） -----
        this.buttonSize = Math.max(20, this.height / 30);
        this.buttonSpacing = 4;
        this.pageTextWidth = this.rightPanelWidth - 52;
        this.buttonStartX = this.padding / 2;

        this.prevButton =
                Button.builder(Component.literal("<"),
                               btn -> {
                                   if (currentPage > 0) {
                                       currentPage--;
                                       scrollOffset = 0;
                                       updateCurrentPageLines();
                                   }
                               })
                        .bounds(this.buttonStartX, this.buttonY, this.buttonSize, this.buttonSize)
                        .tooltip(Tooltip.create(Component.translatable("gui.random_enchant.guide.previous_button")))
                        .build();
        this.addRenderableWidget(prevButton);

        this.nextButton =
                Button.builder(Component.literal(">"),
                               btn -> {
                                   if (currentPage < pages.size() - 1) {
                                       currentPage++;
                                       scrollOffset = 0;
                                       updateCurrentPageLines();
                                   }
                               })
                        .bounds(this.buttonStartX + this.buttonSize + this.buttonSpacing + this.pageTextWidth,
                                this.buttonY, this.buttonSize, this.buttonSize)
                        .tooltip(Tooltip.create(Component.translatable("gui.random_enchant.guide.next_button")))
                        .build();
        this.addRenderableWidget(nextButton);

        updateCurrentPageLines();
        this.allSearchResults = new ArrayList<>();
    }

    private void onSearchTextChanged(String text) {
        if (text.isEmpty()) {
            // 当输入框为空时，显示提示文本
            searchBox.setSuggestion(Component.translatable("gui.random_enchant.guide.search").getString());
        } else {
            // 当有输入内容时，清除提示
            searchBox.setSuggestion(null);
        }
    }

    private void performSearch() {
        String query = searchBox.getValue().toLowerCase(Locale.ROOT).trim();
        if (query.isEmpty()) {
            searchMode = false; // 搜索词为空，退出搜索模式
            allSearchResults.clear();
            searchResultsList.updateResults(allSearchResults);
            return;
        }

        searchMode = true; // 标记已执行搜索
        allSearchResults.clear();
        for (int page = 0; page < pages.size(); page++) {
            String pageText = pages.get(page);
            String[] lines = pageText.split("\\r?\\n");
            for (int line = 0; line < lines.length; line++) {
                String lineStr = lines[line].trim();
                // 支持拼音搜索：如果 JustEnoughCharacters 模组加载，使用其匹配方法
                if (matchesQuery(lineStr, query)) {
                    String preview = lineStr.length() > 100 ? lineStr.substring(0, 97) + "..." : lineStr;
                    allSearchResults.add(new SearchResult(page, line, preview));
                }
            }
        }

        searchResultsList.updateResults(allSearchResults);
    }

    /**
     * 检查文本是否匹配搜索词，支持拼音搜索（JustEnoughCharacters / 通用拼音搜索模组）
     *
     * @param text  要检查的文本
     * @param query 搜索词（已转为小写）
     * @return 是否匹配
     */
    private boolean matchesQuery(String text, String query) {
        if (text.toLowerCase(Locale.ROOT).contains(query)) {
            return true;
        }
        // 尝试使用 JustEnoughCharacters 的拼音匹配
        return JecMatcher.contains(text, query);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (currentPageLines == null) return false;
        if (button == 0 && currentPageLines.size() > maxLines) {
            int totalLines = currentPageLines.size();
            int scrollbarHeight = (int) ((float) maxLines / totalLines * contentHeight);
            int scrollbarY = contentY + (int) (scrollOffset / totalLines * contentHeight);
            int scrollbarX = contentX + contentWidth + 2;
            int scrollbarWidth = 3;

            if (mouseX >= scrollbarX && mouseX <= scrollbarX + scrollbarWidth && mouseY >= scrollbarY &&
                mouseY <= scrollbarY + scrollbarHeight) {
                isDraggingScrollbar = true;
                dragStartMouseY = (int) mouseY;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDraggingScrollbar) {
            int totalLines = currentPageLines.size();
            int scrollbarHeight = (int) ((float) maxLines / totalLines * contentHeight);
            int maxScroll = totalLines - maxLines;
            double deltaY = mouseY - dragStartMouseY;
            double deltaScroll = deltaY / (contentHeight - scrollbarHeight) * maxScroll;
            scrollOffset = Mth.clamp(scrollOffset + deltaScroll, 0, maxScroll);
            dragStartMouseY = (int) mouseY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isDraggingScrollbar) {
            isDraggingScrollbar = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void updateCurrentPageLines() {
        if (pages == null || pages.isEmpty()) {
            currentPageLines = new ArrayList<>();
            return;
        }
        String pageText = pages.get(currentPage);
        Font font = Minecraft.getInstance().font;
        currentPageLines = font.split(Component.literal(pageText), contentWidth);
        maxLines = contentHeight / font.lineHeight;
        double maxScroll = Math.max(0, currentPageLines.size() - maxLines);
        scrollOffset = Mth.clamp(scrollOffset, 0, maxScroll);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.minecraft == null || currentPageLines == null) return;
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        Font font = Minecraft.getInstance().font;
        int lineHeight = font.lineHeight;

        // ----- 绘制右侧指南内容 -----
        if (!currentPageLines.isEmpty()) {
            int startLine = Mth.floor(scrollOffset);
            int endLine = Math.min(startLine + maxLines, currentPageLines.size());
            int y = contentY;
            for (int i = startLine; i < endLine; i++) {
                graphics.drawString(font, currentPageLines.get(i), contentX, y, 0xFFFFFF, false);
                y += lineHeight;
            }
        }

        // ----- 绘制内容区域滚动条（仅在需要时）-----
        if (currentPageLines.size() > maxLines) {
            int totalLines = currentPageLines.size();
            int scrollbarHeight = (int) ((float) maxLines / totalLines * contentHeight);
            int scrollbarY = contentY + (int) (scrollOffset / totalLines * contentHeight);

            graphics.fill(contentX + contentWidth + 2, contentY, contentX + contentWidth + 4, contentY + contentHeight,
                          0xFF888888);
            graphics.fill(contentX + contentWidth + 2, scrollbarY, contentX + contentWidth + 4,
                          scrollbarY + scrollbarHeight, 0xFFEEEEEE);
        }

        // ----- 绘制标题 -----
        String title = Component.translatable("gui.random_enchant.guide.title").getString();
        graphics.drawString(font, title, this.width / 2 - font.width(title) / 2, padding, 0xFFFFFF, false);

        // ----- 绘制页码（两个按钮中间）-----
        String pageIndicator = (currentPage + 1) + "/" + pages.size();
        int pageTextX = this.buttonStartX + this.buttonSize + this.buttonSpacing +
                        (this.pageTextWidth - font.width(pageIndicator)) / 2;
        graphics.drawString(font, pageIndicator, pageTextX, this.buttonY + (this.buttonSize - font.lineHeight) / 2,
                            0xFFFFFF, false);

        // ----- 无结果提示（仅在搜索模式且结果为空时显示）-----
        if (searchMode && allSearchResults.isEmpty()) {
            String noResult = Component.translatable("gui.random_enchant.guide.no_results").getString();
            graphics.drawString(font, noResult, this.padding, this.searchListY + 10, 0xFF5555, false);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (mouseX >= contentX && mouseX <= contentX + contentWidth && mouseY >= contentY &&
            mouseY <= contentY + contentHeight) {
            if (currentPageLines.size() > maxLines) {
                double delta = -scrollY * 0.5;
                double maxScroll = currentPageLines.size() - maxLines;
                scrollOffset = Mth.clamp(scrollOffset + delta, 0, maxScroll);
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.minecraft == null) {
            return true; // Exit early to avoid null access
        }
        // 回车键触发搜索（仅当搜索框获得焦点时）
        if ((keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) && searchBox != null &&
            searchBox.isFocused()) {
            performSearch();
            return true;
        }
        // 左右箭头翻页（保持不变）
        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            if (currentPage > 0) {
                currentPage--;
                scrollOffset = 0;
                updateCurrentPageLines();
            }
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (currentPage < pages.size() - 1) {
                currentPage++;
                scrollOffset = 0;
                updateCurrentPageLines();
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void removed() {
        super.removed();
        this.minecraft = null; // Ensure no further access
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int width, int height) {
        // 保存当前状态
        int savedPage = this.currentPage;
        double savedScroll = this.scrollOffset;

        // 重新初始化
        super.resize(minecraft, width, height);

        // 恢复状态
        this.currentPage = savedPage;
        this.scrollOffset = savedScroll;
        updateCurrentPageLines();
    }

    /**
     * JustEnoughCharacters (通用拼音搜索) 模组的兼容工具类
     * 通过反射调用 JEC 的拼音匹配方法，避免硬依赖
     * JEC 的核心匹配类为 me.towdium.jecharacters.utils.Match
     * 其方法 boolean contains(CharSequence, CharSequence) 用于检查文本是否包含搜索词（支持拼音）
     */
    static class JecMatcher {
        private static boolean jecChecked = false;
        private static Method containsMethod;

        /**
         * 检查文本是否包含搜索词，支持拼音匹配
         *
         * @param text   要搜索的文本
         * @param search 搜索词
         * @return 是否匹配
         */
        static boolean contains(String text, String search) {
            if (!jecChecked) {
                initJec();
                jecChecked = true;
            }
            if (containsMethod != null) {
                try {
                    return (Boolean) containsMethod.invoke(null, text, search);
                } catch (Exception e) {
                    // 拼音匹配失败时返回false，回退到普通匹配
                    LOGGER.debug("JustEnoughCharacters 拼音匹配失败，回退到普通匹配: {}", e.getMessage());
                }
            }
            return false;
        }

        private static void initJec() {
            try {
                Class<?> matcherClass = Class.forName("me.towdium.jecharacters.utils.Match");
                // contains(CharSequence, CharSequence) 静态方法
                containsMethod = matcherClass.getMethod("contains", CharSequence.class, CharSequence.class);
                LOGGER.info("JustEnoughCharacters (通用拼音搜索) 模组已加载，GuideScreen 已启用拼音搜索支持");
            } catch (ClassNotFoundException e) {
                LOGGER.debug("JustEnoughCharacters 模组未安装");
            } catch (NoSuchMethodException e) {
                LOGGER.debug("JustEnoughCharacters 未找到匹配方法: {}", e.getMessage());
            } catch (Exception e) {
                LOGGER.debug("JustEnoughCharacters 初始化失败: {}", e.getMessage());
            }
        }
    }

    private record SearchResult(int page, int line, String preview) {}

    private class SearchResultsList extends ObjectSelectionList<SearchResultsList.Entry> {
        public SearchResultsList(Minecraft mc, int width, int height, int top, int itemHeight) {
            super(mc, width, height, top, itemHeight);
        }

        public void updateResults(List<SearchResult> results) {
            this.clearEntries();
            for (SearchResult result: results) {
                this.addEntry(new Entry(result));
            }
            // 重置滚动条到顶部
            this.setScrollAmount(0);
        }

        @Override
        protected int getScrollbarPosition() {
            return this.getRight() - 6;
        }

        @Override
        public int getRowWidth() {
            return this.width - 10;
        }

        class Entry extends ObjectSelectionList.Entry<Entry> {
            private final SearchResult result;

            Entry(SearchResult result) { this.result = result; }

            @Override
            public void render(@NotNull GuiGraphics graphics, int index, int top, int left, int width, int height,
                               int mouseX, int mouseY, boolean hovering, float partialTick) {
                Font font = GuideScreen.this.font;
                String prefix = "P" + (result.page + 1) + ": ";
                String preview = result.preview;
                int maxWidth = width - 6;

                String fullText = prefix + preview;
                String displayText;
                if (font.width(fullText) <= maxWidth) {
                    displayText = fullText;
                } else {
                    int ellipsisWidth = font.width("...");
                    int available = maxWidth - ellipsisWidth;
                    displayText = font.plainSubstrByWidth(fullText, available) + "...";
                }

                int y = top + (height - 8) / 2;
                graphics.drawString(font, displayText, left + 2, y, 0xFFFFFF, false);

                if (hovering) {
                    GuideScreen.this.setTooltipForNextRenderPass(Component.literal(result.preview));
                }
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button == 0) {
                    currentPage = result.page;
                    scrollOffset = Math.max(0, result.line - 2);
                    updateCurrentPageLines();
                    return true;
                }
                return false;
            }

            @Override
            public @NotNull Component getNarration() {
                return Component.literal(result.preview);
            }
        }
    }
}
