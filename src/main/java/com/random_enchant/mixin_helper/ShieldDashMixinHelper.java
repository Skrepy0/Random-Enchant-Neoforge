package com.random_enchant.mixin_helper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShieldDashMixinHelper {
    private static final Map<Integer, Integer> entityValueMap = new ConcurrentHashMap<>();
    private static final Map<Integer, Integer> HitCoolDownMap = new ConcurrentHashMap<>();
    private static volatile boolean isAttackKeyPressed = false;

    public static void storeEntityValue(int entityID, int value) { entityValueMap.put(entityID, value); }

    public static int getEntityValue(int entityID) { return entityValueMap.getOrDefault(entityID, 0); }

    public static void storeHitCoolDown(int entityID, int value) { HitCoolDownMap.put(entityID, value); }

    public static int getHitCoolDown(int entityID) { return HitCoolDownMap.getOrDefault(entityID, 0); }

    public static void removeEntity(int entityID) {
        entityValueMap.remove(entityID);
        HitCoolDownMap.remove(entityID);
    }

    public static void setIsAttackKeyPressed(boolean isAttackKeyPressed) {
        ShieldDashMixinHelper.isAttackKeyPressed = isAttackKeyPressed;
    }

    public static boolean isAttackKeyPressed() { return isAttackKeyPressed; }
}
