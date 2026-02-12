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

package net.fabricmc.fabric.impl.networking.v0;

import java.util.Objects;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.PacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.networking.GenericFutureListenerHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.entity.player.Player;

public class ServerSidePacketRegistryImpl implements ServerSidePacketRegistry, PacketRegistry {
	@Override
	public boolean canPlayerReceive(Player player, ResourceLocation id) {
		if (player instanceof ServerPlayer) {
			return ServerPlayNetworking.canSend((ServerPlayer) player, id);
		}

		return false;
	}

	@Override
	public void sendToPlayer(Player player, Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> completionListener) {
		if (player instanceof ServerPlayer) {
			((ServerPlayer) player).connection.send(packet, GenericFutureListenerHolder.create(completionListener));
			return;
		}

		throw new RuntimeException("Can only send to ServerPlayerEntities!");
	}

	@Override
	public Packet<?> toPacket(ResourceLocation id, FriendlyByteBuf buf) {
		return new ClientboundCustomPayloadPacket(id, buf);
	}

	@Override
	public void register(ResourceLocation id, PacketConsumer consumer) {
		Objects.requireNonNull(consumer, "PacketConsumer cannot be null");

		ServerPlayNetworking.registerGlobalReceiver(id, (server, player, handler, buf, sender) -> {
			consumer.accept(new PacketContext() {
				@Override
				public EnvType getPacketEnvironment() {
					return EnvType.SERVER;
				}

				@Override
				public Player getPlayer() {
					return player;
				}

				@Override
				public BlockableEventLoop<?> getTaskQueue() {
					return server;
				}
			}, buf);
		});
	}

	@Override
	public void unregister(ResourceLocation id) {
		ServerPlayNetworking.unregisterGlobalReceiver(id);
	}
}
