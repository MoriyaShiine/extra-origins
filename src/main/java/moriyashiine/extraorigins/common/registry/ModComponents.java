/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.component.entity.MagicSporesComponent;
import net.minecraft.util.Identifier;

public class ModComponents implements EntityComponentInitializer {
	public static final ComponentKey<MagicSporesComponent> MAGIC_SPORES = ComponentRegistry.getOrCreate(new Identifier(ExtraOrigins.MOD_ID, "magic_spores"), MagicSporesComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(MAGIC_SPORES, MagicSporesComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
	}
}
