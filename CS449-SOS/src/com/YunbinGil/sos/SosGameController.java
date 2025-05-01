package com.YunbinGil.sos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SosGameController {
    private SosGame game;
    private boolean gameOver = false;

    private boolean blueIsComputer = false;
    private boolean redIsComputer = false;

    private Player bluePlayer;
    private Player redPlayer;

    private List<SosLine> sosLines = new ArrayList<>();

    private RecordManager recorder;
    private boolean recording = false;

    public SosGameController(SosGame game) {
        this.game = game;
    }

    public void setPlayerTypes(boolean blueIsComputer, boolean redIsComputer) {
        this.blueIsComputer = blueIsComputer;
        this.redIsComputer = redIsComputer;

        // ✅ 다형성 기반으로 Player 객체 할당
        this.bluePlayer = blueIsComputer ? new ComputerPlayer(true) : new HumanPlayer(true);
        this.redPlayer = redIsComputer ? new ComputerPlayer(false) : new HumanPlayer(false);
    }

    public void handleMove(int row, int col, char letter, boolean isBlueTurn) {
        if (game.isCellEmpty(row, col) && !gameOver) {
            game.placeLetter(row, col, letter);

            // ✅ 기록하기
            if (recording) {
                String color = isBlueTurn ? "BLUE" : "RED";
                String mode = game instanceof SimpleGame ? "SIMPLE" : "GENERAL";
                recorder.recordMove(color, row, col, letter, mode);
            }

            for (int i = 0; i < game.boardSize; i++) {
                for (int j = 0; j < game.boardSize; j++) {
                    for (int[] d : SosGame.DIRECTIONS) {
                        addLineIfNew(i, j, d[0], d[1], isBlueTurn ? Color.BLUE : Color.RED);
                    }
                }
            }
            if (game.checkWinner()) {
                gameOver = true;

                // ✅ 게임 끝나면 저장
                if (recording) {
                    recorder.saveToFile();
                    System.out.println("✅ Game recorded.");
                }
            }
        }
    }

    // ✅ 다형성을 이용한 컴퓨터 턴 처리
    public Player.Move handleComputerTurn(boolean isBlue) {
        Player currentPlayer = isBlue ? bluePlayer : redPlayer;

        if (!(currentPlayer instanceof ComputerPlayer)) return null;

        Player.Move move = currentPlayer.decideMove(game.getBoard());

        if (move != null) {
            System.out.println("🤖 Computer moves: " + move.row + "," + move.col + " = " + move.letter);
            game.placeLetter(move.row, move.col, move.letter);

            // ✅ 기록하기
            if (recording) {
                String color = isBlue ? "BLUE" : "RED";
                String mode = game instanceof SimpleGame ? "SIMPLE" : "GENERAL";
                recorder.recordMove(color, move.row, move.col, move.letter, mode);
            }

            for (int i = 0; i < game.boardSize; i++) {
                for (int j = 0; j < game.boardSize; j++) {
                    for (int[] d : SosGame.DIRECTIONS) {
                        addLineIfNew(i, j, d[0], d[1], isBlue ? Color.BLUE : Color.RED);
                    }
                }
            }
            if (game.checkWinner()) {
                gameOver = true;

                // ✅ 게임 끝나면 저장
                if (recording) {
                    recorder.saveToFile();
                    System.out.println("✅ Game recorded.");
                }
            }
        } else {
            System.out.println("🤖 No move available.");
        }

        return move;
    }

    private void addLineIfNew(int row, int col, int dx, int dy, Color color) {
        if (game.checkDirection(row, col, dx, dy)) {
            for (SosLine line : sosLines) {
                if (line.row == row && line.col == col && line.dx == dx && line.dy == dy) {
                    return; // 중복 방지
                }
            }
            sosLines.add(new SosLine(row, col, dx, dy, color));
            if (color == Color.BLUE) game.sosCountBlue++;
            else game.sosCountRed++;
        }
    }

    // 🔻 Getter 메서드들
    public List<SosLine> getSosLines() {
        return sosLines;
    }

    public SosGame getGame() {
        return game;
    }

    public boolean isBlueComputer() {
        return blueIsComputer;
    }

    public boolean isRedComputer() {
        return redIsComputer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getResultMessage() {
        return game.getWinner();
    }

    public void enableRecording(String fileName) {
        recorder = new RecordManager(fileName);
        recording = true;
    }
}
