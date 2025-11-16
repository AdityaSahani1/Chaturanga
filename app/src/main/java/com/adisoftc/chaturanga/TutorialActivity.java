package com.adisoftc.chaturanga;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.adisoftc.chaturanga.utils.Constants;

public class TutorialActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        
        TextView contentText = findViewById(R.id.tutorial_content);
        contentText.setText(Constants.TUTORIAL_TEXT);
        
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}
