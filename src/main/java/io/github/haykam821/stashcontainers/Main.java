package io.github.haykam821.stashcontainers;

import io.github.haykam821.stashcontainers.command.StashContainerCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class Main implements ModInitializer {
	public static final String MOD_ID = "stashcontainers";

	@Override
	public void onInitialize() {
		// Commands
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			StashContainerCommand.register(dispatcher);
		});
	}
}