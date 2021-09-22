package moriyashiine.extraorigins.client.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MountS2CPacket {
	public static final Identifier ID = new Identifier(ExtraOrigins.MODID, "mount_s2c");
	
	public static void send(ServerPlayerEntity player, Entity entity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(entity.getId());
		ServerPlayNetworking.send(player, ID, buf);
	}
	
	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		int id = buf.readInt();
		client.execute(new Runnable() {
			@Override
			public void run() {
				Entity entity = client.world.getEntityById(id);
				if (entity != null) {
					entity.startRiding(client.player, true);
				}
			}
		});
	}
}
