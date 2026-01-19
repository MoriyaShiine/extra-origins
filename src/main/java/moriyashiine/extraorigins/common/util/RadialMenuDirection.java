/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.extraorigins.common.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;

public enum RadialMenuDirection {
	UP(0),
	DOWN(1),
	LEFT(2),
	RIGHT(3);

	private static final IntFunction<RadialMenuDirection> ID_TO_VALUE_FUNCTION = ValueLists.createIdToValueFunction(RadialMenuDirection::getId, values(), ValueLists.OutOfBoundsHandling.WRAP);
	public static final PacketCodec<ByteBuf, RadialMenuDirection> PACKET_CODEC = PacketCodecs.indexed(ID_TO_VALUE_FUNCTION, RadialMenuDirection::getId);

	private final int id;

	RadialMenuDirection(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
