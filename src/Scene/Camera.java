package Scene;

import rMath.Vector3D;

public class Camera extends Scene.objects.Shape {
    Vector3D direction = new Vector3D(0, 0, 1);
    public Camera (float x, float y, float z) {
        super(x, y, z, 1);

    }
}
