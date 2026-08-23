package com.random_enchant.network.packet.S2C;

import com.random_enchant.RandomEnchant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OneWithShadowS2CPacket implements CustomPacketPayload {
    private static final Map<Integer, Integer> ID_FLAG_MAP = new ConcurrentHashMap<>();
    public static Type<OneWithShadowS2CPacket> TYPE = new Type<OneWithShadowS2CPacket>(
            ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "one_with_shadow"));
    private static volatile int id;
    private static volatile int flag;
    // stream codec
    public static final StreamCodec<FriendlyByteBuf, OneWithShadowS2CPacket> STREAM_CODEC =
            CustomPacketPayload.codec(OneWithShadowS2CPacket::write, OneWithShadowS2CPacket::new);

    public OneWithShadowS2CPacket(int id, int flag) {
        OneWithShadowS2CPacket.id = id;
        OneWithShadowS2CPacket.flag = flag;
        ID_FLAG_MAP.put(id, flag);
    }

    public OneWithShadowS2CPacket(FriendlyByteBuf buf) {
        id = buf.readInt();
        flag = buf.readInt();
        ID_FLAG_MAP.put(id, flag);
    }

    public static void handle(OneWithShadowS2CPacket data, IPayloadContext context) {
        context.enqueueWork(() -> { ID_FLAG_MAP.put(id, flag); });
    }

    public static int getFlagById(int id) { return ID_FLAG_MAP.getOrDefault(id, -1); }

    public static void removeId(int id) { ID_FLAG_MAP.remove(id); }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(id);
        pBuffer.writeInt(flag);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
