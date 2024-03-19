/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin.mobneutrality;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MobNeutralityPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBruteBrain;
import net.minecraft.entity.mob.PiglinBruteEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PiglinBruteBrain.class)
public class PiglinBruteBrainMixin {
	@Unique
	private static Entity cachedEntity = null;

	@ModifyVariable(method = "tick", at = @At("HEAD"), argsOnly = true)
	private static PiglinBruteEntity extraorigins$mobNeutrality(PiglinBruteEntity entity) {
		cachedEntity = entity;
		return entity;
	}

	@WrapOperation(method = "method_30255", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInRange(Lnet/minecraft/entity/Entity;D)Z"))
	private static boolean extraorigins$mobNeutrality(LivingEntity instance, Entity entity, double v, Operation<Boolean> original) {
		boolean inRange = original.call(instance, entity, v);
		if (inRange) {
			for (MobNeutralityPower power : PowerHolderComponent.getPowers(entity, MobNeutralityPower.class)) {
				if (power.shouldBeNeutral(cachedEntity)) {
					return false;
				}
			}
		}
		return inRange;
	}
}
