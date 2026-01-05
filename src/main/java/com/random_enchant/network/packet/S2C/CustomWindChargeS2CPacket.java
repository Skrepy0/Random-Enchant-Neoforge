package com.random_enchant.network.packet.S2C;

import com.random_enchant.RandomEnchant;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CustomWindChargeS2CPacket implements CustomPacketPayload {
    // stream codec
    public static final StreamCodec<FriendlyByteBuf, CustomWindChargeS2CPacket> STREAM_CODEC =
            CustomPacketPayload.codec(CustomWindChargeS2CPacket::write, CustomWindChargeS2CPacket::new);
    public static Type<CustomWindChargeS2CPacket> TYPE =
            new Type<CustomWindChargeS2CPacket>(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "custom_wind_charge"));
    public int radius;
    public UUID uuid;

    public CustomWindChargeS2CPacket(UUID uuid, int radius) {
        this.uuid = uuid;
        this.radius = radius;
    }

    public CustomWindChargeS2CPacket(FriendlyByteBuf buf) {
        this.uuid = buf.readUUID();
        this.radius = buf.readInt();
    }

    public static void handle(CustomWindChargeS2CPacket data, IPayloadContext context) {
        runEnqueue(data, context);
    }

    @OnlyIn(Dist.CLIENT)
    private static void runEnqueue(CustomWindChargeS2CPacket data, IPayloadContext context) {
        context.enqueueWork(() -> {
            CustomWindChargeData.set(data.uuid, data.radius);
            if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null) {
                Minecraft.getInstance().level.playSound(Minecraft.getInstance().player, Minecraft.getInstance().player.getOnPos(), SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS);
            }
        });
    }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeUUID(this.uuid);
        pBuffer.writeInt(this.radius);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class CustomWindChargeData {
        private static final Map<UUID, Integer> dataMap = new ConcurrentHashMap<>();

        public static void set(UUID newUuid, int newRadius) {
            dataMap.put(newUuid, newRadius);
        }

        public static int getRadius(UUID uuid) {
            return dataMap.getOrDefault(uuid, 0);
        }
    }
}
