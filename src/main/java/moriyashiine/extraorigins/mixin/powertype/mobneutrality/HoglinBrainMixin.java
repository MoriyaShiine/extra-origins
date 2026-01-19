/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.mixin.powertype.mobneutrality;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.type.MobNeutralityPowerType;
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
		if (original.isPresent() && PowerHolderComponent.getPowerTypes(original.get(), MobNeutralityPowerType.class).stream().anyMatch(powerType -> powerType.isActive() && powerType.shouldBeNeutral(hoglin))) {
			return Optional.empty();
		}
		return original;
	}
}
