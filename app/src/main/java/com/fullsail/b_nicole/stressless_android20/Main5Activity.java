package com.fullsail.b_nicole.stressless_android20;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Main5Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Main5Activity";

    MediaObject mediaObject;
    private AudioService audioService;
    Intent audioIntent;

    ImageButton stopButton;
    ImageButton playButton;
    ImageButton pauseButton;

    TextView songTitle;
    ImageView albumCover;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        if(getIntent().hasExtra("MEDIA_OBJECT")){
            mediaObject = (MediaObject) getIntent().getSerializableExtra("MEDIA_OBJECT");
        }

        stopButton = (ImageButton) findViewById(R.id.audio_player_stop);
        playButton = (ImageButton) findViewById(R.id.audio_player_play);
        pauseButton = (ImageButton) findViewById(R.id.audio_player_pause);

        stopButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);

        songTitle = (TextView) findViewById(R.id.audio_player_text);
        albumCover = (ImageView) findViewById(R.id.audio_player_album_cover);

        if(mediaObject != null ){
            songTitle.setText(mediaObject.getSourceName());
            albumCover.setImageResource(mediaObject.getImageResource());
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.e(TAG, "onResume: " );
        audioIntent = new Intent(this, AudioService.class);
        audioIntent.putExtra("MEDIA_OBJECT",mediaObject);
        bindService(audioIntent, serviceConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(serviceConnection);
    }
    
    public void stop() {
        audioService.stop();
    }

    public void play() {
        audioService.play();
    }

    public void pause() {
        audioService.pause();
    }


    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected() called with: componentName = [" + componentName + "], iBinder = [" + iBinder + "]");
            AudioService.AudioServiceBinder binder = (AudioService.AudioServiceBinder) iBinder;
            audioService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            audioService = null;
        }
    };

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.audio_player_play:
                play();
                break;
            case R.id.audio_player_pause:
                pause();
                break;
            case R.id.audio_player_stop:
                stop();
                break;
        }
    }
}
