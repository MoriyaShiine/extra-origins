/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.mixin.powertype.canwalkonpowdersnow;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.type.PowerType;
import moriyashiine.extraorigins.common.powertype.CanWalkOnPowderSnowPowerType;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
	@ModifyReturnValue(method = "canWalkOnPowderSnow", at = @At("RETURN"))
	private static boolean extraorigins$canWalkOnPowderSnow(boolean original, Entity entity) {
		return original || PowerHolderComponent.getPowerTypes(entity, CanWalkOnPowderSnowPowerType.class).stream().anyMatch(PowerType::isActive);
	}
}
