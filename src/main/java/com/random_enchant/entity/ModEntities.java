package com.random_enchant.entity;

import com.random_enchant.RandomEnchant;
import com.random_enchant.entity.custom.CustomWindChargeEntity;
import com.random_enchant.entity.custom.LightningProjectileEntity;
import com.random_enchant.entity.custom.ThrownMace;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, RandomEnchant.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<CustomWindChargeEntity>> CUSTOM_WIND_CHARGE =
            ENTITY_TYPES.register(
                    "custom_wind_charge",
                    ()
                            -> EntityType.Builder
                                       .<CustomWindChargeEntity>of(CustomWindChargeEntity::new, MobCategory.MISC)
                                       .sized(0.3125F, 0.3125F)
                                       .eyeHeight(0.0F)
                                       .build("custom_wind_charge"));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownMace>> THROWN_ITEM =
            ENTITY_TYPES.register(
                    "thrown_item_entity",
                    ()
                            -> EntityType.Builder
                            .<ThrownMace>of(ThrownMace::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .eyeHeight(0.0F)
                            .build("thrown_item_entity"));
    public static final DeferredHolder<EntityType<?>, EntityType<LightningProjectileEntity>> LIGHTNING_PROJECTILE =
            ENTITY_TYPES.register(
                    "lightning_projectile",
                    ()
                            -> EntityType.Builder
                                       .<LightningProjectileEntity>of(LightningProjectileEntity::new, MobCategory.MISC)
                                       .sized(0.25f, 0.25f)
                                       .build("lightning_projectile"));

    public static void register(IEventBus eventBus) { ENTITY_TYPES.register(eventBus); }
}
