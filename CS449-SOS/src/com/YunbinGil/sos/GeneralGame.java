package com.YunbinGil.sos;

public class GeneralGame extends SosGame {

    public GeneralGame(int size) {
        super(size);
    }

    @Override
    public boolean checkWinner() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == ' ') {
                    return false; // 빈 칸이 남아 있으면 게임 계속 진행
                }
            }
        }
        return true; // 보드가 꽉 차면 게임 종료
    }

    public String getWinner() {
        System.out.println("🔍 Blue SOS Count: " + sosCountBlue);
        System.out.println("🔍 Red SOS Count: " + sosCountRed);
        if (sosCountBlue > sosCountRed) return "Blue Wins!";
        else if (sosCountRed > sosCountBlue) return "Red Wins!";
        else return "Draw!";
    }
}
