package com.fullsail.b_nicole.stressless_android20;


import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class VideoPlayerActivity extends AppCompatActivity {

    private static final String TAG = "VideoPlayerActivity";

    VideoView videoView;
    MediaObject mediaObject;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        if(getIntent().hasExtra("MEDIA_OBJECT")){
            mediaObject = (MediaObject) getIntent().getSerializableExtra("MEDIA_OBJECT");
        }

        videoView = (VideoView) findViewById(R.id.video_player);
        InputStream ins = VideoPlayerActivity.this.getResources().openRawResource (mediaObject.getMediaResource());
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

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {

                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                    VideoPlayerActivity videoPlayerActivity = (VideoPlayerActivity) getContext();
                    videoPlayerActivity.finish();
                }

                return super.dispatchKeyEvent(event);
            }
        };

        videoView.setVideoPath(tmpFile.getPath());
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                //mediaController.show();
                videoView.start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference location = databaseReference.child("users").child(uid).child("favorites").child("video");
        
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
        
        getMenuInflater().inflate(R.menu.video_player_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        Map<String, Object> values = new HashMap<>();
        values.put(mediaObject.getSourceName(),mediaObject.getSourceName());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference location = databaseReference.child("users").child(uid).child("favorites").child("video");

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
