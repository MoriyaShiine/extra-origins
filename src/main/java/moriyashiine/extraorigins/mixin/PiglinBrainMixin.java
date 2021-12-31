/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MobNeutralityPower;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
	@Inject(method = "wearsGoldArmor", at = @At("HEAD"), cancellable = true)
	private static void extraorigins$addMobNeutrality(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
		PowerHolderComponent.getPowers(target, MobNeutralityPower.class).forEach(mobNeutralityPower -> {
			if (mobNeutralityPower.shouldBeNeutral(EntityType.PIGLIN)) {
				cir.setReturnValue(true);
			}
		});
	}
	
	@Inject(method = "onGuardedBlockInteracted", at = @At("HEAD"), cancellable = true)
	private static void extraorigins$addMobNeutrality(PlayerEntity player, boolean blockOpen, CallbackInfo ci) {
		PowerHolderComponent.getPowers(player, MobNeutralityPower.class).forEach(mobNeutralityPower -> {
			if (mobNeutralityPower.shouldBeNeutral(EntityType.PIGLIN)) {
				ci.cancel();
			}
		});
	}
}
