package moriyashiine.extraorigins.common.registry;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.power.BoneMealPower;
import moriyashiine.extraorigins.common.power.MobNeutralityPower;
import moriyashiine.extraorigins.common.power.ModifySizePower;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.pehkui.api.ScaleRegistries;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EOPowers {
	private static final Map<PowerFactory<?>, Identifier> POWER_FACTORIES = new LinkedHashMap<>();
	
	public static final PowerFactory<Power> BONE_MEAL = create(new PowerFactory<>(new Identifier(ExtraOrigins.MODID, "bone_meal"), new SerializableData().add("key", ApoliDataTypes.KEY), data -> (type, entity) -> {
		BoneMealPower power = new BoneMealPower(type, entity);
		power.setKey((Active.Key) data.get("key"));
		return power;
	}).allowCondition());
	
	@SuppressWarnings("unchecked")
	public static final PowerFactory<Power> MODIFY_SIZE = create(new PowerFactory<>(new Identifier(ExtraOrigins.MODID, "modify_size"), new SerializableData().add("scale_types", SerializableDataTypes.IDENTIFIERS, Collections.singletonList(ScaleRegistries.getId(ScaleRegistries.SCALE_TYPES, EOScaleTypes.MODIFY_SIZE_TYPE))).add("scale", SerializableDataTypes.FLOAT), data -> (type, entity) -> new ModifySizePower(type, entity, (List<Identifier>) data.get("scale_types"), data.getFloat("scale"))).allowCondition());
	
	@SuppressWarnings("unchecked")
	public static final PowerFactory<Power> MOB_NEUTRALITY = create(new PowerFactory<>(new Identifier(ExtraOrigins.MODID, "mob_neutrality"), new SerializableData().add("entity_types", SerializableDataType.list(SerializableDataTypes.ENTITY_TYPE)), data -> (type, entity) -> new MobNeutralityPower(type, entity, (List<EntityType<?>>) data.get("entity_types"))).allowCondition());
	
	public static final PowerType<Power> ALL_THAT_GLITTERS = new PowerTypeReference<>(new Identifier(ExtraOrigins.MODID, "all_that_glitters"));
	
	private static <T extends Power> PowerFactory<T> create(PowerFactory<T> factory) {
		POWER_FACTORIES.put(factory, factory.getSerializerId());
		return factory;
	}
	
	public static void init() {
		POWER_FACTORIES.keySet().forEach(powerType -> Registry.register(ApoliRegistries.POWER_FACTORY, POWER_FACTORIES.get(powerType), powerType));
	}
}
