/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.action.type.entity;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.BlockAction;
import io.github.apace100.apoli.action.EntityAction;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.condition.BlockCondition;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.util.MiscUtil;
import io.github.apace100.calio.data.SerializableData;
import moriyashiine.extraorigins.common.init.ModActionTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockRaycastActionEntityActionType extends EntityActionType {
	public static final TypedDataObjectFactory<BlockRaycastActionEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
			new SerializableData()
					.add("block_condition", BlockCondition.DATA_TYPE)
					.add("block_action", BlockAction.DATA_TYPE, null)
					.addFunctionedDefault("block_actions", BlockAction.DATA_TYPE.list(), data -> MiscUtil.singletonListOrNull(data.get("block_action")))
					.add("entity_action", EntityAction.DATA_TYPE, null)
					.addFunctionedDefault("entity_actions", EntityAction.DATA_TYPE.list(), data -> MiscUtil.singletonListOrNull(data.get("entity_action")))
					.validate(MiscUtil.validateAnyFieldsPresent("block_action", "block_actions"))
					.validate(MiscUtil.validateAnyFieldsPresent("entity_action", "entity_actions")),
			data -> new BlockRaycastActionEntityActionType(
					data.get("block_condition"),
					data.get("block_actions"),
					data.get("entity_actions")
			),
			(actionType, serializableData) -> serializableData.instance()
					.set("block_condition", actionType.blockCondition)
					.set("block_actions", actionType.blockActions)
					.set("entity_actions", actionType.entityActions)
	);

	private final BlockCondition blockCondition;
	private final List<BlockAction> blockActions;
	private final List<EntityAction> entityActions;

	public BlockRaycastActionEntityActionType(BlockCondition blockCondition, List<BlockAction> blockActions, List<EntityAction> entityActions) {
		this.blockCondition = blockCondition;
		this.blockActions = blockActions;
		this.entityActions = entityActions;
	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return ModActionTypes.BLOCK_RAYCAST_ACTION;
	}

	@Override
	public void accept(EntityActionContext context) {
		if (context.entity() instanceof PlayerEntity player) {
			BlockHitResult result = player.getWorld().raycast(new RaycastContext(player.getEyePos(), player.getEyePos().add(player.getRotationVector().multiply(player.getBlockInteractionRange())), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
			if (blockCondition.test(player.getWorld(), result.getBlockPos())) {
				blockActions.forEach(blockAction -> blockAction.execute(player.getWorld(), result.getBlockPos(), result.getSide()));
				entityActions.forEach(entityAction -> entityAction.execute(player));
			}
		}
	}
}
