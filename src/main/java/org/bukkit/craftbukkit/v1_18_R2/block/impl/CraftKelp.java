/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_18_R2.block.impl;

import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

public final class CraftKelp extends CraftBlockData implements org.bukkit.block.data.Ageable {

    public CraftKelp() {
        super();
    }

    public CraftKelp(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftAgeable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty AGE = getInteger(net.minecraft.world.level.block.KelpBlock.class, "age");

    @Override
    public int getAge() {
        return get(AGE);
    }

    @Override
    public void setAge(int age) {
        set(AGE, age);
    }

    @Override
    public int getMaximumAge() {
        return getMax(AGE);
    }
}
