package Rendering;

/*
Used for calculating where a beam of light intersects an object

Based on:
https://en.wikipedia.org/wiki/M%C3%B6ller%E2%80%93Trumbore_intersection_algorithm

but not using Java3D
*/

import Scene.objects.dependencies.Triangle;
import rMath.Vector3D;
import rMath.Vertex;

public class MollerTrumbore {

    private static final double EPSILON = 0.0000001;

    public static boolean rayIntersectsTriangle(Vertex rayOrigin,
                                                Vector3D rayVector,
                                                Triangle inTriangle,
                                                Vertex outIntersectionPoint) {
        Vertex vertex0 = inTriangle.points[0];
        Vertex vertex1 = inTriangle.points[1];
        Vertex vertex2 = inTriangle.points[2];
        Vector3D edge1 = new Vector3D();
        Vector3D edge2 = new Vector3D();
        Vector3D h = new Vector3D();
        Vector3D s = new Vector3D();
        Vector3D q = new Vector3D();

        double a, f, u, v;
        edge1.set_sub(vertex1, vertex0); // same as "edge1.x = vertex1.x - vertex0.x; ... "
        edge2.set_sub(vertex2, vertex0);
        h.set_cross(rayVector, edge2); // h = rayVector cross edge2
        a = edge1.dot(h);

        if (a > -EPSILON && a < EPSILON) {
            return false;    // This ray is parallel to this triangle.
        }

        f = 1.0 / a;
        s.set_sub(rayOrigin, vertex0);
        u = f * (s.dot(h));

        if (u < 0.0 || u > 1.0) {
            return false;
        }

        q.set_cross(s, edge1);
        v = f * rayVector.dot(q);

        if (v < 0.0 || u + v > 1.0) {
            return false;
        }

        // At this stage we can compute t to find out where the intersection point is on the line.
        double t = f * edge2.dot(q);
        if (t > EPSILON) // ray intersection
        {
            outIntersectionPoint.set(0.0, 0.0, 0.0);
            outIntersectionPoint.scaleAdd(t, rayVector, rayOrigin);
            return true;
        } else // This means that there is a line intersection but not a ray intersection.
        {
            return false;
        }
    }
}