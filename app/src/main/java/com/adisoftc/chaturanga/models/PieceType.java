package com.adisoftc.chaturanga.models;

public enum PieceType {
    RAJA(1, "King"),
    RATHA(2, "Chariot"),
    GAJA(3, "Elephant"),
    ASHVA(4, "Horse"),
    PADATI(5, "Pawn"),
    
    QUEEN(6, "Queen"),
    BISHOP(7, "Bishop"),
    KNIGHT(8, "Knight"),
    ROOK(9, "Rook"),
    CHESS_PAWN(10, "Pawn");
    
    private final int diceValue;
    private final String displayName;
    
    PieceType(int diceValue, String displayName) {
        this.diceValue = diceValue;
        this.displayName = displayName;
    }
    
    public int getDiceValue() {
        return diceValue;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static PieceType fromDiceValue(int value) {
        for (PieceType type : values()) {
            if (type.diceValue == value) {
                return type;
            }
        }
        return null;
    }
}
