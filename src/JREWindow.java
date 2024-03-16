import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JREWindow extends JPanel implements ActionListener {
    private int WIDTH, HEIGHT;
    private Timer timer;
    public JREWindow() {

        timer = new Timer(16, this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
