package com.YunbinGil.sos;

public class SimpleGame extends SosGame {

    public SimpleGame(int size) {
        super(size);
    }

    @Override
    public boolean checkWinner() {
        if (countSOS() > 0) {
            return true;  // SOS가 하나라도 있으면 승자 발생
        }

        // 🏁 빈 칸이 없으면 무승부
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == ' ') {
                    return false;  // 빈 칸이 남아 있으면 게임 계속 진행
                }
            }
        }
        return true;  // 빈 칸이 없으면 무승부로 게임 종료
    }
    @Override
    public String getWinner() {
        return countSOS() == 0 ? "Draw! No winner." : (!isBlueTurn ? "Red Wins!" : "Blue Wins!");
    }
}
