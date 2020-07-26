package moriyashiine.respawnablepets.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class RespawnablePets implements ModInitializer {
	public static final String MODID = "respawnablepets";
	
	public static final Item ETHERIC_GEM = new Item(new Item.Settings().group(ItemGroup.MISC).rarity(Rarity.RARE).maxCount(1)) {
		@Override
		@Environment(EnvType.CLIENT)
		public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
			tooltip.add(new TranslatableText("tooltip." + RespawnablePets.MODID + ".etheric_gem").formatted(Formatting.GRAY));
		}
	};
	
	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier(MODID, "etheric_gem"), ETHERIC_GEM);
	}
}
