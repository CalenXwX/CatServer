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

package net.fabricmc.fabric.mixin.self_mixin.dimension; // package net.fabricmc.fabric.mixin.dimension; // CatServer - we can't patch the target class, so we remain the mixin. // CatServer - org.spongepowered.asm.mixin.transformer.throwables.IllegalClassLoadError: net.fabricmc.fabric.mixin.registry.sync.RegistriesAccessor is in a defined mixin package net.fabricmc.fabric.mixin.* owned by mixins.catserver.json and cannot be referenced directly

import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.impl.dimension.TaggedChoiceExtension;
import net.fabricmc.fabric.impl.dimension.TaggedChoiceTypeExtension;

@Mixin(value = TaggedChoice.class, remap = false)
public class TaggedChoiceMixin implements TaggedChoiceExtension {
	@Unique
	boolean failSoft = false;

	@Override
	public void fabric$setFailSoft(boolean cond) {
		failSoft = cond;
	}

	/**
	 * Pass the failSoft information into TaggedChoice.TaggedChoiceType.
	 */
	@SuppressWarnings("rawtypes")
	@Inject(
			method = "lambda$apply$0", at = @At("RETURN"), remap = false
	)
	private void onApply(Pair key, CallbackInfoReturnable<Type> cir) {
		if (failSoft) {
			Type returnValue = cir.getReturnValue();

			if (returnValue instanceof TaggedChoice.TaggedChoiceType<?> taggedChoiceType) {
				((TaggedChoiceTypeExtension) (Object) taggedChoiceType).fabric$setFailSoft(true);
			}
		}
	}
}
