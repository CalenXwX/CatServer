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

package net.fabricmc.fabric.impl.resource.loader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.WorldDataConfiguration;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

/**
 * Internal utilities for managing resource packs.
 */
public final class ModResourcePackUtil {
	private static final Gson GSON = new Gson();

	private ModResourcePackUtil() {
	}

	/**
	 * Appends mod resource packs to the given list.
	 *
	 * @param packs   the resource pack list to append
	 * @param type    the type of resource
	 * @param subPath the resource pack sub path directory in mods, may be {@code null}
	 */
	public static void appendModResourcePacks(List<ModResourcePack> packs, PackType type, @Nullable String subPath) {
		for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
			if (container.getMetadata().getType().equals("builtin")) {
				continue;
			}

			ModResourcePack pack = ModNioResourcePack.create(container.getMetadata().getId(), container, subPath, type, ResourcePackActivationType.ALWAYS_ENABLED);

			if (pack != null) {
				packs.add(pack);
			}
		}
	}

	public static boolean containsDefault(ModMetadata info, String filename) {
		return "pack.mcmeta".equals(filename);
	}

	public static InputStream openDefault(ModMetadata info, PackType type, String filename) {
		switch (filename) {
		case "pack.mcmeta":
			String description = Objects.requireNonNullElse(info.getName(), "");
			String metadata = serializeMetadata(SharedConstants.getCurrentVersion().getPackVersion(type), description);
			return IOUtils.toInputStream(metadata, Charsets.UTF_8);
		default:
			return null;
		}
	}

	public static String serializeMetadata(int packVersion, String description) {
		JsonObject pack = new JsonObject();
		pack.addProperty("pack_format", packVersion);
		pack.addProperty("description", description);
		JsonObject metadata = new JsonObject();
		metadata.add("pack", pack);
		return GSON.toJson(metadata);
	}

	public static Component getName(ModMetadata info) {
		if (info.getName() != null) {
			return Component.literal(info.getName());
		} else {
			return Component.translatable("pack.name.fabricMod", info.getId());
		}
	}

	/**
	 * Creates the default data pack settings that replaces
	 * {@code DataPackSettings.SAFE_MODE} used in vanilla.
	 * @return the default data pack settings
	 */
	public static WorldDataConfiguration createDefaultDataConfiguration() {
		ModResourcePackCreator modResourcePackCreator = new ModResourcePackCreator(PackType.SERVER_DATA);
		List<Pack> moddedResourcePacks = new ArrayList<>();
		modResourcePackCreator.loadPacks(moddedResourcePacks::add);

		List<String> enabled = new ArrayList<>(DataPackConfig.DEFAULT.getEnabled());
		List<String> disabled = new ArrayList<>(DataPackConfig.DEFAULT.getDisabled());

		// This ensures that any built-in registered data packs by mods which needs to be enabled by default are
		// as the data pack screen automatically put any data pack as disabled except the Default data pack.
		for (Pack profile : moddedResourcePacks) {
			try (PackResources pack = profile.open()) {
				if (pack instanceof FabricModResourcePack || (pack instanceof ModNioResourcePack && ((ModNioResourcePack) pack).getActivationType().isEnabledByDefault())) {
					enabled.add(profile.getId());
				} else {
					disabled.add(profile.getId());
				}
			}
		}

		return new WorldDataConfiguration(
				new DataPackConfig(enabled, disabled),
				FeatureFlags.DEFAULT_FLAGS
		);
	}

	/**
	 * Vanilla enables all available datapacks automatically in TestServer#create, but it does so in alphabetical order,
	 * which means the Vanilla pack has higher precedence than modded, breaking our tests.
	 * To fix this, we move all modded pack profiles to the end of the list.
	 */
	public static DataPackConfig createTestServerSettings(List<String> enabled, List<String> disabled) {
		// Collect modded profiles
		Set<String> moddedProfiles = new HashSet<>();
		ModResourcePackCreator modResourcePackCreator = new ModResourcePackCreator(PackType.SERVER_DATA);
		modResourcePackCreator.loadPacks(profile -> moddedProfiles.add(profile.getId()));

		// Remove them from the enabled list
		List<String> moveToTheEnd = new ArrayList<>();

		for (Iterator<String> it = enabled.iterator(); it.hasNext();) {
			String profile = it.next();

			if (moddedProfiles.contains(profile)) {
				moveToTheEnd.add(profile);
				it.remove();
			}
		}

		// Add back at the end
		enabled.addAll(moveToTheEnd);

		return new DataPackConfig(enabled, disabled);
	}
}
