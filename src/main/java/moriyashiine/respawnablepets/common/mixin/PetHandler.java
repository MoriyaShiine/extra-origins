package moriyashiine.respawnablepets.common.mixin;

import moriyashiine.respawnablepets.client.network.message.SmokePuffMessage;
import moriyashiine.respawnablepets.common.RespawnablePets;
import moriyashiine.respawnablepets.common.world.RPWorldState;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.Tag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class PetHandler extends Entity {
	private static final Tag<EntityType<?>> BLACKLIST = TagRegistry.entityType(new Identifier(RespawnablePets.MODID, "blacklisted"));
	
	@Shadow
	public abstract DamageTracker getDamageTracker();
	
	@Shadow
	public abstract float getHealth();
	
	public PetHandler(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void toggleRespawn(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!world.isClient) {
			Entity attacker = source.getSource();
			if (attacker instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) attacker;
				if (player.getMainHandStack().getItem() == RespawnablePets.ETHERIC_GEM) {
					CompoundTag stored = toTag(new CompoundTag());
					if (stored.containsUuid("Owner") && player.getUuid().equals(stored.getUuid("Owner"))) {
						if (BLACKLIST.contains(getType())) {
							player.sendMessage(new TranslatableText("message." + RespawnablePets.MODID + ".blacklisted", getDisplayName()), true);
						}
						else {
							RPWorldState rpWorldState = RPWorldState.get(world);
							Object obj = this;
							if (isPetRespawnable(rpWorldState, (LivingEntity) obj)) {
								player.sendMessage(new TranslatableText("message." + RespawnablePets.MODID + ".disable_respawn", getDisplayName()), true);
								for (int i = rpWorldState.petsToRespawn.size() - 1; i >= 0; i--) {
									if (rpWorldState.petsToRespawn.get(i).equals(getUuid())) {
										rpWorldState.petsToRespawn.remove(i);
										rpWorldState.markDirty();
									}
								}
							}
							else {
								player.sendMessage(new TranslatableText("message." + RespawnablePets.MODID + ".enable_respawn", getDisplayName()), true);
								rpWorldState.petsToRespawn.add(getUuid());
								rpWorldState.markDirty();
							}
							
						}
					}
					else {
						player.sendMessage(new TranslatableText("message." + RespawnablePets.MODID + ".not_owner", getDisplayName()), true);
					}
					callbackInfo.cancel();
				}
			}
		}
	}
	
	@Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
	private void storeToWorld(DamageSource source, float amount, CallbackInfo callbackInfo) {
		if (!world.isClient) {
			RPWorldState rpWorldState = RPWorldState.get(world);
			Object obj = this;
			if (getHealth() - amount <= 0 && isPetRespawnable(rpWorldState, (LivingEntity) obj)) {
				CompoundTag stored = new CompoundTag();
				saveSelfToTag(stored);
				rpWorldState.storedPets.add(stored);
				rpWorldState.markDirty();
				PlayerStream.watching(this).forEach(foundPlayer -> SmokePuffMessage.send(foundPlayer, getEntityId()));
				world.playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1, 1);
				remove();
				PlayerEntity owner = findPlayer(world, stored.getUuid("Owner"));
				if (owner != null && world.getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES)) {
					owner.sendMessage(getDamageTracker().getDeathMessage(), false);
				}
				callbackInfo.cancel();
			}
		}
	}
	
	@Inject(method = "wakeUp", at = @At("HEAD"))
	private void respawnPets(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			RPWorldState rpWorldState = RPWorldState.get(world);
			for (int i = rpWorldState.storedPets.size() - 1; i >= 0; i--) {
				CompoundTag nbt = rpWorldState.storedPets.get(i);
				if (getUuid().equals(nbt.getUuid("Owner"))) {
					LivingEntity pet = (LivingEntity) Registry.ENTITY_TYPE.get(new Identifier(nbt.getString("id"))).create(world);
					if (pet != null) {
						pet.fromTag(nbt);
						pet.setWorld(world);
						pet.teleport(getX() + 0.5, getY() + 0.5, getZ() + 0.5);
						pet.removed = false;
						pet.setHealth(pet.getMaxHealth());
						pet.extinguish();
						pet.clearStatusEffects();
						pet.fallDistance = 0;
						world.spawnEntity(pet);
						rpWorldState.storedPets.remove(i);
						rpWorldState.markDirty();
					}
				}
			}
		}
	}
	
	private static PlayerEntity findPlayer(World world, UUID uuid) {
		for (ServerWorld serverWorld : Objects.requireNonNull(world.getServer()).getWorlds()) {
			PlayerEntity player = serverWorld.getPlayerByUuid(uuid);
			if (player != null) {
				return player;
			}
		}
		return null;
	}
	
	private static boolean isPetRespawnable(RPWorldState rpWorldState, LivingEntity entity) {
		for (UUID uuid : rpWorldState.petsToRespawn) {
			if (entity.getUuid().equals(uuid)) {
				return true;
			}
		}
		return false;
	}
}