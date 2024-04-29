/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin.mobneutrality;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MobNeutralityPower;
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
		if (original) {
			for (MobNeutralityPower power : PowerHolderComponent.getPowers(entity, MobNeutralityPower.class)) {
				if (power.shouldBeNeutral(this) && power.isActive()) {
					return false;
				}
			}
		}
		return original;
	}
}
