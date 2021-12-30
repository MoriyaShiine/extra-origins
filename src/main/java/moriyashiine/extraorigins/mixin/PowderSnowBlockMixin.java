/*
 * All Rights Reserved (c) 2021 MoriyaShiine
 */

/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.ModPowers;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin {
	@Inject(method = "canWalkOnPowderSnow", at = @At("RETURN"), cancellable = true)
	private static void extraorigins$allowNimble(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && ModPowers.NIMBLE.isActive(entity)) {
			cir.setReturnValue(true);
		}
	}
}
