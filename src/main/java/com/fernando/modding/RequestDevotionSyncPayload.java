package com.fernando.modding;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Paquete Client→Server solicitando sincronización de datos de devoción.
 * Se envía cuando el jugador abre el Notebook.
 */
public record RequestDevotionSyncPayload() implements CustomPacketPayload {

    public static final Type<RequestDevotionSyncPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Spirit.MODID, "request_devotion_sync")
    );

    public static final StreamCodec<ByteBuf, RequestDevotionSyncPayload> STREAM_CODEC = StreamCodec.unit(
            new RequestDevotionSyncPayload()
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
