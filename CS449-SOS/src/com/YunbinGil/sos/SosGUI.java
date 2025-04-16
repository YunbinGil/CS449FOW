package com.YunbinGil.sos;

import javax.swing.*;
import java.awt.*;

public class SosGUI extends JFrame {
    private SosGame game;
    private SosGameController controller;
    private JButton[][] buttons;
    private JPanel boardPanel;
    private JPanel overlayPanel;
    private boolean gameOver = false;
    private JLabel currentTurnLabel;
    private JComboBox<String> boardSizeBox;
    private JComboBox<String> modeBox;
    private JComboBox<String> bluePlayerTypeBox;
    private JComboBox<String> redPlayerTypeBox;

    public SosGUI() {
        setTitle("SOS Game");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setupTopPanel();
        setupPlayerSelectors();

        boardPanel = new JPanel();
        overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawWinningLines(g);
            }
        };
        overlayPanel.setOpaque(false);

        add(overlayPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void setupTopPanel() {
        JPanel topPanel = new JPanel();
        boardSizeBox = new JComboBox<>(new String[]{"3", "5", "8"});
        modeBox = new JComboBox<>(new String[]{"Simple", "General"});
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> startNewGame());
        currentTurnLabel = new JLabel("Current Turn: Blue");

        topPanel.add(new JLabel("Board Size:"));
        topPanel.add(boardSizeBox);
        topPanel.add(new JLabel("Mode:"));
        topPanel.add(modeBox);
        topPanel.add(new JLabel("   "));
        topPanel.add(currentTurnLabel);
        topPanel.add(newGameButton);

        add(topPanel, BorderLayout.SOUTH);
    }

    private void setupPlayerSelectors() {
        JPanel panel = new JPanel();
        bluePlayerTypeBox = new JComboBox<>(new String[]{"Human", "Computer"});
        redPlayerTypeBox = new JComboBox<>(new String[]{"Human", "Computer"});
        panel.add(new JLabel("Blue Player:"));
        panel.add(bluePlayerTypeBox);
        panel.add(new JLabel("Red Player:"));
        panel.add(redPlayerTypeBox);
        add(panel, BorderLayout.NORTH);
    }
    private void setupBoard(int size) {
        if (boardPanel != null) remove(boardPanel);
        if (overlayPanel != null) remove(overlayPanel);

        boardPanel = new JPanel(new GridLayout(size, size));
        buttons = new JButton[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 24));
                buttons[i][j].addActionListener(e -> placeLetter(row, col));
                boardPanel.add(buttons[i][j]);
            }
        }

        overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawWinningLines(g);
            }
        };
        overlayPanel.setOpaque(false);
        overlayPanel.setLayout(new BorderLayout());
        overlayPanel.add(boardPanel, BorderLayout.CENTER);

        add(overlayPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void startNewGame() {
        int size = Integer.parseInt((String) boardSizeBox.getSelectedItem());
        boolean isSimple = modeBox.getSelectedItem().equals("Simple");
        game = isSimple ? new SimpleGame(size) : new GeneralGame(size);
        controller = new SosGameController(game);

        boolean blueIsComputer = bluePlayerTypeBox.getSelectedItem().equals("Computer");
        boolean redIsComputer = redPlayerTypeBox.getSelectedItem().equals("Computer");
        controller.setPlayerTypes(blueIsComputer, redIsComputer);

        gameOver = false;
        updateCurrentTurnLabel();

        setupBoard(size);
        repaintOverlay();

        if (blueIsComputer) {
            SwingUtilities.invokeLater(() -> {
                controller.handleComputerTurn(true);
                repaintOverlay();
            });
        }
    }

    private void placeLetter(int row, int col) {
        if (game == null || !game.isCellEmpty(row, col) || gameOver) return;

        String[] choices = {"S", "O"};
        String letter = (String) JOptionPane.showInputDialog(this, "Choose a letter:",
                "Move", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

        if (letter != null) {
            boolean isBlueTurn = controller.getGame().isBlueTurn();
            controller.handleMove(row, col, letter.charAt(0), isBlueTurn);

            char actual = controller.getGame().getLetter(row, col);
            buttons[row][col].setText(String.valueOf(actual));

            repaintOverlay();

            if (controller.isGameOver()) {
                gameOver = true;
                disableBoard();
                highlightWinningSOS();
                JOptionPane.showMessageDialog(this, controller.getResultMessage(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            updateCurrentTurnLabel();

            SwingUtilities.invokeLater(() -> {
                while (!controller.isGameOver()) {
                    boolean isComputer = (controller.getGame().isBlueTurn() && controller.isBlueComputer()) ||
                            (!controller.getGame().isBlueTurn() && controller.isRedComputer());

                    if (isComputer) {
                        boolean before = controller.getGame().isBlueTurn();
                        ComputerPlayer.Move move = controller.handleComputerTurn(before);
                        if (move != null) {
                            buttons[move.row][move.col].setText(String.valueOf(move.letter));
                        }
                        repaintOverlay();
                        updateCurrentTurnLabel();

                        if (controller.isGameOver()) {
                            gameOver = true;
                            disableBoard();
                            highlightWinningSOS();
                            JOptionPane.showMessageDialog(this, controller.getResultMessage(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }

                        if (controller.getGame().isBlueTurn() == before) break;

                    } else {
                        break;
                    }
                }

                if (controller.isGameOver() && !gameOver) {
                    gameOver = true;
                    disableBoard();
                    highlightWinningSOS();
                    JOptionPane.showMessageDialog(this, controller.getResultMessage(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
    }

    private void updateCurrentTurnLabel() {
        boolean isBlue = controller.getGame().isBlueTurn();
        currentTurnLabel.setText("Current Turn: " + (isBlue ? "Blue" : "Red"));
    }

    private void disableBoard() {
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                button.setEnabled(false);
            }
        }
    }

    private void repaintOverlay() {
        overlayPanel.repaint();
    }

    private void highlightWinningSOS() {
        repaintOverlay();
    }

    private void drawWinningLines(Graphics g) {
        if (controller == null) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        for (SosLine line : controller.getSosLines()) {
            int row = line.row, col = line.col;
            int dx = line.dx, dy = line.dy;
            int row1 = row - dx, col1 = col - dy;
            int row2 = row + dx, col2 = col + dy;

            if (inBounds(row1, col1) && inBounds(row2, col2)) {
                JButton b1 = buttons[row1][col1];
                JButton b2 = buttons[row2][col2];
                Point p1 = b1.getLocation();
                Point p2 = b2.getLocation();
                int x1 = p1.x + b1.getWidth() / 2;
                int y1 = p1.y + b1.getHeight() / 2;
                int x2 = p2.x + b2.getWidth() / 2;
                int y2 = p2.y + b2.getHeight() / 2;

                g2.setColor(line.color);
                g2.drawLine(x1, y1, x2, y2);
            }
        }
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < buttons.length && col >= 0 && col < buttons[0].length;
    }
}
