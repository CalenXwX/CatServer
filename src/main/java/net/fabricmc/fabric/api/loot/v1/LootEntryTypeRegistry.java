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

package net.fabricmc.fabric.api.loot.v1;

import net.fabricmc.fabric.impl.loot.table.LootEntryTypeRegistryImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

/**
 * Fabric's extensions to {@link net.minecraft.world.level.storage.loot.entries.LootPoolEntries} for registering
 * custom loot entry types.
 *
 * @see #register
 * @deprecated Use {@link net.minecraft.core.registries.BuiltInRegistries#LOOT_POOL_ENTRY_TYPE} from vanilla instead.
 */
@Deprecated
public interface LootEntryTypeRegistry {
	LootEntryTypeRegistry INSTANCE = new LootEntryTypeRegistryImpl();

	/**
	 * Registers a loot entry type serializer by its ID.
	 *
	 * @param id the loot entry's ID
	 * @param serializer the loot entry serializer
	 */
	void register(ResourceLocation id, Serializer<? extends LootPoolEntryContainer> serializer);
}
