/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common.init;

import moriyashiine.extraorigins.common.component.entity.RandomPowerGranterComponent;
import net.minecraft.entity.LivingEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModEntityComponents implements EntityComponentInitializer {
	public static final ComponentKey<RandomPowerGranterComponent> RANDOM_POWER_GRANTER = ComponentRegistry.getOrCreate(RandomPowerGranterComponent.RANDOM_POWER_GRANTER, RandomPowerGranterComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(LivingEntity.class, RANDOM_POWER_GRANTER).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(RandomPowerGranterComponent::new);
	}
}
