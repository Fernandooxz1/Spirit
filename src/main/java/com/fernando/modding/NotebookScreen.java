package com.fernando.modding;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public class NotebookScreen extends Screen {

    private int scrollOffset = 0;
    private int maxScroll = 0;
    private boolean syncRequested = false;

    public NotebookScreen() {
        super(Component.literal("Diario Espiritual"));
    }

    @Override
    protected void init() {
        super.init();
        // Solicitar datos actualizados del servidor
        if (!syncRequested) {
            PacketDistributor.sendToServer(new RequestDevotionSyncPayload());
            syncRequested = true;
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Método vacío para evitar el desenfoque borroso de Minecraft
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int nuevoScroll = scrollOffset - (int) (scrollY * 24);
        scrollOffset = Math.max(0, Math.min(nuevoScroll, maxScroll));
        return true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 1. Oscurecimiento limpio
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

        // Leer del cache del cliente (sincronizado desde el servidor)
        int discoveredCount = ClientDevotionCache.getDiscoveredCount();
        int tier = ClientDevotionCache.getDevotionTier();
        long readingSeconds = ClientDevotionCache.getTotalReadingSeconds();
        long minutes = readingSeconds / 60;
        boolean hasBlessed = player.hasEffect(Spirit.BLESSED);

        // 3. Encabezado
        guiGraphics.drawCenteredString(this.font, "DIARIO ESPIRITUAL", centerX, cardY + 10, 0xFFD700);

        String stats = String.format("§7Revelaciones: §f%d/%d §8| §7Devoción: §eBlessed %s §8| §7Lectura: §f%dm",
                discoveredCount, EpiphanyRegistry.EPIPHANIES.size(),
                tier >= 2 ? "II" : "I", minutes);
        guiGraphics.drawCenteredString(this.font, stats, centerX, cardY + 24, 0xFFFFFF);

        guiGraphics.fill(cardX + 16, cardY + 36, cardX + cardWidth - 16, cardY + 37, 0xFF333333);

        // 4. Área de lista con recorte
        int listYMin = cardY + 42;
        int listYMax = cardY + cardHeight - 12;
        int contentWidth = cardWidth - 36;
        int contentX = cardX + 18;

        guiGraphics.enableScissor(contentX - 2, listYMin, contentX + contentWidth + 2, listYMax);

        int y = listYMin - scrollOffset;

        for (var epiphany : EpiphanyRegistry.EPIPHANIES) {
            // Usar el cache del cliente en lugar del NBT del servidor
            boolean isDiscovered = ClientDevotionCache.isVerseDiscovered(epiphany.id());

            // Caja de la entrada
            int entryHeight = isDiscovered ? 46 : 28;

            if (y + entryHeight >= listYMin && y <= listYMax) {
                int boxColor = isDiscovered ? 0x22FFFFFF : 0x11FFFFFF;
                guiGraphics.fill(contentX, y, contentX + contentWidth, y + entryHeight - 2, boxColor);
                guiGraphics.renderOutline(contentX, y, contentWidth, entryHeight - 2, isDiscovered ? 0xFF555555 : 0xFF2A2A2A);

                if (isDiscovered) {
                    // Título y Referencia
                    String header = "§e" + epiphany.citation() + " §8- §6" + epiphany.title();
                    guiGraphics.drawString(this.font, header, contentX + 6, y + 5, 0xFFFFFF);

                    // Badge de estado
                    String badge = hasBlessed ? "§a[ACTIVO]" : "§7[DESBLOQUEADO]";
                    guiGraphics.drawString(this.font, badge, contentX + contentWidth - 85, y + 5, 0xFFFFFF);

                    // Texto del versículo (truncado o en 2 renglones)
                    var lines = this.font.split(Component.literal("§7«" + epiphany.text() + "»"), contentWidth - 12);
                    int lineY = y + 18;
                    for (int l = 0; l < Math.min(2, lines.size()); l++) {
                        guiGraphics.drawString(this.font, lines.get(l), contentX + 6, lineY, 0xCCCCCC);
                        lineY += 9;
                    }
                } else {
                    // Oculto / No descubierto
                    guiGraphics.drawString(this.font, "§8??? - Pasaje Secreto en las Escrituras", contentX + 6, y + 5, 0x888888);
                    guiGraphics.drawString(this.font, "§8Explora la Biblia para recibir esta revelación divina.", contentX + 6, y + 15, 0x555555);
                }
            }

            y += entryHeight + 4;
        }

        int totalHeight = (y + scrollOffset) - listYMin;
        maxScroll = Math.max(0, totalHeight - (listYMax - listYMin) + 10);

        guiGraphics.disableScissor();

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
