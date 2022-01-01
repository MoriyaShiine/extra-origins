/*
 * All Rights Reserved (c) 2021-2022 MoriyaShiine
 */

package moriyashiine.extraorigins.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.origins.OriginsClient;
import moriyashiine.extraorigins.client.network.packet.DismountPacket;
import moriyashiine.extraorigins.client.network.packet.MountS2CPacket;
import moriyashiine.extraorigins.client.particle.SporeParticle;
import moriyashiine.extraorigins.common.ExtraOrigins;
import moriyashiine.extraorigins.common.component.entity.MagicSporesComponent;
import moriyashiine.extraorigins.common.network.packet.ChangeSporePacket;
import moriyashiine.extraorigins.common.power.MagicSporesPower;
import moriyashiine.extraorigins.common.registry.ModComponents;
import moriyashiine.extraorigins.common.registry.ModParticleTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

@Environment(EnvType.CLIENT)
public class ExtraOriginsClient implements ClientModInitializer {
	private static MagicSporesComponent.Mode targetMode = null;
	private static boolean renderModeSwitch = false;
	private static int timer = 0;
	
	@Override
	public void onInitializeClient() {
		registerPackets();
		registerEvents();
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.SPORE, SporeParticle.Factory::new);
	}
	
	private void registerPackets() {
		ClientPlayNetworking.registerGlobalReceiver(MountS2CPacket.ID, MountS2CPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(DismountPacket.ID, DismountPacket::receive);
	}
	
	private void registerEvents() {
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
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
				if (OriginsClient.usePrimaryActivePowerKeybind.isPressed() && PowerHolderComponent.hasPower(client.player, MagicSporesPower.class)) {
					client.mouse.unlockCursor();
					changeTargetMode(client);
					handleModeChange(client);
					renderModeSwitch = true;
				}
				else {
					client.mouse.lockCursor();
					targetMode = null;
					renderModeSwitch = false;
					timer = 0;
				}
			}
			
			private void changeTargetMode(MinecraftClient client) {
				double x = client.mouse.getX() - (client.getWindow().getWidth() / 2F);
				double y = (client.getWindow().getHeight() / 2F) - client.mouse.getY();
				if (Math.abs(x) > 48 || Math.abs(y) > 48) {
					MagicSporesComponent.Mode modeFromDirection = switch (Direction.getFacing(x, y, 0)) {
						case UP -> MagicSporesComponent.Mode.MOBILITY;
						case WEST -> MagicSporesComponent.Mode.OFFENSE;
						case EAST -> MagicSporesComponent.Mode.DEFENSE;
						default -> null;
					};
					if (targetMode != modeFromDirection) {
						targetMode = modeFromDirection;
						timer = 0;
					}
				}
				else {
					targetMode = null;
					timer = 0;
				}
			}
			
			@SuppressWarnings("ConstantConditions")
			private void handleModeChange(MinecraftClient client) {
				if (targetMode != null && ModComponents.MAGIC_SPORES.get(client.player).getMode() != targetMode) {
					timer++;
					if (timer == 20) {
						ChangeSporePacket.send(targetMode);
						timer = 0;
					}
				}
			}
		});
		HudRenderCallback.EVENT.register(new HudRenderCallback() {
			private static final Identifier ICONS = new Identifier(ExtraOrigins.MOD_ID, "textures/gui/icons.png");
			
			@Override
			public void onHudRender(MatrixStack matrixStack, float tickDelta) {
				if (!renderModeSwitch) {
					return;
				}
				RenderSystem.setShaderTexture(0, ICONS);
				//outline
				MinecraftClient.getInstance().inGameHud.drawTexture(matrixStack, (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - 64, (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) - 64, 0, 0, 128, 128);
				//left
				renderSection(MagicSporesComponent.Mode.OFFENSE, matrixStack, -67, -28, 128);
				//right
				renderSection(MagicSporesComponent.Mode.DEFENSE, matrixStack, 34, -32, 160);
				//up
				renderSection(MagicSporesComponent.Mode.MOBILITY, matrixStack, -16, -71, 192);
				//progress
				if (timer > 0) {
					MinecraftClient.getInstance().inGameHud.drawTexture(matrixStack, (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - 13, (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) - 13, 48, 128, 26, 26);
					MinecraftClient.getInstance().inGameHud.drawTexture(matrixStack, (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - 12, (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) - 12, 24, 128, 24, 24);
					MinecraftClient.getInstance().inGameHud.drawTexture(matrixStack, (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - 12, (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) - 12, 0, 128, 24, (int) (24 - 24 * ((timer + 1) / 20f)));
				}
				RenderSystem.setShaderTexture(0, InGameHud.GUI_ICONS_TEXTURE);
			}
			
			@SuppressWarnings("ConstantConditions")
			private void renderSection(MagicSporesComponent.Mode targetMode, MatrixStack matrixStack, int posXOffset, int posYOffset, int u) {
				int v = 0;
				if (ModComponents.MAGIC_SPORES.get(MinecraftClient.getInstance().player).getMode() == targetMode) {
					v += 64;
				}
				else if (ExtraOriginsClient.targetMode == targetMode) {
					v += 32;
				}
				MinecraftClient.getInstance().inGameHud.drawTexture(matrixStack, (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) + posXOffset, (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) + posYOffset, u, v, 32, 32);
			}
		});
	}
}
