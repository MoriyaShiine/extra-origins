package moriyashiine.extraorigins.common;

import moriyashiine.extraorigins.common.network.packet.BoneMealPacket;
import moriyashiine.extraorigins.common.registry.EOConditions;
import moriyashiine.extraorigins.common.registry.EOPowers;
import moriyashiine.extraorigins.common.registry.EOScaleTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ExtraOrigins implements ModInitializer {
	public static final String MODID = "extraorigins";
	
	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(BoneMealPacket.ID, BoneMealPacket::handle);
		EOScaleTypes.init();
		EOPowers.init();
		EOConditions.init();
	}
}
