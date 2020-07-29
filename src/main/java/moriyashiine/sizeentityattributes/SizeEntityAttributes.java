package moriyashiine.sizeentityattributes;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SizeEntityAttributes {
	public static final EntityAttribute WIDTH_MULTIPLIER = Registry.register(Registry.ATTRIBUTE, new Identifier("sizeentityattributes", "width_multiplier"), new ClampedEntityAttribute("attribute.name.generic.sizeentityattributes.width_multiplier", 1, 0.125, 8)).setTracked(true);
	public static final EntityAttribute HEIGHT_MULTIPLIER = Registry.register(Registry.ATTRIBUTE, new Identifier("sizeentityattributes", "height_multiplier"), new ClampedEntityAttribute("attribute.name.generic.sizeentityattributes.height_multiplier", 1, 0.125, 8)).setTracked(true);
}