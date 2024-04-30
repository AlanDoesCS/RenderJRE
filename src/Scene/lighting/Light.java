package Scene.lighting;

import Levels.Level;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rMath.Vector3D;

import java.awt.*;
import java.lang.reflect.Field;

public abstract class Light {
    float x, y, z;
    String id = "idLess";
    boolean visibility = true;
    Color color = Color.WHITE;
    Level parent = null;

    // initialisation handlers
    public Light(float x, float y, float z, Color color, Level parent) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.parent = parent;
    }
    public static Light of(JSONObject object, Level parentLevel) {
        Light light;

        JSONArray coordinates = (JSONArray) object.get("coordinate");
        Object colorObj = object.get("color");

        // coordinate handling
        float x = ((Double) coordinates.get(0)).floatValue();
        float y = ((Double) coordinates.get(1)).floatValue();
        float z = ((Double) coordinates.get(2)).floatValue();

        // color handling
        Color color;

        if (colorObj instanceof String) {
            // Color stored as a string (e.g., "BLACK")
            try {
                Field field = Class.forName("java.awt.Color").getField((String) colorObj);
                color = (Color)field.get(null);
            } catch (Exception e) {
                color = null; // Not defined
            }
        } else if (colorObj instanceof JSONArray colorArray) {
            // Color stored as an array (e.g., [255, 255, 255])
            int red = ((Long) colorArray.get(0)).intValue();
            int green = ((Long) colorArray.get(1)).intValue();
            int blue = ((Long) colorArray.get(2)).intValue();
            color = new Color(red, green, blue);
        } else {
            throw new IllegalArgumentException("Invalid color format");
        }

        light = switch ((String) object.get("type")) {
            case ("DirectLight") -> new DirectLight(x, y, z, new Vector3D((JSONArray) object.get("direction")), color, parentLevel);
            // TODO: implement lighting types:
            case ("PointLight") -> new PointLight(x, y, z, color, parentLevel);
            case ("RectLight") -> new RectLight(x, y, z, color, parentLevel);
            default -> throw new IllegalArgumentException("Attempted to create illegal or unsupported object type");
        };

        light.setId((String) object.get("id"));
        light.setVisibility((boolean) object.get("visible"));

        return light;
    }

    // Accessors and Mutators
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
    public boolean isVisible() {
        return visibility;
    }
}
