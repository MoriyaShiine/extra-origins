package moriyashiine.extraorigins.common.registry;

import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class EOConditions {
	private static final Map<ConditionFactory<PlayerEntity>, Identifier> PLAYER_CONDITIONS = new LinkedHashMap<>();
	
	public static final ConditionFactory<PlayerEntity> PIGLIN_SAFE = createPlayerCondition(new ConditionFactory<>(new Identifier(ExtraOrigins.MODID, "piglin_safe"), new SerializableData(), (instance, playerEntity) -> playerEntity.world.getDimension().isPiglinSafe()));
	
	private static ConditionFactory<PlayerEntity> createPlayerCondition(ConditionFactory<PlayerEntity> factory) {
		PLAYER_CONDITIONS.put(factory, factory.getSerializerId());
		return factory;
	}
	
	public static void init() {
		PLAYER_CONDITIONS.keySet().forEach(condition -> Registry.register(ModRegistries.PLAYER_CONDITION, PLAYER_CONDITIONS.get(condition), condition));
	}
}
