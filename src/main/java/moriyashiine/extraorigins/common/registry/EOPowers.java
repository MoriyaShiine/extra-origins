package moriyashiine.extraorigins.common.registry;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import io.github.apace100.origins.power.*;
import io.github.apace100.origins.registry.ModRegistries;
import moriyashiine.sizeentityattributes.SizeEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class EOPowers {
	private static final Map<PowerType<?>, Identifier> POWER_TYPES = new LinkedHashMap<>();
	
	public static final PowerType<Power> BITE_SIZED = create("bite_sized", new PowerType<>(((powerType, playerEntity) -> new AttributePower(powerType, playerEntity) {
		@Override
		public void onAdded() {
			super.onAdded();
			playerEntity.calculateDimensions();
		}
		
		@Override
		public void onRemoved() {
			super.onRemoved();
			playerEntity.calculateDimensions();
		}
	}.addModifier(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(UUID.fromString("c1e81d44-5775-404f-81d1-d5d3012b0ff0"), "Health mod", -10, EntityAttributeModifier.Operation.ADDITION)).addModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(UUID.fromString("286e76f1-38ec-45e4-8242-93e1c34127d0"), "Movement speed mod", -0.05, EntityAttributeModifier.Operation.ADDITION)).addModifier(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(UUID.fromString("a3669c90-3f4f-458d-a377-e967a43e6292"), "Attack range mod", -1, EntityAttributeModifier.Operation.ADDITION)).addModifier(ReachEntityAttributes.REACH, new EntityAttributeModifier(UUID.fromString("db4d52ff-cab0-4547-85fa-33dfa6232dd9"), "Reach mod", -2, EntityAttributeModifier.Operation.ADDITION)).addModifier(SizeEntityAttributes.WIDTH_MULTIPLIER, new EntityAttributeModifier(UUID.fromString("c4281fa8-ecbc-465a-ac95-ab6e54f72c8c"), "Width multiplier mod", -0.75, EntityAttributeModifier.Operation.ADDITION)).addModifier(SizeEntityAttributes.HEIGHT_MULTIPLIER, new EntityAttributeModifier(UUID.fromString("f3d93a3a-4014-4b99-97ff-f4bbfbf19503"), "Height multiplier mod", -0.75, EntityAttributeModifier.Operation.ADDITION)))));
	public static final PowerType<Power> SMALL_APPETITE = create("small_appetite", new PowerType<>((powerType, playerEntity) -> new ModifyExhaustionPower(powerType, playerEntity, 0.25f)));
	public static final PowerType<Power> LIGHTWEIGHT = create("lightweight", new PowerType<>(Power::new));
	public static final PowerType<Power> WEAK = create("weak", new PowerType<>(((powerType, playerEntity) -> new ModifyDamageDealtPower(powerType, playerEntity, (player, source) -> true, damage -> damage * 2 / 3f))));
	
	public static final PowerType<Power> LUNAR_DEALER = create("lunar_dealer", new PowerType<>(((powerType, playerEntity) -> new ModifyDamageDealtPower(powerType, playerEntity, (player, source) -> true, damage -> damage * (playerEntity.world.getMoonSize() + 0.5f)))));
	public static final PowerType<Power> LUNAR_TAKER = create("lunar_taker", new PowerType<>(((powerType, playerEntity) -> new ModifyDamageTakenPower(powerType, playerEntity, (player, source) -> true, damage -> damage * (DimensionType.field_24752[playerEntity.world.getDimension().method_28531(playerEntity.world.getTimeOfDay() + 96000)] + 0.5f)))));
	public static final PowerType<Power> NIGHT_VISION = create("night_vision", new PowerType<>(((powerType, playerEntity) -> new NightVisionPower(powerType, playerEntity).addCondition(usingPlayer -> true))));
	
	public static final PowerType<Power> PHOTOSYNTHESIS = create("photosynthesis", new PowerType<>(((powerType, playerEntity) -> new PreventItemUsePower(powerType, playerEntity, ItemStack::isFood))));
	public static final PowerType<Power> ABSORBING = create("absorbing", new PowerType<>(((powerType, playerEntity) -> new ModifyDamageDealtPower(powerType, playerEntity, (player, source) -> player.isWet(), damage -> damage * 1.5f).addCondition(Entity::isWet))));
	public static final PowerType<Power> FLAMMABLE = create("flammable", new PowerType<>(((powerType, playerEntity) -> new ModifyDamageTakenPower(powerType, playerEntity, (player, source) -> source.isFire(), damage -> damage * 2))));
	
	private static <T extends Power> PowerType<T> create(String name, PowerType<T> power) {
		POWER_TYPES.put(power, new Identifier("extraorigins", name));
		return power;
	}
	
	public static void init() {
		POWER_TYPES.keySet().forEach(powerType -> Registry.register(ModRegistries.POWER_TYPE, POWER_TYPES.get(powerType), powerType));
	}
}