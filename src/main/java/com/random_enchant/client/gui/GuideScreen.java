package com.random_enchant.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 模组指南显示屏幕，支持分页、滚动和全文搜索。
 * 布局：左侧指南内容，右侧顶部搜索框 + 下方搜索结果列表。
 */
public class GuideScreen extends Screen {
    private final List<String> pages;                // 原始每页文本
    private int currentPage = 0;                      // 当前页码（从0开始）
    private double scrollOffset = 0;                   // 当前页的垂直滚动偏移（单位：行）
    private List<FormattedCharSequence> currentPageLines; // 当前页拆分成行后的列表
    private int maxLines;                               // 内容区域可显示的最大行数

    // 搜索相关
    private EditBox searchBox;                           // 搜索输入框
    private Button searchButton;                         // 搜索按钮
    private SearchResultsList searchResultsList;         // 搜索结果列表
    private List<SearchResult> allSearchResults;         // 所有搜索结果（原始数据）
    private boolean searchMode = false;                   // 是否处于搜索模式（即显示了搜索结果）

    // 左侧指南内容区域
    private static final int CONTENT_X = 160;
    private static final int CONTENT_Y = 30;
    private static final int CONTENT_WIDTH = 450;         // 宽度减小，为右侧面板留出空间
    private static final int CONTENT_HEIGHT = 350;

    // 右侧面板（搜索框 + 结果列表）
    private static final int RIGHT_PANEL_X = 1;
    private static final int RIGHT_PANEL_WIDTH = 122;                          // 右侧面板宽度
    private static final int SEARCH_BOX_Y = 30;                                // 搜索框 Y 坐标
    private static final int SEARCH_BUTTON_X = RIGHT_PANEL_X + RIGHT_PANEL_WIDTH + 5; // 搜索按钮 X
    private static final int SEARCH_LIST_Y = SEARCH_BOX_Y + 25;                // 结果列表 Y 起始     // 结果列表 Y 起始
    private static final int SEARCH_LIST_HEIGHT = 280;                         // 结果列表高度

    private boolean isDraggingScrollbar = false; // 是否正在拖动滚动条
    private int dragStartMouseY;                  // 开始拖动时的鼠标 Y 坐标（用于计算偏移）

    public GuideScreen(List<String> pages) {
        super(Component.translatable("gui.random_enchant.guide.title"));
        this.pages = pages;
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

        Font font = Minecraft.getInstance().font;
        int centerX = this.width / 2;
        int buttonY = this.height - 30;

        // ----- 右侧搜索框和按钮 -----
        this.searchBox = new EditBox(font, RIGHT_PANEL_X, SEARCH_BOX_Y, RIGHT_PANEL_WIDTH, 20, Component.translatable("gui.random_enchant.guide.search"));
        this.searchBox.setMaxLength(50);
        this.searchBox.setResponder(this::onSearchTextChanged);
        this.addRenderableWidget(searchBox);

        this.searchButton = Button.builder(Component.literal("🔍"), btn -> performSearch())
                .bounds(SEARCH_BUTTON_X, SEARCH_BOX_Y, 20, 20).build();
        this.addRenderableWidget(searchButton);

        // ----- 搜索结果列表（位于搜索框下方）-----
        this.searchResultsList = new SearchResultsList(this.minecraft, RIGHT_PANEL_WIDTH, SEARCH_LIST_HEIGHT, SEARCH_LIST_Y, 30);
        this.addRenderableWidget(searchResultsList);

        // ----- 底部翻页按钮 -----
        this.addRenderableWidget(Button.builder(Component.literal("<"), btn -> {
            if (currentPage > 0) {
                currentPage--;
                scrollOffset = 0;
                updateCurrentPageLines();
            }
        }).bounds(RIGHT_PANEL_X, buttonY, 20, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal(">"), btn -> {
            if (currentPage < pages.size() - 1) {
                currentPage++;
                scrollOffset = 0;
                updateCurrentPageLines();
            }
        }).bounds(RIGHT_PANEL_X + 100, buttonY, 20, 20).build());

        // 初始化当前页内容
        updateCurrentPageLines();
        this.allSearchResults = new ArrayList<>();
    }

    /**
     * 搜索框文本变化时的响应（可选项，这里选择点击按钮后搜索，以提高性能）
     */
    private void onSearchTextChanged(String text) {
        // 可选：实时搜索，但为了性能，保留按钮触发
    }

    /**
     * 执行搜索，更新搜索结果列表
     */
    private void performSearch() {
        String query = searchBox.getValue().toLowerCase(Locale.ROOT).trim();
        if (query.isEmpty()) {
            searchMode = false;
            allSearchResults.clear();
            searchResultsList.updateResults(allSearchResults);
            return;
        }

        allSearchResults.clear();
        for (int page = 0; page < pages.size(); page++) {
            String pageText = pages.get(page);
            // 按换行符分割页面文本为行
            String[] lines = pageText.split("\\r?\\n");
            for (int line = 0; line < lines.length; line++) {
                String lineStr = lines[line].trim();
                if (lineStr.toLowerCase(Locale.ROOT).contains(query)) {
                    // 限制预览长度
                    String preview = lineStr.length() > 100 ? lineStr.substring(0, 97) + "..." : lineStr;
                    allSearchResults.add(new SearchResult(page, line, preview));
                }
            }
        }

        searchMode = !allSearchResults.isEmpty();
        searchResultsList.updateResults(allSearchResults);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && currentPageLines.size() > maxLines) {
            // 检查是否点击在滚动条滑块区域
            int totalLines = currentPageLines.size();
            int scrollbarHeight = (int) ((float) maxLines / totalLines * CONTENT_HEIGHT);
            int scrollbarY = CONTENT_Y + (int) (scrollOffset / totalLines * CONTENT_HEIGHT);
            int scrollbarX = CONTENT_X + CONTENT_WIDTH + 2;
            int scrollbarWidth = 3; // 滑块宽度

            if (mouseX >= scrollbarX && mouseX <= scrollbarX + scrollbarWidth &&
                    mouseY >= scrollbarY && mouseY <= scrollbarY + scrollbarHeight) {
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
            int scrollbarHeight = (int) ((float) maxLines / totalLines * CONTENT_HEIGHT);
            int maxScroll = totalLines - maxLines;
            // 计算鼠标移动的距离，换算成行数的变化
            double deltaY = mouseY - dragStartMouseY;
            double deltaScroll = deltaY / (CONTENT_HEIGHT - scrollbarHeight) * maxScroll;
            scrollOffset = Mth.clamp(scrollOffset + deltaScroll, 0, maxScroll);
            dragStartMouseY = (int) mouseY; // 更新起始点，实现连续拖动
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

    /**
     * 更新当前页的行列表和最大可见行数
     */
    private void updateCurrentPageLines() {
        if (pages == null || pages.isEmpty()) {
            currentPageLines = new ArrayList<>();
            return;
        }
        String pageText = pages.get(currentPage);
        Font font = Minecraft.getInstance().font;
        currentPageLines = font.split(Component.literal(pageText), CONTENT_WIDTH);
        maxLines = CONTENT_HEIGHT / font.lineHeight;
        // 确保滚动偏移不超过上限
        double maxScroll = Math.max(0, currentPageLines.size() - maxLines);
        scrollOffset = Mth.clamp(scrollOffset, 0, maxScroll);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.minecraft == null) return;
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        Font font = Minecraft.getInstance().font;
        int lineHeight = font.lineHeight;

        // ----- 绘制左侧指南内容 -----
        if (!currentPageLines.isEmpty()) {
            int startLine = Mth.floor(scrollOffset);
            int endLine = Math.min(startLine + maxLines, currentPageLines.size());
            int y = CONTENT_Y;
            for (int i = startLine; i < endLine; i++) {
                graphics.drawString(font, currentPageLines.get(i), CONTENT_X, y, 0xFFFFFF, false);
                y += lineHeight;
            }
        }

        // ----- 绘制内容区域滚动条（仅在需要时）-----
        if (currentPageLines.size() > maxLines) {
            int totalLines = currentPageLines.size();
            int scrollbarHeight = (int) ((float) maxLines / totalLines * CONTENT_HEIGHT);
            int scrollbarY = CONTENT_Y + (int) (scrollOffset / totalLines * CONTENT_HEIGHT);

            graphics.fill(CONTENT_X + CONTENT_WIDTH + 2, CONTENT_Y,
                    CONTENT_X + CONTENT_WIDTH + 4, CONTENT_Y + CONTENT_HEIGHT, 0xFF888888);
            graphics.fill(CONTENT_X + CONTENT_WIDTH + 2, scrollbarY,
                    CONTENT_X + CONTENT_WIDTH + 4, scrollbarY + scrollbarHeight, 0xFFEEEEEE);
        }

        // ----- 绘制页码（底部中央）-----
        String pageIndicator = (currentPage + 1) + "/" + pages.size();
        graphics.drawString(font, pageIndicator, RIGHT_PANEL_X + 60 - font.width(pageIndicator) / 2, this.height - 25, 0xFFFFFF, false);

        // ----- 如果搜索结果为空且有搜索词，显示提示 -----
        if (searchMode && allSearchResults.isEmpty()) {
            String noResult = Component.translatable("gui.random_enchant.guide.no_results").getString();
            graphics.drawString(font, noResult, RIGHT_PANEL_X, SEARCH_LIST_Y + 10, 0xFF5555, false);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        // 优先处理指南内容区域滚动（如果鼠标在左侧区域内）
        if (mouseX >= CONTENT_X && mouseX <= CONTENT_X + CONTENT_WIDTH &&
                mouseY >= CONTENT_Y && mouseY <= CONTENT_Y + CONTENT_HEIGHT) {
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
    public boolean isPauseScreen() {
        return false;
    }

    // ==================== 内部类：搜索结果条目 ====================
    private record SearchResult(int page, int line, String preview) {
    }

    // ==================== 内部类：搜索结果列表 ====================
    private class SearchResultsList extends ObjectSelectionList<SearchResultsList.Entry> {
        public SearchResultsList(Minecraft mc, int width, int height, int top, int hight) {
            super(mc, width, height, top, hight);
        }

        public void updateResults(List<SearchResult> results) {
            this.clearEntries();
            for (GuideScreen.SearchResult result : results) {
                this.addEntry(new Entry(result));
            }
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

            Entry(SearchResult result) {
                this.result = result;
            }

            @Override
            public void render(GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
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

                // 垂直居中绘制
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
            public Component getNarration() {
                return Component.literal(result.preview);
            }
        }
    }
}