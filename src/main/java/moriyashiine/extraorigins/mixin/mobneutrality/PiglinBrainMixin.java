/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin.mobneutrality;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MobNeutralityPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {
	@Unique
	private static Entity cachedEntity = null;

	@ModifyVariable(method = "tickActivities", at = @At("HEAD"), argsOnly = true)
	private static PiglinEntity extraorigins$mobNeutrality(PiglinEntity entity) {
		cachedEntity = entity;
		return entity;
	}

	@Inject(method = "wearsGoldArmor", at = @At("HEAD"), cancellable = true)
	private static void extraorigins$mobNeutrality(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
		for (MobNeutralityPower power : PowerHolderComponent.getPowers(target, MobNeutralityPower.class)) {
			if (power.shouldBeNeutral(cachedEntity)) {
				cir.setReturnValue(true);
				return;
			}
		}
	}

	@Inject(method = "onGuardedBlockInteracted", at = @At("HEAD"), cancellable = true)
	private static void extraorigins$mobNeutrality(PlayerEntity player, boolean blockOpen, CallbackInfo ci) {
		for (MobNeutralityPower power : PowerHolderComponent.getPowers(player, MobNeutralityPower.class)) {
			if (power.shouldBeNeutral(cachedEntity)) {
				ci.cancel();
				return;
			}
		}
	}
}
