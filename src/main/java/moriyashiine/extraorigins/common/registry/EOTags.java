package moriyashiine.extraorigins.common.registry;

import moriyashiine.extraorigins.common.ExtraOrigins;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class EOTags {
	public static final Tag<Item> GOLDEN_ARMOR_0 = TagRegistry.item(new Identifier(ExtraOrigins.MODID, "golden_armor_0"));
	public static final Tag<Item> GOLDEN_ARMOR_1 = TagRegistry.item(new Identifier(ExtraOrigins.MODID, "golden_armor_1"));
	public static final Tag<Item> GOLDEN_TOOLS = TagRegistry.item(new Identifier(ExtraOrigins.MODID, "golden_tools"));
	public static final Tag<Item> PIGLIN_EFFECTIVE = TagRegistry.item(new Identifier(ExtraOrigins.MODID, "piglin_effective"));
}
