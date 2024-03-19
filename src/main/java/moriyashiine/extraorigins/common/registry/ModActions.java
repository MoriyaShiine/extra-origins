/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.registry;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.function.Predicate;

public class ModActions {
	public static ActionFactory<Entity> BLOCK_ACTION_LOOKING_AT = new ActionFactory<>(ExtraOrigins.id("block_action_looking_at"), new SerializableData().add("block_condition", ApoliDataTypes.BLOCK_CONDITION).add("block_action", ApoliDataTypes.BLOCK_ACTION).add("entity_action", ApoliDataTypes.ENTITY_ACTION, null).add("entity_actions", ApoliDataTypes.ENTITY_ACTIONS, null),
			(data, entity) -> {
				if (entity instanceof LivingEntity living) {
					BlockHitResult hitResult = entity.getWorld().raycast(new RaycastContext(entity.getEyePos(), entity.getEyePos().add(entity.getRotationVector().multiply(ReachEntityAttributes.getReachDistance(living, entity instanceof PlayerEntity player && player.isCreative() ? 5 : 4.5))), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
					Predicate<CachedBlockPosition> blockCondition = data.get("block_condition");
					if (blockCondition.test(new CachedBlockPosition(entity.getWorld(), hitResult.getBlockPos(), false))) {
						//noinspection unchecked
						((ActionFactory<Triple<World, BlockPos, Direction>>.Instance) data.get("block_action")).accept(Triple.of(entity.getWorld(), hitResult.getBlockPos(), Direction.UP));
						List<ActionFactory<Entity>.Instance> entityActions = data.get("entity_actions");
						if (entityActions != null) {
							entityActions.forEach(action -> action.accept(entity));
						} else {
							ActionFactory<Entity>.Instance entityAction = data.get("entity_action");
							if (entityAction != null) {
								entityAction.accept(entity);
							}
						}
					}
				}
			});

	public static void init() {
		Registry.register(ApoliRegistries.ENTITY_ACTION, BLOCK_ACTION_LOOKING_AT.getSerializerId(), BLOCK_ACTION_LOOKING_AT);
	}
}
