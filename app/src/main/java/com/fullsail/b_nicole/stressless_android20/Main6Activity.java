package com.fullsail.b_nicole.stressless_android20;


import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Main6Activity extends AppCompatActivity {

    TextView videoTitle;
    VideoView videoView;
    ImageButton stopButton;
    ImageButton playButton;
    ImageButton pauseButton;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

//        videoTitle = (TextView) findViewById(R.id.video_player_text);
        videoView = (VideoView) findViewById(R.id.video_player);
//        stopButton = (ImageButton) findViewById(R.id.video_player_stop);
//        playButton = (ImageButton) findViewById(R.id.video_player_play);
//        pauseButton = (ImageButton) findViewById(R.id.video_player_pause);


        InputStream ins = Main6Activity.this.getResources().openRawResource (R.raw.circles);
        File tmpFile = null;
        OutputStream output;

        try {
            tmpFile = File.createTempFile("video","mp4");
            output = new FileOutputStream(tmpFile);

            final byte[] buffer = new byte[102400];
            int read;

            while ((read = ins.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }

            output.flush();
            output.close();
            ins.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaController = new MediaController(this) {
            @Override
            public void hide() {
                this.show();
            }
        };

        videoView.setVideoPath(tmpFile.getPath());
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mediaController.show();
                videoView.start();
            }
        });

    }
}
