package io.github.haykam821.stashcontainers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.haykam821.stash.component.StashComponent;
import io.github.haykam821.stashcontainers.component.StashContainerConnectionComponent;
import io.github.haykam821.stashcontainers.component.StashContainersComponentInitializer;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.ItemStack;

@Mixin(LootableContainerBlockEntity.class)
public class LootableContainerBlockEntityMixin {
	@Inject(method = "setStack", at = @At("TAIL"))
	private void insertIntoStash(CallbackInfo ci) {
		LootableContainerBlockEntity blockEntity = (LootableContainerBlockEntity) (Object) this;
		StashContainerConnectionComponent connection = StashContainersComponentInitializer.STASH_CONTAINER_CONNECTION.get(blockEntity);

		StashComponent stash = connection.getStash();
		if (stash != null) {
			for (int slot = 0; slot < blockEntity.size(); slot++) {
				ItemStack stack = blockEntity.getStack(slot);
				if (StashComponent.isInsertable(stack)) {
					stash.insertStack(stack.copy());
					blockEntity.removeStack(slot);
				}
			}
		}
	}
}