/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MobNeutralityPower;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinBruteBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PiglinBruteBrain.class)
public class PiglinBruteBrainMixin {
	@Inject(method = "getTargetIfInRange", at = @At("RETURN"), cancellable = true)
	private static void extraorigins$mobNeutrality(AbstractPiglinEntity piglin, MemoryModuleType<? extends LivingEntity> memoryModuleType, CallbackInfoReturnable<Optional<? extends LivingEntity>> cir) {
		if (cir.getReturnValue().isPresent()) {
			piglin.getBrain().getOptionalMemory(memoryModuleType).filter(entity -> {
				PowerHolderComponent.getPowers(entity, MobNeutralityPower.class).forEach(mobNeutralityPower -> {
					if (mobNeutralityPower.shouldBeNeutral(EntityType.PIGLIN_BRUTE)) {
						cir.setReturnValue(Optional.empty());
					}
				});
				return false;
			});
		}
	}
}
