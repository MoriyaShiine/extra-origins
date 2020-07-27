package moriyashiine.extraorigins.common.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class SmallAppetiteHandler extends LivingEntity
{
	protected SmallAppetiteHandler(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
	private void addExhaustion(float exhaustion, CallbackInfo callbackInfo)
	{
		if (EOPowers.BITE_SIZED.isActive(this) && random.nextBoolean())
		{
			callbackInfo.cancel();
		}
	}
}