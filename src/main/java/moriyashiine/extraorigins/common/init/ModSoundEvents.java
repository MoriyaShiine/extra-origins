/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common.init;

import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class ModSoundEvents {
	public static final SoundEvent BLOCK_MYCELIUM_GROW = registerSoundEvent("block.mycelium.grow");
	public static final SoundEvent ENTITY_GENERIC_SPORE_SHIFT = registerSoundEvent("entity.generic.spore_shift");

	private static SoundEvent registerSoundEvent(String name) {
		SoundEvent soundEvent = SoundEvent.of(ExtraOrigins.id(name));
		return Registry.register(Registries.SOUND_EVENT, soundEvent.getId(), soundEvent);
	}

	public static void init() {
	}
}
