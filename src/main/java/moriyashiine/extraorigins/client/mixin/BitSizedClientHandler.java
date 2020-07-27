package moriyashiine.extraorigins.client.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public class BitSizedClientHandler
{
	@Inject(method = "scale", at = @At("TAIL"))
	private void scale(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, CallbackInfo callbackInfo) {
		if (EOPowers.BITE_SIZED.isActive(abstractClientPlayerEntity))
		{
			matrixStack.scale(0.5f, 0.5f, 0.5f);
		}
	}
}