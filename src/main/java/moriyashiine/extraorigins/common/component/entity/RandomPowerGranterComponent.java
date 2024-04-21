/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyPlayerSpawnPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.origins.Origins;
import moriyashiine.extraorigins.client.packet.NotifyRandomPowerChangePacket;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomPowerGranterComponent implements AutoSyncedComponent, CommonTickingComponent {
	public static final Identifier RANDOM_POWER_GRANTER = ExtraOrigins.id("random_power_granter");

	private static final List<Identifier> DISALLOWED_IDENTIFIERS = List.of(RANDOM_POWER_GRANTER, ExtraOrigins.id("rooted"), Origins.identifier("hunger_over_time"), Origins.identifier("invisibility"), Origins.identifier("phantomize"), Origins.identifier("phasing"), Origins.identifier("webbing"));

	private final LivingEntity obj;
	private final TemporaryPowerType[] temporaryPowerTypes = new TemporaryPowerType[3];
	private boolean enabled = false;

	public RandomPowerGranterComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		enabled = tag.getBoolean("Enabled");
		Arrays.fill(temporaryPowerTypes, null);
		if (enabled) {
			NbtList list = tag.getList("PowerTypes", NbtElement.COMPOUND_TYPE);
			for (int i = 0; i < list.size(); i++) {
				NbtCompound compound = list.getCompound(i);
				temporaryPowerTypes[i] = new TemporaryPowerType(PowerTypeRegistry.get(Identifier.tryParse(compound.getString("Identifier"))), compound.getInt("Duration"), compound.getInt("MaxDuration"));
			}
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("Enabled", enabled);
		if (enabled) {
			NbtList list = new NbtList();
			for (TemporaryPowerType temporaryPowerType : temporaryPowerTypes) {
				NbtCompound compound = new NbtCompound();
				compound.putString("Identifier", temporaryPowerType.powerType.getIdentifier().toString());
				compound.putInt("Duration", temporaryPowerType.duration);
				compound.putInt("MaxDuration", temporaryPowerType.maxDuration);
				list.add(compound);
			}
			tag.put("PowerTypes", list);
		}
	}

	@Override
	public void tick() {
		if (enabled) {
			for (TemporaryPowerType temporaryPowerType : temporaryPowerTypes) {
				if (temporaryPowerType.duration > 0) {
					temporaryPowerType.duration--;
				}
			}
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (enabled) {
			for (int i = 0; i < temporaryPowerTypes.length; i++) {
				if (temporaryPowerTypes[i].duration == 0) {
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

	public TemporaryPowerType[] getTemporaryPowerTypes() {
		return temporaryPowerTypes;
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
		Arrays.fill(temporaryPowerTypes, null);
		PowerHolderComponent powerHolderComponent = PowerHolderComponent.KEY.get(obj);
		powerHolderComponent.removeAllPowersFromSource(RANDOM_POWER_GRANTER);
		powerHolderComponent.sync();
	}

	private PowerType<?> getRandomPower() {
		List<PowerType<?>> allPowers = new ArrayList<>();
		for (PowerType<?> powerType : PowerTypeRegistry.values()) {
			if (isPowerAllowed(powerType)) {
				allPowers.add(powerType);
			}
		}
		return allPowers.get(obj.getRandom().nextInt(allPowers.size()));
	}

	private void givePower(PowerType<?> powerType, int index) {
		temporaryPowerTypes[index] = new TemporaryPowerType(powerType, obj.getRandom().nextBetween(6000, 18000));
		PowerHolderComponent powerHolderComponent = PowerHolderComponent.KEY.get(obj);
		powerHolderComponent.addPower(powerType, RANDOM_POWER_GRANTER);
		powerHolderComponent.sync();
	}

	private void randomizePower(int index) {
		PowerHolderComponent powerHolderComponent = PowerHolderComponent.KEY.get(obj);
		powerHolderComponent.removePower(temporaryPowerTypes[index].getPowerType(), RANDOM_POWER_GRANTER);
		powerHolderComponent.sync();
		Identifier oldId = temporaryPowerTypes[index].getPowerType().getIdentifier();
		givePower(getRandomPower(), index);
		if (obj instanceof ServerPlayerEntity player) {
			NotifyRandomPowerChangePacket.send(player, index, oldId, temporaryPowerTypes[index].getPowerType().getIdentifier());
		}
	}

	private boolean isPowerAllowed(PowerType<?> powerType) {
		for (TemporaryPowerType temporaryPowerType : temporaryPowerTypes) {
			if (temporaryPowerType != null && temporaryPowerType.powerType == powerType) {
				return false;
			}
		}
		if (powerType.isHidden() || powerType.isSubPower()) {
			return false;
		}
		String name = powerType.getName().getString();
		if (name.startsWith("power.") && name.endsWith(".name")) {
			return false;
		}
		if (powerType.create(null) instanceof ModifyPlayerSpawnPower) {
			return false;
		}
		return !DISALLOWED_IDENTIFIERS.contains(powerType.getIdentifier());
	}

	public static class TemporaryPowerType {
		private final PowerType<?> powerType;
		private int duration;
		private final int maxDuration;

		public TemporaryPowerType(PowerType<?> powerType, int duration, int maxDuration) {
			this.powerType = powerType;
			this.duration = duration;
			this.maxDuration = maxDuration;
		}

		public TemporaryPowerType(PowerType<?> powerType, int duration) {
			this(powerType, duration, duration);
		}

		public PowerType<?> getPowerType() {
			return powerType;
		}

		public float getProgress() {
			return (float) duration / maxDuration;
		}
	}
}
