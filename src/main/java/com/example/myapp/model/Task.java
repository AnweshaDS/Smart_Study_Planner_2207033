package com.example.myapp.model;

public class Task {
    private int id;
    private String title;
    private String description;
    private String status; // TODO, RUNNING, PAUSED, COMPLETED
    private int plannedTime; // minutes
    private int spentTime;   // minutes

    public Task() {}

    public Task(int id, String title, String description, String status, int plannedTime, int spentTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.plannedTime = plannedTime;
        this.spentTime = spentTime;
    }

    // Getters / setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getPlannedTime() { return plannedTime; }
    public void setPlannedTime(int plannedTime) { this.plannedTime = plannedTime; }
    public int getSpentTime() { return spentTime; }
    public void setSpentTime(int spentTime) { this.spentTime = spentTime; }

    @Override
    public String toString() {
        return title + " (" + status + ")";
    }
}
