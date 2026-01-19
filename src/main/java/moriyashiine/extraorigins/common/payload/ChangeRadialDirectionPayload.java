/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.payload;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.client.payload.MarkRadialDirectionChangedPayload;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.power.type.RadialMenuPowerType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ChangeRadialDirectionPayload(RadialMenuPowerType.Direction direction,
										   Identifier power) implements CustomPayload {
	public static final Id<ChangeRadialDirectionPayload> ID = new Id<>(ExtraOrigins.id("change_radial_direction"));
	public static final PacketCodec<RegistryByteBuf, ChangeRadialDirectionPayload> CODEC = PacketCodec.tuple(RadialMenuPowerType.Direction.PACKET_CODEC, ChangeRadialDirectionPayload::direction, Identifier.PACKET_CODEC, ChangeRadialDirectionPayload::power, ChangeRadialDirectionPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}

	public static void send(RadialMenuPowerType.Direction direction, Identifier power) {
		ClientPlayNetworking.send(new ChangeRadialDirectionPayload(direction, power));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<ChangeRadialDirectionPayload> {
		@Override
		public void receive(ChangeRadialDirectionPayload payload, ServerPlayNetworking.Context context) {
			PowerHolderComponent.getPowerTypes(context.player(), RadialMenuPowerType.class).stream().filter(powerType -> powerType.getPower().getId().equals(payload.power())).findFirst().ifPresent(powerType -> {
				powerType.setDirection(payload.direction());
				PowerHolderComponent.syncPower(context.player(), powerType.getPower());
				MarkRadialDirectionChangedPayload.send(context.player());
			});
		}
	}
}
