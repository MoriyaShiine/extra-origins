/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.client.payload;

import moriyashiine.extraorigins.client.event.RadialMenuEvents;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record MarkRadialDirectionChangedPayload() implements CustomPayload {
	public static final Id<MarkRadialDirectionChangedPayload> ID = new Id<>(ExtraOrigins.id("mark_radial_direction_changed"));
	public static final PacketCodec<PacketByteBuf, MarkRadialDirectionChangedPayload> CODEC = PacketCodec.unit(new MarkRadialDirectionChangedPayload());

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, new MarkRadialDirectionChangedPayload());
	}

	public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<MarkRadialDirectionChangedPayload> {
		@Override
		public void receive(MarkRadialDirectionChangedPayload payload, ClientPlayNetworking.Context context) {
			RadialMenuEvents.directionChanged = false;
		}
	}
}
