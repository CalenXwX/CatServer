/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public interface IForgePlayer {
    private Player self() {
        return (Player)this;
    }

    /**
     * The entity reach is increased by 3 for creative players, unless it is currently zero, which disables attacks and entity interactions.
     * This comes from {@link net.minecraft.client.multiplayer.MultiPlayerGameMode#getPickRange() MultiPlayerGameMode.getPickRange()}
     * If you want the raw value, get the attribute yourself.
     * @return The entity reach of this player.
     */
    default double getEntityReach() {
        double range = self().getAttributeValue(ForgeMod.ENTITY_REACH.get());
        return range == 0 ? 0 : range + (self().isCreative() ? 3 : 0);
    }

    /**
     * The reach distance is increased by 0.5 for creative players, unless it is currently zero, which disables interactions.
     * This comes from {@link net.minecraft.client.multiplayer.MultiPlayerGameMode#getPickRange() MultiPlayerGameMode.getPickRange()}
     * If you want the raw value, get the attribute yourself.
     * @return The reach distance of this player.
     */
    default double getBlockReach() {
        double reach = self().getAttributeValue(ForgeMod.BLOCK_REACH.get());
        return reach == 0 ? 0 : reach + (self().isCreative() ? 0.5 : 0);
    }

    /**
     * Checks if the player can reach an entity by targeting the passed vector.<br>
     * On the server, additional padding is added to account for movement/lag.
     * @param entityHitVec The vector being range-checked.
     * @param padding Extra validation distance.
     * @return If the player can attack the entity.
     * @apiNote Do not use for block checks, as this method uses {@link #getEntityReach()}
     */
    default boolean canReach(Vec3 entityHitVec, double padding) {
        return self().getEyePosition().closerThan(entityHitVec, getEntityReach() + padding);
    }

    /**
     * Checks if the player can reach an entity.<br>
     * On the server, additional padding is added to account for movement/lag.
     * @param entity The entity being range-checked.
     * @param padding Extra validation distance.
     * @return If the player can attack the passed entity.
     * @apiNote Prefer using {@link #canReach(Vec3, double)} if you have a {@link HitResult} available.
     */
    default boolean canReach(Entity entity, double padding) {
        return isCloseEnough(entity, getEntityReach() + padding);
    }

    /**
     * Checks if the player can reach an entity.<br>
     * On the server, additional padding is added to account for movement/lag.
     *
     * Unlike {@link #getEntityReach()} or {@link #canReach(Entity,double)} this does not
     * add the 3.0 creative mode implicit padding.
     *
     * @param entity The entity being range-checked.
     * @param padding Extra validation distance.
     * @return If the player can attack the passed entity.
     * @apiNote Prefer using {@link #canReach(Vec3, double)} if you have a {@link HitResult} available.
     */
    default boolean canReachRaw(Entity entity, double padding) {
        double range = self().getAttributeValue(ForgeMod.ENTITY_REACH.get()) + padding;
        return isCloseEnough(entity, range);
    }

    /**
     * Checks if the player can reach a block.<br>
     * On the server, additional padding is added to account for movement/lag.
     * @param pos The position being range-checked.
     * @param padding Extra validation distance.
     * @return If the player can interact with this location.
     */
    default boolean canReach(BlockPos pos, double padding) {
        double reach = this.getBlockReach() + padding;
        return self().getEyePosition().distanceToSqr(this.catserver$IForgePlayer$canReach$distanceToSqr$ModifyArg$OverwriteTarget(Vec3.atCenterOf(pos))) <= this.catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$WrapOperation$OverwriteTarget(this.catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$Redirect$OverwriteTarget(reach * reach)); // CatServer
    }

    /**
     * Checks if the player can reach a block.<br>
     * On the server, additional padding is added to account for movement/lag.
     *
     * Unlike {@link #getBlockReach()} or {@link #canReach(BlockPos,double)} this does not
     * add the 0.5 creative mode implicit padding.
     *
     * @param pos The position being range-checked.
     * @param padding Extra validation distance.
     * @return If the player can interact with this location.
     */
    default boolean canReachRaw(BlockPos pos, double padding) {
        double reach = self().getAttributeValue(ForgeMod.BLOCK_REACH.get()) + padding;
        return self().getEyePosition().distanceToSqr(this.catserver$IForgePlayer$canReach$distanceToSqr$ModifyArg$OverwriteTarget(Vec3.atCenterOf(pos))) <= this.catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$WrapOperation$OverwriteTarget(this.catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$Redirect$OverwriteTarget(reach * reach)); // CatServer
    }

    /**
     * Utility check to see if the player is close enough to a target entity. Uses "eye-to-closest-corner" checks.
     * @param entity The entity being checked against
     * @param dist The max distance allowed
     * @return If the eye-to-center distance between this player and the passed entity is less than dist.
     * @implNote This method inflates the bounding box by the pick radius, which differs from vanilla. But vanilla doesn't use the pick radius, the only entity with > 0 is AbstractHurtingProjectile.
     */
    default boolean isCloseEnough(Entity entity, double dist) {
        // This causes the "eye-to-closest-corner" checks, which can cause issues with servers.
        // https://github.com/MinecraftForge/MinecraftForge/issues/9309
        // But to not break expectations of others, its staying until a config can be set.
        // The vanilla code is:
        //    return entity.getBoundingBox().distanceToSqr(self().getEyePosition()) < dist * dist;
        Vec3 eye = self().getEyePosition();
        AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
        return aabb.distanceToSqr(eye) < this.catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$WrapOperation$OverwriteTarget(this.catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$Redirect$OverwriteTarget(dist * dist)); // CatServer
    }

    // CatServer start
    // compat:fabric-mod:pehkui-3.8.3+1.14.4-1.21:virtuoel.pehkui.mixin.compat1204minus.compat119plus.ServerPlayerInteractionManagerMixin#pehkui$processBlockBreakingAction$center
    default Vec3 catserver$IForgePlayer$canReach$distanceToSqr$ModifyArg$OverwriteTarget(Vec3 originalValue) {
        return originalValue;
    }

    // compat:fabric-mod:reach-entity-attributes-2.4.0:com.jamieswhiteshirt.reachentityattributes.mixin.ServerPlayNetworkHandlerMixin#getActualAttackRange
    // compat:fabric-mod:reach-entity-attributes-2.4.0:com.jamieswhiteshirt.reachentityattributes.mixin.ServerPlayerInteractionManagerMixin#getActualReachDistance
    default double catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$Redirect$OverwriteTarget(double originalValue) {
        return originalValue;
    }

    // compat:fabric-mod:pehkui-3.8.3+1.14.4-1.21:virtuoel.pehkui.mixin.reach.compat1204minus.compat119plus.ServerPlayerInteractionManagerMixin#pehkui$processBlockBreakingAction$distance
    default double catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$WrapOperation$OverwriteTarget(double originalValue) {
        return originalValue;
    }
    // CatServer end
}
