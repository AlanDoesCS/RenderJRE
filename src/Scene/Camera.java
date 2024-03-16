package Scene;

import rMath.Vector3D;

public class Camera<T> extends Scene.objects.Shape {
    Vector3D direction = new Vector3D(0, 0, 1);
    public Camera (double x, double y, double z) {
        super(x, y, z, 1);

    }
}
