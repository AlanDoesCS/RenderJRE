import java.awt.*;

public class LightSource extends Shape {
    public double brightness, falloff_rate;
    public Vector3D direction, ambient;
    private void init(double brightness, double falloff_rate, double ambient, double dir_x, double dir_y, double dir_z) {
        this.direction = new Vector3D(dir_x, dir_y, dir_z);
        this.brightness = brightness;
        this.falloff_rate = falloff_rate;
        this.ambient = new Vector3D(ambient, ambient, ambient);
    }
    public LightSource(double x, double y, double z, double brightness, double falloff_rate, Color colour) {
        super(new double[][]{{x,y,z}}, x, y, z, 0.1, colour);
        init(brightness, falloff_rate, 0.05, 1, -1, -0.5);
    }
    public LightSource(double x, double y, double z, double brightness, Color colour) {
        super(new double[][]{{x,y,z}}, x, y, z, 0.1, colour);
        init(brightness, 0.1, 0.05, 1, -1, -0.5);

    }
}