/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import moriyashiine.extraorigins.common.util.RadialMenuDirection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class RadialMenuPower extends Power implements Active {
	private Key key;
	public final Identifier spriteLocation;
	public final Consumer<Entity> upAction;
	public final Consumer<Entity> downAction;
	public final Consumer<Entity> leftAction;
	public final Consumer<Entity> rightAction;
	private final Consumer<Entity> lostAction;
	public final int swapTime;

	private RadialMenuDirection direction;

	private boolean shouldUpdate = false;

	public RadialMenuPower(PowerType<?> type, LivingEntity entity, Identifier spriteLocation, Consumer<Entity> upAction, Consumer<Entity> downAction, Consumer<Entity> leftAction, Consumer<Entity> rightAction, Consumer<Entity> lostAction, int swapTime, RadialMenuDirection defaultDirection) {
		super(type, entity);
		this.spriteLocation = spriteLocation;
		this.upAction = upAction;
		this.downAction = downAction;
		this.leftAction = leftAction;
		this.rightAction = rightAction;
		this.lostAction = lostAction;
		this.swapTime = swapTime;
		this.direction = defaultDirection;
		setTicking(true);
	}

	@Override
	public void onUse() {
	}

	@Override
	public void tick() {
		if (shouldUpdate) {
			update();
			shouldUpdate = false;
		}
	}

	@Override
	public void onAdded() {
		shouldUpdate = true;
	}

	@Override
	public void onLost() {
		if (lostAction != null) {
			lostAction.accept(entity);
		}
	}

	@Override
	public Key getKey() {
		return key;
	}

	@Override
	public void setKey(Key key) {
		this.key = key;
	}

	public RadialMenuDirection getDirection() {
		return this.direction;
	}

	public void setDirection(RadialMenuDirection direction) {
		this.direction = direction;
		shouldUpdate = true;
	}

	@Override
	public void fromTag(NbtElement tag) {
		if (tag instanceof NbtCompound nbt) {
			direction = RadialMenuDirection.valueOf(nbt.getString("Direction"));
		}
	}

	@Override
	public NbtElement toTag() {
		NbtCompound nbt = new NbtCompound();
		nbt.putString("Direction", direction.toString());
		return nbt;
	}

	public Consumer<Entity> getActionFromDirection(RadialMenuDirection direction) {
		return switch (direction) {
			case UP -> upAction;
			case DOWN -> downAction;
			case LEFT -> leftAction;
			case RIGHT -> rightAction;
		};
	}

	private void update() {
		Consumer<Entity> actionFromDirection = getActionFromDirection(direction);
		if (actionFromDirection != null) {
			actionFromDirection.accept(entity);
		}
	}
}
