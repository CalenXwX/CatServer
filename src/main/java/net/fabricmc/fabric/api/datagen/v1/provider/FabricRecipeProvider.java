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

package net.fabricmc.fabric.api.datagen.v1.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;

/**
 * Extend this class and implement {@link FabricRecipeProvider#buildRecipes}.
 *
 * <p>Register an instance of the class with {@link FabricDataGenerator.Pack#addProvider} in a {@link net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint}.
 */
public abstract class FabricRecipeProvider extends RecipeProvider {
	protected final FabricDataOutput output;

	public FabricRecipeProvider(FabricDataOutput output) {
		super(output);
		this.output = output;
	}

	/**
	 * Implement this method and then use the range of methods in {@link RecipeProvider} or from one of the recipe json factories such as {@link ShapedRecipeBuilder} or {@link ShapelessRecipeBuilder}.
	 */
	@Override
	public abstract void buildRecipes(Consumer<FinishedRecipe> exporter);

	/**
	 * Return a new exporter that applies the specified conditions to any recipe json provider it receives.
	 */
	protected Consumer<FinishedRecipe> withConditions(Consumer<FinishedRecipe> exporter, ConditionJsonProvider... conditions) {
		Preconditions.checkArgument(conditions.length > 0, "Must add at least one condition.");
		return json -> {
			FabricDataGenHelper.addConditions(json, conditions);
			exporter.accept(json);
		};
	}

	@Override
	public CompletableFuture<?> run(CachedOutput writer) {
		Set<ResourceLocation> generatedRecipes = Sets.newHashSet();
		List<CompletableFuture<?>> list = new ArrayList<>();
		buildRecipes(provider -> {
			ResourceLocation identifier = getRecipeIdentifier(provider.getId());

			if (!generatedRecipes.add(identifier)) {
				throw new IllegalStateException("Duplicate recipe " + identifier);
			}

			JsonObject recipeJson = provider.serializeRecipe();
			ConditionJsonProvider[] conditions = FabricDataGenHelper.consumeConditions(provider);
			ConditionJsonProvider.write(recipeJson, conditions);

			list.add(DataProvider.saveStable(writer, recipeJson, this.recipePathProvider.json(identifier)));
			JsonObject advancementJson = provider.serializeAdvancement();

			if (advancementJson != null) {
				ConditionJsonProvider.write(advancementJson, conditions);
				list.add(DataProvider.saveStable(writer, advancementJson, this.advancementPathProvider.json(getRecipeIdentifier(provider.getAdvancementId()))));
			}
		});
		return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
	}

	/**
	 * Override this method to change the recipe identifier. The default implementation normalizes the namespace to the mod ID.
	 */
	protected ResourceLocation getRecipeIdentifier(ResourceLocation identifier) {
		return new ResourceLocation(output.getModId(), identifier.getPath());
	}
}
