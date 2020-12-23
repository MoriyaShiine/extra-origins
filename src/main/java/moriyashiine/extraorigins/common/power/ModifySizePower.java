package moriyashiine.extraorigins.common.power;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import moriyashiine.extraorigins.common.interfaces.BabyAccessor;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.api.ScaleData;
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
		BabyAccessor.get(player).ifPresent(babyAccessor -> babyAccessor.setBaby(scale <= 0.5));
		for (ScaleType type : BITE_SIZED_TYPES) {
			ScaleData data = ScaleData.of(player, type);
			data.setScale(data.getScale() * scale);
			data.setTargetScale(data.getTargetScale() * scale);
			data.markForSync();
		}
	}
	
	@Override
	public void onRemoved() {
		super.onRemoved();
		BabyAccessor.get(player).ifPresent(babyAccessor -> babyAccessor.setBaby(false));
		for (ScaleType type : BITE_SIZED_TYPES) {
			ScaleData data = ScaleData.of(player, type);
			data.setScale(data.getScale() / scale);
			data.setTargetScale(data.getTargetScale() / scale);
			data.markForSync();
		}
	}
}
