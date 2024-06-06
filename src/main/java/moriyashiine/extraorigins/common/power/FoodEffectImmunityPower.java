/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FoodEffectImmunityPower extends Power {
	private final Set<StatusEffect> effects = new HashSet<>();
	private final boolean inverted;

	public FoodEffectImmunityPower(PowerType<?> type, LivingEntity entity, List<StatusEffect> effects, boolean inverted) {
		super(type, entity);
		this.effects.addAll(effects);
		this.inverted = inverted;
	}

	public boolean shouldRemove(StatusEffect effect) {
		if (inverted) {
			return !effects.contains(effect);
		}
		return effects.contains(effect);
	}
}
