package com.adisoftc.chaturanga;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.adisoftc.chaturanga.ai.ChaturangaAI;
import com.adisoftc.chaturanga.logic.GameState;
import com.adisoftc.chaturanga.models.*;
import com.adisoftc.chaturanga.ui.BoardView;
import com.adisoftc.chaturanga.utils.AdManager;
import com.adisoftc.chaturanga.utils.SoundManager;

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
        setContentView(R.layout.activity_game);
        
        AdManager.initializeAds(this);
        AdManager.loadInterstitialAd(this);
        
        handler = new Handler();
        
        String modeStr = getIntent().getStringExtra("GAME_MODE");
        GameMode mode = getGameMode(modeStr);
        
        gameState = new GameState(mode);
        ai = new ChaturangaAI(1);
        
        // Initialize views
        statusText = findViewById(R.id.status_text);
        diceResultText = findViewById(R.id.dice_result_text);
        boardView = findViewById(R.id.board_view);
        diceButton = findViewById(R.id.dice_button);
        
        LinearLayout adContainer = findViewById(R.id.ad_container);
        if (adContainer != null) {
            AdManager.loadBannerAd(this, adContainer);
        }
        
        // Set up board
        boardView.setGameState(gameState);
        statusText.setText("Current Player: " + gameState.getCurrentPlayer().getName());
        
        // Set up dice button
        if (mode.usesDice()) {
            diceButton.setVisibility(Button.VISIBLE);
            diceResultText.setVisibility(TextView.VISIBLE);
            diceButton.setEnabled(true);
        }
        
        diceButton.setOnClickListener(v -> rollDice());
        
        // Set up undo and menu buttons
        findViewById(R.id.btn_undo).setOnClickListener(v -> {
            Toast.makeText(this, "Undo feature coming soon!", Toast.LENGTH_SHORT).show();
        });
        
        findViewById(R.id.btn_menu).setOnClickListener(v -> finish());
        
        boardView.setOnMoveListener(new BoardView.OnMoveListener() {
            @Override
            public void onMove(Move move) {
                handleMove(move);
            }
            
            @Override
            public void onSquareSelected(Position position) {
                
            }
        });
        
        if (!mode.usesDice()) {
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
