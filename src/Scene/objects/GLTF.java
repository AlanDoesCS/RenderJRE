package Scene.objects;

import org.json.simple.JSONObject;

import java.awt.*;

public class GLTF extends Shape {
    public GLTF(double x, double y, double z, JSONObject size, Color colour) {
        super(x, y, z, (double) size.get("scale"), colour);
    }
}
