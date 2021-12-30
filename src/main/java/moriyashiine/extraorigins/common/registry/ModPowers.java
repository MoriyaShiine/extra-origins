/*
 * All Rights Reserved (c) 2021 MoriyaShiine
 */

/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

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
import moriyashiine.extraorigins.common.power.MagicSporesPower;
import moriyashiine.extraorigins.common.power.MobNeutralityPower;
import moriyashiine.extraorigins.common.power.ModifySizePower;
import moriyashiine.extraorigins.common.power.MountPower;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.pehkui.api.ScaleRegistries;

import java.util.Collections;

public class ModPowers {
	public static final PowerFactory<Power> MAGIC_SPORES = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "magic_spores"), new SerializableData().add("key", ApoliDataTypes.KEY, new Active.Key()), data -> (type, entity) -> {
		MagicSporesPower power = new MagicSporesPower(type, entity);
		power.setKey(data.get("key"));
		return power;
	});
	
	public static final PowerFactory<Power> START_RIDING = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "start_riding"), new SerializableData().add("key", ApoliDataTypes.KEY, new Active.Key()), data -> (type, entity) -> {
		MountPower power = new MountPower(type, entity);
		power.setKey(data.get("key"));
		return power;
	}).allowCondition();
	
	public static final PowerFactory<Power> MODIFY_SIZE = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "modify_size"), new SerializableData().add("scale_types", SerializableDataTypes.IDENTIFIERS, Collections.singletonList(ScaleRegistries.getId(ScaleRegistries.SCALE_TYPES, ModScaleTypes.MODIFY_SIZE_TYPE))).add("scale", SerializableDataTypes.FLOAT), data -> (type, entity) -> new ModifySizePower(type, entity, data.get("scale_types"), data.getFloat("scale"))).allowCondition();
	
	public static final PowerFactory<Power> MOB_NEUTRALITY = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "mob_neutrality"), new SerializableData().add("entity_types", SerializableDataType.list(SerializableDataTypes.ENTITY_TYPE)), data -> (type, entity) -> new MobNeutralityPower(type, entity, data.get("entity_types"))).allowCondition();
	
	public static final PowerType<Power> DECOMPOSITION = new PowerTypeReference<>(new Identifier(ExtraOrigins.MOD_ID, "decomposition"));
	
	public static final PowerType<Power> NIMBLE = new PowerTypeReference<>(new Identifier(ExtraOrigins.MOD_ID, "nimble"));
	
	public static final PowerType<Power> ALL_THAT_GLITTERS = new PowerTypeReference<>(new Identifier(ExtraOrigins.MOD_ID, "all_that_glitters"));
	
	public static void init() {
		Registry.register(ApoliRegistries.POWER_FACTORY, MAGIC_SPORES.getSerializerId(), MAGIC_SPORES);
		Registry.register(ApoliRegistries.POWER_FACTORY, START_RIDING.getSerializerId(), START_RIDING);
		Registry.register(ApoliRegistries.POWER_FACTORY, MODIFY_SIZE.getSerializerId(), MODIFY_SIZE);
		Registry.register(ApoliRegistries.POWER_FACTORY, MOB_NEUTRALITY.getSerializerId(), MOB_NEUTRALITY);
	}
}
