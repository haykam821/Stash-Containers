package io.github.haykam821.stashcontainers.component;

import java.util.UUID;

import dev.onyxstudios.cca.api.v3.component.Component;
import io.github.haykam821.stash.component.StashComponent;
import io.github.haykam821.stash.component.StashComponentInitializer;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;

public class StashContainerConnectionComponent implements Component {
	private final LootableContainerBlockEntity blockEntity;
	private UUID playerUuid;

	public StashContainerConnectionComponent(LootableContainerBlockEntity blockEntity) {
		this.blockEntity = blockEntity;
	}

	public void setPlayerUuid(UUID playerUuid) {
		this.playerUuid = playerUuid;
	}

	public void clearPlayerUuid() {
		this.playerUuid = null;
	}

	public StashComponent getStash() {
		return StashContainerConnectionComponent.getStash(this.playerUuid, this.blockEntity.getWorld().getServer());
	}

	public static StashComponent getStash(UUID uuid, MinecraftServer server) {
		return StashContainerConnectionComponent.getStash(uuid, server.getPlayerManager());
	}

	public static StashComponent getStash(UUID uuid, PlayerManager playerManager) {
		return StashContainerConnectionComponent.getStash(playerManager.getPlayer(uuid));
	}

	public static StashComponent getStash(PlayerEntity player) {
		if (player == null) return null;
		return StashComponentInitializer.STASH.get(player);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.playerUuid = nbt.getUuid("PlayerUUID");
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		if (this.playerUuid != null) {
			nbt.putUuid("PlayerUUID", this.playerUuid);
		}
	}
}