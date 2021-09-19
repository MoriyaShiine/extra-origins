package moriyashiine.extraorigins.common.network.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class BoneMealPacket {
	public static final Identifier ID = new Identifier(ExtraOrigins.MODID, "bone_meal");
	
	public static void send(BlockPos pos, int exhaustion) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeLong(pos.asLong());
		buf.writeInt(exhaustion);
		ClientPlayNetworking.send(ID, buf);
	}
	
	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler network, PacketByteBuf buf, PacketSender sender) {
		BlockPos pos = BlockPos.fromLong(buf.readLong());
		int exhaustion = buf.readInt();
		server.execute(() -> {
			if (exhaustion == 0 || player.getHungerManager().getFoodLevel() > 0) {
				BlockState state = player.world.getBlockState(pos);
				if (state.getBlock() instanceof Fertilizable fertilizable) {
					if (fertilizable.canGrow(player.world, player.getRandom(), pos, state)) {
						fertilizable.grow(player.getServerWorld(), player.getRandom(), pos, state);
						player.world.syncWorldEvent(2005, pos, 0);
						player.world.playSound(null, pos, SoundEvents.ITEM_BONE_MEAL_USE, SoundCategory.BLOCKS, 1, 1);
						player.swingHand(Hand.MAIN_HAND, true);
						if (exhaustion > 0) {
							player.addExhaustion(exhaustion);
						}
					}
				}
			}
		});
	}
}
