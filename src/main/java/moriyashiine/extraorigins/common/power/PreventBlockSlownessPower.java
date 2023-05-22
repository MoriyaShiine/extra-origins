/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PreventBlockSlownessPower extends Power {
	private final Set<Block> blocks = new HashSet<>();
	private final boolean inverted;

	public PreventBlockSlownessPower(PowerType<?> type, LivingEntity entity, List<Block> blocks, boolean inverted) {
		super(type, entity);
		this.blocks.addAll(blocks);
		this.inverted = inverted;
	}

	public boolean shouldPreventSlowness(Block block) {
		if (inverted) {
			return !blocks.contains(block);
		}
		return blocks.contains(block);
	}
}
