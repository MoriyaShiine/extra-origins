/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.event;

import moriyashiine.extraorigins.common.power.RandomPowerGranterPower;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class RandomPowerGranterEvent implements ServerPlayerEvents.AfterRespawn {
	@Override
	public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
		if (!alive) {
			RandomPowerGranterPower.init(newPlayer, true);
		}
	}
}
