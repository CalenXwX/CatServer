/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.impl.screenhandler;

import java.util.Objects;

import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public final class Networking {
	private static final Logger LOGGER = LoggerFactory.getLogger("fabric-screen-handler-api-v1/server");

	// [Packet format]
	// typeId: identifier
	// syncId: varInt
	// title: text
	// customData: buf
	public static final ResourceLocation OPEN_ID = new ResourceLocation("fabric-screen-handler-api-v1", "open_screen");

	/**
	 * Opens an extended screen handler by sending a custom packet to the client.
	 *
	 * @param player  the player
	 * @param factory the screen handler factory
	 * @param handler the screen handler instance
	 * @param syncId  the synchronization ID
	 */
	public static void sendOpenPacket(ServerPlayer player, ExtendedScreenHandlerFactory factory, AbstractContainerMenu handler, int syncId) {
		Objects.requireNonNull(player, "player is null");
		Objects.requireNonNull(factory, "factory is null");
		Objects.requireNonNull(handler, "handler is null");

		ResourceLocation typeId = BuiltInRegistries.MENU.getKey(handler.getType());

		if (typeId == null) {
			LOGGER.warn("Trying to open unregistered screen handler {}", handler);
			return;
		}

		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeResourceLocation(typeId);
		buf.writeVarInt(syncId);
		buf.writeComponent(factory.getDisplayName());
		factory.writeScreenOpeningData(player, buf);

		ServerPlayNetworking.send(player, OPEN_ID, buf);
	}
}
