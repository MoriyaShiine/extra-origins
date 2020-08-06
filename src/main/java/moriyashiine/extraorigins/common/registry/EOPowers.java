package moriyashiine.extraorigins.common.registry;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import io.github.apace100.origins.power.*;
import io.github.apace100.origins.registry.ModRegistries;
import moriyashiine.extraorigins.common.interfaces.BabyAccessor;
import moriyashiine.sizeentityattributes.SizeEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

import java.util.LinkedHashMap;
import java.util.Map;

public class EOPowers {
	private static final Map<PowerType<?>, Identifier> POWER_TYPES = new LinkedHashMap<>();
	
	public static final PowerType<Power> BITE_SIZED = create("bite_sized", new PowerType<>(((powerType, playerEntity) -> new AttributePower(powerType, playerEntity) {
		@Override
		public void onAdded() {
			super.onAdded();
			BabyAccessor.get(playerEntity).ifPresent(babyAccessor -> babyAccessor.setBaby(true));
			playerEntity.calculateDimensions();
		}
		
		@Override
		public void onRemoved() {
			super.onRemoved();
			BabyAccessor.get(playerEntity).ifPresent(babyAccessor -> babyAccessor.setBaby(false));
			playerEntity.calculateDimensions();
		}
	}.addModifier(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier("Health mod", -10, EntityAttributeModifier.Operation.ADDITION)).addModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier("Movement speed mod", -0.05, EntityAttributeModifier.Operation.ADDITION)).addModifier(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier("Attack range mod", -1, EntityAttributeModifier.Operation.ADDITION)).addModifier(ReachEntityAttributes.REACH, new EntityAttributeModifier("Reach mod", -2, EntityAttributeModifier.Operation.ADDITION)).addModifier(SizeEntityAttributes.WIDTH_MULTIPLIER, new EntityAttributeModifier("Width multiplier mod", -0.75, EntityAttributeModifier.Operation.ADDITION)).addModifier(SizeEntityAttributes.HEIGHT_MULTIPLIER, new EntityAttributeModifier("Height multiplier mod", -0.75, EntityAttributeModifier.Operation.ADDITION)))));
	public static final PowerType<Power> SMALL_APPETITE = create("small_appetite", new PowerType<>((powerType, playerEntity) -> new ModifyExhaustionPower(powerType, playerEntity, 0.25f)));
	public static final PowerType<Power> LIGHTWEIGHT = create("lightweight", new PowerType<>(Power::new));
	public static final PowerType<Power> WEAK = create("weak", new PowerType<>(((powerType, playerEntity) -> new ModifyDamageDealtPower(powerType, playerEntity, (player, source) -> true, damage -> damage * 2 / 3f))));
	
	public static final PowerType<Power> LUNAR_DEALER = create("lunar_dealer", new PowerType<>(((powerType, playerEntity) -> new ModifyDamageDealtPower(powerType, playerEntity, (player, source) -> true, damage -> damage * (playerEntity.world.getMoonSize() + 0.5f)))));
	public static final PowerType<Power> LUNAR_TAKER = create("lunar_taker", new PowerType<>(((powerType, playerEntity) -> new ModifyDamageTakenPower(powerType, playerEntity, (player, source) -> true, damage -> damage * (DimensionType.field_24752[playerEntity.world.getDimension().method_28531(playerEntity.world.getTimeOfDay() + 96000)] + 0.5f)))));
	public static final PowerType<Power> NIGHT_VISION = create("night_vision", new PowerType<>(((powerType, playerEntity) -> new NightVisionPower(powerType, playerEntity).addCondition(usingPlayer -> true))));
	
	public static final PowerType<Power> PHOTOSYNTHESIS = create("photosynthesis", new PowerType<>(((powerType, playerEntity) -> new PreventItemUsePower(powerType, playerEntity, ItemStack::isFood))));
	public static final PowerType<Power> ABSORBING = create("absorbing", new PowerType<>(((powerType, playerEntity) -> new ModifyDamageDealtPower(powerType, playerEntity, (player, source) -> player.isWet(), damage -> damage * 1.5f).addCondition(Entity::isWet))));
	public static final PowerType<Power> FLAMMABLE = create("flammable", new PowerType<>(((powerType, playerEntity) -> new ModifyDamageTakenPower(powerType, playerEntity, (player, source) -> source.isFire(), damage -> damage * 2))));
	
	public static final PowerType<Power> INORGANIC = create("inorganic", new PowerType<>((powerType, playerEntity) -> new PreventItemUsePower(powerType, playerEntity, ItemStack::isFood)));
	public static final PowerType<Power> GLASS_CANNON = create("glass_cannon", new PowerType<>((powerType, playerEntity) -> new ModifyDamageDealtPower(powerType, playerEntity, (player, source) -> true, damage -> damage * 2)));
	@SuppressWarnings("unchecked")
	public static final PowerType<Power> GLASS_CANNON_DUMMY = create("glass_cannon_dummy", new PowerType<>((powerType, playerEntity) -> new ModifyDamageTakenPower(powerType, playerEntity, (player, source) -> true, damage -> damage * 2)).setHidden());
	
	public static final PowerType<Power> ALL_THAT_GLITTERS = create("all_that_glitters", new PowerType<>(((powerType, playerEntity) -> new ModifyDamageDealtPower(powerType, playerEntity, (player, source) -> true, damage -> {
		Item item = playerEntity.getMainHandStack().getItem();
		return damage + (item instanceof ToolItem && ((ToolItem) item).getMaterial() == ToolMaterials.GOLD ? 2 : 0);
	}))));
	@SuppressWarnings("unchecked")
	public static final PowerType<Power> ALL_THAT_GLITTERS_DUMMY = create("all_that_glitters_dummy", new PowerType<>(((powerType, playerEntity) -> new ModifyDamageTakenPower(powerType, playerEntity, (player, source) -> true, damage -> {
		byte armorPieces = 0;
		for (ItemStack stack : playerEntity.getArmorItems()) {
			if (stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() == ArmorMaterials.GOLD) {
				armorPieces++;
			}
		}
		return damage * (1 - (armorPieces * 0.08f));
	}))).setHidden());
	public static final PowerType<Power> CROSSBOW_MASTER = create("crossbow_master", new PowerType<>(Power::new));
	public static final PowerType<Power> HOMESICK = create("homesick", new PowerType<>(Power::new));
	public static final PowerType<Power> FLABBY = create("flabby", new PowerType<>(((powerType, playerEntity) -> new AttributePower(powerType, playerEntity).addModifier(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier("Health mod", -4, EntityAttributeModifier.Operation.ADDITION)))));
	public static final PowerType<Power> SOUL_SPOOKED = create("soul_spooked", new PowerType<>(Power::new));
	
	private static <T extends Power> PowerType<T> create(String name, PowerType<T> power) {
		POWER_TYPES.put(power, new Identifier("extraorigins", name));
		return power;
	}
	
	public static void init() {
		POWER_TYPES.keySet().forEach(powerType -> Registry.register(ModRegistries.POWER_TYPE, POWER_TYPES.get(powerType), powerType));
	}
}
