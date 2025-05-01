package com.YunbinGil.sos;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SosGUI extends JFrame {
    private SosGame game;
    private SosGameController controller;
    private JButton[][] buttons;
    private JPanel boardPanel;
    private JPanel overlayPanel;
    private JLayeredPane layeredPane;
    private boolean gameOver = false;
    private JLabel currentTurnLabel;
    private JComboBox<String> boardSizeBox;
    private JComboBox<String> modeBox;
    private JComboBox<String> bluePlayerTypeBox;
    private JComboBox<String> redPlayerTypeBox;
    private boolean allowComputerLoop = false;
    private Timer computerLoopTimer = null; //to Prevent duplicate runs
    JCheckBox recordCheckBox;


    public SosGUI() {
        setTitle("SOS Game");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setupTopPanel();
        setupPlayerSelectors();

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        add(layeredPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void setupTopPanel() {
        JPanel topPanel = new JPanel();
        boardSizeBox = new JComboBox<>(new String[]{"3", "5", "8"});
        modeBox = new JComboBox<>(new String[]{"Simple", "General"});
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> {
            startNewGame();
        });
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

        recordCheckBox = new JCheckBox("Record");
        recordCheckBox.setSelected(false); // 기본 체크 안 함
        panel.add(recordCheckBox);
        add(panel, BorderLayout.NORTH);
    }

    private void setupBoard(int size) {
        if (boardPanel != null) layeredPane.remove(boardPanel);
        if (overlayPanel != null) layeredPane.remove(overlayPanel);

        boardPanel = new JPanel(new GridLayout(size, size));
        buttons = new JButton[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 24));
                buttons[i][j].addActionListener(e -> {
                    placeLetter(row, col);

                });
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
        overlayPanel.setLayout(null);  // overlay는 자유 배치

        // 창 크기 기반 위치 계산 (중앙 정렬)
        int windowWidth = getContentPane().getWidth();
        int windowHeight = getContentPane().getHeight();
        int usableHeight = windowHeight - 125; // 위·아래 컨트롤 여백 제외
        int usableSize = Math.min(windowWidth, usableHeight);

        int cellSize = usableSize / size;
        int panelSize = cellSize * size;

        int xOffset = (windowWidth - panelSize) / 2;
        int yOffset = (usableHeight - panelSize) / 2;

        boardPanel.setBounds(xOffset, yOffset, panelSize, panelSize);
        overlayPanel.setBounds(xOffset, yOffset, panelSize, panelSize);

        layeredPane.add(boardPanel, Integer.valueOf(0));
        layeredPane.add(overlayPanel, Integer.valueOf(1));
        revalidate();
        repaint();
    }



    private void startNewGame() {
        if (computerLoopTimer != null) {
            computerLoopTimer.stop();  // ✅ 기존 타이머 중단
            computerLoopTimer = null;
        }

        int size = Integer.parseInt((String) boardSizeBox.getSelectedItem());
        boolean isSimple = modeBox.getSelectedItem().equals("Simple");
        game = isSimple ? new SimpleGame(size) : new GeneralGame(size);
        controller = new SosGameController(game);

        if (recordCheckBox.isSelected()) {
            controller.enableRecording("recorded_game.txt");
            System.out.println("📼 Recording will start with this game.");
        }

        boolean blueIsComputer = bluePlayerTypeBox.getSelectedItem().equals("Computer");
        boolean redIsComputer = redPlayerTypeBox.getSelectedItem().equals("Computer");
        controller.setPlayerTypes(blueIsComputer, redIsComputer);

        gameOver = false;
        updateCurrentTurnLabel();

        setupBoard(size);
        repaintOverlay();

        if (blueIsComputer || redIsComputer) {
            SwingUtilities.invokeLater(this::runComputerLoop);  // ✅ 자동 루프 시작
        }

    }
    private void runComputerLoop() {
        if (controller == null || controller.isGameOver()) return;

        boolean isBlueTurn = controller.getGame().isBlueTurn();
        boolean isComputer = (isBlueTurn && controller.isBlueComputer()) || (!isBlueTurn && controller.isRedComputer());

        if (!isComputer) return;

        boolean before = isBlueTurn;
        ComputerPlayer.Move move = controller.handleComputerTurn(isBlueTurn);
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

            if (recordCheckBox.isSelected()) {
                String filename = JOptionPane.showInputDialog(this, "Enter filename to save the game:", "Save Game Record", JOptionPane.PLAIN_MESSAGE);

                if (filename != null && !filename.trim().isEmpty()) {
                    if (!filename.endsWith(".txt")) {
                        filename += ".txt";
                    }
                    controller.saveToCustomFile(filename);
                }
            }


            return;
        }

        // SOS로 턴이 안 바뀌었다면 루프 중단
        if (controller.getGame().isBlueTurn() == before) return;

        // 다음 턴도 computer면 500ms 후 재귀
        computerLoopTimer = new Timer(500, e -> runComputerLoop());
        computerLoopTimer.setRepeats(false);
        computerLoopTimer.start();
    }


    private void placeLetter(int row, int col) {
        if (game == null || !game.isCellEmpty(row, col) || gameOver) return;

        String[] choices = {"S", "O"};
        String letter = (String) JOptionPane.showInputDialog(this, "Choose a letter:",
                "Move", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

        if (letter != null) {
            boolean isBlueTurn = controller.getGame().isBlueTurn();
            controller.handleMove(row, col, letter.charAt(0), isBlueTurn);
            buttons[row][col].setText(String.valueOf(controller.getGame().getLetter(row, col)));
            if (controller.getGame().isGeneralMode()) {
                highlightWinningSOS();
                repaintOverlay();
            }

            if (controller.isGameOver()) {
                gameOver = true;
                disableBoard();
                highlightWinningSOS();
                JOptionPane.showMessageDialog(this, controller.getResultMessage(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            game.placeLetter(row, col, letter.charAt(0));
            updateCurrentTurnLabel();
            boolean nextIsComputer = (controller.getGame().isBlueTurn() && controller.isBlueComputer())
                    || (!controller.getGame().isBlueTurn() && controller.isRedComputer());

            if (nextIsComputer && !controller.isGameOver()) {
                runComputerLoop();
            }
        }
    }

    private void updateCurrentTurnLabel() {
        boolean isBlue = controller.getGame().isBlueTurn();
        currentTurnLabel.setText("Current Turn: " + (isBlue ? "Blue" : "Red"));
        System.out.println("turn is changed to "+ (isBlue ? "Blue" : "Red"));
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
        if (!game.isGeneralMode()) {
            Color winnerColor = controller.getResultMessage().contains("Blue") ? Color.BLUE : Color.RED;

            controller.getSosLines().clear();
            for (int i = 0; i < controller.getGame().getBoard().length; i++) {
                for (int j = 0; j < controller.getGame().getBoard()[0].length; j++) {
                    for (int[] d : SosGame.DIRECTIONS) {
                        if (game.checkDirection(i, j, d[0], d[1])) {
                            controller.getSosLines().add(new SosLine(i, j, d[0], d[1], winnerColor));
                        }
                    }
                }
            }
        }
        overlayPanel.repaint();
    }

    private void drawWinningLines(Graphics g) {
        if (controller == null) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5));
        List<SosLine> sosLines = controller.getSosLines();

        for (SosLine line : sosLines) {
            g2.setColor(line.color);

            int row1 = line.row - line.dx;
            int col1 = line.col - line.dy;
            int row2 = line.row + line.dx;
            int col2 = line.col + line.dy;

            if (inBounds(row1, col1) && inBounds(row2, col2)) {
                JButton b1 = buttons[row1][col1];
                JButton b2 = buttons[row2][col2];
                Point p1 = SwingUtilities.convertPoint(b1.getParent(), b1.getLocation(), overlayPanel);
                Point p2 = SwingUtilities.convertPoint(b2.getParent(), b2.getLocation(), overlayPanel);

                int x1 = p1.x + b1.getWidth() / 2;
                int y1 = p1.y + b1.getHeight() / 2;
                int x2 = p2.x + b2.getWidth() / 2;
                int y2 = p2.y + b2.getHeight() / 2;

                g2.drawLine(x1, y1, x2, y2);
            }
        }
    }
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < buttons.length && col >= 0 && col < buttons[0].length;
    }
}