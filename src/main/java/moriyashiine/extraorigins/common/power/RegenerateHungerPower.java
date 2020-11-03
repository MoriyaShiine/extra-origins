package moriyashiine.extraorigins.common.power;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import net.minecraft.entity.player.PlayerEntity;

public class RegenerateHungerPower extends Power {
	public final int amount;
	public final float chance;
	
	public RegenerateHungerPower(PowerType<?> type, PlayerEntity player, int amount, float chance) {
		super(type, player);
		this.amount = amount;
		this.chance = chance;
	}
}
