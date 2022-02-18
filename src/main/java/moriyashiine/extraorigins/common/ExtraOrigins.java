/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.packet.ChangeSporePacket;
import moriyashiine.extraorigins.common.packet.MountC2SPacket;
import moriyashiine.extraorigins.common.power.MountPower;
import moriyashiine.extraorigins.common.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtraOrigins implements ModInitializer {
	public static final String MOD_ID = "extraorigins";
	public static final Logger LOGGER = LogManager.getLogger(ExtraOrigins.class);

	@Override
	public void onInitialize() {
		ModParticleTypes.init();
		ModSoundEvents.init();
		ModScaleTypes.init();
		ModPowers.init();
		ModConditions.init();
		initPackets();
		initEvents();
	}

	private void initPackets() {
		ServerPlayNetworking.registerGlobalReceiver(ChangeSporePacket.ID, ChangeSporePacket::receive);
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
