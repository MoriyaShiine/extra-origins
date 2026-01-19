/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.condition.type.block;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.BlockConditionContext;
import io.github.apace100.apoli.condition.type.BlockConditionType;
import moriyashiine.extraorigins.common.init.ModConditionTypes;
import net.minecraft.block.Fertilizable;
import org.jetbrains.annotations.NotNull;

public class FertilizableBlockConditionType extends BlockConditionType {
	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return ModConditionTypes.FERTILIZABLE;
	}

	@Override
	public boolean test(BlockConditionContext context) {
		return context.blockState().getBlock() instanceof Fertilizable fertilizable && fertilizable.isFertilizable(context.world(), context.pos(), context.blockState());
	}
}
