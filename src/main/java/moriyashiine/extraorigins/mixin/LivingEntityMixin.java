/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.component.entity.MagicSporesComponent;
import moriyashiine.extraorigins.common.power.FoodEffectImmunityPower;
import moriyashiine.extraorigins.common.power.MagicSporesPower;
import moriyashiine.extraorigins.common.registry.ModComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@ModifyVariable(method = "applyFoodEffects", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/FoodComponent;getStatusEffects()Ljava/util/List;"))
	private List<Pair<StatusEffectInstance, Float>> extraorigins$foodEffectImmunity(List<Pair<StatusEffectInstance, Float>> obj) {
		for (FoodEffectImmunityPower foodEffectImmunityPower : PowerHolderComponent.getPowers(LivingEntity.class.cast(this), FoodEffectImmunityPower.class)) {
			if (!obj.isEmpty()) {
				obj = new ArrayList<>(obj);
				for (int i = obj.size() - 1; i >= 0; i--) {
					if (foodEffectImmunityPower.shouldRemove(obj.get(i).getFirst().getEffectType())) {
						obj.remove(i);
					}
				}
			}
		}
		return obj;
	}

	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private float extraorigins$magicSporesDamageTakenModifier(float obj, DamageSource source) {
		if (PowerHolderComponent.hasPower(LivingEntity.class.cast(this), MagicSporesPower.class)) {
			MagicSporesComponent magicSporesComponent = ModComponents.MAGIC_SPORES.getNullable(this);
			if (magicSporesComponent != null) {
				obj *= magicSporesComponent.getMode().getDamageTakenModifier();
			}
		}
		return obj;
	}
}
