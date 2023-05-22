/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.extraorigins.client.event.RadialMenuEvents;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MarkRadialDirectionChangedPacket {
	public static final Identifier ID = ExtraOrigins.id("mark_radial_direction_changed");

	public static void send(ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, ID, new PacketByteBuf(Unpooled.buffer()));
	}

	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		client.execute(() -> RadialMenuEvents.directionChanged = false);
	}
}
