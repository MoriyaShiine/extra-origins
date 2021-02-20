package moriyashiine.extraorigins.common.registry;

import io.github.apace100.origins.power.Active;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import io.github.apace100.origins.power.PowerTypeReference;
import io.github.apace100.origins.power.factory.PowerFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.power.BoneMealPower;
import moriyashiine.extraorigins.common.power.LargeHeadPower;
import moriyashiine.extraorigins.common.power.ModifySizePower;
import moriyashiine.extraorigins.common.power.RegenerateHungerPower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class EOPowers {
	private static final Map<PowerFactory<?>, Identifier> POWER_FACTORIES = new LinkedHashMap<>();
	
	public static final PowerFactory<Power> BONE_MEAL = create(new PowerFactory<>(new Identifier(ExtraOrigins.MODID, "bone_meal"), new SerializableData().add("key", SerializableDataType.ACTIVE_KEY_TYPE, Active.KeyType.PRIMARY), data -> (type, player) -> {
		BoneMealPower power = new BoneMealPower(type, player);
		power.setKey((Active.KeyType) data.get("key"));
		return power;
	}).allowCondition());
	public static final PowerFactory<Power> REGENERATE_HUNGER = create(new PowerFactory<>(new Identifier(ExtraOrigins.MODID, "regenerate_hunger"), new SerializableData().add("amount", SerializableDataType.INT).add("chance", SerializableDataType.FLOAT), data -> (type, player) -> new RegenerateHungerPower(type, player, data.getInt("amount"), data.getFloat("chance"))).allowCondition());
	public static final PowerType<Power> PHOTOSYNTHESIS = new PowerTypeReference<>(new Identifier(ExtraOrigins.MODID, "photosynthesis"));
	public static final PowerType<Power> ABSORBING = new PowerTypeReference<>(new Identifier(ExtraOrigins.MODID, "absorbing"));
	
	public static final PowerType<Power> INORGANIC = new PowerTypeReference<>(new Identifier(ExtraOrigins.MODID, "inorganic"));
	public static final PowerType<Power> EFFECT_IMMUNITY = new PowerTypeReference<>(new Identifier(ExtraOrigins.MODID, "effect_immunity"));
	
	public static final PowerFactory<Power> MODIFY_SIZE = create(new PowerFactory<>(new Identifier(ExtraOrigins.MODID, "modify_size"), new SerializableData().add("scale", SerializableDataType.FLOAT), data -> (type, player) -> new ModifySizePower(type, player, data.getFloat("scale"))).allowCondition());
	public static final PowerFactory<Power> LARGE_HEAD = create(new PowerFactory<>(new Identifier(ExtraOrigins.MODID, "large_head"), new SerializableData(), data -> (BiFunction<PowerType<Power>, PlayerEntity, Power>) LargeHeadPower::new).allowCondition());
	
	public static final PowerType<Power> ALL_THAT_GLITTERS = new PowerTypeReference<>(new Identifier(ExtraOrigins.MODID, "all_that_glitters"));
	public static final PowerType<Power> PIGLIN_NEUTRALITY = new PowerTypeReference<>(new Identifier(ExtraOrigins.MODID, "piglin_neutrality"));
	public static final PowerType<Power> CROSSBOW_MASTER = new PowerTypeReference<>(new Identifier(ExtraOrigins.MODID, "crossbow_master"));
	public static final PowerType<Power> HOMESICK = new PowerTypeReference<>(new Identifier(ExtraOrigins.MODID, "homesick"));
	
	private static <T extends Power> PowerFactory<T> create(PowerFactory<T> factory) {
		POWER_FACTORIES.put(factory, factory.getSerializerId());
		return factory;
	}
	
	public static void init() {
		POWER_FACTORIES.keySet().forEach(powerType -> Registry.register(ModRegistries.POWER_FACTORY, POWER_FACTORIES.get(powerType), powerType));
	}
}
