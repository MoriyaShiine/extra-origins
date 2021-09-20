package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import moriyashiine.extraorigins.common.network.packet.StartRidingPacketC2S;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class StartRidingPower extends Power implements Active {
	private Key key;
	
	public StartRidingPower(PowerType<?> type, LivingEntity entity) {
		super(type, entity);
	}
	
	@Override
	public void onUse() {
		if (entity.world.isClient && entity instanceof ClientPlayerEntity) {
			MinecraftClient client = MinecraftClient.getInstance();
			if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
				StartRidingPacketC2S.send(((EntityHitResult) client.crosshairTarget).getEntity());
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
