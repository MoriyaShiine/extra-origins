package moriyashiine.extraorigins.client.mixin;

import moriyashiine.extraorigins.common.interfaces.BiteSizedAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public class BitSizedClientHandler {
	@Inject(method = "scale", at = @At("TAIL"))
	private void scale(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, CallbackInfo callbackInfo) {
		if (((BiteSizedAccessor) abstractClientPlayerEntity).getBiteSized()) {
			matrixStack.scale(0.5f, 0.5f, 0.5f);
		}
	}
	
	@Inject(method = "setupTransforms", at = @At("TAIL"))
	private void setupTransforms(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float h, CallbackInfo callbackInfo) {
		if (((BiteSizedAccessor) abstractClientPlayerEntity).getBiteSized()) {
			float leaningPitch = abstractClientPlayerEntity.getLeaningPitch(h);
			if (leaningPitch > 0 && abstractClientPlayerEntity.isInSwimmingPose()) {
				matrixStack.translate(0, 1, -0.3);
			}
		}
	}
	
	@Mixin(EntityRenderDispatcher.class)
	private static class Shadow {
		@ModifyVariable(method = {"renderShadow"}, at = @At("HEAD"), index = 6)
		private static float renderShadow(float radius, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, float opacity, float tickDelta, WorldView world) {
			if (entity instanceof BiteSizedAccessor && ((BiteSizedAccessor) entity).getBiteSized()) {
				return radius * 0.25f;
			}
			return radius;
		}
	}
}