package com.fernando.modding;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BibleScreen extends Screen {

    private int currentBookIndex = 0;
    private int currentChapterIndex = 0;
    private int scrollOffset = 0;
    private int maxScroll = 0;

    private int bookScrollOffset = 0;
    private int maxBookScroll = 0;
    private static final int BOOK_ITEM_HEIGHT = 16;

    public BibleScreen() {
        super(Component.literal("Santa Biblia"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;

        this.addRenderableWidget(Button.builder(Component.literal("<"), btn -> previousChapter())
                .bounds(centerX - 150, 15, 20, 20)
                .build());

        this.addRenderableWidget(Button.builder(Component.literal(">"), btn -> nextChapter())
                .bounds(centerX + 130, 15, 20, 20)
                .build());
    }

    private void previousChapter() {
        if (currentChapterIndex > 0) {
            currentChapterIndex--;
        } else if (currentBookIndex > 0) {
            currentBookIndex--;
            currentChapterIndex = BibleData.BOOKS.get(currentBookIndex).chapters().size() - 1;
        }
        scrollOffset = 0;
    }

    private void nextChapter() {
        var currentBook = BibleData.BOOKS.get(currentBookIndex);
        if (currentChapterIndex < currentBook.chapters().size() - 1) {
            currentChapterIndex++;
        } else if (currentBookIndex < BibleData.BOOKS.size() - 1) {
            currentBookIndex++;
            currentChapterIndex = 0;
        }
        scrollOffset = 0;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int centerX = this.width / 2;
        int cardWidth = 320;
        int cardX = centerX - cardWidth / 2;
        int sidebarWidth = 56;
        int sidebarX = Math.max(6, cardX - sidebarWidth - 8);

        if (mouseX >= sidebarX && mouseX <= sidebarX + sidebarWidth) {
            int nuevoScroll = bookScrollOffset - (int) (scrollY * BOOK_ITEM_HEIGHT * 2);
            bookScrollOffset = Math.max(0, Math.min(nuevoScroll, maxBookScroll));
            return true;
        }

        int nuevoScroll = scrollOffset - (int) (scrollY * 24);
        scrollOffset = Math.max(0, Math.min(nuevoScroll, maxScroll));
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int centerX = this.width / 2;
            int cardWidth = 320;
            int cardX = centerX - cardWidth / 2;
            int sidebarWidth = 56;
            int sidebarX = Math.max(6, cardX - sidebarWidth - 8);
            int listYMin = 26;
            int listYMax = this.height - 12;

            if (mouseX >= sidebarX && mouseX <= sidebarX + sidebarWidth && mouseY >= listYMin && mouseY <= listYMax) {
                int clickedY = (int) mouseY - listYMin + bookScrollOffset;
                int clickedIndex = clickedY / BOOK_ITEM_HEIGHT;
                if (clickedIndex >= 0 && clickedIndex < BibleData.BOOKS.size()) {
                    currentBookIndex = clickedIndex;
                    currentChapterIndex = 0;
                    scrollOffset = 0;
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        guiGraphics.fill(0, 0, this.width, this.height, 0x88000000);

        int centerX = this.width / 2;

        int cardWidth = 320;
        int cardHeight = this.height - 16;
        int cardX = centerX - cardWidth / 2;
        int cardY = 8;

        guiGraphics.fill(cardX, cardY, cardX + cardWidth, cardY + cardHeight, 0xEE121212);
        guiGraphics.renderOutline(cardX, cardY, cardWidth, cardHeight, 0xFF3A3A3A);

        if (BibleData.BOOKS.isEmpty()) {
            guiGraphics.drawCenteredString(this.font, "Cargando Biblia...", centerX, this.height / 2, 0xFFFFFF);
            super.render(guiGraphics, mouseX, mouseY, partialTick);
            return;
        }

        int sidebarWidth = 56;
        int sidebarX = Math.max(6, cardX - sidebarWidth - 8);
        int sidebarY = 8;
        int sidebarHeight = this.height - 16;

        guiGraphics.fill(sidebarX, sidebarY, sidebarX + sidebarWidth, sidebarY + sidebarHeight, 0xEE121212);
        guiGraphics.renderOutline(sidebarX, sidebarY, sidebarWidth, sidebarHeight, 0xFF3A3A3A);

        guiGraphics.drawCenteredString(this.font, "LIBROS", sidebarX + sidebarWidth / 2, sidebarY + 6, 0xAAAAAA);

        int listYMin = sidebarY + 18;
        int listYMax = sidebarY + sidebarHeight - 4;
        maxBookScroll = Math.max(0, (BibleData.BOOKS.size() * BOOK_ITEM_HEIGHT) - (listYMax - listYMin));

        guiGraphics.enableScissor(sidebarX + 1, listYMin, sidebarX + sidebarWidth - 1, listYMax);

        BibleData.Book hoveredBook = null;

        for (int i = 0; i < BibleData.BOOKS.size(); i++) {
            int itemY = listYMin + (i * BOOK_ITEM_HEIGHT) - bookScrollOffset;
            if (itemY + BOOK_ITEM_HEIGHT >= listYMin && itemY <= listYMax) {
                boolean isSelected = (i == currentBookIndex);
                boolean isHovered = (mouseX >= sidebarX + 3 && mouseX <= sidebarX + sidebarWidth - 3
                        && mouseY >= itemY && mouseY < itemY + BOOK_ITEM_HEIGHT);

                if (isSelected) {
                    guiGraphics.fill(sidebarX + 3, itemY, sidebarX + sidebarWidth - 3, itemY + BOOK_ITEM_HEIGHT - 2, 0xFF4A3800);
                    guiGraphics.renderOutline(sidebarX + 3, itemY, sidebarWidth - 6, BOOK_ITEM_HEIGHT - 2, 0xFFFFD700);
                } else if (isHovered) {
                    guiGraphics.fill(sidebarX + 3, itemY, sidebarX + sidebarWidth - 3, itemY + BOOK_ITEM_HEIGHT - 2, 0x33FFFFFF);
                }

                int textColor = isSelected ? 0xFFFFD700 : (isHovered ? 0xFFFFFFFF : 0xFFBBBBBB);
                guiGraphics.drawCenteredString(this.font, BibleData.BOOKS.get(i).abbrev(), sidebarX + sidebarWidth / 2, itemY + 3, textColor);

                if (isHovered) {
                    hoveredBook = BibleData.BOOKS.get(i);
                }
            }
        }

        guiGraphics.disableScissor();

        var book = BibleData.BOOKS.get(currentBookIndex);
        var chapter = book.chapters().get(currentChapterIndex);

        String title = book.name() + " " + (currentChapterIndex + 1);
        guiGraphics.drawCenteredString(this.font, title, centerX, 20, 0xFFD700);

        int margenIzquierdo = centerX - 140;
        int anchoTexto = 280;
        int areaYMin = 44;
        int areaYMax = this.height - 18;

        guiGraphics.enableScissor(margenIzquierdo, areaYMin, margenIzquierdo + anchoTexto, areaYMax);

        int y = areaYMin - scrollOffset;

        for (int v = 0; v < chapter.size(); v++) {
            String numeroVersiculo = (v + 1) + ". ";
            String textoCompleto = numeroVersiculo + chapter.get(v);

            var renglones = this.font.split(Component.literal(textoCompleto), anchoTexto);

            for (var renglon : renglones) {
                if (y + 10 >= areaYMin && y <= areaYMax) {
                    guiGraphics.drawString(this.font, renglon, margenIzquierdo, y, 0xFFE0E0E0);
                }
                y += 10;
            }
            y += 5;
        }

        int alturaTotalContenido = (y + scrollOffset) - areaYMin;
        int alturaVentana = areaYMax - areaYMin;
        maxScroll = Math.max(0, alturaTotalContenido - alturaVentana + 10);

        guiGraphics.disableScissor();

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (hoveredBook != null) {
            guiGraphics.renderTooltip(this.font, Component.literal(hoveredBook.name()), (int) mouseX, (int) mouseY);
        }
    }
}