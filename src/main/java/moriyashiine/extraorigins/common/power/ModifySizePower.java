package moriyashiine.extraorigins.common.power;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import moriyashiine.extraorigins.common.registry.EOScaleTypes;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.api.ScaleData;

public class ModifySizePower extends Power {
	public final float scale;
	
	public ModifySizePower(PowerType<?> type, PlayerEntity player, float scale) {
		super(type, player);
		setTicking(true);
		this.scale = scale;
	}
	
	@Override
	public void tick() {
		super.tick();
		ScaleData data = EOScaleTypes.MODIFY_SIZE_TYPE.getScaleData(player);
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
		EOScaleTypes.MODIFY_SIZE_TYPE.getScaleData(player).setScale(1);
	}
}
