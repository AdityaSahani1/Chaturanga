package com.adisoftc.chaturanga;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.adisoftc.chaturanga.utils.UpdateManager;

public class MainActivity extends Activity {
    
    private UpdateManager updateManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize update manager and check for updates
        updateManager = new UpdateManager(this);
        updateManager.checkForUpdate();
        
        // Set up button listeners
        findViewById(R.id.btn_4player_dice).setOnClickListener(v -> startGame("INDIAN_4P_DICE"));
        findViewById(R.id.btn_4player_strategic).setOnClickListener(v -> startGame("INDIAN_4P_STRATEGIC"));
        findViewById(R.id.btn_persian).setOnClickListener(v -> startGame("PERSIAN_SHATRANJ"));
        findViewById(R.id.btn_chess).setOnClickListener(v -> startGame("MODERN_CHESS"));
        
        findViewById(R.id.btn_tutorial).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TutorialActivity.class));
        });
        
        findViewById(R.id.btn_history).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        });
        
        findViewById(R.id.btn_feedback).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FeedbackActivity.class));
        });
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
