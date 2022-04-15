package moriyashiine.extraorigins.mixin.client;

import io.github.apace100.apoli.ApoliClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
@Mixin(ApoliClient.class)
public interface ApoliClientAccessor {
    @Accessor(value = "idToKeyBindingMap", remap = false)
    static HashMap<String, KeyBinding> eo_getIdToKeybindingMap() {
        throw new RuntimeException("");
    }
}
