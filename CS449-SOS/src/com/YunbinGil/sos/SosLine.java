package com.YunbinGil.sos;

import java.awt.Color;

public class SosLine {
    public final int row, col, dx, dy;
    public final Color color;

    public SosLine(int row, int col, int dx, int dy, Color color) {
        this.row = row;
        this.col = col;
        this.dx = dx;
        this.dy = dy;
        this.color = color;
    }
}