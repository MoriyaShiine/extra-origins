package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.EOPowers;
import moriyashiine.extraorigins.common.registry.EOTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract Item getItem();
	
	@Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At("HEAD"), cancellable = true)
	private <T extends LivingEntity> void damage(int amount, T entity, Consumer<T> breakCallback, CallbackInfo callbackInfo) {
		if (EOPowers.ALL_THAT_GLITTERS.isActive(entity)) {
			if (EOTags.GOLDEN_TOOLS.contains(getItem()) && entity.world.random.nextFloat() < 15 / 16f) {
				callbackInfo.cancel();
			}
			if (EOTags.GOLDEN_ARMOR.contains(getItem()) && entity.world.random.nextFloat() < 3 / 4f) {
				callbackInfo.cancel();
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	@Inject(method = "getTooltip", at = @At("RETURN"))
	private void getTooltip(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> callbackInfo) {
		if (EOPowers.ALL_THAT_GLITTERS.get(player) != null) {
			if (EOTags.GOLDEN_TOOLS.contains(getItem())) {
				callbackInfo.getReturnValue().add(new TranslatableText("tooltip.extraorigins.damage_bonus", 2.5).formatted(Formatting.GOLD));
			}
			if (EOTags.GOLDEN_ARMOR.contains(getItem())) {
				callbackInfo.getReturnValue().add(new TranslatableText("tooltip.extraorigins.damage_reduction", 8).formatted(Formatting.GOLD));
			}
		}
	}
}
