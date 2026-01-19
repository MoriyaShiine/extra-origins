/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.init;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.EntityActionTypes;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.action.type.entity.BlockRaycastActionEntityActionType;

public class ModActionTypes {
	public static final ActionConfiguration<BlockRaycastActionEntityActionType> BLOCK_RAYCAST_ACTION = EntityActionTypes.register(ActionConfiguration.of(ExtraOrigins.id("block_raycast_action"), BlockRaycastActionEntityActionType.DATA_FACTORY));

	public static void init() {
	}
}
