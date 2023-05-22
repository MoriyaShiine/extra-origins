/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.registry;

import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
	public static class Items {
		public static final TagKey<Item> GOLDEN_ARMOR = TagKey.of(Registries.ITEM.getKey(), ExtraOrigins.id("golden_armor"));
		public static final TagKey<Item> GOLDEN_TOOLS = TagKey.of(Registries.ITEM.getKey(), ExtraOrigins.id("golden_tools"));
		public static final TagKey<Item> NETHERITE_ARMOR = TagKey.of(Registries.ITEM.getKey(), ExtraOrigins.id("netherite_armor"));
		public static final TagKey<Item> NETHERITE_TOOLS = TagKey.of(Registries.ITEM.getKey(), ExtraOrigins.id("netherite_tools"));
	}
}
