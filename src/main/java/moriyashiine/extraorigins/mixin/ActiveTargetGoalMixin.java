/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MobNeutralityPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin<T extends LivingEntity> {
	@Shadow
	protected TargetPredicate targetPredicate;

	@Inject(method = "<init>(Lnet/minecraft/entity/mob/MobEntity;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V", at = @At("TAIL"))
	private void extraorigins$addMobNeutrality(MobEntity mob, Class<T> targetClass, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, @Nullable Predicate<LivingEntity> predicate, CallbackInfo ci) {
		Predicate<LivingEntity> originalPredicate = ((TargetPredicateAccessor) targetPredicate).eo_getTargetPredicate();
		if (originalPredicate == null) {
			originalPredicate = entity -> true;
		}
		targetPredicate.setPredicate(originalPredicate.and(target -> {
			for (MobNeutralityPower mobNeutralityPower : PowerHolderComponent.getPowers(target, MobNeutralityPower.class)) {
				if (mobNeutralityPower.shouldBeNeutral(mob.getType())) {
					return false;
				}
			}
			return true;
		}));
	}
}
