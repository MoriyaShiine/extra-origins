/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin.mobneutrality;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MobNeutralityPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin(FleeEntityGoal.class)
public class FleeEntityGoalMixin {
	@ModifyVariable(method = "<init>(Lnet/minecraft/entity/mob/PathAwareEntity;Ljava/lang/Class;Ljava/util/function/Predicate;FDDLjava/util/function/Predicate;)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	private static Predicate<LivingEntity> extraorigins$mobNeutrality(Predicate<LivingEntity> value, PathAwareEntity mob) {
		Predicate<LivingEntity> neutralityPredicate = target -> {
			for (MobNeutralityPower power : PowerHolderComponent.getPowers(target, MobNeutralityPower.class)) {
				if (power.shouldBeNeutral(mob) && power.isActive()) {
					return false;
				}
			}
			return true;
		};
		if (value == null) {
			return neutralityPredicate;
		}
		return value.and(neutralityPredicate);
	}
}
