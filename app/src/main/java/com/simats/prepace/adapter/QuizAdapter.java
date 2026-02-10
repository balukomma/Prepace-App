package com.simats.prepace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.simats.prepace.R;
import com.simats.prepace.model.Quiz;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private Context context;
    private List<Quiz> quizList;

    public QuizAdapter(Context context, List<Quiz> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.tvName.setText(quiz.getName());
        holder.tvDesc.setText(quiz.getDescription());
        holder.tvQuestions.setText(quiz.getQuestions() + " Questions");
        holder.tvDuration.setText(quiz.getDuration() + " mins");
        holder.tvDifficulty.setText(quiz.getDifficulty());

        // Set difficulty badge and colors based on difficulty
        switch (quiz.getDifficulty().toLowerCase()) {
            case "easy":
                holder.tvDifficulty.setBackgroundResource(R.drawable.bg_difficulty_easy);
                holder.tvDifficulty.setTextColor(0xFF388E3C); // Green
                break;
            case "medium":
                holder.tvDifficulty.setBackgroundResource(R.drawable.bg_difficulty_medium);
                holder.tvDifficulty.setTextColor(0xFFFBC02D); // Yellow/Orange
                break;
            case "hard":
                holder.tvDifficulty.setBackgroundResource(R.drawable.bg_difficulty_hard);
                holder.tvDifficulty.setTextColor(0xFFD32F2F); // Red
                break;
        }

        android.view.View.OnClickListener startQuizListener = v -> {
            android.content.Intent intent = new android.content.Intent(context, com.simats.prepace.QuizInstructionsActivity.class);
            intent.putExtra("quiz_title", quiz.getName());
            intent.putExtra("quiz_description", quiz.getDescription());
            intent.putExtra("question_count", quiz.getQuestions());
            intent.putExtra("time_limit", quiz.getDuration());
            intent.putExtra("category_title", quiz.getCategory());
            context.startActivity(intent);
        };

        holder.itemView.setOnClickListener(startQuizListener);
        if (holder.btnStartQuiz != null) {
            holder.btnStartQuiz.setOnClickListener(startQuizListener);
        }
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDesc, tvQuestions, tvDuration, tvDifficulty;
        android.widget.Button btnStartQuiz;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvQuizName);
            tvDesc = itemView.findViewById(R.id.tvQuizDesc);
            tvQuestions = itemView.findViewById(R.id.tvQuestionCount);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvDifficulty = itemView.findViewById(R.id.tvDifficultyBadge);
            btnStartQuiz = itemView.findViewById(R.id.btnStartQuiz);
        }
    }
}
