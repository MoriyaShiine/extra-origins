package moriyashiine.respawnablepets.client;

import moriyashiine.respawnablepets.client.network.message.SmokePuffMessage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

@Environment(EnvType.CLIENT)
public class RespawnablePetsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientSidePacketRegistry.INSTANCE.register(SmokePuffMessage.ID, SmokePuffMessage::handle);
	}
}
