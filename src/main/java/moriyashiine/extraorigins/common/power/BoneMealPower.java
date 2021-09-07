package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import moriyashiine.extraorigins.common.network.packet.BoneMealPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class BoneMealPower extends Power implements Active {
	private final int exhaustion;
	
	private Key key;
	
	public BoneMealPower(PowerType<?> type, LivingEntity entity, int exhaustion) {
		super(type, entity);
		this.exhaustion = exhaustion;
	}
	
	@Override
	public void onUse() {
		if (entity.world.isClient && entity instanceof ClientPlayerEntity) {
			MinecraftClient client = MinecraftClient.getInstance();
			if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
				BoneMealPacket.send(((BlockHitResult) client.crosshairTarget).getBlockPos(), exhaustion);
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
