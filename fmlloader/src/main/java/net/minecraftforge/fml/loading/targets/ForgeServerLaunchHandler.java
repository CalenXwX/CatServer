/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.fml.loading.targets;

import cpw.mods.modlauncher.api.ILaunchHandlerService;
import net.minecraftforge.fml.loading.LibraryFinder;
import net.minecraftforge.fml.loading.VersionInfo;

import java.nio.file.Path;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class ForgeServerLaunchHandler extends CommonServerLaunchHandler implements ILaunchHandlerService {
    @Override public String name() { return "forgeserver"; }

    @Override
    protected BiPredicate<String, String> processMCStream(VersionInfo versionInfo, Stream.Builder<Path> mc, BiPredicate<String, String> filter, Stream.Builder<List<Path>> mods) {
        var forgepatches = LibraryFinder.findPathForMaven(versionInfo.forgeGroup(), "forge", "", "server", versionInfo.mcAndForgeVersion());
        var forgejar = LibraryFinder.findPathForMaven(versionInfo.forgeGroup(), "forge", "", "universal", versionInfo.mcAndForgeVersion());
        var datafixerupperjar = LibraryFinder.findPathForMaven("com.mojang", "datafixerupper", "", "", versionInfo.datafixerupperVersion()); // CatServer - fabric mixins datafixerupper, so we remove datafixerupper from bootstraplauncher and load it here
        mc.add(forgepatches);
        mc.add(datafixerupperjar); // CatServer
        mods.add(List.of(forgejar));
        return filter;
    }
}
