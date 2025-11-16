package com.adisoftc.chaturanga;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.adisoftc.chaturanga.ai.ChaturangaAI;
import com.adisoftc.chaturanga.logic.GameState;
import com.adisoftc.chaturanga.models.*;
import com.adisoftc.chaturanga.ui.BoardView;

public class GameActivity extends Activity {
    private BoardView boardView;
    private GameState gameState;
    private TextView statusText;
    private TextView diceResultText;
    private Button diceButton;
    private ChaturangaAI ai;
    private Handler handler;
    private boolean useAI = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        handler = new Handler();
        
        String modeStr = getIntent().getStringExtra("GAME_MODE");
        GameMode mode = getGameMode(modeStr);
        
        gameState = new GameState(mode);
        ai = new ChaturangaAI(1);
        
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(0xFF1E1E1E);
        
        statusText = new TextView(this);
        statusText.setText("Current Player: " + gameState.getCurrentPlayer().getName());
        statusText.setTextSize(18);
        statusText.setTextColor(0xFFFFFFFF);
        statusText.setPadding(20, 20, 20, 20);
        statusText.setGravity(Gravity.CENTER);
        
        diceResultText = new TextView(this);
        diceResultText.setText("");
        diceResultText.setTextSize(16);
        diceResultText.setTextColor(0xFFFFD700);
        diceResultText.setPadding(20, 10, 20, 10);
        diceResultText.setGravity(Gravity.CENTER);
        
        boardView = new BoardView(this, null);
        boardView.setGameState(gameState);
        
        LinearLayout.LayoutParams boardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1.0f
        );
        boardView.setLayoutParams(boardParams);
        
        diceButton = new Button(this);
        diceButton.setText("Roll Dice");
        diceButton.setTextSize(18);
        diceButton.setPadding(20, 30, 20, 30);
        diceButton.setBackgroundColor(0xFFD4AF37);
        diceButton.setTextColor(0xFF000000);
        
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        btnParams.setMargins(40, 20, 40, 20);
        diceButton.setLayoutParams(btnParams);
        
        if (!mode.usesDice()) {
            diceButton.setVisibility(Button.GONE);
        }
        
        diceButton.setOnClickListener(v -> rollDice());
        
        boardView.setOnMoveListener(new BoardView.OnMoveListener() {
            @Override
            public void onMove(Move move) {
                handleMove(move);
            }
            
            @Override
            public void onSquareSelected(Position position) {
                
            }
        });
        
        mainLayout.addView(statusText);
        mainLayout.addView(diceResultText);
        mainLayout.addView(boardView);
        mainLayout.addView(diceButton);
        
        setContentView(mainLayout);
        
        if (mode.usesDice()) {
            diceButton.setEnabled(true);
        } else {
            checkAITurn();
        }
    }
    
    private GameMode getGameMode(String modeStr) {
        switch (modeStr) {
            case "INDIAN_4P_DICE": return GameMode.INDIAN_4P_DICE;
            case "INDIAN_4P_STRATEGIC": return GameMode.INDIAN_4P_STRATEGIC;
            case "PERSIAN_SHATRANJ": return GameMode.PERSIAN_SHATRANJ;
            case "MODERN_CHESS": return GameMode.MODERN_CHESS;
            default: return GameMode.INDIAN_4P_DICE;
        }
    }
    
    private void rollDice() {
        int result = gameState.rollDice();
        PieceType allowedType = gameState.getAllowedPieceType();
        
        diceResultText.setText("Dice: " + result + " - Move " + allowedType.getDisplayName());
        diceButton.setEnabled(false);
        
        handler.postDelayed(() -> {
            if (gameState.getAllValidMoves().isEmpty()) {
                Toast.makeText(this, "No valid moves! Turn skipped.", Toast.LENGTH_SHORT).show();
                skipTurn();
            } else {
                checkAITurn();
            }
        }, 500);
    }
    
    private void handleMove(Move move) {
        if (gameState.makeMove(move)) {
            boardView.setGameState(gameState);
            
            if (gameState.isGameOver()) {
                showGameOver();
            } else {
                updateStatus();
                
                if (gameState.needsDiceRoll()) {
                    diceButton.setEnabled(true);
                } else {
                    handler.postDelayed(this::checkAITurn, 500);
                }
            }
        }
    }
    
    private void checkAITurn() {
        if (useAI && shouldAIPlay()) {
            handler.postDelayed(() -> {
                if (gameState.needsDiceRoll()) {
                    rollDice();
                } else {
                    makeAIMove();
                }
            }, 1000);
        }
    }
    
    private boolean shouldAIPlay() {
        Player current = gameState.getCurrentPlayer();
        if (gameState.getMode().is4Player()) {
            return current != Player.SOUTH;
        } else {
            return current == Player.BLACK;
        }
    }
    
    private void makeAIMove() {
        Move aiMove = ai.getBestMove(gameState);
        
        if (aiMove != null) {
            handleMove(aiMove);
        } else {
            skipTurn();
        }
    }
    
    private void skipTurn() {
        Player nextPlayer = gameState.getCurrentPlayer().getNextPlayer(gameState.getMode());
        
        while (gameState.getBoard().isPlayerDefeated(nextPlayer) && !gameState.isGameOver()) {
            nextPlayer = nextPlayer.getNextPlayer(gameState.getMode());
        }
        
        updateStatus();
        
        if (gameState.needsDiceRoll()) {
            diceButton.setEnabled(true);
        } else {
            checkAITurn();
        }
    }
    
    private void updateStatus() {
        statusText.setText("Current Player: " + gameState.getCurrentPlayer().getName());
        
        if (gameState.needsDiceRoll()) {
            diceResultText.setText("Roll the dice to continue");
        }
    }
    
    private void showGameOver() {
        String message;
        if (gameState.getWinner() != null) {
            message = gameState.getWinner().getName() + " wins!";
        } else {
            message = "Game Over - Draw!";
        }
        
        new AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage(message)
            .setPositiveButton("New Game", (dialog, which) -> {
                recreate();
            })
            .setNegativeButton("Main Menu", (dialog, which) -> {
                finish();
            })
            .setCancelable(false)
            .show();
    }
}
