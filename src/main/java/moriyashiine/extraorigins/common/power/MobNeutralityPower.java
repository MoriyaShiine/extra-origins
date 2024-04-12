/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.Predicate;

public class MobNeutralityPower extends Power {
	private final Predicate<Entity> entityCondition;

	public MobNeutralityPower(PowerType<?> type, LivingEntity entity, Predicate<Entity> entityCondition) {
		super(type, entity);
		this.entityCondition = entityCondition;
	}

	public boolean shouldBeNeutral(Entity entity) {
		return entityCondition.test(entity);
	}
}
