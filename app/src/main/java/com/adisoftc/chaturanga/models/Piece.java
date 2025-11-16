package com.adisoftc.chaturanga.models;

public class Piece {
    private PieceType type;
    private Player owner;
    private boolean hasMoved;
    private boolean isActive;
    
    public Piece(PieceType type, Player owner) {
        this.type = type;
        this.owner = owner;
        this.hasMoved = false;
        this.isActive = true;
    }
    
    public PieceType getType() {
        return type;
    }
    
    public void setType(PieceType type) {
        this.type = type;
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public void setOwner(Player owner) {
        this.owner = owner;
    }
    
    public boolean hasMoved() {
        return hasMoved;
    }
    
    public void setMoved(boolean moved) {
        this.hasMoved = moved;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    public Piece copy() {
        Piece copy = new Piece(this.type, this.owner);
        copy.hasMoved = this.hasMoved;
        copy.isActive = this.isActive;
        return copy;
    }
    
    @Override
    public String toString() {
        return owner.getName() + " " + type.getDisplayName();
    }
}
