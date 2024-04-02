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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PiglinBruteBrain.class)
public class PiglinBruteBrainMixin {
	@WrapOperation(method = "method_30255", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInRange(Lnet/minecraft/entity/Entity;D)Z"))
	private static boolean extraorigins$mobNeutrality(LivingEntity instance, Entity entity, double radius, Operation<Boolean> original) {
		boolean inRange = original.call(instance, entity, radius);
		if (inRange) {
			for (MobNeutralityPower power : PowerHolderComponent.getPowers(instance, MobNeutralityPower.class)) {
				if (power.shouldBeNeutral(entity)) {
					return false;
				}
			}
		}
		return inRange;
	}
}
