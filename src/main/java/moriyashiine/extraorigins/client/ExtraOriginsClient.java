/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.client;

import moriyashiine.extraorigins.client.handler.MagicSporesHandler;
import moriyashiine.extraorigins.client.packet.DismountPacket;
import moriyashiine.extraorigins.client.packet.MarkSporeChangedPacket;
import moriyashiine.extraorigins.client.packet.MountS2CPacket;
import moriyashiine.extraorigins.client.particle.SporeParticle;
import moriyashiine.extraorigins.common.registry.ModParticleTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

@Environment(EnvType.CLIENT)
public class ExtraOriginsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MagicSporesHandler.init();
		initParticles();
		initPackets();
	}

	private void initParticles() {
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.OFFENSE_SPORE, SporeParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.DEFENSE_SPORE, SporeParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.MOBILITY_SPORE, SporeParticle.Factory::new);
	}

	private void initPackets() {
		ClientPlayNetworking.registerGlobalReceiver(MountS2CPacket.ID, MountS2CPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(DismountPacket.ID, DismountPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(MarkSporeChangedPacket.ID, MarkSporeChangedPacket::receive);
	}
}
