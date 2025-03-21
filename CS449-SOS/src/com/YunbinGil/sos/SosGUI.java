package com.YunbinGil.sos;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SosGUI extends JFrame {
    private SosGame game;
    private JButton[][] buttons;
    private int boardSize = 5;
    private boolean isSimpleGame = true;
    private JPanel boardPanel;
    private JLabel currentTurnLabel;
    private boolean isBlueTurn = true;
    private boolean gameOver = false;
    private JPanel overlayPanel;
    private JLayeredPane layeredPane;
    private List<int[]> sosLines = new ArrayList<>(); // 각 SOS의 중심 좌표 (row, col)

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
            case 0:
                boardSize = 3;
                break;
            case 1:
                boardSize = 5;
                break;
            case 2:
                boardSize = 8;
                break;
        }
    }

    private void startNewGame() {
        gameOver = false;
        game = isSimpleGame ? new SimpleGame(boardSize) : new GeneralGame(boardSize);
        isBlueTurn = true;
        updateCurrentTurnLabel();

        sosLines.clear();

        if (layeredPane != null) remove(layeredPane);

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane)); // 자동으로 크기 맞추도록 변경
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        // **기본 boardPanel 생성 (Sprint 2에서 설정한 크기 유지)**
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

        // **승리한 SOS 강조를 위한 overlayPanel 생성**
        overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (gameOver) {
                    drawWinningLines(g);
                }
            }
        };
        overlayPanel.setOpaque(false);

        // **boardPanel과 overlayPanel 크기를 자동으로 맞춤**
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
            game.placeLetter(row, col, letter.charAt(0));
            buttons[row][col].setText(letter);

            if (!isSimpleGame && game.checkDirection(row, col, 1, 0)) {
                sosLines.add(new int[]{row, col, 1, 0});
            }
            if (!isSimpleGame && game.checkDirection(row, col, 0, 1)) {
                sosLines.add(new int[]{row, col, 0, 1});
            }
            if (!isSimpleGame && game.checkDirection(row, col, 1, 1)) {
                sosLines.add(new int[]{row, col, 1, 1});
            }
            if (!isSimpleGame && game.checkDirection(row, col, 1, -1)) {
                sosLines.add(new int[]{row, col, 1, -1});
            }
            overlayPanel.repaint();

            if (game.checkWinner()) {
                gameOver = true;
                disableBoard();
                highlightWinningSOS();

                String resultMessage;
                if (game instanceof SimpleGame && ((SimpleGame) game).countSOS() == 0) {
                    resultMessage = "Draw! No winner.";
                }else if (game instanceof GeneralGame) {
                    resultMessage = ((GeneralGame) game).getWinner();
                }
                else {
                    resultMessage = isSimpleGame ? (isBlueTurn ? "Red Wins!" : "Blue Wins!") :
                            ((GeneralGame) game).getWinner();
                }
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
        overlayPanel.setVisible(true);
        overlayPanel.repaint();
    }

    private void drawWinningLines(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        g2.setColor(isBlueTurn ? Color.RED : Color.BLUE);

        for (int[] line : sosLines) {
            int row = line[0];
            int col = line[1];
            int dx = line[2];
            int dy = line[3];

            // 중심 O 기준으로 좌우 S를 그리기 위해 좌표 계산
            int row1 = row - dx;
            int col1 = col - dy;
            int row2 = row + dx;
            int col2 = col + dy;

            drawLineOnGrid(g2, row1, col1, row2, col2);
        }

        if (gameOver && isSimpleGame) {
            g2.setColor(isBlueTurn ? Color.RED : Color.BLUE);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (game.checkDirection(i, j, 1, 0)) { // 가로 (좌 → 우)
                    System.out.println("→ Drawing Horizontal Line");

                    drawLineOnGrid(g2, i - 1, j, i + 1, j);
                }
                if (game.checkDirection(i, j, 0, 1)) { // 세로 (위 → 아래)
                    System.out.println("↓ Drawing Vertical Line");

                    drawLineOnGrid(g2, i, j - 1, i, j + 1);
                }
                if (game.checkDirection(i, j, 1, 1)) { // 대각선 (\)
                    System.out.println("↘ Drawing Diagonal Line (Top-Left to Bottom-Right)");
                    drawLineOnGrid(g2, i - 1, j - 1, i + 1, j + 1);
                }
                if (game.checkDirection(i, j, 1, -1)) { // 대각선 (/)
                    System.out.println("↙ Drawing Diagonal Line (Top-Right to Bottom-Left)");
                    drawLineOnGrid(g2, i - 1, j + 1, i + 1, j - 1);
                }
            }
        }
        }
    }


    private void drawLineOnGrid(Graphics2D g2, int row1, int col1, int row2, int col2) {
        int cellWidth = overlayPanel.getWidth() / boardSize;
        int cellHeight = overlayPanel.getHeight() / boardSize;

        // **각 셀의 중앙 좌표 계산 (오프셋 조정)**
        int x1 = (col1 * cellWidth) + (cellWidth / 2);
        int y1 = (row1 * cellHeight) + (cellHeight / 2);
        int x2 = (col2 * cellWidth) + (cellWidth / 2);
        int y2 = (row2 * cellHeight) + (cellHeight / 2);


        // **보정값 추가해서 그리드 정확한 중앙에 선이 그려지도록 조정**
        g2.drawLine(x1, y1, x2, y2);
    }

}
