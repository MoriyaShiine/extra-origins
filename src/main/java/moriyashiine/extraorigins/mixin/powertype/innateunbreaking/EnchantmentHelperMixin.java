/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.mixin.powertype.innateunbreaking;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.powertype.InnateUnbreakingPowerType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "getItemDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;forEachEnchantment(Lnet/minecraft/item/ItemStack;Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;)V"))
	private static void extraorigins$innateUnbreaking(ServerWorld world, ItemStack stack, int baseItemDamage, CallbackInfoReturnable<Integer> cir, @Local MutableFloat itemDamage) {
		if (InnateUnbreakingPowerType.cachedPlayer != null) {
			Enchantment unbreaking = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).get(Enchantments.UNBREAKING);
			if (unbreaking != null) {
				int level = 0;
				for (InnateUnbreakingPowerType powerType : PowerHolderComponent.getPowerTypes(InnateUnbreakingPowerType.cachedPlayer, InnateUnbreakingPowerType.class)) {
					if (powerType.isActive() && powerType.doesApply(world, stack)) {
						level += powerType.getLevel();
					}
				}
				if (level > 0) {
					unbreaking.modifyItemDamage(world, level, stack, itemDamage);
				}
			}
			InnateUnbreakingPowerType.cachedPlayer = null;
		}
	}
}
