/*
 * All Rights Reserved (c) 2021 MoriyaShiine
 */

/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.client.network.packet.DismountPacket;
import moriyashiine.extraorigins.common.power.MountPower;
import moriyashiine.extraorigins.common.registry.ModPowers;
import moriyashiine.extraorigins.common.registry.ModScaleTypes;
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

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	private EntityDimensions dimensions;
	
	@Shadow
	@Nullable
	public abstract Entity getFirstPassenger();
	
	@Inject(method = "slowMovement", at = @At("HEAD"), cancellable = true)
	private void extraorigins$disableNimbleSlownessFromBlocks(BlockState state, Vec3d multiplier, CallbackInfo ci) {
		if (ModPowers.NIMBLE.isActive((Entity) (Object) this)) {
			ci.cancel();
		}
	}
	
	@SuppressWarnings("ConstantConditions")
	@Inject(method = "getMountedHeightOffset", at = @At("HEAD"), cancellable = true)
	private void extraorigins$changeMountedPlayerOffsetWithPower(CallbackInfoReturnable<Double> cir) {
		if ((Object) this instanceof PlayerEntity player && getFirstPassenger() != null && PowerHolderComponent.hasPower(getFirstPassenger(), MountPower.class)) {
			cir.setReturnValue((double) (dimensions.height * ModScaleTypes.MODIFY_SIZE_TYPE.getScaleData(player).getScale()));
		}
	}
	
	@Inject(method = "dismountVehicle", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;removePassenger(Lnet/minecraft/entity/Entity;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void extraorigins$dismountPlayerWithPower(CallbackInfo ci, Entity entity) {
		if (entity instanceof ServerPlayerEntity player && PowerHolderComponent.hasPower((Entity) (Object) this, MountPower.class)) {
			DismountPacket.send(player, (Entity) (Object) this);
		}
	}
}
