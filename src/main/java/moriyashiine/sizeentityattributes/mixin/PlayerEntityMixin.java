package moriyashiine.sizeentityattributes.mixin;

import moriyashiine.sizeentityattributes.SizeEntityAttributes;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "getDimensions", at = @At("RETURN"), cancellable = true)
	private void getDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> callbackInfo) {
		callbackInfo.setReturnValue(callbackInfo.getReturnValue().scaled((float) getAttributeValue(SizeEntityAttributes.WIDTH_MULTIPLIER), (float) getAttributeValue(SizeEntityAttributes.HEIGHT_MULTIPLIER)));
	}
	
	@Inject(method = "getActiveEyeHeight", at = @At("RETURN"), cancellable = true)
	public void getActiveEyeHeight(CallbackInfoReturnable<Float> callbackInfo) {
		double heightMultiplier = age > 0 ? getAttributeValue(SizeEntityAttributes.HEIGHT_MULTIPLIER) : 1;
		callbackInfo.setReturnValue((float) (callbackInfo.getReturnValue() * heightMultiplier - 1 / 128f + (getPose() == EntityPose.SWIMMING && heightMultiplier < 1 ? heightMultiplier / 8 : 0)));
	}
}