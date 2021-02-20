package moriyashiine.extraorigins.common.power;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import moriyashiine.extraorigins.common.interfaces.BabyAccessor;
import net.minecraft.entity.player.PlayerEntity;

public class LargeHeadPower extends Power {
	public LargeHeadPower(PowerType<?> type, PlayerEntity player) {
		super(type, player);
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		((BabyAccessor) player).setBaby(true);
	}
	
	@Override
	public void onRemoved() {
		super.onRemoved();
		((BabyAccessor) player).setBaby(false);
	}
}
