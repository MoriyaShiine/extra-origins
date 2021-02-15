package moriyashiine.extraorigins.common.power;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import moriyashiine.extraorigins.common.interfaces.BabyAccessor;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.api.ScaleType;

public class ModifySizePower extends Power {
	private static final ScaleType[] BITE_SIZED_TYPES = {ScaleType.WIDTH, ScaleType.HEIGHT, ScaleType.DROPS};
	
	public final float scale;
	
	public ModifySizePower(PowerType<?> type, PlayerEntity player, float scale) {
		super(type, player);
		this.scale = scale;
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		((BabyAccessor) player).setBaby(scale <= 0.5);
		for (ScaleType type : BITE_SIZED_TYPES) {
			type.getScaleData(player).setScale(type.getScaleData(player).getScale() / 4f);
		}
	}
	
	@Override
	public void onRemoved() {
		super.onRemoved();
		((BabyAccessor) player).setBaby(false);
		for (ScaleType type : BITE_SIZED_TYPES) {
			type.getScaleData(player).setScale(type.getScaleData(player).getScale() * 4f);
		}
	}
}
