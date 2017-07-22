package com.fullsail.b_nicole.stressless_android20;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by b_nicole on 7/21/17.
 */

public class AudioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "AudioService";

    MediaObject mediaObject;

    private MediaPlayer mediaPlayer;
    private States states = new States();

    public class AudioServiceBinder extends Binder {
        public AudioService getService(){
            return AudioService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TAG", "onStartCommand: ");
        mediaObject = (MediaObject) intent.getSerializableExtra("MEDIA_OBJECT");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

        states.setCurrentState(States.STATE_IDLE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.e(TAG, "onBind: ");
        mediaObject = (MediaObject) intent.getSerializableExtra("MEDIA_OBJECT");
        return new AudioServiceBinder();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        states.setCurrentState(States.STATE_PREPARED);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        states.setCurrentState(States.STATE_START);

    }

    public void play(){
        boolean isPaused = states.getCurrentState()  == States.STATE_PAUSED;
        boolean isCOMPLETED = states.getCurrentState() == States.STATE_PLAYBACK_COMPLETED;

        if (isPaused){
            mediaPlayer.start();
            states.setCurrentState(States.STATE_START);
        }else{
            mediaPlayer.reset();
            states.setCurrentState(States.STATE_IDLE);
            try {
                String packageName = getPackageName();
                String resources = "android.resource://";

                Log.e(TAG, "play: " + resources + packageName + "/" + mediaObject.getMediaResource() );
                Uri uri = Uri.parse(resources + packageName + "/" + mediaObject.getMediaResource());
                mediaPlayer.setDataSource(this, uri);
                states.setCurrentState(States.STATE_INTIALIZED);
            } catch (Exception e) { e.printStackTrace(); }

            mediaPlayer.prepareAsync();
            states.setCurrentState(States.STATE_PREPARING);
        }
    }

    public void pause() {

        boolean isStarted = states.getCurrentState() == States.STATE_START;

        if (isStarted) {
            mediaPlayer.pause();
            states.setCurrentState(States.STATE_PAUSED);
        }
    }

    public void stop() {
        boolean isPaused = states.getCurrentState() == States.STATE_PAUSED;
        boolean isStarted = states.getCurrentState() == States.STATE_START;
        if (isPaused || isStarted) {
            mediaPlayer.stop();
        }
    }



}
