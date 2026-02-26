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
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private boolean searchMode = false;                   // 是否处于搜索模式（即已执行过搜索）

    // 左侧指南内容区域
    private static final int CONTENT_X = 160;
    private static final int CONTENT_Y = 30;
    private static final int CONTENT_WIDTH = 450;
    private static final int CONTENT_HEIGHT = 350;

    // 右侧面板（搜索框 + 结果列表）
    private static final int RIGHT_PANEL_X = 2;
    private static final int RIGHT_PANEL_WIDTH = 122;
    private static final int SEARCH_BOX_Y = 30;
    private static final int SEARCH_BUTTON_X = RIGHT_PANEL_X + RIGHT_PANEL_WIDTH + 5;
    private static final int SEARCH_LIST_Y = SEARCH_BOX_Y + 25;
    private static final int SEARCH_LIST_HEIGHT = 280;

    private boolean isDraggingScrollbar = false;
    private int dragStartMouseY;

    public GuideScreen(List<String> pages) {
        super(Component.translatable("gui.random_enchant.guide.title"));
        this.pages = pages;
        this.currentPageLines = new ArrayList<>();
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
        int buttonY = this.height - 30;

        // ----- 右侧搜索框和按钮 -----
        this.searchBox = new EditBox(font, RIGHT_PANEL_X, SEARCH_BOX_Y, RIGHT_PANEL_WIDTH - 3, 20, Component.translatable("gui.random_enchant.guide.search"));
        this.searchBox.setMaxLength(50);
        this.searchBox.setResponder(this::onSearchTextChanged);
        this.searchBox.setSuggestion(Component.translatable("gui.random_enchant.guide.search").getString());
        this.addRenderableWidget(searchBox);

        this.searchButton = Button.builder(Component.literal("🔍"), btn -> performSearch()).bounds(SEARCH_BUTTON_X, SEARCH_BOX_Y, 20, 20).build();
        this.searchButton.setTooltip(Tooltip.create(Component.translatable("gui.random_enchant.guide.search")));
        this.addRenderableWidget(searchButton);

        this.searchResultsList = new SearchResultsList(this.minecraft, RIGHT_PANEL_WIDTH, SEARCH_LIST_HEIGHT, SEARCH_LIST_Y, (SEARCH_LIST_Y + SEARCH_LIST_HEIGHT) / 15);
        this.addRenderableWidget(searchResultsList);

        // ----- 底部翻页按钮 -----
        this.addRenderableWidget(Button.builder(Component.literal("<"), btn -> {
            if (currentPage > 0) {
                currentPage--;
                scrollOffset = 0;
                updateCurrentPageLines();
            }
        }).bounds(RIGHT_PANEL_X, buttonY, 20, 20).tooltip(Tooltip.create(Component.translatable("gui.random_enchant.guide.previous_button"))).build());

        this.addRenderableWidget(Button.builder(Component.literal(">"), btn -> {
            if (currentPage < pages.size() - 1) {
                currentPage++;
                scrollOffset = 0;
                updateCurrentPageLines();
            }
        }).bounds(RIGHT_PANEL_X + 100, buttonY, 20, 20).tooltip(Tooltip.create(Component.translatable("gui.random_enchant.guide.next_button"))).build());

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
            searchMode = false;          // 搜索词为空，退出搜索模式
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
                if (lineStr.toLowerCase(Locale.ROOT).contains(query)) {
                    String preview = lineStr.length() > 100 ? lineStr.substring(0, 97) + "..." : lineStr;
                    allSearchResults.add(new SearchResult(page, line, preview));
                }
            }
        }

        searchResultsList.updateResults(allSearchResults);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (currentPageLines == null) return false;
        if (button == 0 && currentPageLines.size() > maxLines) {
            int totalLines = currentPageLines.size();
            int scrollbarHeight = (int) ((float) maxLines / totalLines * CONTENT_HEIGHT);
            int scrollbarY = CONTENT_Y + (int) (scrollOffset / totalLines * CONTENT_HEIGHT);
            int scrollbarX = CONTENT_X + CONTENT_WIDTH + 2;
            int scrollbarWidth = 3;

            if (mouseX >= scrollbarX && mouseX <= scrollbarX + scrollbarWidth && mouseY >= scrollbarY && mouseY <= scrollbarY + scrollbarHeight) {
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
            double deltaY = mouseY - dragStartMouseY;
            double deltaScroll = deltaY / (CONTENT_HEIGHT - scrollbarHeight) * maxScroll;
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
        currentPageLines = font.split(Component.literal(pageText), CONTENT_WIDTH);
        maxLines = CONTENT_HEIGHT / font.lineHeight;
        double maxScroll = Math.max(0, currentPageLines.size() - maxLines);
        scrollOffset = Mth.clamp(scrollOffset, 0, maxScroll);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.minecraft == null || currentPageLines == null) return; // 增加空检查
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

            graphics.fill(CONTENT_X + CONTENT_WIDTH + 2, CONTENT_Y, CONTENT_X + CONTENT_WIDTH + 4, CONTENT_Y + CONTENT_HEIGHT, 0xFF888888);
            graphics.fill(CONTENT_X + CONTENT_WIDTH + 2, scrollbarY, CONTENT_X + CONTENT_WIDTH + 4, scrollbarY + scrollbarHeight, 0xFFEEEEEE);
        }

        // ----- 绘制标题 -----
        String title = Component.translatable("gui.random_enchant.guide.title").getString();
        graphics.drawString(font, title, this.width / 2 - font.width(title) / 2, 10, 0xFFFFFF, false);

        // ----- 绘制页码（底部中央）-----
        String pageIndicator = (currentPage + 1) + "/" + pages.size();
        graphics.drawString(font, pageIndicator, RIGHT_PANEL_X + 60 - font.width(pageIndicator) / 2, this.height - 25, 0xFFFFFF, false);

        // ----- 无结果提示（仅在搜索模式且结果为空时显示）-----
        if (searchMode && allSearchResults.isEmpty()) {
            String noResult = Component.translatable("gui.random_enchant.guide.no_results").getString();
            graphics.drawString(font, noResult, RIGHT_PANEL_X, SEARCH_LIST_Y + 10, 0xFF5555, false);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (mouseX >= CONTENT_X && mouseX <= CONTENT_X + CONTENT_WIDTH && mouseY >= CONTENT_Y && mouseY <= CONTENT_Y + CONTENT_HEIGHT) {
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
        if ((keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) && searchBox != null && searchBox.isFocused()) {
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
    public boolean isPauseScreen() {
        return true;
    }

    private record SearchResult(int page, int line, String preview) {
    }

    private class SearchResultsList extends ObjectSelectionList<SearchResultsList.Entry> {
        public SearchResultsList(Minecraft mc, int width, int height, int top, int bottom) {
            super(mc, width, height, top, bottom);
        }

        public void updateResults(List<SearchResult> results) {
            this.clearEntries();
            for (SearchResult result : results) {
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