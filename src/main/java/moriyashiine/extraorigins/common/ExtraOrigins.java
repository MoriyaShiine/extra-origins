/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common;

import moriyashiine.extraorigins.client.payload.MarkRadialDirectionChangedPayload;
import moriyashiine.extraorigins.client.payload.NotifyRandomPowerChangePacket;
import moriyashiine.extraorigins.common.event.RandomPowerGranterEvent;
import moriyashiine.extraorigins.common.init.*;
import moriyashiine.extraorigins.common.payload.ChangeRadialDirectionPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ExtraOrigins implements ModInitializer {
	public static final String MOD_ID = "extraorigins";

	@Override
	public void onInitialize() {
		ModParticleTypes.init();
		ModSoundEvents.init();
		ModActionTypes.init();
		ModConditionTypes.init();
		ModPowerTypes.init();
		initEvents();
		initPayloads();
	}

	public static Identifier id(String value) {
		return Identifier.of(MOD_ID, value);
	}

	private void initEvents() {
		ServerPlayerEvents.AFTER_RESPAWN.register(new RandomPowerGranterEvent());
	}

	private void initPayloads() {
		// client payloads
		PayloadTypeRegistry.playS2C().register(MarkRadialDirectionChangedPayload.ID, MarkRadialDirectionChangedPayload.CODEC);
		PayloadTypeRegistry.playS2C().register(NotifyRandomPowerChangePacket.ID, NotifyRandomPowerChangePacket.CODEC);
		// server payloads
		PayloadTypeRegistry.playC2S().register(ChangeRadialDirectionPayload.ID, ChangeRadialDirectionPayload.CODEC);
		// server receivers
		ServerPlayNetworking.registerGlobalReceiver(ChangeRadialDirectionPayload.ID, new ChangeRadialDirectionPayload.Receiver());
	}
}
