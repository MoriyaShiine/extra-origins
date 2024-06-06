/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.mixin.mount.client;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MountPower;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin {
	@ModifyVariable(method = "raycast", at = @At("HEAD"), argsOnly = true)
	private static Predicate<Entity> extraorigins$preventHittingPlayerMount(Predicate<Entity> value) {
		return value.and(entity -> entity.getRootVehicle() != MinecraftClient.getInstance().player || !PowerHolderComponent.hasPower(entity, MountPower.class));
	}
}
