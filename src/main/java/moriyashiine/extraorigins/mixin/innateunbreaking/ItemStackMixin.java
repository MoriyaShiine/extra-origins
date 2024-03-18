/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin.innateunbreaking;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.InnateUnbreakingPower;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@WrapOperation(method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I"))
	private int extraorigins$innateUnbreaking(Enchantment enchantment, ItemStack stack, Operation<Integer> original, int amount, Random random, @Nullable ServerPlayerEntity player) {
		int level = original.call(enchantment, stack);
		if (player != null) {
			for (InnateUnbreakingPower power : PowerHolderComponent.getPowers(player, InnateUnbreakingPower.class)) {
				if (power.doesApply((ItemStack) (Object) this) && power.isActive()) {
					level += power.getLevel();
				}
			}
		}
		return level;
	}
}
