package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class HomesickHandler extends Entity {
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);
	
	public HomesickHandler(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "getGroup", at = @At("HEAD"), cancellable = true)
	private void getGroup(CallbackInfoReturnable<EntityGroup> callbackInfo) {
		if (EOPowers.HOMESICK.isActive(this) && !world.getDimension().isPiglinSafe()) {
			callbackInfo.setReturnValue(EntityGroup.UNDEAD);
		}
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (EOPowers.HOMESICK.isActive(this) && !world.getDimension().isPiglinSafe()) {
			addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 1, true, false));
		}
	}
	
	@Mixin(PlayerEntity.class)
	private static abstract class FireImmune extends LivingEntity {
		protected FireImmune(EntityType<? extends LivingEntity> entityType, World world) {
			super(entityType, world);
		}
		
		@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
		private void isFireImmune(CallbackInfoReturnable<Boolean> callbackInfo) {
			if (EOPowers.HOMESICK.isActive(this) && !world.getDimension().isPiglinSafe()) {
				callbackInfo.setReturnValue(true);
			}
		}
	}
}
