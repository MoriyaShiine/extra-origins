/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import moriyashiine.extraorigins.common.component.entity.MagicSporesComponent;
import moriyashiine.extraorigins.common.registry.ModComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class MagicSporesPower extends Power implements Active {
	private Key key;

	public MagicSporesPower(PowerType<?> type, LivingEntity entity) {
		super(type, entity);
	}

	@Override
	public void onUse() {
	}

	@Override
	public void onAdded() {
		update(false);
	}

	@Override
	public void onGained() {
		update(true);
	}

	@Override
	public void onLost() {
		update(true);
	}

	@Override
	public Key getKey() {
		return key;
	}

	@Override
	public void setKey(Key key) {
		this.key = key;
	}

	private void update(boolean setMobility) {
		ModComponents.MAGIC_SPORES.maybeGet(entity).ifPresent(magicSporesComponent -> {
			if (setMobility) {
				magicSporesComponent.setMode(MagicSporesComponent.Mode.MOBILITY);
			}
			if (entity instanceof ServerPlayerEntity player) {
				magicSporesComponent.updateAttributes(player);
			}
		});
	}
}
