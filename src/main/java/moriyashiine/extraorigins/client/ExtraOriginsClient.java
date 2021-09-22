package moriyashiine.extraorigins.client;

import moriyashiine.extraorigins.client.network.packet.MountS2CPacket;
import moriyashiine.extraorigins.client.network.packet.DismountPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class ExtraOriginsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(MountS2CPacket.ID, MountS2CPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(DismountPacket.ID, DismountPacket::receive);
	}
}
