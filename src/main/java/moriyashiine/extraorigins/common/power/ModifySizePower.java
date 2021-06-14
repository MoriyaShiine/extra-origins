package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import moriyashiine.extraorigins.common.registry.EOScaleTypes;
import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.api.ScaleData;

public class ModifySizePower extends Power {
	public final float scale;
	
	public ModifySizePower(PowerType<?> type, LivingEntity entity, float scale) {
		super(type, entity);
		setTicking(true);
		this.scale = scale;
	}
	
	@Override
	public void tick() {
		super.tick();
		ScaleData data = EOScaleTypes.MODIFY_SIZE_TYPE.getScaleData(entity);
		if (isActive() && data.getScale() != scale) {
			data.setScale(scale);
		}
		else if (!isActive() && data.getScale() != 1) {
			data.setScale(1);
		}
	}
	
	@Override
	public void onLost() {
		super.onLost();
		EOScaleTypes.MODIFY_SIZE_TYPE.getScaleData(entity).setScale(1);
	}
}
