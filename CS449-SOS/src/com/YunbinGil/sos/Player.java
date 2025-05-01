package com.YunbinGil.sos;


public abstract class Player {
    protected boolean isBlue;

    public Player(boolean isBlue) {
        this.isBlue = isBlue;
    }

    public boolean isBluePlayer() {
        return isBlue;
    }

    public abstract Move decideMove(char[][] board);

    public static class Move {
        public int row, col;
        public char letter;
        public Move(int row, int col, char letter) {
            this.row = row;
            this.col = col;
            this.letter = letter;
        }
    }
}

