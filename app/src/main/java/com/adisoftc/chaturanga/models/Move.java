package com.adisoftc.chaturanga.models;

public class Move {
    private Position from;
    private Position to;
    private Piece piece;
    private Piece capturedPiece;
    private boolean isPromotion;
    private PieceType promotionType;
    
    public Move(Position from, Position to, Piece piece) {
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.isPromotion = false;
    }
    
    public Position getFrom() {
        return from;
    }
    
    public Position getTo() {
        return to;
    }
    
    public Piece getPiece() {
        return piece;
    }
    
    public Piece getCapturedPiece() {
        return capturedPiece;
    }
    
    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }
    
    public boolean isPromotion() {
        return isPromotion;
    }
    
    public void setPromotion(boolean promotion) {
        this.isPromotion = promotion;
    }
    
    public PieceType getPromotionType() {
        return promotionType;
    }
    
    public void setPromotionType(PieceType promotionType) {
        this.promotionType = promotionType;
    }
    
    public boolean isCapture() {
        return capturedPiece != null;
    }
    
    @Override
    public String toString() {
        return from.toNotation() + "-" + to.toNotation();
    }
}
