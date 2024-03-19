import Levels.Level;
import Rendering.JREWindow;
import Rendering.Renderer;
import Scene.lighting.DirectLight;
import Scene.objects.*;

import java.awt.Color;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JREWindow Window = new JREWindow(60, 1000, 1000);
        Window.setBackground(Color.BLACK);
        Window.passArguments(new String[]{"diffuse", "wireframe"});

        JFrame fr = new JFrame();
        fr.setBounds(10, 10, Window.getWIDTH(), Window.getHEIGHT());
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        fr.setBackground(Color.BLACK);
        fr.add(Window);
        fr.setVisible(true);

        Levels.Level w = new Level("src/Levels/scene.json");
    }
}
