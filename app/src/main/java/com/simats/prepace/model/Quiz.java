package com.simats.prepace.model;

public class Quiz {
    private String name;
    private String difficulty;
    private String description;
    private int questions;
    private int duration;
    private String category;

    public Quiz() {} // Empty constructor

    public Quiz(String name, String difficulty, String description, int questions, int duration, String category) {
        this.name = name;
        this.difficulty = difficulty;
        this.description = description;
        this.questions = questions;
        this.duration = duration;
        this.category = category;
    }

    public String getName() { return name; }
    public String getDifficulty() { return difficulty; }
    public String getDescription() { return description; }
    public int getQuestions() { return questions; }
    public int getDuration() { return duration; }
    public String getCategory() { return category; }
}
