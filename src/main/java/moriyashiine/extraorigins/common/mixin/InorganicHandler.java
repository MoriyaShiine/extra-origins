package moriyashiine.extraorigins.common.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class InorganicHandler extends LivingEntity {
	@Shadow
	public abstract HungerManager getHungerManager();
	
	protected InorganicHandler(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (EOPowers.INORGANIC.isActive(this)) {
			getHungerManager().setFoodLevel(0);
			if (age % 100 == 0) {
				heal(1);
			}
		}
	}
	
	@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
	private void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (EOPowers.INORGANIC.isActive(this) && (damageSource == DamageSource.STARVE || damageSource == DamageSource.DROWN || damageSource == DamageSource.CACTUS || damageSource == DamageSource.SWEET_BERRY_BUSH || damageSource == DamageSource.IN_WALL || damageSource == DamageSource.FALLING_BLOCK || (damageSource instanceof EntityDamageSource && ((EntityDamageSource) damageSource).isThorns()))) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Mixin(LivingEntity.class)
	private static abstract class EffectImmunity extends Entity {
		public EffectImmunity(EntityType<?> type, World world) {
			super(type, world);
		}
		
		@Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
		private void canHaveStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> callbackInfo) {
			if (EOPowers.INORGANIC.isActive(this)) {
				callbackInfo.setReturnValue(false);
			}
		}
	}
}