package moriyashiine.extraorigins.common.interfaces;

import net.minecraft.entity.Entity;

import java.util.Optional;

public interface BabyAccessor {
	static Optional<BabyAccessor> get(Entity entity) {
		if (entity instanceof BabyAccessor) {
			return Optional.of((BabyAccessor) entity);
		}
		return Optional.empty();
	}
	
	boolean getBaby();
	
	void setBaby(boolean baby);
}
