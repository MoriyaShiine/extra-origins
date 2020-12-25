package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
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
}
