package catserver.server.debug;

import net.minecraftforge.fml.loading.FMLPaths;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

public class CatServerDebugUtil {
    public static void dumpClassNode(ClassNode classNode) {
        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        byte[] bytes = classWriter.toByteArray();
        Path folderPath = FMLPaths.GAMEDIR.get().resolve("catserver_debug_dump");
        folderPath.toFile().mkdirs();
        File dumpFile = folderPath.resolve(classNode.name.replace("/", ".") + "-" + Objects.hashCode(bytes) + ".class").toFile();
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(dumpFile))) {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
