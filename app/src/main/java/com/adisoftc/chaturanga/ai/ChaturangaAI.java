package com.adisoftc.chaturanga.ai;

import com.adisoftc.chaturanga.logic.*;
import com.adisoftc.chaturanga.models.*;
import java.util.List;
import java.util.Random;

public class ChaturangaAI {
    private Random random;
    private int difficulty;
    
    public ChaturangaAI(int difficulty) {
        this.random = new Random();
        this.difficulty = difficulty;
    }
    
    public Move getBestMove(GameState gameState) {
        List<Move> validMoves = gameState.getAllValidMoves();
        
        if (validMoves.isEmpty()) {
            return null;
        }
        
        if (difficulty == 0) {
            return validMoves.get(random.nextInt(validMoves.size()));
        }
        
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        
        for (Move move : validMoves) {
            int score = evaluateMove(move, gameState);
            score += random.nextInt(6) - 3;
            
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        
        return bestMove;
    }
    
    private int evaluateMove(Move move, GameState gameState) {
        int score = 0;
        
        if (move.isCapture()) {
            Piece captured = move.getCapturedPiece();
            score += getPieceValue(captured.getType());
            
            if (captured.getType() == PieceType.RAJA) {
                score += 5000;
            }
        }
        
        Piece piece = move.getPiece();
        score += getPositionValue(move.getTo(), piece.getType());
        
        if (piece.getType() == PieceType.RAJA) {
            if (isKingSafe(move.getTo(), gameState)) {
                score += 1000;
            } else {
                score -= 500;
            }
        }
        
        if (move.isPromotion()) {
            score += 200;
        }
        
        if (piece.getType() == PieceType.PADATI || piece.getType() == PieceType.CHESS_PAWN) {
            score += 5;
        }
        
        return score;
    }
    
    private int getPieceValue(PieceType type) {
        switch (type) {
            case RAJA: return 10000;
            case QUEEN: return 900;
            case RATHA:
            case ROOK: return 500;
            case GAJA:
            case BISHOP: return 300;
            case ASHVA:
            case KNIGHT: return 300;
            case PADATI:
            case CHESS_PAWN: return 100;
            default: return 0;
        }
    }
    
    private int getPositionValue(Position pos, PieceType type) {
        int centerBonus = 0;
        int distanceFromCenter = Math.abs(pos.getRow() - 3) + Math.abs(pos.getCol() - 3);
        centerBonus = (6 - distanceFromCenter) * 2;
        
        return centerBonus;
    }
    
    private boolean isKingSafe(Position kingPos, GameState gameState) {
        return true;
    }
}
