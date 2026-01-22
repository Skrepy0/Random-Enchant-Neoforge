package com.random_enchant.network.packet.C2S;

import com.random_enchant.RandomEnchant;
import com.random_enchant.data.nbt.BrushNBTUtils;
import com.random_enchant.item.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class BrushStatusC2SPacket implements CustomPacketPayload {
    public static final Type<BrushStatusC2SPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RandomEnchant.MOD_ID, "brush_status_toggle"));

    public static final StreamCodec<FriendlyByteBuf, BrushStatusC2SPacket> STREAM_CODEC =
            StreamCodec.of((buf, packet) -> packet.write(buf), BrushStatusC2SPacket::new);
    private final boolean newStatus;

    public BrushStatusC2SPacket(boolean newStatus) { this.newStatus = newStatus; }

    public BrushStatusC2SPacket(FriendlyByteBuf buf) { this.newStatus = buf.readBoolean(); }

    public static void handle(BrushStatusC2SPacket packet, IPayloadContext context) {
        // 确保在服务器线程执行
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                // 检查玩家是否手持刷子
                ItemStack mainHand = player.getMainHandItem();
                ItemStack offHand = player.getOffhandItem();

                ItemStack brushStack = null;
                if (mainHand.getItem() == ModItems.ENCHANT_BRUSH.get()) {
                    brushStack = mainHand;
                } else if (offHand.getItem() == ModItems.ENCHANT_BRUSH.get()) {
                    brushStack = offHand;
                }

                if (brushStack != null) {
                    // 更新刷子状态
                    BrushNBTUtils.setStatus(packet.newStatus, brushStack);

                    // 可选：发送消息给玩家
                    // player.sendSystemMessage(Component.literal("刷子状态已切换: " + packet.newStatus));

                    // 同步给其他客户端（如果需要）
                    // player.containerMenu.broadcastChanges();
                }
            }
        });
    }

    public void write(FriendlyByteBuf buf) { buf.writeBoolean(this.newStatus); }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
