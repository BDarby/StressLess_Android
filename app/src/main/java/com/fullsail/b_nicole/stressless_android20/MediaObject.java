package com.fullsail.b_nicole.stressless_android20;

import java.io.Serializable;

/**
 * Created by b_nicole on 7/20/17.
 */

public class MediaObject implements Serializable {

    private String sourceName;
    private int mediaResource;
    private int imageResource;

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
