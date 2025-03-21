package com.YunbinGil.sos;

public class SimpleGame extends SosGame {

    public SimpleGame(int size) {
        super(size);
    }

    @Override
    public boolean checkWinner() {
        return countSOS() > 0;
    }
}
