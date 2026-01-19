/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.powertype;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.ValueModifyingPowerType;
import io.github.apace100.apoli.util.modifier.Modifier;
import moriyashiine.extraorigins.common.init.ModPowerTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ModifyHostileDetectionRangePowerType extends ValueModifyingPowerType {
	public ModifyHostileDetectionRangePowerType(List<Modifier> modifiers, Optional<EntityCondition> condition) {
		super(modifiers, condition);
	}

	@Override
	public @NotNull PowerConfiguration<?> getConfig() {
		return ModPowerTypes.MODIFY_HOSTILE_DETECTION_RANGE;
	}
}
