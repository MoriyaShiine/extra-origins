package moriyashiine.respawnablepets.common.world;

import moriyashiine.respawnablepets.common.RespawnablePets;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RPWorldState extends PersistentState {
	public static final String KEY = RespawnablePets.MODID;
	
	public final List<CompoundTag> storedPets = new ArrayList<>();
	public final List<UUID> petsToRespawn = new ArrayList<>();
	
	public RPWorldState(String key) {
		super(key);
	}
	
	@Override
	public void fromTag(CompoundTag tag) {
		ListTag storedPets = tag.getList("StoredPets", NbtType.COMPOUND);
		for (int i = 0; i < storedPets.size(); i++) {
			this.storedPets.add(storedPets.getCompound(i));
		}
		ListTag petsToRespawn = tag.getList("PetsToRespawn", NbtType.COMPOUND);
		for (int i = 0; i < petsToRespawn.size(); i++) {
			this.petsToRespawn.add(petsToRespawn.getCompound(i).getUuid("UUID"));
		}
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		ListTag storedPets = new ListTag();
		storedPets.addAll(this.storedPets);
		tag.put("StoredPets", storedPets);
		ListTag petsToRespawn = new ListTag();
		for (UUID uuid : this.petsToRespawn) {
			CompoundTag pet = new CompoundTag();
			pet.putUuid("UUID", uuid);
			petsToRespawn.add(pet);
		}
		tag.put("PetsToRespawn", petsToRespawn);
		return tag;
	}
	
	public static RPWorldState get(World world) {
		return Objects.requireNonNull(Objects.requireNonNull(world.getServer()).getWorld(World.OVERWORLD)).getPersistentStateManager().getOrCreate(() -> new RPWorldState(KEY), KEY);
	}
}