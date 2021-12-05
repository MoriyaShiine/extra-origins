package moriyashiine.extraorigins.common;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.network.packet.MountC2SPacket;
import moriyashiine.extraorigins.common.power.MountPower;
import moriyashiine.extraorigins.common.registry.ModConditions;
import moriyashiine.extraorigins.common.registry.ModPowers;
import moriyashiine.extraorigins.common.registry.ModScaleTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;

public class ExtraOrigins implements ModInitializer {
	public static final String MOD_ID = "extraorigins";
	
	@Override
	public void onInitialize() {
		ModScaleTypes.init();
		ModPowers.init();
		ModConditions.init();
		ServerPlayNetworking.registerGlobalReceiver(MountC2SPacket.ID, MountC2SPacket::receive);
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			if (PowerHolderComponent.hasPower(handler.player, MountPower.class) && handler.player.getVehicle() instanceof PlayerEntity) {
				handler.player.dismountVehicle();
			}
		});
	}
}
