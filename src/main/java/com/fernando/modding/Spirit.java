package com.fernando.modding;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(Spirit.MODID)
public class Spirit {
    public static final String MODID = "spirit";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, MODID);

    // MOB EFFECTS
    public static final DeferredHolder<MobEffect, BlessedEffect> BLESSED = MOB_EFFECTS.register("blessed", BlessedEffect::new);

    // NEW ITEMS
    public static final DeferredItem<Bible> BIBLE = ITEMS.register("bible",
            () -> new Bible(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.RARE)
                    .fireResistant())
    );

    public static final DeferredItem<NotebookItem> NOTEBOOK = ITEMS.register("notebook",
            () -> new NotebookItem(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON))
    );

    // CREATIVE TABS
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPIRIT_TAB = CREATIVE_MODE_TABS.register("spirit_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MODID))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> BIBLE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(BIBLE.get());
                output.accept(NOTEBOOK.get());
            }).build());

    public Spirit(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerPayloads);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        MOB_EFFECTS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM SPIRIT SETUP");
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(MODID);
        registrar.playToServer(
                DiscoverEpiphanyPayload.TYPE,
                DiscoverEpiphanyPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        Player player = context.player();
                        if (player != null) {
                            PlayerDevotionData.addReadingTime(player, payload.readingSeconds());
                            if (!payload.epiphanyId().isEmpty()) {
                                var epiphanyOpt = EpiphanyRegistry.findById(payload.epiphanyId());
                                if (epiphanyOpt.isPresent()) {
                                    var epiphany = epiphanyOpt.get();
                                    boolean isNew = PlayerDevotionData.addDiscoveredVerse(player, epiphany.id());
                                    BlessedEffect.applyOrExtendBlessed(player, 6000); // +5 minutos de Blessed
                                    String msg = isNew ?
                                            "§6✨ ¡Nueva Revelación! §e" + epiphany.citation() + " §8- §f" + epiphany.title() :
                                            "§6✨ Revelación renovada (+5m): §e" + epiphany.citation();
                                    player.displayClientMessage(Component.literal(msg), true);
                                }
                            }
                        }
                    });
                }
        );
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO FROM SPIRIT SERVER STARTING");
    }

    @SubscribeEvent
    public void onEffectRemoved(MobEffectEvent.Remove event) {
        if (event.getEffect().is(BLESSED)) {
            BlessedEffect.removeSubEffects(event.getEntity());
        }
    }

    @SubscribeEvent
    public void onEffectExpired(MobEffectEvent.Expired event) {
        if (event.getEffectInstance() != null && event.getEffectInstance().is(BLESSED)) {
            BlessedEffect.removeSubEffects(event.getEntity());
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            PlayerDevotionData.copyFrom(event.getOriginal(), event.getEntity());
        }
    }
}
