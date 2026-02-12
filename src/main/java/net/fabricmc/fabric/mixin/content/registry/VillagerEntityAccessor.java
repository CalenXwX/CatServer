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

package net.fabricmc.fabric.mixin.content.registry;

import java.util.Map;
import java.util.Set;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

// CatServer start
// @Mixin(Villager.class)
public interface VillagerEntityAccessor {
	// @Mutable
	// @Accessor("ITEM_FOOD_VALUES")
	static void fabric_setItemFoodValues(Map<Item, Integer> items) {
		Villager.FOOD_POINTS = items;
	}

	// @Mutable
	// @Accessor("GATHERABLE_ITEMS")
	static void fabric_setGatherableItems(Set<Item> items) {
		Villager.WANTED_ITEMS = items;
	}

	// @Accessor("GATHERABLE_ITEMS")
	static Set<Item> fabric_getGatherableItems() {
		return Villager.WANTED_ITEMS;
	}
}
// CatServer end
