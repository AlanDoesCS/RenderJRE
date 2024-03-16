import rMath.Vector3D;
import rMath.Vertex;

public class Tools {
    public static Vector3D displacement(Vertex source, Vertex target) {
        double dx = target.x - source.x;
        double dy = target.y - source.y;
        double dz = target.z - source.z;

        return new Vector3D(dx, dy, dz);
    }
}
