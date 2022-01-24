/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.common.registry;

import moriyashiine.extraorigins.common.ExtraOrigins;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModParticleTypes {
	public static final ParticleType<DefaultParticleType> SPORE = FabricParticleTypes.simple();

	public static void init() {
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(ExtraOrigins.MOD_ID, "spore"), SPORE);
	}
}
