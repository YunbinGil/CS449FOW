package com.YunbinGil.sos;

import javax.swing.*;
import java.awt.*;

public class SosGUI extends JFrame {
    private SosGame game;
    private JButton[][] buttons;
    private int boardSize = 5;
    private boolean isSimpleGame = true;

    public SosGUI() {
        setTitle("SOS Game");  // Window Title
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ✅ Top panel: Title Label
        JPanel topPanel = new JPanel();
        String[] sizes = {"3x3", "5x5", "8x8"};
        JComboBox<String> sizeSelector = new JComboBox<>(sizes);
        sizeSelector.addActionListener(e -> setBoardSize(sizeSelector.getSelectedIndex()));

        String[] modes = {"Simple", "General"};
        JComboBox<String> modeSelector = new JComboBox<>(modes);
        sizeSelector.addActionListener(e -> isSimpleGame = modeSelector.getSelectedIndex() == 0);

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> startNewGame());

        topPanel.add(new JLabel("Board Size:"));
        topPanel.add(sizeSelector);
        topPanel.add(new JLabel("Game Mode:"));
        topPanel.add(modeSelector);
        topPanel.add(newGameButton);
        add(topPanel, BorderLayout.NORTH);

        initializeGame();
        setVisible(true);
    }

    private void setBoardSize(int index) {
        switch (index) {
            case 0: boardSize = 3; break;
            case 1: boardSize = 5; break;
            case 2: boardSize = 8; break;
        }
    }
    private void initializeGame() {
        game = new SosGame(boardSize);
        JPanel boardPanel = new JPanel(new GridLayout(boardSize, boardSize));
        buttons = new JButton[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 24));
                final int row = i, col = j;
                buttons[i][j].addActionListener(e -> placeLetter(row, col));
                boardPanel.add(buttons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
    }
    private void placeLetter(int row, int col) {
        if (game == null || game.isCellEmpty(row, col)) {
            String[] choices = {"S", "O"};
            String letter = (String) JOptionPane.showInputDialog(this, "Choose a letter:",
                    "Move", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
            if (letter != null) {
                game.placeLetter(row, col, letter.charAt(0));
                buttons[row][col].setText(letter);
            }
        }
    }
    private void startNewGame() {
        getContentPane().removeAll();
        initializeGame();
        revalidate();
        repaint();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SosGUI::new);
    }
}
