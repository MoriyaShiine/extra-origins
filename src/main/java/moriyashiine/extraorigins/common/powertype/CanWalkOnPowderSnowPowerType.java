/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.powertype;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import moriyashiine.extraorigins.common.init.ModPowerTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CanWalkOnPowderSnowPowerType extends PowerType {
	public CanWalkOnPowderSnowPowerType(Optional<EntityCondition> condition) {
		super(condition);
	}

	@Override
	public @NotNull PowerConfiguration<?> getConfig() {
		return ModPowerTypes.CAN_WALK_ON_POWDER_SNOW;
	}
}
