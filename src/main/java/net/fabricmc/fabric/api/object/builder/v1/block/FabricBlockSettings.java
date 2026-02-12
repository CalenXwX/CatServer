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

package net.fabricmc.fabric.api.object.builder.v1.block;

import java.util.function.Function;
import java.util.function.ToIntFunction;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

/**
 * Fabric's version of Block.Settings. Adds additional methods and hooks
 * not found in the original class.
 *
 * <p>Make note that this behaves slightly different from the
 * vanilla counterpart, copying some settings that vanilla does not.
 *
 * <p>To use it, simply replace Block.Settings.of() with
 * FabricBlockSettings.of().
 */
// CatServer - the method names will not be mapped to srg when remapping mods, so we remained the fabric methods...
public class FabricBlockSettings extends BlockBehaviour.Properties {
	protected FabricBlockSettings() {
		super();
	}

	protected FabricBlockSettings(BlockBehaviour.Properties settings) {
		this();
		// Mostly Copied from vanilla's copy method
		// Note: If new methods are added to Block settings, an accessor must be added here
		AbstractBlockSettingsAccessor thisAccessor = (AbstractBlockSettingsAccessor) this;
		AbstractBlockSettingsAccessor otherAccessor = (AbstractBlockSettingsAccessor) settings;

		// Copied in vanilla: sorted by vanilla copy order
		this.destroyTime(otherAccessor.getHardness());
		this.explosionResistance(otherAccessor.getResistance());
		this.collidable(otherAccessor.getCollidable());
		thisAccessor.setRandomTicks(otherAccessor.getRandomTicks());
		this.lightLevel(otherAccessor.getLuminance());
		thisAccessor.setMapColorProvider(otherAccessor.getMapColorProvider());
		this.sound(otherAccessor.getSoundGroup());
		this.friction(otherAccessor.getSlipperiness());
		this.speedFactor(otherAccessor.getVelocityMultiplier());
		thisAccessor.setDynamicBounds(otherAccessor.getDynamicBounds());
		thisAccessor.setOpaque(otherAccessor.getOpaque());
		thisAccessor.setIsAir(otherAccessor.getIsAir());
		thisAccessor.setBurnable(otherAccessor.getBurnable());
		thisAccessor.setLiquid(otherAccessor.getLiquid());
		thisAccessor.setForceNotSolid(otherAccessor.getForceNotSolid());
		thisAccessor.setForceSolid(otherAccessor.getForceSolid());
		this.pushReaction(otherAccessor.getPistonBehavior());
		thisAccessor.setToolRequired(otherAccessor.isToolRequired());
		thisAccessor.setOffsetter(otherAccessor.getOffsetter());
		thisAccessor.setBlockBreakParticles(otherAccessor.getBlockBreakParticles());
		thisAccessor.setRequiredFeatures(otherAccessor.getRequiredFeatures());
		this.emissiveRendering(otherAccessor.getEmissiveLightingPredicate());
		this.instrument(otherAccessor.getInstrument());
		thisAccessor.setReplaceable(otherAccessor.getReplaceable());

		// Not copied in vanilla: field definition order
		this.jumpFactor(otherAccessor.getJumpVelocityMultiplier());
		this.drops(otherAccessor.getLootTableId());
		this.isValidSpawn(otherAccessor.getAllowsSpawningPredicate());
		this.isRedstoneConductor(otherAccessor.getSolidBlockPredicate());
		this.isSuffocating(otherAccessor.getSuffocationPredicate());
		this.isViewBlocking(otherAccessor.getBlockVisionPredicate());
		this.hasPostProcess(otherAccessor.getPostProcessPredicate());
	}

	public static FabricBlockSettings create() {
		return new FabricBlockSettings();
	}

	/**
	 * @deprecated Use {@link FabricBlockSettings#create()} instead.
	 */
	@Deprecated
	public static FabricBlockSettings of() {
		return create();
	}

	public static FabricBlockSettings copyOf(BlockBehaviour block) {
		return new FabricBlockSettings(((AbstractBlockAccessor) block).getSettings());
	}

	public static FabricBlockSettings copyOf(BlockBehaviour.Properties settings) {
		return new FabricBlockSettings(settings);
	}

	@Override
	public FabricBlockSettings noCollission() {
		super.noCollission();
		return this;
	}
	public FabricBlockSettings noCollision() {
		return this.noCollission();
	}

	@Override
	public FabricBlockSettings noOcclusion() {
		super.noOcclusion();
		return this;
	}
	public FabricBlockSettings nonOpaque() {
		return this.noOcclusion();
	}

	@Override
	public FabricBlockSettings friction(float value) {
		super.friction(value);
		return this;
	}
	public FabricBlockSettings slipperiness(float value) {
		return this.friction(value);
	}

	@Override
	public FabricBlockSettings speedFactor(float velocityMultiplier) {
		super.speedFactor(velocityMultiplier);
		return this;
	}
	public FabricBlockSettings velocityMultiplier(float velocityMultiplier) {
		return this.speedFactor(velocityMultiplier);
	}

	@Override
	public FabricBlockSettings jumpFactor(float jumpVelocityMultiplier) {
		super.jumpFactor(jumpVelocityMultiplier);
		return this;
	}
	public FabricBlockSettings jumpVelocityMultiplier(float jumpVelocityMultiplier) {
		return this.jumpFactor(jumpVelocityMultiplier);
	}

	@Override
	public FabricBlockSettings sound(SoundType group) {
		super.sound(group);
		return this;
	}
	public FabricBlockSettings sounds(SoundType group) {
		return this.sound(group);
	}

	/**
	 * @deprecated Please use {@link FabricBlockSettings#lightLevel(ToIntFunction)}.
	 */
	/*
	@Deprecated
	public FabricBlockSettings lightLevel(ToIntFunction<BlockState> levelFunction) {
		return this.lightLevel(levelFunction);
	}
	*/

	@Override
	public FabricBlockSettings lightLevel(ToIntFunction<BlockState> luminanceFunction) {
		super.lightLevel(luminanceFunction);
		return this;
	}
	public FabricBlockSettings luminance(ToIntFunction<BlockState> luminanceFunction) {
		return this.lightLevel(luminanceFunction);
	}

	@Override
	public FabricBlockSettings strength(float hardness, float resistance) {
		super.strength(hardness, resistance);
		return this;
	}

	@Override
	public FabricBlockSettings instabreak() {
		super.instabreak();
		return this;
	}
	public FabricBlockSettings breakInstantly() {
		return this.instabreak();
	}

	public FabricBlockSettings strength(float strength) {
		super.strength(strength);
		return this;
	}

	@Override
	public FabricBlockSettings randomTicks() {
		super.randomTicks();
		return this;
	}
	public FabricBlockSettings ticksRandomly() {
		return this.randomTicks();
	}

	@Override
	public FabricBlockSettings dynamicShape() {
		super.dynamicShape();
		return this;
	}
	public FabricBlockSettings dynamicBounds() {
		return this.dynamicShape();
	}

	@Override
	public FabricBlockSettings noLootTable() {
		super.noLootTable();
		return this;
	}
	public FabricBlockSettings dropsNothing() {
		return this.noLootTable();
	}

	@Override
	public FabricBlockSettings dropsLike(Block block) {
		super.dropsLike(block);
		return this;
	}

	@Override
	public FabricBlockSettings air() {
		super.air();
		return this;
	}

	@Override
	public FabricBlockSettings isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
		super.isValidSpawn(predicate);
		return this;
	}
	public FabricBlockSettings allowsSpawning(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
		return this.isValidSpawn(predicate);
	}

	@Override
	public FabricBlockSettings isRedstoneConductor(BlockBehaviour.StatePredicate predicate) {
		super.isRedstoneConductor(predicate);
		return this;
	}
	public FabricBlockSettings solidBlock(BlockBehaviour.StatePredicate predicate) {
		return this.isRedstoneConductor(predicate);
	}

	@Override
	public FabricBlockSettings isSuffocating(BlockBehaviour.StatePredicate predicate) {
		super.isSuffocating(predicate);
		return this;
	}
	public FabricBlockSettings suffocates(BlockBehaviour.StatePredicate predicate) {
		return this.isSuffocating(predicate);
	}

	@Override
	public FabricBlockSettings isViewBlocking(BlockBehaviour.StatePredicate predicate) {
		super.isViewBlocking(predicate);
		return this;
	}
	public FabricBlockSettings blockVision(BlockBehaviour.StatePredicate predicate) {
		return this.isViewBlocking(predicate);
	}

	@Override
	public FabricBlockSettings hasPostProcess(BlockBehaviour.StatePredicate predicate) {
		super.hasPostProcess(predicate);
		return this;
	}
	public FabricBlockSettings postProcess(BlockBehaviour.StatePredicate predicate) {
		return this.hasPostProcess(predicate);
	}

	@Override
	public FabricBlockSettings emissiveRendering(BlockBehaviour.StatePredicate predicate) {
		super.emissiveRendering(predicate);
		return this;
	}
	public FabricBlockSettings emissiveLighting(BlockBehaviour.StatePredicate predicate) {
		return this.emissiveRendering(predicate);
	}

	/**
	 * Make the block require tool to drop and slows down mining speed if the incorrect tool is used.
	 */
	@Override
	public FabricBlockSettings requiresCorrectToolForDrops() {
		super.requiresCorrectToolForDrops();
		return this;
	}
	public FabricBlockSettings requiresTool() {
		return this.requiresCorrectToolForDrops();
	}

	@Override
	public FabricBlockSettings mapColor(MapColor color) {
		super.mapColor(color);
		return this;
	}

	@Override
	public FabricBlockSettings destroyTime(float hardness) {
		super.destroyTime(hardness);
		return this;
	}
	public FabricBlockSettings hardness(float hardness) {
		return this.destroyTime(hardness);
	}

	@Override
	public FabricBlockSettings explosionResistance(float resistance) {
		super.explosionResistance(resistance);
		return this;
	}
	public FabricBlockSettings resistance(float resistance) {
		return this.explosionResistance(resistance);
	}

	@Override
	public FabricBlockSettings offsetType(BlockBehaviour.OffsetType offsetType) {
		super.offsetType(offsetType);
		return this;
	}
	public FabricBlockSettings offset(BlockBehaviour.OffsetType offsetType) {
		return this.offsetType(offsetType);
	}

	@Override
	public FabricBlockSettings noParticlesOnBreak() {
		super.noParticlesOnBreak();
		return this;
	}
	public FabricBlockSettings noBlockBreakParticles() {
		return this.noParticlesOnBreak();
	}

	@Override
	public FabricBlockSettings requiredFeatures(FeatureFlag... features) {
		super.requiredFeatures(features);
		return this;
	}
	public FabricBlockSettings requires(FeatureFlag... features) {
		return this.requiredFeatures(features);
	}

	@Override
	public FabricBlockSettings mapColor(Function<BlockState, MapColor> mapColorProvider) {
		super.mapColor(mapColorProvider);
		return this;
	}

	@Override
	public FabricBlockSettings ignitedByLava() {
		super.ignitedByLava();
		return this;
	}
	public FabricBlockSettings burnable() {
		return this.ignitedByLava();
	}

	@Override
	public FabricBlockSettings liquid() {
		super.liquid();
		return this;
	}

	@Override
	public FabricBlockSettings forceSolidOn() {
		super.forceSolidOn();
		return this;
	}
	public FabricBlockSettings solid() {
		return this.forceSolidOn();
	}

	@Override
	public FabricBlockSettings forceSolidOff() {
		super.forceSolidOff();
		return this;
	}
	public FabricBlockSettings notSolid() {
		return this.forceSolidOff();
	}

	@Override
	public FabricBlockSettings pushReaction(PushReaction pistonBehavior) {
		super.pushReaction(pistonBehavior);
		return this;
	}
	public FabricBlockSettings pistonBehavior(PushReaction pistonBehavior) {
		return this.pushReaction(pistonBehavior);
	}

	@Override
	public FabricBlockSettings instrument(NoteBlockInstrument instrument) {
		super.instrument(instrument);
		return this;
	}

	@Override
	public FabricBlockSettings replaceable() {
		super.replaceable();
		return this;
	}

	/* FABRIC ADDITIONS*/

	/**
	 * @deprecated Please use {@link FabricBlockSettings#luminance(int)}.
	 */
	@Deprecated
	public FabricBlockSettings lightLevel(int lightLevel) {
		this.luminance(lightLevel);
		return this;
	}

	public FabricBlockSettings luminance(int luminance) {
		this.lightLevel(ignored -> luminance);
		return this;
	}

	public FabricBlockSettings drops(ResourceLocation dropTableId) {
		((AbstractBlockSettingsAccessor) this).setLootTableId(dropTableId);
		return this;
	}

	/* FABRIC DELEGATE WRAPPERS */

	/**
	 * @deprecated Please migrate to {@link FabricBlockSettings#mapColor(MapColor)}
	 */
	@Deprecated
	public FabricBlockSettings materialColor(MapColor color) {
		return this.mapColor(color);
	}

	/**
	 * @deprecated Please migrate to {@link FabricBlockSettings#mapColor(DyeColor)}
	 */
	@Deprecated
	public FabricBlockSettings materialColor(DyeColor color) {
		return this.mapColor(color);
	}

	public FabricBlockSettings mapColor(DyeColor color) {
		return this.mapColor(color.getMapColor());
	}

	public FabricBlockSettings collidable(boolean collidable) {
		((AbstractBlockSettingsAccessor) this).setCollidable(collidable);
		return this;
	}
}
