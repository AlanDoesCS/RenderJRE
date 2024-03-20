package Scene.lighting;

import rMath.Vector3D;

import java.awt.*;

public class DirectLight extends Light {
    public Color ambient;
    public Vector3D direction;

    public DirectLight(double x, double y, double z, Vector3D direction, Color ambient) {
        super(x, y, z, Color.white);
        this.direction = direction;
        this.ambient = ambient;
    }
    public DirectLight(double x, double y, double z, Vector3D direction, Color ambient, Color color) {
        super(x, y, z, Color.white);
        this.direction = direction;
        this.color = color;
        this.ambient = ambient;
    }
}