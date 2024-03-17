/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin.mobneutrality;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MobNeutralityPower;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin {
	@Inject(method = "isPlayerStaring", at = @At("HEAD"), cancellable = true)
	private void extraorigins$mobNeutrality(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		for (MobNeutralityPower power : PowerHolderComponent.getPowers(player, MobNeutralityPower.class)) {
			if (power.shouldBeNeutral(EntityType.ENDERMAN) && power.isActive()) {
				cir.setReturnValue(false);
				return;
			}
		}
	}
}
