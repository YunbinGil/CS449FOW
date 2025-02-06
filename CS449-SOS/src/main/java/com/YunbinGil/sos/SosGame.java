package main.java.com.YunbinGil.sos;

public class SosGame {
    private char[][] board;

    public SosGame(int size) {
        board = new char[size][size];
    }

    public void placeLetter(int row, int col, char letter) {
        if (board[row][col] == '\0') {
            board[row][col] = letter;
        }
    }

    public char getLetter(int row, int col) {
        return board[row][col];
    }

    public boolean checkWin() {
        // (간단한 예시) 만약 첫 행이 "SOS"이면 승리
        return board[0][0] == 'S' && board[0][1] == 'O' && board[0][2] == 'S';
    }
}
