package catserver.server.patcher.plugins;

import catserver.server.BukkitInjector;
import catserver.server.CatServer;
import catserver.server.patcher.IPatcher;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.NamespacedKey;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.*;
import java.util.stream.Collectors;

public class UltimateTimberPatcher implements IPatcher {
    private static final int ELEMENT_COUNT_PER_SUB_METHOD = 2000;
    private static final String CLASS_XMaterial_NAME = "com.craftaro.third_party.com.cryptomorin.xseries.XMaterial";
    private static final String CLASS_XMaterial_INTERNAL_NAME = CLASS_XMaterial_NAME.replace('.', '/');
    private static final String CLASS_XMaterial_DESC = "L" + CLASS_XMaterial_INTERNAL_NAME + ";";
    private static final String CLASS_XMaterial_ARRAY_DESC = "[" + CLASS_XMaterial_DESC;

    @Override
    public byte[] transform(String className, byte[] basicClass) {
        // loaded by JavaPluginLoader#urlClassLoader
        if (className.equals(CLASS_XMaterial_NAME)) {
            return patchXMaterial(basicClass);
        }
        return basicClass;
    }

    private byte[] patchXMaterial(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        boolean success = true;
        boolean found$values = false;
        boolean found_clinit_ = false;

        // find names to add
        List<String> exist = this.getAllEnumFields(node.fields);
        int newEnumValuesStartId = this.getNewEnumValuesStartId(node.fields);
        success &= (newEnumValuesStartId != -1);
        Map.Entry<List<String>, Map<String, Integer>> newEnumsToAdd = this.findAllToAdd(exist, newEnumValuesStartId);

        // create fields
        success &= this.addEnumFields(node.fields, newEnumsToAdd.getKey(), newEnumValuesStartId);

        // modify methods
        // if mods added too many items and blocks, $values and <clinit> will be larger than the limit(65535), so we split them to sub methods.
        List<MethodNode> methodsToAdd = Lists.newArrayList();
        for (MethodNode method : node.methods) {
            if (method.name.equals("$values")) {
                success &= this.modify$values(method.instructions, newEnumsToAdd.getKey(), newEnumValuesStartId, methodsToAdd);
                found$values = true;
            } else if (method.name.equals("<clinit>")) {
                success &= this.modify_clinit_(method.instructions, newEnumsToAdd, methodsToAdd);
                found_clinit_ = true;
            }
        }
        node.methods.addAll(methodsToAdd);
        success &= found$values;
        success &= found_clinit_;

        // write back
        if (success) {
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            node.accept(writer);
            return writer.toByteArray();
        } else {
            return basicClass;
        }
    }

    private List<String> getAllEnumFields(List<FieldNode> fieldNodes) {
        List<String> ret = Lists.newArrayList();
        for (FieldNode fieldNode : fieldNodes) {
            if ((fieldNode.access & Opcodes.ACC_ENUM) != 0) {
                ret.add(fieldNode.name);
            }
        }
        return ret;
    }

    private int getNewEnumValuesStartId(List<FieldNode> fieldNodes) {
        // find the last enum value field, and add new enum fields after it.
        for (int counter = fieldNodes.size() - 1; counter >= 0; counter--) {
            if ((fieldNodes.get(counter).access & Opcodes.ACC_ENUM) != 0) {
                return counter + 1;
            }
        }

        this.fail("Failed to get enum values start id.");
        return -1;
    }

    private Map.Entry<List<String>, Map<String, Integer>> findAllToAdd(List<String> exist, int startId) {
        Set<String> set = Sets.newHashSet();
        for (var entry : ForgeRegistries.BLOCKS.getEntries()) {
            ResourceLocation location = entry.getKey().location();
            // Skip minecraft
            if (Objects.equals(location.getNamespace(), NamespacedKey.MINECRAFT)) {
                continue;
            }
            String blockName = BukkitInjector.standardize(location);
            if (!exist.contains(blockName)) {
                set.add(blockName);
            }
        }

        for (var entry : ForgeRegistries.ITEMS.getEntries()) {
            ResourceLocation location = entry.getKey().location();
            // Skip minecraft
            if (Objects.equals(location.getNamespace(), NamespacedKey.MINECRAFT)) {
                continue;
            }
            String itemName = BukkitInjector.standardize(location);
            if (!exist.contains(itemName)) {
                set.add(itemName);
            }
        }

        List<String> retSortedList = set.stream().sorted().collect(Collectors.toList());
        Map<String, Integer> retMap = Maps.newHashMap();
        int id = startId;
        for (String name : retSortedList) {
            retMap.put(name, id);
            id++;
        }
        return new AbstractMap.SimpleEntry<>(retSortedList, retMap);
    }

    /**
     * @return true -> success, false -> fail
     */
    private boolean addEnumFields(List<FieldNode> fieldNodes, List<String> enumNamesAndIdsToAdd, int newEnumValuesStartId) {
        List<FieldNode> newFields = Lists.newArrayList();
        for (String newFieldName : enumNamesAndIdsToAdd) {
            newFields.add(new FieldNode(
                    Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL | Opcodes.ACC_ENUM,
                    newFieldName,
                    CLASS_XMaterial_DESC,
                    null,
                    null
            ));
        }
        fieldNodes.addAll(newEnumValuesStartId, newFields);
        return true;
    }

    /**
     * @return true -> success, false -> fail
     */
    // Enum#$values()
    private boolean modify$values(InsnList instructions, List<String> enumNamesToAdd, int startId, List<MethodNode> methodsToAdd) {
        // L0
        // LINENUMBER 68 L0
        // SIPUSH 1463
        // ANEWARRAY com/craftaro/third_party/com/cryptomorin/xseries/XMaterial
        // ---------------------------------------------------------------------------------------
        // DUP
        // ICONST_0
        // GETSTATIC com/craftaro/third_party/com/cryptomorin/xseries/XMaterial.ACACIA_BOAT : Lcom/craftaro/third_party/com/cryptomorin/xseries/XMaterial;
        // AASTORE
        // ...
        // DUP
        // SIPUSH {ID}
        // GETSTATIC com/craftaro/third_party/com/cryptomorin/xseries/XMaterial.{NAME} : Lcom/craftaro/third_party/com/cryptomorin/xseries/XMaterial;
        // AASTORE
        // ---------------------------------------------------------------------------------------
        // ARETURN

        // modify array length
        // find first SIPUSH
        // new XMaterial[1463] -> new XMaterial[1463 + enumNamesToAdd.size()]
        for (AbstractInsnNode insnNode : instructions) {
            if (insnNode.getOpcode() == Opcodes.SIPUSH) {
                ((IntInsnNode) insnNode).operand += enumNamesToAdd.size();
                break;
            }
        }

        // put our new enums into the array
        List<InsnList> insnListsForSubMethods = Lists.newArrayList();
        InsnList insnsOfSubMethod = null;
        AbstractInsnNode last = instructions.getLast();
        if (last.getOpcode() == Opcodes.ARETURN) {
            // create instructions for each enum value
            for (int counter = 0; counter < enumNamesToAdd.size(); counter++) {
                if (counter % ELEMENT_COUNT_PER_SUB_METHOD == 0) {
                    insnsOfSubMethod = new InsnList();
                    insnListsForSubMethods.add(insnsOfSubMethod);
                }
                this.addEnumValueTo$values(insnsOfSubMethod, enumNamesToAdd.get(counter), startId + counter);
            }

            // build sub methods and call them
            InsnList newInsnsCallingSubMethods = this.buildSubMethodsFor$valuesAndReturnCalling(insnListsForSubMethods, methodsToAdd);
            instructions.insertBefore(last, newInsnsCallingSubMethods);
            return true;
        }

        this.fail("Inject point in $values not found.");
        return false;
    }

    // <clinit>
    private boolean modify_clinit_(InsnList instructions, Map.Entry<List<String>, Map<String, Integer>> newEnumsToAdd, List<MethodNode> methodsToAdd) {
        Map<String, Integer> name2Id = newEnumsToAdd.getValue();
        List<InsnList> insnListsForSubMethods = Lists.newArrayList();
        InsnList insnsOfSubMethod = null;
        // build insns
        List<String> newEnumNames = newEnumsToAdd.getKey();
        for (int counter = 0; counter < newEnumNames.size(); counter++) {
            String enumName = newEnumNames.get(counter);
            if (counter % ELEMENT_COUNT_PER_SUB_METHOD == 0) {
                insnsOfSubMethod = new InsnList();
                insnListsForSubMethods.add(insnsOfSubMethod);
            }
            this.addEnumValueTo_clinit_(insnsOfSubMethod, enumName, name2Id.get(enumName));
        }

        // find last XMaterial#<init>, and inject after the following PUTSTATIC
        for (int index = instructions.size() - 1; index > 0; index--) {
            AbstractInsnNode insn = instructions.get(index);
            if (insn instanceof MethodInsnNode) {
                MethodInsnNode min = (MethodInsnNode) insn;
                if (Opcodes.INVOKESPECIAL == min.getOpcode() && "<init>".equals(min.name) && CLASS_XMaterial_INTERNAL_NAME.equals(min.owner)) {
                    AbstractInsnNode nextNode = min.getNext();
                    if (nextNode instanceof FieldInsnNode) {
                        FieldInsnNode fin = (FieldInsnNode) nextNode;
                        if (Opcodes.PUTSTATIC == fin.getOpcode() && CLASS_XMaterial_INTERNAL_NAME.equals(fin.owner) && CLASS_XMaterial_DESC.equals(fin.desc)) {
                            // build sub methods and call them
                            InsnList newInsnsCallingSubMethods = this.buildSubMethodsFor_clinit_AndReturnCalling(insnListsForSubMethods, methodsToAdd);
                            instructions.insert(fin, newInsnsCallingSubMethods);
                            return true;
                        }
                    }
                }
            }
        }

        this.fail("Inject point in <clinit> not found.");
        return false;
    }

    private InsnList buildSubMethodsFor$valuesAndReturnCalling(List<InsnList> insnListsForSubMethods, List<MethodNode> methodsToAdd) {
        InsnList ret = new InsnList();

        for (int methodIndex = 0; methodIndex < insnListsForSubMethods.size(); methodIndex++) {
            // add ALOAD 0 and RETURN to the sub method
            InsnList insnListForSubMethod = insnListsForSubMethods.get(methodIndex);
            LabelNode label_methodHead = new LabelNode();
            insnListForSubMethod.insert(label_methodHead);
            insnListForSubMethod.insert(new VarInsnNode(Opcodes.ALOAD, 0));
            insnListForSubMethod.add(new InsnNode(Opcodes.POP));
            insnListForSubMethod.add(new InsnNode(Opcodes.RETURN));
            LabelNode label_methodTail = new LabelNode();
            insnListForSubMethod.add(label_methodTail);

            // build the sub method
            String newMethodName = "$values$catserver$part_" + methodIndex;
            String newMethodDesc = "(" + CLASS_XMaterial_ARRAY_DESC + ")V";
            MethodNode newMethod = new MethodNode(
                    Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC,
                    newMethodName,
                    newMethodDesc,
                    null,
                    null
            );
            newMethod.instructions = insnListForSubMethod;
            newMethod.localVariables = Lists.newArrayList(new LocalVariableNode("arr", CLASS_XMaterial_ARRAY_DESC, null, label_methodHead, label_methodTail, 0));
            newMethod.parameters = Lists.newArrayList(new ParameterNode("arr", 0));
            newMethod.maxLocals = 1;
            methodsToAdd.add(newMethod);

            // call the sub method
            ret.add(new InsnNode(Opcodes.DUP));
            ret.add(new MethodInsnNode(Opcodes.INVOKESTATIC, CLASS_XMaterial_INTERNAL_NAME, newMethodName, newMethodDesc));
        }

        return ret;
    }

    private InsnList buildSubMethodsFor_clinit_AndReturnCalling(List<InsnList> insnListsForSubMethods, List<MethodNode> methodsToAdd) {
        InsnList ret = new InsnList();

        for (int methodIndex = 0; methodIndex < insnListsForSubMethods.size(); methodIndex++) {
            // add ALOAD 0 and RETURN to the sub method
            InsnList insnListForSubMethod = insnListsForSubMethods.get(methodIndex);
            insnListForSubMethod.add(new InsnNode(Opcodes.RETURN));

            // build the sub method
            String newMethodName = "_clinit_$catserver$part_" + methodIndex;
            String newMethodDesc = "()V";
            MethodNode newMethod = new MethodNode(
                    Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC,
                    newMethodName,
                    newMethodDesc,
                    null,
                    null
            );
            newMethod.instructions = insnListForSubMethod;
            newMethod.localVariables = Lists.newArrayList();
            newMethod.parameters = Lists.newArrayList();
            newMethod.maxLocals = 0;
            methodsToAdd.add(newMethod);

            // call the sub method
            ret.add(new MethodInsnNode(Opcodes.INVOKESTATIC, CLASS_XMaterial_INTERNAL_NAME, newMethodName, newMethodDesc));
        }

        return ret;
    }

    private void addEnumValueTo$values(InsnList insnList, String enumName, int enumId) {
        insnList.add(new InsnNode(Opcodes.DUP));
        insnList.add(new IntInsnNode(Opcodes.SIPUSH, enumId));
        insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, CLASS_XMaterial_INTERNAL_NAME, enumName, CLASS_XMaterial_DESC));
        insnList.add(new InsnNode(Opcodes.AASTORE));
    }

    private void addEnumValueTo_clinit_(InsnList insnList, String enumName, int id) {
        insnList.add(new TypeInsnNode(Opcodes.NEW, CLASS_XMaterial_INTERNAL_NAME));
        insnList.add(new InsnNode(Opcodes.DUP));
        insnList.add(new LdcInsnNode(enumName));
        insnList.add(new IntInsnNode(Opcodes.SIPUSH, id));
        insnList.add(new InsnNode(Opcodes.ICONST_0));
        insnList.add(new TypeInsnNode(Opcodes.ANEWARRAY, "java/lang/String"));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, CLASS_XMaterial_INTERNAL_NAME, "<init>", "(Ljava/lang/String;I[Ljava/lang/String;)V", false));
        insnList.add(new FieldInsnNode(Opcodes.PUTSTATIC, CLASS_XMaterial_INTERNAL_NAME, enumName, CLASS_XMaterial_DESC));
    }

    private void fail(String reason) {
        CatServer.LOGGER.warn("Failed to add enum fields to com.craftaro.third_party.com.cryptomorin.xseries.XMaterial: " + reason);
    }
}
