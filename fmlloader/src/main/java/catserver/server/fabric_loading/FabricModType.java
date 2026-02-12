package catserver.server.fabric_loading;

public enum FabricModType {
    JAVA     ("Java          ", false),
    MINECRAFT("Minecraft     ", false),
    FABRIC   ("Fabric Mod    ", false),
    FORGE    ("Forge Mod     ", true),
    FML      ("FML Dependency", true),
    GAME_LIB ("Forge Mod Lib ", true),
    UNKNOWN  ("UNKNOWN       ", true),
    ;

    public final String typeName;
    public final boolean isFromForge;

    private FabricModType(String typeName, boolean isFromForge) {
        this.typeName = typeName;
        this.isFromForge = isFromForge;
    }
}
