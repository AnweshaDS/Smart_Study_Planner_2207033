package com.example.myapp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class PomodoroTimer {

    public interface Listener {
        void onTick(long remainingSeconds, boolean isStudy);
        void onPhaseSwitch(boolean isStudy);
        void onStop(long studiedSeconds);
    }

    private final int studySeconds;
    private final int breakSeconds;
    private final Listener listener;

    private Timeline timeline;
    private boolean isStudy = true;
    private long remaining;
    private long sessionStartMillis;

    public PomodoroTimer(int studySeconds, int breakSeconds, Listener listener) {
        this.studySeconds = studySeconds;
        this.breakSeconds = breakSeconds;
        this.listener = listener;
    }

    public void start() {
        sessionStartMillis = System.currentTimeMillis();
        remaining = isStudy ? studySeconds : breakSeconds;

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> tick())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void tick() {
        remaining--;
        listener.onTick(remaining, isStudy);

        if (remaining <= 0) {
            saveSession();
            isStudy = !isStudy;
            remaining = isStudy ? studySeconds : breakSeconds;
            sessionStartMillis = System.currentTimeMillis();
            listener.onPhaseSwitch(isStudy);
        }
    }

    private void saveSession() {
        long now = System.currentTimeMillis();
        long deltaSeconds = (now - sessionStartMillis) / 1000;
        if (deltaSeconds > 0) {
            listener.onStop(deltaSeconds);
        }
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();
            saveSession();
        }
    }
}
