/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.registry;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.*;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.power.*;
import moriyashiine.extraorigins.common.util.RadialMenuDirection;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import virtuoel.pehkui.api.ScaleRegistries;

import java.util.Collections;

public class ModPowers {
	public static final PowerFactory<Power> RADIAL_MENU = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "radial_menu"), new SerializableData()
			.add("key", ApoliDataTypes.KEY, new Active.Key())
			.add("sprite_location", SerializableDataTypes.IDENTIFIER)
			.add("up_action", ApoliDataTypes.ENTITY_ACTION, null)
			.add("down_action", ApoliDataTypes.ENTITY_ACTION, null)
			.add("left_action", ApoliDataTypes.ENTITY_ACTION, null)
			.add("right_action", ApoliDataTypes.ENTITY_ACTION, null)
			.add("lost_action", ApoliDataTypes.ENTITY_ACTION, null)
			.add("swap_time", SerializableDataTypes.INT, 20)
			.add("default_direction", SerializableDataType.enumValue(RadialMenuDirection.class), RadialMenuDirection.UP), data -> (type, entity) -> {
		RadialMenuPower power = new RadialMenuPower(type, entity, data.getId("sprite_location"), data.get("up_action"), data.get("down_action"), data.get("left_action"), data.get("right_action"), data.get("lost_action"), data.getInt("swap_time"), data.get("default_direction"));
		power.setKey(data.get("key"));
		return power;
	}).allowCondition();

	public static final PowerFactory<Power> FOOD_EFFECT_IMMUNITY = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "food_effect_immunity"), new SerializableData().add("effects", SerializableDataTypes.STATUS_EFFECTS, Collections.emptyList()).add("inverted", SerializableDataTypes.BOOLEAN, false), data -> (type, entity) -> new FoodEffectImmunityPower(type, entity, data.get("effects"), data.getBoolean("inverted"))).allowCondition();

	public static final PowerFactory<Power> MODIFY_SIZE = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "modify_size"), new SerializableData().add("scale_types", SerializableDataTypes.IDENTIFIERS, Collections.singletonList(ScaleRegistries.getId(ScaleRegistries.SCALE_TYPES, ModScaleTypes.MODIFY_SIZE_TYPE))).add("scale", SerializableDataTypes.FLOAT), data -> (type, entity) -> new ModifySizePower(type, entity, data.get("scale_types"), data.getFloat("scale"))).allowCondition();

	public static final PowerFactory<Power> PREVENT_BLOCK_SLOWNESS = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "prevent_block_slowness"), new SerializableData().add("blocks", SerializableDataType.list(SerializableDataTypes.BLOCK), Collections.emptyList()).add("inverted", SerializableDataTypes.BOOLEAN, false), data -> (type, entity) -> new PreventBlockSlownessPower(type, entity, data.get("blocks"), data.getBoolean("inverted"))).allowCondition();

	public static final PowerFactory<Power> CAN_WALK_ON_POWDER_SNOW = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "can_walk_on_powder_snow"), new SerializableData(), data -> (type, entity) -> new CanStandOnPowderSnowPower(type, entity)).allowCondition();

	public static final PowerFactory<Power> START_RIDING = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "start_riding"), new SerializableData().add("key", ApoliDataTypes.KEY, new Active.Key()), data -> (type, entity) -> {
		MountPower power = new MountPower(type, entity);
		power.setKey(data.get("key"));
		return power;
	}).allowCondition();

	public static final PowerFactory<Power> MOB_NEUTRALITY = new PowerFactory<>(new Identifier(ExtraOrigins.MOD_ID, "mob_neutrality"), new SerializableData().add("entity_types", SerializableDataType.list(SerializableDataTypes.ENTITY_TYPE), Collections.emptyList()).add("inverted", SerializableDataTypes.BOOLEAN, false), data -> (type, entity) -> new MobNeutralityPower(type, entity, data.get("entity_types"), data.getBoolean("inverted"))).allowCondition();

	public static final PowerFactory<Power> MODIFY_AIR_STRAFING_SPEED = ValueModifyingPower.createValueModifyingFactory(ModifyAirStrafingSpeedPower::new, new Identifier(ExtraOrigins.MOD_ID, "modify_air_strafing_speed"));

	public static final PowerType<Power> ALL_THAT_GLITTERS = new PowerTypeReference<>(new Identifier(ExtraOrigins.MOD_ID, "all_that_glitters"));

	public static void init() {
		Registry.register(ApoliRegistries.POWER_FACTORY, RADIAL_MENU.getSerializerId(), RADIAL_MENU);
		Registry.register(ApoliRegistries.POWER_FACTORY, FOOD_EFFECT_IMMUNITY.getSerializerId(), FOOD_EFFECT_IMMUNITY);
		Registry.register(ApoliRegistries.POWER_FACTORY, MODIFY_SIZE.getSerializerId(), MODIFY_SIZE);
		Registry.register(ApoliRegistries.POWER_FACTORY, PREVENT_BLOCK_SLOWNESS.getSerializerId(), PREVENT_BLOCK_SLOWNESS);
		Registry.register(ApoliRegistries.POWER_FACTORY, CAN_WALK_ON_POWDER_SNOW.getSerializerId(), CAN_WALK_ON_POWDER_SNOW);
		Registry.register(ApoliRegistries.POWER_FACTORY, START_RIDING.getSerializerId(), START_RIDING);
		Registry.register(ApoliRegistries.POWER_FACTORY, MOB_NEUTRALITY.getSerializerId(), MOB_NEUTRALITY);
		Registry.register(ApoliRegistries.POWER_FACTORY, MODIFY_AIR_STRAFING_SPEED.getSerializerId(), MODIFY_AIR_STRAFING_SPEED);
	}
}
