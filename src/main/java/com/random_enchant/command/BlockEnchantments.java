package com.random_enchant.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.random_enchant.enchantment.ModEnchantHelper;
import com.random_enchant.enchantment.enchantmentblock.BlockEnchantmentStorage;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class BlockEnchantments {
    /**
     * 注册方块附魔命令
     */
    public static void register(net.neoforged.neoforge.event.RegisterCommandsEvent event) {
        CommandBuildContext contexts = event.getBuildContext();
        event.getDispatcher().register(
                Commands.literal("block_enchant")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("get").then(
                                Commands.argument("block", BlockPosArgument.blockPos()).executes(context -> {
                                    BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "block");
                                    return getBlockEnchantmentAtPos(context, pos.getX(), pos.getY(), pos.getZ());
                                })))
                        .then(Commands.literal("add").then(
                                Commands.argument("block", BlockPosArgument.blockPos())
                                        .then(Commands.argument("enchantment",
                                                                ResourceArgument.resource(contexts,
                                                                                          Registries.ENCHANTMENT))
                                                      .then(Commands.argument("level",
                                                                              IntegerArgumentType.integer(1, 255))
                                                                    .executes(context -> {
                                                                        BlockPos pos =
                                                                                BlockPosArgument.getLoadedBlockPos(
                                                                                        context, "block");
                                                                        Holder<Enchantment> enchantmentHolder =
                                                                                ResourceArgument.getResource(
                                                                                        context, "enchantment",
                                                                                        Registries.ENCHANTMENT);
                                                                        ResourceLocation enchantmentId =
                                                                                enchantmentHolder.unwrapKey()
                                                                                        .get()
                                                                                        .location();
                                                                        return addBlockEnchantmentAtPos(
                                                                                context, pos.getX(), pos.getY(),
                                                                                pos.getZ(), enchantmentId,
                                                                                IntegerArgumentType.getInteger(
                                                                                        context, "level"));
                                                                    })))))
                        .then(Commands.literal("remove").then(
                                Commands.argument("block", BlockPosArgument.blockPos()).executes(context -> {
                                    BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "block");
                                    return removeBlockEnchantmentAtPos(context, pos.getX(), pos.getY(), pos.getZ());
                                }))));
    }

    /**
     * 获取指定位置的方块附魔
     */
    private static int getBlockEnchantmentAtPos(CommandContext<CommandSourceStack> context, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        ListTag listTag = BlockEnchantmentStorage.getEnchantmentsAtPosition(pos);
        if (!listTag.isEmpty()) {
            context.getSource().sendSuccess(
                    () -> Component.translatable("command.random_enchant.block_enchant.enchant_tag"), false);
            for (int i = 0; i < listTag.size(); i++) {
                Tag tag = listTag.get(i);
                Component message = Component.literal("§a" + tag.toString());
                context.getSource().sendSuccess(() -> message, false);
            }
            return 1;
        }
        Component message = Component.translatable("command.random_enchant.block_enchant.has_no_enchantment");
        context.getSource().sendSuccess(() -> message, false);
        return 0;
    }

    /**
     * 在指定位置设置方块附魔
     */
    private static int addBlockEnchantmentAtPos(CommandContext<CommandSourceStack> context, int x, int y, int z,
                                                net.minecraft.resources.ResourceLocation enchantmentId, int level) {
        BlockPos pos = new BlockPos(x, y, z);

        // 创建附魔列表
        net.minecraft.nbt.ListTag enchantments = new net.minecraft.nbt.ListTag();
        net.minecraft.nbt.CompoundTag enchantmentTag = new net.minecraft.nbt.CompoundTag();
        enchantmentTag.putString("id", enchantmentId.toString());
        enchantmentTag.putInt("lvl", level);
        enchantments.add(enchantmentTag);

        ListTag oldEnchantments = BlockEnchantmentStorage.getEnchantmentsAtPosition(pos);
        BlockEnchantmentStorage.removeBlockEnchantment(pos);
        // 合并附魔列表
        ListTag newEnchantments = mergeNbtLists(oldEnchantments, enchantments);
        // 储存信息
        BlockEnchantmentStorage.addBlockEnchantment(pos, newEnchantments);

        Component message = Component.translatable("command.random_enchant.block_enchant.add_1")
                                    .append(pos.toString())
                                    .append(Component.translatable("command.random_enchant.block_enchant.add_2"))
                                    .append("§d" + enchantmentId.toString())
                                    .append(" §alvl: §g" + ModEnchantHelper.toRoman(level));
        context.getSource().sendSuccess(() -> message, false);

        return 1;
    }

    private static ListTag mergeNbtLists(ListTag list1, ListTag list2) {
        ListTag mergedList = new ListTag();
        mergedList.addAll(list1);
        mergedList.addAll(list2);
        return mergedList;
    }

    /**
     * 移除指定位置的方块附魔
     */
    private static int removeBlockEnchantmentAtPos(CommandContext<CommandSourceStack> context, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);

        // 检查是否有附魔
        boolean hasEnchant = !BlockEnchantmentStorage.getEnchantmentsAtPosition(pos).isEmpty();

        if (!hasEnchant) {
            Component message = Component.translatable("command.random_enchant.block_enchant.has_no_enchantment");
            context.getSource().sendSuccess(() -> message, false);
            return 0;
        }

        // 移除附魔
        BlockEnchantmentStorage.removeBlockEnchantment(pos);

        Component message = Component.translatable("command.random_enchant.block_enchant.remove");
        context.getSource().sendSuccess(() -> message, false);

        return 1;
    }
}
