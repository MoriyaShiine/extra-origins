/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.registry;

import moriyashiine.extraorigins.common.ExtraOrigins;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class ModTags {
	public static final Tag<Item> GOLDEN_ARMOR = TagFactory.ITEM.create(new Identifier(ExtraOrigins.MOD_ID, "golden_armor"));
	public static final Tag<Item> GOLDEN_TOOLS = TagFactory.ITEM.create(new Identifier(ExtraOrigins.MOD_ID, "golden_tools"));
	public static final Tag<Item> NETHERITE_ARMOR = TagFactory.ITEM.create(new Identifier(ExtraOrigins.MOD_ID, "netherite_armor"));
	public static final Tag<Item> NETHERITE_TOOLS = TagFactory.ITEM.create(new Identifier(ExtraOrigins.MOD_ID, "netherite_tools"));
}
