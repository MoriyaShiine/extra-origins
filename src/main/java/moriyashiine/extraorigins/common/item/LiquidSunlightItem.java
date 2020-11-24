package moriyashiine.extraorigins.common.item;

import moriyashiine.extraorigins.common.registry.EOPowers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class LiquidSunlightItem extends Item {
	public LiquidSunlightItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return Items.POTION.use(world, user, hand);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (EOPowers.PHOTOSYNTHESIS.get(user) != null && user instanceof PlayerEntity) {
			((PlayerEntity) user).getHungerManager().add(6, 0.5f);
		}
		else {
			user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1));
		}
		return Items.POTION.finishUsing(stack, world, user);
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return Items.POTION.getUseAction(stack);
	}
	
	@Override
	public SoundEvent getDrinkSound() {
		return Items.POTION.getDrinkSound();
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return Items.POTION.getMaxUseTime(stack);
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
}
