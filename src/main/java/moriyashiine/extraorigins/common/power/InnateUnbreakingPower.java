/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class InnateUnbreakingPower extends Power {
	private final Predicate<Pair<World, ItemStack>> itemCondition;
	private final int level;

	public InnateUnbreakingPower(PowerType<?> type, LivingEntity entity, Predicate<Pair<World, ItemStack>> itemCondition, int level) {
		super(type, entity);
		this.itemCondition = itemCondition;
		this.level = level;
	}

	public boolean doesApply(ItemStack stack) {
		return itemCondition == null || itemCondition.test(new Pair<>(entity.getWorld(), stack));
	}

	public int getLevel() {
		return level;
	}
}
