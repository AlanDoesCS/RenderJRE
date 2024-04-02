package Tools;

import Rendering.Pixel;
import Scene.objects.Shape;
import Scene.objects.dependencies.Triangle;

import java.lang.Math;

public class math {
    public static float clamp (float min, float value, float max) {
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
    public static float distance(Shape shape1, Shape shape2) {
        float dx = shape1.getOrigin().x - shape2.getOrigin().x;
        float dy = shape1.getOrigin().y - shape2.getOrigin().y;
        float dz = shape1.getOrigin().z - shape2.getOrigin().z;

        return (float) Math.sqrt(dx*dx + dy*dy + dz*dz); // return magnitude of displacement vector
    }
    public static float distance(Triangle triangle, Shape parent, Shape shape2) {
        float s = parent.getScale();

        float dx = parent.getOrigin().x + s*triangle.midpoint.x - shape2.getOrigin().x;
        float dy = parent.getOrigin().y + s*triangle.midpoint.y - shape2.getOrigin().y;
        float dz = parent.getOrigin().z + s*triangle.midpoint.z - shape2.getOrigin().z;

        return (float) Math.sqrt(dx*dx + dy*dy + dz*dz); // return magnitude of displacement vector
    }

    public static Pixel minX(Pixel a, Pixel b) {
        if (a.getX() < b.getX()) {
            return a;
        }
        return b;
    }

    public static Pixel maxX(Pixel a, Pixel b) {
        if (a.getX() >= b.getX()) {
            return a;
        }
        return b;
    }

    // stupid crazy quake III inverse square root approximation algorithm
    public static float Q_rsqrt(float number) { // approximation of "1/sqrt(number);"
        float x = number;
        float xhalf = 0.5f * x;
        int i = Float.floatToIntBits(x);
        i = 0x5f3759df - (i >> 1);
        x = Float.intBitsToFloat(i);
        x *= (1.5f - xhalf * x * x);
        return x;
    }
}
