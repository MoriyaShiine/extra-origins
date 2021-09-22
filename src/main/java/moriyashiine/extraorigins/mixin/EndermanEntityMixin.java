package moriyashiine.extraorigins.mixin;

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
public abstract class EndermanEntityMixin {
	@Inject(method = "isPlayerStaring", at = @At("RETURN"), cancellable = true)
	private void isPlayerStaring(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			PowerHolderComponent.getPowers(player, MobNeutralityPower.class).forEach(power -> {
				if (power.entityTypes.contains(EntityType.ENDERMAN) && power.isActive()) {
					cir.setReturnValue(false);
				}
			});
		}
	}
}
