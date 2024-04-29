package Tools;

import java.awt.Color;

import static Tools.math.randInt;

public class swing {
    public static Color generateRandomColor() {
        return new Color(randInt(0, 256), randInt(0, 256), randInt(0, 256));
    }
}
