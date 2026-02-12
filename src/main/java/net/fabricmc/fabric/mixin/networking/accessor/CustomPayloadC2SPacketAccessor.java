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

package net.fabricmc.fabric.mixin.networking.accessor;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// CatServer start
// @Mixin(ServerboundCustomPayloadPacket.class)
public interface CustomPayloadC2SPacketAccessor {
	// @Accessor
	ResourceLocation getChannel();

	// @Accessor
	// the name conflicts with a vanilla method, and that method will be renamed after forge's reobf
	default FriendlyByteBuf getData() {
		return ((net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket)this).data;
	}
}
// CatServer end
