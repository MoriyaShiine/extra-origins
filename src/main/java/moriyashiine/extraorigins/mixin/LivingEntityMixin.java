/*
 * All Rights Reserved (c) 2021 MoriyaShiine
 */

/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.component.entity.MagicSporesComponent;
import moriyashiine.extraorigins.common.registry.ModComponents;
import moriyashiine.extraorigins.common.registry.ModPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private float extraorigins$magicSporesDamageTakenModifier(float obj, DamageSource source) {
		MagicSporesComponent magicSporesComponent = ModComponents.MAGIC_SPORES.getNullable(this);
		if (magicSporesComponent != null) {
			obj *= magicSporesComponent.getMode().getDamageTakenModifier();
		}
		return obj;
	}
	
	@Inject(method = "applyFoodEffects", at = @At("HEAD"), cancellable = true)
	private void extraorigins$decomposeFoodEffects(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci) {
		if (ModPowers.DECOMPOSITION.isActive((Entity) (Object) this)) {
			ci.cancel();
		}
	}
}
