/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.mixin.powertype.foodeffectimmunity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.powertype.FoodEffectImmunityPowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@WrapWithCondition(method = "applyFoodEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"))
	private boolean extraorigins$foodEffectImmunity(LivingEntity instance, StatusEffectInstance effect) {
		return PowerHolderComponent.getPowerTypes(instance, FoodEffectImmunityPowerType.class).stream().noneMatch(powerType -> powerType.isActive() && powerType.shouldRemove(effect.getEffectType()));
	}
}
