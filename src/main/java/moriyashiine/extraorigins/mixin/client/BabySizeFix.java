package moriyashiine.extraorigins.mixin.client;

import moriyashiine.extraorigins.common.interfaces.BabyAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public class BabySizeFix {
	@Inject(method = "scale", at = @At("TAIL"))
	private <T extends LivingEntity> void scale(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, CallbackInfo callbackInfo) {
		BabyAccessor.get(abstractClientPlayerEntity).ifPresent(babyAccessor -> {
			if (babyAccessor.getBaby()) {
				matrixStack.scale(2, 2, 2);
			}
		});
	}
}
