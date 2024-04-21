/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.init;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import moriyashiine.extraorigins.common.component.entity.RandomPowerGranterComponent;
import net.minecraft.entity.LivingEntity;

public class ModEntityComponents implements EntityComponentInitializer {
	public static final ComponentKey<RandomPowerGranterComponent> RANDOM_POWER_GRANTER = ComponentRegistry.getOrCreate(RandomPowerGranterComponent.RANDOM_POWER_GRANTER, RandomPowerGranterComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(LivingEntity.class, RANDOM_POWER_GRANTER).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(RandomPowerGranterComponent::new);
	}
}
