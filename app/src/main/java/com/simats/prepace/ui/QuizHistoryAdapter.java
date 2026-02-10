package com.simats.prepace.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simats.prepace.R;
import com.simats.prepace.model.QuizResult;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizHistoryAdapter extends RecyclerView.Adapter<QuizHistoryAdapter.ViewHolder> {

    private Context context;
    private List<QuizResult> results;

    public QuizHistoryAdapter(Context context, List<QuizResult> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizResult res = results.get(position);

        holder.tvQuizTitle.setText(res.getQuizTitle());
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        holder.tvDate.setText(sdf.format(new Date(res.getTimestamp())));

        int qTotal = res.getTotalQuestions() > 0 ? res.getTotalQuestions() : 1;
        int score = res.getScore();
        int wrong = qTotal - score;
        int percentage = (int) (((float) score / qTotal) * 100);

        holder.tvScoreBadge.setText(percentage + "%");
        holder.tvScoreVal.setText(percentage + "%");
        holder.tvCorrectVal.setText(String.valueOf(score));
        holder.tvWrongVal.setText(String.valueOf(wrong));

        // Color badge based on score
        int badgeColor = 0xFFFFF3E0; // Orange light
        int textColor = 0xFFFF6D00; // Orange
        
        if(percentage >= 80) {
             badgeColor = 0xFFE8F5E9; // Green light
             textColor = 0xFF2E7D32; // Green
        } else if (percentage < 50) {
             // keep orange/red ish
        }
        
        // Note: setting background tint programmatically requires valid color states or drawables, 
        // simplifying here by just relying on xml default or simple updates if needed.
        // For now trusting XML defaults for layout demo.

        holder.btnViewDetails.setOnClickListener(v -> {
            // Navigate to ReviewAnswersActivity
            Intent intent = new Intent(context, com.simats.prepace.ReviewAnswersActivity.class);
            
            int totalQ = res.getTotalQuestions() > 0 ? res.getTotalQuestions() : 5;
            int correctCount = res.getScore();
            
            // Use the comprehensive generator
            List<com.simats.prepace.model.Question> dummyQuestions = 
                com.simats.prepace.utils.MockQuizReviewGenerator.generateMockQuestions(
                    res.getCategory(), 
                    res.getQuizTitle(), 
                    totalQ, 
                    correctCount
                );
            
            intent.putExtra("question_list", (java.io.Serializable) dummyQuestions);
            intent.putExtra("score", score); // Optional if Activity needs it
            intent.putExtra("total_questions", totalQ); 
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuizTitle, tvDate, tvScoreBadge, tvScoreVal, tvCorrectVal, tvWrongVal;
        Button btnViewDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuizTitle = itemView.findViewById(R.id.tvQuizTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvScoreBadge = itemView.findViewById(R.id.tvScoreBadge);
            tvScoreVal = itemView.findViewById(R.id.tvScoreVal);
            tvCorrectVal = itemView.findViewById(R.id.tvCorrectVal);
            tvWrongVal = itemView.findViewById(R.id.tvWrongVal);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}
