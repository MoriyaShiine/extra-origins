package moriyashiine.extraorigins.client;

import moriyashiine.extraorigins.common.registry.EOItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;

@Environment(EnvType.CLIENT)
public class ExtraOriginsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ColorProviderRegistryImpl.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? 0xffff00 : 0xffffff, EOItems.LIQUID_SUNLIGHT);
	}
}
