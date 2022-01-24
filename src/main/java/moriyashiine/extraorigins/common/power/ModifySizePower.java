/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModifySizePower extends Power {
	private final Set<ScaleType> scaleTypes = new HashSet<>();
	public final float scale;

	public ModifySizePower(PowerType<?> type, LivingEntity entity, List<Identifier> identifiers, float scale) {
		super(type, entity);
		identifiers.forEach(identifier -> scaleTypes.add(ScaleRegistries.SCALE_TYPES.get(identifier)));
		this.scale = scale;
		setTicking(true);
	}

	@Override
	public void tick() {
		scaleTypes.forEach(scaleType -> {
			ScaleData data = scaleType.getScaleData(entity);
			if (isActive() && data.getScale() != scale) {
				data.setScale(scale);
			} else if (!isActive() && data.getScale() != 1) {
				data.setScale(1);
			}
		});
	}

	@Override
	public void onLost() {
		scaleTypes.forEach(scaleType -> scaleType.getScaleData(entity).setScale(1));
	}
}
