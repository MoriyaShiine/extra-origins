package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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
			if (world.isClient) {
				getHungerManager().setSaturationLevelClient(20);
			}
			if (age % 100 == 0) {
				heal(1);
			}
		}
	}
	
	@Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
	private void getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> callbackInfo) {
		if (EOPowers.INORGANIC.isActive(this)) {
			callbackInfo.setReturnValue(SoundEvents.BLOCK_GLASS_HIT);
		}
	}
	
	@Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
	private void getHurtSound(CallbackInfoReturnable<SoundEvent> callbackInfo) {
		if (EOPowers.INORGANIC.isActive(this)) {
			callbackInfo.setReturnValue(SoundEvents.BLOCK_GLASS_BREAK);
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
				callbackInfo.setReturnValue(effect.getEffectType() == StatusEffects.BAD_OMEN);
			}
		}
		
		@Inject(method = "isAffectedBySplashPotions", at = @At("HEAD"), cancellable = true)
		private void isAffectedBySplashPotions(CallbackInfoReturnable<Boolean> callbackInfo) {
			if (EOPowers.INORGANIC.isActive(this)) {
				callbackInfo.setReturnValue(false);
			}
		}
	}
	
	@Mixin(Entity.class)
	private static abstract class NoAir {
		@Shadow
		public World world;
		
		@Inject(method = "getAir", at = @At("HEAD"), cancellable = true)
		private void getAir(CallbackInfoReturnable<Integer> callbackInfo) {
			if (EOPowers.INORGANIC.isActive(((Entity) (Object) this)) && world.isClient) {
				callbackInfo.setReturnValue(0);
			}
		}
	}
}
