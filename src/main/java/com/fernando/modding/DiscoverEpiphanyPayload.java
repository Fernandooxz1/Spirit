package com.fernando.modding;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DiscoverEpiphanyPayload(String epiphanyId, int readingSeconds) implements CustomPacketPayload {

    public static final Type<DiscoverEpiphanyPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Spirit.MODID, "discover_epiphany"));

    public static final StreamCodec<ByteBuf, DiscoverEpiphanyPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, DiscoverEpiphanyPayload::epiphanyId,
            ByteBufCodecs.VAR_INT, DiscoverEpiphanyPayload::readingSeconds,
            DiscoverEpiphanyPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
