/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.extraorigins.client.packet.MountS2CPacket;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MountC2SPacket implements ServerPlayNetworking.PlayChannelHandler {
	public static final Identifier ID = ExtraOrigins.id("mount_c2s");

	public static void send(Entity entity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(entity.getId());
		ClientPlayNetworking.send(ID, buf);
	}

	@Override
	public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		int id = buf.readInt();
		server.execute(() -> {
			Entity entity = player.getWorld().getEntityById(id);
			if (entity != null) {
				if (player.getUuid().equals(entity.getUuid())) {
					return;
				}
				player.startRiding(entity, true);
				if (entity instanceof ServerPlayerEntity playerBeingRidden) {
					MountS2CPacket.send(playerBeingRidden, player);
				}
			}
		});
	}
}
