package Rendering;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JREWindow extends JPanel implements ActionListener {
    private int WIDTH, HEIGHT;
    private Timer timer;
    public JREWindow(int FPS) {
        int delay = 1000/FPS;
        timer = new Timer(delay, this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
