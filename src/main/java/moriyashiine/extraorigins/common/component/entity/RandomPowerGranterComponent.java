/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.component.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerManager;
import io.github.apace100.apoli.power.type.PowerTypes;
import io.github.apace100.origins.Origins;
import moriyashiine.extraorigins.client.payload.NotifyRandomPowerChangePacket;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomPowerGranterComponent implements AutoSyncedComponent, CommonTickingComponent {
	public static final Identifier RANDOM_POWER_GRANTER = ExtraOrigins.id("random_power_granter");

	private static final List<Identifier> DISALLOWED_IDENTIFIERS = List.of(RANDOM_POWER_GRANTER, ExtraOrigins.id("rooted"), Origins.identifier("hunger_over_time"), Origins.identifier("invisibility"), Origins.identifier("phantomize"), Origins.identifier("phasing"), Origins.identifier("webbing"));

	private final LivingEntity obj;
	private final TemporaryPower[] temporaryPowers = new TemporaryPower[3];
	private boolean enabled = false;

	public RandomPowerGranterComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup wrapperLookup) {
		enabled = tag.getBoolean("Enabled");
		Arrays.fill(temporaryPowers, null);
		if (enabled) {
			NbtList list = tag.getList("Powers", NbtElement.COMPOUND_TYPE);
			for (int i = 0; i < list.size(); i++) {
				NbtCompound compound = list.getCompound(i);
				temporaryPowers[i] = new TemporaryPower(PowerManager.get(Identifier.of(compound.getString("Power"))), compound.getInt("Duration"), compound.getInt("MaxDuration"));
			}
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup wrapperLookup) {
		tag.putBoolean("Enabled", enabled);
		if (enabled) {
			NbtList list = new NbtList();
			for (TemporaryPower temporaryPower : temporaryPowers) {
				NbtCompound compound = new NbtCompound();
				compound.putString("Power", temporaryPower.power.getId().toString());
				compound.putInt("Duration", temporaryPower.duration);
				compound.putInt("MaxDuration", temporaryPower.maxDuration);
				list.add(compound);
			}
			tag.put("Powers", list);
		}
	}

	@Override
	public void tick() {
		if (enabled) {
			for (TemporaryPower temporaryPower : temporaryPowers) {
				if (temporaryPower.duration > 0) {
					temporaryPower.duration--;
				}
			}
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (enabled) {
			for (int i = 0; i < temporaryPowers.length; i++) {
				if (temporaryPowers[i].duration == 0) {
					randomizePower(i);
					sync();
				}
			}
		}
	}

	public void sync() {
		ModEntityComponents.RANDOM_POWER_GRANTER.sync(obj);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public TemporaryPower[] getTemporaryPowers() {
		return temporaryPowers;
	}

	public void initializePowers() {
		removePowers();
		enabled = true;
		givePower(getRandomPower(), 0);
		givePower(getRandomPower(), 1);
		givePower(getRandomPower(), 2);
	}

	public void removePowers() {
		enabled = false;
		Arrays.fill(temporaryPowers, null);
		PowerHolderComponent powerHolderComponent = PowerHolderComponent.KEY.get(obj);
		powerHolderComponent.removeAllPowersFromSource(RANDOM_POWER_GRANTER);
		powerHolderComponent.sync();
	}

	private Power getRandomPower() {
		List<Power> allPowers = new ArrayList<>();
		for (Power power : PowerManager.values()) {
			if (!PowerManager.isDisabled(power.getId()) && isPowerAllowed(power)) {
				allPowers.add(power);
			}
		}
		return allPowers.get(obj.getRandom().nextInt(allPowers.size()));
	}

	private void givePower(Power power, int index) {
		temporaryPowers[index] = new TemporaryPower(power, obj.getRandom().nextBetween(6000, 18000));
		PowerHolderComponent powerHolderComponent = PowerHolderComponent.KEY.get(obj);
		powerHolderComponent.addPower(power, RANDOM_POWER_GRANTER);
		powerHolderComponent.sync();
	}

	private void randomizePower(int index) {
		PowerHolderComponent.KEY.get(obj).removePower(temporaryPowers[index].getPower(), RANDOM_POWER_GRANTER);
		Identifier oldId = temporaryPowers[index].getPower().getId();
		givePower(getRandomPower(), index);
		if (obj instanceof ServerPlayerEntity player) {
			NotifyRandomPowerChangePacket.send(player, index, oldId, temporaryPowers[index].getPower().getId());
		}
	}

	private boolean isPowerAllowed(Power power) {
		for (TemporaryPower temporaryPower : temporaryPowers) {
			if (temporaryPower != null && temporaryPower.power == power) {
				return false;
			}
		}
		if (power.isHidden() || power.isSubPower()) {
			return false;
		}
		String name = power.getName().getString();
		if (name.startsWith("power.") && name.endsWith(".name")) {
			return false;
		}
		if (power.getType().getConfig() == PowerTypes.MODIFY_PLAYER_SPAWN) {
			return false;
		}
		return !DISALLOWED_IDENTIFIERS.contains(power.getId());
	}

	public static class TemporaryPower {
		private final Power power;
		private int duration;
		private final int maxDuration;

		public TemporaryPower(Power power, int duration, int maxDuration) {
			this.power = power;
			this.duration = duration;
			this.maxDuration = maxDuration;
		}

		public TemporaryPower(Power power, int duration) {
			this(power, duration, duration);
		}

		public Power getPower() {
			return power;
		}

		public float getProgress() {
			return (float) duration / maxDuration;
		}
	}
}
