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
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class FoodEffectImmunityPowerType extends PowerType {
	public static final TypedDataObjectFactory<FoodEffectImmunityPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
			new SerializableData()
					.add("effects", SerializableDataTypes.STATUS_EFFECT_ENTRIES, List.of())
					.add("inverted", SerializableDataTypes.BOOLEAN, false),
			(data, condition) -> new FoodEffectImmunityPowerType(
					condition,
					data.get("effects"),
					data.get("inverted")
			),
			(powerType, serializableData) -> serializableData.instance()
					.set("effects", powerType.effects)
					.set("inverted", powerType.inverted)
	);

	private final List<RegistryEntry<StatusEffect>> effects;
	private final boolean inverted;

	public FoodEffectImmunityPowerType(Optional<EntityCondition> condition, List<RegistryEntry<StatusEffect>> effects, boolean inverted) {
		super(condition);
		this.effects = effects;
		this.inverted = inverted;
	}

	@Override
	public @NotNull PowerConfiguration<?> getConfig() {
		return ModPowerTypes.FOOD_EFFECT_IMMUNITY;
	}

	public boolean shouldRemove(RegistryEntry<StatusEffect> effect) {
		if (inverted) {
			return !effects.contains(effect);
		}
		return effects.contains(effect);
	}
}
