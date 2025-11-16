package com.adisoftc.chaturanga.models;

public enum Player {
    SOUTH(0, "South", 0xFFD4AF37),
    WEST(1, "West", 0xFF8B4513),
    NORTH(2, "North", 0xFF2F4F4F),
    EAST(3, "East", 0xFF8B0000),
    
    WHITE(0, "White", 0xFFFFFFFF),
    BLACK(1, "Black", 0xFF000000);
    
    private final int id;
    private final String name;
    private final int color;
    
    Player(int id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getColor() {
        return color;
    }
    
    public Player getNextPlayer(GameMode mode) {
        if (mode == GameMode.MODERN_CHESS || mode == GameMode.PERSIAN_SHATRANJ) {
            return this == WHITE ? BLACK : WHITE;
        } else {
            switch (this) {
                case SOUTH: return WEST;
                case WEST: return NORTH;
                case NORTH: return EAST;
                case EAST: return SOUTH;
                default: return SOUTH;
            }
        }
    }
    
    public Player getAlly() {
        switch (this) {
            case SOUTH: return NORTH;
            case NORTH: return SOUTH;
            case EAST: return WEST;
            case WEST: return EAST;
            default: return null;
        }
    }
    
    public boolean isAlly(Player other) {
        return this.getAlly() == other;
    }
}
