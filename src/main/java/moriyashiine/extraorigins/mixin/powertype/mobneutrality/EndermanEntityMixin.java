/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.mixin.powertype.mobneutrality;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.powertype.MobNeutralityPowerType;
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
		return original && PowerHolderComponent.getPowerTypes(player, MobNeutralityPowerType.class).stream().noneMatch(powerType -> powerType.isActive() && powerType.shouldBeNeutral(this));
	}
}
