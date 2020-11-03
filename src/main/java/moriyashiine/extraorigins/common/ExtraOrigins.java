package moriyashiine.extraorigins.common;

import moriyashiine.extraorigins.common.registry.EOConditions;
import moriyashiine.extraorigins.common.registry.EOPowers;
import net.fabricmc.api.ModInitializer;

public class ExtraOrigins implements ModInitializer {
	public static final String MODID = "extraorigins";
	
	@Override
	public void onInitialize() {
		EOPowers.init();
		EOConditions.init();
	}
}
