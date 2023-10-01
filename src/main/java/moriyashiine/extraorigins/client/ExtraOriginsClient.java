/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.client;

import moriyashiine.extraorigins.client.event.RadialMenuEvents;
import moriyashiine.extraorigins.client.packet.DismountPacket;
import moriyashiine.extraorigins.client.packet.MarkRadialDirectionChangedPacket;
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
		initPackets();
		initParticles();
		RadialMenuEvents.init();
	}

	private void initPackets() {
		ClientPlayNetworking.registerGlobalReceiver(MountS2CPacket.ID, new MountS2CPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(DismountPacket.ID, new DismountPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(MarkRadialDirectionChangedPacket.ID, new MarkRadialDirectionChangedPacket.Receiver());
	}

	private void initParticles() {
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.OFFENSE_SPORE, SporeParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.DEFENSE_SPORE, SporeParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.MOBILITY_SPORE, SporeParticle.Factory::new);
	}
}
