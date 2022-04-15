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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public float airStrafingSpeed;

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

	@Inject(method = "tick", at = @At("HEAD"))
	private void extraorigins$modifyAirStrafingSpeed(CallbackInfo ci) {
		airStrafingSpeed = PowerHolderComponent.modify(LivingEntity.class.cast(this), ModifyAirStrafingSpeedPower.class, airStrafingSpeed);
	}
}
