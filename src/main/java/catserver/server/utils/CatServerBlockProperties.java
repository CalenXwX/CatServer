package catserver.server.utils;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.ToIntFunction;

public class CatServerBlockProperties {
    // CatServer start - in FabricBlockSettings this fabric method conflicts with a vanilla method, so we move it here.
    // This should not be in AbstractBlockSettingsAccessor or the mods will call the interface method with invokevirtual.
    // Seems vanilla lightLevel overrides this one, but it doesn't override after forge reobf.
    /**
     * @deprecated Please use {@link FabricBlockSettings#lightLevel(ToIntFunction)}.
     */
    @Deprecated
    public BlockBehaviour.Properties lightLevel(ToIntFunction<BlockState> levelFunction) {
        return ((BlockBehaviour.Properties)this).lightLevel(levelFunction);
    }
    // CatServer end
}
