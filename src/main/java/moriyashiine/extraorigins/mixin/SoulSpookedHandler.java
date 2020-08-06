package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class SoulSpookedHandler extends Entity {
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);
	
	public SoulSpookedHandler(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
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
}
