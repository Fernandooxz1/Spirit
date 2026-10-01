package com.fernando.modding;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class NotebookScreen extends Screen {

    public static final int TAB_REVELATIONS = 0;
    public static final int TAB_BLESSINGS = 1;

    private int currentTab = TAB_REVELATIONS;
    private int scrollOffset = 0;
    private int maxScroll = 0;
    private boolean syncRequested = false;

    private Button btnRevelations;
    private Button btnBlessings;

    public NotebookScreen() {
        this(TAB_REVELATIONS);
    }

    public NotebookScreen(int initialTab) {
        super(Component.translatable("item.spirit.notebook"));
        this.currentTab = initialTab;
    }

    @Override
    protected void init() {
        super.init();
        if (!syncRequested) {
            PacketDistributor.sendToServer(new RequestDevotionSyncPayload());
            syncRequested = true;
        }

        int centerX = this.width / 2;
        int cardWidth = 360;
        int cardX = centerX - cardWidth / 2;
        int cardY = 8;

        // Pestañas superiores: [ 📖 Revelaciones ] y [ ✨ Bendiciones ]
        btnRevelations = this.addRenderableWidget(Button.builder(
                Component.literal("📖 Revelaciones"),
                btn -> switchTab(TAB_REVELATIONS)
        ).bounds(cardX + 16, cardY + 28, 158, 20).build());

        btnBlessings = this.addRenderableWidget(Button.builder(
                Component.literal("✨ Bendiciones"),
                btn -> switchTab(TAB_BLESSINGS)
        ).bounds(cardX + 186, cardY + 28, 158, 20).build());

        updateTabButtonStyles();
    }

    private void switchTab(int tab) {
        this.currentTab = tab;
        this.scrollOffset = 0;
        updateTabButtonStyles();
    }

    private void updateTabButtonStyles() {
        if (btnRevelations != null && btnBlessings != null) {
            btnRevelations.active = (currentTab != TAB_REVELATIONS);
            btnBlessings.active = (currentTab != TAB_BLESSINGS);
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

        // Título del Diario
        guiGraphics.drawCenteredString(this.font, "DIARIO ESPIRITUAL", centerX, cardY + 7, 0xFFD700);

        // Barra de estadísticas del lector
        String statsBar = "§7Revelaciones: §e" + discoveredCount + "/" + EpiphanyRegistry.EPIPHANIES.size() +
                " §8| §7Lectura: §f" + formatReadingTime(readingSeconds) +
                " §8| §7Rango: " + (tier >= 2 ? "§6Blessed II" : "§bBlessed I");
        guiGraphics.drawCenteredString(this.font, statsBar, centerX, cardY + 17, 0xCCCCCC);

        // Separador debajo de las pestañas
        guiGraphics.fill(cardX + 16, cardY + 52, cardX + cardWidth - 16, cardY + 53, 0xFF333333);

        // Área de lista con recorte
        int listYMin = cardY + 58;
        int listYMax = cardY + cardHeight - 12;
        int contentWidth = cardWidth - 36;
        int contentX = cardX + 18;

        guiGraphics.enableScissor(contentX - 2, listYMin, contentX + contentWidth + 2, listYMax);

        int y = listYMin - scrollOffset;

        if (currentTab == TAB_REVELATIONS) {
            // ================= PESTAÑA: REVELACIONES =================
            for (var epiphany : EpiphanyRegistry.EPIPHANIES) {
                boolean isDiscovered = ClientDevotionCache.isVerseDiscovered(epiphany.id());
                int entryHeight = isDiscovered ? 54 : 28;

                if (y + entryHeight >= listYMin && y <= listYMax) {
                    int boxColor = isDiscovered ? 0x22FFFFFF : 0x11FFFFFF;
                    guiGraphics.fill(contentX, y, contentX + contentWidth, y + entryHeight - 2, boxColor);
                    guiGraphics.renderOutline(contentX, y, contentWidth, entryHeight - 2, isDiscovered ? 0xFF555555 : 0xFF2A2A2A);

                    if (isDiscovered) {
                        // Título y Referencia
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
        } else {
            // ================= PESTAÑA: BENDICIONES =================
            // 1. Tarjeta de Estado de Gracia
            int statusHeight = 36;
            if (y + statusHeight >= listYMin && y <= listYMax) {
                guiGraphics.fill(contentX, y, contentX + contentWidth, y + statusHeight - 2, 0x2A181405);
                guiGraphics.renderOutline(contentX, y, contentWidth, statusHeight - 2, hasBlessed ? 0xFFFFD700 : 0xFF555555);

                if (hasBlessed) {
                    MobEffectInstance blessedInst = player.getEffect(Spirit.BLESSED);
                    int secRemaining = (blessedInst != null) ? blessedInst.getDuration() / 20 : 0;
                    String timeFormatted = String.format("%d:%02d", secRemaining / 60, secRemaining % 60);

                    guiGraphics.drawString(this.font, "§6Estado de Gracia: §aBLESSED " + (tier >= 2 ? "II" : "I"), contentX + 6, y + 5, 0xFFFFFF);
                    if (tier >= 2) {
                        guiGraphics.drawString(this.font, "§e★ DEVOCIÓN MÁXIMA", contentX + contentWidth - 110, y + 5, 0xFFFF55);
                    } else {
                        guiGraphics.drawString(this.font, "§8(Nivel II: 2h y 15 vers.)", contentX + contentWidth - 120, y + 5, 0x888888);
                    }
                    guiGraphics.drawString(this.font, "§7Duración restante: §e" + timeFormatted + " §8| §7Lectura total: §f" + formatReadingTime(readingSeconds), contentX + 6, y + 18, 0xCCCCCC);
                } else {
                    guiGraphics.drawString(this.font, "§6Estado de Gracia: §cINACTIVO", contentX + 6, y + 5, 0xFFFFFF);
                    String rankBadge = (tier >= 2 ? "§6Rango: Blessed II" : "§7Rango: Blessed I");
                    guiGraphics.drawString(this.font, rankBadge, contentX + contentWidth - 95, y + 5, 0xFFFFFF);
                    guiGraphics.drawString(this.font, "§7Lectura total: §f" + formatReadingTime(readingSeconds) + " §8| §7Lee la Biblia para activar dones.", contentX + 6, y + 18, 0xAAAAAA);
                }
            }
            y += statusHeight + 6;

            // 2. Encabezado de la lista de Dones
            if (y + 12 >= listYMin && y <= listYMax) {
                guiGraphics.drawString(this.font, "§eDONES Y EFECTOS OTORGADOS:", contentX + 2, y, 0xFFD700);
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
                if (y + 30 >= listYMin && y <= listYMax) {
                    guiGraphics.drawString(this.font, "§8Aún no has descubierto bendiciones.", contentX + 6, y + 5, 0x888888);
                    guiGraphics.drawString(this.font, "§8Busca los pasajes clave en la Biblia para obtener dones.", contentX + 6, y + 16, 0x666666);
                }
                y += 36;
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
                    y += itemHeight + 3;
                }
            }
        }

        int totalHeight = (y + scrollOffset) - listYMin;
        maxScroll = Math.max(0, totalHeight - (listYMax - listYMin) + 10);

        guiGraphics.disableScissor();

        // Renderizar botones de pestañas
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
