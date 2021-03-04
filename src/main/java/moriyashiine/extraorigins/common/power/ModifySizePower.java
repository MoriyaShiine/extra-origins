package moriyashiine.extraorigins.common.power;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;

public class ModifySizePower extends Power {
	private static final ScaleType[] MODIFY_SIZE_TYPES = {ScaleType.WIDTH, ScaleType.HEIGHT, ScaleType.DROPS};
	
	public final float scale;
	
	public ModifySizePower(PowerType<?> type, PlayerEntity player, float scale) {
		super(type, player);
		this.scale = scale;
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		for (ScaleType type : MODIFY_SIZE_TYPES) {
			ScaleData data = type.getScaleData(player);
			data.setScale(data.getBaseScale() * scale);
		}
	}
	
	@Override
	public void onRemoved() {
		super.onRemoved();
		for (ScaleType type : MODIFY_SIZE_TYPES) {
			ScaleData data = type.getScaleData(player);
			data.setScale(data.getBaseScale() / scale);
		}
	}
}
