package com.random_enchant.network.packet.S2C;

import com.random_enchant.RandomEnchant;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UpdateProjectileVelocityPacket implements CustomPacketPayload {
    // stream codec
    public static final StreamCodec<FriendlyByteBuf, UpdateProjectileVelocityPacket> STREAM_CODEC =
            CustomPacketPayload.codec(UpdateProjectileVelocityPacket::write, UpdateProjectileVelocityPacket::new);
    public static Type<UpdateProjectileVelocityPacket> TYPE = new Type<UpdateProjectileVelocityPacket>(
            ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "update_projectile_velocity_s2c"));
    public Vec3 finalVelocity;
    public int id;

    public UpdateProjectileVelocityPacket(int id, Vec3 finalVelocity) {
        this.id = id;
        this.finalVelocity = finalVelocity;
    }

    public UpdateProjectileVelocityPacket(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.finalVelocity = buf.readVec3();
    }

    public static void handle(UpdateProjectileVelocityPacket data, IPayloadContext context) {
        runEnqueue(data, context);
    }

    private static void runEnqueue(UpdateProjectileVelocityPacket data, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (data.id != -1 && Minecraft.getInstance().level != null) {
                Entity entity = Minecraft.getInstance().level.getEntity(data.id);
                if (entity instanceof Projectile) {
                    entity.setDeltaMovement(data.finalVelocity);
                }
            }
        });
    }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(this.id);
        pBuffer.writeVec3(this.finalVelocity);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
