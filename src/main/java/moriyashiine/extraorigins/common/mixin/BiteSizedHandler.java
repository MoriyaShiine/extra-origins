package moriyashiine.extraorigins.common.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class BiteSizedHandler extends Entity {
	protected BiteSizedHandler(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
	private void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (EOPowers.BITE_SIZED.isActive(this) && (damageSource == DamageSource.CACTUS || damageSource == DamageSource.SWEET_BERRY_BUSH || (damageSource instanceof EntityDamageSource && ((EntityDamageSource) damageSource).isThorns()))) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Mixin(LivingEntity.class)
	private static abstract class JumpVelocity extends Entity {
		public JumpVelocity(EntityType<?> type, World world) {
			super(type, world);
		}
		
		@Inject(method = "getJumpVelocity", at = @At("RETURN"), cancellable = true)
		private void getJumpVelocity(CallbackInfoReturnable<Float> callbackInfo) {
			if (EOPowers.BITE_SIZED.isActive(this)) {
				callbackInfo.setReturnValue(callbackInfo.getReturnValue() * 0.6f);
			}
		}
	}
	
	@Mixin(TrackTargetGoal.class)
	private static abstract class MonsterAITracker extends Goal {
		@Final
		@Shadow
		protected MobEntity mob;
		
		@Shadow
		protected LivingEntity target;
		
		@Inject(method = "getFollowRange", at = @At("RETURN"), cancellable = true)
		private void getFollowRange(CallbackInfoReturnable<Double> callbackInfo) {
			LivingEntity target = mob.getTarget();
			if (target == null) {
				target = this.target;
			}
			if (EOPowers.BITE_SIZED.isActive(target)) {
				callbackInfo.setReturnValue(callbackInfo.getReturnValue() / 4);
			}
		}
	}
}