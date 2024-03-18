/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.registry;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.power.RadialMenuPower;
import moriyashiine.extraorigins.common.util.RadialMenuDirection;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class ModConditions {
	public static final ConditionFactory<CachedBlockPosition> FERTILIZABLE = new ConditionFactory<>(ExtraOrigins.id("fertilizable"), new SerializableData(), (instance, cachedBlockPosition) -> cachedBlockPosition.getBlockState().getBlock() instanceof Fertilizable);

	public static final ConditionFactory<Entity> RADIAL_MENU_DIRECTION = new ConditionFactory<>(ExtraOrigins.id("radial_menu_direction"), new SerializableData().add("direction", SerializableDataType.enumValue(RadialMenuDirection.class)).add("power", ApoliDataTypes.POWER_TYPE), (instance, entity) -> {
		if (((PowerTypeReference<?>) instance.get("power")).get(entity) instanceof RadialMenuPower radialMenuPower) {
			return radialMenuPower.isActive() && radialMenuPower.getDirection() == instance.get("direction");
		}
		return false;
	});
	public static final ConditionFactory<Entity> PIGLIN_SAFE = new ConditionFactory<>(ExtraOrigins.id("piglin_safe"), new SerializableData(), (instance, entity) -> entity.getWorld().getDimension().piglinSafe());

	public static final ConditionFactory<Pair<DamageSource, Float>> SHOT_FROM_CROSSBOW = new ConditionFactory<>(ExtraOrigins.id("shot_from_crossbow"), new SerializableData(), (instance, damageSourceFloatPair) -> damageSourceFloatPair.getLeft().getSource() instanceof PersistentProjectileEntity projectile && projectile.isShotFromCrossbow());

	public static void init() {
		Registry.register(ApoliRegistries.BLOCK_CONDITION, FERTILIZABLE.getSerializerId(), FERTILIZABLE);
		Registry.register(ApoliRegistries.ENTITY_CONDITION, RADIAL_MENU_DIRECTION.getSerializerId(), RADIAL_MENU_DIRECTION);
		Registry.register(ApoliRegistries.ENTITY_CONDITION, PIGLIN_SAFE.getSerializerId(), PIGLIN_SAFE);
		Registry.register(ApoliRegistries.DAMAGE_CONDITION, SHOT_FROM_CROSSBOW.getSerializerId(), SHOT_FROM_CROSSBOW);
	}
}
