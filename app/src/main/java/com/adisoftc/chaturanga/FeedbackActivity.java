package com.adisoftc.chaturanga;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class FeedbackActivity extends Activity {
    
    private EditText nameEdit;
    private EditText emailEdit;
    private EditText feedbackEdit;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        
        nameEdit = findViewById(R.id.name_edit);
        emailEdit = findViewById(R.id.email_edit);
        feedbackEdit = findViewById(R.id.feedback_edit);
        
        findViewById(R.id.submit_button).setOnClickListener(v -> submitFeedback());
    }
    
    private void submitFeedback() {
        String feedback = feedbackEdit.getText().toString().trim();
        
        if (feedback.isEmpty()) {
            Toast.makeText(this, "Please enter your feedback", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_LONG).show();
        finish();
    }
}
