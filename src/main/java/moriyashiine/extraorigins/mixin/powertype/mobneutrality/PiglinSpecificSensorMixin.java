/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.mixin.powertype.mobneutrality;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.powertype.MobNeutralityPowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.PiglinSpecificSensor;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PiglinSpecificSensor.class)
public class PiglinSpecificSensorMixin {
	@WrapOperation(method = "sense", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PiglinBrain;wearsGoldArmor(Lnet/minecraft/entity/LivingEntity;)Z"))
	private boolean extraorigins$mobNeutrality(LivingEntity player, Operation<Boolean> original, ServerWorld world, LivingEntity entity) {
		return original.call(player) || PowerHolderComponent.getPowerTypes(player, MobNeutralityPowerType.class).stream().anyMatch(powerType -> powerType.isActive() && powerType.shouldBeNeutral(entity));
	}
}
