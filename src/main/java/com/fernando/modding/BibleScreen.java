package com.fernando.modding;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashSet;
import java.util.Set;

public class BibleScreen extends Screen {

    private int currentBookIndex = 0;      // 0 = Génesis
    private int currentChapterIndex = 0;   // 0 = Capítulo 1
    private int scrollOffset = 0;          // Desplazamiento vertical de versículos
    private int maxScroll = 0;             // Límite máximo de scroll de versículos

    // Navegación de libros al costado (Sidebar)
    private int bookScrollOffset = 0;      // Desplazamiento vertical de la lista de libros
    private int maxBookScroll = 0;         // Límite de scroll de la lista de libros
    private static final int BOOK_ITEM_HEIGHT = 16;

    // Detección de lectura y revelación (Epifanías)
    private int readingTicksOnChapter = 0;
    private int lastReadBookIndex = -1;
    private int lastReadChapterIndex = -1;
    private final Set<String> sessionDiscovered = new HashSet<>();
    private long openTimeMillis = 0;

    public BibleScreen() {
        super(Component.literal("Santa Biblia"));
    }

    @Override
    protected void init() {
        super.init();
        openTimeMillis = System.currentTimeMillis();

        int centerX = this.width / 2;

        // Botón "<" (Capítulo anterior)
        this.addRenderableWidget(Button.builder(Component.literal("<"), btn -> previousChapter())
                .bounds(centerX - 150, 15, 20, 20)
                .build());

        // Botón ">" (Capítulo siguiente)
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
        readingTicksOnChapter = 0;
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
        readingTicksOnChapter = 0;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int centerX = this.width / 2;
        int cardWidth = 320;
        int cardX = centerX - cardWidth / 2;
        int sidebarWidth = 56;
        int sidebarX = Math.max(6, cardX - sidebarWidth - 8);

        // Si el puntero está sobre la barra lateral de libros, scrolleamos los libros
        if (mouseX >= sidebarX && mouseX <= sidebarX + sidebarWidth) {
            int nuevoScroll = bookScrollOffset - (int) (scrollY * BOOK_ITEM_HEIGHT * 2);
            bookScrollOffset = Math.max(0, Math.min(nuevoScroll, maxBookScroll));
            return true;
        }

        // Si está sobre la tarjeta de lectura, scrolleamos los versículos
        int nuevoScroll = scrollOffset - (int) (scrollY * 24);
        scrollOffset = Math.max(0, Math.min(nuevoScroll, maxScroll));
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // Clic izquierdo
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
                    readingTicksOnChapter = 0;
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Método vacío para neutralizar completamente el shader de desenfoque de Minecraft
    }

    @Override
    public void onClose() {
        long elapsedSec = (System.currentTimeMillis() - openTimeMillis) / 1000;
        if (elapsedSec > 0) {
            PacketDistributor.sendToServer(new DiscoverEpiphanyPayload("", (int) Math.min(3600, elapsedSec)));
        }
        super.onClose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 1. Oscurecimiento suave del fondo sin desenfoque molesto
        guiGraphics.fill(0, 0, this.width, this.height, 0x88000000);

        int centerX = this.width / 2;

        // 2. Tarjeta central de lectura (fondo oscuro elegante con borde sutil)
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

        // Detección de lectura y epifanías (100 ticks = 5 segundos)
        if (lastReadBookIndex != currentBookIndex || lastReadChapterIndex != currentChapterIndex) {
            lastReadBookIndex = currentBookIndex;
            lastReadChapterIndex = currentChapterIndex;
            readingTicksOnChapter = 0;
        } else {
            readingTicksOnChapter++;
            if (readingTicksOnChapter == 100) {
                var epiphanyOpt = EpiphanyRegistry.findByChapter(currentBookIndex, currentChapterIndex);
                if (epiphanyOpt.isPresent()) {
                    var epiphany = epiphanyOpt.get();
                    if (!sessionDiscovered.contains(epiphany.id())) {
                        sessionDiscovered.add(epiphany.id());
                        PacketDistributor.sendToServer(new DiscoverEpiphanyPayload(epiphany.id(), 5));
                        if (Minecraft.getInstance().player != null) {
                            Minecraft.getInstance().player.playSound(SoundEvents.PLAYER_LEVELUP, 0.6f, 1.3f);
                        }
                    }
                }
            }
        }

        // 3. Barra lateral de libros al costado (Sidebar)
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

        // 4. Contenido del libro y capítulo actual
        var book = BibleData.BOOKS.get(currentBookIndex);
        var chapter = book.chapters().get(currentChapterIndex);

        // Título dorado (ej: "Génesis 1")
        String title = book.name() + " " + (currentChapterIndex + 1);
        guiGraphics.drawCenteredString(this.font, title, centerX, 20, 0xFFD700);

        // Área de recorte de versículos
        int margenIzquierdo = centerX - 140;
        int anchoTexto = 280;
        int areaYMin = 44;
        int areaYMax = this.height - 18;

        guiGraphics.enableScissor(margenIzquierdo, areaYMin, margenIzquierdo + anchoTexto, areaYMax);

        var chapterEpiphany = EpiphanyRegistry.findByChapter(currentBookIndex, currentChapterIndex);

        int y = areaYMin - scrollOffset;

        for (int v = 0; v < chapter.size(); v++) {
            boolean isEpiphanyVerse = chapterEpiphany.isPresent() && (chapterEpiphany.get().verseIndex() == v);
            String numeroVersiculo = (v + 1) + ". ";
            String textoCompleto = numeroVersiculo + chapter.get(v);

            var renglones = this.font.split(Component.literal(textoCompleto), anchoTexto);

            for (var renglon : renglones) {
                if (y + 10 >= areaYMin && y <= areaYMax) {
                    // Si es un versículo clave con epifanía divina, brilla en dorado suave
                    int color = isEpiphanyVerse ? 0xFFFFD700 : 0xFFE0E0E0;
                    guiGraphics.drawString(this.font, renglon, margenIzquierdo, y, color);
                }
                y += 10;
            }
            y += 5; // Separación entre versículos
        }

        int alturaTotalContenido = (y + scrollOffset) - areaYMin;
        int alturaVentana = areaYMax - areaYMin;
        maxScroll = Math.max(0, alturaTotalContenido - alturaVentana + 10);

        guiGraphics.disableScissor();

        // 5. Botones interactivos < y >
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // 6. Tooltip flotante al pasar el mouse por un libro
        if (hoveredBook != null) {
            guiGraphics.renderTooltip(this.font, Component.literal(hoveredBook.name()), (int) mouseX, (int) mouseY);
        }
    }
}