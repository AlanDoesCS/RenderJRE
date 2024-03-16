package Scene.lighting;

import rMath.Vector3D;

import java.awt.*;

public class DirectLight {
    public Color defaultColour = Color.WHITE, ambient;
    public Vector3D direction;
    private void init(Color ambient, double dir_x, double dir_y, double dir_z) {
        this.direction = new Vector3D(dir_x, dir_y, dir_z);
        this.ambient = ambient;
    }
    public DirectLight(double x, double y, double z, Color ambient) {
        init(ambient, x, y, z);
    }
}