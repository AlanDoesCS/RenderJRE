import Rendering.JREWindow;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JREWindow Window = new JREWindow(60, 1000, 700);
        Window.addLevel(new Levels.Level1.Level());
        Window.addLevel(new Levels.RedCubeTest.Level());
        Window.loadLevel(0);

        //ArrayList<String> arguments = new ArrayList<>(Arrays.asList("diffuse"));
        //Window.passArguments(arguments);

        JFrame fr = new JFrame();
        fr.setBounds(0, 0, Window.getWIDTH(), Window.getHEIGHT());
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        fr.setBackground(Color.BLACK);
        fr.add(Window);
        fr.setVisible(true);
    }
}
