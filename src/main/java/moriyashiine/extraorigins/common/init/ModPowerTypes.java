/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.init;

import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.PowerTypes;
import io.github.apace100.apoli.power.type.ValueModifyingPowerType;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.component.entity.RandomPowerGranterComponent;
import moriyashiine.extraorigins.common.powertype.*;

public class ModPowerTypes {
	public static final PowerConfiguration<CanWalkOnPowderSnowPowerType> CAN_WALK_ON_POWDER_SNOW = PowerTypes.register(PowerConfiguration.conditionedSimple(ExtraOrigins.id("can_walk_on_powder_snow"), CanWalkOnPowderSnowPowerType::new));
	public static final PowerConfiguration<DelayedHitboxPowerType> DELAYED_HITBOX = PowerTypes.register(PowerConfiguration.of(ExtraOrigins.id("delayed_hitbox"), DelayedHitboxPowerType.DATA_FACTORY));
	public static final PowerConfiguration<FoodEffectImmunityPowerType> FOOD_EFFECT_IMMUNITY = PowerTypes.register(PowerConfiguration.of(ExtraOrigins.id("food_effect_immunity"), FoodEffectImmunityPowerType.DATA_FACTORY));
	public static final PowerConfiguration<InnateUnbreakingPowerType> INNATE_UNBREAKING = PowerTypes.register(PowerConfiguration.of(ExtraOrigins.id("innate_unbreaking"), InnateUnbreakingPowerType.DATA_FACTORY));
	public static final PowerConfiguration<MobNeutralityPowerType> MOB_NEUTRALITY = PowerTypes.register(PowerConfiguration.of(ExtraOrigins.id("mob_neutrality"), MobNeutralityPowerType.DATA_FACTORY));
	public static final PowerConfiguration<ModifyHostileDetectionRangePowerType> MODIFY_HOSTILE_DETECTION_RANGE = PowerTypes.register(ValueModifyingPowerType.createModifyingConfiguration(ExtraOrigins.id("modify_hostile_detection_range"), ModifyHostileDetectionRangePowerType::new));
	public static final PowerConfiguration<ModifyItemAttributesPowerType> MODIFY_ITEM_ATTRIBUTES = PowerTypes.register(PowerConfiguration.of(ExtraOrigins.id("modify_item_attributes"), ModifyItemAttributesPowerType.DATA_FACTORY));
	public static final PowerConfiguration<PreventBlockSlownessPowerType> PREVENT_BLOCK_SLOWNESS = PowerTypes.register(PowerConfiguration.conditionedSimple(ExtraOrigins.id("prevent_block_slowness"), PreventBlockSlownessPowerType::new));
	public static final PowerConfiguration<RadialMenuPowerType> RADIAL_MENU = PowerTypes.register(PowerConfiguration.of(ExtraOrigins.id("radial_menu"), RadialMenuPowerType.DATA_FACTORY));
	public static final PowerConfiguration<RandomPowerGranterPowerType> RANDOM_POWER_GRANTER = PowerTypes.register(PowerConfiguration.simple(RandomPowerGranterComponent.RANDOM_POWER_GRANTER, RandomPowerGranterPowerType::new));

	public static void init() {
	}
}
