/*
 * All Rights Reserved (c) 2021 MoriyaShiine
 */

/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MobNeutralityPower extends Power {
	public final Set<EntityType<?>> entityTypes = new HashSet<>();
	
	public MobNeutralityPower(PowerType<?> type, LivingEntity entity, List<EntityType<?>> entityTypes) {
		super(type, entity);
		this.entityTypes.addAll(entityTypes);
	}
}
