/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin.foodeffectimmunity;

import com.google.common.collect.ImmutableList;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.FoodEffectImmunityPower;
import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SuspiciousStewItem.class)
public class SuspiciousStewItemMixin {
	@Unique
	private static LivingEntity cachedEntity = null;

	@Inject(method = "finishUsing", at = @At("HEAD"))
	private void extraorigins$cacheLivingBefore(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
		cachedEntity = user;
	}

	@ModifyVariable(method = "method_53207", at = @At("HEAD"), argsOnly = true)
	private static List<SuspiciousStewIngredient.StewEffect> extraorigins$foodEffectImmunity(List<SuspiciousStewIngredient.StewEffect> list) {
		if (PowerHolderComponent.hasPower(cachedEntity, FoodEffectImmunityPower.class)) {
			ImmutableList.Builder<SuspiciousStewIngredient.StewEffect> newList = ImmutableList.builder();
			for (SuspiciousStewIngredient.StewEffect stewEffect : list) {
				if (PowerHolderComponent.getPowers(cachedEntity, FoodEffectImmunityPower.class).stream().noneMatch(foodEffectImmunityPower -> foodEffectImmunityPower.shouldRemove(stewEffect.effect()))) {
					newList.add(stewEffect);
				}
			}
			cachedEntity = null;
			return newList.build();
		}
		return list;
	}
}
