package io.github.haykam821.stashcontainers.command;

import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import io.github.haykam821.stashcontainers.component.StashContainerConnectionComponent;
import io.github.haykam821.stashcontainers.component.StashContainersComponentInitializer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class StashContainerCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("stashcontainer").requires(context -> {
			return context.hasPermissionLevel(2);
		});

		// Connect
		LiteralArgumentBuilder<ServerCommandSource> connectBuilder = CommandManager.literal("connect");
		connectBuilder.then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
			.then(CommandManager.argument("player", EntityArgumentType.player()).executes(context -> {
				return StashContainerCommand.connect(context, EntityArgumentType.getPlayer(context, "player").getUuid());
			}))
			.then(CommandManager.argument("uuid", UuidArgumentType.uuid()).executes(context -> {
				return StashContainerCommand.connect(context, UuidArgumentType.getUuid(context, "uuid"));
			})));
		builder.then(connectBuilder);

		// Disconnect
		LiteralArgumentBuilder<ServerCommandSource> disconnectBuilder = CommandManager.literal("disconnect");
		disconnectBuilder
			.then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
			.executes(StashContainerCommand::disconnect));
		builder.then(disconnectBuilder);
		
		dispatcher.register(builder);
	}

	private static int connect(CommandContext<ServerCommandSource> context, UUID uuid) throws CommandSyntaxException {
		BlockPos pos = BlockPosArgumentType.getLoadedBlockPos(context, "pos");

		BlockEntity blockEntity = context.getSource().getWorld().getBlockEntity(pos);
		if (!(blockEntity instanceof LootableContainerBlockEntity)) {
			context.getSource().sendError(new TranslatableText("commands.stashcontainers.stashcontainer.connect.failure"));
			return 0;
		}

		StashContainerConnectionComponent connection = StashContainersComponentInitializer.STASH_CONTAINER_CONNECTION.get((LootableContainerBlockEntity) blockEntity);
		connection.setPlayerUuid(uuid);

		context.getSource().sendFeedback(new TranslatableText("commands.stashcontainers.stashcontainer.connect.success", uuid), false);
		return 1;
	}

	private static int disconnect(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		BlockPos pos = BlockPosArgumentType.getLoadedBlockPos(context, "pos");

		BlockEntity blockEntity = context.getSource().getWorld().getBlockEntity(pos);
		if (!(blockEntity instanceof LootableContainerBlockEntity)) {
			context.getSource().sendError(new TranslatableText("commands.stashcontainers.stashcontainer.disconnect.failure"));
			return 0;
		}

		StashContainerConnectionComponent connection = StashContainersComponentInitializer.STASH_CONTAINER_CONNECTION.get((LootableContainerBlockEntity) blockEntity);
		connection.clearPlayerUuid();

		context.getSource().sendFeedback(new TranslatableText("commands.stashcontainers.stashcontainer.disconnect.success"), false);
		return 1;
	}
}