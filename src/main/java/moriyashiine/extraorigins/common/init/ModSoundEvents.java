/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.common.init;

import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class ModSoundEvents {
	public static final SoundEvent BLOCK_MYCELIUM_GROW = SoundEvent.of(ExtraOrigins.id("block.mycelium.grow"));
	public static final SoundEvent ENTITY_GENERIC_SPORE_SHIFT = SoundEvent.of(ExtraOrigins.id("entity.generic.spore_shift"));

	public static void init() {
		Registry.register(Registries.SOUND_EVENT, BLOCK_MYCELIUM_GROW.getId(), BLOCK_MYCELIUM_GROW);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_SPORE_SHIFT.getId(), ENTITY_GENERIC_SPORE_SHIFT);
	}
}
