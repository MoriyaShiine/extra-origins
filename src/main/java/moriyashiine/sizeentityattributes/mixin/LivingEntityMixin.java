package moriyashiine.sizeentityattributes.mixin;

import moriyashiine.sizeentityattributes.SizeEntityAttributes;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract double getAttributeValue(EntityAttribute attribute);
	
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (age == 1 || world.isClient) {
			calculateDimensions();
		}
	}
	
	@Inject(method = "getActiveEyeHeight", at = @At("RETURN"), cancellable = true)
	private void getActiveEyeHeight(CallbackInfoReturnable<Float> callbackInfo) {
		callbackInfo.setReturnValue((float) (callbackInfo.getReturnValue() * (age > 0 ? getAttributeValue(SizeEntityAttributes.HEIGHT_MULTIPLIER) : 1)));
	}
	
	@Inject(method = "getDimensions", at = @At("RETURN"), cancellable = true)
	private void getDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> callbackInfo) {
		callbackInfo.setReturnValue(callbackInfo.getReturnValue().scaled((float) getAttributeValue(SizeEntityAttributes.WIDTH_MULTIPLIER), (float) getAttributeValue(SizeEntityAttributes.HEIGHT_MULTIPLIER)));
	}
	
	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> builder) {
		builder.getReturnValue().add(SizeEntityAttributes.WIDTH_MULTIPLIER);
		builder.getReturnValue().add(SizeEntityAttributes.HEIGHT_MULTIPLIER);
	}
}