/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common.condition.type.entity;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.EntityConditionContext;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import moriyashiine.extraorigins.common.init.ModConditionTypes;
import org.jetbrains.annotations.NotNull;

public class PiglinSafeEntityConditionType extends EntityConditionType {
	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return ModConditionTypes.PIGLIN_SAFE;
	}

	@Override
	public boolean test(EntityConditionContext context) {
		return context.world().getDimension().piglinSafe();
	}
}
