/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.mixin.mobneutrality;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MobNeutralityPower;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends LivingEntity {
	protected EndermanEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyReturnValue(method = "isPlayerStaring", at = @At("RETURN"))
	private boolean extraorigins$mobNeutrality(boolean original, PlayerEntity player) {
		if (original) {
			for (MobNeutralityPower power : PowerHolderComponent.getPowers(player, MobNeutralityPower.class)) {
				if (power.shouldBeNeutral(this) && power.isActive()) {
					return false;
				}
			}
		}
		return original;
	}
}
