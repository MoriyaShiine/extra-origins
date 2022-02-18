/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import com.mojang.datafixers.util.Pair;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.component.entity.MagicSporesComponent;
import moriyashiine.extraorigins.common.power.FoodEffectImmunityPower;
import moriyashiine.extraorigins.common.power.MagicSporesPower;
import moriyashiine.extraorigins.common.power.ModifyAirStrafingSpeedPower;
import moriyashiine.extraorigins.common.registry.ModComponents;
import moriyashiine.extraorigins.common.registry.ModParticleTypes;
import moriyashiine.extraorigins.common.registry.ModPowers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow public float airStrafingSpeed;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow public abstract Random getRandom();

	@ModifyVariable(method = "applyFoodEffects", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/FoodComponent;getStatusEffects()Ljava/util/List;"))
	private List<Pair<StatusEffectInstance, Float>> extraorigins$foodEffectImmunity(List<Pair<StatusEffectInstance, Float>> obj) {
		for (FoodEffectImmunityPower foodEffectImmunityPower : PowerHolderComponent.getPowers(LivingEntity.class.cast(this), FoodEffectImmunityPower.class)) {
			if (!obj.isEmpty()) {
				obj = new ArrayList<>(obj);
				for (int i = obj.size() - 1; i >= 0; i--) {
					if (foodEffectImmunityPower.shouldRemove(obj.get(i).getFirst().getEffectType())) {
						obj.remove(i);
					}
				}
			}
		}
		return obj;
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void extraorigins$modifyAirStrafeSpeedAndSpawnTruffleParticles(CallbackInfo ci) {
		this.airStrafingSpeed = (float) PowerHolderComponent.modify((LivingEntity)(Object)this, ModifyAirStrafingSpeedPower.class, this.airStrafingSpeed);
		if (ModPowers.OFFENSE_SPORE_PARTICLES.isActive(this) || ModPowers.DEFENSE_SPORE_PARTICLES.isActive(this) || ModPowers.MOBILITY_SPORE_PARTICLES.isActive(this)) {
			PlayerEntity player = MinecraftClient.getInstance().player;
			if (this.world.isClient && this.getRandom().nextBoolean() && !this.isInvisible()) {
				if ((LivingEntity)(Object)this != player || MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson()) {
					List<ParticleEffect> activeParticles = new ArrayList<>();
					if (ModPowers.OFFENSE_SPORE_PARTICLES.isActive(this)) {
						activeParticles.add((ParticleEffect)ModParticleTypes.OFFENSE_SPORE);
					}
					if (ModPowers.DEFENSE_SPORE_PARTICLES.isActive(this)) {
						activeParticles.add((ParticleEffect)ModParticleTypes.DEFENSE_SPORE);
					}
					if (ModPowers.MOBILITY_SPORE_PARTICLES.isActive(this)) {
						activeParticles.add((ParticleEffect)ModParticleTypes.MOBILITY_SPORE);
					}
					activeParticles.forEach(particle -> {
						this.world.addParticle(particle, this.getParticleX(1), this.getRandomBodyY(), this.getParticleZ(1), MathHelper.nextDouble(this.getRandom(), 0.85, 1), MathHelper.nextDouble(this.getRandom(), 0.85, 1), MathHelper.nextDouble(this.getRandom(), 0.85, 1));
					});
				}
			}
		}
	}
}
