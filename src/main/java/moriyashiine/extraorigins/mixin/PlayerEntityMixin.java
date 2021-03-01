package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.interfaces.BabyAccessor;
import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements BabyAccessor {
	private static final TrackedData<Boolean> BABY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
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
	
	@Inject(method = "tick", at = @At("HEAD"))
	private void tick(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			if (EOPowers.DELICATE.isActive(this)) {
				float temperature = world.getBiome(getBlockPos()).getTemperature(getBlockPos());
				if (temperature < 0.15f || temperature > 1) {
					addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 0, true, false));
				}
			}
			if (EOPowers.HOMESICK.isActive(this) && !world.getDimension().isPiglinSafe()) {
				addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 1, true, false));
			}
		}
	}
	
	@Inject(method = "canFoodHeal", at = @At("HEAD"), cancellable = true)
	private void canFoodHeal(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (EOPowers.PHOTOSYNTHESIS.isActive(this)) {
			callbackInfo.setReturnValue(false);
		}
	}
	
	@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
	private void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (damageSource.isFire() && EOPowers.HOMESICK.isActive(this) && !world.getDimension().isPiglinSafe()) {
			callbackInfo.setReturnValue(true);
		}
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
}
