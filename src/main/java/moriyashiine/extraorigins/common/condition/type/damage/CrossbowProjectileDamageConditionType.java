/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.condition.type.damage;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.DamageConditionContext;
import io.github.apace100.apoli.condition.type.DamageConditionType;
import moriyashiine.extraorigins.common.init.ModConditionTypes;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.jetbrains.annotations.NotNull;

public class CrossbowProjectileDamageConditionType extends DamageConditionType {
	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return ModConditionTypes.CROSSBOW_PROJECTILE;
	}

	@Override
	public boolean test(DamageConditionContext context) {
		return context.source().getSource() instanceof PersistentProjectileEntity projectile && projectile.isShotFromCrossbow();
	}
}
