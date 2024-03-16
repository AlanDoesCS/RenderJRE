package Scene.objects;
import Scene.objects.dependencies.*;

import rMath.Vertex;
import java.awt.Color;

public class Hexagonal_Prism extends Shape {
    public Hexagonal_Prism(double x, double y, double z, double length, double scale, Color colour) {
        super(x, y, z, scale, colour);

        double r3 = Math.sqrt(3);
        double l = length/2;

        Vertex A = new Vertex(r3, l, 1);
        Vertex B = new Vertex(r3, l, -1);
        Vertex C = new Vertex(-r3, l, 1);
        Vertex D = new Vertex(-r3, l, -1);
        Vertex E = new Vertex(0,l,2);
        Vertex F = new Vertex(0,l,-2);

        Vertex G = new Vertex(r3, -l, 1);
        Vertex H = new Vertex(r3, -l, -1);
        Vertex I = new Vertex(-r3, -l, 1);
        Vertex J = new Vertex(-r3, -l, -1);
        Vertex K = new Vertex(0,-l,2);
        Vertex L = new Vertex(0,-l,-2);

        vertices = new Vertex[]{A,B,C,D,E,F,G,H,I,J,K,L};

        for (Vertex v : vertices) { // change to fill 1x1 space
            v.x /= 2;
            v.y /= 2;
            v.z /= 2;
        }

        Vertex[][] faces = { // triangles
                {A, E, K}, {K, G, A},
                {B, A, G}, {G, H, B},
                {F, B, H}, {H, L, F},
                {D, F, L}, {L, J, D},
                {C, D, J}, {J, I, C},
                {E, C, I}, {I, K, E},
                //Sides
                {F, D, C}, {F, C, E},
                {F, E, A}, {F, A, B},

                {L, H, G}, {L, G, K},
                {L, K, I}, {L, I, J},
        };

        ///*
        for (Vertex[] face : faces) { // debug
            Vertex temp = face[0];
            face[0] = face[2];
            face[2] = temp;
        }
        //*/

        for (Vertex[] face : faces) {
            triangles.add(new Triangle(face[0], face[1], face[2]));
        }
    }
}