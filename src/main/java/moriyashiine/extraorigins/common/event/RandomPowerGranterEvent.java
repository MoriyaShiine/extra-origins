/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common.event;

import moriyashiine.extraorigins.common.powertype.RandomPowerGranterPowerType;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class RandomPowerGranterEvent implements ServerPlayerEvents.AfterRespawn {
	@Override
	public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
		if (!alive) {
			RandomPowerGranterPowerType.init(newPlayer, true);
		}
	}
}
