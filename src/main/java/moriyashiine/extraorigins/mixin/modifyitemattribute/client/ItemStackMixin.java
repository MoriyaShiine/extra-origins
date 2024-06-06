/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.mixin.modifyitemattribute.client;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import moriyashiine.extraorigins.common.power.ModifyItemAttributePower;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@WrapOperation(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;"))
	private Multimap<EntityAttribute, EntityAttributeModifier> extraorigins$addToolBonusTooltips(ItemStack instance, EquipmentSlot slot, Operation<Multimap<EntityAttribute, EntityAttributeModifier>> original, @Nullable PlayerEntity player) {
		Multimap<EntityAttribute, EntityAttributeModifier> value = original.call(instance, slot);
		if (player != null && !value.isEmpty()) {
			List<ModifyItemAttributePower> powers = PowerHolderComponent.getPowers(player, ModifyItemAttributePower.class);
			if (!powers.isEmpty()) {
				value = LinkedHashMultimap.create(value);
				for (ModifyItemAttributePower power : powers) {
					if (slot == power.getSlot() && power.doesApply(instance)) {
						for (AttributedEntityAttributeModifier modifier : power.getModifiers()) {
							value.put(modifier.getAttribute(), modifier.getModifier());
						}
					}
				}
			}
		}
		return value;
	}
}
