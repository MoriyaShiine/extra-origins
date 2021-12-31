/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.registry;

import moriyashiine.extraorigins.common.ExtraOrigins;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSoundEvents {
	public static final SoundEvent BLOCK_MYCELIUM_GROW = new SoundEvent(new Identifier(ExtraOrigins.MOD_ID, "block.mycelium.grow"));
	public static final SoundEvent ENTITY_GENERIC_SPORE_SHIFT = new SoundEvent(new Identifier(ExtraOrigins.MOD_ID, "entity.generic.spore_shift"));
	
	public static void init() {
		Registry.register(Registry.SOUND_EVENT, BLOCK_MYCELIUM_GROW.getId(), BLOCK_MYCELIUM_GROW);
		Registry.register(Registry.SOUND_EVENT, ENTITY_GENERIC_SPORE_SHIFT.getId(), ENTITY_GENERIC_SPORE_SHIFT);
	}
}
