package gui;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import gui.Gameover;

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
        frame.setSize(800, 800);

        for (int i = 0; i < ROWS; i++) { //loop through each row
            for (int j = 0; j < COLS; j++) { // loop through each column
            JPanel panel = new JPanel(new BorderLayout()); //create new JPanel for each square
            panel.setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.BLACK); //alternate square colors
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // add black border to each square
            JLabel label = new JLabel("", SwingConstants.CENTER); //create label to hold chess piece
            label.setFont(new Font("Serif", Font.PLAIN, 46)); //set font size for chess pieces
            label.setForeground((i + j) % 2 == 0 ? Color.BLACK : Color.WHITE); //set icon color (contrast)
            panel.add(label, BorderLayout.CENTER); //add label to center of panel
            panel.addMouseListener(new MouseAdapter() { //Add mouse listener for click e
                @Override
                public void mouseClicked(MouseEvent e) {
                handleMouseClick(panel); //handle piece selection/movement
                }
            });
            gameBoardSquares[i][j] = panel; //store panel in the board array
            frame.add(panel); //add the panel to the frame
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
                clickedPanel.setBorder(BorderFactory.createLineBorder(new Color(0x00FF00), 4)); // Example hex color
            }
        } else {
            movePiece(selectedPiece, clickedPanel);
            selectedPiece.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            selectedPiece = null;
        }
    }
    
    private void movePiece(JPanel sourcePanel, JPanel targetPanel){
        JLabel sourceLabel = (JLabel) sourcePanel.getComponent(0);
        JLabel targetLabel = (JLabel) targetPanel.getComponent(0);
        
        if(!sourceLabel.getText().isEmpty()){
            targetLabel.setText(sourceLabel.getText());
            sourceLabel.setText("");
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
  