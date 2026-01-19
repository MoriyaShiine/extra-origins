/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.mixin.powertype.preventblockslowness;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.type.PowerType;
import moriyashiine.extraorigins.common.powertype.PreventBlockSlownessPowerType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "slowMovement", at = @At("HEAD"), cancellable = true)
	private void extraorigins$preventBlockSlowness(BlockState state, Vec3d multiplier, CallbackInfo ci) {
		if (PowerHolderComponent.getPowerTypes((Entity) (Object) this, PreventBlockSlownessPowerType.class).stream().anyMatch(PowerType::isActive)) {
			ci.cancel();
		}
	}
}
