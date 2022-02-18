/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.packet.ChangeSporePacket;
import moriyashiine.extraorigins.common.power.MagicSporesPower;
import moriyashiine.extraorigins.common.registry.ModComponents;
import moriyashiine.extraorigins.common.util.MagicSporeOption;
import moriyashiine.extraorigins.mixin.client.ApoliClientAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

import java.util.List;

@Environment(EnvType.CLIENT)
public class MagicSporesHandler {
	public static boolean sporeChanged = false;

	private static MagicSporeOption targetMode = null;
	private static boolean renderModeSwitch = false;
	private static int timer = 0;
	private static MagicSporesPower lastUsedPower;
	private static List<MagicSporesPower> activePowers;

	public static void init() {
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			sporeChanged = false;
			targetMode = null;
			renderModeSwitch = false;
			timer = 0;
		});
		ClientTickEvents.END_CLIENT_TICK.register(new ClientTickEvents.EndTick() {
			@Override
			public void onEndTick(MinecraftClient client) {
				if (client.currentScreen != null) {
					return;
				}
				activePowers = PowerHolderComponent.getPowers(client.player, MagicSporesPower.class).stream().filter(power -> ApoliClientAccessor.eo_getIdToKeybindingMap().containsKey(power.getKey().key) && ApoliClientAccessor.eo_getIdToKeybindingMap().get(power.getKey().key).isPressed()).toList();
				if (!activePowers.isEmpty()) {
					client.mouse.unlockCursor();
					changeTargetMode(client);
					handleModeChange(client);
					renderModeSwitch = true;
				} else {
					client.mouse.lockCursor();
					sporeChanged = false;
					targetMode = null;
					renderModeSwitch = false;
					timer = 0;
				}
			}

			private void changeTargetMode(MinecraftClient client) {
				double x = client.mouse.getX() - (client.getWindow().getWidth() / 2F);
				double y = (client.getWindow().getHeight() / 2F) - client.mouse.getY();
				if (Math.abs(x) > 48 || Math.abs(y) > 48) {
					MagicSporeOption modeFromDirection = switch (Direction.getFacing(x, y, 0)) {
						case UP -> MagicSporeOption.UP;
						case WEST -> MagicSporeOption.LEFT;
						case EAST -> MagicSporeOption.RIGHT;
						default -> null;
					};
					if (targetMode != modeFromDirection) {
						targetMode = modeFromDirection;
						timer = 0;
					}
				} else {
					targetMode = null;
					timer = 0;
				}
			}

			@SuppressWarnings("ConstantConditions")
			private void handleModeChange(MinecraftClient client) {
				if (activePowers.get(0) != lastUsedPower) {
					timer = 0;
					lastUsedPower = activePowers.get(0);
				}
				if (targetMode != null && (!lastUsedPower.storeOption || lastUsedPower.getStoredOption() != targetMode)) {
					if (lastUsedPower.upAction == null && targetMode == MagicSporeOption.UP || lastUsedPower.rightAction == null && targetMode == MagicSporeOption.RIGHT || lastUsedPower.leftAction == null && targetMode == MagicSporeOption.LEFT) {
						return;
					}
					if (!sporeChanged) {
						timer++;
					}
					if (timer == lastUsedPower.swapTime) {
						sporeChanged = true;
						ChangeSporePacket.send(targetMode, lastUsedPower.getType());
						timer = 0;
					}
				}
			}
		});
		HudRenderCallback.EVENT.register(new HudRenderCallback() {

			@Override
			public void onHudRender(MatrixStack matrixStack, float tickDelta) {
				if (!renderModeSwitch) {
					return;
				}
				RenderSystem.setShaderTexture(0, lastUsedPower.spriteLocation);
				//outline
				MinecraftClient.getInstance().inGameHud.drawTexture(matrixStack, (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - 64, (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) - 64, 0, 0, 128, 128);
				//left
				renderSection(MagicSporeOption.LEFT, matrixStack, -67, -28, 128);
				//right
				renderSection(MagicSporeOption.RIGHT, matrixStack, 34, -32, 160);
				//up
				renderSection(MagicSporeOption.UP, matrixStack, -16, -71, 192);
				//progress
				if (timer > 0) {
					MinecraftClient.getInstance().inGameHud.drawTexture(matrixStack, (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - 13, (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) - 13, 48, 128, 26, 26);
					MinecraftClient.getInstance().inGameHud.drawTexture(matrixStack, (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - 12, (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) - 12, 24, 128, 24, 24);
					MinecraftClient.getInstance().inGameHud.drawTexture(matrixStack, (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - 12, (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) - 12, 0, 128, 24, (int) (24 - 24 * ((timer + 1) / (float)lastUsedPower.swapTime)));
				}
				RenderSystem.setShaderTexture(0, InGameHud.GUI_ICONS_TEXTURE);
			}

			@SuppressWarnings("ConstantConditions")
			private void renderSection(MagicSporeOption targetMode, MatrixStack matrixStack, int posXOffset, int posYOffset, int u) {
				int v = 0;
				if (lastUsedPower.getStoredOption() == targetMode) {
					v += 64;
				} else if (MagicSporesHandler.targetMode == targetMode && !(lastUsedPower.upAction == null && targetMode == MagicSporeOption.UP || lastUsedPower.leftAction == null && targetMode == MagicSporeOption.LEFT || lastUsedPower.rightAction == null && targetMode == MagicSporeOption.RIGHT)) {
					v += 32;
				}
				MinecraftClient.getInstance().inGameHud.drawTexture(matrixStack, (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) + posXOffset, (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) + posYOffset, u, v, 32, 32);
			}
		});
	}
}
