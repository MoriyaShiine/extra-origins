/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common.powertype;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import moriyashiine.extraorigins.common.init.ModPowerTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DelayedHitboxPowerType extends PowerType {
	public static final TypedDataObjectFactory<DelayedHitboxPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
			new SerializableData()
					.add("ticks", SerializableDataTypes.POSITIVE_INT),
			(data, condition) -> new DelayedHitboxPowerType(
					condition,
					data.get("ticks")
			),
			(powerType, serializableData) -> serializableData.instance()
					.set("ticks", powerType.ticks)
	);

	private final int ticks;

	public DelayedHitboxPowerType(Optional<EntityCondition> condition, int ticks) {
		super(condition);
		this.ticks = ticks;
	}

	@Override
	public @NotNull PowerConfiguration<?> getConfig() {
		return ModPowerTypes.DELAYED_HITBOX;
	}

	public int getTicks() {
		return ticks;
	}
}
