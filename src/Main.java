import Levels.Level;
import Levels.Level1.Level1;
import Rendering.JREWindow;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JREWindow Window = new JREWindow(60, 1000, 1000);
        Window.setBackground(Color.BLACK);
        Window.addLevel(new Level1());
        Window.loadLevel(0);

        ArrayList<String> arguments = new ArrayList<>(Arrays.asList("wire", "diffuse"));
        Window.passArguments(arguments);

        JFrame fr = new JFrame();
        fr.setBounds(10, 10, Window.getWIDTH(), Window.getHEIGHT());
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        fr.setBackground(Color.BLACK);
        fr.add(Window);
        fr.setVisible(true);
    }
}
