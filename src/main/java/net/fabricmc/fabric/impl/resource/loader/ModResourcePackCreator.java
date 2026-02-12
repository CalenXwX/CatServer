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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;

/**
 * Represents a resource pack provider for mods and built-in mods resource packs.
 */
public class ModResourcePackCreator implements RepositorySource {
	public static final PackSource RESOURCE_PACK_SOURCE = new PackSource() {
		@Override
		public Component decorate(Component packName) {
			return Component.translatable("pack.nameAndSource", packName, Component.translatable("pack.source.fabricmod"));
		}

		@Override
		public boolean shouldAddAutomatically() {
			return true;
		}
	};
	public static final ModResourcePackCreator CLIENT_RESOURCE_PACK_PROVIDER = new ModResourcePackCreator(PackType.CLIENT_RESOURCES);
	private final PackType type;

	public ModResourcePackCreator(PackType type) {
		this.type = type;
	}

	/**
	 * Registers the resource packs.
	 *
	 * @param consumer The resource pack profile consumer.
	 */
	@Override
	public void loadPacks(Consumer<Pack> consumer) {
		/*
			Register order rule in this provider:
			1. Mod resource packs
			2. Mod built-in resource packs

			Register order rule globally:
			1. Default and Vanilla built-in resource packs
			2. Mod resource packs
			3. Mod built-in resource packs
			4. User resource packs
		 */

		// Build a list of mod resource packs.
		List<ModResourcePack> packs = new ArrayList<>();
		ModResourcePackUtil.appendModResourcePacks(packs, type, null);

		if (!packs.isEmpty()) {
			// Make the resource pack profile for mod resource packs.
			// Mod resource packs must always be enabled to avoid issues, and they are inserted
			// on top to ensure that they are applied after vanilla built-in resource packs.
			MutableComponent title = Component.translatable("pack.name.fabricMods");
			Pack resourcePackProfile = Pack.readMetaAndCreate("fabric", title,
					true, factory -> new FabricModResourcePack(this.type, packs), type, Pack.Position.TOP,
					RESOURCE_PACK_SOURCE);

			if (resourcePackProfile != null) {
				consumer.accept(resourcePackProfile);
			}
		}

		// Register all built-in resource packs provided by mods.
		ResourceManagerHelperImpl.registerBuiltinResourcePacks(this.type, consumer);
	}
}
