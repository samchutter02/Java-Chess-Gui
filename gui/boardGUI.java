package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class boardGUI {

    private static final int ROWS = 8;
    private static final int COLS = 8;
    private final JPanel[][] gameBoardSquares = new JPanel[ROWS][COLS];
    private JPanel selectedPiece = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new boardGUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(ROWS, COLS));
        frame.setSize(600, 600);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK);
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setFont(new Font("Serif", Font.PLAIN, 36));
                label.setForeground((i + j) % 2 == 0 ? Color.BLACK : Color.WHITE);
                panel.add(label, BorderLayout.CENTER);
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleMouseClick(panel);
                    }
                });
                gameBoardSquares[i][j] = panel;
                frame.add(panel);
            }
        }

        setupGamePieces();

        frame.setVisible(true);
    }

    private void setupGamePieces() {
        //black pieces
        getLabelAt(0, 0).setText("♜");
        getLabelAt(0, 1).setText("♞");
        getLabelAt(0, 2).setText("♝");
        getLabelAt(0, 3).setText("♛");
        getLabelAt(0, 4).setText("♚");
        getLabelAt(0, 5).setText("♝");
        getLabelAt(0, 6).setText("♞");
        getLabelAt(0, 7).setText("♜");
        for (int j = 0; j < COLS; j++) {
            getLabelAt(1, j).setText("♟");
        }
        // white pieces
        getLabelAt(7, 0).setText("♖");
        getLabelAt(7, 1).setText("♘");
        getLabelAt(7, 2).setText("♗");
        getLabelAt(7, 3).setText("♕");
        getLabelAt(7, 4).setText("♔");
        getLabelAt(7, 5).setText("♗");
        getLabelAt(7, 6).setText("♘");
        getLabelAt(7, 7).setText("♖");
        for (int j = 0; j < COLS; j++) {
            getLabelAt(6, j).setText("♙");
        }
    }

    private JLabel getLabelAt(int row, int col) {
        JPanel panel = gameBoardSquares[row][col];
        return (JLabel) panel.getComponent(0);
    }

    private void handleMouseClick(JPanel clickedPanel) {
        JLabel clickedLabel = (JLabel) clickedPanel.getComponent(0);
        if (selectedPiece == null) {
            if (!clickedLabel.getText().isEmpty()) {
                selectedPiece = clickedPanel;
                clickedPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            }
        } else {
            if (clickedPanel == selectedPiece) {
                selectedPiece.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                selectedPiece = null;
            } else {
                JLabel selectedLabel = (JLabel) selectedPiece.getComponent(0);
                //move piece
                clickedLabel.setText(selectedLabel.getText());
                selectedLabel.setText("");
                selectedPiece.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                selectedPiece = null;
            }
        }
    }

    private int[] findPanelPosition(JPanel panel) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (gameBoardSquares[i][j] == panel) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1}; //Not found
    }
}