/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.extraorigins.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.packet.ChangeRadialDirectionPacket;
import moriyashiine.extraorigins.common.power.RadialMenuPower;
import moriyashiine.extraorigins.common.util.RadialMenuDirection;
import moriyashiine.extraorigins.mixin.client.ApoliClientAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

import java.util.List;

@Environment(EnvType.CLIENT)
public class RadialMenuEvents {
	public static boolean directionChanged = false;

	private static List<RadialMenuPower> activePowers;
	private static RadialMenuPower lastUsedPower;
	private static RadialMenuDirection targetDirection = null;
	private static boolean renderModeSwitch = false;
	private static int timer = 0;

	public static void init() {
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			activePowers = null;
			lastUsedPower = null;
			directionChanged = false;
			targetDirection = null;
			renderModeSwitch = false;
			timer = 0;
		});
		ClientTickEvents.END_CLIENT_TICK.register(new ClientTickEvents.EndTick() {
			@Override
			public void onEndTick(MinecraftClient client) {
				if (client.currentScreen != null) {
					return;
				}
				activePowers = PowerHolderComponent.getPowers(client.player, RadialMenuPower.class).stream().filter(power -> ApoliClientAccessor.extraorigins$idToKeyBindingMap().containsKey(power.getKey().key) && ApoliClientAccessor.extraorigins$idToKeyBindingMap().get(power.getKey().key).isPressed()).toList();
				if (!activePowers.isEmpty()) {
					client.mouse.unlockCursor();
					changeTargetMode(client);
					handleModeChange();
					renderModeSwitch = true;
				} else {
					client.mouse.lockCursor();
					lastUsedPower = null;
					directionChanged = false;
					targetDirection = null;
					renderModeSwitch = false;
					timer = 0;
				}
			}

			private void changeTargetMode(MinecraftClient client) {
				double x = client.mouse.getX() - (client.getWindow().getWidth() / 2F);
				double y = (client.getWindow().getHeight() / 2F) - client.mouse.getY();
				if (Math.abs(x) > 48 || Math.abs(y) > 48) {
					RadialMenuDirection direction = switch (Direction.getFacing(x, y, 0)) {
						case UP -> RadialMenuDirection.UP;
						case DOWN -> RadialMenuDirection.DOWN;
						case WEST -> RadialMenuDirection.LEFT;
						case EAST -> RadialMenuDirection.RIGHT;
						default -> null;
					};
					if (targetDirection != direction) {
						targetDirection = direction;
						timer = 0;
					}
				} else {
					targetDirection = null;
					timer = 0;
				}
			}

			private void handleModeChange() {
				if (activePowers.get(0) != lastUsedPower) {
					timer = 0;
					lastUsedPower = activePowers.get(0);
				}
				if (targetDirection != null && lastUsedPower.getDirection() != targetDirection && lastUsedPower.getActionFromDirection(targetDirection) != null) {
					if (!directionChanged) {
						timer++;
					}
					if (timer == lastUsedPower.swapTime) {
						directionChanged = true;
						timer = 0;
						ChangeRadialDirectionPacket.send(targetDirection, lastUsedPower.getType());
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
				MinecraftClient client = MinecraftClient.getInstance();
				RenderSystem.setShaderTexture(0, lastUsedPower.spriteLocation);
				DrawableHelper.drawTexture(matrixStack, (client.getWindow().getScaledWidth() / 2) - 64, (client.getWindow().getScaledHeight() / 2) - 64, 0, 0, 128, 128, 320, 256);
				renderSection(RadialMenuDirection.UP, matrixStack, -32, -80, 0);
				renderSection(RadialMenuDirection.DOWN, matrixStack, -32, 16, 64);
				renderSection(RadialMenuDirection.LEFT, matrixStack, -80, -32, 128);
				renderSection(RadialMenuDirection.RIGHT, matrixStack, 16, -32, 192);
				if (timer > 0) {
					DrawableHelper.drawTexture(matrixStack, (client.getWindow().getScaledWidth() / 2) - 13, (client.getWindow().getScaledHeight() / 2) - 13, 48, 128, 26, 26, 320, 256);
					DrawableHelper.drawTexture(matrixStack, (client.getWindow().getScaledWidth() / 2) - 12, (client.getWindow().getScaledHeight() / 2) - 12, 24, 128, 24, 24, 320, 256);
					DrawableHelper.drawTexture(matrixStack, (client.getWindow().getScaledWidth() / 2) - 12, (client.getWindow().getScaledHeight() / 2) - 12, 0, 128, 24, (int) (24 - 24 * ((timer + 1) / (float) lastUsedPower.swapTime)), 320, 256);
				}
				RenderSystem.setShaderTexture(0, InGameHud.GUI_ICONS_TEXTURE);
			}

			private void renderSection(RadialMenuDirection targetMode, MatrixStack matrixStack, int posXOffset, int posYOffset, int v) {
				if (lastUsedPower.getActionFromDirection(targetMode) == null) {
					return;
				}
				int u = 128;
				if (lastUsedPower.getDirection() == targetMode) {
					u += 128;
				} else if (targetDirection == targetMode) {
					u += 64;
				}
				DrawableHelper.drawTexture(matrixStack, (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) + posXOffset, (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) + posYOffset, u, v, 64, 64, 320, 256);
			}
		});
	}
}
