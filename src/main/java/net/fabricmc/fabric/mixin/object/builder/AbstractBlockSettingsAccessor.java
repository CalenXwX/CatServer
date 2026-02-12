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

package net.fabricmc.fabric.mixin.object.builder;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// CatServer start
// @Mixin(BlockBehaviour.Properties.class)
public interface AbstractBlockSettingsAccessor {
	/* GETTERS */
	// @Accessor
	float getHardness();

	// @Accessor
	float getResistance();

	// @Accessor
	boolean getCollidable();

	// @Accessor
	boolean getRandomTicks();

	// @Accessor("luminance")
	ToIntFunction<BlockState> getLuminance();

	// @Accessor
	Function<BlockState, MapColor> getMapColorProvider();

	// @Accessor
	SoundType getSoundGroup();

	// @Accessor
	float getSlipperiness();

	// @Accessor
	float getVelocityMultiplier();

	// @Accessor
	float getJumpVelocityMultiplier();

	// @Accessor
	boolean getDynamicBounds();

	// @Accessor
	boolean getOpaque();

	// @Accessor
	boolean getIsAir();

	// @Accessor
	boolean isToolRequired();

	// @Accessor
	BlockBehaviour.StateArgumentPredicate<EntityType<?>> getAllowsSpawningPredicate();

	// @Accessor
	BlockBehaviour.StatePredicate getSolidBlockPredicate();

	// @Accessor
	BlockBehaviour.StatePredicate getSuffocationPredicate();

	// @Accessor
	BlockBehaviour.StatePredicate getBlockVisionPredicate();

	// @Accessor
	BlockBehaviour.StatePredicate getPostProcessPredicate();

	// @Accessor
	BlockBehaviour.StatePredicate getEmissiveLightingPredicate();

	// @Accessor
	Optional<BlockBehaviour.OffsetFunction> getOffsetter();

	// @Accessor
	ResourceLocation getLootTableId();

	// @Accessor
	boolean getBlockBreakParticles();

	// @Accessor
	FeatureFlagSet getRequiredFeatures();

	// @Accessor
	boolean getBurnable();

	// @Accessor
	boolean getLiquid();

	// @Accessor
	boolean getForceNotSolid();

	// @Accessor
	boolean getForceSolid();

	// @Accessor
	PushReaction getPistonBehavior();

	// @Accessor
	NoteBlockInstrument getInstrument();

	// @Accessor
	boolean getReplaceable();

	/* SETTERS */
	// @Accessor
	void setCollidable(boolean collidable);

	// @Accessor
	void setRandomTicks(boolean ticksRandomly);

	// @Accessor
	void setMapColorProvider(Function<BlockState, MapColor> mapColorProvider);

	// @Accessor
	void setDynamicBounds(boolean dynamicBounds);

	// @Accessor
	void setOpaque(boolean opaque);

	// @Accessor
	void setIsAir(boolean isAir);

	// @Accessor
	void setLootTableId(ResourceLocation lootTableId);

	// @Accessor
	void setToolRequired(boolean toolRequired);

	// @Accessor
	void setBlockBreakParticles(boolean blockBreakParticles);

	// @Accessor
	void setRequiredFeatures(FeatureFlagSet requiredFeatures);

	// @Accessor
	void setOffsetter(Optional<BlockBehaviour.OffsetFunction> offsetter);

	// @Accessor
	void setBurnable(boolean burnable);

	// @Accessor
	void setLiquid(boolean liquid);

	// @Accessor
	void setForceNotSolid(boolean forceNotSolid);

	// @Accessor
	void setForceSolid(boolean forceSolid);

	// @Accessor
	void setReplaceable(boolean replaceable);
}
// CatServer end
