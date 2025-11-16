package com.adisoftc.chaturanga.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.adisoftc.chaturanga.logic.*;
import com.adisoftc.chaturanga.models.*;
import java.util.ArrayList;
import java.util.List;

public class BoardView extends View {
    private GameState gameState;
    private Paint lightSquarePaint;
    private Paint darkSquarePaint;
    private Paint highlightPaint;
    private Paint textPaint;
    private Paint piecePaint;
    
    private Position selectedPosition;
    private List<Move> validMoves;
    private OnMoveListener moveListener;
    
    private float squareSize;
    private float boardSize;
    private float offsetX;
    private float offsetY;
    
    public interface OnMoveListener {
        void onMove(Move move);
        void onSquareSelected(Position position);
    }
    
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        lightSquarePaint = new Paint();
        lightSquarePaint.setColor(0xFFF0D9B5);
        lightSquarePaint.setStyle(Paint.Style.FILL);
        
        darkSquarePaint = new Paint();
        darkSquarePaint.setColor(0xFFB58863);
        darkSquarePaint.setStyle(Paint.Style.FILL);
        
        highlightPaint = new Paint();
        highlightPaint.setColor(0x8800FF00);
        highlightPaint.setStyle(Paint.Style.FILL);
        
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        
        piecePaint = new Paint();
        piecePaint.setAntiAlias(true);
        piecePaint.setTextAlign(Paint.Align.CENTER);
        
        validMoves = new ArrayList<>();
    }
    
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        invalidate();
    }
    
    public void setOnMoveListener(OnMoveListener listener) {
        this.moveListener = listener;
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        boardSize = Math.min(w, h) * 0.9f;
        squareSize = boardSize / 8;
        offsetX = (w - boardSize) / 2;
        offsetY = (h - boardSize) / 2;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (gameState == null) return;
        
        drawBoard(canvas);
        drawPieces(canvas);
        drawHighlights(canvas);
    }
    
    private void drawBoard(Canvas canvas) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Paint paint = ((row + col) % 2 == 0) ? lightSquarePaint : darkSquarePaint;
                
                float left = offsetX + col * squareSize;
                float top = offsetY + (7 - row) * squareSize;
                float right = left + squareSize;
                float bottom = top + squareSize;
                
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
    }
    
    private void drawPieces(Canvas canvas) {
        Board board = gameState.getBoard();
        piecePaint.setTextSize(squareSize * 0.6f);
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.isActive()) {
                    drawPiece(canvas, piece, row, col);
                }
            }
        }
    }
    
    private void drawPiece(Canvas canvas, Piece piece, int row, int col) {
        String symbol = getPieceSymbol(piece.getType());
        piecePaint.setColor(piece.getOwner().getColor());
        
        float x = offsetX + col * squareSize + squareSize / 2;
        float y = offsetY + (7 - row) * squareSize + squareSize / 2 + squareSize * 0.2f;
        
        canvas.drawText(symbol, x, y, piecePaint);
    }
    
    private String getPieceSymbol(PieceType type) {
        switch (type) {
            case RAJA: return "♔";
            case QUEEN: return "♕";
            case RATHA:
            case ROOK: return "♖";
            case GAJA: return "🐘";
            case BISHOP: return "♗";
            case ASHVA:
            case KNIGHT: return "♘";
            case PADATI:
            case CHESS_PAWN: return "♙";
            default: return "?";
        }
    }
    
    private void drawHighlights(Canvas canvas) {
        if (selectedPosition != null) {
            float col = selectedPosition.getCol();
            float row = 7 - selectedPosition.getRow();
            
            float left = offsetX + col * squareSize;
            float top = offsetY + row * squareSize;
            float right = left + squareSize;
            float bottom = top + squareSize;
            
            canvas.drawRect(left, top, right, bottom, highlightPaint);
        }
        
        Paint dotPaint = new Paint();
        dotPaint.setColor(0x88FFFF00);
        dotPaint.setStyle(Paint.Style.FILL);
        
        for (Move move : validMoves) {
            float col = move.getTo().getCol();
            float row = 7 - move.getTo().getRow();
            
            float cx = offsetX + col * squareSize + squareSize / 2;
            float cy = offsetY + row * squareSize + squareSize / 2;
            
            canvas.drawCircle(cx, cy, squareSize * 0.15f, dotPaint);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            
            if (x >= offsetX && x < offsetX + boardSize &&
                y >= offsetY && y < offsetY + boardSize) {
                
                int col = (int) ((x - offsetX) / squareSize);
                int row = 7 - (int) ((y - offsetY) / squareSize);
                
                handleSquareClick(row, col);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }
    
    private void handleSquareClick(int row, int col) {
        Position clickedPos = new Position(row, col);
        
        if (selectedPosition != null) {
            for (Move move : validMoves) {
                if (move.getTo().equals(clickedPos)) {
                    if (moveListener != null) {
                        moveListener.onMove(move);
                    }
                    selectedPosition = null;
                    validMoves.clear();
                    invalidate();
                    return;
                }
            }
        }
        
        Piece piece = gameState.getBoard().getPiece(clickedPos);
        if (piece != null && piece.getOwner() == gameState.getCurrentPlayer() && piece.isActive()) {
            selectedPosition = clickedPos;
            validMoves = gameState.getValidMoves(clickedPos);
            
            if (moveListener != null) {
                moveListener.onSquareSelected(clickedPos);
            }
            
            invalidate();
        } else {
            selectedPosition = null;
            validMoves.clear();
            invalidate();
        }
    }
    
    public void clearSelection() {
        selectedPosition = null;
        validMoves.clear();
        invalidate();
    }
}
