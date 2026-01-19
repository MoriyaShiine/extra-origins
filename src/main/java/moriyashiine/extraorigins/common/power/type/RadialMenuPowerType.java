/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.power.type;

import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.Active;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.util.keybinding.KeyBindingReference;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.netty.buffer.ByteBuf;
import moriyashiine.extraorigins.common.init.ModPowerTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.ValueLists;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.IntFunction;

public class RadialMenuPowerType extends PowerType implements Active {
	public static final TypedDataObjectFactory<RadialMenuPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
			new SerializableData()
					.add("key", ApoliDataTypes.KEY)
					.add("sprite_location", SerializableDataTypes.IDENTIFIER)
					.add("up_action", EntityAction.DATA_TYPE.optional(), Optional.empty())
					.add("down_action", EntityAction.DATA_TYPE.optional(), Optional.empty())
					.add("left_action", EntityAction.DATA_TYPE.optional(), Optional.empty())
					.add("right_action", EntityAction.DATA_TYPE.optional(), Optional.empty())
					.add("lost_action", EntityAction.DATA_TYPE.optional(), Optional.empty())
					.add("swap_time", SerializableDataTypes.POSITIVE_INT, 20)
					.add("default_direction", SerializableDataType.enumValue(Direction.class), Direction.UP),
			(data, condition) -> new RadialMenuPowerType(
					condition,
					data.get("key"),
					data.get("sprite_location"),
					data.get("up_action"),
					data.get("down_action"),
					data.get("left_action"),
					data.get("right_action"),
					data.get("lost_action"),
					data.getInt("swap_time"),
					data.get("default_direction")
			),
			(powerType, serializableData) -> serializableData.instance()
					.set("key", powerType.key)
					.set("sprite_location", powerType.spriteLocation)
					.set("up_action", powerType.upAction)
					.set("down_action", powerType.downAction)
					.set("left_action", powerType.leftAction)
					.set("right_action", powerType.rightAction)
					.set("lost_action", powerType.lostAction)
					.set("swap_time", powerType.swapTime)
					.set("default_direction", powerType.direction)
	);

	private final KeyBindingReference key;
	public final Identifier spriteLocation;
	private final Optional<EntityAction> upAction;
	private final Optional<EntityAction> downAction;
	private final Optional<EntityAction> leftAction;
	private final Optional<EntityAction> rightAction;
	private final Optional<EntityAction> lostAction;
	public final int swapTime;

	private Direction direction;

	public RadialMenuPowerType(Optional<EntityCondition> condition, KeyBindingReference key, Identifier spriteLocation, Optional<EntityAction> upAction, Optional<EntityAction> downAction, Optional<EntityAction> leftAction, Optional<EntityAction> rightAction, Optional<EntityAction> lostAction, int swapTime, Direction defaultDirection) {
		super(condition);
		this.key = key;
		this.spriteLocation = spriteLocation;
		this.upAction = upAction;
		this.downAction = downAction;
		this.leftAction = leftAction;
		this.rightAction = rightAction;
		this.lostAction = lostAction;
		this.swapTime = swapTime;
		direction = defaultDirection;
	}

	@Override
	public @NotNull PowerConfiguration<?> getConfig() {
		return ModPowerTypes.RADIAL_MENU;
	}

	@Override
	public KeyBindingReference getKey() {
		return key;
	}

	@Override
	public void onUse() {
	}

	@Override
	public void onAdded() {
		update();
	}

	@Override
	public void onLost() {
		lostAction.ifPresent(action -> action.execute(getHolder()));
	}

	public Direction getDirection() {
		return this.direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
		update();
	}

	@Override
	public void fromTag(NbtElement tag) {
		if (tag instanceof NbtCompound nbt) {
			direction = Direction.valueOf(nbt.getString("Direction"));
		}
	}

	@Override
	public NbtElement toTag() {
		NbtCompound nbt = new NbtCompound();
		nbt.putString("Direction", direction.toString());
		return nbt;
	}

	public Optional<EntityAction> getActionFromDirection(Direction direction) {
		return switch (direction) {
			case UP -> upAction;
			case DOWN -> downAction;
			case LEFT -> leftAction;
			case RIGHT -> rightAction;
		};
	}

	private void update() {
		getActionFromDirection(direction).ifPresent(action -> action.execute(getHolder()));
	}

	public enum Direction {
		UP(0),
		DOWN(1),
		LEFT(2),
		RIGHT(3);

		private static final IntFunction<Direction> ID_TO_VALUE_FUNCTION = ValueLists.createIdToValueFunction(Direction::getId, values(), ValueLists.OutOfBoundsHandling.WRAP);
		public static final PacketCodec<ByteBuf, Direction> PACKET_CODEC = PacketCodecs.indexed(ID_TO_VALUE_FUNCTION, Direction::getId);

		private final int id;

		Direction(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}
}
