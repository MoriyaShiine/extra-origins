/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.ModPowers;
import moriyashiine.extraorigins.common.registry.ModTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract Item getItem();

	@Shadow
	public abstract boolean damage(int amount, Random random, @Nullable ServerPlayerEntity player);

	@Shadow
	public abstract boolean isIn(TagKey<Item> tag);

	@Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At("HEAD"), cancellable = true)
	private <T extends LivingEntity> void extraorigins$increaseGoldToolDurability(int amount, T entity, Consumer<T> breakCallback, CallbackInfo ci) {
		if (!entity.getWorld().isClient && ModPowers.ALL_THAT_GLITTERS.get(entity) != null && !(entity instanceof PlayerEntity player && player.isCreative())) {
			if (getItem() instanceof ToolItem) {
				if (isIn(ModTags.Items.GOLDEN_TOOLS)) {
					if (entity.getWorld().random.nextFloat() < 15 / 16F) {
						ci.cancel();
					}
				} else if (entity.getRandom().nextBoolean() && !isIn(ModTags.Items.NETHERITE_TOOLS)) {
					damage(amount, entity.getRandom(), null);
				}
			}
			if (getItem() instanceof ArmorItem) {
				if (isIn(ModTags.Items.GOLDEN_ARMOR)) {
					if (entity.getWorld().random.nextFloat() < 3 / 4F) {
						ci.cancel();
					}
				} else if (entity.getRandom().nextBoolean() && !isIn(ModTags.Items.NETHERITE_ARMOR)) {
					damage(amount, entity.getRandom(), null);
				}
			}
		}
	}
}
