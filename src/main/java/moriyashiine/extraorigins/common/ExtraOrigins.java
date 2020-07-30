package moriyashiine.extraorigins.common;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.fabricmc.api.ModInitializer;

public class ExtraOrigins implements ModInitializer {
	@Override
	public void onInitialize() {
		EOPowers.init();
	}
}