package com.simats.prepace.model;

public class Notification {
    public enum Type {
        QUIZ_COMPLETED,
        ACHIEVEMENT_UNLOCKED,
        DAILY_CHALLENGE,
        NEW_QUIZZES,
        PERSONAL_BEST
    }

    private String title;
    private String message;
    private String timestamp;
    private Type type;
    private boolean isUnread;

    public Notification(String title, String message, String timestamp, Type type, boolean isUnread) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
        this.isUnread = isUnread;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Type getType() {
        return type;
    }

    public boolean isUnread() {
        return isUnread;
    }
}
