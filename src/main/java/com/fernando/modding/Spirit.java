package com.fernando.modding;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
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
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);

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

    public static final DeferredItem<Bible> BIBLE_ITEM = BIBLE;
    public static final DeferredItem<NotebookItem> NOTEBOOK_ITEM = NOTEBOOK;

    // RECIPE SERIALIZERS
    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<BibleCraftingRecipe>> BIBLE_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("crafting_special_bible", () -> new SimpleCraftingRecipeSerializer<>(BibleCraftingRecipe::new));

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<NotebookCraftingRecipe>> NOTEBOOK_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("crafting_special_notebook", () -> new SimpleCraftingRecipeSerializer<>(NotebookCraftingRecipe::new));

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
        RECIPE_SERIALIZERS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM SPIRIT SETUP");
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(MODID);

        // 1. Descubrir epifanía (Client → Server)
        registrar.playToServer(
                DiscoverEpiphanyPayload.TYPE,
                DiscoverEpiphanyPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        Player player = context.player();
                        if (player != null) {
                            // Siempre contar tiempo de lectura (viene en onClose)
                            if (payload.readingSeconds() > 0) {
                                PlayerDevotionData.addReadingTime(player, payload.readingSeconds());
                            }
                            // Procesar epifanía solo si hay ID
                            if (!payload.epiphanyId().isEmpty()) {
                                var epiphanyOpt = EpiphanyRegistry.findById(payload.epiphanyId());
                                if (epiphanyOpt.isPresent()) {
                                    var epiphany = epiphanyOpt.get();
                                    boolean isNew = PlayerDevotionData.addDiscoveredVerse(player, epiphany.id());
                                    if (isNew) {
                                        // Solo aplicar Blessed para descubrimientos NUEVOS
                                        BlessedEffect.applyOrExtendBlessed(player, 6000); // +5 minutos de Blessed

                                        String effectName = epiphany.effect().value().getDisplayName().getString();
                                        String chatMsg = "§6✨ ¡Nueva Revelación! §e" + epiphany.citation() + " §8- §f" + epiphany.title() + " §b[+" + effectName + "]";
                                        player.sendSystemMessage(Component.literal(chatMsg));

                                        // Sincronizar datos al cliente después de la revelación
                                        sendDevotionSync(player);
                                    }
                                }
                            }
                        }
                    });
                }
        );

        // 2. Solicitar sincronización de devoción (Client → Server)
        registrar.playToServer(
                RequestDevotionSyncPayload.TYPE,
                RequestDevotionSyncPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        Player player = context.player();
                        if (player != null) {
                            sendDevotionSync(player);
                        }
                    });
                }
        );

        // 3. Sincronización de devoción (Server → Client)
        registrar.playToClient(
                DevotionSyncPayload.TYPE,
                DevotionSyncPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        ClientDevotionCache.update(
                                payload.discoveredVerses(),
                                payload.totalReadingSeconds(),
                                payload.devotionTier()
                        );
                    });
                }
        );
    }

    /**
     * Envía los datos de devoción del jugador desde el servidor al cliente.
     */
    public static void sendDevotionSync(Player player) {
        if (player == null || player.level().isClientSide()) return;
        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            var discovered = new java.util.ArrayList<>(PlayerDevotionData.getDiscoveredVerses(player));
            long readingSeconds = PlayerDevotionData.getTotalReadingSeconds(player);
            int tier = PlayerDevotionData.getDevotionTier(player);
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                    serverPlayer,
                    new DevotionSyncPayload(discovered, readingSeconds, tier)
            );
        }
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

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        sendDevotionSync(event.getEntity());
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getCrafting().is(BIBLE.get())) {
            removeCraftingItems(event.getInventory(), Items.LEATHER, 6);
            removeCraftingItems(event.getInventory(), Items.PAPER, 32);
        } else if (event.getCrafting().is(NOTEBOOK.get())) {
            removeCraftingItems(event.getInventory(), Items.LEATHER, 1);
            removeCraftingItems(event.getInventory(), Items.PAPER, 9);
        }
    }

    private void removeCraftingItems(Container inv, Item item, int amount) {
        if (inv == null || amount <= 0) return;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            if (amount <= 0) break;
            ItemStack stack = inv.getItem(i);
            if (stack.is(item)) {
                int toTake = Math.min(amount, stack.getCount());
                stack.shrink(toTake);
                if (stack.isEmpty()) {
                    inv.setItem(i, ItemStack.EMPTY);
                }
                amount -= toTake;
            }
        }
    }
}
