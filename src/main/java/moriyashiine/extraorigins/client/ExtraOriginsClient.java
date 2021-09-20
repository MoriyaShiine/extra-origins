package moriyashiine.extraorigins.client;

import moriyashiine.extraorigins.client.network.packet.StartRidingPacketS2C;
import moriyashiine.extraorigins.client.network.packet.StopRidingPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class ExtraOriginsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(StartRidingPacketS2C.ID, StartRidingPacketS2C::receive);
		ClientPlayNetworking.registerGlobalReceiver(StopRidingPacket.ID, StopRidingPacket::receive);
	}
}
