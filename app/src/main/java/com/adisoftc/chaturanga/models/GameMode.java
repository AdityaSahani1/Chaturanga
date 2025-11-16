package com.adisoftc.chaturanga.models;

public enum GameMode {
    INDIAN_4P_DICE("4-Player Chaturanga (Dice)", 4, true),
    INDIAN_4P_STRATEGIC("4-Player Chaturanga (Strategic)", 4, false),
    PERSIAN_SHATRANJ("Persian Shatranj", 2, false),
    MODERN_CHESS("Modern Chess", 2, false);
    
    private final String displayName;
    private final int playerCount;
    private final boolean useDice;
    
    GameMode(String displayName, int playerCount, boolean useDice) {
        this.displayName = displayName;
        this.playerCount = playerCount;
        this.useDice = useDice;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getPlayerCount() {
        return playerCount;
    }
    
    public boolean usesDice() {
        return useDice;
    }
    
    public boolean is4Player() {
        return playerCount == 4;
    }
}
