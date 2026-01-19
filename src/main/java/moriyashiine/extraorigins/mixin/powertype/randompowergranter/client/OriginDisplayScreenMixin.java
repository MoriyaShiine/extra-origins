/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.mixin.powertype.randompowergranter.client;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.screen.OriginDisplayScreen;
import moriyashiine.extraorigins.client.event.RandomPowerGranterClientEvent;
import moriyashiine.extraorigins.common.component.entity.RandomPowerGranterComponent;
import moriyashiine.extraorigins.common.init.ModEntityComponents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(OriginDisplayScreen.class)
public class OriginDisplayScreenMixin extends Screen {
	@Unique
	private final List<Power> randomPowers = new ArrayList<>();

	@Unique
	private int index = 0;

	protected OriginDisplayScreenMixin(Text title) {
		super(title);
	}

	@WrapOperation(method = "renderOriginContent", at = @At(value = "INVOKE", target = "Lio/github/apace100/origins/origin/Origin;getPowers()Lcom/google/common/collect/ImmutableList;"))
	private ImmutableList<Power> extraorigins$randomPowerGranter(Origin instance, Operation<ImmutableList<Power>> original) {
		List<Power> powers = original.call(instance);
		randomPowers.clear();
		index = 0;
		if (client != null && client.player != null) {
			RandomPowerGranterComponent randomPowerGranterComponent = ModEntityComponents.RANDOM_POWER_GRANTER.get(client.player);
			if (randomPowerGranterComponent.isEnabled()) {
				powers = new ArrayList<>(powers);
				for (RandomPowerGranterComponent.TemporaryPower temporaryPower : randomPowerGranterComponent.getTemporaryPowers()) {
					powers.add(temporaryPower.getPower());
					randomPowers.add(temporaryPower.getPower());
				}
			}
		}
		return ImmutableList.copyOf(powers);
	}

	@WrapOperation(method = "renderOriginContent", at = @At(value = "INVOKE", target = "Lio/github/apace100/apoli/power/Power;getName()Lnet/minecraft/text/MutableText;"))
	private MutableText extraorigins$randomPowerGranter(Power instance, Operation<MutableText> original) {
		MutableText name = original.call(instance);
		if (randomPowers.contains(instance)) {
			name = Text.literal(RandomPowerGranterClientEvent.CHARACTERS[index] + " ").append(name);
			index++;
		}
		return name;
	}

	@WrapOperation(method = "renderOriginContent", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;"))
	private MutableText extraorigins$randomPowerGranter(MutableText instance, Formatting formatting, Operation<MutableText> original) {
		MutableText formatted = original.call(instance, formatting);
		if (index > 0) {
			return formatted.formatted(RandomPowerGranterClientEvent.FORMATTING[index - 1]);
		}
		return formatted;
	}
}
