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

package net.fabricmc.fabric.api.server;

import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;

/**
 * Helper streams for looking up players on a server.
 *
 * <p>In general, most of these methods will only function with a {@link ServerLevel} instance.
 *
 * @deprecated Please use {@link PlayerLookup} instead.
 */
@Deprecated
public final class PlayerStream {
	private PlayerStream() { }

	public static Stream<ServerPlayer> all(MinecraftServer server) {
		if (server.getPlayerList() != null) {
			return server.getPlayerList().getPlayers().stream();
		} else {
			return Stream.empty();
		}
	}

	public static Stream<Player> world(Level world) {
		if (world instanceof ServerLevel) {
			// noinspection unchecked,rawtypes
			return ((Stream) ((ServerLevel) world).players().stream());
		} else {
			throw new RuntimeException("Only supported on ServerWorld!");
		}
	}

	public static Stream<Player> watching(Level world, ChunkPos pos) {
		if (world instanceof ServerLevel) {
			//noinspection unchecked,rawtypes
			return (Stream) PlayerLookup.tracking((ServerLevel) world, pos).stream();
		}

		throw new RuntimeException("Only supported on ServerWorld!");
	}

	/**
	 * Warning: If the provided entity is a PlayerEntity themselves, it is not
	 * guaranteed by the contract that said PlayerEntity is included in the
	 * resulting stream.
	 */
	@SuppressWarnings("JavaDoc")
	public static Stream<Player> watching(Entity entity) {
		//noinspection unchecked,rawtypes
		return (Stream) PlayerLookup.tracking(entity).stream();
	}

	public static Stream<Player> watching(BlockEntity entity) {
		return watching(entity.getLevel(), entity.getBlockPos());
	}

	public static Stream<Player> watching(Level world, BlockPos pos) {
		return watching(world, new ChunkPos(pos));
	}

	public static Stream<Player> around(Level world, Vec3 vector, double radius) {
		double radiusSq = radius * radius;
		return world(world).filter((p) -> p.distanceToSqr(vector) <= radiusSq);
	}

	public static Stream<Player> around(Level world, BlockPos pos, double radius) {
		double radiusSq = radius * radius;
		return world(world).filter((p) -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= radiusSq);
	}
}
