/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin.foodeffectimmunity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.FoodEffectImmunityPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@WrapOperation(method = "applyFoodEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/FoodComponent;getStatusEffects()Ljava/util/List;"))
	private List<Pair<StatusEffectInstance, Float>> extraorigins$foodEffectImmunity(FoodComponent instance, Operation<List<Pair<StatusEffectInstance, Float>>> original) {
		List<Pair<StatusEffectInstance, Float>> effects = original.call(instance);
		if (!effects.isEmpty()) {
			List<FoodEffectImmunityPower> powers = PowerHolderComponent.getPowers((LivingEntity) (Object) this, FoodEffectImmunityPower.class);
			if (!powers.isEmpty()) {
				effects = new ArrayList<>(effects);
				for (FoodEffectImmunityPower foodEffectImmunityPower : powers) {
					for (int i = effects.size() - 1; i >= 0; i--) {
						if (foodEffectImmunityPower.shouldRemove(effects.get(i).getFirst().getEffectType())) {
							effects.remove(i);
						}
					}
				}
			}
		}
		return effects;
	}
}
