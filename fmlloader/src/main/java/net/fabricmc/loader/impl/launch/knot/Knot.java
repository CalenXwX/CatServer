/*
 * Copyright 2016 FabricMC
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

package net.fabricmc.loader.impl.launch.knot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.FormattedException;
import net.fabricmc.loader.impl.game.GameProvider;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.loader.impl.launch.FabricMixinBootstrap;
import net.fabricmc.loader.impl.util.LoaderUtil;
import net.fabricmc.loader.impl.util.SystemProperties;
import net.fabricmc.loader.impl.util.UrlUtil;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;

public final class Knot extends FabricLauncherBase {
	public static final Knot INSTANCE = new Knot(EnvType.SERVER); // CatServer
	private static final boolean IS_DEVELOPMENT = Boolean.parseBoolean(System.getProperty(SystemProperties.DEVELOPMENT, "false"));

	protected Map<String, Object> properties = new HashMap<>();

	private static KnotClassLoaderInterface classLoader; // CatServer - static
	public static ClassLoader forgeTransformingClassLoader;
	private EnvType envType;
	private final List<Path> classPath = new ArrayList<>();
	private static final GameProvider provider = createGameProvider(); // CatServer - in CatServer the provider no longer provides the game, it just offers some values  be static final
	private boolean unlocked;

	@Deprecated // CatServer - we don't use
	public static void launch(String[] args, EnvType type) {
		setupUncaughtExceptionHandler();

		try {
			Knot knot = new Knot(type);
			ClassLoader cl = knot.init(args);

			if (knot.provider == null) {
				throw new IllegalStateException("Game provider was not initialized! (Knot#init(String[]))");
			}

			// CatServer comment - invoke net.minecraft.server.Main.main()
			knot.provider.launch(cl);
		} catch (FormattedException e) {
			handleFormattedException(e);
		}
	}

	public static void preDiscoverAndRemapMods() {
		FabricLoaderImpl loader = FabricLoaderImpl.INSTANCE;
		loader.setGameProvider(Knot.provider);
	}

	public static void load() {
		FabricLoaderImpl loader = FabricLoaderImpl.INSTANCE;
		loader.load();
		// loader.freeze();
	}

	public Knot(EnvType type) {
		this.envType = type;
	}

	public ClassLoader init(String[] args) {
		setProperties(properties);

		// CatServer start
		classLoader = KnotClassLoaderInterface.create(false, IS_DEVELOPMENT, EnvType.SERVER, /*provider*/null); // CatServer
		// CatServer end

		// configure fabric vars
		if (envType == null) {
			String side = System.getProperty(SystemProperties.SIDE);
			if (side == null) throw new RuntimeException("Please specify side or use a dedicated Knot!");

			switch (side.toLowerCase(Locale.ROOT)) {
			case "client":
				envType = EnvType.CLIENT;
				break;
			case "server":
				envType = EnvType.SERVER;
				break;
			default:
				throw new RuntimeException("Invalid side provided: must be \"client\" or \"server\"!");
			}
		}

		classPath.clear();

		List<String> missing = null;
		List<String> unsupported = null;

		for (String cpEntry : System.getProperty("java.class.path").split(File.pathSeparator)) {
			if (cpEntry.equals("*") || cpEntry.endsWith(File.separator + "*")) {
				if (unsupported == null) unsupported = new ArrayList<>();
				unsupported.add(cpEntry);
				continue;
			}

			Path path = Paths.get(cpEntry);

			if (!Files.exists(path)) {
				if (missing == null) missing = new ArrayList<>();
				missing.add(cpEntry);
				continue;
			}

			classPath.add(LoaderUtil.normalizeExistingPath(path));
		}

		if (unsupported != null) Log.warn(LogCategory.KNOT, "Knot does not support wildcard class path entries: %s - the game may not load properly!", String.join(", ", unsupported));
		if (missing != null) Log.warn(LogCategory.KNOT, "Class path entries reference missing files: %s - the game may not load properly!", String.join(", ", missing));

		// provider = createGameProvider(/*args*/); // CatServer - remove args  be final and init in <clinit>
		Log.finishBuiltinConfig();
		Log.info(LogCategory.GAME_PROVIDER, "Loading %s %s with Fabric Loader %s", /*provider.getGameName()*/"Minecraft", /*provider.getRawGameVersion()*/net.minecraftforge.fml.loading.FMLLoader.versionInfo().mcVersion(), FabricLoaderImpl.VERSION); // CatServer

		// Setup classloader
		// TODO: Provide KnotCompatibilityClassLoader in non-exclusive-Fabric pre-1.13 environments?
		boolean useCompatibility = false && (provider.requiresUrlClassLoader() || Boolean.parseBoolean(System.getProperty("fabric.loader.useCompatibilityClassLoader", "false"))); // CatServer
		// classLoader = KnotClassLoaderInterface.create(useCompatibility, isDevelopment(), envType, /*provider*/null); // CatServer - moved to <clinit>
		ClassLoader cl = classLoader.getClassLoader();

		// provider.initialize(this); // CatServer

		forgeTransformingClassLoader = Thread.currentThread().getContextClassLoader(); // CatServer - now the context classloader is TransformingClassLoader
		Thread.currentThread().setContextClassLoader(cl);

		FabricLoaderImpl loader = FabricLoaderImpl.INSTANCE;
		/* // CatServer - moved to preDiscoverAndRemapMods() and load() and call early
		loader.setGameProvider(Knot.provider);
		loader.load();
		*/
		loader.freeze();

		FabricLoaderImpl.loadAccessWideners(); // CatServer

		FabricMixinBootstrap.init(getEnvironmentType(), FabricLoaderImpl.INSTANCE);
		FabricLauncherBase.finishMixinBootstrapping();

		Thread.currentThread().setContextClassLoader(forgeTransformingClassLoader); // CatServer

		// classLoader.initializeTransformers(); // CatServer - forge initializes mixin, and we process the bytes in FabricLoaderLaunchPluginService

		provider.unlockClassPath(this);
		unlocked = true;

		/* // CatServer - moved to catserver$preLaunch(). Now MixinEnvironment.getDefaultEnvironment().getActiveTransformer() == null, but preLaunch requires the transformer not to be null
		try {
			loader.invokeEntrypoints("preLaunch", PreLaunchEntrypoint.class, PreLaunchEntrypoint::onPreLaunch);
		} catch (RuntimeException e) {
			throw FormattedException.ofLocalized("exception.initializerFailure", e);
		}
		*/

		return cl;
	}

	// CatServer start
	public void catserver$preLaunch() {
		try {
			FabricLoaderImpl.INSTANCE.invokeEntrypoints("preLaunch", PreLaunchEntrypoint.class, PreLaunchEntrypoint::onPreLaunch);
		} catch (RuntimeException e) {
			throw FormattedException.ofLocalized("exception.initializerFailure", e);
		}
	}
	// CatServer end

	private static GameProvider createGameProvider(/*String[] args*/) { // CatServer - static  remove args
		// fast path with direct lookup

		GameProvider embeddedGameProvider = findEmbedddedGameProvider();

		if (
				embeddedGameProvider != null
				&& embeddedGameProvider.isEnabled()
				&& embeddedGameProvider.locateGame(/*this*/INSTANCE, /*args*/new String[0])
		) { // CatServer - static
			return embeddedGameProvider;
		}

		throw new RuntimeException("Unreachable"); // CatServer

		/* // CatServer - unused
		// slow path with service loader

		List<GameProvider> failedProviders = new ArrayList<>();

		for (GameProvider provider : ServiceLoader.load(GameProvider.class)) {
			if (!provider.isEnabled()) continue; // don't attempt disabled providers and don't include them in the error report

			if (provider != embeddedGameProvider // don't retry already failed provider
					&& provider.locateGame(this, args)) {
				return provider;
			}

			failedProviders.add(provider);
		}

		// nothing found

		String msg;

		if (failedProviders.isEmpty()) {
			msg = "No game providers present on the class path!";
		} else if (failedProviders.size() == 1) {
			msg = String.format("%s game provider couldn't locate the game! "
					+ "The game may be absent from the class path, lacks some expected files, suffers from jar "
					+ "corruption or is of an unsupported variety/version.",
					failedProviders.get(0).getGameName());
		} else {
			msg = String.format("None of the game providers (%s) were able to locate their game!",
					failedProviders.stream().map(GameProvider::getGameName).collect(Collectors.joining(", ")));
		}

		Log.error(LogCategory.GAME_PROVIDER, msg);

		throw new RuntimeException(msg);
		*/
	}

	/**
	 * Find game provider embedded into the Fabric Loader jar, best effort.
	 *
	 * <p>This is faster than going through service loader because it only looks at a single jar.
	 */
	private static GameProvider findEmbedddedGameProvider() {
		/*
		try {
			Path flPath = UrlUtil.getCodeSource(Knot.class);
			if (flPath == null || !flPath.getFileName().toString().endsWith(".jar")) return null; // not a jar

			try (ZipFile zf = new ZipFile(flPath.toFile())) {
				ZipEntry entry = zf.getEntry("META-INF/services/net.fabricmc.loader.impl.game.GameProvider"); // same file as used by service loader
				if (entry == null) return null;

				try (InputStream is = zf.getInputStream(entry)) {
					byte[] buffer = new byte[100];
					int offset = 0;
					int len;

					while ((len = is.read(buffer, offset, buffer.length - offset)) >= 0) {
						offset += len;
						if (offset == buffer.length) buffer = Arrays.copyOf(buffer, buffer.length * 2);
					}

					String content = new String(buffer, 0, offset, StandardCharsets.UTF_8).trim();
					if (content.indexOf('\n') >= 0) return null; // potentially more than one entry -> bail out

					int pos = content.indexOf('#');
					if (pos >= 0) content = content.substring(0, pos).trim();

					if (!content.isEmpty()) {
						return (GameProvider) Class.forName(content).getConstructor().newInstance();
					}
				}
			}

			return null;
		} catch (IOException | ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		*/ // CatServer - the code above means:
		return new net.fabricmc.loader.impl.game.minecraft.MinecraftGameProvider();
	}

	@Override
	public String getTargetNamespace() {
		// TODO: Won't work outside of Yarn
		return "srg"; // IS_DEVELOPMENT ? "named" : "intermediary"; // CatServer - use forge srg
	}

	@Override
	public List<Path> getClassPath() {
		return classPath;
	}

	@Override
	public void addToClassPath(Path path, String... allowedPrefixes) {
		Log.debug(LogCategory.KNOT, "Adding " + path + " to classpath.");

		classLoader.setAllowedPrefixes(path, allowedPrefixes);
		classLoader.addCodeSource(path);
	}

	@Override
	public void setAllowedPrefixes(Path path, String... prefixes) {
		classLoader.setAllowedPrefixes(path, prefixes);
	}

	@Override
	public void setValidParentClassPath(Collection<Path> paths) {
		classLoader.setValidParentClassPath(paths);
	}

	@Override
	public EnvType getEnvironmentType() {
		return envType;
	}

	@Override
	public boolean isClassLoaded(String name) {
		return classLoader.isClassLoaded(name);
	}

	@Override
	public Class<?> loadIntoTarget(String name) throws ClassNotFoundException {
		return classLoader.loadIntoTarget(name);
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		return classLoader.getClassLoader().getResourceAsStream(name);
	}

	@Override
	public ClassLoader getTargetClassLoader() {
		KnotClassLoaderInterface classLoader = this.classLoader;

		return classLoader != null ? classLoader.getClassLoader() : null;
	}

	@Override
	public byte[] getClassByteArray(String name, boolean runTransformers) throws IOException {
		if (!unlocked) throw new IllegalStateException("early getClassByteArray access");

		if (runTransformers) {
			return classLoader.getPreMixinClassBytes(name);
		} else {
			return classLoader.getRawClassBytes(name);
		}
	}

	@Override
	public Manifest getManifest(Path originPath) {
		return classLoader.getManifest(originPath);
	}

	@Override
	public boolean isDevelopment() {
		return IS_DEVELOPMENT;
	}

	@Deprecated // CatServer
	@Override
	public String getEntrypoint() {
		// CatServer start
		// return provider.getEntrypoint();
		throw new IllegalStateException("This method should not be called in CatServer");
		// CatServer end
	}

	@Deprecated // CatServer
	public static void main(String[] args) {
		new Knot(null).init(args);
	}

	static {
		LoaderUtil.verifyNotInTargetCl(Knot.class);
		LoaderUtil.verifyClasspath();
	}
}
