package moriyashiine.extraorigins.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Predicate;

@Mixin(TargetPredicate.class)
public interface TargetPredicateAccessor {
	@Accessor("predicate")
	Predicate<LivingEntity> eo_getTargetPredicate();
}
