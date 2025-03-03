package com.YunbinGil.sos;

public class SosGame {
    private char[][] board;
    private int boardSize;

    public SosGame(int size) {
        this.boardSize = size;
        board = new char[boardSize][boardSize];
    }

    public boolean isCellEmpty(int row, int col) {
        return board[row][col] == '\0';
    }

    public void placeLetter(int row, int col, char letter) {
        if (isCellEmpty(row, col)) {
            board[row][col] = letter;
        }
    }

    public char getLetter(int row, int col) {
        return board[row][col];
    }

    public void resetBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = '\0';
            }
        }
    }
}
