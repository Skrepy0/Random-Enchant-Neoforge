package com.random_enchant.network.packet.S2C;

import com.random_enchant.RandomEnchant;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class BellSoundS2CPacket implements CustomPacketPayload {
    // stream codec
    public static final StreamCodec<FriendlyByteBuf, BellSoundS2CPacket> STREAM_CODEC =
            CustomPacketPayload.codec(BellSoundS2CPacket::write, BellSoundS2CPacket::new);
    public static Type<BellSoundS2CPacket> TYPE =
            new Type<BellSoundS2CPacket>(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "bell_sound"));
    public int flag;
    public BlockPos blockPos;


    public BellSoundS2CPacket(int flag, BlockPos pos) {
        this.blockPos = pos;
        this.flag = flag;
    }

    public BellSoundS2CPacket(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
        this.flag = buf.readInt();
    }

    public static void handle(BellSoundS2CPacket data, IPayloadContext context) {
        playSound(data, context);
    }

    @OnlyIn(Dist.CLIENT)
    private static void playSound(BellSoundS2CPacket data, IPayloadContext context) {
        context.enqueueWork(() -> {
            int i = data.flag;
            BlockPos pos = data.blockPos;
            if (i == 1 && Minecraft.getInstance().level != null) {
                Minecraft.getInstance().level
                        .playSound(Minecraft.getInstance().player, pos, SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.BLOCKS);
            }
        });
    }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeBlockPos(this.blockPos);
        pBuffer.writeInt(this.flag);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
