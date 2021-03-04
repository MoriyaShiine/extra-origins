package moriyashiine.extraorigins.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.HashSet;

@Environment(EnvType.CLIENT)
@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin {
	@Shadow
	public ModelPart helmet;
	
	@Inject(method = "getHeadParts", at = @At("RETURN"), cancellable = true)
	private void getHeadParts(CallbackInfoReturnable<Iterable<ModelPart>> callbackInfo) {
		Collection<ModelPart> parts = new HashSet<>();
		callbackInfo.getReturnValue().forEach(parts::add);
		parts.add(helmet);
		callbackInfo.setReturnValue(parts);
	}
}
