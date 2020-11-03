package io.github.haykam821.stashcontainers.component;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import io.github.haykam821.stashcontainers.Main;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.Identifier;

public class StashContainersComponentInitializer implements BlockComponentInitializer {
	private static final Identifier STASH_CONTAINER_CONNECTION_ID = new Identifier(Main.MOD_ID, "stash_container_connection");
	public static final ComponentKey<StashContainerConnectionComponent> STASH_CONTAINER_CONNECTION = ComponentRegistryV3.INSTANCE.getOrCreate(STASH_CONTAINER_CONNECTION_ID, StashContainerConnectionComponent.class);

	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
		registry.registerFor(LootableContainerBlockEntity.class, STASH_CONTAINER_CONNECTION, StashContainerConnectionComponent::new);
	}
}