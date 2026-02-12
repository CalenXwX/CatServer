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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceMetadata;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * Represents a group resource pack, holds multiple resource packs as one.
 */
public abstract class GroupResourcePack implements PackResources {
	protected final PackType type;
	protected final List<? extends PackResources> packs;
	protected final Map<String, List<PackResources>> namespacedPacks = new Object2ObjectOpenHashMap<>();

	public GroupResourcePack(PackType type, List<? extends PackResources> packs) {
		this.type = type;
		this.packs = packs;
		this.packs.forEach(pack -> pack.getNamespaces(this.type)
				.forEach(namespace -> this.namespacedPacks.computeIfAbsent(namespace, value -> new ArrayList<>())
						.add(pack)));
	}

	@Override
	public IoSupplier<InputStream> getResource(PackType type, ResourceLocation id) {
		List<? extends PackResources> packs = this.namespacedPacks.get(id.getNamespace());

		if (packs != null) {
			// Last to first, since higher priority packs are at the end
			for (int i = packs.size() - 1; i >= 0; i--) {
				PackResources pack = packs.get(i);
				IoSupplier<InputStream> supplier = pack.getResource(type, id);

				if (supplier != null) {
					return supplier;
				}
			}
		}

		return null;
	}

	@Override
	public void listResources(PackType type, String namespace, String prefix, ResourceOutput consumer) {
		List<? extends PackResources> packs = this.namespacedPacks.get(namespace);

		if (packs == null) {
			return;
		}

		// First to last, since later calls override previously returned data
		for (PackResources pack : packs) {
			pack.listResources(type, namespace, prefix, consumer);
		}
	}

	@Override
	public Set<String> getNamespaces(PackType type) {
		return this.namespacedPacks.keySet();
	}

	public void appendResources(PackType type, ResourceLocation id, List<Resource> resources) {
		List<? extends PackResources> packs = this.namespacedPacks.get(id.getNamespace());

		if (packs == null) {
			return;
		}

		ResourceLocation metadataId = FallbackResourceManager.getMetadataLocation(id);

		// Last to first, since higher priority packs are at the end
		for (int i = packs.size() - 1; i >= 0; i--) {
			PackResources pack = packs.get(i);
			IoSupplier<InputStream> supplier = pack.getResource(type, id);

			if (supplier != null) {
				IoSupplier<ResourceMetadata> metadataSupplier = () -> {
					IoSupplier<InputStream> rawMetadataSupplier = pack.getResource(this.type, metadataId);
					return rawMetadataSupplier != null ? FallbackResourceManager.parseMetadata(rawMetadataSupplier) : ResourceMetadata.EMPTY;
				};

				resources.add(new Resource(pack, supplier, metadataSupplier));
			}
		}
	}

	public String getFullName() {
		return this.packId() + " (" + this.packs.stream().map(PackResources::packId).collect(Collectors.joining(", ")) + ")";
	}

	@Override
	public void close() {
		this.packs.forEach(PackResources::close);
	}
}
