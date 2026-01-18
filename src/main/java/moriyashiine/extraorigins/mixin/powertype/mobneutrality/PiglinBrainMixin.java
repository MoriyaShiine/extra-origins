/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.mixin.powertype.mobneutrality;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.powertype.MobNeutralityPowerType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {
	@WrapOperation(method = "onGuardedBlockInteracted", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getNonSpectatingEntities(Ljava/lang/Class;Lnet/minecraft/util/math/Box;)Ljava/util/List;"))
	private static List<?> extraorigins$mobNeutrality(World instance, Class<? extends Entity> aClass, Box box, Operation<List<? extends Entity>> original, PlayerEntity player) {
		List<? extends Entity> list = original.call(instance, aClass, box);
		for (int i = list.size() - 1; i >= 0; i--) {
			for (MobNeutralityPowerType power : PowerHolderComponent.getPowerTypes(player, MobNeutralityPowerType.class)) {
				if (power.isActive() && power.shouldBeNeutral(list.get(i))) {
					list.remove(i);
					break;
				}
			}
		}
		return list;
	}
}
