/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.mixin.powertype.modifyhostiledetectionrange;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.powertype.ModifyHostileDetectionRangePowerType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyReturnValue(method = "getAttackDistanceScalingFactor", at = @At("RETURN"))
	private double extraorigins$modifyHostileDetectionRange(double original, Entity entity) {
		if (entity == null || entity instanceof Monster) {
			return PowerHolderComponent.modify(this, ModifyHostileDetectionRangePowerType.class, original);
		}
		return original;
	}
}
