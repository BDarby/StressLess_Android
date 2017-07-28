//Brittany Darby
//Android Deployment - C201707
//MediaObject

package com.fullsail.b_nicole.stressless_android20;

import java.io.Serializable;



class MediaObject implements Serializable {

    private final String sourceName;
    private final int mediaResource;
    private final int imageResource;

    public MediaObject(String sourceName, int mediaResource, int imageResource) {
        this.sourceName = sourceName;
        this.mediaResource = mediaResource;
        this.imageResource = imageResource;
    }

    public String getSourceName() {
        return sourceName;
    }

    public int getMediaResource() {
        return mediaResource;
    }

    public int getImageResource() {
        return imageResource;
    }
}
