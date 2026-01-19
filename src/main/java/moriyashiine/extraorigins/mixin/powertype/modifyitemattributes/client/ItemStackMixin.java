/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.mixin.powertype.modifyitemattributes.client;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import moriyashiine.extraorigins.common.power.type.ModifyItemAttributesPowerType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Unique
	private static boolean append = false;

	@Shadow
	protected abstract void appendAttributeModifierTooltip(Consumer<Text> textConsumer, @Nullable PlayerEntity player, RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier);

	@SuppressWarnings("ConstantValue")
	@Inject(method = "appendAttributeModifiersTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;applyAttributeModifier(Lnet/minecraft/component/type/AttributeModifierSlot;Ljava/util/function/BiConsumer;)V", shift = At.Shift.AFTER))
	private void extraorigins$modifyItemAttributes(Consumer<Text> textConsumer, @Nullable PlayerEntity player, CallbackInfo ci, @Local AttributeModifierSlot modifierSlot, @Local MutableBoolean shouldAppendSlotName) {
		if (player != null && !shouldAppendSlotName.booleanValue()) {
			for (ModifyItemAttributesPowerType powerType : PowerHolderComponent.getPowerTypes(player, ModifyItemAttributesPowerType.class)) {
				if (powerType.isActive() && powerType.doesApply(player.getWorld(), (ItemStack) (Object) this)) {
					for (AttributedEntityAttributeModifier attributedModifier : powerType.attributedModifiers()) {
						if (powerType.getSlot() == modifierSlot) {
							append = true;
							appendAttributeModifierTooltip(textConsumer, player, attributedModifier.attribute(), attributedModifier.modifier());
							append = false;
						}
					}
				}
			}
		}
	}

	@ModifyArg(method = "appendAttributeModifierTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;"))
	private Formatting extraorigins$modifyItemAttributes(Formatting formatting) {
		if (append) {
			return Formatting.GOLD;
		}
		return formatting;
	}
}
