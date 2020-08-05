package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class CrossbowMasterHandler {
	@ModifyVariable(method = "damage", at = @At("HEAD"))
	private float damage(float amount, DamageSource damageSource) {
		Entity attacker = damageSource.getAttacker();
		if (EOPowers.CROSSBOW_MASTER.isActive(attacker)) {
			Entity projectile = damageSource.getSource();
			if (projectile instanceof PersistentProjectileEntity && ((PersistentProjectileEntity) projectile).isShotFromCrossbow()) {
				return amount * 2;
			}
		}
		return amount;
	}
}