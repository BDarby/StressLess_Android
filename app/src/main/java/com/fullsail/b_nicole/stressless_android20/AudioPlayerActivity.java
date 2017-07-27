package com.fullsail.b_nicole.stressless_android20;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AudioPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AudioPlayerActivity";

    MediaObject mediaObject;
    private AudioService audioService;
    Intent audioIntent;

    ImageButton stopButton;
    ImageButton playButton;
    ImageButton pauseButton;

    TextView songTitle;
    ImageView albumCover;

    boolean isAutoPlayOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        if(getIntent().hasExtra("MEDIA_OBJECT")){
            mediaObject = (MediaObject) getIntent().getSerializableExtra("MEDIA_OBJECT");
        }

        if (getIntent().hasExtra("AUTO_PLAY")){
            isAutoPlayOn = getIntent().getBooleanExtra("AUTO_PLAY", false);
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
            AudioService.AudioServiceBinder binder = (AudioService.AudioServiceBinder) iBinder;
            audioService = binder.getService();
            if (isAutoPlayOn){
                AudioPlayerActivity.this.play();
            }
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference location = databaseReference.child("users").child(uid).child("favorites").child("audio");

        location.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    if (map.containsKey(mediaObject.getSourceName())) {
                        menu.getItem(0).setIcon(R.drawable.ic_favorite_24dp);
                    } else {
                        menu.getItem(0).setIcon(R.drawable.ic_favorite_border_24dp);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                menu.getItem(0).setIcon(R.drawable.ic_favorite_border_24dp);
            }
        });

        getMenuInflater().inflate(R.menu.audio_player_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        Map <String, Object> values = new HashMap<>();
        values.put(mediaObject.getSourceName(),mediaObject.getSourceName());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference location = databaseReference.child("users").child(uid).child("favorites").child("audio");

        location.updateChildren(values, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null){
                    item.setIcon(R.drawable.ic_favorite_24dp);
                }
            }
        });

        return super.onOptionsItemSelected(item);
    }
}
