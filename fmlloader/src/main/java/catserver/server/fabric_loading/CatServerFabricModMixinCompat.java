package catserver.server.fabric_loading;

import com.google.common.collect.Lists;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class CatServerFabricModMixinCompat {
    public static final String c_Mixin_desc = "Lorg/spongepowered/asm/mixin/Mixin;";
    private static final String ANNOTATION_Inject = "Lorg/spongepowered/asm/mixin/injection/Inject;";
    private static final String ANNOTATION_ModifyVariable = "Lorg/spongepowered/asm/mixin/injection/ModifyVariable;";
    private static final String ANNOTATION_ModifyArg = "Lorg/spongepowered/asm/mixin/injection/ModifyArg;";
    private static final String ANNOTATION_Redirect = "Lorg/spongepowered/asm/mixin/injection/Redirect;";
    private static final String ANNOTATION_ModifyConstant = "Lorg/spongepowered/asm/mixin/injection/ModifyConstant;";
    private static final String ANNOTATION_ModifyExpressionValue = "Lcom/llamalad7/mixinextras/injector/ModifyExpressionValue;";
    private static final String ANNOTATION_At = "Lorg/spongepowered/asm/mixin/injection/At;";
    private static final String ANNOTATION_WrapOperation = "Lcom/llamalad7/mixinextras/injector/wrapoperation/WrapOperation;";
    private static final String ANNOTATION_Overwrite = "Lorg/spongepowered/asm/mixin/Overwrite;";
    private static final String c_ServerPlayerGameMode_desc = "Lnet/minecraft/server/level/ServerPlayerGameMode;";
    private static final String c_Player_desc = "Lnet/minecraft/world/entity/player/Player;";
    private static final String c_Block_desc = "Lnet/minecraft/world/level/block/Block;";
    private static final String c_Level_desc = "Lnet/minecraft/world/level/Level;";
    private static final String c_Item_desc = "Lnet/minecraft/world/item/Item;";
    private static final String c_Container_desc = "Lnet/minecraft/world/Container;";
    private static final String c_AbstractContainerMenu_desc = "Lnet/minecraft/world/inventory/AbstractContainerMenu;";
    private static final String c_BlockEntity_desc = "Lnet/minecraft/world/level/block/entity/BlockEntity;";
    private static final String c_ContainerEntity_desc = "Lnet/minecraft/world/entity/vehicle/ContainerEntity;";
    private static final String c_ServerGamePacketListenerImpl_name = "net/minecraft/server/network/ServerGamePacketListenerImpl";
    private static final String c_ServerGamePacketListenerImpl_desc = desc(c_ServerGamePacketListenerImpl_name);
    private static final String c_CallbackInfoReturnable_desc = "Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;";
    private static final String c_Boolean_desc = "Ljava/lang/Boolean;";
    private static final String c_BlockPos_desc = "Lnet/minecraft/core/BlockPos;";
    private static final String c_Entity_desc = "Lnet/minecraft/world/entity/Entity;";
    private static final String c_AABB_name = "net/minecraft/world/phys/AABB";
    private static final String c_AABB_desc = desc(c_AABB_name);
    private static final String c_Operation_name = "com/llamalad7/mixinextras/injector/wrapoperation/Operation";
    private static final String c_Operation_desc = desc(c_Operation_name);
    private static final String c_Object_desc = "Ljava/lang/Object;";
    private static final String c_Double_name = "java/lang/Double";
    private static final String c_IForgePlayer_desc = "Lnet/minecraftforge/common/extensions/IForgePlayer;";
    private static final String c_LivingEntity_name = "net/minecraft/world/entity/LivingEntity";
    private static final String c_ServerPlayer_name = "net/minecraft/server/level/ServerPlayer";
    private static final String c_Entity_name = "net/minecraft/world/entity/Entity";
    private static final String c_LivingEntity_desc = desc(c_LivingEntity_name);
    private static final String c_PlayerList_name = "net/minecraft/server/players/PlayerList";
    private static final String c_PlayerList_desc = desc(c_PlayerList_name);
    private static final String c_PhantomSpawner_name = "net/minecraft/world/level/levelgen/PhantomSpawner";
    private static final String c_PhantomSpawner_desc = desc(c_PhantomSpawner_name);
    private static final String c_EnchantmentHelper_name = "net/minecraft/world/item/enchantment/EnchantmentHelper";
    private static final String c_EnchantmentHelper_desc = desc(c_EnchantmentHelper_name);
    private static final String c_FoodData_name = "net/minecraft/world/food/FoodData";
    private static final String c_FoodData_desc = desc(c_FoodData_name);
    private static final String c_ItemStack_name = "net/minecraft/world/item/ItemStack";
    private static final String c_ItemStack_desc = desc(c_ItemStack_name);
    private static final String m_Double_doubleValue_name = "doubleValue";
    private static final String m_Double_doubleValue_desc = "()D";
    private static final List<Entry<String, String>> m_ServerPlayerGameMode$tryBreakBlock = List.of(new SimpleEntry<>("tryBreakBlock", "(Lnet/minecraft/util/math/BlockPos;)Z"));
    private static final List<Entry<String, String>> m_ServerPlayerGameMode$handleBlockBreakAction = List.of(
            new SimpleEntry<>("m_214168_", "(Lnet/minecraft/core/BlockPos;Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;Lnet/minecraft/core/Direction;II)V"),
            new SimpleEntry<>("processBlockBreakingAction", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/network/packet/c2s/play/PlayerActionC2SPacket$Action;Lnet/minecraft/util/math/Direction;II)V")
    );
    private static final String c_apoli_ServerPlayerInteractionManagerMixin_name = "io/github/apace100/apoli/mixin/ServerPlayerInteractionManagerMixin";
    private static final String m_ItemStack_mineBlock = "Lnet/minecraft/item/ItemStack;postMine(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V";
    private static final String m_Vec3_distanceToSqr = "Lnet/minecraft/world/phys/Vec3;m_82557_(Lnet/minecraft/world/phys/Vec3;)D";
    private static final String m_AABB$inflate = "Lnet/minecraft/world/phys/AABB;m_82377_(DDD)Lnet/minecraft/world/phys/AABB;";
    private static final String m_ItemStack_getFoodProperties = c_ItemStack_desc + "getFoodProperties(" + c_LivingEntity_desc + ")Lnet/minecraft/world/food/FoodProperties;";
    private static final String m_Enchantment_canApplyAtEnchantingTable = "Lnet/minecraft/world/item/enchantment/Enchantment;canApplyAtEnchantingTable(" + c_ItemStack_desc + ")Z";
    private static final String m_FoodData$eat_args_Item_ItemStack_LivingEntity = c_FoodData_desc + "eat(" + c_Item_desc + c_ItemStack_desc + c_LivingEntity_desc + ")V";
    private static final String m_ItemStack_isEdible = c_ItemStack_desc + "m_41614_()Z";
    private static final String m_IForgePlayer_canReach_args_BlockPos_D = "canReach(Lnet/minecraft/core/BlockPos;D)Z";
    private static final String m_IForgePlayer_canReachRaw_args_BlockPos_D = "canReachRaw(" + c_BlockPos_desc + "D)Z";
    private static final String m_IForgePlayer_isCloseEnough_args_Entity_D = "isCloseEnough(" + c_Entity_desc + "D)Z";
    private static final String m_IForgePlayer_catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$Redirect$OverwriteTarget_name = "catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$Redirect$OverwriteTarget";
    private static final String m_IForgePlayer_catserver$IForgePlayer$canReach$distanceToSqr$ModifyArg$OverwriteTarget_name = "catserver$IForgePlayer$canReach$distanceToSqr$ModifyArg$OverwriteTarget";
    private static final String m_IForgePlayer_catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$WrapOperation$OverwriteTarget_name = "catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$WrapOperation$OverwriteTarget";
    private static final String m_Item_catserver$Item$getPlayerPOVHitResult$MixinTarget = "catserver$Item$getPlayerPOVHitResult$MixinTarget(D)D";
    private static final String m_Player_catserver$fabricModRedirectTarget_IForgeItem$getSweepHitBox_AABB$inflate_name = "catserver$fabricModRedirectTarget_IForgeItem$getSweepHitBox_AABB$inflate";
    private static final String m_Player_catserver$fabricModRedirectTarget_IForgeItem$getSweepHitBox_AABB$inflate_desc = "(" + c_AABB_desc + "DDD" + c_Operation_desc + c_Entity_desc + ")" + c_Object_desc;
    private static final String m_Player_catserver$fabricModRedirectTarget_IForgeItem$getSweepHitBox_AABB$inflate = m_Player_catserver$fabricModRedirectTarget_IForgeItem$getSweepHitBox_AABB$inflate_name + m_Player_catserver$fabricModRedirectTarget_IForgeItem$getSweepHitBox_AABB$inflate_desc;
    private static final String m_AbstractContainerMenu_catserver$AbstractContainerMenu$stillValid$64_0D$ModifyConstant$OverwriteTarget_name = "catserver$AbstractContainerMenu$stillValid$64_0D$ModifyConstant$OverwriteTarget";
    private static final String m_AbstractContainerMenu_catserver$AbstractContainerMenu$stillValid$64_0D$ModifyExpressionValue$OverwriteTarget_name = "catserver$AbstractContainerMenu$stillValid$64_0D$ModifyExpressionValue$OverwriteTarget";
    private static final String m_PlayerList_catserver$PlayerList$respawn$serverplayer$initInventoryMenu$Inject$OverwriteTarget_name = "catserver$PlayerList$respawn$serverplayer$initInventoryMenu$Inject$OverwriteTarget";
    private static final String f_ServerPlayerGameMode_f_9245__name = "f_9245_";
    private static final String f_ServerGamePacketListenerImpl_f_9743_name = "f_9743_";
    private static final List<String> f_ServerGamePacketListenerImpl_MAX_INTERACTION_DISTANCE = List.of(
            "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D",
            "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;f_215198_:D"
    );
    private static final String f_ServerGamePacketListenerImpl$f_215198__name = "f_215198_";
    private static final List<Entry<String, String>> m_Player$getDigSpeed_args_BlockState = List.of(
            new SimpleEntry<>("m_36281_", "(Lnet/minecraft/world/level/block/state/BlockState;)F"),
            new SimpleEntry<>("getBlockBreakingSpeed", "(Lnet/minecraft/block/BlockState;)F")
    );
    private static final String m_Player$getDigSpeed_args_BlockState_BlockPos = "getDigSpeed(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Blockpos;)F";
    private static final List<Entry<String, String>> m_Item$getPlayerPOVHitResult = List.of(
            new SimpleEntry<>("m_41435_", "(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/ClipContext$Fluid;)Lnet/minecraft/world/phys/BlockHitResult;"),
            new SimpleEntry<>("raycast", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/RaycastContext$FluidHandling;)Lnet/minecraft/util/hit/BlockHitResult;")
    );
    private static final List<Entry<String, String>> m_Container$stillValidBlockEntity_args_BlockEntity_Player_I = List.of(
            new SimpleEntry<>("m_271806_", "(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/player/Player;I)Z"),
            new SimpleEntry<>("canPlayerUse", "(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/player/PlayerEntity;I)Z")
    );
    private static final String m_Container$stillValidBlockEntity_args_BlockEntity_Player_D = "stillValidBlockEntity(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/entity/player/PlayerEntity;D)Z";
    private static final List<Entry<String, String>> m_AbstractContainerMenu$lambda$stillValid$0_vanilla = List.of(
            new SimpleEntry<>("m_38913_", "(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Ljava/lang/Boolean;"),
            new SimpleEntry<>("method_17696", "(Lnet/minecraft/block/Block;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Ljava/lang/Boolean;")
    );
    private static final String m_AbstractContainerMenu$lambda$stillValid$0_forge = "lambda$stillValid$0(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Ljava/lang/Boolean;";
    private static final List<Entry<String, String>> m_ContainerEntity$isChestVehicleStillValid = List.of(
            new SimpleEntry<>("m_219954_", "(Lnet/minecraft/world/entity/player/Player;)Z"),
            new SimpleEntry<>("canPlayerAccess", "(Lnet/minecraft/entity/player/PlayerEntity;)Z")
    );
    private static final String m_ContainerEntity_catserver$ContainerEntity$isChestVehicleStillValid$MixinTarget = "catserver$ContainerEntity$isChestVehicleStillValid$MixinTarget(D)D";
    private static final List<Entry<String, String>> m_ServerGamePacketListener$handleInteract = List.of(
            new SimpleEntry<>("m_6946_", "(Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;)V"),
            new SimpleEntry<>("onPlayerInteractEntity", "(Lnet/minecraft/network/packet/c2s/play/PlayerInteractEntityC2SPacket;)V")
    );
    private static final List<Entry<String, String>> m_ServerGamePacketListener$handleUseItemOn = List.of(
            new SimpleEntry<>("m_6371_", "(Lnet/minecraft/network/protocol/game/ServerboundUseItemOnPacket;)V"),
            new SimpleEntry<>("onPlayerInteractBlock", "(Lnet/minecraft/network/packet/c2s/play/PlayerInteractBlockC2SPacket;)V")
    );
    private static final List<Entry<String, String>> m_Player$attack = List.of(
            new SimpleEntry<>("m_5706_", "(Lnet/minecraft/world/entity/Entity;)V"),
            new SimpleEntry<>("attack", "(Lnet/minecraft/entity/Entity;)V")
    );
    private static final List<Entry<String, String>> m_LivingEntity$addEatEffect = List.of(
            new SimpleEntry<>("m_21063_", "(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V"),
            new SimpleEntry<>("applyFoodEffects", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)V")
    );
    private static final List<Entry<String, String>> m_Item$use = List.of(
            new SimpleEntry<>("m_7203_", "(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;"),
            new SimpleEntry<>("use", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;")
    );
    private static final List<Entry<String, String>> m_LivingEntity$shouldTriggerItemUseEffects = List.of(
            new SimpleEntry<>("m_21332_", "()Z"),
            new SimpleEntry<>("shouldSpawnConsumptionEffects", "()Z")
    );
    private static final List<Entry<String, String>> m_PlayerList$respawn = List.of(
            new SimpleEntry<>("m_11236_", "(Lnet/minecraft/server/level/ServerPlayer;Z)Lnet/minecraft/server/level/ServerPlayer;"),
            new SimpleEntry<>("respawnPlayer", "(Lnet/minecraft/server/network/ServerPlayerEntity;Z)Lnet/minecraft/server/network/ServerPlayerEntity;")
    );
    private static final List<Entry<String, String>> m_PhantomSpawner$tick = List.of(
            new SimpleEntry<>("m_7995_", "(Lnet/minecraft/server/level/ServerLevel;ZZ)I"),
            new SimpleEntry<>("spawn", "(Lnet/minecraft/server/world/ServerWorld;ZZ)I")
    );
    private static final List<Entry<String, String>> m_EnchantmentHelper$getAvailableEnchantmentResults = List.of(
            new SimpleEntry<>("m_44817_", "(ILnet/minecraft/world/item/ItemStack;Z)Ljava/util/List;"),
            new SimpleEntry<>("getPossibleEntries", "(ILnet/minecraft/item/ItemStack;Z)Ljava/util/List;")
    );
    private static final List<Entry<String, String>> m_FoodData$eat_args_Item_ItemStack = List.of(
            new SimpleEntry<>("m_38712_", "(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V"),
            new SimpleEntry<>("eat", "(Lnet/minecraft/item/Item;Lnet/minecraft/item/ItemStack;)V")
    );
    private static final List<String> m_Item_getFoodProperties_full = List.of(
            "Lnet/minecraft/world/item/Item;m_41473_()Lnet/minecraft/world/food/FoodProperties;",
            "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;"
    );
    private static final List<String> m_Item_isEdible_full = List.of(
            "Lnet/minecraft/world/item/Item;m_41472_()Z",
            "Lnet/minecraft/item/Item;isFood()Z"
    );
    private static final List<String> m_ServerPlayer_initInventoryMenu_full = List.of(
            "Lnet/minecraft/server/level/ServerPlayer;m_143429_()V",
            "Lnet/minecraft/server/network/ServerPlayerEntity;onSpawn()V"
    );
    private static final List<String> m_Random_nextInt_full = List.of(
            "Lnet/minecraft/util/math/random/Random;nextInt(I)I"
    );
    private static final List<String> m_EnchantmentCategory_canEnchant_full = List.of(
            "Lnet/minecraft/world/item/enchantment/EnchantmentCategory;m_7454_(Lnet/minecraft/world/item/Item;)Z",
            "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"
    );

    // Mixin annotation constants
    private static final String METHOD = "method";
    private static final String VALUE = "value";
    private static final String AT = "at";
    private static final String ORDINAL = "ordinal";
    private static final String TARGET = "target";
    private static final String OPCODE = "opcode";
    private static final String INDEX = "index";
    private static final String RETURN = "RETURN";
    private static final String INVOKE = "INVOKE";
    private static final String FIELD = "FIELD";
    private static final String DOUBLE_VALUE = "doubleValue";
    private static final String CONSTANT_LOWER = "constant";
    private static final String CONSTANT_UPPER = "CONSTANT";
    private static final String ARGS = "args";

    public static boolean tryFixMixin(final ClassNode classNode, final AnnotationNode mixinAnnotation) {
        boolean classNodeChanged = false;
        try {
            if (isMixinTargetClassMatched(mixinAnnotation, c_ServerPlayerGameMode_desc)) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.visibleAnnotations != null) {
                        for (AnnotationNode methodAnnotation : methodNode.visibleAnnotations) {
                            if (ANNOTATION_Inject.equals(methodAnnotation.desc)) {
                                // @Inject
                                // apoli-2.9.2+mc.1.20.x:ServerPlayerInteractionManagerMixin#actionOnBlockBreak
                                // @Inject(
                                //    method = {"tryBreakBlock"},
                                //    at = {
                                //      @At(
                                //        value = "RETURN",
                                //        ordinal = 4,
                                //        shift = Shift.BEFORE
                                //      )
                                //    },
                                //    locals = LocalCapture.CAPTURE_FAILHARD
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_ServerPlayerGameMode$tryBreakBlock)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueOrdinal_FromArrayAtMixinAnnotation(methodAnnotation, RETURN, 4)) instanceof AnnotationNode atAnnotationNode) { // just check null value
                                        modifyOrdinalWithoutCheck(atAnnotationNode, 6);
                                        if (c_apoli_ServerPlayerInteractionManagerMixin_name.equals(classNode.name)) {
                                            fix_apoli_ServerPlayerInteractionManagerMixin_actionOnBlockBreak(methodNode);
                                        }
                                        classNodeChanged = true;
                                    } else if (((Object) tryGetAtAnnotation_MatchesValueOrdinal_FromArrayAtMixinAnnotation(methodAnnotation, RETURN, 3) instanceof AnnotationNode atAnnotationNode)) {
                                        modifyOrdinalWithoutCheck(atAnnotationNode, 4);
                                        classNodeChanged = true;
                                    } else if (((Object) tryGetAtAnnotation_MatchesValueOrdinal_FromArrayAtMixinAnnotation(methodAnnotation, RETURN, 2) instanceof AnnotationNode atAnnotationNode)) {
                                        modifyOrdinalWithoutCheck(atAnnotationNode, 3);
                                        classNodeChanged = true;
                                    }
                                }
                            } else if (ANNOTATION_ModifyVariable.equals(methodAnnotation.desc)) {
                                // @ModifyVariable
                                // apoli-2.9.2+mc.1.20.x:ServerPlayerInteractionManagerMixin#modifyEffectiveTool
                                // @ModifyVariable(
                                //   method = {"tryBreakBlock"},
                                //   at = @At(
                                //     value = "INVOKE",
                                //     target = "Lnet/minecraft/item/ItemStack;postMine(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V"
                                //   ),
                                //    ordinal = 1
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_ServerPlayerGameMode$tryBreakBlock)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTarget_FromSingleAtMixinAnnotation(methodAnnotation, INVOKE, m_ItemStack_mineBlock)) instanceof AnnotationNode atAnnotationNode) {
                                        if (methodNode.desc != null && methodNode.desc.endsWith(")Z")) {
                                            if (isModifyVariableAnnotationMatchesOrdinal(methodAnnotation, 1)) {
                                                modifyOrdinalWithoutCheck(methodAnnotation, 0);
                                                classNodeChanged = true;
                                            }
                                        }
                                    }
                                }
                            } else if (ANNOTATION_ModifyArg.equals(methodAnnotation.desc)) {
                                // @ModifyArg
                                // Pehkui-3.8.3+1.14.4-1.21:virtuoel.pehkui.mixin.compat1204minus.compat119plus.ServerPlayerInteractionManagerMixin#pehkui$processBlockBreakingAction$center
                                // @ModifyArg(
                                //   method = {"method_14263(Lnet/minecraft/class_2338;Lnet/minecraft/class_2846$class_2847;Lnet/minecraft/class_2350;II)V"},
                                //   index = 0,
                                //   at = @At(
                                //     value = "INVOKE",
                                //     target = "Lnet/minecraft/class_243;method_1025(Lnet/minecraft/class_243;)D"
                                //   )
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_ServerPlayerGameMode$handleBlockBreakAction)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTarget_FromSingleAtMixinAnnotation(methodAnnotation, INVOKE, m_Vec3_distanceToSqr)) instanceof AnnotationNode atAnnotationNode && classNode.methods.size() == 2) { // 2 methods: <init> pehkui$processBlockBreakingAction$center. if there are more methods, we cannot simply change the @Mixin() annotation...
                                        // @Mixin({class_3225.class}) -> @Mixin({IForgePlayer.class})
                                        redirectMixinTargetClassWithoutCheck(mixinAnnotation, c_IForgePlayer_desc);
                                        // @Overwrite
                                        changeExistingMixinAnnotationToOverwriteAndRenameMethod(methodNode, methodAnnotation, m_IForgePlayer_catserver$IForgePlayer$canReach$distanceToSqr$ModifyArg$OverwriteTarget_name);
                                        // this.field_14008 -> (ServerPlayer)this
                                        replaceGetFieldToThisWithForceCast(methodNode, f_ServerPlayerGameMode_f_9245__name, c_ServerPlayer_name);
                                        // remove shadow field field_14008
                                        removeFieldWithoutCheck(classNode, f_ServerPlayerGameMode_f_9245__name);
                                        // interface
                                        setClassToInterfaceWithoutCheck(classNode);
                                        // public default
                                        setMethodToInterfaceDefaultWithoutCheck(methodNode);
                                        classNodeChanged = true;
                                    }
                                }
                            } else if (ANNOTATION_Redirect.equals(methodAnnotation.desc)) {
                                // @Redirect
                                // reach-entity-attributes-2.4.0:com.jamieswhiteshirt.reachentityattributes.mixin.ServerPlayerInteractionManagerMixin#getActualReachDistance
                                // @Redirect(
                                //   method = {"processBlockBreakingAction"},
                                //   at = @At(
                                //     value = "FIELD",
                                //     target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D",
                                //     opcode = 178
                                //   )
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_ServerPlayerGameMode$handleBlockBreakAction)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTargetOpcode_FromSingleAtMixinAnnotation(methodAnnotation, FIELD, f_ServerGamePacketListenerImpl_MAX_INTERACTION_DISTANCE, Opcodes.GETSTATIC)) instanceof AnnotationNode atAnnotationNode && classNode.methods.size() == 2) { // 2 methods: <init> getActualReachDistance. if there are more methods, we cannot simply change the @Mixin() annotation...
                                        // @Mixin({class_3225/ServerPlayerGameMode.class}) -> @Mixin({IForgePlayer.class})
                                        redirectMixinTargetClassWithoutCheck(mixinAnnotation, c_IForgePlayer_desc);
                                        // @Overwrite
                                        changeExistingMixinAnnotationToOverwriteAndRenameMethod(methodNode, methodAnnotation, m_IForgePlayer_catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$Redirect$OverwriteTarget_name);
                                        // this.field_14008 -> (LivingEntity)this
                                        replaceGetFieldToThisWithForceCast(methodNode, f_ServerPlayerGameMode_f_9245__name, c_ServerPlayer_name);
                                        // fix parameters and local variables
                                        fix_reach_entity_attributes_ServerPlayerInteractionManagerMixin_getActualReachDistance(methodNode);
                                        // ServerGamePacketListenerImpl.f_215198_ -> parameter
                                        redirectGetStaticToGetLocalVariableWithForceCast(methodNode, c_ServerGamePacketListenerImpl_name, f_ServerGamePacketListenerImpl$f_215198__name, 1, Opcodes.DLOAD);
                                        // remove shadow field field_14008
                                        removeFieldWithoutCheck(classNode, f_ServerPlayerGameMode_f_9245__name);
                                        // interface
                                        setClassToInterfaceWithoutCheck(classNode);
                                        // public default
                                        setMethodToInterfaceDefaultWithoutCheck(methodNode);
                                        classNodeChanged = true;
                                    }
                                }
                            } else if (ANNOTATION_WrapOperation.equals(methodAnnotation.desc)) {
                                // @WrapOperation
                                // pehkui-3.8.3+1.14.4-1.21:virtuoel.pehkui.mixin.reach.compat1204minus.compat119plus.ServerPlayerInteractionManagerMixin#pehkui$processBlockBreakingAction$distance
                                // @WrapOperation(
                                //   method = {"m_214168_"},
                                //   at = {
                                //     @At(
                                //       value = "FIELD",
                                //       opcode = 178,
                                //       target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;f_215198_:D"
                                //     )
                                //   }
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_ServerPlayerGameMode$handleBlockBreakAction)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTargetOpcode_FromArrayAtMixinAnnotation(methodAnnotation, FIELD, f_ServerGamePacketListenerImpl_MAX_INTERACTION_DISTANCE, Opcodes.GETSTATIC)) instanceof AnnotationNode atAnnotationNode) {
                                        // @Mixin({class_3225/ServerPlayerGameMode.class}) -> @Mixin({IForgePlayer.class})
                                        redirectMixinTargetClassWithoutCheck(mixinAnnotation, c_IForgePlayer_desc);
                                        // @Overwrite
                                        changeExistingMixinAnnotationToOverwriteAndRenameMethod(methodNode, methodAnnotation, m_IForgePlayer_catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$WrapOperation$OverwriteTarget_name);
                                        // this.field_14008/f_9245_ -> (LivingEntity)this
                                        replaceGetFieldToThisWithForceCast(methodNode, f_ServerPlayerGameMode_f_9245__name, c_Entity_name);
                                        // (Operation<Double> original) -> (double original)
                                        change_Operation$Double_to_double(methodNode);
                                        // remove shadow field field_14008
                                        removeFieldWithoutCheck(classNode, f_ServerPlayerGameMode_f_9245__name);
                                        // interface
                                        setClassToInterfaceWithoutCheck(classNode);
                                        // public default
                                        setMethodToInterfaceDefaultWithoutCheck(methodNode);
                                        classNodeChanged = true;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (isMixinTargetClassMatched(mixinAnnotation, c_Player_desc)) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.visibleAnnotations != null) {
                        for (AnnotationNode methodAnnotation : methodNode.visibleAnnotations) {
                            if (ANNOTATION_ModifyVariable.equals(methodAnnotation.desc)) {
                                // @ModifyVariable
                                // additionalentityattributes-1.7.4+1.20.0:de.dafuqs.additionalentityattributes.mixin.common.PlayerEntityMixin#additionalEntityAttributes$adjustBlockBreakingSpeed
                                // @ModifyVariable(
                                //   method = {"getBlockBreakingSpeed"},
                                //   at = @At(
                                //     value = "INVOKE",
                                //     target = "Lnet/minecraft/entity/effect/StatusEffectUtil;hasHaste(Lnet/minecraft/entity/LivingEntity;)Z"
                                //   ),
                                //   index = 2
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_Player$getDigSpeed_args_BlockState)) {
                                    // always redirect the target method because forge moved the whole method body to a new method
                                    // public float getDestroySpeed(BlockState p_36282_) -> public float getDigSpeed(BlockState p_36282_, @Nullable BlockPos pos)
                                    redirectMixinTargetMethodWithoutCheck(methodAnnotation, m_Player$getDigSpeed_args_BlockState_BlockPos);
                                    if (mixinAnnotationHas(methodAnnotation, INDEX, true)) {
                                        setMixinAnnotationIndexWithoutCheck(methodAnnotation, 1);
                                    }
                                    classNodeChanged = true;
                                }
                            } else if (ANNOTATION_WrapOperation.equals(methodAnnotation.desc)) {
                                // @WrapOperation
                                // pehkui-3.8.3+1.14.4-1.21:virtuoel.pehkui.mixin.PlayerEntityMixin#pehkui$attack$expand
                                // @WrapOperation(
                                //   method = {"m_5706_"},
                                //   at = {
                                //     @At(
                                //       value = "INVOKE",
                                //       target = "Lnet/minecraft/world/phys/AABB;m_82377_(DDD)Lnet/minecraft/world/phys/AABB;"
                                //     )
                                //   }
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_Player$attack)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTarget_FromArrayAtMixinAnnotation(methodAnnotation, INVOKE, m_AABB$inflate)) instanceof AnnotationNode atAnnotationNode) {
                                        // @WrapOperation(...) -> @Overwrite
                                        replaceAnnotationToOverwriteWithoutCheck(methodAnnotation);
                                        // ret AABB -> ret Object, remove original.call() and (AABB)
                                        fix_pehkui_PlayerEntityMixin_pehkui$attack$expand(methodNode);
                                        classNodeChanged = true;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (isMixinTargetClassMatched(mixinAnnotation, c_Item_desc)) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.visibleAnnotations != null) {
                        for (AnnotationNode methodAnnotation : methodNode.visibleAnnotations) {
                            if (ANNOTATION_ModifyConstant.equals(methodAnnotation.desc)) {
                                // reach-entity-attributes-2.4.0:com.jamieswhiteshirt.reachentityattributes.mixin.ItemMixin#getActualReachDistance
                                // @ModifyConstant(
                                //   method = {"raycast(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/RaycastContext$FluidHandling;)Lnet/minecraft/util/hit/BlockHitResult;"},
                                //   require = 4,
                                //   allow = 4,
                                //   constant = {
                                //     @Constant(
                                //       doubleValue = 5.0
                                //     )
                                //   }
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_Item$getPlayerPOVHitResult)) {
                                    if (isModifyConstantValueMatched(methodAnnotation, DOUBLE_VALUE, 5.0D)) {
                                        replaceAnnotationToRedirectWithoutCheck(methodAnnotation, m_Item_catserver$Item$getPlayerPOVHitResult$MixinTarget);
                                        classNodeChanged = true;
                                    }
                                }
                            } else if (ANNOTATION_ModifyExpressionValue.equals(methodAnnotation.desc)) {
                                if (isTargetMethodMatched(methodAnnotation, m_Item$use)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTarget_FromArrayAtMixinAnnotation(methodAnnotation, INVOKE, m_Item_isEdible_full)) instanceof AnnotationNode atAnnotationNode) {
                                        // shape-shifter-curse-1.8.2:net.onixary.shapeShifterCurseFabric.mixin.CustomEdibleItemMixin#use$isFood
                                        // @ModifyExpressionValue(
                                        //   method = {"use"},
                                        //   at = {
                                        //     @At(
                                        //       value = "INVOKE",
                                        //       target = "Lnet/minecraft/item/Item;isFood()Z"
                                        //     )
                                        //   }
                                        // )
                                        redirectAtTargetWithoutCheck(atAnnotationNode, m_ItemStack_isEdible);
                                        classNodeChanged = true;
                                    } else if (((Object) tryGetAtAnnotation_MatchesValueTarget_FromArrayAtMixinAnnotation(methodAnnotation, INVOKE, m_Item_getFoodProperties_full)) instanceof AnnotationNode atAnnotationNode) {
                                        // shape-shifter-curse-1.8.2:net.onixary.shapeShifterCurseFabric.mixin.CustomEdibleItemMixin#use$getFoodComponent
                                        // @ModifyExpressionValue(
                                        //   method = {"use"},
                                        //   at = {
                                        //     @At(
                                        //       value = "INVOKE",
                                        //       target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;"
                                        //     )
                                        //   }
                                        // )
                                        redirectAtTargetWithoutCheck(atAnnotationNode, m_ItemStack_getFoodProperties); // here is INVOKEVIRTUAL ItemStack#getFoodProperties rather than INVOKEINTERFACE IForgeItemStack#getFoodProperties
                                        classNodeChanged = true;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (isMixinTargetClassMatched(mixinAnnotation, c_Container_desc)) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.visibleAnnotations != null) {
                        for (AnnotationNode methodAnnotation : methodNode.visibleAnnotations) {
                            if (ANNOTATION_Inject.equals(methodAnnotation.desc)) {
                                // reach-entity-attributes-2.4.0:com.jamieswhiteshirt.reachentityattributes.mixinInventoryValidationMixin#checkWithinActualReach
                                // @Inject(
                                //   method = {"canPlayerUse(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/player/PlayerEntity;I)Z"},
                                //   require = 1,
                                //   allow = 1,
                                //   at = {
                                //     @At(
                                //       shift = Shift.BEFORE,
                                //       value = "INVOKE",
                                //       target = "Lnet/minecraft/util/math/BlockPos;getX()I"
                                //     )
                                //   },
                                //   locals = LocalCapture.CAPTURE_FAILHARD,
                                //   cancellable = true
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_Container$stillValidBlockEntity_args_BlockEntity_Player_I)) {
                                    redirectMixinTargetMethodWithoutCheck(methodAnnotation, m_Container$stillValidBlockEntity_args_BlockEntity_Player_D);
                                    fix_reach_entity_attributes_InventoryValidationMixin_checkWithinActualReach(methodNode);
                                    classNodeChanged = true;
                                }
                            }
                        }
                    }
                }
            } else if (isMixinTargetClassMatched(mixinAnnotation, c_AbstractContainerMenu_desc)) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.visibleAnnotations != null) {
                        for (AnnotationNode methodAnnotation : methodNode.visibleAnnotations) {
                            if (ANNOTATION_ModifyConstant.equals(methodAnnotation.desc)) {
                                // reach-entity-attributes-2.4.0:com.jamieswhiteshirt.reachentityattributes.ScreenHandlerMixin#getActualReachDistance
                                // @ModifyConstant(
                                //   method = {"method_17696(Lnet/minecraft/block/Block;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Ljava/lang/Boolean;"},
                                //   require = 1,
                                //   allow = 1,
                                //   constant = {
                                //     @Constant(
                                //       doubleValue = 64.0
                                //     )
                                //   }
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_AbstractContainerMenu$lambda$stillValid$0_vanilla)) {
                                    if (isModifyConstantValueMatched(methodAnnotation, DOUBLE_VALUE, 64.0D)) {
                                        replaceAnnotationToOverwriteWithoutCheck(methodAnnotation);
                                        changeExistingMixinAnnotationToOverwriteAndRenameMethod(methodNode, methodAnnotation, m_AbstractContainerMenu_catserver$AbstractContainerMenu$stillValid$64_0D$ModifyConstant$OverwriteTarget_name);
                                        fix_reach_entity_attributes_ScreenHandlerMixin_getActualReachDistance__and__pehkui_ScreenHandlerMixin_pehkui$canUse$distance(methodNode);
                                        classNodeChanged = true;
                                    }
                                }
                            } else if (ANNOTATION_ModifyExpressionValue.equals(methodAnnotation.desc)) {
                                if (isTargetMethodMatched(methodAnnotation, m_AbstractContainerMenu$lambda$stillValid$0_vanilla)) {
                                    if (((Object)tryGetAtAnnotation_valueIsCONSTANT_MatchesConstant_FromArrayAtMixinAnnotation(methodAnnotation, DOUBLE_VALUE + "=" + "0.5D")) instanceof AnnotationNode annotationNode) {
                                        // Pehkui-3.8.3+1.14.4-1.21:virtuoel.pehkui.mixin.compat1204minus.ScreenHandlerMixin#pehkui$canUse$xOffset/pehkui$canUse$yOffset/pehkui$canUse$zOffset
                                        // @ModifyExpressionValue(
                                        //   method = {"m_38913_"},
                                        //   at = {
                                        //     @At(
                                        //       value = "CONSTANT",
                                        //       args = {"doubleValue=0.5D"},
                                        //       ordinal = 0/1/2
                                        //     )
                                        //   }
                                        // )
                                        redirectMixinTargetMethodWithoutCheck(methodAnnotation, m_AbstractContainerMenu$lambda$stillValid$0_forge);
                                        fix_pehkui_ScreenHandlerMixin_pehkui$canUse$xyzOffset(methodNode);
                                        classNodeChanged = true;
                                    } else if (((Object)tryGetAtAnnotation_valueIsCONSTANT_MatchesConstant_FromArrayAtMixinAnnotation(methodAnnotation, DOUBLE_VALUE + "=" + "64.0D")) instanceof AnnotationNode annotationNode) {
                                        // pehkui-3.8.3+1.14.4-1.21:virtuoel.pehkui.mixin.reach.compat1204minus.ScreenHandlerMixin#pehkui$canUse$distance
                                        // @ModifyExpressionValue(
                                        //   method = {"m_38913_"},
                                        //   at = {
                                        //     @At(
                                        //       value = "CONSTANT",
                                        //       args = {"doubleValue=64.0D"}
                                        //     )
                                        //   }
                                        // )
                                        replaceAnnotationToOverwriteWithoutCheck(methodAnnotation);
                                        changeExistingMixinAnnotationToOverwriteAndRenameMethod(methodNode, methodAnnotation, m_AbstractContainerMenu_catserver$AbstractContainerMenu$stillValid$64_0D$ModifyExpressionValue$OverwriteTarget_name);
                                        fix_reach_entity_attributes_ScreenHandlerMixin_getActualReachDistance__and__pehkui_ScreenHandlerMixin_pehkui$canUse$distance(methodNode);
                                        classNodeChanged = true;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (isMixinTargetClassMatched(mixinAnnotation, c_ContainerEntity_desc)) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.visibleAnnotations != null) {
                        for (AnnotationNode methodAnnotation : methodNode.visibleAnnotations) {
                            if (ANNOTATION_ModifyConstant.equals(methodAnnotation.desc)) {
                                // reach-entity-attributes-2.4.0:com.jamieswhiteshirt.reachentityattributes.VehicleInventoryValidationMixin#getActualReachDistance
                                // @ModifyConstant(
                                //   method = {"canPlayerAccess(Lnet/minecraft/entity/player/PlayerEntity;)Z"},
                                //   require = 1,
                                //   allow = 1,
                                //   constant = {
                                //     @Constant(
                                //       doubleValue = 8.0
                                //     )
                                //   }
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_ContainerEntity$isChestVehicleStillValid)) {
                                    if (isModifyConstantValueMatched(methodAnnotation, DOUBLE_VALUE, 8.0D)) {
                                        replaceAnnotationToRedirectWithoutCheck(methodAnnotation, m_ContainerEntity_catserver$ContainerEntity$isChestVehicleStillValid$MixinTarget);
                                        classNodeChanged = true;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (isMixinTargetClassMatched(mixinAnnotation, c_ServerGamePacketListenerImpl_desc)) {
                Iterator<MethodNode> methodNodeIterator = classNode.methods.iterator();
                while (methodNodeIterator.hasNext()) {
                    MethodNode methodNode = methodNodeIterator.next();
                    if (methodNode.visibleAnnotations != null) {
                        annotationLoop:
                        for (AnnotationNode methodAnnotation : methodNode.visibleAnnotations) {
                            if (ANNOTATION_Redirect.equals(methodAnnotation.desc)) {
                                if (isTargetMethodMatched(methodAnnotation, m_ServerGamePacketListener$handleInteract)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTargetOpcode_FromSingleAtMixinAnnotation(methodAnnotation, FIELD, f_ServerGamePacketListenerImpl_MAX_INTERACTION_DISTANCE, Opcodes.GETSTATIC)) instanceof AnnotationNode atAnnotationNode) {
                                        // reach-entity-attributes-2.4.0:com.jamieswhiteshirt.reachentityattributes.ServerPlayNetworkHandlerMixin#getActualAttackRange()D
                                        // we have handled: changeExistingMixinAnnotationToOverwriteAndRenameMethod(methodNode, methodAnnotation, m_IForgePlayer_catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$Redirect$OverwriteTarget_name);
                                        methodNodeIterator.remove();
                                        classNodeChanged = true;
                                        break annotationLoop;
                                    }
                                } else if (isTargetMethodMatched(methodAnnotation, m_ServerGamePacketListener$handleUseItemOn)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTargetOpcode_FromSingleAtMixinAnnotation(methodAnnotation, FIELD, f_ServerGamePacketListenerImpl_MAX_INTERACTION_DISTANCE, Opcodes.GETSTATIC)) instanceof AnnotationNode atAnnotationNode) {
                                        // reach-entity-attributes-2.4.0:com.jamieswhiteshirt.reachentityattributes.ServerPlayNetworkHandlerMixin#getActualReachDistance()D
                                        // we have handled: changeExistingMixinAnnotationToOverwriteAndRenameMethod(methodNode, methodAnnotation, m_IForgePlayer_catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$Redirect$OverwriteTarget_name);
                                        methodNodeIterator.remove();
                                        classNodeChanged = true;
                                        break annotationLoop;
                                    }
                                }
                            } else if (ANNOTATION_ModifyConstant.equals(methodAnnotation.desc)) {
                                if (isTargetMethodMatched(methodAnnotation, m_ServerGamePacketListener$handleUseItemOn)) {
                                    if (isModifyConstantValueMatched(methodAnnotation, DOUBLE_VALUE, 64.0D)) {
                                        // reach-entity-attributes-2.4.0:com.jamieswhiteshirt.reachentityattributes.ServerPlayNetworkHandlerMixin#getActualReachDistance(D)D
                                        // we have handled: changeExistingMixinAnnotationToOverwriteAndRenameMethod(methodNode, methodAnnotation, m_IForgePlayer_catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$Redirect$OverwriteTarget_name);
                                        methodNodeIterator.remove();
                                        classNodeChanged = true;
                                        break annotationLoop;
                                    }
                                }
                            } else if (ANNOTATION_ModifyArg.equals(methodAnnotation.desc)) {
                                if (isTargetMethodMatched(methodAnnotation, m_ServerGamePacketListener$handleUseItemOn)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTarget_FromSingleAtMixinAnnotation(methodAnnotation, INVOKE, m_Vec3_distanceToSqr)) instanceof AnnotationNode atAnnotationNode) {
                                        // pehkui-3.8.3+1.14.4-1.21:virtuoel.pehkui.mixin.compat1204minus.compat119plus.ServerPlayNetworkHandlerMixin#pehkui$onPlayerInteractBlock$center
                                        // @Mixin({ServerGamePacketListenerImpl.class}) -> @Mixin({IForgePlayer.class})
                                        redirectMixinTargetClassWithoutCheck(mixinAnnotation, c_IForgePlayer_desc);
                                        // class_3225#method_14263 -> IForgePlayer#canReach
                                        redirectMixinTargetMethodWithoutCheck(methodAnnotation, m_IForgePlayer_canReach_args_BlockPos_D);
                                        // this.field_14008 -> (LivingEntity)this
                                        replaceGetFieldToThisWithForceCast(methodNode, f_ServerGamePacketListenerImpl_f_9743_name, c_ServerPlayer_name);
                                        // remove shadow field field_14008
                                        removeFieldWithoutCheck(classNode, f_ServerGamePacketListenerImpl_f_9743_name);
                                        // interface
                                        setClassToInterfaceWithoutCheck(classNode);
                                        // public default
                                        setMethodToInterfaceDefaultWithoutCheck(methodNode);
                                        classNodeChanged = true;
                                    }
                                }
                            } else if (ANNOTATION_WrapOperation.equals(methodAnnotation.desc)) {
                                if (isTargetMethodMatched(methodAnnotation, m_ServerGamePacketListener$handleInteract)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTargetOpcode_FromArrayAtMixinAnnotation(methodAnnotation, FIELD, f_ServerGamePacketListenerImpl_MAX_INTERACTION_DISTANCE, Opcodes.GETSTATIC)) instanceof AnnotationNode atAnnotationNode) {
                                        // pehkui-3.8.3+1.14.4-1.21:virtuoel.pehkui.mixin.reach.compat1204minus.compat119plus.ServerPlayNetworkHandlerMixin#pehkui$onPlayerInteractEntity$distance
                                        // we have handled: changeExistingMixinAnnotationToOverwriteAndRenameMethod(methodNode, methodAnnotation, m_IForgePlayer_catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$WrapOperation$OverwriteTarget_name);
                                        methodNodeIterator.remove();
                                        classNodeChanged = true;
                                        break annotationLoop;
                                    }
                                } else if (isTargetMethodMatched(methodAnnotation, m_ServerGamePacketListener$handleUseItemOn)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTargetOpcode_FromArrayAtMixinAnnotation(methodAnnotation, FIELD, f_ServerGamePacketListenerImpl_MAX_INTERACTION_DISTANCE, Opcodes.GETSTATIC)) instanceof AnnotationNode atAnnotationNode) {
                                        // pehkui-3.8.3+1.14.4-1.21:virtuoel.pehkui.mixin.reach.compat1204minus.compat119plus.ServerPlayNetworkHandlerMixin#pehkui$onPlayerInteractBlock$distance
                                        // we have handled: changeExistingMixinAnnotationToOverwriteAndRenameMethod(methodNode, methodAnnotation, m_IForgePlayer_catserver$IForgePlayer$canReach$ServerGamePacketListenerImpl$MAX_INTERACTION_DISTANCE$WrapOperation$OverwriteTarget_name);
                                        methodNodeIterator.remove();
                                        classNodeChanged = true;
                                        break annotationLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (isMixinTargetClassMatched(mixinAnnotation, c_LivingEntity_desc)) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.visibleAnnotations != null) {
                        for (AnnotationNode methodAnnotation : methodNode.visibleAnnotations) {
                            if (ANNOTATION_ModifyExpressionValue.equals(methodAnnotation.desc)) {
                                // shape-shifter-curse-1.8.2:net.onixary.shapeShifterCurseFabric.mixin.CustomEdiblePlayerAMixin#applyFoodEffects$getFoodComponent+shouldSpawnConsumptionEffects$getFoodComponent
                                // @ModifyExpressionValue(
                                //   method = {"applyFoodEffects"},
                                //   at = {
                                //     @At(
                                //       value = "INVOKE",
                                //       target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;"
                                //     )
                                //   }
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_LivingEntity$addEatEffect) || isTargetMethodMatched(methodAnnotation, m_LivingEntity$shouldTriggerItemUseEffects)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTarget_FromArrayAtMixinAnnotation(methodAnnotation, INVOKE, m_Item_getFoodProperties_full)) instanceof AnnotationNode atAnnotationNode) {
                                        redirectAtTargetWithoutCheck(atAnnotationNode, m_ItemStack_getFoodProperties); // here is INVOKEVIRTUAL ItemStack#getFoodProperties rather than INVOKEINTERFACE IForgeItemStack#getFoodProperties
                                        classNodeChanged = true;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (isMixinTargetClassMatched(mixinAnnotation, c_PlayerList_desc)) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.visibleAnnotations != null) {
                        for (AnnotationNode methodAnnotation : methodNode.visibleAnnotations) {
                            if (ANNOTATION_Inject.equals(methodAnnotation.desc)) {
                                // compat:fabric-mod:apoli-2.9.2+mc.1.20.x:io.github.apace100.apoli.mixin.LoginMixin#invokePowerRespawnCallback
                                // @Inject(
                                //   method = {"respawnPlayer"},
                                //   at = {
                                //     @At(
                                //       value = "INVOKE",
                                //       target = "Lnet/minecraft/server/network/ServerPlayerEntity;onSpawn()V"
                                //     )
                                //   },
                                //   locals = LocalCapture.CAPTURE_FAILHARD
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_PlayerList$respawn)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTarget_FromArrayAtMixinAnnotation(methodAnnotation, INVOKE, m_ServerPlayer_initInventoryMenu_full)) instanceof AnnotationNode atAnnotationNode) {
                                        changeExistingMixinAnnotationToOverwriteAndRenameMethod(methodNode, methodAnnotation, m_PlayerList_catserver$PlayerList$respawn$serverplayer$initInventoryMenu$Inject$OverwriteTarget_name);
                                        classNodeChanged = true;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (isMixinTargetClassMatched(mixinAnnotation, c_PhantomSpawner_desc)) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.visibleAnnotations != null) {
                        for (AnnotationNode methodAnnotation : methodNode.visibleAnnotations) {
                            if (ANNOTATION_ModifyVariable.equals(methodAnnotation.desc)) {
                                // compat:fabric-mod:apoli-2.9.2+mc.1.20.x:io.github.apace100.apoli.mixin.PhantomSpawnerMixin#modifyTicks
                                // @ModifyVariable(
                                //   method = {"spawn"},
                                //   at = @At(
                                //     value = "INVOKE",
                                //     target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I",
                                //     ordinal = 1
                                //   ),
                                //   ordinal = 1
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_PhantomSpawner$tick)) {
                                    if (isModifyVariableAnnotationMatchesOrdinal(methodAnnotation, 1) && ((Object) tryGetAtAnnotation_MatchesValueTargetOrdinal_FromSingleAtMixinAnnotation(methodAnnotation, INVOKE, m_Random_nextInt_full, 1)) instanceof AnnotationNode atAnnotationNode) {
                                        modifyOrdinalWithoutCheck(atAnnotationNode, 2);
                                        classNodeChanged = true;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (isMixinTargetClassMatched(mixinAnnotation, c_EnchantmentHelper_desc)) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.visibleAnnotations != null) {
                        for (AnnotationNode methodAnnotation : methodNode.visibleAnnotations) {
                            if (ANNOTATION_ModifyExpressionValue.equals(methodAnnotation.desc)) {
                                // compat:fabric-mod:shape-shifter-curse-1.8.2:net.onixary.shapeShifterCurseFabric.mixin.EnchantmentHelperMixin#isAcceptableItem
                                // @ModifyExpressionValue(
                                //   method = {"getPossibleEntries"},
                                //   at = {
                                //     @At(
                                //       value = "INVOKE",
                                //       target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"
                                //     )
                                //   }
                                // )
                                if (isTargetMethodMatched(methodAnnotation, m_EnchantmentHelper$getAvailableEnchantmentResults)) {
                                    if (((Object) tryGetAtAnnotation_MatchesValueTarget_FromArrayAtMixinAnnotation(methodAnnotation, INVOKE, m_EnchantmentCategory_canEnchant_full)) instanceof AnnotationNode atAnnotationNode) {
                                        redirectAtTargetWithoutCheck(atAnnotationNode, m_Enchantment_canApplyAtEnchantingTable);
                                        classNodeChanged = true;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (isMixinTargetClassMatched(mixinAnnotation, c_FoodData_desc)) {
                for (MethodNode methodNode : classNode.methods) {
                    if (methodNode.visibleAnnotations != null) {
                        for (AnnotationNode methodAnnotation : methodNode.visibleAnnotations) {
                            if (isTargetMethodMatched(methodAnnotation, m_FoodData$eat_args_Item_ItemStack)) {
                                redirectMixinTargetMethodWithoutCheck(methodAnnotation, m_FoodData$eat_args_Item_ItemStack_LivingEntity);
                                if (ANNOTATION_Inject.equals(methodAnnotation.desc)) {
                                    // compat:fabric-mod:apoli-2.9.2+mc.1.20.x:io.github.apace100.apoli.mixin.HungerManagerMixin#executeAdditionalEatAction
                                    // @Inject(
                                    //   method = {"eat"},
                                    //   at = {
                                    //     @At(
                                    //       value = "INVOKE",
                                    //       target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V",
                                    //       shift = Shift.AFTER
                                    //     )
                                    //   }
                                    // )
                                    fix_apoli_HungerManagerMixin_executeAdditionalEatAction(methodNode);
                                    classNodeChanged = true;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException("[CatServer] Something is wrong when fixing mods' mixin classes", e);
        }
        return classNodeChanged;
    }

    private static String desc(String name) {
        return "L" + name + ";";
    }

    private static void redirectAtTargetWithoutCheck(AnnotationNode atAnnotationNode, String newAtTarget) {
        atAnnotationNode.values.set(atAnnotationNode.values.indexOf(TARGET) + 1, newAtTarget);
    }

    private static void replaceAnnotationToRedirectWithoutCheck(final AnnotationNode methodAnnotationNode, final String targetMethod) {
        methodAnnotationNode.desc = ANNOTATION_Redirect;
        Object methods = methodAnnotationNode.values.get(methodAnnotationNode.values.indexOf(METHOD) + 1);
        List<Object> newValues = new ArrayList<>();
        newValues.add(METHOD);
        newValues.add(methods);
        AnnotationNode newAtAnnotation = new AnnotationNode(ANNOTATION_At);
        newAtAnnotation.values = new ArrayList<>();
        newAtAnnotation.values.add(VALUE);
        newAtAnnotation.values.add(INVOKE);
        newAtAnnotation.values.add(TARGET);
        newAtAnnotation.values.add(targetMethod);
        newValues.add(AT);
        newValues.add(newAtAnnotation);
        methodAnnotationNode.values = newValues;
    }

    private static void replaceAnnotationToOverwriteWithoutCheck(final AnnotationNode methodAnnotationNode) {
        methodAnnotationNode.desc = ANNOTATION_Overwrite;
        methodAnnotationNode.values = new ArrayList<>();
    }

    private static void changeExistingMixinAnnotationToOverwriteAndRenameMethod(final MethodNode methodNode, final AnnotationNode methodAnnotationNode, final String overwriteTargetMethodName) {
        replaceAnnotationToOverwriteWithoutCheck(methodAnnotationNode);
        methodNode.visibleAnnotations = Lists.newArrayList(methodAnnotationNode);
        methodNode.invisibleAnnotations = null;
        methodNode.name = overwriteTargetMethodName;
    }

    private static void replaceGetFieldToThisWithForceCast(final MethodNode methodNode, final String fieldName, final String forceCastTargetClass) {
        InsnList insnList = methodNode.instructions;
        AbstractInsnNode[] insnArray = insnList.toArray();

        // ALOAD 0 (this)
        // GETFIELD fieldName <----- remove this
        // INVOKE...
        for (int i = 0; i < insnArray.length; i++) {
            AbstractInsnNode insn = insnArray[i];

            if (insn.getOpcode() == Opcodes.GETFIELD && fieldName.equals(((FieldInsnNode)insn).name)) { // current one is GETFIELD fieldName
                if (i > 0 && (insnArray[i - 1].getOpcode() == Opcodes.ALOAD) && (((VarInsnNode) insnArray[i - 1]).var == 0)) { // last insn is ALOAD 0
                    insnList.insert(insn, new TypeInsnNode(Opcodes.CHECKCAST, forceCastTargetClass));
                    insnList.remove(insn);
                }
            }
        }
    }

    private static void redirectGetStaticToGetLocalVariableWithForceCast(final MethodNode methodNode, final String staticFieldHolderClass, final String staticFieldName, final int localVariableIndex, final int getLocalVariableOpcode) {
        AbstractInsnNode[] insnArray = methodNode.instructions.toArray();
        for (int i = 0; i < insnArray.length; i++) {
            AbstractInsnNode insn = insnArray[i];
            if (insn instanceof FieldInsnNode fieldInsnNode && staticFieldHolderClass.equals(fieldInsnNode.owner) && staticFieldName.equals(fieldInsnNode.name) && Integer.valueOf(Opcodes.GETSTATIC).equals(fieldInsnNode.getOpcode())) {
                VarInsnNode varInsnNode = new VarInsnNode(getLocalVariableOpcode, localVariableIndex);
                methodNode.instructions.set(fieldInsnNode, varInsnNode);
            }
        }
    }

    private static void modifyInstructionsWhenLocalVarsChanged(MethodNode methodNode, Map<Integer, Integer> localVarIndexMap) {
        for (AbstractInsnNode insn : methodNode.instructions) {
            if (insn instanceof VarInsnNode varInsn) {
                Integer newIndex = localVarIndexMap.get(varInsn.var);
                if (newIndex != null) {
                    varInsn.var = newIndex;
                }
            } else if (insn instanceof IincInsnNode iincInsn) {
                Integer newIndex = localVarIndexMap.get(iincInsn.var);
                if (newIndex != null) {
                    iincInsn.var = newIndex;
                }
            } else if (insn instanceof FrameNode frameNode) {
                if (frameNode.local != null) {
                    for (int i = 0; i < frameNode.local.size(); i++) {
                        Object local = frameNode.local.get(i);
                        if (local instanceof Integer oldIndex) {
                            Integer newIndex = localVarIndexMap.get(oldIndex);
                            if (newIndex != null) {
                                frameNode.local.set(i, newIndex);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void modifyInstructionsWhenLocalVarsChangedSimplyAddIndex(MethodNode methodNode, int since, int add) {
        for (AbstractInsnNode insn : methodNode.instructions) {
            if (insn instanceof VarInsnNode varInsn) {
                if (varInsn.var >= since) {
                    varInsn.var += add;
                }
            } else if (insn instanceof IincInsnNode iincInsn) {
                if (iincInsn.var >= since) {
                    iincInsn.var += add;
                }
            } else if (insn instanceof FrameNode frameNode) {
                if (frameNode.local != null) {
                    for (int i = 0; i < frameNode.local.size(); i++) {
                        Object local = frameNode.local.get(i);
                        if (local instanceof Integer oldIndex) {
                            if (oldIndex >= since) {
                                frameNode.local.set(i, oldIndex + add);
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isModifyConstantValueMatched(final AnnotationNode modifyConstantAnnotationNode, final String valueType, final Object value) {
        try {
            AnnotationNode annotationNode_Constant = (AnnotationNode)((List<?>)modifyConstantAnnotationNode.values.get(modifyConstantAnnotationNode.values.indexOf(CONSTANT_LOWER) + 1)).get(0);
            return value.equals(annotationNode_Constant.values.get(annotationNode_Constant.values.indexOf(valueType) + 1));
        } catch (Throwable e) {
        }
        return false;
    }

    private static boolean isModifyVariableAnnotationMatchesOrdinal(final AnnotationNode modifyVariableAnnotationNode, final Integer ordinal) {
        try {
            return ordinal.equals(modifyVariableAnnotationNode.values.get(modifyVariableAnnotationNode.values.indexOf(ORDINAL) + 1));
        } catch (Throwable e) {
        }
        return false;
    }

    private static void removeFieldWithoutCheck(final ClassNode classNode, final String toRemove) {
        Iterator<FieldNode> iterator = classNode.fields.iterator();
        while (iterator.hasNext()) {
            FieldNode fieldNode = iterator.next();
            if (toRemove.equals(fieldNode.name)) {
                iterator.remove();
            }
        }
    }

    private static void setMethodToInterfaceDefaultWithoutCheck(final MethodNode methodNode) {
        methodNode.access = Opcodes.ACC_PUBLIC;
    }

    private static void setClassToInterfaceWithoutCheck(final ClassNode classNode) {
        classNode.access = Opcodes.ACC_PUBLIC | Opcodes.ACC_INTERFACE | Opcodes.ACC_ABSTRACT;
    }

    private static void redirectMixinTargetClassWithoutCheck(final AnnotationNode classMixinAnnotationNode, final String newTarget) {
        classMixinAnnotationNode.values.set(classMixinAnnotationNode.values.indexOf(VALUE) + 1, Lists.newArrayList(Type.getType(newTarget)));
    }

    private static void redirectMixinTargetMethodWithoutCheck(final AnnotationNode methodAnnotationNode, final String newTarget) {
        redirectMixinTargetMethodWithoutCheck(methodAnnotationNode, Lists.newArrayList(newTarget));
    }

    private static void redirectMixinTargetMethodWithoutCheck(final AnnotationNode methodAnnotationNode, final List<String> newTargets) {
        methodAnnotationNode.values.set(methodAnnotationNode.values.indexOf(METHOD) + 1, newTargets);
    }

    private static void modifyOrdinalWithoutCheck(final AnnotationNode ordinalableAnnotationNode, final Integer ordinal) {
        ordinalableAnnotationNode.values.set(ordinalableAnnotationNode.values.indexOf(ORDINAL) + 1, ordinal);
    }

    private static void redirectAtAnnotationToMethodInvokeWithoutCheck(final AnnotationNode atAnnotation, final String newTargetMethod) {
        atAnnotation.values = Lists.newArrayList(VALUE, INVOKE, TARGET, newTargetMethod);
    }

    private static void setMixinAnnotationIndexWithoutCheck(final AnnotationNode methodAnnotation, final Integer toAdd) {
        List<Object> newAnnotationValues = new ArrayList<>();
        for (int pos = 0; pos < methodAnnotation.values.size();) {
            Object object = methodAnnotation.values.get(pos);
            if (INDEX.equals(object)) {
                newAnnotationValues.add(object);
                newAnnotationValues.add(((Integer)methodAnnotation.values.get(pos + 1)) + toAdd);
                pos++;
                pos++;
            } else {
                newAnnotationValues.add(object);
                pos++;
            }
        }
        methodAnnotation.values = newAnnotationValues;
    }

    private static boolean mixinAnnotationHas(final AnnotationNode methodAnnotation, final String key, final boolean hasFollowingValue) {
        try {
            if (!methodAnnotation.values.contains(key)) {
                return false;
            }
            if (hasFollowingValue && ((methodAnnotation.values.indexOf(key) + 1) >= methodAnnotation.values.size())) {
                return false;
            }
            return true;
        } catch (Throwable e) {
        }
        return false;
    }

    // @ModifyExpressionValue(..., at = {@At(value = "CONSTANT", args = {"doubleValue=0.5D"}, ordinal = xxx)})
    private static AnnotationNode tryGetAtAnnotation_valueIsCONSTANT_MatchesConstant_FromArrayAtMixinAnnotation(final AnnotationNode mixinAnnotation, final String argsTypeValue) {
        try {
            AnnotationNode atAnnotation = tryGetUniqueAtAnnotation_FromArrayAtMixinAnnotation(mixinAnnotation);
            return tryGetAtAnnotationn_valueIsCONSTANT_MatchesConstant_NoException(atAnnotation, argsTypeValue);
        } catch (Throwable e) {
        }
        return null;
    }

    // @ModifyVariable(..., at = @At(...))
    private static AnnotationNode tryGetAtAnnotation_MatchesValueTargetOpcode_FromSingleAtMixinAnnotation(final AnnotationNode mixinAnnotation, final String value, final List<String> target, Integer opcode) {
        try {
            AnnotationNode atAnnotation = tryGetUniqueAtAnnotation_FromSingleAtMixinAnnotation(mixinAnnotation);
            return tryGetAtAnnotation_MatchesValueTargetOpcode_NoException(atAnnotation, value, target, opcode);
        } catch (Throwable e) {
        }
        return null;
    }

    // @ModifyVariable(..., at = @At(...))
    private static AnnotationNode tryGetAtAnnotation_MatchesValueTarget_FromSingleAtMixinAnnotation(final AnnotationNode mixinAnnotation, final String value, final String target) {
        try {
            AnnotationNode atAnnotation = tryGetUniqueAtAnnotation_FromSingleAtMixinAnnotation(mixinAnnotation);
            return tryGetAtAnnotation_MatchesValueTarget_NoException(atAnnotation, value, target);
        } catch (Throwable e) {
        }
        return null;
    }

    // @WrapOperation(..., at = {@At(...), @At(...), ...})
    private static AnnotationNode tryGetAtAnnotation_MatchesValueTarget_FromArrayAtMixinAnnotation(final AnnotationNode mixinAnnotation, final String value, final List<String> target) {
        try {
            AnnotationNode atAnnotation = tryGetUniqueAtAnnotation_FromArrayAtMixinAnnotation(mixinAnnotation);
            return tryGetAtAnnotation_MatchesValueTarget_NoException(atAnnotation, value, target);
        } catch (Throwable e) {
        }
        return null;
    }

    // @WrapOperation(..., at = {@At(...), @At(...), ...})
    private static AnnotationNode tryGetAtAnnotation_MatchesValueTargetOrdinal_FromSingleAtMixinAnnotation(final AnnotationNode mixinAnnotation, final String value, final List<String> target, final Integer ordinal) {
        try {
            AnnotationNode atAnnotation = tryGetUniqueAtAnnotation_FromSingleAtMixinAnnotation(mixinAnnotation);
            return tryGetAtAnnotation_MatchesValueTargetOrdinal_NoException(atAnnotation, value, target, ordinal);
        } catch (Throwable e) {
        }
        return null;
    }

    // @WrapOperation(..., at = {@At(...), @At(...), ...})
    private static AnnotationNode tryGetAtAnnotation_MatchesValueTarget_FromArrayAtMixinAnnotation(final AnnotationNode mixinAnnotation, final String value, final String target) {
        try {
            AnnotationNode atAnnotation = tryGetUniqueAtAnnotation_FromArrayAtMixinAnnotation(mixinAnnotation);
            return tryGetAtAnnotation_MatchesValueTarget_NoException(atAnnotation, value, target);
        } catch (Throwable e) {
        }
        return null;
    }

    // @WrapOperation(..., at = {@At(...), @At(...), ...})
    private static AnnotationNode tryGetAtAnnotation_MatchesValueTargetOpcode_FromArrayAtMixinAnnotation(final AnnotationNode mixinAnnotation, final String value, final List<String> target, final int opcode) {
        try {
            AnnotationNode atAnnotation = tryGetUniqueAtAnnotation_FromArrayAtMixinAnnotation(mixinAnnotation);
            return tryGetAtAnnotation_MatchesValueTargetOpcode_NoException(atAnnotation, value, target, opcode);
        } catch (Throwable e) {
        }
        return null;
    }

    // @Inject(..., at = {@At(...), @At(...), ...})
    private static AnnotationNode tryGetAtAnnotation_MatchesValueOrdinal_FromArrayAtMixinAnnotation(final AnnotationNode mixinAnnotation, final String value, final Integer ordinal) {
        try {
            AnnotationNode atAnnotation = tryGetUniqueAtAnnotation_FromArrayAtMixinAnnotation(mixinAnnotation);
            return tryGetAtAnnotation_MatchesValueOrdinal_NoException(atAnnotation, value, ordinal);
        } catch (Throwable e) {
        }
        return null;
    }

    private static AnnotationNode tryGetAtAnnotationn_valueIsCONSTANT_MatchesConstant_NoException(final AnnotationNode atAnnotation, final String argsTypeValue) {
        try {
            if (CONSTANT_UPPER.equals(atAnnotation.values.get(atAnnotation.values.indexOf(VALUE) + 1))) {
                if (atAnnotation.values.get(atAnnotation.values.indexOf(ARGS) + 1) instanceof List<?> list && list.size() == 1 && argsTypeValue.equals(list.get(0))) {
                    return atAnnotation;
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static AnnotationNode tryGetAtAnnotation_MatchesValueOrdinal_NoException(final AnnotationNode atAnnotation, final String value, final Integer ordinal) {
        try {
            if (value.equals(atAnnotation.values.get(atAnnotation.values.indexOf(VALUE) + 1)) && ordinal.equals(atAnnotation.values.get(atAnnotation.values.indexOf(ORDINAL) + 1))) {
                return atAnnotation;
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static AnnotationNode tryGetAtAnnotation_MatchesValueTarget_NoException(final AnnotationNode atAnnotation, final String value, final String target) {
        try {
            if (value.equals(atAnnotation.values.get(atAnnotation.values.indexOf(VALUE) + 1)) && target.equals(atAnnotation.values.get(atAnnotation.values.indexOf(TARGET) + 1))) {
                return atAnnotation;
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static AnnotationNode tryGetAtAnnotation_MatchesValueTargetOrdinal_NoException(final AnnotationNode atAnnotation, final String value, final List<String> targetNames, final Integer ordinal) {
        for (String target : targetNames) {
            try {
                if (value.equals(atAnnotation.values.get(atAnnotation.values.indexOf(VALUE) + 1)) && target.equals(atAnnotation.values.get(atAnnotation.values.indexOf(TARGET) + 1)) && ordinal.equals(atAnnotation.values.get(atAnnotation.values.indexOf(ORDINAL) + 1))) {
                    return atAnnotation;
                }
            } catch (Throwable e) {
            }
        }
        return null;
    }

    private static AnnotationNode tryGetAtAnnotation_MatchesValueTarget_NoException(final AnnotationNode atAnnotation, final String value, final List<String> targetNames) {
        for (String target : targetNames) {
            try {
                if (value.equals(atAnnotation.values.get(atAnnotation.values.indexOf(VALUE) + 1)) && target.equals(atAnnotation.values.get(atAnnotation.values.indexOf(TARGET) + 1))) {
                    return atAnnotation;
                }
            } catch (Throwable e) {
            }
        }
        return null;
    }

    private static AnnotationNode tryGetAtAnnotation_MatchesValueTargetOpcode_NoException(final AnnotationNode atAnnotation, final String value, final List<String> targetNames, final Integer opcode) {
        for (String target : targetNames) {
            try {
                if (value.equals(atAnnotation.values.get(atAnnotation.values.indexOf(VALUE) + 1)) && target.equals(atAnnotation.values.get(atAnnotation.values.indexOf(TARGET) + 1)) && opcode.equals(atAnnotation.values.get(atAnnotation.values.indexOf(OPCODE) + 1))) {
                    return atAnnotation;
                }
            } catch (Throwable e) {
            }
        }
        return null;
    }

    private static AnnotationNode tryGetUniqueAtAnnotation_FromArrayAtMixinAnnotation(final AnnotationNode mixinAnnotation) {
        try {
            ArrayList<?> atPositions = (ArrayList<?>) mixinAnnotation.values.get(mixinAnnotation.values.indexOf(AT) + 1);
            if (atPositions.size() == 1) {
                return (AnnotationNode) atPositions.get(0);
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static AnnotationNode tryGetUniqueAtAnnotation_FromSingleAtMixinAnnotation(final AnnotationNode mixinAnnotation) {
        try {
            AnnotationNode atAnnotation = (AnnotationNode) mixinAnnotation.values.get(mixinAnnotation.values.indexOf(AT) + 1);
            return atAnnotation;
        } catch (Throwable e) {
        }
        return null;
    }

    private static boolean isTargetMethodMatched(final AnnotationNode mixinAnnotation, final List<Entry<String, String>> targetWithDifferentMappings) {
        try {
            String targetMethodNameFromAsm = (String) ((ArrayList<?>) mixinAnnotation.values.get(mixinAnnotation.values.indexOf(METHOD) + 1)).get(0);
            for (Entry<String, String> name : targetWithDifferentMappings) {
                if (name.getKey().equals(targetMethodNameFromAsm) || (name.getKey() + name.getValue()).equals(targetMethodNameFromAsm)) {
                    return true;
                }
            }
        } catch (Throwable e) {
        }
        return false;
    }

    private static boolean isMixinTargetClassMatched(final AnnotationNode mixinAnnotation, final String cls) {
        try {
            return ((Type) ((ArrayList<?>) mixinAnnotation.values.get(mixinAnnotation.values.indexOf(VALUE) + 1)).get(0)).getDescriptor().equals(cls);
        } catch (Throwable e) {
            return false;
        }
    }

    private static void change_Operation$Double_to_double(final MethodNode methodNode) {
        methodNode.desc = methodNode.desc.replace("(" + c_Operation_desc, "(" + "D");
        methodNode.signature = null;

        methodNode.localVariables.get(1).desc = "D";
        methodNode.localVariables.get(1).signature = null;

        // find instructions to modify
        // aload_1 -> dload_1
        // iconst_0 -> remove
        // anewarray <java/lang/Object> -> remove
        // invokeinterface <com/llamalad7/mixinextras/injector/wrapoperation/Operation.call : ([Ljava/lang/Object;)Ljava/lang/Object;> count 2 -> remove
        // checkcast <java/lang/Double> -> remove
        // invokevirtual <java/lang/Double.doubleValue : ()D> -> remove
        List<VarInsnNode> toChangeLoadOpcode = new ArrayList<>();
        List<AbstractInsnNode> toRemove = new ArrayList<>();
        boolean shouldRemove = false;
        for (AbstractInsnNode instruction : methodNode.instructions) {
            if (instruction instanceof VarInsnNode varInsnNode && varInsnNode.var == 1) {
                // ALOAD 1 -> Operation<xxx>
                toChangeLoadOpcode.add(varInsnNode);
                shouldRemove = true;
            } else if (instruction instanceof MethodInsnNode methodInsnNode && methodInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL && c_Double_name.equals(methodInsnNode.owner) && m_Double_doubleValue_name.equals(methodInsnNode.name) && m_Double_doubleValue_desc.equals(methodInsnNode.desc)) {
                // invokevirtual java/lang/Double.doubleValue : ()D
                toRemove.add(instruction);
                shouldRemove = false;
            } else if (shouldRemove) {
                toRemove.add(instruction);
            }
        }

        // ALOAD 1 -> DLOAD 1
        for (VarInsnNode insn : toChangeLoadOpcode) {
            insn.setOpcode(Opcodes.DLOAD);
        }

        // remove
        for (AbstractInsnNode insn : toRemove) {
            methodNode.instructions.remove(insn);
        }

        // Operation<Double> -> double, so slot indexes ++
        for (int i = 2; i < methodNode.localVariables.size(); i++) {
            methodNode.localVariables.get(i).index++;
        }

        modifyInstructionsWhenLocalVarsChangedSimplyAddIndex(methodNode, 2, 1);
    }

    private static void fix_apoli_ServerPlayerInteractionManagerMixin_actionOnBlockBreak(MethodNode methodNode) {
        methodNode.desc = "(Lnet/minecraft/core/BlockPos;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;Lnet/minecraft/world/level/block/state/BlockState;ILnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;ZZ)V";
        methodNode.signature = "(Lnet/minecraft/core/BlockPos;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable<Ljava/lang/Boolean;>;Lnet/minecraft/world/level/block/state/BlockState;ILnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;ZZ)V";

        // we add a new parameter int_catserver$exp and change the order of bl and bl2
        ParameterNode parameterNode_pos = methodNode.parameters.get(0);
        ParameterNode parameterNode_cir = methodNode.parameters.get(1);
        ParameterNode parameterNode_blockState = methodNode.parameters.get(2);
        ParameterNode parameterNode_blockEntity = methodNode.parameters.get(3);
        ParameterNode parameterNode_block = methodNode.parameters.get(4);
        ParameterNode parameterNode_bl = methodNode.parameters.get(5);
        ParameterNode parameterNode_itemStack = methodNode.parameters.get(6);
        ParameterNode parameterNode_itemStack2 = methodNode.parameters.get(7);
        ParameterNode parameterNode_bl2 = methodNode.parameters.get(8);

        ArrayList<ParameterNode> tempParameterNodes = new ArrayList<>(10);
        tempParameterNodes.add(parameterNode_pos);
        tempParameterNodes.add(parameterNode_cir);
        tempParameterNodes.add(parameterNode_blockState);
        tempParameterNodes.add(new ParameterNode("int_catserver$exp", 0));
        tempParameterNodes.add(parameterNode_blockEntity);
        tempParameterNodes.add(parameterNode_block);
        tempParameterNodes.add(parameterNode_itemStack);
        tempParameterNodes.add(parameterNode_itemStack2);
        tempParameterNodes.add(parameterNode_bl);
        tempParameterNodes.add(parameterNode_bl2);
        methodNode.parameters = tempParameterNodes; // replace at last, then if something works wrong, the old parameters can be remained

        LocalVariableNode localVariableNode_this = methodNode.localVariables.get(0);
        LocalVariableNode localVariableNode_pos = methodNode.localVariables.get(1);
        LocalVariableNode localVariableNode_cir = methodNode.localVariables.get(2);
        LocalVariableNode localVariableNode_blockState = methodNode.localVariables.get(3);
        LocalVariableNode localVariableNode_blockEntity = methodNode.localVariables.get(4);
        LocalVariableNode localVariableNode_block = methodNode.localVariables.get(5);
        LocalVariableNode localVariableNode_bl = methodNode.localVariables.get(6);
        LocalVariableNode localVariableNode_itemStack = methodNode.localVariables.get(7);
        LocalVariableNode localVariableNode_itemStack2 = methodNode.localVariables.get(8);
        LocalVariableNode localVariableNode_bl2 = methodNode.localVariables.get(9);

        ArrayList<LocalVariableNode> tempLocalVariableNodes = new ArrayList<>(10);
        tempLocalVariableNodes.add(localVariableNode_this);
        tempLocalVariableNodes.add(localVariableNode_pos);
        tempLocalVariableNodes.add(localVariableNode_cir);
        tempLocalVariableNodes.add(localVariableNode_blockState);
        tempLocalVariableNodes.add(new LocalVariableNode("int_catserver$exp", "I", null, (LabelNode)methodNode.instructions.getFirst(), (LabelNode)methodNode.instructions.getLast(), 4));
        tempLocalVariableNodes.add(new LocalVariableNode(localVariableNode_blockEntity.name, localVariableNode_blockEntity.desc, localVariableNode_blockEntity.signature, localVariableNode_blockEntity.start, localVariableNode_blockEntity.end, 5));
        tempLocalVariableNodes.add(new LocalVariableNode(localVariableNode_block.name, localVariableNode_block.desc, localVariableNode_block.signature, localVariableNode_block.start, localVariableNode_block.end, 6));
        tempLocalVariableNodes.add(new LocalVariableNode(localVariableNode_itemStack.name, localVariableNode_itemStack.desc, localVariableNode_itemStack.signature, localVariableNode_itemStack.start, localVariableNode_itemStack.end, 7));
        tempLocalVariableNodes.add(new LocalVariableNode(localVariableNode_itemStack2.name, localVariableNode_itemStack2.desc, localVariableNode_itemStack2.signature, localVariableNode_itemStack2.start, localVariableNode_itemStack2.end, 8));
        tempLocalVariableNodes.add(new LocalVariableNode(localVariableNode_bl.name, localVariableNode_bl.desc, localVariableNode_bl.signature, localVariableNode_bl.start, localVariableNode_bl.end, 9));
        tempLocalVariableNodes.add(new LocalVariableNode(localVariableNode_bl2.name, localVariableNode_bl2.desc, localVariableNode_bl2.signature, localVariableNode_bl2.start, localVariableNode_bl2.end, 10));

        methodNode.localVariables = tempLocalVariableNodes;

        Map<Integer, Integer> localVarIndexMap = new HashMap<>();
        localVarIndexMap.put(0, 0);
        localVarIndexMap.put(1, 1);
        localVarIndexMap.put(2, 2);
        localVarIndexMap.put(3, 3);
        localVarIndexMap.put(4, 5);
        localVarIndexMap.put(5, 6);
        localVarIndexMap.put(6, 9);
        localVarIndexMap.put(7, 7);
        localVarIndexMap.put(8, 8);
        localVarIndexMap.put(9, 10);
        modifyInstructionsWhenLocalVarsChanged(methodNode, localVarIndexMap);
    }

    private static void fix_reach_entity_attributes_ServerPlayerInteractionManagerMixin_getActualReachDistance(MethodNode methodNode) {
        methodNode.desc = "(D)D";
        methodNode.signature = "(D)D";

        ArrayList<ParameterNode> tempParameterNodes = new ArrayList<>();
        tempParameterNodes.add(new ParameterNode("double_catserver$forgeValue", 0));
        methodNode.parameters = tempParameterNodes; // replace at last, then if something works wrong, the old parameters can be remained

        methodNode.localVariables.add(1, new LocalVariableNode("double_catserver$forgeValue", "D", null, (LabelNode)methodNode.instructions.getFirst(), (LabelNode)methodNode.instructions.getLast(), 1));
    }

    private static void fix_reach_entity_attributes_InventoryValidationMixin_checkWithinActualReach(MethodNode methodNode) {
        methodNode.desc = methodNode.desc.replace("(" + c_BlockEntity_desc + c_Player_desc + "I", "(" + c_BlockEntity_desc + c_Player_desc + "D");
        if (methodNode.signature != null) {
            methodNode.signature = methodNode.signature.replace("(" + c_BlockEntity_desc + c_Player_desc + "I", "(" + c_BlockEntity_desc + c_Player_desc + "D");
        }

        // I -> D
        // uses 1 more slot
        methodNode.maxLocals++;
        methodNode.localVariables.get(2).desc = "D";
        for (int i = 3; i < methodNode.localVariables.size(); i++) {
            methodNode.localVariables.get(i).index++;
        }

        // fix insns
        // IMUL -> DMUL
        // IMUL -> DMUL
        // remove I2D
        List<VarInsnNode> varInsnNodesToChangeI2D = new ArrayList<>();
        List<AbstractInsnNode> insnNodesToReplace = new ArrayList<>();
        List<AbstractInsnNode> insnNodesToRemove = new ArrayList<>();
        for (AbstractInsnNode instruction : methodNode.instructions) {
            if (instruction instanceof VarInsnNode varInsnNode) {
                if (varInsnNode.getOpcode() == Opcodes.ILOAD && varInsnNode.var == 2) {
                    varInsnNodesToChangeI2D.add(varInsnNode);
                }
            } else if (instruction instanceof InsnNode insnNode) {
                if (insnNode.getOpcode() == Opcodes.IMUL && insnNode.getPrevious() instanceof VarInsnNode varInsnNode && varInsnNode.getOpcode() == Opcodes.ILOAD && varInsnNode.var == 2) {
                    insnNodesToReplace.add(insnNode);
                } else if (instruction.getOpcode() == Opcodes.I2D && instruction.getPrevious() instanceof InsnNode previousInsnNode && previousInsnNode.getOpcode() == Opcodes.IMUL) {
                    insnNodesToRemove.add(instruction);
                }
            }
        }
        // modify
        for (VarInsnNode toChangeI2D : varInsnNodesToChangeI2D) {
            toChangeI2D.setOpcode(Opcodes.DLOAD);
        }
        for (AbstractInsnNode toReplace : insnNodesToReplace) {
            AbstractInsnNode previous = toReplace.getPrevious();
            methodNode.instructions.remove(toReplace);
            methodNode.instructions.insert(previous, new InsnNode(Opcodes.DMUL));
        }
        for (AbstractInsnNode toRemove : insnNodesToRemove) {
            methodNode.instructions.remove(toRemove);
        }

        // local vars after D: index++
        modifyInstructionsWhenLocalVarsChangedSimplyAddIndex(methodNode, 3, 1);
    }

    private static void fix_reach_entity_attributes_ScreenHandlerMixin_getActualReachDistance__and__pehkui_ScreenHandlerMixin_pehkui$canUse$distance(MethodNode methodNode) {
        // 0 D
        // 1 .
        // 2 Player
        // 3 Block
        // below is same as fix_pehkui_ScreenHandlerMixin_pehkui$canUse$xyzOffset
        methodNode.desc = methodNode.desc.replace("(D" + c_Block_desc + c_Player_desc, "(D" + c_Player_desc + c_Block_desc);
        if (methodNode.signature != null) {
            methodNode.signature = methodNode.signature.replace("(D" + c_Block_desc + c_Player_desc, "(D" + c_Player_desc + c_Block_desc);
        }

        // exchange para 1 and 2
        methodNode.parameters.add(1, methodNode.parameters.remove(2));

        // exchange local var 1 and 2
        methodNode.localVariables.add(1, methodNode.localVariables.remove(2));
        methodNode.localVariables.get(1).index -= 1;
        methodNode.localVariables.get(2).index += 1;

        Map<Integer, Integer> localVarIndexMap = new HashMap<>();
        localVarIndexMap.put(2, 3);
        localVarIndexMap.put(3, 2);
        modifyInstructionsWhenLocalVarsChanged(methodNode, localVarIndexMap);
    }

    private static void fix_pehkui_ScreenHandlerMixin_pehkui$canUse$xyzOffset(MethodNode methodNode) {
        // 0 D
        // 1 .
        // 2 Player
        // 3 Block
        methodNode.desc = methodNode.desc.replace("(D" + c_Block_desc + c_Player_desc, "(D" + c_Player_desc + c_Block_desc);
        if (methodNode.signature != null) {
            methodNode.signature = methodNode.signature.replace("(D" + c_Block_desc + c_Player_desc, "(D" + c_Player_desc + c_Block_desc);
        }

        // exchange para 0 and 1
        methodNode.parameters.add(1, methodNode.parameters.remove(2));

        // exchange local var 0 and 1
        methodNode.localVariables.add(1, methodNode.localVariables.remove(2));
        methodNode.localVariables.get(1).index -= 1;
        methodNode.localVariables.get(2).index += 1;

        Map<Integer, Integer> localVarIndexMap = new HashMap<>();
        localVarIndexMap.put(2, 3);
        localVarIndexMap.put(3, 2);
        modifyInstructionsWhenLocalVarsChanged(methodNode, localVarIndexMap);
    }

    private static void fix_pehkui_PlayerEntityMixin_pehkui$attack$expand(MethodNode methodNode) {
        // ret AABB -> ret Object
        methodNode.name = m_Player_catserver$fabricModRedirectTarget_IForgeItem$getSweepHitBox_AABB$inflate_name;
        methodNode.desc = m_Player_catserver$fabricModRedirectTarget_IForgeItem$getSweepHitBox_AABB$inflate_desc;
        methodNode.signature = null;
        // private -> public
        methodNode.access = Opcodes.ACC_PUBLIC;
        // remove @Local(argsOnly = true)
        methodNode.invisibleParameterAnnotations = null;
        // look for insns to remove
        List<AbstractInsnNode> toRemove = new ArrayList<>();
        for (AbstractInsnNode instruction : methodNode.instructions) {
            if (instruction instanceof VarInsnNode varInsnNode && varInsnNode.var == 8) {
                // original
                toRemove.add(instruction);
            } else if (instruction instanceof MethodInsnNode methodInsnNode && methodInsnNode.getOpcode() == Opcodes.INVOKEINTERFACE && c_Operation_name.equals(methodInsnNode.owner)) {
                // original.call
                toRemove.add(instruction);
            } else if (instruction instanceof TypeInsnNode typeInsnNode && typeInsnNode.getOpcode() == Opcodes.CHECKCAST && c_AABB_name.equals(typeInsnNode.desc)) {
                // (AABB)
                toRemove.add(instruction);
            }
        }
        // remove insns
        for (AbstractInsnNode insnNode : toRemove) {
            methodNode.instructions.remove(insnNode);
        }
    }

    private static void fix_apoli_HungerManagerMixin_executeAdditionalEatAction(MethodNode methodNode) {
        // private void executeAdditionalEatAction(Item item, ItemStack stack, CallbackInfo ci) -> private void executeAdditionalEatAction(Item item, ItemStack stack, LivingEntity forge$livingEntity, CallbackInfo ci)
        methodNode.desc = methodNode.desc.replace("(" + c_Item_desc + c_ItemStack_desc, "(" + c_Item_desc + c_ItemStack_desc + c_LivingEntity_desc);
        if (methodNode.signature != null) {
            methodNode.signature = methodNode.signature.replace("(" + c_Item_desc + c_ItemStack_desc, "(" + c_Item_desc + c_ItemStack_desc + c_LivingEntity_desc);
        }

        // add para
        methodNode.parameters.add(2, new ParameterNode("forge$livingEntity", 0));

        // add local var
        // 0 this
        // 1 Item
        // 2 ItemStack
        // 3 LivingEntity <- inserted
        // 4 CallbackInfo
        methodNode.localVariables.add(3, new LocalVariableNode("forge$livingEntity", c_LivingEntity_desc, null, (LabelNode)methodNode.instructions.getFirst(), (LabelNode)methodNode.instructions.getLast(), 3));
        for (int i = 4; i < methodNode.localVariables.size(); i++) {
            methodNode.localVariables.get(i).index += 1;
        }

        modifyInstructionsWhenLocalVarsChangedSimplyAddIndex(methodNode, 3, 1);
    }
}
