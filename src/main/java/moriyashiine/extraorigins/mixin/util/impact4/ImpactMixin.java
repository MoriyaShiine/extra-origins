/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.mixin.util.impact4;

import io.github.apace100.origins.origin.Impact;
import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(Impact.class)
public class ImpactMixin {
	@Unique
	private static final Impact VERY_HIGH = addImpact4(ExtraOrigins.id("choose_origin/impact/very_high"));

	@Shadow
	@Final
	@Mutable
	private static Impact[] $VALUES;

	@Invoker("<init>")
	public static Impact extraorigins$init(String name, int id, int impactValue, String translationKey, Formatting textStyle, Identifier spriteId) {
		throw new UnsupportedOperationException();
	}

	@Unique
	private static Impact addImpact4(Identifier spriteId) {
		List<Impact> originalList = new ArrayList<>(Arrays.asList(Impact.values()));
		Impact impact4 = extraorigins$init("VERY_HIGH", originalList.getLast().ordinal() + 1, 4, "very_high", Formatting.LIGHT_PURPLE, spriteId);
		originalList.add(impact4);
		$VALUES = originalList.toArray(new Impact[0]);
		return impact4;
	}
}
