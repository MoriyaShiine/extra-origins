package moriyashiine.extraorigins.common.mixin;

import io.github.apace100.origins.power.Power;
import io.github.apace100.origins.power.PowerType;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BiFunction;

@Mixin(PowerType.class)
public interface PowerTypeAccessor {
	@Invoker(value = "<init>")
	static <T extends Power> PowerType<T> createPowerType(BiFunction<PowerType<T>, PlayerEntity, T> factory) {
		throw new UnsupportedOperationException();
	}
}