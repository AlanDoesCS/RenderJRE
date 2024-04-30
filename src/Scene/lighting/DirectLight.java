package Scene.lighting;

import Levels.Level;
import rMath.Vector3D;

import java.awt.*;

public class DirectLight extends Light {
    public Color ambient;
    public Vector3D direction;

    public DirectLight(float x, float y, float z, Vector3D direction, Color ambient, Level parentLevel) {
        super(x, y, z, Color.white, parentLevel);
        this.direction = direction;
        this.ambient = ambient;
    }
    public DirectLight(float x, float y, float z, Vector3D direction, Color ambient, Color color, Level parentLevel) {
        super(x, y, z, Color.white, parentLevel);
        this.direction = direction;
        this.color = color;
        this.ambient = ambient;
    }
}