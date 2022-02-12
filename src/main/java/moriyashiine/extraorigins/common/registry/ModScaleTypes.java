/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.registry;

import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.*;

import java.util.Map;

public class ModScaleTypes {
	private static final ScaleType[] MODIFY_SIZE_TYPES = {ScaleTypes.WIDTH, ScaleTypes.HEIGHT, ScaleTypes.DROPS, ScaleTypes.VISIBILITY};

	public static final ScaleModifier MODIFY_SIZE_MODIFIER = register(ScaleRegistries.SCALE_MODIFIERS, new TypedScaleModifier(() -> ModScaleTypes.MODIFY_SIZE_TYPE));

	public static final ScaleType MODIFY_SIZE_TYPE = register(ScaleRegistries.SCALE_TYPES, ScaleType.Builder.create().addDependentModifier(MODIFY_SIZE_MODIFIER).affectsDimensions().build());

	private static <T> T register(Map<Identifier, T> registry, T entry) {
		return ScaleRegistries.register(registry, new Identifier(ExtraOrigins.MOD_ID, "modify_size"), entry);
	}

	public static void init() {
		for (ScaleType type : MODIFY_SIZE_TYPES) {
			type.getDefaultBaseValueModifiers().add(MODIFY_SIZE_MODIFIER);
		}
	}
}
