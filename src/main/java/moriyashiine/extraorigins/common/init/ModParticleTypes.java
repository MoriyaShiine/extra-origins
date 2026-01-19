/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.init;

import moriyashiine.extraorigins.common.ExtraOrigins;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticleTypes {
	public static final SimpleParticleType OFFENSE_SPORE = register("offense_spore");
	public static final SimpleParticleType DEFENSE_SPORE = register("defense_spore");
	public static final SimpleParticleType MOBILITY_SPORE = register("mobility_spore");

	private static SimpleParticleType register(String name) {
		return Registry.register(Registries.PARTICLE_TYPE, ExtraOrigins.id(name), FabricParticleTypes.simple());
	}

	public static void init() {
	}
}
