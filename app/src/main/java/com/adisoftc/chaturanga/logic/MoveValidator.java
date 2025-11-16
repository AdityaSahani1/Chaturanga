package com.adisoftc.chaturanga.logic;

import com.adisoftc.chaturanga.models.*;
import java.util.ArrayList;
import java.util.List;

public class MoveValidator {
    private Board board;
    private GameMode mode;
    
    public MoveValidator(Board board) {
        this.board = board;
        this.mode = board.getMode();
    }
    
    public List<Move> getValidMoves(Position from, Player currentPlayer) {
        List<Move> moves = new ArrayList<>();
        Piece piece = board.getPiece(from);
        
        if (piece == null || piece.getOwner() != currentPlayer || !piece.isActive()) {
            return moves;
        }
        
        switch (piece.getType()) {
            case RAJA:
                addKingMoves(from, piece, moves);
                break;
            case RATHA:
            case ROOK:
                addRathaMoves(from, piece, moves);
                break;
            case GAJA:
                addGajaMoves(from, piece, moves);
                break;
            case BISHOP:
                addBishopMoves(from, piece, moves);
                break;
            case ASHVA:
            case KNIGHT:
                addAshvaMoves(from, piece, moves);
                break;
            case PADATI:
                addPadatiMoves(from, piece, moves);
                break;
            case CHESS_PAWN:
                addChessPawnMoves(from, piece, moves);
                break;
            case QUEEN:
                addQueenMoves(from, piece, moves);
                break;
        }
        
        return moves;
    }
    
    public List<Move> getValidMovesForPieceType(PieceType type, Player currentPlayer) {
        List<Move> allMoves = new ArrayList<>();
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                Piece piece = board.getPiece(pos);
                
                if (piece != null && piece.getOwner() == currentPlayer && 
                    piece.getType() == type && piece.isActive()) {
                    allMoves.addAll(getValidMoves(pos, currentPlayer));
                }
            }
        }
        
        return allMoves;
    }
    
    private void addKingMoves(Position from, Piece piece, List<Move> moves) {
        int[][] directions = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
        
        for (int[] dir : directions) {
            Position to = new Position(from.getRow() + dir[0], from.getCol() + dir[1]);
            if (isValidDestination(to, piece)) {
                moves.add(new Move(from, to, piece));
            }
        }
    }
    
    private void addRathaMoves(Position from, Piece piece, List<Move> moves) {
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        
        for (int[] dir : directions) {
            for (int i = 1; i < 8; i++) {
                Position to = new Position(from.getRow() + dir[0]*i, from.getCol() + dir[1]*i);
                if (!to.isValid()) break;
                
                Piece target = board.getPiece(to);
                if (target == null) {
                    moves.add(new Move(from, to, piece));
                } else {
                    if (canCapture(piece, target)) {
                        moves.add(new Move(from, to, piece));
                    }
                    break;
                }
            }
        }
    }
    
    private void addGajaMoves(Position from, Piece piece, List<Move> moves) {
        int[][] jumps = {{-2,-2},{-2,2},{2,-2},{2,2}};
        
        for (int[] jump : jumps) {
            Position to = new Position(from.getRow() + jump[0], from.getCol() + jump[1]);
            if (isValidDestination(to, piece)) {
                moves.add(new Move(from, to, piece));
            }
        }
    }
    
    private void addBishopMoves(Position from, Piece piece, List<Move> moves) {
        int[][] directions = {{-1,-1},{-1,1},{1,-1},{1,1}};
        
        for (int[] dir : directions) {
            for (int i = 1; i < 8; i++) {
                Position to = new Position(from.getRow() + dir[0]*i, from.getCol() + dir[1]*i);
                if (!to.isValid()) break;
                
                Piece target = board.getPiece(to);
                if (target == null) {
                    moves.add(new Move(from, to, piece));
                } else {
                    if (canCapture(piece, target)) {
                        moves.add(new Move(from, to, piece));
                    }
                    break;
                }
            }
        }
    }
    
    private void addAshvaMoves(Position from, Piece piece, List<Move> moves) {
        int[][] jumps = {
            {-2,-1},{-2,1},{-1,-2},{-1,2},
            {1,-2},{1,2},{2,-1},{2,1}
        };
        
        for (int[] jump : jumps) {
            Position to = new Position(from.getRow() + jump[0], from.getCol() + jump[1]);
            if (isValidDestination(to, piece)) {
                moves.add(new Move(from, to, piece));
            }
        }
    }
    
    private void addPadatiMoves(Position from, Piece piece, List<Move> moves) {
        Player owner = piece.getOwner();
        int forwardRow = from.getRow();
        int forwardCol = from.getCol();
        
        int rowDir = 0, colDir = 0;
        if (owner == Player.SOUTH) {
            rowDir = 1;
        } else if (owner == Player.NORTH) {
            rowDir = -1;
        } else if (owner == Player.WEST) {
            colDir = 1;
        } else if (owner == Player.EAST) {
            colDir = -1;
        } else {
            rowDir = 1;
        }
        
        Position forward = new Position(forwardRow + rowDir, forwardCol + colDir);
        if (forward.isValid() && board.getPiece(forward) == null) {
            Move move = new Move(from, forward, piece);
            if (isPawnPromotion(forward, owner)) {
                move.setPromotion(true);
                move.setPromotionType(PieceType.RATHA);
            }
            moves.add(move);
        }
        
        if (mode.is4Player()) {
            if (owner == Player.SOUTH || owner == Player.NORTH) {
                Position captureLeft = new Position(forwardRow + rowDir, forwardCol - 1);
                Position captureRight = new Position(forwardRow + rowDir, forwardCol + 1);
                addPawnCapture(from, captureLeft, piece, moves, owner);
                addPawnCapture(from, captureRight, piece, moves, owner);
            } else {
                Position captureUp = new Position(forwardRow - 1, forwardCol + colDir);
                Position captureDown = new Position(forwardRow + 1, forwardCol + colDir);
                addPawnCapture(from, captureUp, piece, moves, owner);
                addPawnCapture(from, captureDown, piece, moves, owner);
            }
        } else {
            Position captureLeft = new Position(forwardRow + rowDir, forwardCol - 1);
            Position captureRight = new Position(forwardRow + rowDir, forwardCol + 1);
            addPawnCapture(from, captureLeft, piece, moves, owner);
            addPawnCapture(from, captureRight, piece, moves, owner);
        }
    }
    
    private void addChessPawnMoves(Position from, Piece piece, List<Move> moves) {
        Player owner = piece.getOwner();
        int direction = owner == Player.WHITE ? 1 : -1;
        
        Position forward = new Position(from.getRow() + direction, from.getCol());
        if (forward.isValid() && board.getPiece(forward) == null) {
            Move move = new Move(from, forward, piece);
            if (isPawnPromotion(forward, owner)) {
                move.setPromotion(true);
                move.setPromotionType(PieceType.QUEEN);
            }
            moves.add(move);
            
            if (!piece.hasMoved()) {
                Position doubleForward = new Position(from.getRow() + 2*direction, from.getCol());
                if (doubleForward.isValid() && board.getPiece(doubleForward) == null) {
                    moves.add(new Move(from, doubleForward, piece));
                }
            }
        }
        
        Position captureLeft = new Position(from.getRow() + direction, from.getCol() - 1);
        Position captureRight = new Position(from.getRow() + direction, from.getCol() + 1);
        addPawnCapture(from, captureLeft, piece, moves, owner);
        addPawnCapture(from, captureRight, piece, moves, owner);
    }
    
    private void addPawnCapture(Position from, Position to, Piece piece, List<Move> moves, Player owner) {
        if (to.isValid()) {
            Piece target = board.getPiece(to);
            if (target != null && canCapture(piece, target)) {
                Move move = new Move(from, to, piece);
                if (isPawnPromotion(to, owner)) {
                    move.setPromotion(true);
                    move.setPromotionType(mode == GameMode.MODERN_CHESS ? PieceType.QUEEN : PieceType.RATHA);
                }
                moves.add(move);
            }
        }
    }
    
    private boolean isPawnPromotion(Position pos, Player owner) {
        if (mode == GameMode.MODERN_CHESS) {
            return (owner == Player.WHITE && pos.getRow() == 7) || 
                   (owner == Player.BLACK && pos.getRow() == 0);
        } else if (mode.is4Player()) {
            if (owner == Player.SOUTH && pos.getRow() >= 6) return true;
            if (owner == Player.NORTH && pos.getRow() <= 1) return true;
            if (owner == Player.WEST && pos.getCol() >= 6) return true;
            if (owner == Player.EAST && pos.getCol() <= 1) return true;
        }
        return false;
    }
    
    private void addQueenMoves(Position from, Piece piece, List<Move> moves) {
        addRathaMoves(from, piece, moves);
        addBishopMoves(from, piece, moves);
    }
    
    private boolean isValidDestination(Position to, Piece piece) {
        if (!to.isValid()) return false;
        
        Piece target = board.getPiece(to);
        if (target == null) return true;
        
        return canCapture(piece, target);
    }
    
    private boolean canCapture(Piece attacker, Piece target) {
        if (!target.isActive()) return false;
        
        Player attackerOwner = attacker.getOwner();
        Player targetOwner = target.getOwner();
        
        if (attackerOwner == targetOwner) return false;
        
        if (mode.is4Player()) {
            if (attackerOwner.isAlly(targetOwner)) return false;
        }
        
        return true;
    }
    
    public boolean isKingInDanger(Player player) {
        Position kingPos = findKingPosition(player);
        if (kingPos == null) return false;
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                Piece piece = board.getPiece(pos);
                
                if (piece != null && piece.getOwner() != player && piece.isActive()) {
                    List<Move> moves = getValidMoves(pos, piece.getOwner());
                    for (Move move : moves) {
                        if (move.getTo().equals(kingPos)) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    private Position findKingPosition(Player player) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getType() == PieceType.RAJA && 
                    piece.getOwner() == player && piece.isActive()) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }
}
