package com.YunbinGil.sos;

public class HumanPlayer extends Player {
    public HumanPlayer(boolean isBlue) {
        super(isBlue);
    }

    @Override
    public Move decideMove(char[][] board) {
        return null; // 실제 클릭으로 동작하므로 null
    }
}