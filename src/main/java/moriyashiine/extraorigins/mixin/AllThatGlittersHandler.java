package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.registry.EOPowers;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.*;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class AllThatGlittersHandler {
	@Shadow
	public abstract Item getItem();
	
	@Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At("HEAD"), cancellable = true)
	private <T extends LivingEntity> void damage(int amount, T entity, Consumer<T> breakCallback, CallbackInfo callbackInfo) {
		if (EOPowers.ALL_THAT_GLITTERS.isActive(entity)) {
			Item item = getItem();
			if (item instanceof ToolItem && ((ToolItem) item).getMaterial() == ToolMaterials.GOLD && entity.world.random.nextFloat() < 15 / 16f) {
				callbackInfo.cancel();
			}
			if (item instanceof ArmorItem && ((ArmorItem) item).getMaterial() == ArmorMaterials.GOLD && entity.world.random.nextFloat() < 3 / 4f) {
				callbackInfo.cancel();
			}
		}
	}
	
	@Mixin(PiglinBrain.class)
	private static class NeutralPiglins {
		@Inject(method = "wearsGoldArmor", at = @At("HEAD"), cancellable = true)
		private static void shouldAttack(LivingEntity target, CallbackInfoReturnable<Boolean> callbackInfo) {
			if (EOPowers.ALL_THAT_GLITTERS.isActive(target)) {
				callbackInfo.setReturnValue(true);
			}
		}
	}
	
	@Mixin(LivingEntity.class)
	private abstract static class GoldArmorBuff extends Entity {
		private static final Tag<Item> GOLDEN_ARMOR = TagRegistry.item(new Identifier(ExtraOrigins.MODID, "golden_armor"));
		
		public GoldArmorBuff(EntityType<?> type, World world) {
			super(type, world);
		}
		
		@ModifyVariable(method = "damage", at = @At("HEAD"))
		private float damage(float amount, DamageSource source) {
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
	}
}
