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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import net.minecraft.SharedConstants;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.loader.api.FabricLoader;

/**
 * The Fabric mods resource pack, holds all the mod resource packs as one pack.
 */
public class FabricModResourcePack extends GroupResourcePack {
	public FabricModResourcePack(PackType type, List<ModResourcePack> packs) {
		super(type, packs);
	}

	@Override
	public IoSupplier<InputStream> getRootResource(String... pathSegments) {
		String fileName = String.join("/", pathSegments);

		if ("pack.mcmeta".equals(fileName)) {
			String description = "pack.description.modResources";
			String fallback = "Mod resources.";
			String pack = String.format("{\"pack\":{\"pack_format\":" + SharedConstants.getCurrentVersion().getPackVersion(type) + ",\"description\":{\"translate\":\"%s\",\"fallback\":\"%s.\"}}}", description, fallback);
			return () -> IOUtils.toInputStream(pack, Charsets.UTF_8);
		} else if ("pack.png".equals(fileName)) {
			return FabricLoader.getInstance().getModContainer("fabric-resource-loader-v0")
					.flatMap(container -> container.getMetadata().getIconPath(512).flatMap(container::findPath))
					.map(path -> (IoSupplier<InputStream>) (() -> Files.newInputStream(path)))
					.orElse(null);
		}

		return null;
	}

	@Override
	public <T> @Nullable T getMetadataSection(MetadataSectionSerializer<T> metaReader) throws IOException {
		IoSupplier<InputStream> inputSupplier = this.getRootResource("pack.mcmeta");

		if (inputSupplier != null) {
			try (InputStream input = inputSupplier.get()) {
				return AbstractPackResources.getMetadataFromStream(metaReader, input);
			}
		} else {
			return null;
		}
	}

	@Override
	public String packId() {
		return "fabric";
	}

	@Override
	public boolean isBuiltin() {
		return true;
	}
}
