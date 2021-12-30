/*
 * All Rights Reserved (c) 2021 MoriyaShiine
 */

/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import moriyashiine.extraorigins.common.registry.ModPowers;
import moriyashiine.extraorigins.common.registry.ModTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Environment(EnvType.CLIENT)
	private static final EntityAttributeModifier ATTACK_DAMAGE_MODIFIER = new EntityAttributeModifier("Origin modifier", 2.5, EntityAttributeModifier.Operation.ADDITION);
	@Environment(EnvType.CLIENT)
	private static final EntityAttributeModifier ARMOR_MODIFIER_0 = new EntityAttributeModifier("Origin modifier", 1, EntityAttributeModifier.Operation.ADDITION);
	@Environment(EnvType.CLIENT)
	private static final EntityAttributeModifier ARMOR_MODIFIER_1 = new EntityAttributeModifier("Origin modifier", 2, EntityAttributeModifier.Operation.ADDITION);
	@Environment(EnvType.CLIENT)
	private static final EntityAttributeModifier MOVEMENT_SPEED_MODIFIER = new EntityAttributeModifier("Origin modifier", 0.08, EntityAttributeModifier.Operation.MULTIPLY_BASE);
	
	@Shadow
	public abstract Item getItem();
	
	@Shadow
	public abstract boolean damage(int amount, Random random, @Nullable ServerPlayerEntity player);
	
	@Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At("HEAD"), cancellable = true)
	private <T extends LivingEntity> void extraorigins$increaseGoldToolDurability(int amount, T entity, Consumer<T> breakCallback, CallbackInfo ci) {
		if (!entity.world.isClient && ModPowers.ALL_THAT_GLITTERS.get(entity) != null && !(entity instanceof PlayerEntity player && player.isCreative())) {
			if (getItem() instanceof ToolItem) {
				if (ModTags.GOLDEN_TOOLS.contains(getItem())) {
					if (entity.world.random.nextFloat() < 15 / 16F) {
						ci.cancel();
					}
				}
				else if (entity.getRandom().nextBoolean() && !ModTags.NETHERITE_TOOLS.contains(getItem())) {
					damage(amount, entity.getRandom(), null);
				}
			}
			if (getItem() instanceof ArmorItem) {
				if (ModTags.GOLDEN_ARMOR.contains(getItem())) {
					if (entity.world.random.nextFloat() < 3 / 4F) {
						ci.cancel();
					}
				}
				else if (entity.getRandom().nextBoolean() && !ModTags.NETHERITE_ARMOR.contains(getItem())) {
					damage(amount, entity.getRandom(), null);
				}
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	@ModifyVariable(method = "getTooltip", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;"))
	private Multimap<EntityAttribute, EntityAttributeModifier> extraorigins$addToolBonusTooltips(Multimap<EntityAttribute, EntityAttributeModifier> obj, PlayerEntity player, TooltipContext context) {
		if (ModPowers.ALL_THAT_GLITTERS.get(player) != null && !obj.isEmpty()) {
			obj = LinkedHashMultimap.create(obj);
			if (getItem() instanceof ToolItem && ModTags.GOLDEN_TOOLS.contains(getItem())) {
				obj.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER);
			}
			if (getItem() instanceof ArmorItem armorItem) {
				if (ModTags.GOLDEN_ARMOR.contains(getItem())) {
					obj.put(EntityAttributes.GENERIC_ARMOR, armorItem.getSlotType() == EquipmentSlot.CHEST || ((ArmorItem) getItem()).getSlotType() == EquipmentSlot.LEGS ? ARMOR_MODIFIER_1 : ARMOR_MODIFIER_0);
				}
				if (ModTags.NETHERITE_ARMOR.contains(getItem())) {
					obj.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, MOVEMENT_SPEED_MODIFIER);
				}
			}
		}
		return obj;
	}
}
