package com.adisoftc.chaturanga.logic;

import com.adisoftc.chaturanga.models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState {
    private Board board;
    private GameMode mode;
    private Player currentPlayer;
    private List<Move> moveHistory;
    private MoveValidator validator;
    private Random random;
    private int lastDiceRoll;
    private PieceType allowedPieceType;
    private boolean gameOver;
    private Player winner;
    
    public GameState(GameMode mode) {
        this.mode = mode;
        this.board = new Board(mode);
        this.validator = new MoveValidator(board);
        this.moveHistory = new ArrayList<>();
        this.random = new Random();
        this.gameOver = false;
        
        if (mode.is4Player()) {
            this.currentPlayer = Player.SOUTH;
        } else {
            this.currentPlayer = Player.WHITE;
        }
    }
    
    public int rollDice() {
        lastDiceRoll = random.nextInt(4) + 1;
        allowedPieceType = PieceType.fromDiceValue(lastDiceRoll);
        return lastDiceRoll;
    }
    
    public int getLastDiceRoll() {
        return lastDiceRoll;
    }
    
    public PieceType getAllowedPieceType() {
        return allowedPieceType;
    }
    
    public boolean canMakeMove(Move move) {
        if (gameOver) return false;
        
        Piece piece = move.getPiece();
        if (piece.getOwner() != currentPlayer) return false;
        
        if (mode.usesDice() && allowedPieceType != null) {
            if (piece.getType() != allowedPieceType) {
                if (!(piece.getType() == PieceType.PADATI && allowedPieceType == PieceType.RATHA)) {
                    return false;
                }
            }
        }
        
        List<Move> validMoves = validator.getValidMoves(move.getFrom(), currentPlayer);
        boolean moveFound = false;
        for (Move validMove : validMoves) {
            if (validMove.getTo().equals(move.getTo())) {
                moveFound = true;
                break;
            }
        }
        
        if (!moveFound) return false;
        
        if (!mode.is4Player()) {
            if (!isMoveSafe(move)) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isMoveSafe(Move move) {
        Board tempBoard = board.copy();
        tempBoard.movePiece(move);
        
        MoveValidator tempValidator = new MoveValidator(tempBoard);
        return !tempValidator.isKingInDanger(currentPlayer);
    }
    
    public boolean makeMove(Move move) {
        if (!canMakeMove(move)) return false;
        
        boolean success = board.movePiece(move);
        if (success) {
            moveHistory.add(move);
            
            if (move.isCapture() && move.getCapturedPiece().getType() == PieceType.RAJA) {
                checkGameOver();
            }
            
            if (!gameOver) {
                nextTurn();
            }
        }
        
        return success;
    }
    
    private void nextTurn() {
        do {
            currentPlayer = currentPlayer.getNextPlayer(mode);
        } while (board.isPlayerDefeated(currentPlayer) && !gameOver);
        
        if (mode.usesDice()) {
            allowedPieceType = null;
        }
    }
    
    private void checkGameOver() {
        List<Player> active = board.getActivePlayers();
        
        if (active.size() == 1) {
            gameOver = true;
            winner = active.get(0);
        } else if (active.size() == 0) {
            gameOver = true;
            winner = null;
        }
    }
    
    public List<Move> getValidMoves(Position pos) {
        if (gameOver) return new ArrayList<>();
        
        List<Move> moves = validator.getValidMoves(pos, currentPlayer);
        
        if (mode.usesDice() && allowedPieceType != null) {
            Piece piece = board.getPiece(pos);
            if (piece != null && piece.getType() != allowedPieceType) {
                if (!(piece.getType() == PieceType.PADATI && allowedPieceType == PieceType.RATHA)) {
                    return new ArrayList<>();
                }
            }
        }
        
        if (!mode.is4Player()) {
            List<Move> safeMoves = new ArrayList<>();
            for (Move move : moves) {
                if (isMoveSafe(move)) {
                    safeMoves.add(move);
                }
            }
            return safeMoves;
        }
        
        return moves;
    }
    
    public List<Move> getAllValidMoves() {
        if (gameOver) return new ArrayList<>();
        
        if (mode.usesDice() && allowedPieceType != null) {
            return validator.getValidMovesForPieceType(allowedPieceType, currentPlayer);
        }
        
        List<Move> allMoves = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                allMoves.addAll(getValidMoves(pos));
            }
        }
        return allMoves;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public GameMode getMode() {
        return mode;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    public List<Move> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public Player getWinner() {
        return winner;
    }
    
    public boolean needsDiceRoll() {
        return mode.usesDice() && allowedPieceType == null;
    }
}
