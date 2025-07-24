package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;
import gui.Gameover;

public class boardGUI implements Serializable {

    private static final int ROWS = 8;
    private static final int COLS = 8;
    private final JPanel[][] gameBoardSquares = new JPanel[ROWS][COLS];
    private JPanel selectedPiece = null;
    private JFrame frame;
    private String[][] boardState = new String[ROWS][COLS];

    // Customization settings
    private Color lightSquareColor = Color.WHITE;
    private Color darkSquareColor = Color.BLACK;
    private String pieceStyle = "Classic"; // "Classic" or "Modern"
    private int boardSize = 800; // Default size

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new boardGUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(ROWS, COLS));
        frame.setSize(boardSize, boardSize);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game Menu Options");
        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem saveGameItem = new JMenuItem("Save Game");
        JMenuItem loadGameItem = new JMenuItem("Load Game");
        JMenuItem settingsItem = new JMenuItem("Settings");

        // Make menu font bigger
        Font menuFont = new Font("Arial", Font.BOLD, 22);
        gameMenu.setFont(menuFont);
        newGameItem.setFont(menuFont);
        saveGameItem.setFont(menuFont);
        loadGameItem.setFont(menuFont);
        settingsItem.setFont(menuFont);

        gameMenu.add(newGameItem);
        gameMenu.add(saveGameItem);
        gameMenu.add(loadGameItem);
        gameMenu.addSeparator();
        gameMenu.add(settingsItem);
        menuBar.add(gameMenu);
        frame.setJMenuBar(menuBar);

        // Menu options
        newGameItem.addActionListener(e -> resetBoard());
        saveGameItem.addActionListener(e -> saveGame());
        loadGameItem.addActionListener(e -> loadGame());
        settingsItem.addActionListener(e -> openSettingsDialog());

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground((i + j) % 2 == 0 ? lightSquareColor : darkSquareColor);
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setFont(new Font("Serif", Font.PLAIN, 46));
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

        resetBoard();

        frame.setVisible(true);
    }

    private void resetBoard() {
        // clear board state
        for (int i = 0; i < ROWS; i++) {
            Arrays.fill(boardState[i], "");
        }
        // Set up black pieces
        boardState[0] = new String[] { "♜", "♞", "♝", "♛", "♚", "♝", "♞", "♜" };
        Arrays.fill(boardState[1], "♟");
        // Set up white pieces
        boardState[7] = new String[] { "♖", "♘", "♗", "♕", "♔", "♗", "♘", "♖" };
        Arrays.fill(boardState[6], "♙");
        // clear middle
        for (int i = 2; i <= 5; i++) {
            Arrays.fill(boardState[i], "");
        }
        updateBoardFromState();
        if (selectedPiece != null) {
            selectedPiece.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            selectedPiece = null;
        }
    }

    private void updateBoardFromState() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                JLabel label = getLabelAt(i, j);
                label.setText(getStyledPiece(boardState[i][j]));
            }
        }
        // Update square colors in case settings changed
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                gameBoardSquares[i][j].setBackground((i + j) % 2 == 0 ? lightSquareColor : darkSquareColor);
            }
        }
        frame.setSize(boardSize, boardSize);
    }

    private void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(boardState);
                JOptionPane.showMessageDialog(frame, "Game saved successfully.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving game: " + ex.getMessage());
            }
        }
    }

    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = in.readObject();
                if (obj instanceof String[][] loadedState) {
                    boardState = loadedState;
                    updateBoardFromState();
                    if (selectedPiece != null) {
                        selectedPiece.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        selectedPiece = null;
                    }
                    JOptionPane.showMessageDialog(frame, "Game loaded successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid save file.");
                }
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(frame, "Error loading game: " + ex.getMessage());
            }
        }
    }

    private JLabel getLabelAt(int row, int col) {
        JPanel panel = gameBoardSquares[row][col];
        return (JLabel) panel.getComponent(0);
    }

    private void handleMouseClick(JPanel clickedPanel) {
        int[] clickedPos = findPanelPosition(clickedPanel);
        JLabel clickedLabel = (JLabel) clickedPanel.getComponent(0);
        if (selectedPiece == null) {
            if (!clickedLabel.getText().isEmpty()) {
                selectedPiece = clickedPanel;
                clickedPanel.setBorder(BorderFactory.createLineBorder(new Color(0x00FF00), 4));
            }
        } else {
            if (clickedPanel == selectedPiece) {
                selectedPiece.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                selectedPiece = null;
                return;
            }
            movePiece(selectedPiece, clickedPanel);
            selectedPiece.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            selectedPiece = null;
        }
    }

    private void movePiece(JPanel sourcePanel, JPanel targetPanel) {
        JLabel sourceLabel = (JLabel) sourcePanel.getComponent(0);
        JLabel targetLabel = (JLabel) targetPanel.getComponent(0);

        int[] from = findPanelPosition(sourcePanel);
        int[] to = findPanelPosition(targetPanel);

        String moving = boardState[from[0]][from[1]];
        String captured = boardState[to[0]][to[1]];

        if (!moving.isEmpty()) {
            if ("♚".equals(captured)) {
                Gameover.showGameOverMessage("White wins! Black King was captured.");
                System.exit(0);
                return;
            } else if ("♔".equals(captured)) {
                Gameover.showGameOverMessage("Black wins! White King was captured.");
                System.exit(0);
                return;
            }

            boardState[to[0]][to[1]] = moving;
            boardState[from[0]][from[1]] = "";
            targetLabel.setText(getStyledPiece(moving));
            sourceLabel.setText("");
        }
    }

    private int[] findPanelPosition(JPanel panel) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (gameBoardSquares[i][j] == panel) {
                    return new int[] { i, j };
                }
            }
        }
        return new int[] { -1, -1 }; // Not found
    }

    // Settings dialog
    private void openSettingsDialog() {
        JDialog dialog = new JDialog(frame, "Settings", true);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.setSize(400, 300);

        // Board colors
        JLabel lightLabel = new JLabel("Light Square Color:");
        JButton lightButton = new JButton();
        lightButton.setBackground(lightSquareColor);
        lightButton.addActionListener(e -> {
            Color c = JColorChooser.showDialog(dialog, "Choose Light Square Color", lightSquareColor);
            if (c != null)
                lightButton.setBackground(c);
        });

        JLabel darkLabel = new JLabel("Dark Square Color:");
        JButton darkButton = new JButton();
        darkButton.setBackground(darkSquareColor);
        darkButton.addActionListener(e -> {
            Color c = JColorChooser.showDialog(dialog, "Choose Dark Square Color", darkSquareColor);
            if (c != null)
                darkButton.setBackground(c);
        });

        // Piece style
        JLabel pieceLabel = new JLabel("Piece Style:");
        String[] pieceStyles = { "Classic", "Modern" };
        JComboBox<String> pieceCombo = new JComboBox<>(pieceStyles);
        pieceCombo.setSelectedItem(pieceStyle);

        // Board size
        JLabel sizeLabel = new JLabel("Board Size:");
        String[] sizes = { "Small", "Medium", "Large" };
        JComboBox<String> sizeCombo = new JComboBox<>(sizes);
        if (boardSize <= 500)
            sizeCombo.setSelectedIndex(0);
        else if (boardSize <= 800)
            sizeCombo.setSelectedIndex(1);
        else
            sizeCombo.setSelectedIndex(2);

        // OK/Cancel
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        ok.addActionListener(e -> {
            lightSquareColor = lightButton.getBackground();
            darkSquareColor = darkButton.getBackground();
            pieceStyle = (String) pieceCombo.getSelectedItem();
            String sizeSel = (String) sizeCombo.getSelectedItem();
            boardSize = switch (sizeSel) {
                case "Small" -> 500;
                case "Medium" -> 800;
                case "Large" -> 1000;
                default -> 800;
            };
            updateBoardFromState();
            dialog.dispose();
        });
        cancel.addActionListener(e -> dialog.dispose());

        dialog.add(lightLabel);
        dialog.add(lightButton);
        dialog.add(darkLabel);
        dialog.add(darkButton);
        dialog.add(pieceLabel);
        dialog.add(pieceCombo);
        dialog.add(sizeLabel);
        dialog.add(sizeCombo);
        dialog.add(ok);
        dialog.add(cancel);

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    // Piece style logic (expand as you add more styles)
    private String getStyledPiece(String piece) {
        if (pieceStyle.equals("Modern")) {
            // Example: use different Unicode or text for modern style
            // You can expand this mapping as you wish
            return switch (piece) {
                case "♜" -> "\u265C";
                case "♞" -> "\u265E";
                case "♝" -> "\u265D";
                case "♛" -> "\u265B";
                case "♚" -> "\u265A";
                case "♟" -> "\u265F";
                case "♖" -> "\u2656";
                case "♘" -> "\u2658";
                case "♗" -> "\u2657";
                case "♕" -> "\u2655";
                case "♔" -> "\u2654";
                case "♙" -> "\u2659";
                default -> piece;
            };
        }
        // Classic style (default)
        return piece;
    }
}