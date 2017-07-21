package com.fullsail.b_nicole.stressless_android20;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

public class Main5Activity extends AppCompatActivity {

    ImageButton stopButton;
    ImageButton playButton;
    ImageButton pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        stopButton = (ImageButton) findViewById(R.id.audio_player_stop);
        playButton = (ImageButton) findViewById(R.id.audio_player_play);
        pauseButton = (ImageButton) findViewById(R.id.audio_player_pause);
    }

    public void stop() {

    }

    public void play() {

    }

    public void pause() {

    }

}
