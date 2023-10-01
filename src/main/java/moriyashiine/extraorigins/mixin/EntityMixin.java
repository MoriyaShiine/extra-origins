/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.client.packet.DismountPacket;
import moriyashiine.extraorigins.common.power.MountPower;
import moriyashiine.extraorigins.common.power.PreventBlockSlownessPower;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import virtuoel.pehkui.api.ScaleTypes;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	private EntityDimensions dimensions;

	@Shadow
	@Nullable
	public abstract Entity getFirstPassenger();

	@Inject(method = "slowMovement", at = @At("HEAD"), cancellable = true)
	private void extraorigins$preventBlockSlowness(BlockState state, Vec3d multiplier, CallbackInfo ci) {
		PowerHolderComponent.getPowers((Entity) (Object) this, PreventBlockSlownessPower.class).forEach(preventBlockSlownessPower -> {
			if (preventBlockSlownessPower.shouldPreventSlowness(state.getBlock())) {
				ci.cancel();
			}
		});
	}

	@Inject(method = "getMountedHeightOffset", at = @At("RETURN"), cancellable = true)
	private void extraorigins$changeMountedPlayerOffsetWithPower(CallbackInfoReturnable<Double> cir) {
		if ((Entity) (Object) this instanceof PlayerEntity player && getFirstPassenger() instanceof PlayerEntity rider && PowerHolderComponent.hasPower(rider, MountPower.class)) {
			cir.setReturnValue(cir.getReturnValueD() * ScaleTypes.HEIGHT.getScaleData(player).getScale() / dimensions.height);
		}
	}

	@Inject(method = "dismountVehicle", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;removePassenger(Lnet/minecraft/entity/Entity;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void extraorigins$dismountPlayerWithPower(CallbackInfo ci, Entity entity) {
		if (entity instanceof ServerPlayerEntity player && PowerHolderComponent.hasPower((Entity) (Object) this, MountPower.class)) {
			DismountPacket.send(player, (Entity) (Object) this);
		}
	}
}
