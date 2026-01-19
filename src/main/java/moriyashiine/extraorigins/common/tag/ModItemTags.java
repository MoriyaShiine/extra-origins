/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.tag;

import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModItemTags {
	public static final TagKey<Item> GOLDEN_EQUIPMENT = TagKey.of(RegistryKeys.ITEM, ExtraOrigins.id("golden_equipment"));
	public static final TagKey<Item> GOLDEN_ARMOR = TagKey.of(RegistryKeys.ITEM, ExtraOrigins.id("golden_armor"));
	public static final TagKey<Item> GOLDEN_TOOLS = TagKey.of(RegistryKeys.ITEM, ExtraOrigins.id("golden_tools"));

}
