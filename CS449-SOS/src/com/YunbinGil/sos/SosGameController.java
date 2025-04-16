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

            if (!game.isGeneralMode()) {
                addSimpleWinningLines(isBlueTurn);
            } else {
                sosLines.addAll(game.checkAllDirections(row, col, isBlueTurn ? Color.BLUE : Color.RED));
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

            if (!game.isGeneralMode()) {
                addSimpleWinningLines(isBlue);
                if (game.checkWinner()) {
                    gameOver = true;
                }
            } else {
                sosLines.addAll(game.checkAllDirections(move.row, move.col, isBlue ? Color.BLUE : Color.RED));
                if (game.checkWinner()) {
                    gameOver = true;
                }
            }
        } else {
            System.out.println("🤖 No move available.");
        }

        return move;
    }

    private void addSimpleWinningLines(boolean isBlue) {
        for (int i = 0; i < game.getBoard().length; i++) {
            for (int j = 0; j < game.getBoard()[0].length; j++) {
                if (game.checkDirection(i, j, 1, 0)) sosLines.add(new SosLine(i, j, 1, 0, isBlue ? Color.BLUE : Color.RED));
                if (game.checkDirection(i, j, 0, 1)) sosLines.add(new SosLine(i, j, 0, 1, isBlue ? Color.BLUE : Color.RED));
                if (game.checkDirection(i, j, 1, 1)) sosLines.add(new SosLine(i, j, 1, 1, isBlue ? Color.BLUE : Color.RED));
                if (game.checkDirection(i, j, 1, -1)) sosLines.add(new SosLine(i, j, 1, -1, isBlue ? Color.BLUE : Color.RED));
            }
        }
    }
    private void addGeneralSosLines(int row, int col, boolean isBlueTurn) {
        if (game.checkDirection(row, col, 1, 0)) sosLines.add(new SosLine(row, col, 1, 0, isBlueTurn ? java.awt.Color.BLUE : java.awt.Color.RED));
        if (game.checkDirection(row, col, 0, 1)) sosLines.add(new SosLine(row, col, 0, 1, isBlueTurn ? java.awt.Color.BLUE : java.awt.Color.RED));
        if (game.checkDirection(row, col, 1, 1)) sosLines.add(new SosLine(row, col, 1, 1, isBlueTurn ? java.awt.Color.BLUE : java.awt.Color.RED));
        if (game.checkDirection(row, col, 1, -1)) sosLines.add(new SosLine(row, col, 1, -1, isBlueTurn ? java.awt.Color.BLUE : java.awt.Color.RED));
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