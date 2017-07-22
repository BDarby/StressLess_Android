package com.fullsail.b_nicole.stressless_android20;

import android.content.Context;

import java.util.ArrayList;



public class AudioLibrary {

    ArrayList<MediaObject> audioObjects = new ArrayList<>();

    public AudioLibrary() {


    }

    public AudioLibrary(Context context){

        String packageName = context.getPackageName();
        String resourcesName = "android.resource://";




    }

    public void setAudioObjects(ArrayList<MediaObject> audioOnjects) {
        this.audioObjects = audioOnjects;
    }
}
