package com.YunbinGil.sos;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecordManager {
    private List<String> log = new ArrayList<>();
    private int turn = 1;

    private final String fileName;

    public RecordManager(String fileName) {
        this.fileName = fileName;
    }

    public void recordMove(String playerColor, int row, int col, char letter, String mode) {
        String line = turn + "," + playerColor + "," + row + "," + col + "," + letter + "," + mode;
        log.add(line);
        turn++;
    }

    public void saveToFile() {
        System.out.println("📁 Saving to file: " + fileName);
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String line : log) {
                writer.write(line + "\n");
            }
            System.out.println("📁 File write complete!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToSpecificFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (String line : log) {
                writer.write(line + "\n");
            }
            System.out.println("📁 Saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        log.clear();
        turn = 1;
    }
}
