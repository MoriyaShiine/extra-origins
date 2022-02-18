/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.packet;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.netty.buffer.Unpooled;
import moriyashiine.extraorigins.client.packet.MarkSporeChangedPacket;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.power.MagicSporesPower;
import moriyashiine.extraorigins.common.util.MagicSporeOption;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Locale;

public class ChangeSporePacket {
	public static final Identifier ID = new Identifier(ExtraOrigins.MOD_ID, "change_spore");

	public static void send(MagicSporeOption mode, PowerType<?> powerType) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeString(mode.toString());
		ApoliDataTypes.POWER_TYPE.send(buf, new PowerTypeReference<>(powerType.getIdentifier()));
		ClientPlayNetworking.send(ID, buf);
	}

	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		MagicSporeOption option = MagicSporeOption.valueOf(buf.readString().toUpperCase(Locale.ROOT));
		PowerTypeReference<?> power = ApoliDataTypes.POWER_TYPE.receive(buf);
		if (!(power.get(player) instanceof MagicSporesPower magicSporesPower)) return;
		switch (option) {
			case LEFT -> {
				if (magicSporesPower.storeOption) {
					magicSporesPower.setStoredOption(MagicSporeOption.LEFT);
					PowerHolderComponent.syncPower(player, magicSporesPower.getType());
				}
				magicSporesPower.leftAction.accept(player);
			}
			case RIGHT -> {
				if (magicSporesPower.storeOption) {
					magicSporesPower.setStoredOption(MagicSporeOption.RIGHT);
					PowerHolderComponent.syncPower(player, magicSporesPower.getType());
				}
				magicSporesPower.rightAction.accept(player);
			}
			case UP -> {
				if (magicSporesPower.storeOption) {
					magicSporesPower.setStoredOption(MagicSporeOption.UP);
					PowerHolderComponent.syncPower(player, magicSporesPower.getType());
				}
				magicSporesPower.upAction.accept(player);
			}
		}
		MarkSporeChangedPacket.send(player);
	}
}
