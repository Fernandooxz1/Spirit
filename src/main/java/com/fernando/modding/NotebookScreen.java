package com.fernando.modding;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Pantalla del Diario Espiritual.
 * Muestra el progreso de revelaciones descubiertas, citas bíblicas, textos sagrados,
 * dones asociados y estadísticas globales de lectura del jugador.
 */
public class NotebookScreen extends Screen {

    private int scrollOffset = 0;
    private int maxScroll = 0;
    private boolean syncRequested = false;

    public NotebookScreen() {
        super(Component.translatable("item.spirit.notebook"));
    }

    @Override
    protected void init() {
        super.init();
        if (!syncRequested) {
            PacketDistributor.sendToServer(new RequestDevotionSyncPayload());
            syncRequested = true;
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Sin desenfoque molesto
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int nuevoScroll = scrollOffset - (int) (scrollY * 24);
        scrollOffset = Math.max(0, Math.min(nuevoScroll, maxScroll));
        return true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 1. Oscurecimiento suave del fondo
        guiGraphics.fill(0, 0, this.width, this.height, 0x88000000);

        int centerX = this.width / 2;
        int cardWidth = 360;
        int cardHeight = this.height - 16;
        int cardX = centerX - cardWidth / 2;
        int cardY = 8;

        // 2. Tarjeta principal
        guiGraphics.fill(cardX, cardY, cardX + cardWidth, cardY + cardHeight, 0xEE121212);
        guiGraphics.renderOutline(cardX, cardY, cardWidth, cardHeight, 0xFF3A3A3A);

        var player = Minecraft.getInstance().player;
        if (player == null) {
            super.render(guiGraphics, mouseX, mouseY, partialTick);
            return;
        }

        int discoveredCount = ClientDevotionCache.getDiscoveredCount();
        int tier = ClientDevotionCache.getDevotionTier();
        long readingSeconds = ClientDevotionCache.getTotalReadingSeconds();
        boolean hasBlessed = player.hasEffect(Spirit.BLESSED);

        // Título del Diario Espiritual
        guiGraphics.drawCenteredString(this.font, "DIARIO ESPIRITUAL", centerX, cardY + 8, 0xFFD700);

        // Barra de estadísticas del lector
        String statsBar = "§7Revelaciones: §e" + discoveredCount + "/" + EpiphanyRegistry.EPIPHANIES.size() +
                " §8| §7Lectura: §f" + formatReadingTime(readingSeconds) +
                " §8| §7Rango: " + (tier >= 2 ? "§6Blessed II" : "§bBlessed I");
        guiGraphics.drawCenteredString(this.font, statsBar, centerX, cardY + 20, 0xCCCCCC);

        // Separador decorativo
        guiGraphics.fill(cardX + 16, cardY + 34, cardX + cardWidth - 16, cardY + 35, 0xFF333333);

        // Área de lista con recorte
        int listYMin = cardY + 40;
        int listYMax = cardY + cardHeight - 12;
        int contentWidth = cardWidth - 36;
        int contentX = cardX + 18;

        guiGraphics.enableScissor(contentX - 2, listYMin, contentX + contentWidth + 2, listYMax);

        int y = listYMin - scrollOffset;

        // Lista de las 112 Revelaciones
        for (var epiphany : EpiphanyRegistry.EPIPHANIES) {
            boolean isDiscovered = ClientDevotionCache.isVerseDiscovered(epiphany.id());
            int entryHeight = isDiscovered ? 54 : 28;

            if (y + entryHeight >= listYMin && y <= listYMax) {
                int boxColor = isDiscovered ? 0x22FFFFFF : 0x11FFFFFF;
                guiGraphics.fill(contentX, y, contentX + contentWidth, y + entryHeight - 2, boxColor);
                guiGraphics.renderOutline(contentX, y, contentWidth, entryHeight - 2, isDiscovered ? 0xFF555555 : 0xFF2A2A2A);

                if (isDiscovered) {
                    // Título y Referencia Bíblica
                    String header = "§e" + epiphany.citation() + " §8- §6" + epiphany.title();
                    guiGraphics.drawString(this.font, header, contentX + 6, y + 4, 0xFFFFFF);

                    // Badge de estado
                    String badge = hasBlessed ? "§a[ACTIVO]" : "§7[DESBLOQUEADO]";
                    guiGraphics.drawString(this.font, badge, contentX + contentWidth - 85, y + 4, 0xFFFFFF);

                    // Efecto otorgado
                    String effectName = epiphany.effect().value().getDisplayName().getString();
                    String effectLine = "§b✦ Don: §f" + effectName + " " + (tier >= 2 ? "II" : "I");
                    guiGraphics.drawString(this.font, effectLine, contentX + 6, y + 15, 0x55FFFF);

                    // Texto del versículo (truncado a 2 renglones)
                    var lines = this.font.split(Component.literal("§7«" + epiphany.text() + "»"), contentWidth - 12);
                    int lineY = y + 27;
                    for (int l = 0; l < Math.min(2, lines.size()); l++) {
                        guiGraphics.drawString(this.font, lines.get(l), contentX + 6, lineY, 0xCCCCCC);
                        lineY += 9;
                    }
                } else {
                    guiGraphics.drawString(this.font, "§8??? - Pasaje Secreto en las Escrituras", contentX + 6, y + 5, 0x888888);
                    guiGraphics.drawString(this.font, "§8Explora la Biblia para recibir esta revelación divina.", contentX + 6, y + 15, 0x555555);
                }
            }
            y += entryHeight + 4;
        }

        int totalHeight = (y + scrollOffset) - listYMin;
        maxScroll = Math.max(0, totalHeight - (listYMax - listYMin) + 10);

        guiGraphics.disableScissor();

        // Barra de desplazamiento visual
        if (maxScroll > 0) {
            int scrollTrackHeight = listYMax - listYMin;
            int scrollThumbHeight = Math.max(16, (int) ((float) scrollTrackHeight * scrollTrackHeight / (scrollTrackHeight + maxScroll)));
            int scrollThumbY = listYMin + (int) ((float) scrollOffset / maxScroll * (scrollTrackHeight - scrollThumbHeight));
            int scrollbarX = cardX + cardWidth - 8;
            guiGraphics.fill(scrollbarX, listYMin, scrollbarX + 3, listYMax, 0x44000000);
            guiGraphics.fill(scrollbarX, scrollThumbY, scrollbarX + 3, scrollThumbY + scrollThumbHeight, 0x88AAAAAA);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    /**
     * Formats reading time in seconds cleanly into human-readable hours/minutes (e.g. "1h 30m", "45m", "30s").
     */
    public static String formatReadingTime(long totalSeconds) {
        if (totalSeconds <= 0) return "0m";
        long hours = totalSeconds / 3600;
        long mins = (totalSeconds % 3600) / 60;
        if (hours > 0) {
            return mins > 0 ? (hours + "h " + mins + "m") : (hours + "h");
        } else if (mins > 0) {
            return mins + "m";
        } else {
            return totalSeconds + "s";
        }
    }
}
