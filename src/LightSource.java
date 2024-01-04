import java.awt.*;

public class LightSource extends Shape {
    public double brightness, falloff_rate;
    public LightSource(double x, double y, double z, double brightness, double falloff_rate, Color colour) {
        super(new double[][]{{x,y,z}}, x, y, z, 0.1, colour);
        this.brightness = brightness;
        this.falloff_rate = falloff_rate;
    }
    public LightSource(double x, double y, double z, double brightness, Color colour) {
        super(new double[][]{{x,y,z}}, x, y, z, 0.1, colour);

        this.brightness = brightness;
        this.falloff_rate = 0.1;
    }
}