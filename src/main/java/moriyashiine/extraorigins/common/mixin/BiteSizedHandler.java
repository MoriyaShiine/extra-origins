package moriyashiine.extraorigins.common.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(PlayerEntity.class)
public abstract class BiteSizedHandler extends LivingEntity {
	@Shadow
	@Final
	private static Map<EntityPose, EntityDimensions> POSE_DIMENSIONS;
	
	protected BiteSizedHandler(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "tick", at = @At("TAIL"), cancellable = true)
	private void tick(CallbackInfo callbackInfo)
	{
		if (EOPowers.BITE_SIZED.isActive(this))
		{
			calculateDimensions();
		}
	}
	
	@Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
	private void getDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> callbackInfo)
	{
		if (EOPowers.BITE_SIZED.isActive(this))
		{
			callbackInfo.setReturnValue(POSE_DIMENSIONS.getOrDefault(pose, PlayerEntity.STANDING_DIMENSIONS).scaled(0.5f));
		}
	}
	
	@Inject(method = "getActiveEyeHeight", at = @At("HEAD"), cancellable = true)
	private void getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> callbackInfo)
	{
		if (age > 0 && EOPowers.BITE_SIZED.isActive(this))
		{
			callbackInfo.setReturnValue(super.getActiveEyeHeight(pose, dimensions) * 0.5f);
		}
	}
	
	@Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
	private void addExhaustion(float exhaustion, CallbackInfo callbackInfo)
	{
		if (EOPowers.BITE_SIZED.isActive(this) && random.nextBoolean())
		{
			callbackInfo.cancel();
		}
	}
	
	@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
	private void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> callbackInfo)
	{
		if (EOPowers.BITE_SIZED.isActive(this) && (damageSource == DamageSource.FALL || damageSource == DamageSource.FLY_INTO_WALL))
		{
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Mixin(TrackTargetGoal.class)
	private static abstract class Tracker extends Goal
	{
		@Shadow
		@Final
		protected MobEntity mob;
		
		@Shadow
		protected LivingEntity target;
		
		@Shadow
		protected abstract double getFollowRange();
		
		@Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
		private void shouldContinue(CallbackInfoReturnable<Boolean> callbackInfo)
		{
			if (target != null && EOPowers.BITE_SIZED.isActive(target))
			{
				double range = getFollowRange() / 2;
				if (mob.squaredDistanceTo(target) > range * range)
				{
					stop();
					callbackInfo.setReturnValue(false);
				}
			}
		}
	}
}