package com.fullsail.b_nicole.stressless_android20;

/**
 * Created by b_nicole on 7/21/17.
 */

public class States {

    private int currentState;

    public static final int STATE_IDLE = 0;
    public static final int STATE_INTIALIZED = 1;
    public static final int STATE_PREPARING = 2;
    public static final int STATE_PREPARED = 3;
    public static final int STATE_START = 4;
    public static final int STATE_PAUSED = 5;
    public static final int STATE_STOPPED = 6;
    public static final int STATE_PLAYBACK_COMPLETED = 7;

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

}
