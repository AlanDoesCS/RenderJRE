package Scene.objects;

import Levels.Level;
import org.json.simple.JSONObject;

import java.awt.Color;

public class Cube extends Cuboid {
    public Cube(float x, float y, float z, JSONObject size, Color colour, Level parent) {
        super(x, y, z, size, colour, parent);
    }

}
