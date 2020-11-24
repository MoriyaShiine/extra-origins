package moriyashiine.extraorigins.common;

import moriyashiine.extraorigins.common.registry.EOConditions;
import moriyashiine.extraorigins.common.registry.EOItems;
import moriyashiine.extraorigins.common.registry.EOPowers;
import net.fabricmc.api.ModInitializer;

public class ExtraOrigins implements ModInitializer {
	public static final String MODID = "extraorigins";
	
	@Override
	public void onInitialize() {
		EOItems.init();
		EOPowers.init();
		EOConditions.init();
	}
}
