/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;

public class PreventBlockSlownessPower extends Power {
	public PreventBlockSlownessPower(PowerType<?> type, LivingEntity entity) {
		super(type, entity);
	}
}
