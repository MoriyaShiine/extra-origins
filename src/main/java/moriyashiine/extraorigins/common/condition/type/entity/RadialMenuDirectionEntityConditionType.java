/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common.condition.type.entity;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.EntityConditionContext;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerReference;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import moriyashiine.extraorigins.common.init.ModConditionTypes;
import moriyashiine.extraorigins.common.powertype.RadialMenuPowerType;
import moriyashiine.extraorigins.common.util.RadialMenuDirection;
import org.jetbrains.annotations.NotNull;

public class RadialMenuDirectionEntityConditionType extends EntityConditionType {
	public static final TypedDataObjectFactory<RadialMenuDirectionEntityConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
			new SerializableData()
					.add("power", ApoliDataTypes.POWER_REFERENCE)
					.add("direction", SerializableDataType.enumValue(RadialMenuDirection.class)),
			data -> new RadialMenuDirectionEntityConditionType(
					data.get("power"),
					data.get("direction")),
			(conditionType, serializableData) -> serializableData.instance()
					.set("power", conditionType.power)
					.set("direction", conditionType.direction)
	);

	private final PowerReference power;
	private final RadialMenuDirection direction;

	public RadialMenuDirectionEntityConditionType(PowerReference power, RadialMenuDirection direction) {
		this.power = power;
		this.direction = direction;
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return ModConditionTypes.RADIAL_MENU_DIRECTION;
	}

	@Override
	public boolean test(EntityConditionContext context) {
		return power.getNullablePowerType(context.entity()) instanceof RadialMenuPowerType radialMenuPowerType && radialMenuPowerType.getDirection() == direction;
	}
}
