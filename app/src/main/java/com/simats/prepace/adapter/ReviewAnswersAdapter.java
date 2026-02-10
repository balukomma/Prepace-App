package com.simats.prepace.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.simats.prepace.R;
import com.simats.prepace.model.Question;
import java.util.List;

public class ReviewAnswersAdapter extends RecyclerView.Adapter<ReviewAnswersAdapter.ViewHolder> {

    private Context context;
    private List<Question> questionList;

    public ReviewAnswersAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review_answer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.bind(question, position);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber, tvQuestionText, tvExplanation, tvResultText;
        TextView[] tvOptions;
        LinearLayout layoutResultStatus, layoutExplanation, layoutOptions;
        ImageView ivResultIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);
            tvExplanation = itemView.findViewById(R.id.tvExplanation);
            
            layoutExplanation = itemView.findViewById(R.id.layoutExplanation);
            layoutOptions = itemView.findViewById(R.id.layoutOptions);

            tvOptions = new TextView[4];
            tvOptions[0] = itemView.findViewById(R.id.tvOptionA);
            tvOptions[1] = itemView.findViewById(R.id.tvOptionB);
            tvOptions[2] = itemView.findViewById(R.id.tvOptionC);
            tvOptions[3] = itemView.findViewById(R.id.tvOptionD);
        }

        public void bind(Question question, int position) {
            tvQuestionNumber.setText(String.valueOf(position + 1));
            tvQuestionText.setText(question.getQuestionText());

            // Set Option Texts
            tvOptions[0].setText(question.getOptionA());
            tvOptions[1].setText(question.getOptionB());
            tvOptions[2].setText(question.getOptionC());
            tvOptions[3].setText(question.getOptionD());

            // Reset Styles & Icons
            for (TextView tv : tvOptions) {
                tv.setBackgroundResource(R.drawable.bg_option_review_default);
                tv.setTextColor(Color.parseColor("#555555"));
                tv.setTypeface(null, android.graphics.Typeface.NORMAL);
                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0); // Clear icons
            }

            int userAnswer = question.getUserAnswerIndex();
            int correctAnswer = question.getCorrectAnswerIndex();

            // Always highlight Correct Answer in Green
            tvOptions[correctAnswer].setBackgroundResource(R.drawable.bg_option_review_correct);
            tvOptions[correctAnswer].setTextColor(Color.parseColor("#2E7D32")); // Dark Green
            tvOptions[correctAnswer].setTypeface(null, android.graphics.Typeface.BOLD);
            tvOptions[correctAnswer].setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_circle, 0);
            tvOptions[correctAnswer].setCompoundDrawableTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#2E7D32")));


            // If User Answered Wrong (and not skipped)
            if (userAnswer != -1 && userAnswer != correctAnswer) {
                tvOptions[userAnswer].setBackgroundResource(R.drawable.bg_option_review_wrong);
                tvOptions[userAnswer].setTextColor(Color.parseColor("#C62828")); // Red
                tvOptions[userAnswer].setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close, 0);
                tvOptions[userAnswer].setCompoundDrawableTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#C62828")));
            }
            
            // Explanation
            if (question.getExplanation() != null && !question.getExplanation().isEmpty()) {
                layoutExplanation.setVisibility(View.VISIBLE);
                tvExplanation.setText(question.getExplanation());
            } else {
                // If standard quiz data doesn't have explanation, maybe hide or show default
                // Per design, usually invisible if empty
                 layoutExplanation.setVisibility(View.GONE);
            }
        }
    }
}
