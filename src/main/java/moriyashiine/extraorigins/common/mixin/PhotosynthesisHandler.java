package moriyashiine.extraorigins.common.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class PhotosynthesisHandler
{
	@Shadow
	public abstract void add(int food, float f);
	
	@Inject(method = "update", at = @At("HEAD"))
	private void update(PlayerEntity player, CallbackInfo callbackInfo)
	{
		World world = player.world;
		if (EOPowers.PHOTOSYNTHESIS.isActive(player) && world.isDay() && world.random.nextFloat() < 1/80f && world.isSkyVisible(player.getBlockPos()))
		{
			add(1, 1);
		}
	}
}