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
import moriyashiine.extraorigins.common.power.MountPower;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.pehkui.api.ScaleRegistries;

import java.util.Collections;
import java.util.List;

public class EOPowers {
	public static final PowerFactory<Power> BONE_MEAL = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "bone_meal"), new SerializableData().add("exhaustion", SerializableDataTypes.INT, 0).add("key", ApoliDataTypes.KEY, new Active.Key()), data -> (type, entity) -> {
		BoneMealPower power = new BoneMealPower(type, entity, data.getInt("exhaustion"));
		power.setKey((Active.Key) data.get("key"));
		return power;
	}).allowCondition();
	
	public static final PowerFactory<Power> START_RIDING = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "start_riding"), new SerializableData().add("key", ApoliDataTypes.KEY, new Active.Key()), data -> (type, entity) -> {
		MountPower power = new MountPower(type, entity);
		power.setKey((Active.Key) data.get("key"));
		return power;
	}).allowCondition();
	
	@SuppressWarnings("unchecked")
	public static final PowerFactory<Power> MODIFY_SIZE = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "modify_size"), new SerializableData().add("scale_types", SerializableDataTypes.IDENTIFIERS, Collections.singletonList(ScaleRegistries.getId(ScaleRegistries.SCALE_TYPES, EOScaleTypes.MODIFY_SIZE_TYPE))).add("scale", SerializableDataTypes.FLOAT), data -> (type, entity) -> new ModifySizePower(type, entity, (List<Identifier>) data.get("scale_types"), data.getFloat("scale"))).allowCondition();
	
	@SuppressWarnings("unchecked")
	public static final PowerFactory<Power> MOB_NEUTRALITY = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "mob_neutrality"), new SerializableData().add("entity_types", SerializableDataType.list(SerializableDataTypes.ENTITY_TYPE)), data -> (type, entity) -> new MobNeutralityPower(type, entity, (List<EntityType<?>>) data.get("entity_types"))).allowCondition();
	
	public static final PowerType<Power> ALL_THAT_GLITTERS = new PowerTypeReference<>(new Identifier(ExtraOrigins.MOD_ID, "all_that_glitters"));
	
	public static final PowerType<Power> NIMBLE = new PowerTypeReference<>(new Identifier(ExtraOrigins.MOD_ID, "nimble"));
	
	public static void init() {
		Registry.register(ApoliRegistries.POWER_FACTORY, BONE_MEAL.getSerializerId(), BONE_MEAL);
		Registry.register(ApoliRegistries.POWER_FACTORY, START_RIDING.getSerializerId(), START_RIDING);
		Registry.register(ApoliRegistries.POWER_FACTORY, MODIFY_SIZE.getSerializerId(), MODIFY_SIZE);
		Registry.register(ApoliRegistries.POWER_FACTORY, MOB_NEUTRALITY.getSerializerId(), MOB_NEUTRALITY);
	}
}
