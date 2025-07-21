package gui;
import javax.swing.JOptionPane;

public class Gameover {
    public static void showGameOverMessage(String message) {
        JOptionPane.showMessageDialog(null, message,"Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
}
