package moriyashiine.extraorigins.common;

import moriyashiine.extraorigins.common.network.packet.BoneMealPacket;
import moriyashiine.extraorigins.common.registry.EOConditions;
import moriyashiine.extraorigins.common.registry.EOPowers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ExtraOrigins implements ModInitializer {
	public static final String MODID = "extraorigins";
	
	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(BoneMealPacket.ID, BoneMealPacket::handle);
		EOPowers.init();
		EOConditions.init();
	}
}
