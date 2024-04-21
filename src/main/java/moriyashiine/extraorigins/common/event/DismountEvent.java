/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.event;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MountPower;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class DismountEvent implements ServerPlayConnectionEvents.Disconnect {
	@Override
	public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
		if (PowerHolderComponent.hasPower(handler.player, MountPower.class) && handler.player.getVehicle() instanceof PlayerEntity) {
			handler.player.dismountVehicle();
		}
	}
}
