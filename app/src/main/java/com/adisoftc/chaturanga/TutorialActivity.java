package com.adisoftc.chaturanga;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.adisoftc.chaturanga.utils.Constants;

public class TutorialActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(0xFF2F2F2F);
        
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(40, 40, 40, 40);
        
        TextView titleText = new TextView(this);
        titleText.setText("Tutorial - How to Play");
        titleText.setTextSize(24);
        titleText.setTextColor(0xFFD4AF37);
        titleText.setPadding(0, 0, 0, 30);
        
        TextView contentText = new TextView(this);
        contentText.setText(Constants.TUTORIAL_TEXT);
        contentText.setTextSize(16);
        contentText.setTextColor(0xFFFFFFFF);
        contentText.setLineSpacing(8, 1.0f);
        
        mainLayout.addView(titleText);
        mainLayout.addView(contentText);
        
        scrollView.addView(mainLayout);
        setContentView(scrollView);
    }
}
