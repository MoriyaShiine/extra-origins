package moriyashiine.extraorigins.mixin;

import io.github.apace100.origins.component.OriginComponent;
import moriyashiine.extraorigins.common.power.ModifySizePower;
import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@ModifyVariable(method = "damage", at = @At("HEAD"))
	private float damage(float amount, DamageSource source) {
		Entity attacker = source.getAttacker();
		if (EOPowers.CROSSBOW_MASTER.isActive(attacker)) {
			Entity projectile = source.getSource();
			if (projectile instanceof PersistentProjectileEntity && ((PersistentProjectileEntity) projectile).isShotFromCrossbow()) {
				amount *= 1.75f;
			}
		}
		return amount;
	}
	
	@Inject(method = "getGroup", at = @At("HEAD"), cancellable = true)
	private void getGroup(CallbackInfoReturnable<EntityGroup> callbackInfo) {
		if (EOPowers.HOMESICK.isActive(this) && !world.getDimension().isPiglinSafe()) {
			callbackInfo.setReturnValue(EntityGroup.UNDEAD);
		}
	}
	
	@Inject(method = "getJumpVelocity", at = @At("RETURN"), cancellable = true)
	private void getJumpVelocity(CallbackInfoReturnable<Float> callbackInfo) {
		if (((Object) this) instanceof PlayerEntity && OriginComponent.hasPower(this, ModifySizePower.class)) {
			callbackInfo.setReturnValue(callbackInfo.getReturnValue() * OriginComponent.getPowers(this, ModifySizePower.class).get(0).scale * 2);
		}
	}
}
