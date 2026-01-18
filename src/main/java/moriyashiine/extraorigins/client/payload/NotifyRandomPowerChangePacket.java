/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.client.payload;

import moriyashiine.extraorigins.client.event.RandomPowerGranterClientEvent;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record NotifyRandomPowerChangePacket(int index, Identifier oldPower,
											Identifier newPower) implements CustomPayload {
	public static final Id<NotifyRandomPowerChangePacket> ID = new Id<>(ExtraOrigins.id("notify_random_power_change"));
	public static final PacketCodec<RegistryByteBuf, NotifyRandomPowerChangePacket> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, NotifyRandomPowerChangePacket::index, Identifier.PACKET_CODEC, NotifyRandomPowerChangePacket::oldPower, Identifier.PACKET_CODEC, NotifyRandomPowerChangePacket::newPower, NotifyRandomPowerChangePacket::new);

	public static void send(ServerPlayerEntity player, int index, Identifier oldPower, Identifier newPower) {
		ServerPlayNetworking.send(player, new NotifyRandomPowerChangePacket(index, oldPower, newPower));
	}

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<NotifyRandomPowerChangePacket> {
		@Override
		public void receive(NotifyRandomPowerChangePacket payload, ClientPlayNetworking.Context context) {
			RandomPowerGranterClientEvent.DISPLAY_INSTANCES[payload.index()] = new RandomPowerGranterClientEvent.DisplayInstance(payload.oldPower(), payload.newPower(), 120);
		}
	}
}
