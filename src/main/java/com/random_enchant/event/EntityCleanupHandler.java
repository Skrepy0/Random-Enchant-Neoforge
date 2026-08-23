package com.random_enchant.event;

import com.random_enchant.RandomEnchant;
import com.random_enchant.mixin_helper.BowDashMixinHelper;
import com.random_enchant.mixin_helper.ElytraJumpMixinHelper;
import com.random_enchant.mixin_helper.ShieldDashMixinHelper;
import com.random_enchant.network.packet.S2C.CustomWindChargeS2CPacket;
import com.random_enchant.network.packet.S2C.EntityVelocityUpdateS2CPacket;
import com.random_enchant.network.packet.S2C.OneWithShadowS2CPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

/**
 * 实体清理处理器，防止MixinHelper中的静态Map内存泄露。
 * 当实体死亡或离开世界时清理对应的缓存数据。
 */
@EventBusSubscriber(modid = RandomEnchant.MOD_ID)
public class EntityCleanupHandler {

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        cleanupEntityCache(entity);
    }

    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
        Entity entity = event.getEntity();
        cleanupEntityCache(entity);
    }

    private static void cleanupEntityCache(Entity entity) {
        int entityId = entity.getId();

        // 清理MixinHelper中的缓存
        ShieldDashMixinHelper.removeEntity(entityId);
        BowDashMixinHelper.removeEntity(entityId);
        ElytraJumpMixinHelper.removeEntity(entityId);

        // 清理S2C包中的缓存
        OneWithShadowS2CPacket.removeId(entityId);
        EntityVelocityUpdateS2CPacket.WindChargeStormData.remove(entityId);
        CustomWindChargeS2CPacket.CustomWindChargeData.remove(entity.getUUID());
    }
}
