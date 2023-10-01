/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin {
	@ModifyVariable(method = "raycast", at = @At("HEAD"), argsOnly = true)
	private static Predicate<Entity> extraorigins$preventHittingPlayerMount(Predicate<Entity> value) {
		return value.and(entity -> entity.getRootVehicle() != MinecraftClient.getInstance().player);
	}
}
