package Scene.lighting.shaders;

public abstract class Shader {
    public static Types get(String shading) {
        return switch(shading.toUpperCase()) {
            case ("FLAT") -> Types.FLAT;
            case ("GOURAUD") -> Types.GOURAUD;
            case ("PHONG") -> Types.PHONG;
            case ("WIREFRAME") -> Types.WIREFRAME;
            case ("FILL") -> Types.FILL;
            default -> Types.FLAT;
        };
    }

    public enum Types {
        FLAT,
        GOURAUD,
        PHONG,
        WIREFRAME,
        FILL
    }
}
