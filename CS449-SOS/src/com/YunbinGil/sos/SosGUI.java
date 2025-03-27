package com.YunbinGil.sos;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SosGUI extends JFrame {
    private SosGameController controller;
    private SosGame game;
    private JButton[][] buttons;
    private int boardSize = 3;
    private boolean isSimpleGame = true;
    private JPanel boardPanel;
    private JLabel currentTurnLabel;
    private boolean isBlueTurn = true;
    private boolean gameOver = false;
    private JPanel overlayPanel;
    private JLayeredPane layeredPane;

    public SosGUI() {
        setTitle("SOS Game");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();
        String[] sizes = {"3x3", "5x5", "8x8"};
        JComboBox<String> sizeSelector = new JComboBox<>(sizes);
        sizeSelector.addActionListener(e -> setBoardSize(sizeSelector.getSelectedIndex()));

        String[] modes = {"Simple", "General"};
        JComboBox<String> modeSelector = new JComboBox<>(modes);
        modeSelector.addActionListener(e -> isSimpleGame = modeSelector.getSelectedIndex() == 0);

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> startNewGame());

        currentTurnLabel = new JLabel("Current turn: Blue", SwingConstants.CENTER);
        topPanel.add(new JLabel("Board Size:"));
        topPanel.add(sizeSelector);
        topPanel.add(new JLabel("Game Mode:"));
        topPanel.add(modeSelector);
        topPanel.add(newGameButton);
        add(topPanel, BorderLayout.NORTH);
        add(currentTurnLabel, BorderLayout.SOUTH);

        startNewGame();
        setVisible(true);
    }

    private void setBoardSize(int index) {
        switch (index) {
            case 0 -> boardSize = 3;
            case 1 -> boardSize = 5;
            case 2 -> boardSize = 8;
        }
    }

    private void startNewGame() {
        gameOver = false;
        game = isSimpleGame ? new SimpleGame(boardSize) : new GeneralGame(boardSize);
        controller = new SosGameController(game);
        isBlueTurn = true;
        updateCurrentTurnLabel();

        if (layeredPane != null) remove(layeredPane);

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        boardPanel = new JPanel(new GridLayout(boardSize, boardSize));
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

        overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawWinningLines(g);
            }
        };
        overlayPanel.setOpaque(false);
        overlayPanel.setVisible(true);

        boardPanel.setPreferredSize(new Dimension(getWidth(), getHeight() - 100));
        overlayPanel.setPreferredSize(new Dimension(getWidth(), getHeight() - 100));

        layeredPane.add(boardPanel, Integer.valueOf(0));
        layeredPane.add(overlayPanel, Integer.valueOf(1));

        add(layeredPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void placeLetter(int row, int col) {
        if (game == null || !game.isCellEmpty(row, col) || gameOver) return;

        String[] choices = {"S", "O"};
        String letter = (String) JOptionPane.showInputDialog(this, "Choose a letter:",
                "Move", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

        if (letter != null) {
            controller.handleMove(row, col, letter.charAt(0), isBlueTurn);
            buttons[row][col].setText(letter);
            overlayPanel.repaint();

            if (controller.isGameOver()) {
                gameOver = true;
                disableBoard();
                highlightWinningSOS();

                String resultMessage = controller.getResultMessage();
                JOptionPane.showMessageDialog(this, resultMessage, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            } else {
                isBlueTurn = !isBlueTurn;
                updateCurrentTurnLabel();
            }
        }
    }

    private void updateCurrentTurnLabel() {
        currentTurnLabel.setText("Current turn: " + (isBlueTurn ? "Blue" : "Red"));
    }

    private void disableBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void highlightWinningSOS() {
        // SimpleGame이면 여기서 선 직접 추가
        if (!game.isGeneralMode()) {
            Color winnerColor = controller.getResultMessage().contains("Blue") ? Color.BLUE : Color.RED;

            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    for (int[] d : SosGame.DIRECTIONS) {
                        if (game.checkDirection(i, j, d[0], d[1])) {
                            controller.getSosLines().add(
                                    new SosLine(i, j, d[0], d[1], winnerColor)
                            );
                        }
                    }
                }
            }
        }

        // 선 보이게 하고 다시 그리기
        overlayPanel.setVisible(true);
        overlayPanel.repaint();
    }


    private void drawWinningLines(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        List<SosLine> sosLines = controller.getSosLines();

        for (SosLine line : sosLines) {
            g2.setColor(line.color);
            int row1 = line.row - line.dx;
            int col1 = line.col - line.dy;
            int row2 = line.row + line.dx;
            int col2 = line.col + line.dy;

            if (row1 >= 0 && row1 < boardSize && col1 >= 0 && col1 < boardSize &&
                    row2 >= 0 && row2 < boardSize && col2 >= 0 && col2 < boardSize) {
                drawLineOnGrid(g2, row1, col1, row2, col2);
            }
        }
    }

    private void drawLineOnGrid(Graphics2D g2, int row1, int col1, int row2, int col2) {
        int cellWidth = overlayPanel.getWidth() / boardSize;
        int cellHeight = overlayPanel.getHeight() / boardSize;
        int x1 = (col1 * cellWidth) + (cellWidth / 2);
        int y1 = (row1 * cellHeight) + (cellHeight / 2);
        int x2 = (col2 * cellWidth) + (cellWidth / 2);
        int y2 = (row2 * cellHeight) + (cellHeight / 2);
        g2.drawLine(x1, y1, x2, y2);
    }
}
