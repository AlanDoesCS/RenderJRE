package Scene.objects;

import org.json.simple.JSONObject;

import java.awt.Color;

public class Cube extends Cuboid {
    public Cube(float x, float y, float z, float scale, Color colour) {
        super(x, y, z, 1,1,1, scale, colour);
    }
    public Cube(float x, float y, float z, JSONObject size, Color colour) {
        super(x, y, z, size, colour);
    }
}
