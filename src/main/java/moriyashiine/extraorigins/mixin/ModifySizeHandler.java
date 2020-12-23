package moriyashiine.extraorigins.mixin;

import io.github.apace100.origins.component.OriginComponent;
import moriyashiine.extraorigins.common.interfaces.BabyAccessor;
import moriyashiine.extraorigins.common.power.ModifySizePower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class ModifySizeHandler extends Entity implements BabyAccessor {
	private static final TrackedData<Boolean> BABY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	protected ModifySizeHandler(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public boolean getBaby() {
		return dataTracker.get(BABY);
	}
	
	@Override
	public void setBaby(boolean baby) {
		dataTracker.set(BABY, baby);
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void readCustomDataFromTag(CompoundTag tag, CallbackInfo callbackInfo) {
		setBaby(tag.getBoolean("Baby"));
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void writeCustomDataToTag(CompoundTag tag, CallbackInfo callbackInfo) {
		tag.putBoolean("Baby", getBaby());
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(BABY, false);
	}
	
	@Mixin(LivingEntity.class)
	private static abstract class Baby extends Entity {
		public Baby(EntityType<?> type, World world) {
			super(type, world);
		}
		
		@Inject(method = "isBaby", at = @At("HEAD"), cancellable = true)
		private void isBaby(CallbackInfoReturnable<Boolean> callbackInfo) {
			BabyAccessor.get(this).ifPresent(babyAccessor -> {
				if (babyAccessor.getBaby()) {
					callbackInfo.setReturnValue(true);
				}
			});
		}
	}
	
	@Mixin(LivingEntity.class)
	private static abstract class JumpVelocity extends Entity {
		public JumpVelocity(EntityType<?> type, World world) {
			super(type, world);
		}
		
		@Inject(method = "getJumpVelocity", at = @At("RETURN"), cancellable = true)
		private void getJumpVelocity(CallbackInfoReturnable<Float> callbackInfo) {
			//noinspection ConstantConditions
			if (((Object) this) instanceof PlayerEntity && OriginComponent.hasPower(this, ModifySizePower.class)) {
				callbackInfo.setReturnValue(callbackInfo.getReturnValue() * OriginComponent.getPowers(this, ModifySizePower.class).get(0).scale * 2);
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
			if (target instanceof PlayerEntity && OriginComponent.hasPower(target, ModifySizePower.class)) {
				callbackInfo.setReturnValue(callbackInfo.getReturnValue() * OriginComponent.getPowers(target, ModifySizePower.class).get(0).scale);
			}
		}
	}
}
