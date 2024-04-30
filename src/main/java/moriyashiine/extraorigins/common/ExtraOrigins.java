/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common;

import moriyashiine.extraorigins.common.event.DismountEvent;
import moriyashiine.extraorigins.common.event.RandomPowerGranterEvent;
import moriyashiine.extraorigins.common.init.*;
import moriyashiine.extraorigins.common.packet.ChangeRadialDirectionPacket;
import moriyashiine.extraorigins.common.packet.MountC2SPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ExtraOrigins implements ModInitializer {
	public static final String MOD_ID = "extraorigins";

	@Override
	public void onInitialize() {
		ModParticleTypes.init();
		ModSoundEvents.init();
		ModPowers.init();
		ModConditions.init();
		ModActions.init();
		ModScaleTypes.init();
		initPackets();
		initEvents();
	}

	public static Identifier id(String value) {
		return new Identifier(MOD_ID, value);
	}

	private void initPackets() {
		ServerPlayNetworking.registerGlobalReceiver(ChangeRadialDirectionPacket.ID, new ChangeRadialDirectionPacket.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(MountC2SPacket.ID, new MountC2SPacket.Receiver());
	}

	private void initEvents() {
		ServerPlayConnectionEvents.DISCONNECT.register(new DismountEvent());
		ServerPlayerEvents.AFTER_RESPAWN.register(new RandomPowerGranterEvent());
	}
}
