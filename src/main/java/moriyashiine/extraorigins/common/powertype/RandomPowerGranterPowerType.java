/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common.powertype;

import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import moriyashiine.extraorigins.common.component.entity.RandomPowerGranterComponent;
import moriyashiine.extraorigins.common.init.ModEntityComponents;
import moriyashiine.extraorigins.common.init.ModPowerTypes;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class RandomPowerGranterPowerType extends PowerType {
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
	}
}
