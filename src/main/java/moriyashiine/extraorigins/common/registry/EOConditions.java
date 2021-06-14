package moriyashiine.extraorigins.common.registry;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class EOConditions {
	private static final Map<ConditionFactory<LivingEntity>, Identifier> ENTITY_CONDITIONS = new LinkedHashMap<>();
	private static final Map<ConditionFactory<Pair<DamageSource, Float>>, Identifier> DAMAGE_CONDITIONS = new LinkedHashMap<>();
	
	public static final ConditionFactory<LivingEntity> PIGLIN_SAFE = createEntityCondition(new ConditionFactory<>(new Identifier(ExtraOrigins.MODID, "piglin_safe"), new SerializableData(), (instance, entity) -> entity.world.getDimension().isPiglinSafe()));
	
	public static final ConditionFactory<Pair<DamageSource, Float>> CROSSBOW_ARROW = createDamageCondition(new ConditionFactory<>(new Identifier(ExtraOrigins.MODID, "crossbow_arrow"), new SerializableData(), (instance, damageSourceFloatPair) -> damageSourceFloatPair.getLeft().getSource() instanceof PersistentProjectileEntity projectile && projectile.isShotFromCrossbow()));
	
	private static ConditionFactory<LivingEntity> createEntityCondition(ConditionFactory<LivingEntity> factory) {
		ENTITY_CONDITIONS.put(factory, factory.getSerializerId());
		return factory;
	}
	
	private static ConditionFactory<Pair<DamageSource, Float>> createDamageCondition(ConditionFactory<Pair<DamageSource, Float>> factory) {
		DAMAGE_CONDITIONS.put(factory, factory.getSerializerId());
		return factory;
	}
	
	public static void init() {
		ENTITY_CONDITIONS.keySet().forEach(condition -> Registry.register(ApoliRegistries.ENTITY_CONDITION, ENTITY_CONDITIONS.get(condition), condition));
		DAMAGE_CONDITIONS.keySet().forEach(condition -> Registry.register(ApoliRegistries.DAMAGE_CONDITION, DAMAGE_CONDITIONS.get(condition), condition));
	}
}
