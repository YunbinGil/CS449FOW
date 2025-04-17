package com.YunbinGil.sos;

import java.awt.*;

public class SosGameController {
    private SosGame game;
    public boolean blueIsComputer = false;
    public boolean redIsComputer = false;
    private java.util.List<SosLine> sosLines = new java.util.ArrayList<>();
    private boolean gameOver = false;

    public SosGameController(SosGame game) {
        this.game = game;
    }

    public void setPlayerTypes(boolean blueIsComputer, boolean redIsComputer) {
        this.blueIsComputer = blueIsComputer;
        this.redIsComputer = redIsComputer;
    }

    public void handleMove(int row, int col, char letter, boolean isBlueTurn) {
        if (game.isCellEmpty(row, col) && !gameOver) {
            game.placeLetter(row, col, letter);
            for (int i = 0; i < game.boardSize; i++) {
                for (int j = 0; j < game.boardSize; j++) {
                    for (int[] d : SosGame.DIRECTIONS) {
                        addLineIfNew(i, j, d[0], d[1], isBlueTurn ? Color.BLUE : Color.RED);
                    }
                }
            }
            if (game.checkWinner()) {
                gameOver = true;
            }
        }
    }

    public ComputerPlayer.Move handleComputerTurn(boolean isBlue) {
        if ((isBlue && !blueIsComputer) || (!isBlue && !redIsComputer)) return null;

        ComputerPlayer computer = new ComputerPlayer();
        ComputerPlayer.Move move = computer.decideMove(game.getBoard());

        if (move != null) {
            System.out.println("🤖 Computer moves: " + move.row + "," + move.col + " = " + move.letter);
            game.placeLetter(move.row, move.col, move.letter);
            for (int i = 0; i < game.boardSize; i++) {
                for (int j = 0; j < game.boardSize; j++) {
                    for (int[] d : SosGame.DIRECTIONS) {
                        addLineIfNew(i, j, d[0], d[1], isBlue ? Color.BLUE : Color.RED);
                    }
                }
            }
            if (game.checkWinner()) {
                    gameOver = true;
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

    public java.util.List<SosLine> getSosLines() {
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
}