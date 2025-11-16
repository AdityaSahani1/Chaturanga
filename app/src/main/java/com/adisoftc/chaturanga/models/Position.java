package com.adisoftc.chaturanga.models;

public class Position {
    private int row;
    private int col;
    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public boolean isValid() {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
    
    public String toNotation() {
        char file = (char)('a' + col);
        int rank = row + 1;
        return "" + file + rank;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.col == other.col;
    }
    
    @Override
    public int hashCode() {
        return row * 8 + col;
    }
    
    @Override
    public String toString() {
        return toNotation();
    }
}
