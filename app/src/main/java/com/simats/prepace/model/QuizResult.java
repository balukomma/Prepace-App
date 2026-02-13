package com.simats.prepace.model;

import java.io.Serializable;

public class QuizResult implements Serializable {
    private String category;
    private String quizTitle;
    private int score;
    private int totalQuestions;
    private long timestamp;



    public String getCategory() {
        return category;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public java.util.List<Question> getQuestions() {
        return questions;
    }

    // Constructor with questions list
    private java.util.List<Question> questions;

    public QuizResult(String category, String quizTitle, int score, int totalQuestions, long timestamp, java.util.List<Question> questions) {
        this.category = category;
        this.quizTitle = quizTitle;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.timestamp = timestamp;
        this.questions = questions;
    }

    // Legacy constructor for backward compatibility
    public QuizResult(String category, String quizTitle, int score, int totalQuestions, long timestamp) {
        this(category, quizTitle, score, totalQuestions, timestamp, new java.util.ArrayList<>());
    }
}
