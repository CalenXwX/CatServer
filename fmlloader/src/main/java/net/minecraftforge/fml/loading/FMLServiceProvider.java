/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.loading;

import com.mojang.logging.LogUtils;
import cpw.mods.modlauncher.api.*;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionSpecBuilder;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.forgespi.Environment;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import static net.minecraftforge.fml.loading.LogMarkers.CORE;

public class FMLServiceProvider implements ITransformationService
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private ArgumentAcceptingOptionSpec<String> modsOption;
    private ArgumentAcceptingOptionSpec<String> modListsOption;
    private ArgumentAcceptingOptionSpec<String> mavenRootsOption;
    private ArgumentAcceptingOptionSpec<String> forgeOption;
    private ArgumentAcceptingOptionSpec<String> mcOption;
    private ArgumentAcceptingOptionSpec<String> forgeGroupOption;
    private ArgumentAcceptingOptionSpec<String> mcpOption;
    private ArgumentAcceptingOptionSpec<String> mappingsOption;
    private List<String> modsArgumentList;
    private List<String> modListsArgumentList;
    private List<String> mavenRootsArgumentList;
    private String targetForgeVersion;
    private String targetMcVersion;
    private String targetMcpVersion;
    private String targetMcpMappings;
    private String targetForgeGroup;
    private Map<String, Object> arguments;

    private static final String MIXINEXTRAS = "mixinextras";

    public FMLServiceProvider()
    {
        final String markerselection = System.getProperty("forge.logging.markers", "");
        Arrays.stream(markerselection.split(",")).forEach(marker -> System.setProperty("forge.logging.marker." + marker.toLowerCase(Locale.ROOT), "ACCEPT"));
    }

    @Override
    public String name()
    {
        return "fml";
    }

    @Override
    public void initialize(IEnvironment environment) {
        LOGGER.debug(CORE, "Setting up basic FML game directories");
        FMLPaths.setup(environment);
        LOGGER.debug(CORE, "Loading configuration");
        FMLConfig.load();
        LOGGER.debug(CORE, "Preparing ModFile");
        environment.computePropertyIfAbsent(Environment.Keys.MODFILEFACTORY.get(), k->ModFile::new);
        arguments = new HashMap<>();
        arguments.put("modLists", modListsArgumentList);
        arguments.put("mods", modsArgumentList);
        arguments.put("mavenRoots", mavenRootsArgumentList);
        arguments.put("forgeVersion", targetForgeVersion);
        arguments.put("forgeGroup", targetForgeGroup);
        arguments.put("mcVersion", targetMcVersion);
        arguments.put("mcpVersion", targetMcpVersion);
        arguments.put("mcpMappings", targetMcpMappings);
        LOGGER.debug(CORE, "Preparing launch handler");
        FMLLoader.setupLaunchHandler(environment, arguments);
        FMLEnvironment.setupInteropEnvironment(environment);
        Environment.build(environment);

        net.fabricmc.loader.impl.launch.knot.Knot.preDiscoverAndRemapMods(); // CatServer
    }

    @Override
    public List<Resource> beginScanning(final IEnvironment environment) {
        LOGGER.debug(CORE,"Initiating mod scan");
        return FMLLoader.beginModScan(arguments);
    }

    @Override
    public List<Resource> completeScan(final IModuleLayerManager layerManager) {
        // CatServer start
        List<Resource> forgeResult = FMLLoader.completeScan(layerManager);

        net.fabricmc.loader.impl.launch.knot.Knot.load();

        // provide Fabric mods to classLoader. from FabricLoaderImpl#finishModLoading
        List<cpw.mods.jarhandling.SecureJar> secureJars = net.fabricmc.loader.impl.FabricLoaderImpl.mods.stream()
                .filter(m -> (!net.fabricmc.loader.impl.metadata.AbstractModMetadata.TYPE_BUILTIN.equals(m.getMetadata().getType()) && (!net.fabricmc.loader.impl.FabricLoaderImpl.MOD_ID.equals(m.getMetadata().getId()))))
                .filter(m -> !m.isFromForge())
                .map(net.fabricmc.loader.impl.ModContainerImpl::getCodeSourcePaths)
                .flatMap(java.util.Collection::stream)
                .map(cpw.mods.jarhandling.SecureJar::from)
                .filter(fabricModSecureJar -> {
                    // TODO WARN: now fabric mod list has been dumped to console. if we use a forge dependency as a fabric mod
                    // Forge non-mod jarInJar dependencies
                    List<String> fabricModPackages = new ArrayList<>(fabricModSecureJar.getPackages());
                    if (!fabricModPackages.isEmpty()) {
                        String fabricModPackage_0 = fabricModPackages.get(0);
                        // if any forge game lib has the same package, skip this fabric mod.
                        for (net.minecraftforge.fml.loading.moddiscovery.ModFile gameLibrary : net.minecraftforge.fml.loading.FMLLoader.getGameLibraries()) {
                            if (gameLibrary.getSecureJar().getPackages().contains(fabricModPackage_0)) {
                                LOGGER.info("Using Forge mod library [" + gameLibrary.getSecureJar().moduleDataProvider().uri().toString() + "] as Fabric mod [" + fabricModSecureJar.moduleDataProvider().uri().toString() + "]");
                                return false;
                            }
                        }
                    }
                    return true;
                })
                .collect(java.util.stream.Collectors.toList());
        List<Resource> fabricResult = List.of(new ITransformationService.Resource(IModuleLayerManager.Layer.GAME, secureJars));

        List<Resource> ret = com.google.common.collect.Lists.newArrayList();
        ret.addAll(forgeResult);
        ret.addAll(fabricResult);
        return ret;
        // CatServer end
    }

    @Override
    public void onLoad(IEnvironment environment, Set<String> otherServices) throws IncompatibleEnvironmentException
    {
//        LOGGER.debug("Injecting tracing printstreams for STDOUT/STDERR.");
//        System.setOut(new TracingPrintStream(LogManager.getLogger("STDOUT"), System.out));
//        System.setErr(new TracingPrintStream(LogManager.getLogger("STDERR"), System.err));
        FMLLoader.onInitialLoad(environment, otherServices);
    }

    @Override
    public void arguments(BiFunction<String, String, OptionSpecBuilder> argumentBuilder)
    {
        forgeOption = argumentBuilder.apply("forgeVersion", "Forge Version number").withRequiredArg().ofType(String.class).required();
        forgeGroupOption = argumentBuilder.apply("forgeGroup", "Forge Group (for testing)").withRequiredArg().ofType(String.class).defaultsTo("net.minecraftforge");
        mcOption = argumentBuilder.apply("mcVersion", "Minecraft Version number").withRequiredArg().ofType(String.class).required();
        mcpOption = argumentBuilder.apply("mcpVersion", "MCP Version number").withRequiredArg().ofType(String.class).required();
        mappingsOption = argumentBuilder.apply("mcpMappings", "MCP Mappings Channel and Version").withRequiredArg().ofType(String.class);
        modsOption = argumentBuilder.apply("mods", "List of mods to add").withRequiredArg().ofType(String.class).withValuesSeparatedBy(",");
        modListsOption = argumentBuilder.apply("modLists", "JSON modlists").withRequiredArg().ofType(String.class).withValuesSeparatedBy(",");
        mavenRootsOption = argumentBuilder.apply("mavenRoots", "Maven root directories").withRequiredArg().ofType(String.class).withValuesSeparatedBy(",");
    }

    @Override
    public void argumentValues(OptionResult option)
    {
        modsArgumentList = option.values(modsOption);
        modListsArgumentList = option.values(modListsOption);
        mavenRootsArgumentList = option.values(mavenRootsOption);
        targetForgeVersion = option.value(forgeOption);
        targetForgeGroup = option.value(forgeGroupOption);
        targetMcVersion = option.value(mcOption);
        targetMcpVersion = option.value(mcpOption);
        targetMcpMappings = option.value(mappingsOption);
    }

    @Override
    public @NotNull List<ITransformer> transformers()
    {
        LOGGER.debug(CORE, "Loading coremod transformers");
        return new ArrayList<>(FMLLoader.getCoreModProvider().getCoreModTransformers());
    }

}
