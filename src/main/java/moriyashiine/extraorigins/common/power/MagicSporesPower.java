/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import moriyashiine.extraorigins.common.registry.ModComponents;
import moriyashiine.extraorigins.common.util.MagicSporeOption;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class MagicSporesPower extends Power implements Active {
	private Key key;
	public final Identifier spriteLocation;
	public final Consumer<Entity> upAction;
	public final Consumer<Entity> rightAction;
	public final Consumer<Entity> leftAction;
	private final Consumer<Entity> lostAction;
	public final int swapTime;
	private final MagicSporeOption defaultOption;
	public final boolean storeOption;

	private MagicSporeOption storedOption = MagicSporeOption.NONE;


	public MagicSporesPower(PowerType<?> type, LivingEntity entity, Identifier spriteLocation, Consumer<Entity> upAction, Consumer<Entity> rightAction, Consumer<Entity> leftAction, Consumer<Entity> lostAction, int swapTime, MagicSporeOption defaultOption, boolean storeOption) {
		super(type, entity);
		this.spriteLocation = spriteLocation;
		this.upAction = upAction;
		this.rightAction = rightAction;
		this.leftAction = leftAction;
		this.lostAction = lostAction;
		this.swapTime = swapTime;
		this.defaultOption = defaultOption;
		this.storeOption = storeOption;
	}

	@Override
	public void onUse() {
	}

	@Override
	public void onAdded() {
		convertToNewFormat();
	}

	@Override
	public void onGained() {
		update();
	}

	@Override
	public void onLost() {
		if (this.lostAction == null) return;
		this.lostAction.accept(entity);
	}

	@Override
	public Key getKey() {
		return key;
	}

	@Override
	public void setKey(Key key) {
		this.key = key;
	}

	public MagicSporeOption getStoredOption() {
		return this.storedOption;
	}

	public void setStoredOption(MagicSporeOption value) {
		this.storedOption = value;
	}

	private void update() {
		switch (this.defaultOption) {
			case LEFT, OFFENSE -> {
				leftAction.accept(entity);
				if (storeOption) {
					storedOption = MagicSporeOption.LEFT;
				}
			}
			case RIGHT, DEFENSE -> {
				rightAction.accept(entity);
				if (storeOption) {
					storedOption = MagicSporeOption.RIGHT;
				}
			}
			case UP, MOBILITY -> {
				upAction.accept(entity);
				if (storeOption) {
					storedOption = MagicSporeOption.UP;
				}
			}
		}
	}

	@Override
	public void fromTag(NbtElement tag) {
		if (!(tag instanceof NbtCompound)) return;
		switch (((NbtCompound) tag).getString("StoredOption")) {
			case "Left", "Offense" -> this.storedOption = MagicSporeOption.LEFT;
			case "Right", "Defense" -> this.storedOption = MagicSporeOption.RIGHT;
			case "Up", "Mobility" -> this.storedOption = MagicSporeOption.UP;
		}
	}

	@Override
	public NbtElement toTag() {
		NbtCompound nbt =  new NbtCompound();
		nbt.putString("StoredOption", this.storedOption.toString());
		return nbt;
	}

	private void convertToNewFormat() {
		ModComponents.MAGIC_SPORES.maybeGet(entity).ifPresent(magicSporesComponent -> {
			switch (magicSporesComponent.getMode()) {
				case OFFENSE -> {
					if (storeOption) {
						this.storedOption = MagicSporeOption.LEFT;
					}
					leftAction.accept(entity);
				}
				case DEFENSE -> {
				if (storeOption) {
						this.storedOption = MagicSporeOption.RIGHT;
				}
					rightAction.accept(entity);
				}
				case MOBILITY -> {
					if (storeOption) {
						this.storedOption = MagicSporeOption.UP;
					}
					upAction.accept(entity);
				}
			}
			magicSporesComponent.setMode(MagicSporeOption.BackwardsCompatibleMagicSporeMode.CONVERTED);
		});
	}
}
