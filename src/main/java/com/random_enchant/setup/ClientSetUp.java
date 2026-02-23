package com.random_enchant.setup;

import com.random_enchant.RandomEnchant;
import com.random_enchant.entity.ModEntities;
import com.random_enchant.render.custom_entity.CustomWindChargeRenderer;
import com.random_enchant.render.custom_entity.ThrownMaceEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = RandomEnchant.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetUp {
    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.THROWN_ITEM.get(), ThrownMaceEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.CUSTOM_WIND_CHARGE.get(), CustomWindChargeRenderer::new);
        event.registerEntityRenderer(ModEntities.LIGHTNING_PROJECTILE.get(), ThrownItemRenderer::new);
    }
}
