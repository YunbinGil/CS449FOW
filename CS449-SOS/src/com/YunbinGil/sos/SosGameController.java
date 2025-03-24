package com.YunbinGil.sos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SosGameController {
    private SosGame game;
    private final List<SosLine> sosLines = new ArrayList<>();

    public SosGameController(SosGame game) {
        this.game = game;
    }

    public SosGame getGame() {
        return game;
    }

    public void resetGame(SosGame newGame) {
        this.game = newGame;
        sosLines.clear();
    }

    public List<SosLine> getSosLines() {
        return sosLines;
    }

    public void handleMove(int row, int col, char letter, boolean isBlueTurn) {
        game.placeLetter(row, col, letter);
        Color color = isBlueTurn ? Color.BLUE : Color.RED;

        // GeneralGame: SOS 탐색 후 선 추가
        if (game.isGeneralMode()) {
            for (int i = 0; i < game.boardSize; i++) {
                for (int j = 0; j < game.boardSize; j++) {
                    for (int[] d : SosGame.DIRECTIONS) {
                        addLineIfNew(i, j, d[0], d[1], color);
                    }
                }
            }
        }
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

    public boolean isGameOver() {
        return game.checkWinner();
    }

    public String getResultMessage() {
        return game.getWinner();
    }
}