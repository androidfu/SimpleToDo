package com.chscodecamp.android.simpletodo;

class Task {
    private String title;
    private boolean completed;

    Task(String title) {
        this.title = title;
    }

    String getTitle() {
        return title;
    }

    boolean isCompleted() {
        return completed;
    }

    void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
