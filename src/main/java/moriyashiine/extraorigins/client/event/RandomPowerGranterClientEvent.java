/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.client.event;

import io.github.apace100.apoli.power.PowerManager;
import io.github.apace100.origins.Origins;
import moriyashiine.extraorigins.common.component.entity.RandomPowerGranterComponent;
import moriyashiine.extraorigins.common.init.ModEntityComponents;
import moriyashiine.extraorigins.common.powertype.RandomPowerGranterPowerType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class RandomPowerGranterClientEvent {
	public static final DisplayInstance[] DISPLAY_INSTANCES = new DisplayInstance[3];
	public static final String[] CHARACTERS = {"α", "β", "γ"};
	public static final Formatting[] FORMATTING = {Formatting.GREEN, Formatting.LIGHT_PURPLE, Formatting.GOLD};

	public static class ClientTick implements ClientTickEvents.EndWorldTick {
		@Override
		public void onEndTick(ClientWorld world) {
			for (int i = 0; i < DISPLAY_INSTANCES.length; i++) {
				DisplayInstance displayInstance = DISPLAY_INSTANCES[i];
				if (displayInstance != null && --displayInstance.duration == 0) {
					DISPLAY_INSTANCES[i] = null;
				}
			}
		}
	}

	public static class Hud implements HudRenderCallback {
		private static final MinecraftClient client = MinecraftClient.getInstance();
		private static final Identifier TEXTURE = Origins.identifier("textures/gui/community/spiderkolo/resource_bar_01.png");

		private static final int[] VS = {150, 120, 110};

		@Override
		public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
			if (client.player != null) {
				RandomPowerGranterComponent randomPowerGranterComponent = ModEntityComponents.RANDOM_POWER_GRANTER.get(client.player);
				if (randomPowerGranterComponent.isEnabled()) {
					for (int i = 0; i < randomPowerGranterComponent.getTemporaryPowers().length; i++) {
						int y = 4 + (i * 10);
						drawContext.drawText(client.textRenderer, CHARACTERS[i], 4, y, FORMATTING[i].getColorValue(), true);
						drawContext.drawTexture(TEXTURE, 12, y + 2, 0, 0, 71, 5, 256, 256);
						drawContext.drawTexture(TEXTURE, 12, y + 2, 0, VS[i], (int) (71 * (randomPowerGranterComponent.getTemporaryPowers()[i].getProgress())), 5, 256, 256);
						DisplayInstance displayInstance = DISPLAY_INSTANCES[i];
						if (displayInstance != null) {
							Text display;
							if (displayInstance.oldPower.getString().isEmpty()) {
								display = displayInstance.newPower;
							} else {
								display = displayInstance.oldPower.append(" -> ").append(displayInstance.newPower);
							}
							drawContext.setShaderColor(1, 1, 1, displayInstance.getOpacity());
							drawContext.drawText(client.textRenderer, display, 86, y, FORMATTING[i].getColorValue(), true);
						}
						drawContext.setShaderColor(1, 1, 1, 1);
					}
				}
			}
		}
	}

	public static class DisplayInstance {
		private final MutableText oldPower, newPower;
		private int duration;
		private final float maxDuration;

		public DisplayInstance(Identifier oldPower, Identifier newPower, int duration) {
			if (oldPower.equals(RandomPowerGranterPowerType.INITIAL_ID)) {
				this.oldPower = Text.empty();
			} else {
				this.oldPower = PowerManager.get(oldPower).getName();
			}
			this.newPower = PowerManager.get(newPower).getName();
			this.duration = duration;
			this.maxDuration = duration;
		}

		private float getOpacity() {
			if (duration < 10) {
				return duration / 10F;
			}
			if (duration > maxDuration - 10) {
				return 1 - (duration - (maxDuration - 10)) / 10F;
			}
			return 1;
		}
	}
}
