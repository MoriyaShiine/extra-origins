package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
	@Shadow
	public abstract void add(int food, float f);
	
	@Shadow
	public abstract void addExhaustion(float exhaustion);
	
	@Shadow
	private int foodLevel;
	
	@Inject(method = "update", at = @At("HEAD"), cancellable = true)
	private void update(PlayerEntity player, CallbackInfo callbackInfo) {
		if (EOPowers.PHOTOSYNTHESIS.isActive(player)) {
			if (player.age % 20 == 0 && player.world.isDay() && player.world.isSkyVisible(player.getBlockPos())) {
				add(1, 1);
			}
			if (foodLevel > 0 && player.age % 30 == 0 && player.getHealth() < player.getMaxHealth()) {
				player.heal(1);
				addExhaustion(4);
			}
		}
	}
}
