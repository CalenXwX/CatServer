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

package net.fabricmc.loader.impl.util.mappings;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.spongepowered.asm.mixin.transformer.ClassInfo;

import net.fabricmc.mappingio.tree.MappingTree;

public class MixinIntermediaryDevRemapper extends MixinRemapper {
	private static final String ambiguousName = "<ambiguous>"; // dummy value for ambiguous mappings - needs querying with additional owner and/or desc info

	private final Set<String> allPossibleClassNames = new HashSet<>();
	private final Map<String, String> nameFieldLookup = new HashMap<>();
	private final Map<String, String> nameMethodLookup = new HashMap<>();
	private final Map<String, String> nameDescFieldLookup = new HashMap<>();
	private final Map<String, String> nameDescMethodLookup = new HashMap<>();

	public MixinIntermediaryDevRemapper(MappingTree mappings, String from, String to) {
		super(mappings, mappings.getNamespaceId(from), mappings.getNamespaceId(to));

		this.setYarnId(mappings.getNamespaceId("named"));

		for (MappingTree.ClassMapping classDef : mappings.getClasses()) {
			allPossibleClassNames.add(classDef.getName(from));
			allPossibleClassNames.add(classDef.getName(to));

			putMemberInLookup(fromId, toId, classDef.getFields(), nameFieldLookup, nameDescFieldLookup);
			putMemberInLookup(fromId, toId, classDef.getMethods(), nameMethodLookup, nameDescMethodLookup);

			// CatServer start
			java.util.List<String> fieldsToRemove = new java.util.ArrayList<>();
			java.util.List<String> methodsToRemove = new java.util.ArrayList<>();
			for (String field : nameFieldLookup.keySet()) {
				if (field == null || (!field.startsWith("field_"))) {
					fieldsToRemove.add(field);
				}
			}
			for (String method : nameMethodLookup.keySet()) {
				if (method == null || (!method.startsWith("method_"))) {
					methodsToRemove.add(method);
				}
			}
			fieldsToRemove.forEach(nameFieldLookup::remove);
			methodsToRemove.forEach(nameMethodLookup::remove);
			// CatServer end
		}
	}

	private <T extends MappingTree.MemberMapping> void putMemberInLookup(int from, int to, Collection<T> descriptored, Map<String, String> nameMap, Map<String, String> nameDescMap) {
		for (T field : descriptored) {
			String nameFrom = field.getName(from);
			String descFrom = field.getDesc(from);
			String nameTo = field.getName(to);

			String prev = nameMap.putIfAbsent(nameFrom, nameTo);

			if (prev != null && prev != ambiguousName && !prev.equals(nameTo)) {
				nameDescMap.put(nameFrom, ambiguousName);
			}

			String key = getNameDescKey(nameFrom, descFrom);
			prev = nameDescMap.putIfAbsent(key, nameTo);

			if (prev != null && prev != ambiguousName && !prev.equals(nameTo)) {
				nameDescMap.put(key, ambiguousName);
			}
		}
	}

	private void throwAmbiguousLookup(String type, String name, String desc) {
		throw new RuntimeException("Ambiguous Mixin: " + type + " lookup " + name + " " + desc+" is not unique");
	}

	private String mapMethodNameInner(String owner, String name, String desc) {
		String result = super.mapMethodName(owner, name, desc);

		if (result.equals(name) && yarnId != -1) {
			String otherClass = this.mapTypeNameFromIntermediaryToYarn(owner); // CatServer
			return super.mapMethodName(otherClass, name, unmapDesc(desc));
		} else {
			return result;
		}
	}

	private String mapFieldNameInner(String owner, String name, String desc) {
		String result = super.mapFieldName(owner, name, desc);

		if (result.equals(name)) {
			String otherClass = unmap(owner);
			return super.mapFieldName(otherClass, name, unmapDesc(desc));
		} else {
			return result;
		}
	}

	@Override
	public String mapMethodName(String owner, String name, String desc) {
		// handle unambiguous values early
		if (owner == null || allPossibleClassNames.contains(owner)) {
			String newName;

			if (desc == null) {
				newName = nameMethodLookup.get(name);
			} else {
				newName = nameDescMethodLookup.get(getNameDescKey(name, desc));
			}

			if (newName != null) {
				if (newName == ambiguousName) {
					if (owner == null) {
						throwAmbiguousLookup("method", name, desc);
					}
				} else {
					return tryFixLambdaMethod(newName); // CatServer comment - here we don't need to remap lambda methods
				}
			} else if (owner == null) {
				return tryFixLambdaMethod(name); // CatServer
			} else {
				// FIXME: this kind of namespace mixing shouldn't happen..
				// TODO: this should not repeat more than once
				String unmapOwner = unmap(owner);
				String unmapDesc = unmapDesc(desc);

				if (!unmapOwner.equals(owner) || !unmapDesc.equals(desc)) {
					// CatServer comment - we remap lambda methods in this call.
					// if we remap again, methods forge reobfused wrongly will cause mistakes,
					// just like official CauldronInteraction#m_175724_ is reobfused to m_175689_, and the real m_175689_ is reobfused to lambda$bootStrap$16
					return mapMethodName(unmapOwner, name, unmapDesc);
				} else {
					// take advantage of the fact allPossibleClassNames
					// and nameDescLookup cover all sets; if none are present,
					// we don't have a mapping for it.
					return tryFixLambdaMethod(name); // CatServer
				}
			}
		}

		ClassInfo classInfo = ClassInfo.forName(map(owner));

		if (classInfo == null) { // unknown class?
			return tryFixLambdaMethod(name); // CatServer
		}

		Queue<ClassInfo> queue = new ArrayDeque<>();

		do {
			String ownerO = unmap(classInfo.getName());
			String s;

			if (!(s = mapMethodNameInner(ownerO, name, desc)).equals(name)) {
				return tryFixLambdaMethod(s); // CatServer
			}

			if (classInfo.getSuperName() != null && !classInfo.getSuperName().startsWith("java/")) {
				ClassInfo cSuper = classInfo.getSuperClass();

				if (cSuper != null) {
					queue.add(cSuper);
				}
			}

			for (String itf : classInfo.getInterfaces()) {
				if (itf.startsWith("java/")) {
					continue;
				}

				ClassInfo cItf = ClassInfo.forName(itf);

				if (cItf != null) {
					queue.add(cItf);
				}
			}
		} while ((classInfo = queue.poll()) != null);

		return tryFixLambdaMethod(name); // CatServer
	}

	@Override
	public String mapFieldName(String owner, String name, String desc) {
		// handle unambiguous values early
		if (owner == null || allPossibleClassNames.contains(owner)) {
			String newName = nameDescFieldLookup.get(getNameDescKey(name, desc));

			if (newName != null) {
				if (newName == ambiguousName) {
					if (owner == null) {
						throwAmbiguousLookup("field", name, desc);
					}
				} else {
					return tryFixLambdaField(newName);
				}
			} else if (owner == null) {
				return tryFixLambdaField(name); // CatServer
			} else {
				// FIXME: this kind of namespace mixing shouldn't happen..
				// TODO: this should not repeat more than once
				String unmapOwner = unmap(owner);
				String unmapDesc = unmapDesc(desc);

				if (!unmapOwner.equals(owner) || !unmapDesc.equals(desc)) {
					return mapFieldName(unmapOwner, name, unmapDesc);
				} else {
					// take advantage of the fact allPossibleClassNames
					// and nameDescLookup cover all sets; if none are present,
					// we don't have a mapping for it.
					return tryFixLambdaField(name); // CatServer
				}
			}
		}

		ClassInfo c = ClassInfo.forName(map(owner));

		while (c != null) {
			String nextOwner = unmap(c.getName());
			String s = mapFieldNameInner(nextOwner, name, desc);

			if (!s.equals(name)) {
				return tryFixLambdaField(s); // CatServer
			}

			if (c.getSuperName() == null || c.getSuperName().startsWith("java/")) {
				break;
			}

			c = c.getSuperClass();
		}

		return tryFixLambdaField(name); // CatServer
	}

	private static String getNameDescKey(String name, String descriptor) {
		return name+ ";;" + descriptor;
	}

	// CatServer start
	private static String tryFixLambdaMethod(String original) {
		return catserver.server.fabric_loading.CatServerFabricMixinLambdaHelper.remapMethod(original);
	}

	private static String tryFixLambdaField(String original) {
		return catserver.server.fabric_loading.CatServerFabricMixinLambdaHelper.remapField(original);
	}
	// CatServer end
}
