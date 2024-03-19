/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.packet.ChangeRadialDirectionPacket;
import moriyashiine.extraorigins.common.packet.MountC2SPacket;
import moriyashiine.extraorigins.common.power.MountPower;
import moriyashiine.extraorigins.common.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
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
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			if (PowerHolderComponent.hasPower(handler.player, MountPower.class) && handler.player.getVehicle() instanceof PlayerEntity) {
				handler.player.dismountVehicle();
			}
		});
	}
}
