/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.powertype;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import moriyashiine.extraorigins.common.init.ModPowerTypes;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MobNeutralityPowerType extends PowerType {
	public static final TypedDataObjectFactory<MobNeutralityPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
			new SerializableData()
					.add("target_condition", EntityCondition.DATA_TYPE.optional(), Optional.empty()),
			(data, condition) -> new MobNeutralityPowerType(
					condition,
					data.get("target_condition")
			),
			(powerType, serializableData) -> serializableData.instance()
					.set("target_condition", powerType.targetCondition)
	);

	private final Optional<EntityCondition> targetCondition;

	public MobNeutralityPowerType(Optional<EntityCondition> condition, Optional<EntityCondition> targetCondition) {
		super(condition);
		this.targetCondition = targetCondition;
	}

	@Override
	public @NotNull PowerConfiguration<?> getConfig() {
		return ModPowerTypes.MOB_NEUTRALITY;
	}

	public boolean shouldBeNeutral(Entity entity) {
		return targetCondition.map(entityCondition -> entityCondition.test(entity)).orElse(true);
	}
}
