/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.client.event;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.DelayedHitboxPower;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

public class DelayedHitboxEvent implements ClientTickEvents.EndWorldTick {
	public static Entity target = null;
	public static int ticks = 0;

	@Override
	public void onEndTick(ClientWorld world) {
		Entity entity = MinecraftClient.getInstance().targetedEntity;
		if (entity != null) {
			for (DelayedHitboxPower power : PowerHolderComponent.getPowers(entity, DelayedHitboxPower.class)) {
				target = entity;
				ticks = Math.max(ticks, power.getTicks());
			}
		}
		if (ticks > 0) {
			ticks--;
		}
		if (ticks == 0 || target == null || target.isRemoved() || !target.isAlive()) {
			target = null;
		}
	}
}
