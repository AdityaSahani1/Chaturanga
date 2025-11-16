package com.adisoftc.chaturanga;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackActivity extends Activity {
    
    private EditText nameEdit;
    private EditText emailEdit;
    private EditText feedbackEdit;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(40, 40, 40, 40);
        mainLayout.setBackgroundColor(0xFF2F2F2F);
        
        TextView titleText = new TextView(this);
        titleText.setText("Send Feedback");
        titleText.setTextSize(24);
        titleText.setTextColor(0xFFD4AF37);
        titleText.setPadding(0, 0, 0, 30);
        
        TextView nameLabel = new TextView(this);
        nameLabel.setText("Name (Optional)");
        nameLabel.setTextColor(0xFFFFFFFF);
        nameLabel.setTextSize(16);
        nameLabel.setPadding(0, 10, 0, 5);
        
        nameEdit = new EditText(this);
        nameEdit.setHint("Your name");
        nameEdit.setTextColor(0xFFFFFFFF);
        nameEdit.setHintTextColor(0xFF888888);
        nameEdit.setBackgroundColor(0xFF444444);
        nameEdit.setPadding(20, 20, 20, 20);
        
        TextView emailLabel = new TextView(this);
        emailLabel.setText("Email (Optional)");
        emailLabel.setTextColor(0xFFFFFFFF);
        emailLabel.setTextSize(16);
        emailLabel.setPadding(0, 20, 0, 5);
        
        emailEdit = new EditText(this);
        emailEdit.setHint("your@email.com");
        emailEdit.setTextColor(0xFFFFFFFF);
        emailEdit.setHintTextColor(0xFF888888);
        emailEdit.setBackgroundColor(0xFF444444);
        emailEdit.setPadding(20, 20, 20, 20);
        
        TextView feedbackLabel = new TextView(this);
        feedbackLabel.setText("Your Feedback");
        feedbackLabel.setTextColor(0xFFFFFFFF);
        feedbackLabel.setTextSize(16);
        feedbackLabel.setPadding(0, 20, 0, 5);
        
        feedbackEdit = new EditText(this);
        feedbackEdit.setHint("Tell us what you think...");
        feedbackEdit.setTextColor(0xFFFFFFFF);
        feedbackEdit.setHintTextColor(0xFF888888);
        feedbackEdit.setBackgroundColor(0xFF444444);
        feedbackEdit.setPadding(20, 20, 20, 20);
        feedbackEdit.setMinLines(5);
        feedbackEdit.setGravity(Gravity.TOP | Gravity.START);
        
        Button submitButton = new Button(this);
        submitButton.setText("Submit Feedback");
        submitButton.setTextSize(18);
        submitButton.setPadding(20, 30, 20, 30);
        submitButton.setBackgroundColor(0xFFD4AF37);
        submitButton.setTextColor(0xFF000000);
        
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        btnParams.setMargins(0, 30, 0, 0);
        submitButton.setLayoutParams(btnParams);
        
        submitButton.setOnClickListener(v -> submitFeedback());
        
        mainLayout.addView(titleText);
        mainLayout.addView(nameLabel);
        mainLayout.addView(nameEdit);
        mainLayout.addView(emailLabel);
        mainLayout.addView(emailEdit);
        mainLayout.addView(feedbackLabel);
        mainLayout.addView(feedbackEdit);
        mainLayout.addView(submitButton);
        
        setContentView(mainLayout);
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
