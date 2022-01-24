/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.component.entity.MagicSporesComponent;
import moriyashiine.extraorigins.common.registry.ModComponents;
import moriyashiine.extraorigins.common.registry.ModSoundEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;

public class ChangeSporePacket {
	public static final Identifier ID = new Identifier(ExtraOrigins.MOD_ID, "change_spore");

	public static void send(MagicSporesComponent.Mode mode) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeString(mode.toString());
		ClientPlayNetworking.send(ID, buf);
	}

	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		MagicSporesComponent.Mode mode = MagicSporesComponent.Mode.valueOf(buf.readString().toUpperCase());
		server.execute(() -> ModComponents.MAGIC_SPORES.maybeGet(player).ifPresent(magicSporesComponent -> {
			magicSporesComponent.setMode(mode);
			magicSporesComponent.updateAttributes(player);
			player.world.playSoundFromEntity(null, player, ModSoundEvents.ENTITY_GENERIC_SPORE_SHIFT, SoundCategory.PLAYERS, 1, player.getSoundPitch());
		}));
	}
}
