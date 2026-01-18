/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common.init;

import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.type.BlockConditionTypes;
import io.github.apace100.apoli.condition.type.DamageConditionTypes;
import io.github.apace100.apoli.condition.type.EntityConditionTypes;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.condition.type.block.FertilizableBlockConditionType;
import moriyashiine.extraorigins.common.condition.type.damage.CrossbowProjectileDamageConditionType;
import moriyashiine.extraorigins.common.condition.type.entity.PiglinSafeEntityConditionType;
import moriyashiine.extraorigins.common.condition.type.entity.RadialMenuDirectionEntityConditionType;

public class ModConditionTypes {
	public static final ConditionConfiguration<FertilizableBlockConditionType> FERTILIZABLE = BlockConditionTypes.register(ConditionConfiguration.simple(ExtraOrigins.id("fertilizable"), FertilizableBlockConditionType::new));

	public static final ConditionConfiguration<CrossbowProjectileDamageConditionType> CROSSBOW_PROJECTILE = DamageConditionTypes.register(ConditionConfiguration.simple(ExtraOrigins.id("crossbow_projectile"), CrossbowProjectileDamageConditionType::new));

	public static final ConditionConfiguration<PiglinSafeEntityConditionType> PIGLIN_SAFE = EntityConditionTypes.register(ConditionConfiguration.simple(ExtraOrigins.id("piglin_safe"), PiglinSafeEntityConditionType::new));
	public static final ConditionConfiguration<RadialMenuDirectionEntityConditionType> RADIAL_MENU_DIRECTION = EntityConditionTypes.register(ConditionConfiguration.of(ExtraOrigins.id("radial_menu_direction"), RadialMenuDirectionEntityConditionType.DATA_FACTORY));

	public static void init() {
	}
}
