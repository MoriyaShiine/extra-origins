package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public World world;
	
	@Inject(method = "getAir", at = @At("HEAD"), cancellable = true)
	private void getAir(CallbackInfoReturnable<Integer> callbackInfo) {
		if (EOPowers.INORGANIC.isActive(((Entity) (Object) this))) {
			callbackInfo.setReturnValue(0);
		}
	}
}
