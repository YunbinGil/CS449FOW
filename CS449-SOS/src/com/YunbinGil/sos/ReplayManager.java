package com.YunbinGil.sos;

import java.io.*;
import java.util.*;

public class ReplayManager {
    public static class ReplayMove {
        int row, col;
        char letter;
        boolean isBlue;

        public ReplayMove(int row, int col, char letter, boolean isBlue) {
            this.row = row;
            this.col = col;
            this.letter = letter;
            this.isBlue = isBlue;
        }
    }

    public List<ReplayMove> loadReplayFile(String fileName) {
        List<ReplayMove> moves = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int row = Integer.parseInt(parts[2]);
                int col = Integer.parseInt(parts[3]);
                char letter = parts[4].charAt(0);
                boolean isBlue = parts[1].equalsIgnoreCase("BLUE");
                moves.add(new ReplayMove(row, col, letter, isBlue));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return moves;
    }
}
