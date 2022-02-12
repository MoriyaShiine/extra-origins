/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.FoodEffectImmunityPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SuspiciousStewItem.class)
public abstract class SuspiciousStewItemMixin {
	@ModifyVariable(method = "finishUsing", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/effect/StatusEffect;byRawId(I)Lnet/minecraft/entity/effect/StatusEffect;"))
	private StatusEffect extraorigins$foodEffectImmunity(StatusEffect obj, ItemStack stack, World world, LivingEntity user) {
		for (FoodEffectImmunityPower foodEffectImmunityPower : PowerHolderComponent.getPowers(user, FoodEffectImmunityPower.class)) {
			if (foodEffectImmunityPower.shouldRemove(obj)) {
				return null;
			}
		}
		return obj;
	}
}
