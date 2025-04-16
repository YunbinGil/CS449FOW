package com.YunbinGil.sos;

import java.util.*;

public class ComputerPlayer {
    private static final int[][] DIRECTIONS = {
            {1, 0}, {0, 1}, {1, 1}, {1, -1}
    };

    public static class Move {
        public int row, col;
        public char letter;
        public Move(int row, int col, char letter) {
            this.row = row;
            this.col = col;
            this.letter = letter;
        }
    }

    public Move decideMove(char[][] board) {
        int n = board.length;
        System.out.println("📋 Computer analyzing board:");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print("[" + board[i][j] + "]");
            }
            System.out.println();
        }

        // 🔒 1. Block potential SOS: S O _ / _ O S / S _ S
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int[] d : DIRECTIONS) {
                    int dx = d[0], dy = d[1];

                    // Pattern: S O _
                    if (valid(i, j, n) && board[i][j] == 'S') {
                        int x1 = i + dx, y1 = j + dy;
                        int x2 = i + 2 * dx, y2 = j + 2 * dy;
                        if (valid(x1, y1, n) && board[x1][y1] == 'O'
                                && valid(x2, y2, n) && board[x2][y2] == ' ') {
                            return new Move(x2, y2, 'S');
                        }
                    }

                    // Pattern: _ O S
                    if (valid(i, j, n) && board[i][j] == ' ') {
                        int x1 = i + dx, y1 = j + dy;
                        int x2 = i + 2 * dx, y2 = j + 2 * dy;
                        if (valid(x1, y1, n) && board[x1][y1] == 'O'
                                && valid(x2, y2, n) && board[x2][y2] == 'S') {
                            return new Move(i, j, 'S');
                        }
                    }

                    // Pattern: S _ S
                    if (valid(i, j, n) && board[i][j] == 'S') {
                        int x1 = i + dx, y1 = j + dy;
                        int x2 = i + 2 * dx, y2 = j + 2 * dy;
                        if (valid(x1, y1, n) && board[x1][y1] == ' '
                                && valid(x2, y2, n) && board[x2][y2] == 'S') {
                            return new Move(x1, y1, 'O');
                        }
                    }
                }
            }
        }

        // 🎲 2. No threat detected → pick random empty cell
        List<Move> candidates = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == ' ') {
                    candidates.add(new Move(i, j, Math.random() < 0.5 ? 'S' : 'O'));
                }
            }
        }

        return candidates.isEmpty() ? null : candidates.get(new Random().nextInt(candidates.size()));
    }

    private boolean valid(int x, int y, int n) {
        return x >= 0 && x < n && y >= 0 && y < n;
    }
}
