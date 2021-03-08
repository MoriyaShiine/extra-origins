package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient && EOPowers.DELICATE.isActive(this)) {
			float temperature = world.getBiome(getBlockPos()).getTemperature(getBlockPos());
			if (temperature < 0.15f || temperature > 1) {
				addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 0, true, false));
			}
		}
	}
	
	@Inject(method = "canFoodHeal", at = @At("HEAD"), cancellable = true)
	private void canFoodHeal(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (EOPowers.PHOTOSYNTHESIS.get(this) != null) {
			callbackInfo.setReturnValue(false);
		}
	}
}
