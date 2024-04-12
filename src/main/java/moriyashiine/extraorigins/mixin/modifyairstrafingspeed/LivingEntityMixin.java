/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin.modifyairstrafingspeed;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.ModifyAirStrafingSpeedPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyArg(method = "applyMovementInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"))
	private float extraorigins$modifyAirStrafingSpeed(float value) {
		if (!isOnGround()) {
			return PowerHolderComponent.modify(this, ModifyAirStrafingSpeedPower.class, value);
		}
		return value;
	}
}
