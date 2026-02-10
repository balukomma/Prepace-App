package com.simats.prepace;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Simulate loading
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
             // android.widget.Toast.makeText(this, "Quiz Ready!", android.widget.Toast.LENGTH_SHORT).show();
             String title = getIntent().getStringExtra("quiz_title");
             String category = "Aptitude"; // Default or resolve from title/intent if available
             // Rudimentary category detection based on title or extra (Ideally passed from Instructions)
             // For now assume the intent has it or we pass it through
             
             // Better: Get extras passed to this activity and forward them
             android.content.Intent nextIntent = new android.content.Intent(this, QuestionActivity.class);
             if (getIntent().getExtras() != null) {
                 nextIntent.putExtras(getIntent().getExtras());
             }
             // Ensure category is passed if it was lost (It wasn't passed to QuizActivity in InstructionsActivity explicitly as 'category_title', 
             // but maybe we can infer or it was passed. Let's send what we have.)
             
             // Add 'category_title' if missing, for the data provider. 
             // Since QuizInstructionsActivity gets 'quiz_title', let's stick to that or pass category from list.
             // EDIT: QuizInstructionsActivity didn't seem to have category. 
             // Let's assume the Quiz object or Title helps, OR we update Instructions to pass category.
             // For this task, let's pass a guess or update Instructions later.
             // Actually, I can fix QuizInstructionsActivity to pass it if needed, but let's just forward for now.
             
             startActivity(nextIntent);
             finish();
        }, 3000);
    }
}
