package moriyashiine.extraorigins.common.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class CushionedHandler extends LivingEntity
{
	protected CushionedHandler(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
	private void isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> callbackInfo)
	{
		if (EOPowers.CUSHIONED.isActive(this) && (damageSource == DamageSource.FALL || damageSource == DamageSource.FLY_INTO_WALL))
		{
			callbackInfo.setReturnValue(true);
		}
	}
}