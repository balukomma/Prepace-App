package com.simats.prepace;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.simats.prepace.model.Question;
import com.simats.prepace.utils.QuizDataProvider;
import java.util.List;
import java.util.Locale;

public class QuestionActivity extends AppCompatActivity {

    private TextView tvQuestionNumber, tvTimer, tvQuestion;
    private ProgressBar progressBar;
    private View optionA, optionB, optionC, optionD;
    private TextView[] tvOptionLabels;
    private TextView[] tvOptionTexts;
    private android.widget.ImageView[] ivCheckMarks; // New
    private CardView[] cardOptions; // To change background color

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private CountDownTimer timer;
    private static final long TIME_LIMIT_MS = 15 * 60 * 1000; // 15 minutes

    private int selectedOptionIndex = -1;
    private boolean isAnswerChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Initialize Views
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvTimer = findViewById(R.id.tvTimer);
        tvQuestion = findViewById(R.id.tvQuestion);
        progressBar = findViewById(R.id.progressBar);

        // Options (Included layouts)
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
        
        // Helper arrays for easier access
        View[] options = {optionA, optionB, optionC, optionD};
        tvOptionLabels = new TextView[4];
        tvOptionTexts = new TextView[4];
        ivCheckMarks = new android.widget.ImageView[4];
        cardOptions = new CardView[4];

        for (int i = 0; i < 4; i++) {
            tvOptionLabels[i] = options[i].findViewById(R.id.tvOptionLabel);
            tvOptionTexts[i] = options[i].findViewById(R.id.tvOptionText);
            ivCheckMarks[i] = options[i].findViewById(R.id.ivCheckMark);
            cardOptions[i] = (CardView) options[i]; // The root of included item is CardView
            
            // Set labels A, B, C, D
            tvOptionLabels[i].setText(String.valueOf((char)('A' + i)));
            
            final int index = i;
            options[i].setOnClickListener(v -> selectOption(index));
        }

        findViewById(R.id.btnNext).setOnClickListener(v -> checkAnswerAndNext());
        findViewById(R.id.btnDataClose).setOnClickListener(v -> finish());

        // Get Data
        String category = getIntent().getStringExtra("category_title");
        String quizTitle = getIntent().getStringExtra("quiz_title");
        if (category == null) category = "Aptitude"; // Default
        
        questionList = QuizDataProvider.getQuestions(this, category, quizTitle);
        
        startTimer();
        loadQuestion();
    }

    private void startTimer() {
        timer = new CountDownTimer(TIME_LIMIT_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                Toast.makeText(QuestionActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                finish(); // Or show result
            }
        }.start();
    }

    private void loadQuestion() {
        if (currentQuestionIndex >= questionList.size()) {
            Toast.makeText(this, "Quiz Finished! Score: " + score, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Question q = questionList.get(currentQuestionIndex);
        tvQuestion.setText(q.getQuestionText());
        tvOptionTexts[0].setText(q.getOptionA());
        tvOptionTexts[1].setText(q.getOptionB());
        tvOptionTexts[2].setText(q.getOptionC());
        tvOptionTexts[3].setText(q.getOptionD());

        tvQuestionNumber.setText("Question " + (currentQuestionIndex + 1) + "/" + questionList.size());
        progressBar.setMax(questionList.size());
        progressBar.setProgress(currentQuestionIndex + 1);

        // Change button text on last question
        android.widget.Button btnNext = findViewById(R.id.btnNext);
        if (currentQuestionIndex == questionList.size() - 1) {
            btnNext.setText("Submit Quiz");
        } else {
            btnNext.setText("Next Question");
        }

        resetOptions();
        selectedOptionIndex = -1;
        isAnswerChecked = false;
    }

    private void resetOptions() {
        for (int i = 0; i < 4; i++) {
            cardOptions[i].setCardBackgroundColor(Color.WHITE);
            tvOptionLabels[i].setBackgroundResource(R.drawable.bg_option_label); // Default
            tvOptionLabels[i].setTextColor(Color.parseColor("#555555"));
            tvOptionTexts[i].setTextColor(Color.parseColor("#333333"));
            ivCheckMarks[i].setVisibility(View.GONE);
        }
    }

    private void selectOption(int index) {
        if (isAnswerChecked) return; // Prevent changing after submission if we were checking immediately (logic choice)
        
        // Start: Reset all first for single selection visual
        resetOptions();
        
        selectedOptionIndex = index;
        // Highlight selected
        cardOptions[index].setCardBackgroundColor(Color.parseColor("#2962FF")); // Vibrant Blue
        tvOptionLabels[index].setBackgroundResource(R.drawable.bg_option_label_selected);
        tvOptionLabels[index].setTextColor(Color.WHITE);
        tvOptionTexts[index].setTextColor(Color.WHITE);
        ivCheckMarks[index].setVisibility(View.VISIBLE);
    }

    private void checkAnswerAndNext() {
        if (selectedOptionIndex == -1) {
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
            return;
        }

        Question q = questionList.get(currentQuestionIndex);
        
        // Save user's answer
        q.setUserAnswerIndex(selectedOptionIndex);

        if (selectedOptionIndex == q.getCorrectAnswerIndex()) {
            score++;
        }

        if (currentQuestionIndex < questionList.size() - 1) {
            currentQuestionIndex++;
            loadQuestion();
        } else {
            showSubmitDialog();
        }
    }

    private void showSubmitDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_submit_quiz, null);
        builder.setView(dialogView);
        
        android.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());

        dialogView.findViewById(R.id.btnSubmit).setOnClickListener(v -> {
            dialog.dismiss();
            
            android.content.Intent intent = new android.content.Intent(this, QuizResultActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total_questions", questionList.size());
            // Pass the question list with user answers
             intent.putExtra("question_list", (java.io.Serializable) questionList);
            // Pass metadata for Retry
            intent.putExtra("category_title", getIntent().getStringExtra("category_title"));
            intent.putExtra("quiz_title", getIntent().getStringExtra("quiz_title"));
            startActivity(intent);
            finish();
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}
