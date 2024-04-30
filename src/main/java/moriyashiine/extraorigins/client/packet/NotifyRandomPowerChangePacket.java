/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.extraorigins.client.event.RandomPowerGranterClientEvent;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class NotifyRandomPowerChangePacket {
	public static final Identifier ID = ExtraOrigins.id("notify_random_power_change");

	public static void send(ServerPlayerEntity player, int index, Identifier oldPower, Identifier newPower) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(index);
		buf.writeIdentifier(oldPower);
		buf.writeIdentifier(newPower);
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			int index = buf.readInt();
			Identifier oldPower = buf.readIdentifier();
			Identifier newPower = buf.readIdentifier();
			client.execute(() -> {
				RandomPowerGranterClientEvent.DISPLAY_INSTANCES[index] = new RandomPowerGranterClientEvent.DisplayInstance(oldPower, newPower, 120);
			});
		}
	}
}
