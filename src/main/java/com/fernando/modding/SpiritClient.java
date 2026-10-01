package com.fernando.modding;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.GatherEffectScreenTooltipsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.HashSet;
import java.util.Set;

@Mod(value = Spirit.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Spirit.MODID, value = Dist.CLIENT)
public class SpiritClient {

    public SpiritClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        Spirit.LOGGER.info("HELLO FROM SPIRIT CLIENT SETUP");
        BibleData.load();
    }

    /**
     * Oculta los sub-efectos de Blessed del inventario respetando showIcon() = false.
     * Si el jugador toma una poción vanilla real (showIcon() = true), sí se mostrará normalmente.
     */
    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        IClientMobEffectExtensions hideIfNoIcon = new IClientMobEffectExtensions() {
            @Override
            public boolean isVisibleInInventory(MobEffectInstance instance) {
                return instance.showIcon();
            }

            @Override
            public boolean isVisibleInGui(MobEffectInstance instance) {
                return instance.showIcon();
            }
        };

        Set<Holder<MobEffect>> handled = new HashSet<>();
        for (var epiphany : EpiphanyRegistry.EPIPHANIES) {
            Holder<MobEffect> holder = epiphany.effect();
            if (handled.add(holder) && !event.isMobEffectRegistered(holder.value())) {
                event.registerMobEffect(hideIfNoIcon, holder);
            }
        }
    }

    /**
     * Muestra los dones y versículos bíblicos activos al pasar el ratón sobre "Blessed" en el inventario.
     */
    @SubscribeEvent
    public static void onGatherEffectTooltip(GatherEffectScreenTooltipsEvent event) {
        if (event.getEffectInstance().is(Spirit.BLESSED)) {
            var player = Minecraft.getInstance().player;
            if (player != null) {
                var discovered = ClientDevotionCache.getDiscoveredVerses();
                if (!discovered.isEmpty()) {
                    event.getTooltip().add(Component.literal("§6Dones de las Escrituras activos:"));
                    for (String id : discovered) {
                        var epiphanyOpt = EpiphanyRegistry.findById(id);
                        if (epiphanyOpt.isPresent()) {
                            var e = epiphanyOpt.get();
                            event.getTooltip().add(Component.literal(" §e• §f" + e.title() + " §7(§b" + e.citation() + "§7)"));
                        }
                    }
                }
            }
        }
    }
}
