package rMath;

import Scene.objects.dependencies.*;
import Tools.math;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;

public class Vector3D {
    public float i, j, k;

    public Vector3D(float i, float j, float k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public Vector3D(float[] array) {
        this.i = array[0];
        this.j = array[1];
        this.k = array[2];
    }

    public Vector3D(Vertex a, Vertex b) {
        this.i = b.x - a.x;
        this.j = b.y - a.y;
        this.k = b.z - a.z;
    }

    public Vector3D() {}

    public Vector3D(JSONArray direction) {
        this.i = ((Double) direction.get(0)).floatValue();
        this.j = ((Double) direction.get(1)).floatValue();
        this.k = ((Double) direction.get(2)).floatValue();
    }

    public static Vector3D displacement(Vertex2D source, Vertex2D target) {
        float dx = target.x - source.x;
        float dy = target.y - source.y;
        float dz = target.z - source.z;

        return new Vector3D(dx, dy, dz);
    }

    public static Vector3D displacement(Vertex source, Vertex target) {
        float dx = target.x - source.x;
        float dy = target.y - source.y;
        float dz = target.z - source.z;

        return new Vector3D(dx, dy, dz);
    }

    public float length() {
        return (float) Math.sqrt(i*i + j*j + k*k);
    }

    public void normalise() {
        float radicand = i*i + j*j + k*k;
        float inverseSqrt = math.Q_rsqrt(radicand);
        this.i *= inverseSqrt;
        this.j *= inverseSqrt;
        this.k *= inverseSqrt;
    }

    public Vector3D cross(Vector3D v, Vector3D w) {
        Vector3D result = new Vector3D();

        result.i = v.j*w.k - v.k*w.j;
        result.j = v.k*w.i - v.i*w.k;
        result.k = v.i*w.j - v.j*w.i;

        return result;
    }

    public Vector3D normal(Triangle triangle) { // returns cross product of 2 vectors formed by the triangle
        Vector3D result = new Vector3D();

        Vector3D AB = new Vector3D(triangle.points[0], triangle.points[1]);
        Vector3D AC = new Vector3D(triangle.points[0], triangle.points[2]);

        result.i = AB.j*AC.k - AB.k*AC.j;
        result.j = AB.k*AC.i - AB.i*AC.k;
        result.k = AB.i*AC.j - AB.j*AC.i;

        return result;
    }

    public void set_cross(Vector3D v, Vector3D w) { // sets the vector to be equal to the cross product of 2 vectors
        this.i = v.j*w.k - v.k*w.j;
        this.j = v.k*w.i - v.i*w.k;
        this.k = v.i*w.j - v.j*w.i;
    }

    public float dot(Vector3D v, Vector3D w) {
        return v.i*w.i + v.j*w.j + v.k;
    }

    public float dot(Vector3D w) {
        return dot(this, w);
    }

    public void sub(Vector3D v) {
        this.i -= v.i;
        this.j -= v.j;
        this.k -= v.k;
    }

    public void set_sub(Vertex v, Vertex w) { // sets the vector to be equal to one vertex - another vertex
        this.i -= v.x - w.x;
        this.j -= v.y - w.y;
        this.k -= v.z - w.z;
    }

    public float angle(Vector3D v, Vector3D w) { // returns angle between two vectors
        return (float) Math.acos( dot(v, w) / (v.length() * w.length()));
    }
    public String toString() {
        return "("+i+", "+j+", "+k+")";
    }
}
