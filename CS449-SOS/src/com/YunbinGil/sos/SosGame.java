package com.YunbinGil.sos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class SosGame {
    protected char[][] board;
    protected int boardSize;
    public int sosCountBlue=0;
    public int sosCountRed=0;
    protected boolean isBlueTurn = true;
    protected static final int[][] DIRECTIONS = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};


    public SosGame(int size) {
        this.boardSize = size;
        board = new char[boardSize][boardSize];
        initializeBoard();
    }

    public boolean isCellEmpty(int row, int col) {
        return board[row][col] == ' ';
    }

    public void placeLetter(int row, int col, char letter) {
        if (isCellEmpty(row, col)) {
            board[row][col] = letter;
            if (this instanceof SimpleGame){
                if (!checkSOS(row, col)) {
                    isBlueTurn = !isBlueTurn;  // 턴 전환
                }
            }else{
                isBlueTurn = !isBlueTurn;
            }

        }
    }

    public char getLetter(int row, int col) {
        return board[row][col];
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
    public List<SosLine> checkAllDirections(int row, int col, Color color) {
        List<SosLine> found = new ArrayList<>();
        if (checkDirection(row, col, 1, 0)) found.add(new SosLine(row, col, 1, 0, color));
        if (checkDirection(row, col, 0, 1)) found.add(new SosLine(row, col, 0, 1, color));
        if (checkDirection(row, col, 1, 1)) found.add(new SosLine(row, col, 1, 1, color));
        if (checkDirection(row, col, 1, -1)) found.add(new SosLine(row, col, 1, -1, color));
        return found;
    }


    public void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = ' '; // 빈 칸을 공백 문자로 채움
            }
        }
        sosCountBlue = 0;
        sosCountRed = 0;
        isBlueTurn = true;
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