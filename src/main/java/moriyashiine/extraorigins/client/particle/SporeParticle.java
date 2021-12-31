/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class SporeParticle extends SpriteBillboardParticle {
	protected SporeParticle(ClientWorld clientWorld, double posX, double posY, double posZ, double red, double green, double blue) {
		super(clientWorld, posX, posY, posZ, red, green, blue);
		this.colorRed = MathHelper.clamp((float) red, 0, 1);
		this.colorGreen = MathHelper.clamp((float) green, 0, 1);
		this.colorBlue = MathHelper.clamp((float) blue, 0, 1);
		setBoundingBoxSpacing(0.02F, 0.02F);
		scale *= random.nextFloat() * 0.6F + 0.5F;
		velocityX *= 0.02F;
		velocityY *= 0.02F;
		velocityZ *= 0.02F;
		maxAge = (int) (20 / (Math.random() * 0.8 + 0.2));
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}
	
	@Override
	public void tick() {
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		if (maxAge-- <= 0) {
			markDead();
			return;
		}
		move(velocityX, velocityY, velocityZ);
		velocityX *= 0.99;
		velocityY *= 0.99;
		velocityZ *= 0.99;
	}
	
	@Override
	public void move(double dx, double dy, double dz) {
		setBoundingBox(getBoundingBox().offset(dx, dy, dz));
		repositionFromBoundingBox();
	}
	
	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;
		
		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double posX, double posY, double posZ, double velocityX, double velocityY, double velocityZ) {
			SporeParticle particle = new SporeParticle(clientWorld, posX, posY, posZ, velocityX, velocityY, velocityZ);
			particle.setSprite(spriteProvider);
			return particle;
		}
	}
}
