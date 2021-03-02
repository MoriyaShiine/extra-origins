package moriyashiine.extraorigins.common.registry;

import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

import java.util.Map;

public class EOScaleTypes {
	public static final ScaleModifier MODIFY_SIZE_MODIFIER = register(ScaleRegistries.SCALE_MODIFIERS, "modify_size", new ScaleModifier() {
		@Override
		public float modifyScale(final ScaleData scaleData, float modifiedScale, final float delta) {
			return MODIFY_SIZE_TYPE.getScaleData(scaleData.getEntity()).getScale(delta) * modifiedScale;
		}
	});
	
	private static final ScaleType[] MODIFIED_SIZE_TYPES = {ScaleType.WIDTH, ScaleType.HEIGHT, ScaleType.DROPS};
	
	public static final ScaleType MODIFY_SIZE_TYPE = register(ScaleRegistries.SCALE_TYPES, "modify_size", ScaleType.Builder.create().build());
	
	private static <T> T register(Map<Identifier, T> registry, String name, T entry) {
		return ScaleRegistries.register(registry, new Identifier(ExtraOrigins.MODID, name), entry);
	}
	
	public static void init() {
		for (ScaleType type : MODIFIED_SIZE_TYPES) {
			type.getDefaultBaseValueModifiers().add(MODIFY_SIZE_MODIFIER);
		}
		MODIFY_SIZE_TYPE.getScaleChangedEvent().register(event -> {
			Entity entity = event.getEntity();
			if (entity != null) {
				boolean onGround = entity.isOnGround();
				entity.calculateDimensions();
				entity.setOnGround(onGround);
				for (ScaleType scaleType : ScaleRegistries.SCALE_TYPES.values()) {
					ScaleData data = scaleType.getScaleData(entity);
					if (data.getBaseValueModifiers().contains(MODIFY_SIZE_MODIFIER)) {
						data.markForSync(true);
					}
				}
			}
		});
	}
}
