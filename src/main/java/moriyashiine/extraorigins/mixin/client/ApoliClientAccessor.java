/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin.client;

import io.github.apace100.apoli.ApoliClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;

@Mixin(value = ApoliClient.class, remap = false)
public interface ApoliClientAccessor {
	@Accessor("idToKeyBindingMap")
	static HashMap<String, KeyBinding> extraorigins$idToKeyBindingMap() {
		throw new UnsupportedOperationException();
	}
}
