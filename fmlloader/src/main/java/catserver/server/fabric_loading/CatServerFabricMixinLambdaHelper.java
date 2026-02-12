package catserver.server.fabric_loading;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Forge's patches change some lambda methods' names, then they will not be correctly reobfused to srg name.
 * Fabric maps the lambda methods from intermediary to srg, but the srg methods may be missing.
 * So we rename the srg name to the forge name.
 */
public class CatServerFabricMixinLambdaHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger("CatServerFabricMixinLambdaHelper");
    // <key>the name fabric looking for -> <value>the name in catserver
    public static final HashMap<String, String> classes = Maps.newHashMap();
    public static final HashMap<String, String> methods = Maps.newHashMap();
    public static final HashMap<String, String> fields = Maps.newHashMap();

    static {
        // (not complete)
        // Class

        // Method
        // MappedRegistry
        methods.put("m_211799_", "lambda$bindTags$8");
        methods.put("m_211804_", "lambda$bindTags$9");
        methods.put("m_211810_", "lambda$bindTags$10");
        methods.put("m_211795_", "lambda$bindTags$11");

        // CauldronInteraction
        methods.put("m_175689_", "lambda$bootStrap$16");
        methods.put("m_175626_", "lambda$bootStrap$15");
        methods.put("m_175696_", "lambda$bootStrap$14");
        methods.put("m_175650_", "lambda$bootStrap$13");
        methods.put("m_175703_", "lambda$bootStrap$12");
        methods.put("m_175717_", "lambda$bootStrap$11");
        methods.put("m_175738_", "lambda$newInteractionMap$6");
        methods.put("m_175645_", "lambda$newInteractionMap$7");
        methods.put("m_175682_", "lambda$static$0");
        methods.put("m_175675_", "lambda$static$1");
        methods.put("m_175668_", "lambda$static$2");
        methods.put("m_175661_", "lambda$static$3");
        methods.put("m_278529_", "lambda$static$4");
        methods.put("m_175628_", "lambda$static$5");
        // Forge reobf mistakes!
        methods.put("m_175724_", "m_175689_");
        methods.put("m_175659_", "m_175626_");
        methods.put("m_175731_", "m_175696_");

        // net/minecraft/world/entity/Entity
        methods.put("m_201940_", "lambda$isInWall$12");

        // AbstractContainerMenu
        // methods.put("m_38913_", "lambda$stillValid$0"); // the parameters are not same

        // Field
        // ServerGamePacketListenerImpl$1
        fields.put("f_243930_", "val$serverlevel");
        fields.put("f_143670_", "val$entity");
    }

    public static String remapClass(String original) {
        return classes.getOrDefault(original, original);
    }

    public static String remapMethod(String original) {
        return methods.getOrDefault(original, original);
    }

    public static String remapField(String original) {
        return fields.getOrDefault(original, original);
    }
}
