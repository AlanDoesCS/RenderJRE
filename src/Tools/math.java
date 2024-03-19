package Tools;

import java.lang.Math;

public class math {
    public static double clamp (double min, double value, double max) {
        return Math.max(min, Math.min(max, value));
    }
    public static int randInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
