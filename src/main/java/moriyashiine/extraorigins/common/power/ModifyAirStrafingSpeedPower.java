/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.ValueModifyingPower;
import net.minecraft.entity.LivingEntity;

public class ModifyAirStrafingSpeedPower extends ValueModifyingPower {
	public ModifyAirStrafingSpeedPower(PowerType<?> type, LivingEntity entity) {
		super(type, entity);
	}
}
