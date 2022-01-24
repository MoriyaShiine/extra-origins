/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.registry;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.component.entity.MagicSporesComponent;
import moriyashiine.extraorigins.common.power.MagicSporesPower;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

public class ModConditions {
	public static final ConditionFactory<CachedBlockPosition> FERTILIZABLE = new ConditionFactory<>(new Identifier(ExtraOrigins.MOD_ID, "fertilizable"), new SerializableData(), (instance, cachedBlockPosition) -> cachedBlockPosition.getBlockState().getBlock() instanceof Fertilizable);

	public static final ConditionFactory<Entity> MAGIC_SPORES_MODE = new ConditionFactory<>(new Identifier(ExtraOrigins.MOD_ID, "magic_spores_mode"), new SerializableData().add("mode", SerializableDataTypes.STRING), (instance, entity) -> entity instanceof PlayerEntity player && PowerHolderComponent.hasPower(player, MagicSporesPower.class) && ModComponents.MAGIC_SPORES.get(player).getMode() == MagicSporesComponent.Mode.valueOf(instance.getString("mode").toUpperCase()));
	public static final ConditionFactory<Entity> PIGLIN_SAFE = new ConditionFactory<>(new Identifier(ExtraOrigins.MOD_ID, "piglin_safe"), new SerializableData(), (instance, entity) -> entity.world.getDimension().isPiglinSafe());

	public static final ConditionFactory<Pair<DamageSource, Float>> CROSSBOW_ARROW = new ConditionFactory<>(new Identifier(ExtraOrigins.MOD_ID, "crossbow_arrow"), new SerializableData(), (instance, damageSourceFloatPair) -> damageSourceFloatPair.getLeft().getSource() instanceof PersistentProjectileEntity projectile && projectile.isShotFromCrossbow());

	public static void init() {
		Registry.register(ApoliRegistries.BLOCK_CONDITION, FERTILIZABLE.getSerializerId(), FERTILIZABLE);
		Registry.register(ApoliRegistries.ENTITY_CONDITION, MAGIC_SPORES_MODE.getSerializerId(), MAGIC_SPORES_MODE);
		Registry.register(ApoliRegistries.ENTITY_CONDITION, PIGLIN_SAFE.getSerializerId(), PIGLIN_SAFE);
		Registry.register(ApoliRegistries.DAMAGE_CONDITION, CROSSBOW_ARROW.getSerializerId(), CROSSBOW_ARROW);
	}
}
