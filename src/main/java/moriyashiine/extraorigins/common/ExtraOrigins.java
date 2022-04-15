/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.packet.ChangeRadialDirectionPacket;
import moriyashiine.extraorigins.common.packet.MountC2SPacket;
import moriyashiine.extraorigins.common.power.MountPower;
import moriyashiine.extraorigins.common.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;

public class ExtraOrigins implements ModInitializer {
	public static final String MOD_ID = "extraorigins";

	@Override
	public void onInitialize() {
		ModParticleTypes.init();
		ModSoundEvents.init();
		ModPowers.init();
		ModConditions.init();
		ModScaleTypes.init();
		initPackets();
		initEvents();
	}

	private void initPackets() {
		ServerPlayNetworking.registerGlobalReceiver(ChangeRadialDirectionPacket.ID, ChangeRadialDirectionPacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(MountC2SPacket.ID, MountC2SPacket::receive);
	}

	private void initEvents() {
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			if (PowerHolderComponent.hasPower(handler.player, MountPower.class) && handler.player.getVehicle() instanceof PlayerEntity) {
				handler.player.dismountVehicle();
			}
		});
	}
}
