package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Inject(method = "slowMovement", at = @At("HEAD"), cancellable = true)
	private void slowMovement(BlockState state, Vec3d multiplier, CallbackInfo ci) {
		if (EOPowers.NIMBLE.isActive((Entity) (Object) this)) {
			ci.cancel();
		}
	}
}
