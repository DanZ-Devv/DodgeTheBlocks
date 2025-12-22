import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main (String[] args) {
        // Game window
        JFrame window = new JFrame("Dodge the Falling Blocks!");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // exits jframe with window close
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);  // full-screen
        window.setUndecorated(false);  // keep title bar
        window.setResizable(true);     // allow resizing

        // Game panel
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}