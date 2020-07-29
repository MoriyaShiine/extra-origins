package moriyashiine.sizeentityattributes.mixin.client;

import moriyashiine.sizeentityattributes.SizeEntityAttributes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
	@Inject(method = "scale", at = @At("TAIL"))
	private <T extends LivingEntity> void scale(T entity, MatrixStack matrices, float amount, CallbackInfo callbackInfo) {
		matrices.scale((float) entity.getAttributeValue(SizeEntityAttributes.WIDTH_MULTIPLIER), (float) entity.getAttributeValue(SizeEntityAttributes.HEIGHT_MULTIPLIER), (float) entity.getAttributeValue(SizeEntityAttributes.WIDTH_MULTIPLIER));
	}
	
	@Mixin(EntityRenderDispatcher.class)
	private static class Shadow {
		@ModifyVariable(method = {"renderShadow"}, at = @At("HEAD"), index = 6)
		private static float renderShadow(float radius, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, float opacity, float tickDelta, WorldView world) {
			if (entity instanceof LivingEntity) {
				return (float) (radius * ((LivingEntity) entity).getAttributeValue(SizeEntityAttributes.WIDTH_MULTIPLIER));
			}
			return radius;
		}
	}
}