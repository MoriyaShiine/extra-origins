/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.powertype;

import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import moriyashiine.extraorigins.client.payload.NotifyRandomPowerChangePacket;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.component.entity.RandomPowerGranterComponent;
import moriyashiine.extraorigins.common.init.ModEntityComponents;
import moriyashiine.extraorigins.common.init.ModPowerTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class RandomPowerGranterPowerType extends PowerType {
	public static Identifier INITIAL_ID = ExtraOrigins.id("none");

	@Override
	public @NotNull PowerConfiguration<?> getConfig() {
		return ModPowerTypes.RANDOM_POWER_GRANTER;
	}

	@Override
	public void onGained() {
		init(getHolder(), false);
	}

	@Override
	public void onLost() {
		RandomPowerGranterComponent randomPowerGranterComponent = ModEntityComponents.RANDOM_POWER_GRANTER.get(getHolder());
		randomPowerGranterComponent.removePowers();
		randomPowerGranterComponent.sync();
	}

	public static void init(LivingEntity entity, boolean checkEnabled) {
		RandomPowerGranterComponent randomPowerGranterComponent = ModEntityComponents.RANDOM_POWER_GRANTER.get(entity);
		if (checkEnabled && !randomPowerGranterComponent.isEnabled()) {
			return;
		}
		randomPowerGranterComponent.initializePowers();
		randomPowerGranterComponent.sync();
		if (entity instanceof ServerPlayerEntity player) {
			for (int i = 0; i < randomPowerGranterComponent.getTemporaryPowers().length; i++) {
				NotifyRandomPowerChangePacket.send(player, i, INITIAL_ID, randomPowerGranterComponent.getTemporaryPowers()[i].getPower().getId());
			}
		}
	}
}
