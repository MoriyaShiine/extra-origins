package moriyashiine.extraorigins.common.mixin;

import com.mojang.authlib.GameProfile;
import moriyashiine.extraorigins.common.interfaces.BiteSizedAccessor;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
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
public abstract class BiteSizedHandler extends LivingEntity implements BiteSizedAccessor {
	private static final TrackedData<Boolean> BITE_SIZED = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
	@Shadow
	@Final
	private static Map<EntityPose, EntityDimensions> POSE_DIMENSIONS;
	
	protected BiteSizedHandler(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public boolean getBiteSized() {
		return dataTracker.get(BITE_SIZED);
	}
	
	@Override
	public void setBiteSized(boolean biteSized) {
		dataTracker.set(BITE_SIZED, biteSized);
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		dataTracker.set(BITE_SIZED, tag.getBoolean("BiteSized"));
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		tag.putBoolean("BiteSized", dataTracker.get(BITE_SIZED));
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(BITE_SIZED, false);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (world.isClient) {
			calculateDimensions();
		}
	}
	
	@Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
	private void getDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> callbackInfo) {
		if (getBiteSized()) {
			callbackInfo.setReturnValue(POSE_DIMENSIONS.getOrDefault(pose, PlayerEntity.STANDING_DIMENSIONS).scaled(0.25f, pose == EntityPose.SWIMMING ? 0.5f : 0.25f));
		}
	}
	
	@Inject(method = "getActiveEyeHeight", at = @At("HEAD"), cancellable = true)
	private void getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> callbackInfo) {
		if (getBiteSized()) {
			float size;
			switch (pose) {
				case SWIMMING:
				case FALL_FLYING:
				case SPIN_ATTACK:
					size = 0.4f;
					break;
				case CROUCHING:
					size = 1.27f;
					break;
				default:
					size = 1.62f;
					break;
			}
			callbackInfo.setReturnValue((size * 0.25f) - 1 / 128f);
		}
	}
	
	@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
	private void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (getBiteSized() && (damageSource == DamageSource.CACTUS || damageSource == DamageSource.SWEET_BERRY_BUSH)) {
			callbackInfo.setReturnValue(true);
		}
	}
	
	@Mixin(ServerPlayerEntity.class)
	private static abstract class Server extends PlayerEntity implements BiteSizedAccessor {
		public Server(World world, BlockPos blockPos, GameProfile gameProfile) {
			super(world, blockPos, gameProfile);
		}
		
		@Inject(method = "copyFrom", at = @At("TAIL"))
		private void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo callbackInfo) {
			setBiteSized(((BiteSizedAccessor) oldPlayer).getBiteSized());
		}
	}
	
	@Mixin(Entity.class)
	private static class JumpVelocity {
		@Shadow
		@Final
		protected DataTracker dataTracker;
		
		@Inject(method = "getJumpVelocityMultiplier", at = @At("HEAD"), cancellable = true)
		private void getJumpVelocityMultiplier(CallbackInfoReturnable<Float> callbackInfo) {
			Object obj = this;
			if (obj instanceof BiteSizedAccessor && ((BiteSizedAccessor) obj).getBiteSized()) {
				callbackInfo.setReturnValue(0.6f);
			}
		}
	}
	
	@Mixin(LivingEntity.class)
	private static abstract class Baby extends Entity {
		public Baby(EntityType<?> type, World world) {
			super(type, world);
		}
		
		@Inject(method = "isBaby", at = @At("HEAD"), cancellable = true)
		private void isBaby(CallbackInfoReturnable<Boolean> callbackInfo) {
			Object obj = this;
			if (obj instanceof BiteSizedAccessor && ((BiteSizedAccessor) obj).getBiteSized()) {
				callbackInfo.setReturnValue(true);
			}
		}
	}
	
	@Mixin(TrackTargetGoal.class)
	private static abstract class MonsterAITracker extends Goal {
		@Shadow
		@Final
		protected MobEntity mob;
		
		@Shadow
		protected LivingEntity target;
		
		@Shadow
		protected abstract double getFollowRange();
		
		@Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
		private void shouldContinue(CallbackInfoReturnable<Boolean> callbackInfo) {
			if (target instanceof BiteSizedAccessor && ((BiteSizedAccessor) target).getBiteSized()) {
				double range = getFollowRange() / 4;
				if (mob.squaredDistanceTo(target) > range * range) {
					stop();
					callbackInfo.setReturnValue(false);
				}
			}
		}
	}
}