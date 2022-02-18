/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import moriyashiine.extraorigins.common.registry.ModComponents;
import moriyashiine.extraorigins.common.util.MagicSporesOption;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.*;

public class MagicSporesComponent implements AutoSyncedComponent {
	private final PlayerEntity obj;
	private MagicSporesOption.BackwardsCompatibleMagicSporesMode mode = MagicSporesOption.BackwardsCompatibleMagicSporesMode.CONVERTED;

	public MagicSporesComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		setMode(MagicSporesOption.BackwardsCompatibleMagicSporesMode.valueOf(tag.getString("Mode").toUpperCase(Locale.ROOT)));
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putString("Mode", getMode().toString());
	}

	public MagicSporesOption.BackwardsCompatibleMagicSporesMode getMode() {
		return mode;
	}

	public void setMode(MagicSporesOption.BackwardsCompatibleMagicSporesMode mode) {
		this.mode = mode;
		ModComponents.MAGIC_SPORES.sync(obj);
	}
}
