package moriyashiine.extraorigins.common.registry;

import java.util.Map;

import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

public class EOScaleTypes {
	public static final ScaleModifier MODIFY_SIZE_MODIFIER = register(ScaleRegistries.SCALE_MODIFIERS, "modify_size", new ScaleModifier() {
		@Override
		public float modifyScale(final ScaleData scaleData, float modifiedScale, final float delta) {
			return MODIFY_SIZE_TYPE.getScaleData(scaleData.getEntity()).getScale(delta) * modifiedScale;
		}
	});
	
	public static final ScaleType MODIFY_SIZE_TYPE = register(ScaleRegistries.SCALE_TYPES, "modify_size", ScaleType.Builder.create().build());
	
	private static <T> T register(Map<Identifier, T> registry, String name, T entry) {
		return ScaleRegistries.register(registry, new Identifier(ExtraOrigins.MODID, name), entry);
	}
	
	private static final ScaleType[] MODIFIED_SIZE_TYPES = { ScaleType.WIDTH, ScaleType.HEIGHT, ScaleType.DROPS };
	
	public static void init() {
		for (ScaleType type : MODIFIED_SIZE_TYPES) {
			type.getDefaultBaseValueModifiers().add(MODIFY_SIZE_MODIFIER);
		}
		
		MODIFY_SIZE_TYPE.getScaleChangedEvent().register(s -> {
			Entity e = s.getEntity();
			
			if (e != null) {
				boolean onGround = e.isOnGround();
				e.calculateDimensions();
				e.setOnGround(onGround);
				
				for (ScaleType scaleType : ScaleRegistries.SCALE_TYPES.values()) {
					ScaleData data = scaleType.getScaleData(e);
					
					if (data.getBaseValueModifiers().contains(MODIFY_SIZE_MODIFIER)) {
						data.markForSync(true);
					}
				}
			}
		});
	}
}
