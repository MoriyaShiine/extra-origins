/*
 * All Rights Reserved (c) MoriyaShiine
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SuspiciousStewItem.class)
public class SuspiciousStewItemMixin {
	@Unique
	private static LivingEntity tempLiving = null;

	@Inject(method = "finishUsing", at = @At("HEAD"))
	private void extraorigins$cacheLivingBefore(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
		tempLiving = user;
	}

	@Inject(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/SuspiciousStewItem;forEachEffect(Lnet/minecraft/item/ItemStack;Ljava/util/function/Consumer;)V", shift = At.Shift.AFTER))
	private void extraorigins$cacheLivingAfter(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
		tempLiving = null;
	}

	@ModifyVariable(method = "forEachEffect", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/effect/StatusEffect;byRawId(I)Lnet/minecraft/entity/effect/StatusEffect;"))
	private static StatusEffect extraorigins$foodEffectImmunity(StatusEffect value) {
		for (FoodEffectImmunityPower foodEffectImmunityPower : PowerHolderComponent.getPowers(tempLiving, FoodEffectImmunityPower.class)) {
			if (foodEffectImmunityPower.shouldRemove(value)) {
				return null;
			}
		}
		return value;
	}
}
