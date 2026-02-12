package catserver.server.fabric_loading;

public enum CatServerSharedConstants {
    MC("minecraft"),
    MC_VERSION("1.20.1"),
    ;
    private final String s;
    private CatServerSharedConstants(String s) {
        this.s = s;
    }

    public String get() {
        return s;
    }
}
