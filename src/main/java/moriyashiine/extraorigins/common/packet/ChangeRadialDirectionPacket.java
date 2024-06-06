/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common.packet;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.netty.buffer.Unpooled;
import moriyashiine.extraorigins.client.packet.MarkRadialDirectionChangedPacket;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.power.RadialMenuPower;
import moriyashiine.extraorigins.common.util.RadialMenuDirection;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Locale;

public class ChangeRadialDirectionPacket {
	public static final Identifier ID = ExtraOrigins.id("change_radial_direction");

	public static void send(RadialMenuDirection direction, PowerType<?> powerType) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeString(direction.toString().toUpperCase(Locale.ROOT));
		ApoliDataTypes.POWER_TYPE.send(buf, new PowerTypeReference<>(powerType.getIdentifier()));
		ClientPlayNetworking.send(ID, buf);
	}

	public static class Receiver implements ServerPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			String direction = buf.readString();
			PowerTypeReference<?> power = ApoliDataTypes.POWER_TYPE.receive(buf);
			server.execute(() -> {
				if (power.get(player) instanceof RadialMenuPower radialMenuPower) {
					radialMenuPower.setDirection(RadialMenuDirection.valueOf(direction));
					PowerHolderComponent.syncPower(player, radialMenuPower.getType());
					MarkRadialDirectionChangedPacket.send(player);
				}
			});
		}
	}
}
