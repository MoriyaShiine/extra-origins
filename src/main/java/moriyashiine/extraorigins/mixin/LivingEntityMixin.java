package moriyashiine.extraorigins.mixin;

import io.github.apace100.origins.component.OriginComponent;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.interfaces.BabyAccessor;
import moriyashiine.extraorigins.common.power.ModifySizePower;
import moriyashiine.extraorigins.common.registry.EOPowers;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	private static final Tag<Item> GOLDEN_ARMOR = TagRegistry.item(new Identifier(ExtraOrigins.MODID, "golden_armor"));
	
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);
	
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (EOPowers.HOMESICK.isActive(this) && !world.getDimension().isPiglinSafe()) {
			addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 1, true, false));
		}
		if (EOPowers.SOUL_SPOOKED.isActive(this) && age % 20 == 0) {
			boolean foundSoulFire = false;
			BlockPos.Mutable mutablePos = new BlockPos.Mutable();
			for (byte x = -8; x <= 8; x++) {
				for (byte y = -8; y <= 8; y++) {
					for (byte z = -8; z <= 8; z++) {
						if (BlockTags.PIGLIN_REPELLENTS.contains(world.getBlockState(mutablePos.set(getX() + x, getY() + y, getZ() + z)).getBlock())) {
							foundSoulFire = true;
							break;
						}
					}
				}
			}
			if (foundSoulFire) {
				addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 2));
			}
		}
	}
	
	@ModifyVariable(method = "damage", at = @At("HEAD"))
	private float damage(float amount, DamageSource damageSource) {
		Entity attacker = damageSource.getAttacker();
		if (EOPowers.CROSSBOW_MASTER.isActive(attacker)) {
			Entity projectile = damageSource.getSource();
			if (projectile instanceof PersistentProjectileEntity && ((PersistentProjectileEntity) projectile).isShotFromCrossbow()) {
				return amount * 2;
			}
		}
		if (EOPowers.ALL_THAT_GLITTERS.isActive(this)) {
			int armorPieces = 0;
			for (ItemStack stack : getArmorItems()) {
				if (GOLDEN_ARMOR.contains(stack.getItem())) {
					armorPieces++;
				}
			}
			return amount * (1 - (armorPieces * 0.08f));
		}
		return amount;
	}
	
	@Inject(method = "getGroup", at = @At("HEAD"), cancellable = true)
	private void getGroup(CallbackInfoReturnable<EntityGroup> callbackInfo) {
		if (EOPowers.HOMESICK.isActive(this) && !world.getDimension().isPiglinSafe()) {
			callbackInfo.setReturnValue(EntityGroup.UNDEAD);
		}
	}
	
	@Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
	private void canHaveStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (EOPowers.INORGANIC.isActive(this)) {
			callbackInfo.setReturnValue(effect.getEffectType() == StatusEffects.BAD_OMEN);
		}
	}
	
	@Inject(method = "isAffectedBySplashPotions", at = @At("HEAD"), cancellable = true)
	private void isAffectedBySplashPotions(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (EOPowers.INORGANIC.isActive(this)) {
			callbackInfo.setReturnValue(false);
		}
	}
	
	@Inject(method = "isBaby", at = @At("HEAD"), cancellable = true)
	private void isBaby(CallbackInfoReturnable<Boolean> callbackInfo) {
		BabyAccessor.get(this).ifPresent(babyAccessor -> {
			if (babyAccessor.getBaby()) {
				callbackInfo.setReturnValue(true);
			}
		});
	}
	
	@Inject(method = "getJumpVelocity", at = @At("RETURN"), cancellable = true)
	private void getJumpVelocity(CallbackInfoReturnable<Float> callbackInfo) {
		//noinspection ConstantConditions
		if (((Object) this) instanceof PlayerEntity && OriginComponent.hasPower(this, ModifySizePower.class)) {
			callbackInfo.setReturnValue(callbackInfo.getReturnValue() * OriginComponent.getPowers(this, ModifySizePower.class).get(0).scale * 2);
		}
	}
}
