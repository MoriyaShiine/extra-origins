/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin.mobneutrality;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MobNeutralityPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HoglinBrain;
import net.minecraft.entity.mob.HoglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(HoglinBrain.class)
public class HoglinBrainMixin {
	@ModifyReturnValue(method = "getNearestVisibleTargetablePlayer", at = @At("RETURN"))
	private static Optional<? extends LivingEntity> extraorigins$mobNeutrality(Optional<? extends LivingEntity> original, HoglinEntity hoglin) {
		if (original.isPresent()) {
			LivingEntity entity = original.get();
			for (MobNeutralityPower mobNeutralityPower : PowerHolderComponent.getPowers(entity, MobNeutralityPower.class)) {
				if (mobNeutralityPower.shouldBeNeutral(hoglin)) {
					return Optional.empty();
				}
			}
		}
		return original;
	}
}
