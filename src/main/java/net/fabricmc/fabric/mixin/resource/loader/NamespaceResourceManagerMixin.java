///*
// * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package net.fabricmc.fabric.mixin.resource.loader;
//
//import java.io.InputStream;
//import java.util.List;
//
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.Redirect;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//import net.fabricmc.fabric.impl.resource.loader.GroupResourcePack;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.packs.PackResources;
//import net.minecraft.server.packs.PackType;
//import net.minecraft.server.packs.resources.FallbackResourceManager;
//import net.minecraft.server.packs.resources.IoSupplier;
//import net.minecraft.server.packs.resources.Resource;
//
///**
// * Patches getAllResources and method_41265 to work with GroupResourcePack.
// */
//@Mixin(FallbackResourceManager.class)
//public class NamespaceResourceManagerMixin {
//	private final ThreadLocal<List<Resource>> fabric$getAllResources$resources = new ThreadLocal<>();
//
//	@Inject(method = "getAllResources",
//			at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"),
//			locals = LocalCapture.CAPTURE_FAILHARD)
//	private void onGetAllResources(ResourceLocation id, CallbackInfoReturnable<List<Resource>> cir, ResourceLocation metadataId, List<Resource> resources) {
//		this.fabric$getAllResources$resources.set(resources);
//	}
//
//	@Redirect(method = "getAllResources",
//			at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePack;open(Lnet/minecraft/resource/ResourceType;Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/InputSupplier;"))
//	private IoSupplier<InputStream> onResourceAdd(PackResources pack, PackType type, ResourceLocation id) {
//		if (pack instanceof GroupResourcePack) {
//			((GroupResourcePack) pack).appendResources(type, id, this.fabric$getAllResources$resources.get());
//
//			return null;
//		}
//
//		return pack.getResource(type, id);
//	}
//}
