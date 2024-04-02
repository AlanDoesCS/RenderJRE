package Scene.objects;

import org.json.simple.JSONObject;

import java.awt.*;

public class GLTF extends Shape {
    public GLTF(float x, float y, float z, JSONObject size, Color colour) {
        super(x, y, z, (float) size.get("scale"), colour);
    }
}
