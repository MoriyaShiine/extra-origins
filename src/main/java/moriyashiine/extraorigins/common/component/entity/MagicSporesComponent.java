/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MagicSporesPower;
import moriyashiine.extraorigins.common.registry.ModComponents;
import moriyashiine.extraorigins.common.registry.ModParticleTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class MagicSporesComponent implements AutoSyncedComponent, CommonTickingComponent {
	private static final Map<Predicate<PlayerEntity>, Set<Pair<EntityAttribute, EntityAttributeModifier>>> ATTRIBUTE_MAP;
	
	static {
		ATTRIBUTE_MAP = new HashMap<>();
		ATTRIBUTE_MAP.put(player -> ModComponents.MAGIC_SPORES.get(player).getMode() == Mode.OFFENSE, Set.of(new Pair<>(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(UUID.fromString("6005d677-d726-43e2-b1fa-ae2ce0fe4a01"), "Origin modifier", 0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)), new Pair<>(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(UUID.fromString("544f4632-3cee-482f-b50d-52b4b862ef4c"), "Origin modifier", -0.125, EntityAttributeModifier.Operation.MULTIPLY_TOTAL))));
		ATTRIBUTE_MAP.put(player -> ModComponents.MAGIC_SPORES.get(player).getMode() == Mode.DEFENSE, Set.of(new Pair<>(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(UUID.fromString("4ae0e72e-e72c-49e7-81c1-565108982501"), "Origin modifier", -0.125, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)), new Pair<>(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(UUID.fromString("47c0fe1b-7b74-444c-a532-e0b29e48ee3e"), "Origin modifier", -0.25, EntityAttributeModifier.Operation.MULTIPLY_TOTAL))));
		ATTRIBUTE_MAP.put(player -> ModComponents.MAGIC_SPORES.get(player).getMode() == Mode.MOBILITY, Set.of(new Pair<>(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(UUID.fromString("d586bb77-0d94-4e7f-bbb0-1f41571fdd1c"), "Origin modifier", -0.25, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)), new Pair<>(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(UUID.fromString("6b2451bf-26f6-4d48-87f7-4503ba01ecb9"), "Origin modifier", 0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL))));
	}
	
	private final PlayerEntity obj;
	private Mode mode = Mode.MOBILITY;
	
	public MagicSporesComponent(PlayerEntity obj) {
		this.obj = obj;
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
		setMode(Mode.valueOf(tag.getString("Mode").toUpperCase()));
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putString("Mode", getMode().toString());
	}
	
	@Override
	public void tick() {
		if (!PowerHolderComponent.hasPower(obj, MagicSporesPower.class)) {
			return;
		}
		obj.airStrafingSpeed *= getMode().airStrafingSpeedModifier;
		if (obj.world.isClient && obj.getRandom().nextBoolean()) {
			if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || !MinecraftClient.getInstance().player.getUuid().equals(obj.getUuid())) {
				obj.world.addParticle((ParticleEffect) ModParticleTypes.SPORE, obj.getParticleX(1), obj.getRandomBodyY(), obj.getParticleZ(1), getMode().red + MathHelper.nextDouble(obj.getRandom(), -0.05, 0.05), getMode().green + MathHelper.nextDouble(obj.getRandom(), -0.05, 0.05), getMode().blue + MathHelper.nextDouble(obj.getRandom(), -0.05, 0.05));
			}
		}
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
		ModComponents.MAGIC_SPORES.sync(obj);
	}
	
	public void updateAttributes(PlayerEntity obj) {
		boolean hasPower = PowerHolderComponent.hasPower(obj, MagicSporesPower.class);
		for (Predicate<PlayerEntity> predicate : ATTRIBUTE_MAP.keySet()) {
			for (Pair<EntityAttribute, EntityAttributeModifier> modifierPair : ATTRIBUTE_MAP.get(predicate)) {
				if (hasPower && predicate.test(obj)) {
					if (!obj.getAttributeInstance(modifierPair.getLeft()).hasModifier(modifierPair.getRight())) {
						obj.getAttributeInstance(modifierPair.getLeft()).addPersistentModifier(modifierPair.getRight());
					}
				}
				else {
					if (obj.getAttributeInstance(modifierPair.getLeft()).hasModifier(modifierPair.getRight())) {
						obj.getAttributeInstance(modifierPair.getLeft()).removeModifier(modifierPair.getRight());
					}
				}
			}
		}
	}
	
	public enum Mode {
		OFFENSE("Offense", 1.5F, 0.875F, 0.886, 0.071, 0.071), DEFENSE("Defense", 0.75F, 0.75F, 0.059, 0.624, 0.369), MOBILITY("Mobility", 1.25F, 1.5F, 0.125, 0.549, 0.576);
		
		private final String name;
		private final float damageTakenModifier, airStrafingSpeedModifier;
		private final double red, green, blue;
		
		Mode(String name, float damageTakenModifier, float airStrafingSpeedModifier, double red, double green, double blue) {
			this.name = name;
			this.damageTakenModifier = damageTakenModifier;
			this.airStrafingSpeedModifier = airStrafingSpeedModifier;
			this.red = red;
			this.green = green;
			this.blue = blue;
		}
		
		@Override
		public String toString() {
			return name;
		}
		
		public float getDamageTakenModifier() {
			return damageTakenModifier;
		}
	}
}
