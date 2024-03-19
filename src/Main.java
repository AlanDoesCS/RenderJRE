import Levels.Level;
import Rendering.JREWindow;
import Rendering.Renderer;
import Scene.lighting.DirectLight;
import Scene.objects.*;

import java.awt.Color;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        //Light above
        DirectLight light = new DirectLight(1,-3,-2, Color.WHITE);

        //Cube
        Cube cube1 = new Cube(2, -3, 15, 1, Color.RED);
        cube1.setRotation(0, 0, 0);

        //Cuboid
        Cuboid cuboid = new Cuboid(-2, -3, 20, 3.5, 2, 1.1, 1.5, Color.gray);
        cuboid.setRotation(0,0,0);

        // Hexagonal Prism
        Hexagonal_Prism hex_prism = new Hexagonal_Prism(-2, 2.1, 15, 10, 0.5, Color.ORANGE);
        hex_prism.setRotation(90, 0, 0);

        // Icosahedron
        Icosahedron icosahedron = new Icosahedron(2.1, 1.3, 17, 1, Color.pink);
        icosahedron.setRotation(0, 0, 0);

        // Plane
        Plane plane = new Plane(0, -2.5, 10, 3, 1, 1, Color.YELLOW);

        // Camera position
        Shape camera = new Shape(0,0,0,1);

        // Upside-down pyramid
        Pyramid pyramid = new Pyramid(-3, 0.1, 17.5, 0.6, Color.BLUE);
        pyramid.setRotation(0,0,0);


        Shape[] unsortedObjs = {
                icosahedron,
                cuboid,
                cube1,
                plane,
                //hex_prism,
                new Pyramid(2.7, 0, 15, 1, Color.GREEN),
                pyramid,
                new Cube(-1.2, 3, 18.1, 0.7, Color.MAGENTA)
        };

        /*

        //create renderer Object
        Rendering.Renderer RenderJRE = new Renderer(1000, 75,  1000, 1000);
        JFrame fr = new JFrame();
        fr.setBounds(10, 10, RenderJRE.WindowResX, RenderJRE.WindowResY);
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        String a = "-diffuse";
        JPanel pn = RenderJRE.renderPanel(a, unsortedObjs, camera, light);
        //JREWindow Window = new JREWindow(60);

        fr.setBackground(Color.BLACK);
        fr.add(pn);
        fr.setVisible(true);

         */

        Levels.Level w = new Level("src/Levels/level1.json");
    }
}
