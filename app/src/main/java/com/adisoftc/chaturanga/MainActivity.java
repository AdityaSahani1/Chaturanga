package com.adisoftc.chaturanga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.adisoftc.chaturanga.utils.UpdateManager;

public class MainActivity extends Activity {
    
    private UpdateManager updateManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize update manager and check for updates
        updateManager = new UpdateManager(this);
        updateManager.checkForUpdate();
        
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(40, 40, 40, 40);
        mainLayout.setBackgroundColor(0xFF2F2F2F);
        
        Button btn4PlayerDice = createButton("4-Player Chaturanga (Dice Mode)");
        btn4PlayerDice.setOnClickListener(v -> startGame("INDIAN_4P_DICE"));
        
        Button btn4PlayerStrategic = createButton("4-Player Chaturanga (Strategic)");
        btn4PlayerStrategic.setOnClickListener(v -> startGame("INDIAN_4P_STRATEGIC"));
        
        Button btnPersian = createButton("Persian Shatranj (2-Player)");
        btnPersian.setOnClickListener(v -> startGame("PERSIAN_SHATRANJ"));
        
        Button btnChess = createButton("Modern Chess (2-Player)");
        btnChess.setOnClickListener(v -> startGame("MODERN_CHESS"));
        
        Button btnTutorial = createButton("Tutorial - How to Play");
        btnTutorial.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(intent);
        });
        
        Button btnHistory = createButton("History of Chaturanga");
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
        
        Button btnFeedback = createButton("Send Feedback");
        btnFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
            startActivity(intent);
        });
        
        mainLayout.addView(btn4PlayerDice);
        mainLayout.addView(btn4PlayerStrategic);
        mainLayout.addView(btnPersian);
        mainLayout.addView(btnChess);
        mainLayout.addView(btnTutorial);
        mainLayout.addView(btnHistory);
        mainLayout.addView(btnFeedback);
        
        setContentView(mainLayout);
    }
    
    private Button createButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(16);
        button.setPadding(20, 30, 20, 30);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 20);
        button.setLayoutParams(params);
        
        button.setBackgroundColor(0xFFD4AF37);
        button.setTextColor(0xFF000000);
        
        return button;
    }
    
    private void startGame(String modeStr) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("GAME_MODE", modeStr);
        startActivity(intent);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Resume update if it was in progress
        if (updateManager != null) {
            updateManager.resumeUpdateIfNeeded();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle update result
        if (updateManager != null) {
            updateManager.handleActivityResult(requestCode, resultCode);
        }
    }
}
