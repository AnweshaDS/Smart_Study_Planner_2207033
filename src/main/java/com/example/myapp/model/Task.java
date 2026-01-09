package com.example.myapp.model;

public class Task {

    private int id;
    private String title;
    private int status;

    // planned times (seconds)
    private int targetSeconds;
    private int studySeconds;
    private int breakSeconds;

    // runtime / progress
    private int spentSeconds;
    private long lastStartTime; // millis
    private int todaySpentSeconds;
    private String lastStudyDate;

    //constructor

    public Task(int id, String title, int status,
                int targetSeconds, int studySeconds, int breakSeconds,
                int spentSeconds, long lastStartTime,
                int todaySpentSeconds, String lastStudyDate) {

        this.id = id;
        this.title = title;
        this.status = status;
        this.targetSeconds = targetSeconds;
        this.studySeconds = studySeconds;
        this.breakSeconds = breakSeconds;
        this.spentSeconds = spentSeconds;
        this.lastStartTime = lastStartTime;
        this.todaySpentSeconds = todaySpentSeconds;
        this.lastStudyDate = lastStudyDate;
    }

    // getters

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getStatus() { return status; }

    public int getTargetSeconds() { return targetSeconds; }
    public int getStudySeconds() { return studySeconds; }
    public int getBreakSeconds() { return breakSeconds; }

    public int getSpentSeconds() { return spentSeconds; }
    public long getLastStartTime() { return lastStartTime; }

    public int getTodaySpentSeconds() { return todaySpentSeconds; }
    public String getLastStudyDate() { return lastStudyDate; }

    //setters

    public void setStatus(int status) { this.status = status; }

    public void setSpentSeconds(int spentSeconds) {
        this.spentSeconds = spentSeconds;
    }

    public void setLastStartTime(long lastStartTime) {
        this.lastStartTime = lastStartTime;
    }

    public void setTodaySpentSeconds(int sec) {
        this.todaySpentSeconds = sec;
    }

    public void setLastStudyDate(String date) {
        this.lastStudyDate = date;
    }

    //helpers

    public static String format(int sec) {
        int h = sec / 3600;
        int m = (sec % 3600) / 60;
        int s = sec % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    @Override
    public String toString() {
        return title + " (" + format(spentSeconds) + ")";
    }
}
