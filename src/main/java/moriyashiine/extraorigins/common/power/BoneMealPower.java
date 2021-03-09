package moriyashiine.extraorigins.common.power;

import io.github.apace100.origins.power.Active;
import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import moriyashiine.extraorigins.common.network.packet.BoneMealPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class BoneMealPower extends Power implements Active {
	private Key key;
	
	public BoneMealPower(PowerType<?> type, PlayerEntity player) {
		super(type, player);
	}
	
	@Override
	public void onUse() {
		if (player.world.isClient) {
			MinecraftClient client = MinecraftClient.getInstance();
			if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
				BoneMealPacket.send(((BlockHitResult) client.crosshairTarget).getBlockPos());
			}
		}
	}
	
	@Override
	public Key getKey() {
		return key;
	}
	
	@Override
	public void setKey(Key key) {
		this.key = key;
	}
}
