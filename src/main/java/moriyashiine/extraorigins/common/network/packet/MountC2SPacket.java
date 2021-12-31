/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.extraorigins.client.network.packet.MountS2CPacket;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MountC2SPacket {
	public static final Identifier ID = new Identifier(ExtraOrigins.MOD_ID, "mount_c2s");
	
	public static void send(Entity entity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(entity.getId());
		ClientPlayNetworking.send(ID, buf);
	}
	
	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		int id = buf.readInt();
		server.execute(() -> {
			Entity entity = player.world.getEntityById(id);
			if (entity != null) {
				player.startRiding(entity, true);
				if (entity instanceof ServerPlayerEntity playerBeingRidden) {
					MountS2CPacket.send(playerBeingRidden, player);
				}
			}
		});
	}
}
