/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.registry;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.ValueModifyingPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.power.*;
import moriyashiine.extraorigins.common.util.RadialMenuDirection;
import net.minecraft.registry.Registry;

import java.util.Collections;

public class ModPowers {
	public static final PowerFactory<?> CAN_WALK_ON_POWDER_SNOW = new PowerFactory<>(ExtraOrigins.id("can_walk_on_powder_snow"), new SerializableData(), data -> (type, entity) -> new CanStandOnPowderSnowPower(type, entity)).allowCondition();
	public static final PowerFactory<?> FOOD_EFFECT_IMMUNITY = new PowerFactory<>(ExtraOrigins.id("food_effect_immunity"), new SerializableData().add("effects", SerializableDataTypes.STATUS_EFFECTS, Collections.emptyList()).add("inverted", SerializableDataTypes.BOOLEAN, false), data -> (type, entity) -> new FoodEffectImmunityPower(type, entity, data.get("effects"), data.getBoolean("inverted"))).allowCondition();
	public static final PowerFactory<?> INNATE_UNBREAKING = new PowerFactory<>(ExtraOrigins.id("innate_unbreaking"), new SerializableData().add("level", SerializableDataTypes.INT).add("item_condition", ApoliDataTypes.ITEM_CONDITION, null), data -> (type, entity) -> new InnateUnbreakingPower(type, entity, data.getInt("level"), data.get("item_condition")));
	public static final PowerFactory<?> MOB_NEUTRALITY = new PowerFactory<>(ExtraOrigins.id("mob_neutrality"), new SerializableData().add("entity_types", SerializableDataType.list(SerializableDataTypes.ENTITY_TYPE), Collections.emptyList()).add("inverted", SerializableDataTypes.BOOLEAN, false), data -> (type, entity) -> new MobNeutralityPower(type, entity, data.get("entity_types"), data.getBoolean("inverted"))).allowCondition();
	public static final PowerFactory<?> MODIFY_AIR_STRAFING_SPEED = ValueModifyingPower.createValueModifyingFactory(ModifyAirStrafingSpeedPower::new, ExtraOrigins.id("modify_air_strafing_speed"));
	public static final PowerFactory<?> MODIFY_ITEM_ATTRIBUTE = ModifyItemAttributePower.createFactory(ExtraOrigins.id("modify_item_attribute"));
	public static final PowerFactory<?> MODIFY_SIZE = new PowerFactory<>(ExtraOrigins.id("modify_size"), new SerializableData().add("scale_types", SerializableDataTypes.IDENTIFIERS).add("scale", SerializableDataTypes.FLOAT), data -> (type, entity) -> new ModifySizePower(type, entity, data.get("scale_types"), data.getFloat("scale"))).allowCondition();
	public static final PowerFactory<?> PREVENT_BLOCK_SLOWNESS = new PowerFactory<>(ExtraOrigins.id("prevent_block_slowness"), new SerializableData().add("blocks", SerializableDataType.list(SerializableDataTypes.BLOCK), Collections.emptyList()).add("inverted", SerializableDataTypes.BOOLEAN, false), data -> (type, entity) -> new PreventBlockSlownessPower(type, entity, data.get("blocks"), data.getBoolean("inverted"))).allowCondition();
	public static final PowerFactory<?> RADIAL_MENU = new PowerFactory<>(ExtraOrigins.id("radial_menu"), new SerializableData()
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
	public static final PowerFactory<?> START_RIDING = new PowerFactory<>(ExtraOrigins.id("start_riding"), new SerializableData().add("key", ApoliDataTypes.KEY, new Active.Key()), data -> (type, entity) -> {
		MountPower power = new MountPower(type, entity);
		power.setKey(data.get("key"));
		return power;
	}).allowCondition();

	public static void init() {
		Registry.register(ApoliRegistries.POWER_FACTORY, CAN_WALK_ON_POWDER_SNOW.getSerializerId(), CAN_WALK_ON_POWDER_SNOW);
		Registry.register(ApoliRegistries.POWER_FACTORY, FOOD_EFFECT_IMMUNITY.getSerializerId(), FOOD_EFFECT_IMMUNITY);
		Registry.register(ApoliRegistries.POWER_FACTORY, INNATE_UNBREAKING.getSerializerId(), INNATE_UNBREAKING);
		Registry.register(ApoliRegistries.POWER_FACTORY, MOB_NEUTRALITY.getSerializerId(), MOB_NEUTRALITY);
		Registry.register(ApoliRegistries.POWER_FACTORY, MODIFY_AIR_STRAFING_SPEED.getSerializerId(), MODIFY_AIR_STRAFING_SPEED);
		Registry.register(ApoliRegistries.POWER_FACTORY, MODIFY_ITEM_ATTRIBUTE.getSerializerId(), MODIFY_ITEM_ATTRIBUTE);
		Registry.register(ApoliRegistries.POWER_FACTORY, MODIFY_SIZE.getSerializerId(), MODIFY_SIZE);
		Registry.register(ApoliRegistries.POWER_FACTORY, PREVENT_BLOCK_SLOWNESS.getSerializerId(), PREVENT_BLOCK_SLOWNESS);
		Registry.register(ApoliRegistries.POWER_FACTORY, RADIAL_MENU.getSerializerId(), RADIAL_MENU);
		Registry.register(ApoliRegistries.POWER_FACTORY, START_RIDING.getSerializerId(), START_RIDING);
	}
}
