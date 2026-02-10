package com.simats.prepace.model;

public class Question implements java.io.Serializable {
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int correctAnswerIndex; // 0 for A, 1 for B, 2 for C, 3 for D
    
    private int userAnswerIndex = -1; // -1 means no answer selected
    private String explanation = "Explanation for this answer will be available soon."; // Placeholder

    public Question() {} // Empty constructor for JSON parsing

    public Question(String questionText, String optionA, String optionB, String optionC, String optionD, int correctAnswerIndex) {
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswerIndex = correctAnswerIndex;
    }
    
    public Question(String questionText, String optionA, String optionB, String optionC, String optionD, int correctAnswerIndex, String explanation) {
        this(questionText, optionA, optionB, optionC, optionD, correctAnswerIndex);
        this.explanation = explanation;
    }

    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    
    public int getUserAnswerIndex() { return userAnswerIndex; }
    public void setUserAnswerIndex(int userAnswerIndex) { this.userAnswerIndex = userAnswerIndex; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
