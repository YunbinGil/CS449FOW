package com.YunbinGil.sos;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SosGameController {
    private SosGame game;
    private List<SosLine> sosLines;
    private boolean gameOver;
    private ComputerPlayer blueAI;
    private ComputerPlayer redAI;
    private boolean blueIsComputer;
    private boolean redIsComputer;

    public SosGameController(SosGame game) {
        this.game = game;
        this.sosLines = new ArrayList<>();
        this.gameOver = false;
        this.blueAI = new ComputerPlayer();
        this.redAI = new ComputerPlayer();
    }

    public void setPlayerTypes(boolean blueIsComputer, boolean redIsComputer) {
        this.blueIsComputer = blueIsComputer;
        this.redIsComputer = redIsComputer;
    }

    public void resetGame(SosGame newGame) {
        this.game = newGame;
        this.sosLines.clear();
        this.gameOver = false;
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

    public List<SosLine> getSosLines() {
        return sosLines;
    }

    public void handleMove(int row, int col, char letter, boolean isBlueTurn) {
        if (game.isCellEmpty(row, col) && !gameOver) {
            game.placeLetter(row, col, letter);
            if (!game.isGeneralMode()) {
                if (game.checkWinner()) {
                    addSimpleWinningLines(isBlueTurn);
                    gameOver = true;
                    return;
                }
            } else {
                addGeneralSosLines(row, col, isBlueTurn);
                if (game.checkWinner()) {
                    gameOver = true;
                    return;
                }
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
            sosLines.addAll(game.checkAllDirections(move.row, move.col, isBlue ? Color.BLUE : Color.RED));
        }else{
            System.out.println("🤖 No move available.");
        }

        return move;
    }


    private void addGeneralSosLines(int row, int col, boolean isBlueTurn) {
        if (game.checkDirection(row, col, 1, 0)) sosLines.add(new SosLine(row, col, 1, 0, isBlueTurn ? java.awt.Color.BLUE : java.awt.Color.RED));
        if (game.checkDirection(row, col, 0, 1)) sosLines.add(new SosLine(row, col, 0, 1, isBlueTurn ? java.awt.Color.BLUE : java.awt.Color.RED));
        if (game.checkDirection(row, col, 1, 1)) sosLines.add(new SosLine(row, col, 1, 1, isBlueTurn ? java.awt.Color.BLUE : java.awt.Color.RED));
        if (game.checkDirection(row, col, 1, -1)) sosLines.add(new SosLine(row, col, 1, -1, isBlueTurn ? java.awt.Color.BLUE : java.awt.Color.RED));
    }

    private void addSimpleWinningLines(boolean isBlueTurn) {
        for (int i = 0; i < game.getBoard().length; i++) {
            for (int j = 0; j < game.getBoard()[0].length; j++) {
                if (game.checkDirection(i, j, 1, 0)) sosLines.add(new SosLine(i, j, 1, 0, isBlueTurn ? java.awt.Color.BLUE : java.awt.Color.RED));
                if (game.checkDirection(i, j, 0, 1)) sosLines.add(new SosLine(i, j, 0, 1, isBlueTurn ? java.awt.Color.BLUE : java.awt.Color.RED));
                if (game.checkDirection(i, j, 1, 1)) sosLines.add(new SosLine(i, j, 1, 1, isBlueTurn ? java.awt.Color.BLUE : java.awt.Color.RED));
                if (game.checkDirection(i, j, 1, -1)) sosLines.add(new SosLine(i, j, 1, -1, isBlueTurn ? java.awt.Color.BLUE : java.awt.Color.RED));
            }
        }
    }
}