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

package net.fabricmc.fabric.impl.container;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import io.netty.buffer.Unpooled;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.mixin.container.ServerPlayerEntityAccessor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ContainerProviderImpl implements ContainerProviderRegistry {
	public static final ResourceLocation OPEN_CONTAINER = new ResourceLocation("fabric", "container/open");

	private static final Logger LOGGER = LoggerFactory.getLogger(ContainerProviderImpl.class);

	private static final Map<ResourceLocation, ContainerFactory<AbstractContainerMenu>> FACTORIES = new HashMap<>();

	@Override
	public void registerFactory(ResourceLocation identifier, ContainerFactory<AbstractContainerMenu> factory) {
		if (FACTORIES.containsKey(identifier)) {
			throw new RuntimeException("A factory has already been registered as " + identifier.toString());
		}

		FACTORIES.put(identifier, factory);
	}

	@Override
	public void openContainer(ResourceLocation identifier, Player player, Consumer<FriendlyByteBuf> writer) {
		if (!(player instanceof ServerPlayer)) {
			LOGGER.warn("Please only use ContainerProviderRegistry.openContainer() with server-sided player entities!");
			return;
		}

		openContainer(identifier, (ServerPlayer) player, writer);
	}

	private boolean emittedNoSyncHookWarning = false;

	@Override
	public void openContainer(ResourceLocation identifier, ServerPlayer player, Consumer<FriendlyByteBuf> writer) {
		int syncId;

		if (player instanceof ServerPlayerEntitySyncHook) {
			ServerPlayerEntitySyncHook serverPlayerEntitySyncHook = (ServerPlayerEntitySyncHook) player;
			syncId = serverPlayerEntitySyncHook.fabric_incrementSyncId();
		} else if (player instanceof ServerPlayerEntityAccessor) {
			if (!emittedNoSyncHookWarning) {
				LOGGER.warn("ServerPlayerEntitySyncHook could not be applied - fabric-containers is using a hack!");
				emittedNoSyncHookWarning = true;
			}

			syncId = (((ServerPlayerEntityAccessor) player).getScreenHandlerSyncId() + 1) % 100;
			((ServerPlayerEntityAccessor) player).setScreenHandlerSyncId(syncId);
		} else {
			throw new RuntimeException("Neither ServerPlayerEntitySyncHook nor Accessor present! This should not happen!");
		}

		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeResourceLocation(identifier);
		buf.writeByte(syncId);

		writer.accept(buf);
		player.connection.send(new ClientboundCustomPayloadPacket(OPEN_CONTAINER, buf));

		FriendlyByteBuf clonedBuf = new FriendlyByteBuf(buf.duplicate());
		clonedBuf.readResourceLocation();
		clonedBuf.readUnsignedByte();

		AbstractContainerMenu screenHandler = createContainer(syncId, identifier, player, clonedBuf);

		if (screenHandler == null) {
			return;
		}

		player.containerMenu = screenHandler;
		((ServerPlayerEntityAccessor) player).callOnScreenHandlerOpened(screenHandler);
	}

	public <C extends AbstractContainerMenu> C createContainer(int syncId, ResourceLocation identifier, Player player, FriendlyByteBuf buf) {
		ContainerFactory<AbstractContainerMenu> factory = FACTORIES.get(identifier);

		if (factory == null) {
			LOGGER.error("No container factory found for {}!", identifier.toString());
			return null;
		}

		//noinspection unchecked
		return (C) factory.create(syncId, identifier, player, buf);
	}
}
