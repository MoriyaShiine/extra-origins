package moriyashiine.extraorigins.mixin;

import moriyashiine.extraorigins.common.registry.EOItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.DaylightDetectorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DaylightDetectorBlock.class)
public class LiquidSunlightHandler {
	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> callbackInfo) {
		if (state.get(Properties.POWER) > 0 && !state.get(Properties.INVERTED)) {
			ItemStack stack = player.getStackInHand(hand);
			if (stack.getItem() instanceof GlassBottleItem) {
				boolean client = world.isClient;
				if (!client) {
					if (!player.isCreative()) {
						stack.decrement(1);
					}
					ItemStack bottle = new ItemStack(EOItems.LIQUID_SUNLIGHT);
					if (!player.inventory.insertStack(bottle)) {
						player.dropItem(bottle, false);
					}
				}
				callbackInfo.setReturnValue(ActionResult.success(client));
			}
		}
	}
}
