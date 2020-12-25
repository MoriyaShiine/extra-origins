package moriyashiine.extraorigins.mixin;

import io.github.apace100.origins.component.OriginComponent;
import moriyashiine.extraorigins.common.power.RegenerateHungerPower;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
	@Shadow
	public abstract void add(int food, float f);
	
	@Inject(method = "update", at = @At("HEAD"))
	private void update(PlayerEntity player, CallbackInfo callbackInfo) {
		World world = player.world;
		if (OriginComponent.hasPower(player, RegenerateHungerPower.class)) {
			RegenerateHungerPower power = OriginComponent.getPowers(player, RegenerateHungerPower.class).get(0);
			if (world.random.nextFloat() < power.chance) {
				add(power.amount, 1);
			}
		}
	}
}
