package com.YunbinGil.sos;

public abstract class SosGame {
    protected char[][] board;
    protected int boardSize;
    public int sosCountBlue;
    public int sosCountRed;
    protected boolean isBlueTurn = true;
    protected static final int[][] DIRECTIONS = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};


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
            if (!checkSOS(row, col)) {
                isBlueTurn = !isBlueTurn;  // 턴 전환
            }
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
        sosCountBlue = 0;
        sosCountRed = 0;
        isBlueTurn = true;
    }

    protected boolean checkSOS(int row, int col) {
        return countSOS() > 0;  // 한 번이라도 SOS가 만들어졌는지 확인
    }

    protected int countSOS() {
        int count = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                for (int[] d : DIRECTIONS) {
                    if (checkDirection(i, j, d[0], d[1])) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }


    public boolean checkDirection(int row, int col, int dx, int dy) {
        // 🚨 배열 범위 초과 방지 코드
        int row1 = row - dx, col1 = col - dy; // 첫 번째 'S' 위치
        int row2 = row + dx, col2 = col + dy; // 두 번째 'S' 위치

        // 🚨 배열 범위 초과 방지
        if (row1 < 0 || row1 >= boardSize || col1 < 0 || col1 >= boardSize ||
                row2 < 0 || row2 >= boardSize || col2 < 0 || col2 >= boardSize) {
            return false; // 범위를 벗어나면 false 반환 (예외 방지)
        }

        return board[row][col] == 'O' &&
                board[row1][col1] == 'S' &&
                board[row2][col2] == 'S';
    }

    public void initializeBoard() {
        board = new char[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = ' '; // 빈 칸을 공백 문자로 채움
            }
        }
    }

    public char[][] getBoard() {
        return board;
    }
    public boolean isBlueTurn() {
        return isBlueTurn;
    }

    public boolean isGeneralMode() {
        return this instanceof GeneralGame;
    }


    // 자식 클래스에서 구현할 메서드
    public abstract boolean checkWinner();
    public abstract String getWinner();
}