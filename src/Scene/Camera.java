package Scene;

import Levels.Level;
import rMath.Vector3D;

public class Camera extends Scene.objects.Shape {
    Vector3D direction = new Vector3D(0, 0, 1);
    public Camera (float x, float y, float z, Level parent) {
        super(x, y, z, 1, parent);

    }
}
