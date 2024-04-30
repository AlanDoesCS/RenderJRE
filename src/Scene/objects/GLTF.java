package Scene.objects;

import Levels.Level;
import org.json.simple.JSONObject;

import java.awt.*;

public class GLTF extends Shape {
    public GLTF(float x, float y, float z, JSONObject size, Color colour, Level parent) {
        super(x, y, z, (float) size.get("scale"), colour, parent);
    }
}
