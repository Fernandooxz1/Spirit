package com.fernando.modding;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Paquete Server→Client que sincroniza los datos de devoción del jugador.
 * Se envía cuando el jugador abre el Notebook o cuando descubre una nueva epifanía.
 */
public record DevotionSyncPayload(
        List<String> discoveredVerses,
        long totalReadingSeconds,
        int devotionTier
) implements CustomPacketPayload {

    public static final Type<DevotionSyncPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Spirit.MODID, "devotion_sync")
    );

    public static final StreamCodec<ByteBuf, DevotionSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
            DevotionSyncPayload::discoveredVerses,
            ByteBufCodecs.VAR_LONG,
            DevotionSyncPayload::totalReadingSeconds,
            ByteBufCodecs.VAR_INT,
            DevotionSyncPayload::devotionTier,
            DevotionSyncPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
