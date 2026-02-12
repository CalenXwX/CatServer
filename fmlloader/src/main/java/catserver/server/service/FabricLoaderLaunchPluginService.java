package catserver.server.service;

import catserver.server.fabric_loading.CatServerFabricModMixinCompat;
import com.mojang.logging.LogUtils;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import net.fabricmc.loader.impl.launch.knot.Knot;
import net.fabricmc.loader.impl.transformer.FabricTransformer;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.slf4j.Logger;

import java.util.EnumSet;

public class FabricLoaderLaunchPluginService implements ILaunchPluginService {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EnumSet<Phase> AFTER = EnumSet.of(Phase.AFTER);
    private static final EnumSet<Phase> BEFORE = EnumSet.of(Phase.BEFORE);
    private static final EnumSet<Phase> NONE = EnumSet.noneOf(Phase.class);
    private static final EnumSet<Phase> ALL = EnumSet.allOf(Phase.class);

    private static boolean initialized = false;
    private static boolean fabricDisabled = true;
    private static boolean preLaunched = false;

    public FabricLoaderLaunchPluginService() {
        fabricDisabled = Boolean.getBoolean("catserver.disableFabric");
    }

    @Override
    public @NotNull String name() {
        return "fabric_loader";
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
        if (fabricDisabled) {
            return NONE;
        }

        if (!initialized) {
            initialized = true;
            if (!fabricDisabled) {
                ClassLoader cl = Knot.INSTANCE.init(new String[0]);
            }
        }

        return AFTER;
    }

    @Override
    public int processClassWithFlags(final Phase phase, final ClassNode classNode, final Type classType, final String reason) {
        if (fabricDisabled) {
            return ComputeFlags.NO_REWRITE;
        } else if (!"computing_frames".equals(reason)) {
            // now MixinEnvironment.getDefaultEnvironment().getActiveTransformer() is not null, so we can preLaunch
            if (!preLaunched) {
                preLaunched = true;
                Knot.INSTANCE.catserver$preLaunch();
            }

            // If this is a mixin class, try to fix the mixin and don't try to apply accesswidener
            if (classNode.invisibleAnnotations != null) {
                for (AnnotationNode annotation : classNode.invisibleAnnotations) {
                    if (CatServerFabricModMixinCompat.c_Mixin_desc.equals(annotation.desc)) {
                        return CatServerFabricModMixinCompat.tryFixMixin(classNode, annotation) ? ComputeFlags.COMPUTE_FRAMES : ComputeFlags.NO_REWRITE;
                    }
                }
            }

            // This is not a Mixin class
            // Don't return ComputeFlags.COMPUTE_FRAMES, or the class bytecode will be broken. But ... why???
            return FabricTransformer.transform(classNode, classType) ? ComputeFlags.SIMPLE_REWRITE : ComputeFlags.NO_REWRITE; // from KnotClassDelegate#getPreMixinClassByteArray()
        } else {
            return ComputeFlags.NO_REWRITE;
        }
    }
}
