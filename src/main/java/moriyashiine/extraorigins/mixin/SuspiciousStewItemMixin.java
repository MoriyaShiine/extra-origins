/*
 * All Rights Reserved (c) 2021 MoriyaShiine
 */

/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.ModPowers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SuspiciousStewItem.class)
public abstract class SuspiciousStewItemMixin {
	@ModifyVariable(method = "finishUsing", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getNbt()Lnet/minecraft/nbt/NbtCompound;"))
	private NbtCompound extraorigins$decomposeFoodEffects(NbtCompound obj, ItemStack stack, World world, LivingEntity user) {
		if (ModPowers.DECOMPOSITION.isActive(user)) {
			obj = null;
		}
		return obj;
	}
}
