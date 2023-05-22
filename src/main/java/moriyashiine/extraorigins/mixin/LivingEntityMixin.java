/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.FoodEffectImmunityPower;
import moriyashiine.extraorigins.common.power.ModifyAirStrafingSpeedPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "applyFoodEffects", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/FoodComponent;getStatusEffects()Ljava/util/List;"))
	private List<Pair<StatusEffectInstance, Float>> extraorigins$foodEffectImmunity(List<Pair<StatusEffectInstance, Float>> value) {
		for (FoodEffectImmunityPower foodEffectImmunityPower : PowerHolderComponent.getPowers(LivingEntity.class.cast(this), FoodEffectImmunityPower.class)) {
			if (!value.isEmpty()) {
				value = new ArrayList<>(value);
				for (int i = value.size() - 1; i >= 0; i--) {
					if (foodEffectImmunityPower.shouldRemove(value.get(i).getFirst().getEffectType())) {
						value.remove(i);
					}
				}
			}
		}
		return value;
	}

	@ModifyArg(method = "applyMovementInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"))
	private float extraorigins$modifyAirStrafingSpeed(float value) {
		if (!onGround) {
			return PowerHolderComponent.modify(LivingEntity.class.cast(this), ModifyAirStrafingSpeedPower.class, value);
		}
		return value;
	}
}
