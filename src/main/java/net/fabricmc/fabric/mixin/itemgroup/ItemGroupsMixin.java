///*
// * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package net.fabricmc.fabric.mixin.itemgroup;
//
//import static net.minecraft.world.item.CreativeModeTabs.BUILDING_BLOCKS;
//import static net.minecraft.world.item.CreativeModeTabs.COLORED_BLOCKS;
//import static net.minecraft.world.item.CreativeModeTabs.COMBAT;
//import static net.minecraft.world.item.CreativeModeTabs.FOOD_AND_DRINKS;
//import static net.minecraft.world.item.CreativeModeTabs.FUNCTIONAL_BLOCKS;
//import static net.minecraft.world.item.CreativeModeTabs.HOTBAR;
//import static net.minecraft.world.item.CreativeModeTabs.INGREDIENTS;
//import static net.minecraft.world.item.CreativeModeTabs.INVENTORY;
//import static net.minecraft.world.item.CreativeModeTabs.NATURAL_BLOCKS;
//import static net.minecraft.world.item.CreativeModeTabs.OP_BLOCKS;
//import static net.minecraft.world.item.CreativeModeTabs.REDSTONE_BLOCKS;
//import static net.minecraft.world.item.CreativeModeTabs.SEARCH;
//import static net.minecraft.world.item.CreativeModeTabs.SPAWN_EGGS;
//import static net.minecraft.world.item.CreativeModeTabs.TOOLS_AND_UTILITIES;
//
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import net.fabricmc.fabric.impl.itemgroup.FabricItemGroup;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.world.item.CreativeModeTab;
//import net.minecraft.world.item.CreativeModeTabs;
//
//@Mixin(CreativeModeTabs.class)
//public class ItemGroupsMixin {
//	@Unique
//	private static final int TABS_PER_PAGE = 10;
//
//	@Inject(method = "collect", at = @At("HEAD"), cancellable = true)
//	private static void collect(CallbackInfo ci) {
//		final List<ResourceKey<CreativeModeTab>> vanillaGroups = List.of(BUILDING_BLOCKS, COLORED_BLOCKS, NATURAL, FUNCTIONAL, REDSTONE, HOTBAR, SEARCH, TOOLS, COMBAT, FOOD_AND_DRINK, INGREDIENTS, SPAWN_EGGS, OPERATOR, INVENTORY);
//
//		int count = 0;
//
//		// Sort the item groups to ensure they are in a deterministic order.
//		final List<ResourceKey<CreativeModeTab>> sortedItemGroups = BuiltInRegistries.CREATIVE_MODE_TAB.registryKeySet().stream()
//				.sorted(Comparator.comparing(ResourceKey::location))
//				.toList();
//
//		for (ResourceKey<CreativeModeTab> registryKey : sortedItemGroups) {
//			final CreativeModeTab itemGroup = BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(registryKey);
//			final FabricItemGroup fabricItemGroup = (FabricItemGroup) itemGroup;
//
//			if (vanillaGroups.contains(registryKey)) {
//				// Vanilla group goes on the first page.
//				fabricItemGroup.setPage(0);
//				continue;
//			}
//
//			final ItemGroupAccessor itemGroupAccessor = (ItemGroupAccessor) itemGroup;
//			fabricItemGroup.setPage((count / TABS_PER_PAGE) + 1);
//			int pageIndex = count % TABS_PER_PAGE;
//			CreativeModeTab.Row row = pageIndex < (TABS_PER_PAGE / 2) ? CreativeModeTab.Row.TOP : CreativeModeTab.Row.BOTTOM;
//			itemGroupAccessor.setRow(row);
//			itemGroupAccessor.setColumn(row == CreativeModeTab.Row.TOP ? pageIndex % TABS_PER_PAGE : (pageIndex - TABS_PER_PAGE / 2) % (TABS_PER_PAGE));
//
//			count++;
//		}
//
//		// Overlapping group detection logic, with support for pages.
//		record ItemGroupPosition(CreativeModeTab.Row row, int column, int page) { }
//		var map = new HashMap<ItemGroupPosition, String>();
//
//		for (ResourceKey<CreativeModeTab> registryKey : BuiltInRegistries.CREATIVE_MODE_TAB.registryKeySet()) {
//			final CreativeModeTab itemGroup = BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(registryKey);
//			final FabricItemGroup fabricItemGroup = (FabricItemGroup) itemGroup;
//			final String displayName = itemGroup.getDisplayName().getString();
//			final var position = new ItemGroupPosition(itemGroup.row(), itemGroup.column(), fabricItemGroup.getPage());
//			final String existingName = map.put(position, displayName);
//
//			if (existingName != null) {
//				throw new IllegalArgumentException("Duplicate position: (%s) for item groups %s vs %s".formatted(position, displayName, existingName));
//			}
//		}
//
//		ci.cancel();
//	}
//}
