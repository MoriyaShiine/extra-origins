/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.client;

import moriyashiine.extraorigins.client.event.DelayedHitboxClientEvent;
import moriyashiine.extraorigins.client.event.RadialMenuEvents;
import moriyashiine.extraorigins.client.event.RandomPowerGranterClientEvent;
import moriyashiine.extraorigins.client.particle.SporeParticle;
import moriyashiine.extraorigins.client.payload.MarkRadialDirectionChangedPayload;
import moriyashiine.extraorigins.client.payload.NotifyRandomPowerChangePacket;
import moriyashiine.extraorigins.common.init.ModParticleTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ExtraOriginsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		initParticles();
		initEvents();
		initPayloads();
	}

	private void initParticles() {
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.OFFENSE_SPORE, SporeParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.DEFENSE_SPORE, SporeParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.MOBILITY_SPORE, SporeParticle.Factory::new);
	}

	private void initEvents() {
		RadialMenuEvents.init();
		ClientTickEvents.END_WORLD_TICK.register(new DelayedHitboxClientEvent());
		ClientTickEvents.END_WORLD_TICK.register(new RandomPowerGranterClientEvent.ClientTick());
		HudRenderCallback.EVENT.register(new RandomPowerGranterClientEvent.Hud());
	}

	private void initPayloads() {
		ClientPlayNetworking.registerGlobalReceiver(MarkRadialDirectionChangedPayload.ID, new MarkRadialDirectionChangedPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(NotifyRandomPowerChangePacket.ID, new NotifyRandomPowerChangePacket.Receiver());
	}
}
