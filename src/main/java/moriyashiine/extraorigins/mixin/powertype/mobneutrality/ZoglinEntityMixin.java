/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.mixin.powertype.mobneutrality;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.powertype.MobNeutralityPowerType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZoglinEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ZoglinEntity.class)
public abstract class ZoglinEntityMixin extends LivingEntity {
	protected ZoglinEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyReturnValue(method = "shouldAttack", at = @At("RETURN"))
	private boolean extraorigins$mobNeutrality(boolean original, LivingEntity entity) {
		return original && PowerHolderComponent.getPowerTypes(entity, MobNeutralityPowerType.class).stream().noneMatch(powerType -> powerType.isActive() && powerType.shouldBeNeutral(this));
	}
}
