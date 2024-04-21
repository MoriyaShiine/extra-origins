/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import moriyashiine.extraorigins.common.component.entity.RandomPowerGranterComponent;
import moriyashiine.extraorigins.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;

public class RandomPowerGranterPower extends Power {
	public RandomPowerGranterPower(PowerType<?> type, LivingEntity entity) {
		super(type, entity);
	}

	@Override
	public void onGained() {
		init(entity, false);
	}

	@Override
	public void onLost() {
		RandomPowerGranterComponent randomPowerGranterComponent = ModEntityComponents.RANDOM_POWER_GRANTER.get(entity);
		randomPowerGranterComponent.removePowers();
		randomPowerGranterComponent.sync();
	}

	public static void init(LivingEntity entity, boolean checkEnabled) {
		RandomPowerGranterComponent randomPowerGranterComponent = ModEntityComponents.RANDOM_POWER_GRANTER.get(entity);
		if (checkEnabled && !randomPowerGranterComponent.isEnabled()) {
			return;
		}
		randomPowerGranterComponent.initializePowers();
		randomPowerGranterComponent.sync();
	}
}
