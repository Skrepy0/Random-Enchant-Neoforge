package com.random_enchant.network.packet.C2S;

import com.random_enchant.RandomEnchant;
import com.random_enchant.mixin_helper.ShieldDashMixinHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ShieldDashC2SPacket implements CustomPacketPayload{
    public int shieldDashCoolDown;

    public static final Type<ShieldDashC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID,"shield_dash"));
    public static final StreamCodec<FriendlyByteBuf,ShieldDashC2SPacket> STREAM_CODEC =
            CustomPacketPayload.codec(ShieldDashC2SPacket::write,ShieldDashC2SPacket::new);

    public ShieldDashC2SPacket(FriendlyByteBuf buf) {
        this.shieldDashCoolDown = buf.readInt();
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeInt(this.shieldDashCoolDown);
    }

    public ShieldDashC2SPacket(int shieldDashCoolDown){
        this.shieldDashCoolDown = shieldDashCoolDown;
    }


    public static void handle(final ShieldDashC2SPacket data, final IPayloadContext context) {
//        ShieldDashMixinHelper.storeHitCoolDown(player.getId(),buf.getInt(0));
        ShieldDashMixinHelper.storeHitCoolDown(context.player().getId(),data.shieldDashCoolDown);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}