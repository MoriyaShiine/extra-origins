/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.power.type;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.ItemCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import moriyashiine.extraorigins.common.init.ModPowerTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class InnateUnbreakingPowerType extends PowerType {
	public static final TypedDataObjectFactory<InnateUnbreakingPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
			new SerializableData()
					.add("item_condition", ItemCondition.DATA_TYPE.optional(), Optional.empty())
					.add("level", SerializableDataTypes.POSITIVE_INT),
			(data, condition) -> new InnateUnbreakingPowerType(
					condition,
					data.get("item_condition"),
					data.get("level")
			),
			(powerType, serializableData) -> serializableData.instance()
					.set("item_condition", powerType.itemCondition)
					.set("level", powerType.level)
	);

	@Nullable
	public static ServerPlayerEntity cachedPlayer = null;

	private final Optional<ItemCondition> itemCondition;
	private final int level;

	public InnateUnbreakingPowerType(Optional<EntityCondition> condition, Optional<ItemCondition> itemCondition, int level) {
		super(condition);
		this.itemCondition = itemCondition;
		this.level = level;
	}

	@Override
	public @NotNull PowerConfiguration<?> getConfig() {
		return ModPowerTypes.INNATE_UNBREAKING;
	}

	public boolean doesApply(World world, ItemStack stack) {
		return itemCondition.isEmpty() || itemCondition.get().test(world, stack);
	}

	public int getLevel() {
		return level;
	}
}
