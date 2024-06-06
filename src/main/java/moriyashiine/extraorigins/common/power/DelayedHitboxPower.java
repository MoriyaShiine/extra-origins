/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;

public class DelayedHitboxPower extends Power {
	private final int ticks;

	public DelayedHitboxPower(PowerType<?> type, LivingEntity entity, int ticks) {
		super(type, entity);
		this.ticks = ticks;
	}

	public int getTicks() {
		return ticks;
	}
}
