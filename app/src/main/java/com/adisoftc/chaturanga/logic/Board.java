package com.adisoftc.chaturanga.logic;

import com.adisoftc.chaturanga.models.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private Piece[][] grid;
    private GameMode mode;
    private List<Player> activePlayers;
    private List<Player> defeatedPlayers;
    
    public Board(GameMode mode) {
        this.grid = new Piece[8][8];
        this.mode = mode;
        this.activePlayers = new ArrayList<>();
        this.defeatedPlayers = new ArrayList<>();
        initializeBoard();
    }
    
    private void initializeBoard() {
        if (mode == GameMode.MODERN_CHESS) {
            setupModernChess();
        } else if (mode == GameMode.PERSIAN_SHATRANJ) {
            setupPersianShatranj();
        } else {
            setup4PlayerChaturanga();
        }
    }
    
    private void setup4PlayerChaturanga() {
        activePlayers.add(Player.SOUTH);
        activePlayers.add(Player.WEST);
        activePlayers.add(Player.NORTH);
        activePlayers.add(Player.EAST);
        
        setupPlayerArmy(Player.SOUTH, 0, 0);
        setupPlayerArmy(Player.WEST, 4, 0);
        setupPlayerArmy(Player.NORTH, 4, 4);
        setupPlayerArmy(Player.EAST, 0, 4);
    }
    
    private void setupPlayerArmy(Player player, int startRow, int startCol) {
        int[][] offsets = {
            {0, 0}, {0, 1}, {0, 2}, {0, 3},
            {1, 0}, {1, 1}, {1, 2}, {1, 3}
        };
        
        PieceType[] backRow;
        if (player == Player.SOUTH) {
            backRow = new PieceType[]{PieceType.RATHA, PieceType.ASHVA, PieceType.GAJA, PieceType.RAJA};
        } else if (player == Player.WEST) {
            backRow = new PieceType[]{PieceType.RATHA, PieceType.ASHVA, PieceType.GAJA, PieceType.RAJA};
        } else if (player == Player.NORTH) {
            backRow = new PieceType[]{PieceType.RAJA, PieceType.GAJA, PieceType.ASHVA, PieceType.RATHA};
        } else {
            backRow = new PieceType[]{PieceType.RAJA, PieceType.GAJA, PieceType.ASHVA, PieceType.RATHA};
        }
        
        for (int i = 0; i < 4; i++) {
            int row = startRow + offsets[i][0];
            int col = startCol + offsets[i][1];
            grid[row][col] = new Piece(backRow[i], player);
        }
        
        for (int i = 4; i < 8; i++) {
            int row = startRow + offsets[i][0];
            int col = startCol + offsets[i][1];
            grid[row][col] = new Piece(PieceType.PADATI, player);
        }
    }
    
    private void setupModernChess() {
        activePlayers.add(Player.WHITE);
        activePlayers.add(Player.BLACK);
        
        PieceType[] backRow = {
            PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN,
            PieceType.RAJA, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK
        };
        
        for (int col = 0; col < 8; col++) {
            grid[0][col] = new Piece(backRow[col], Player.WHITE);
            grid[1][col] = new Piece(PieceType.CHESS_PAWN, Player.WHITE);
            grid[6][col] = new Piece(PieceType.CHESS_PAWN, Player.BLACK);
            grid[7][col] = new Piece(backRow[col], Player.BLACK);
        }
    }
    
    private void setupPersianShatranj() {
        activePlayers.add(Player.WHITE);
        activePlayers.add(Player.BLACK);
        
        PieceType[] backRow = {
            PieceType.RATHA, PieceType.ASHVA, PieceType.GAJA, PieceType.RAJA,
            PieceType.QUEEN, PieceType.GAJA, PieceType.ASHVA, PieceType.RATHA
        };
        
        for (int col = 0; col < 8; col++) {
            grid[0][col] = new Piece(backRow[col], Player.WHITE);
            grid[1][col] = new Piece(PieceType.PADATI, Player.WHITE);
            grid[6][col] = new Piece(PieceType.PADATI, Player.BLACK);
            grid[7][col] = new Piece(backRow[col], Player.BLACK);
        }
    }
    
    public Piece getPiece(Position pos) {
        if (!pos.isValid()) return null;
        return grid[pos.getRow()][pos.getCol()];
    }
    
    public Piece getPiece(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) return null;
        return grid[row][col];
    }
    
    public void setPiece(Position pos, Piece piece) {
        if (pos.isValid()) {
            grid[pos.getRow()][pos.getCol()] = piece;
        }
    }
    
    public void setPiece(int row, int col, Piece piece) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            grid[row][col] = piece;
        }
    }
    
    public boolean movePiece(Move move) {
        Piece piece = getPiece(move.getFrom());
        if (piece == null) return false;
        
        Piece captured = getPiece(move.getTo());
        if (captured != null) {
            move.setCapturedPiece(captured);
            
            if (captured.getType() == PieceType.RAJA) {
                handleKingCapture(captured.getOwner());
            }
        }
        
        setPiece(move.getTo(), piece);
        setPiece(move.getFrom(), null);
        piece.setMoved(true);
        
        if (move.isPromotion() && move.getPromotionType() != null) {
            piece.setType(move.getPromotionType());
        }
        
        return true;
    }
    
    private void handleKingCapture(Player defeatedPlayer) {
        activePlayers.remove(defeatedPlayer);
        defeatedPlayers.add(defeatedPlayer);
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = grid[row][col];
                if (piece != null && piece.getOwner() == defeatedPlayer) {
                    piece.setActive(false);
                }
            }
        }
    }
    
    public List<Player> getActivePlayers() {
        return new ArrayList<>(activePlayers);
    }
    
    public List<Player> getDefeatedPlayers() {
        return new ArrayList<>(defeatedPlayers);
    }
    
    public boolean isPlayerDefeated(Player player) {
        return defeatedPlayers.contains(player);
    }
    
    public GameMode getMode() {
        return mode;
    }
    
    public Board copy() {
        Board copy = new Board(this.mode);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (this.grid[row][col] != null) {
                    copy.grid[row][col] = this.grid[row][col].copy();
                } else {
                    copy.grid[row][col] = null;
                }
            }
        }
        copy.activePlayers = new ArrayList<>(this.activePlayers);
        copy.defeatedPlayers = new ArrayList<>(this.defeatedPlayers);
        return copy;
    }
}
