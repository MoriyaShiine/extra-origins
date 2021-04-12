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
import moriyashiine.extraorigins.common.power.ModifySizePower;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class EOPowers {
	private static final Map<PowerFactory<?>, Identifier> POWER_FACTORIES = new LinkedHashMap<>();
	
	public static final PowerFactory<Power> BONE_MEAL = create(new PowerFactory<>(new Identifier(ExtraOrigins.MODID, "bone_meal"), new SerializableData().add("key", SerializableDataType.BACKWARDS_COMPATIBLE_KEY, new Active.Key()), data -> (type, player) -> {
		BoneMealPower power = new BoneMealPower(type, player);
		power.setKey((Active.Key) data.get("key"));
		return power;
	}).allowCondition());
	
	public static final PowerFactory<Power> MODIFY_SIZE = create(new PowerFactory<>(new Identifier(ExtraOrigins.MODID, "modify_size"), new SerializableData().add("scale", SerializableDataType.FLOAT), data -> (type, player) -> new ModifySizePower(type, player, data.getFloat("scale"))).allowCondition());
	
	public static final PowerType<Power> ALL_THAT_GLITTERS = new PowerTypeReference<>(new Identifier(ExtraOrigins.MODID, "all_that_glitters"));
	public static final PowerType<Power> PIGLIN_NEUTRALITY = new PowerTypeReference<>(new Identifier(ExtraOrigins.MODID, "piglin_neutrality"));
	public static final PowerType<Power> CROSSBOW_MASTER = new PowerTypeReference<>(new Identifier(ExtraOrigins.MODID, "crossbow_master"));
	
	private static <T extends Power> PowerFactory<T> create(PowerFactory<T> factory) {
		POWER_FACTORIES.put(factory, factory.getSerializerId());
		return factory;
	}
	
	public static void init() {
		POWER_FACTORIES.keySet().forEach(powerType -> Registry.register(ModRegistries.POWER_FACTORY, POWER_FACTORIES.get(powerType), powerType));
	}
}
