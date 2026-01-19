/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.power.type;

import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.ItemCondition;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.AttributePowerType;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import io.github.apace100.apoli.util.MiscUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import moriyashiine.extraorigins.common.init.ModPowerTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ModifyItemAttributesPowerType extends AttributePowerType {
	public static final TypedDataObjectFactory<ModifyItemAttributesPowerType> DATA_FACTORY = PowerType.createConditionedDataFactory(
			new SerializableData()
					.add("modifier", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIER, null)
					.addFunctionedDefault("modifiers", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIERS, data -> MiscUtil.singletonListOrNull(data.get("modifier")))
					.add("update_health", SerializableDataTypes.BOOLEAN, true)
					.add("item_condition", ItemCondition.DATA_TYPE)
					.add("slot", SerializableDataTypes.ATTRIBUTE_MODIFIER_SLOT)
					.add("tick_rate", SerializableDataTypes.POSITIVE_INT, 10)
					.validate(MiscUtil.validateAnyFieldsPresent("modifier", "modifiers")),
			(data, condition) -> new ModifyItemAttributesPowerType(
					condition,
					data.get("modifiers"),
					data.get("update_health"),
					data.get("item_condition"),
					data.get("slot"),
					data.getInt("tick_rate")
			),
			(powerType, serializableData) -> serializableData.instance()
					.set("modifiers", powerType.attributedModifiers())
					.set("update_health", powerType.shouldUpdateHealth())
					.set("item_condition", powerType.itemCondition)
					.set("slot", powerType.slot)
					.set("tick_rate", powerType.tickRate)
	);

	private final ItemCondition itemCondition;
	private final AttributeModifierSlot slot;
	private final int tickRate;

	public ModifyItemAttributesPowerType(Optional<EntityCondition> condition, List<AttributedEntityAttributeModifier> attributedModifiers, boolean updateHealth, ItemCondition itemCondition, AttributeModifierSlot slot, int tickRate) {
		super(attributedModifiers, updateHealth, condition);
		this.itemCondition = itemCondition;
		this.slot = slot;
		this.tickRate = tickRate;
		setTicking(true);
	}

	@Override
	public @NotNull PowerConfiguration<?> getConfig() {
		return ModPowerTypes.MODIFY_ITEM_ATTRIBUTES;
	}

	@Override
	public void serverTick() {
		if (getHolder().age % tickRate != 0) {
			return;
		}
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (this.slot.matches(slot)) {
				if (doesApply(getHolder().getWorld(), getHolder().getEquippedStack(slot))) {
					addTemporaryModifiers(getHolder());
				} else {
					removeModifiers(getHolder());
				}
			}
		}
	}

	public AttributeModifierSlot getSlot() {
		return slot;
	}

	public boolean doesApply(World world, ItemStack stack) {
		return itemCondition.test(world, stack);
	}
}
