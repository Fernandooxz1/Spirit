package com.fernando.modding;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla dedicada exclusivamente a las Bendiciones y Dones Divinos.
 * Accesible desde la pestaña superior del inventario.
 */
public class BlessingsScreen extends Screen {

    private int scrollOffset = 0;
    private int maxScroll = 0;
    private boolean syncRequested = false;

    public BlessingsScreen() {
        super(Component.literal("Bendiciones Divinas"));
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

        int tier = ClientDevotionCache.getDevotionTier();
        long readingSeconds = ClientDevotionCache.getTotalReadingSeconds();
        boolean hasBlessed = player.hasEffect(Spirit.BLESSED);

        // Título de la pantalla
        guiGraphics.drawCenteredString(this.font, "BENDICIONES DIVINAS", centerX, cardY + 8, 0xFFD700);
        guiGraphics.drawCenteredString(this.font, "§7Dones activos otorgados por las Sagradas Escrituras", centerX, cardY + 20, 0xAAAAAA);

        // Separador decorativo
        guiGraphics.fill(cardX + 16, cardY + 34, cardX + cardWidth - 16, cardY + 35, 0xFF333333);

        // Área de lista con recorte (scissor)
        int listYMin = cardY + 40;
        int listYMax = cardY + cardHeight - 12;
        int contentWidth = cardWidth - 36;
        int contentX = cardX + 18;

        guiGraphics.enableScissor(contentX - 2, listYMin, contentX + contentWidth + 2, listYMax);

        int y = listYMin - scrollOffset;

        // 1. Tarjeta de Estado de Gracia
        int statusHeight = 44;
        if (y + statusHeight >= listYMin && y <= listYMax) {
            guiGraphics.fill(contentX, y, contentX + contentWidth, y + statusHeight - 2, 0x2A181405);
            guiGraphics.renderOutline(contentX, y, contentWidth, statusHeight - 2, hasBlessed ? 0xFFFFD700 : 0xFF555555);

            if (hasBlessed) {
                MobEffectInstance blessedInst = player.getEffect(Spirit.BLESSED);
                int secRemaining = (blessedInst != null) ? blessedInst.getDuration() / 20 : 0;
                String timeFormatted = String.format("%d:%02d", secRemaining / 60, secRemaining % 60);

                guiGraphics.drawString(this.font, "§6Estado de Gracia: §aBLESSED " + (tier >= 2 ? "II" : "I"), contentX + 6, y + 6, 0xFFFFFF);
                if (tier >= 2) {
                    guiGraphics.drawString(this.font, "§e★ DEVOCIÓN MÁXIMA", contentX + contentWidth - 110, y + 6, 0xFFFF55);
                } else {
                    guiGraphics.drawString(this.font, "§8(Nivel II: 2h y 15 vers.)", contentX + contentWidth - 120, y + 6, 0x888888);
                }
                guiGraphics.drawString(this.font, "§7Duración restante: §e" + timeFormatted + " §8| §7Lectura total: §f" + NotebookScreen.formatReadingTime(readingSeconds), contentX + 6, y + 22, 0xCCCCCC);
            } else {
                guiGraphics.drawString(this.font, "§6Estado de Gracia: §cINACTIVO", contentX + 6, y + 6, 0xFFFFFF);
                String rankBadge = (tier >= 2 ? "§6Rango: Blessed II" : "§7Rango: Blessed I");
                guiGraphics.drawString(this.font, rankBadge, contentX + contentWidth - 95, y + 6, 0xFFFFFF);
                guiGraphics.drawString(this.font, "§7Lectura total: §f" + NotebookScreen.formatReadingTime(readingSeconds) + " §8| §7Lee la Biblia para activar dones.", contentX + 6, y + 22, 0xAAAAAA);
            }
        }
        y += statusHeight + 8;

        // 2. Encabezado de la lista de Dones
        if (y + 12 >= listYMin && y <= listYMax) {
            int discoveredCount = ClientDevotionCache.getDiscoveredCount();
            guiGraphics.drawString(this.font, "§eDONES Y EFECTOS OTORGADOS §8(" + discoveredCount + "):", contentX + 2, y, 0xFFD700);
        }
        y += 14;

        // 3. Lista de bendiciones descubiertas
        List<EpiphanyRegistry.Epiphany> discoveredEpiphanies = new ArrayList<>();
        for (var epiphany : EpiphanyRegistry.EPIPHANIES) {
            if (ClientDevotionCache.isVerseDiscovered(epiphany.id())) {
                discoveredEpiphanies.add(epiphany);
            }
        }

        if (discoveredEpiphanies.isEmpty()) {
            int emptyHeight = 44;
            if (y + emptyHeight >= listYMin && y <= listYMax) {
                guiGraphics.fill(contentX, y, contentX + contentWidth, y + emptyHeight - 2, 0x11FFFFFF);
                guiGraphics.renderOutline(contentX, y, contentWidth, emptyHeight - 2, 0xFF2A2A2A);
                guiGraphics.drawString(this.font, "§8Aún no has descubierto bendiciones.", contentX + 6, y + 8, 0x888888);
                guiGraphics.drawString(this.font, "§8Explora la Santa Biblia con el Diario Espiritual para recibir dones.", contentX + 6, y + 22, 0x666666);
            }
            y += emptyHeight + 4;
        } else {
            for (var epiphany : discoveredEpiphanies) {
                int itemHeight = 36;
                if (y + itemHeight >= listYMin && y <= listYMax) {
                    guiGraphics.fill(contentX, y, contentX + contentWidth, y + itemHeight - 2, 0x22FFFFFF);
                    guiGraphics.renderOutline(contentX, y, contentWidth, itemHeight - 2, hasBlessed ? 0xFF44AA44 : 0xFF444444);

                    String effectDisplayName = epiphany.effect().value().getDisplayName().getString();
                    String effectTitle = "§f✨ " + effectDisplayName + " §e" + (tier >= 2 ? "II" : "I");
                    guiGraphics.drawString(this.font, effectTitle, contentX + 6, y + 5, 0xFFFFFF);

                    String badge = hasBlessed ? "§a● DON ACTIVO" : "§7○ EN ESPERA";
                    guiGraphics.drawString(this.font, badge, contentX + contentWidth - 85, y + 5, 0xFFFFFF);

                    String origin = "§7Origen: §e" + epiphany.citation() + " §8- §f" + epiphany.title();
                    guiGraphics.drawString(this.font, origin, contentX + 6, y + 18, 0xAAAAAA);
                }
                y += itemHeight + 4;
            }
        }

        int totalHeight = (y + scrollOffset) - listYMin;
        maxScroll = Math.max(0, totalHeight - (listYMax - listYMin) + 10);

        guiGraphics.disableScissor();

        // Barra de desplazamiento visual si es necesario
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
}
