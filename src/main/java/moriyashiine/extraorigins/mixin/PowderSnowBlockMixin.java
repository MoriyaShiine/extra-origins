/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.CanStandOnPowderSnowPower;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin {
	@Inject(method = "canWalkOnPowderSnow", at = @At("HEAD"), cancellable = true)
	private static void extraorigins$canWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		for (CanStandOnPowderSnowPower canStandOnPowderSnowPower : PowerHolderComponent.getPowers(entity, CanStandOnPowderSnowPower.class)) {
			if (canStandOnPowderSnowPower.isActive()) {
				cir.setReturnValue(true);
				return;
			}
		}
	}
}
