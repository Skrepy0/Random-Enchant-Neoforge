package com.random_enchant.command;

import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class ModCommands {
    public static void registerModCommands(RegisterCommandsEvent event) {
        RandomEnchantCommand.register(event);
        BlockEnchantments.register(event);
    }
}
