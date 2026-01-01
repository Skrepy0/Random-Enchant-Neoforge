package com.random_enchant.network.packet.S2C;

import com.random_enchant.RandomEnchant;
import com.random_enchant.render.particle.ParticleRenderType.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.random_enchant.particle.ModParticleHelper.addParticlesOnBlock;

public class AddEnchantedBlockParticleS2CPacket implements CustomPacketPayload {
    public static final Type<AddEnchantedBlockParticleS2CPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "add_enchanted_block_particle"));

    public final BlockPos blockPos;
    public final RenderType type;

    public AddEnchantedBlockParticleS2CPacket(BlockPos blockPos, RenderType type) {
        this.blockPos = blockPos;
        this.type = type;
    }

    public AddEnchantedBlockParticleS2CPacket(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
        this.type = buf.readEnum(RenderType.class);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeEnum(this.type);
    }

    public static final StreamCodec<FriendlyByteBuf, AddEnchantedBlockParticleS2CPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> packet.write(buf),
                    AddEnchantedBlockParticleS2CPacket::new
            );

    public static void handle(AddEnchantedBlockParticleS2CPacket data, IPayloadContext context) {
        context.enqueueWork(() -> {
            SimpleParticleType particleType = switch (data.type) {
                case START_BLOCK -> ParticleTypes.GLOW;
                case END_BLOCK -> ParticleTypes.FLAME;
                case COMMON -> ParticleTypes.COMPOSTER;
            };
            addParticlesOnBlock(data.blockPos, particleType);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}