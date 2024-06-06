/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.extraorigins.common.init;

import moriyashiine.extraorigins.common.ExtraOrigins;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticleTypes {
	public static final ParticleType<DefaultParticleType> OFFENSE_SPORE = FabricParticleTypes.simple();
	public static final ParticleType<DefaultParticleType> DEFENSE_SPORE = FabricParticleTypes.simple();
	public static final ParticleType<DefaultParticleType> MOBILITY_SPORE = FabricParticleTypes.simple();

	public static void init() {
		Registry.register(Registries.PARTICLE_TYPE, ExtraOrigins.id("offense_spore"), OFFENSE_SPORE);
		Registry.register(Registries.PARTICLE_TYPE, ExtraOrigins.id("defense_spore"), DEFENSE_SPORE);
		Registry.register(Registries.PARTICLE_TYPE, ExtraOrigins.id("mobility_spore"), MOBILITY_SPORE);
	}
}
