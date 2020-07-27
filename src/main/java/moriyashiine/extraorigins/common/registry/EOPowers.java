package moriyashiine.extraorigins.common.registry;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import io.github.apace100.origins.power.*;
import io.github.apace100.origins.registry.ModRegistries;
import moriyashiine.extraorigins.common.mixin.PowerTypeAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;

public class EOPowers {
	private static final Map<PowerType<?>, Identifier> POWER_TYPES = new LinkedHashMap<>();

	public static final PowerType<Power> BITE_SIZED = create("bite_sized", PowerTypeAccessor.createPowerType(((powerType, playerEntity) -> new AttributePower(powerType, playerEntity)
			.addModifier(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(UUID.fromString("c1e81d44-5775-404f-81d1-d5d3012b0ff0"), "Health mod", -10 , EntityAttributeModifier.Operation.ADDITION))
			.addModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(UUID.fromString("18981cb5-3858-4f9f-83db-f2705a9b094d"), "Attack damage mod", 0.5 , EntityAttributeModifier.Operation.MULTIPLY_TOTAL))
			.addModifier(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, new EntityAttributeModifier(UUID.fromString("21b29e99-2813-4ae2-8a0a-f0ec301dfcf5"), "Attack knockback mod", 0.5 , EntityAttributeModifier.Operation.MULTIPLY_TOTAL))
			.addModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(UUID.fromString("286e76f1-38ec-45e4-8242-93e1c34127d0"), "Movement speed mod", -0.04 , EntityAttributeModifier.Operation.ADDITION))
			.addModifier(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(UUID.fromString("a3669c90-3f4f-458d-a377-e967a43e6292"), "Attack range mod", -2 , EntityAttributeModifier.Operation.ADDITION))
			.addModifier(ReachEntityAttributes.REACH, new EntityAttributeModifier(UUID.fromString("db4d52ff-cab0-4547-85fa-33dfa6232dd9"), "Reach mod", -2 , EntityAttributeModifier.Operation.ADDITION)))));
	
	public static final PowerType<Power> LUNAR_DEALER = create("lunar_dealer", PowerTypeAccessor.createPowerType(((powerType, playerEntity) -> new ModifyDamageDealtPower(powerType, playerEntity, (player, source) -> true, damage -> damage * (playerEntity.world.getMoonSize() + 0.5f)))));
	public static final PowerType<Power> LUNAR_TAKER = create("lunar_taker", PowerTypeAccessor.createPowerType(((powerType, playerEntity) -> new ModifyDamageTakenPower(powerType, playerEntity, (player, source) -> true, damage -> damage * (playerEntity.world.getMoonSize() + 0.5f)))));
	public static final PowerType<Power> NIGHT_VISION = create("night_vision", PowerTypeAccessor.createPowerType(((powerType, playerEntity) -> new NightVisionPower(powerType, playerEntity).addCondition(usingPlayer -> true))));
	
	public static final PowerType<Power> PHOTOSYNTHESIS = create("photosynthesis", PowerTypeAccessor.createPowerType(((powerType, playerEntity) -> new PreventItemUsePower(powerType, playerEntity, getFoods()))));
	public static final PowerType<Power> ABSORBING = create("absorbing", PowerTypeAccessor.createPowerType(((powerType, playerEntity) -> new ModifyDamageDealtPower(powerType, playerEntity, (player, source) -> player.isWet(), damage -> damage * 1.5f).addCondition(Entity::isWet))));
	public static final PowerType<Power> FLAMMABLE = create("flammable", PowerTypeAccessor.createPowerType(((powerType, playerEntity) -> new ModifyDamageTakenPower(powerType, playerEntity, (player, source) -> source.isFire(), damage -> damage * 2))));
	
	private static <T extends Power> PowerType<T> create(String name, PowerType<T> power) {
		POWER_TYPES.put(power, new Identifier("extraorigins", name));
		return power;
	}
	
	public static void init() {
		POWER_TYPES.keySet().forEach(powerType -> Registry.register(ModRegistries.POWER_TYPE, POWER_TYPES.get(powerType), powerType));
	}
	
	private static Ingredient getFoods()
	{
		List<Item> foods = new ArrayList<>();
		for (Item item : Registry.ITEM)
		{
			if (item.isFood())
			{
				foods.add(item);
			}
		}
		return Ingredient.ofItems(foods.toArray(new ItemConvertible[0]));
	}
}