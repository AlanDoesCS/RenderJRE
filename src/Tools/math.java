package Tools;

import Rendering.Pixel;
import Scene.objects.Shape;
import Scene.objects.dependencies.Triangle;

import java.lang.Math;

public class math {
    public static double clamp (double min, double value, double max) {
        return Math.max(min, Math.min(max, value));
    }
    public static int randInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public static int min(int a, int b) {
        return Math.min(a, b);
    }
    public static int max(int a, int b) {
        return Math.max(a, b);
    }
    public static double distance(Shape shape1, Shape shape2) {
        double dx = shape1.getOrigin().x - shape2.getOrigin().x;
        double dy = shape1.getOrigin().y - shape2.getOrigin().y;
        double dz = shape1.getOrigin().z - shape2.getOrigin().z;

        return Math.sqrt(dx*dx + dy*dy + dz*dz); // return magnitude of displacement vector
    }
    public static double distance(Triangle triangle, Shape parent, Shape shape2) {
        double s = parent.getScale();

        double dx = parent.getOrigin().x + s*triangle.midpoint.x - shape2.getOrigin().x;
        double dy = parent.getOrigin().y + s*triangle.midpoint.y - shape2.getOrigin().y;
        double dz = parent.getOrigin().z + s*triangle.midpoint.z - shape2.getOrigin().z;

        return Math.sqrt(dx*dx + dy*dy + dz*dz); // return magnitude of displacement vector
    }

    public static Pixel minX(Pixel a, Pixel b) {
        if (a.getX() <= b.getX()) {
            return a;
        }
        return b;
    }

    public static Pixel maxX(Pixel a, Pixel b) {
        if (a.getX() > b.getX()) {
            return a;
        }
        return b;
    }
}
