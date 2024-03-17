package moriyashiine.extraorigins.mixin.allthatglitters.client;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import moriyashiine.extraorigins.common.registry.ModPowers;
import moriyashiine.extraorigins.common.registry.ModTags;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Unique
	private static final EntityAttributeModifier ATTACK_DAMAGE_MODIFIER = new EntityAttributeModifier("Origin modifier", 2.5, EntityAttributeModifier.Operation.ADDITION);

	@Unique
	private static final EntityAttributeModifier ARMOR_MODIFIER_0 = new EntityAttributeModifier("Origin modifier", 1, EntityAttributeModifier.Operation.ADDITION);

	@Unique
	private static final EntityAttributeModifier ARMOR_MODIFIER_1 = new EntityAttributeModifier("Origin modifier", 2, EntityAttributeModifier.Operation.ADDITION);

	@Unique
	private static final EntityAttributeModifier MOVEMENT_SPEED_MODIFIER = new EntityAttributeModifier("Origin modifier", 0.08, EntityAttributeModifier.Operation.MULTIPLY_BASE);

	@Shadow
	public abstract Item getItem();

	@Shadow
	public abstract boolean isIn(TagKey<Item> tag);

	@ModifyVariable(method = "getTooltip", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;"))
	private Multimap<EntityAttribute, EntityAttributeModifier> extraorigins$addToolBonusTooltips(Multimap<EntityAttribute, EntityAttributeModifier> value, PlayerEntity player, TooltipContext context) {
		if (ModPowers.ALL_THAT_GLITTERS.get(player) != null && !value.isEmpty()) {
			value = LinkedHashMultimap.create(value);
			if (getItem() instanceof ToolItem && isIn(ModTags.Items.GOLDEN_TOOLS)) {
				value.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER);
			}
			if (getItem() instanceof ArmorItem armorItem) {
				if (isIn(ModTags.Items.GOLDEN_ARMOR)) {
					value.put(EntityAttributes.GENERIC_ARMOR, armorItem.getSlotType() == EquipmentSlot.CHEST || ((ArmorItem) getItem()).getSlotType() == EquipmentSlot.LEGS ? ARMOR_MODIFIER_1 : ARMOR_MODIFIER_0);
				}
				if (isIn(ModTags.Items.NETHERITE_ARMOR)) {
					value.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, MOVEMENT_SPEED_MODIFIER);
				}
			}
		}
		return value;
	}
}
