package com.random_enchant.event;

import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

public class ModEvents {
    public static void registerAttackEntityEvent(AttackEntityEvent event) {
        RandomEnchantEvent.onPlayerAttack(event);
    }
}
