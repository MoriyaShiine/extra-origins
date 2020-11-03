package moriyashiine.extraorigins.common.registry;

import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class EOConditions {
	private static final Map<ConditionFactory<Pair<DamageSource, Float>>, Identifier> DAMAGE_CONDITIONS = new LinkedHashMap<>();
	
	public static final ConditionFactory<Pair<DamageSource, Float>> THORNS_ENCHANTMENT = createDamageCondition(new ConditionFactory<>(new Identifier(ExtraOrigins.MODID, "thorns_enchantment"), new SerializableData(), (data, dmg) -> dmg.getLeft() instanceof EntityDamageSource && ((EntityDamageSource) dmg.getLeft()).isThorns()));
	
	private static ConditionFactory<Pair<DamageSource, Float>> createDamageCondition(ConditionFactory<Pair<DamageSource, Float>> factory) {
		DAMAGE_CONDITIONS.put(factory, factory.getSerializerId());
		return factory;
	}
	
	public static void init() {
		DAMAGE_CONDITIONS.keySet().forEach(condition -> Registry.register(ModRegistries.DAMAGE_CONDITION, DAMAGE_CONDITIONS.get(condition), condition));
	}
}
