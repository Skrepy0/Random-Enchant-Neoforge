package com.random_enchant.render.custom_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.random_enchant.entity.custom.ThrownMace;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ThrownItemEntityRenderer extends EntityRenderer<ThrownMace> {
    private final ItemRenderer itemRenderer;

    public ThrownItemEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownMace thrownItem) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(ThrownMace entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        // 获取实体携带的物品
        ItemStack itemStack = entity.getDefaultItem().getDefaultInstance();
        if (itemStack.isEmpty()) {
            return;
        }

        poseStack.pushPose();

        // 应用旋转 - 使用正确的实体旋转值
        // 注意：这里需要使用实体当前的旋转值，而不是旧的（废弃的）字段
        float yRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        // 调整旋转以匹配投掷物品的自然方向
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(xRot - 135.0F));

        // 调整物品位置和大小
        poseStack.scale(0.8F, 0.8F, 0.8F);
        poseStack.translate(0.0D, -0.1D, 0.0D);

        // 渲染物品
        this.itemRenderer.renderStatic(
                itemStack,
                ItemDisplayContext.FIXED,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                entity.level(),
                entity.getId()
        );

        poseStack.popPose();

        // 可选：渲染轨迹效果
        renderTrail(entity, partialTicks, poseStack, buffer, packedLight);

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    /**
     * 渲染投掷物品的轨迹效果（可选）
     */
    private void renderTrail(ThrownMace entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // 这里可以添加轨迹粒子效果
        // 例如：根据速度渲染拖尾效果
        if (entity.getDeltaMovement().length() > 0.5F && entity.tickCount % 2 == 0) {
            // 可以在这里添加粒子效果，但需要在客户端事件中处理
            // 或者使用渲染粒子系统
        }
    }

    @Override
    public boolean shouldShowName(ThrownMace entity) {
        // 根据情况决定是否显示名称
        return super.shouldShowName(entity) &&
                (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }
}