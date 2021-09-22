package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import moriyashiine.extraorigins.common.network.packet.MountC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class MountPower extends Power implements Active {
	private Key key;
	
	public MountPower(PowerType<?> type, LivingEntity entity) {
		super(type, entity);
	}
	
	@Override
	public void onUse() {
		if (entity.world.isClient && entity instanceof ClientPlayerEntity) {
			MinecraftClient client = MinecraftClient.getInstance();
			if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
				MountC2SPacket.send(((EntityHitResult) client.crosshairTarget).getEntity());
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
