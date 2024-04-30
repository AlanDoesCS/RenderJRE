package Scene.lighting;

import Levels.Level;

import java.awt.*;

public class PointLight extends Light {
    public PointLight(float x, float y, float z, Color color, Level parentLevel) {
        super(x, y, z, color, parentLevel);
    }
}
