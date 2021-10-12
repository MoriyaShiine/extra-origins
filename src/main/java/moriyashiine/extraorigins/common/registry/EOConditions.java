package moriyashiine.extraorigins.common.registry;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

public class EOConditions {
	public static final ConditionFactory<Entity> PIGLIN_SAFE = new ConditionFactory<>(new Identifier(ExtraOrigins.MODID, "piglin_safe"), new SerializableData(), (instance, entity) -> entity.world.getDimension().isPiglinSafe());
	
	public static final ConditionFactory<Pair<DamageSource, Float>> CROSSBOW_ARROW = new ConditionFactory<>(new Identifier(ExtraOrigins.MODID, "crossbow_arrow"), new SerializableData(), (instance, damageSourceFloatPair) -> damageSourceFloatPair.getLeft().getSource() instanceof PersistentProjectileEntity projectile && projectile.isShotFromCrossbow());
	
	public static void init() {
		Registry.register(ApoliRegistries.ENTITY_CONDITION, PIGLIN_SAFE.getSerializerId(), PIGLIN_SAFE);
		Registry.register(ApoliRegistries.DAMAGE_CONDITION, CROSSBOW_ARROW.getSerializerId(), CROSSBOW_ARROW);
	}
}
