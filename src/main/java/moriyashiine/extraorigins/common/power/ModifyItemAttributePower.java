/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.AttributePower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class ModifyItemAttributePower extends AttributePower {
	private final Predicate<Pair<World, ItemStack>> itemCondition;
	private final EquipmentSlot slot;
	private final int tickRate;

	public ModifyItemAttributePower(PowerType<?> type, LivingEntity entity, EquipmentSlot slot, Predicate<Pair<World, ItemStack>> itemCondition, int tickRate, boolean updateHealth) {
		super(type, entity, updateHealth);
		this.itemCondition = itemCondition;
		this.slot = slot;
		this.tickRate = tickRate;
		setTicking(true);
	}

	@Override
	public void tick() {
		if (entity.age % tickRate != 0) {
			return;
		}
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (this.slot == slot) {
				if (doesApply(entity.getEquippedStack(slot))) {
					applyTempMods();
				} else {
					removeTempMods();
				}
			}
		}
	}

	public List<AttributedEntityAttributeModifier> getModifiers() {
		return modifiers;
	}

	public EquipmentSlot getSlot() {
		return slot;
	}

	public boolean doesApply(ItemStack stack) {
		return itemCondition == null || itemCondition.test(new Pair<>(entity.getWorld(), stack));
	}

	public static PowerFactory<?> createFactory(Identifier identifier) {
		return new PowerFactory<>(identifier,
				new SerializableData()
						.add("equipment_slot", SerializableDataTypes.EQUIPMENT_SLOT)
						.add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
						.add("tick_rate", SerializableDataTypes.POSITIVE_INT, 20)
						.add("update_health", SerializableDataTypes.BOOLEAN, true)
						.add("modifier", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIER, null)
						.add("modifiers", ApoliDataTypes.ATTRIBUTED_ATTRIBUTE_MODIFIERS, null),
				data -> (powerType, livingEntity) -> {
					ModifyItemAttributePower conditionedAttributePower = new ModifyItemAttributePower(
							powerType,
							livingEntity,
							data.get("equipment_slot"),
							data.get("item_condition"),
							data.getInt("tick_rate"),
							data.getBoolean("update_health")
					);
					data.<AttributedEntityAttributeModifier>ifPresent("modifier", conditionedAttributePower::addModifier);
					data.<List<AttributedEntityAttributeModifier>>ifPresent("modifiers", mods -> mods.forEach(conditionedAttributePower::addModifier));
					return conditionedAttributePower;
				});
	}
}
